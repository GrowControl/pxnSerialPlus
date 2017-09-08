/* PoiXson SerialPlus
 * copyright 2017
 * license GPL-3
 * lorenzo at poixson.com
 * http://poixson.com/
 */

#include <jni.h>

#include "SerialPlus.h"

#include <stdio.h>
#include <stdlib.h>
// # include <linux/serial.h>
#include <termios.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <sys/select.h>
#include <errno.h>

#ifdef __SunOS
#include <sys/filio.h> // for FIONREAD in Solaris
#include <string.h>    // for select() function
#endif

// # ifdef __APPLE__
// # include <serial/ioss.h> // for IOSSIOSPEED in Mac (Non standard baudrate)
// # endif



#define NativeSerial_ERR_UNKNOWN_FAIL          -1LL
#define NativeSerial_ERR_PORT_NOT_FOUND        -2LL
#define NativeSerial_ERR_PORT_BUSY             -3LL
#define NativeSerial_ERR_PERMISSION_DENIED     -4LL
#define NativeSerial_ERR_INCORRECT_SERIAL_PORT -5LL

#define PARAMS_FLAG_IGNPAR 1
#define PARAMS_FLAG_PARMRK 2



// implimated in java for linux
//JNIEXPORT jobjectArray JNICALL
//Java_com_poixson_serialplus_natives_NativeSerial_ListPorts
//(JNIEnv *env, jobject obj) {
//return NULL;
//}



// natOpenPort()
JNIEXPORT jlong JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natOpenPort
(JNIEnv *env, jobject obj, jstring portName) {
	const char *port = (*env)->GetStringUTFChars(env, portName, 0);
	fprintf(stderr, "Opening native serial port: %s\n", port);
	// | O_NDELAY
	jlong handle = open(port, O_RDWR | O_NOCTTY);
	if (handle == -1) {
		// port not found
		if (errno == ENOENT) {
			fprintf(stderr, "Port not found: %s\n", port);
			handle = NativeSerial_ERR_PORT_NOT_FOUND; // -2
		} else
		// port busy
		if (errno == EBUSY) {
			fprintf(stderr, "Port is busy: %s\n", port);
			handle = NativeSerial_ERR_PORT_BUSY; // -3
		} else
		// permission denied
		if (errno == EACCES) {
			fprintf(stderr, "Permission denied to port: %s\n", port);
			handle = NativeSerial_ERR_PERMISSION_DENIED; // -4
		// unknown fail
		} else {
			fprintf(stderr, "Failed to open port: %s\n", port);
			handle = NativeSerial_ERR_UNKNOWN_FAIL; // -1
		}
		(*env)->ReleaseStringUTFChars(env, portName, port);
		return handle;
	}
	// exclusive port lock
	ioctl(handle, TIOCEXCL);
	// set blocking
	fcntl(handle, F_SETFL, 0);
	(*env)->ReleaseStringUTFChars(env, portName, port);
	return handle;
}



// natClosePort()
JNIEXPORT jboolean JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natClosePort
(jlong handle) {
//	fprintf(stderr, "Closing native serial port: %s\n", port);
	if (handle <= 0) {
		return JNI_FALSE;
	}
	// clear exclusive port lock
	ioctl(handle, TIOCNXCL);
	return
		close(handle) == 0
		? JNI_TRUE
		: JNI_FALSE;
}



// natSerParams()
JNIEXPORT jlong JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natSetParams
(JNIEnv *env, jobject obj,
jlong handle, jint baud,
jint byteSize, jint stopBits, jint parity,
jboolean setRTS, jboolean setDTR, jint flags) {
	if (handle <= 0) {
		return handle;
	}
	speed_t baudValue = GetBaudByNumber(baud);
	int byteSizeValue = GetByteSizeByNumber(byteSize);

	// get current options
	struct termios settings;
	if (tcgetattr(handle, &settings) != 0) {
		fprintf(stderr, "Failed to get port attributes\n");
		close(handle);
		handle = NativeSerial_ERR_INCORRECT_SERIAL_PORT; // -5
		return handle;
	}
	settings.c_cflag |= (CLOCAL | CREAD);
	settings.c_cflag &= ~CRTSCTS;
	settings.c_lflag &= ~(ICANON | ECHO | ECHOE | ECHOK | ECHONL | ECHOCTL | ECHOPRT | ECHOKE | ISIG | IEXTEN);
	settings.c_iflag &= ~(IXON | IXOFF | IXANY | INPCK | IGNPAR | PARMRK | ISTRIP | IGNBRK | BRKINT | INLCR | IGNCR| ICRNL);
#ifdef IUCLC
	settings.c_iflag &= ~IUCLC;
#endif
	settings.c_oflag &= ~OPOST;
	if ( (flags & PARAMS_FLAG_IGNPAR) == PARAMS_FLAG_IGNPAR ) {
		settings.c_iflag |= IGNPAR;
	}
	if ( (flags & PARAMS_FLAG_PARMRK) == PARAMS_FLAG_PARMRK ) {
		settings.c_iflag |= PARMRK;
	}
	settings.c_cc[VMIN]  = 0;  // Minimum number of characters to read
	settings.c_cc[VTIME] = 10; // Time to wait for data (tenths of seconds)

	// set baud rate
	if (baudValue != -1) {
		if (cfsetispeed(&settings, baudValue) < 0 ||
		cfsetospeed(&settings, baudValue) < 0) {
			fprintf(stderr, "Failed to set baud rate\n");
			close(handle);
			handle = NativeSerial_ERR_INCORRECT_SERIAL_PORT; // -5
			return handle;
		}
	}

	// set data bits
	if (byteSizeValue != -1) {
		settings.c_cflag &= ~CSIZE;
		settings.c_cflag |= byteSizeValue;
	}

	// set stop bits
	if (stopBits == 0) { // 1 bit
		settings.c_cflag &= ~CSTOPB;
	} else
	// 1 = 1.5 bits ; 2 = 2 bits
	if ( (stopBits == 1) || (stopBits == 2) ) {
		settings.c_cflag |= CSTOPB;
	}

	// clear parity
#ifdef PAREXT
	settings.c_cflag &= ~(PARENB | PARODD | PAREXT);
#elif defined CMSPAR
	settings.c_cflag &= ~(PARENB | PARODD | CMSPAR);
#else
	settings.c_cflag &= ~(PARENB | PARODD);
#endif
	// odd parity
	if (parity == 1) {
		settings.c_cflag |= (PARENB | PARODD);
		settings.c_iflag |= INPCK;
	} else
	// even parity
	if (parity == 2) {
		settings.c_cflag |= PARENB;
		settings.c_iflag |= INPCK;
	} else
	// mark parity
	if (parity == 3) {
#ifdef PAREXT
		settings.c_cflag |= (PARENB | PARODD | PAREXT);
		settings.c_iflag |= INPCK;
#elif defined CMSPAR
		settings.c_cflag |= (PARENB | PARODD | CMSPAR);
		settings.c_iflag |= INPCK;
#endif
	} else
	// space parity
	if (parity == 4) {
#ifdef PAREXT
		settings.c_cflag |= (PARENB | PAREXT);
		settings.c_iflag |= INPCK;
#elif defined CMSPAR
		settings.c_cflag |= (PARENB | CMSPAR);
		settings.c_iflag |= INPCK;
#endif
	}

	// set the settings
	if (tcsetattr(handle, TCSAFLUSH, &settings) != 0) {
		fprintf(stderr, "Failed to set port attributes\n");
		close(handle);
		handle = NativeSerial_ERR_INCORRECT_SERIAL_PORT; // -5
		return handle;
	}
	// flush settings
	tcflush(handle, TCIOFLUSH);

	// line status
	int lineStatus;
	jboolean result = JNI_FALSE;
	if (ioctl(handle, TIOCMGET, &lineStatus) >= 0) {
		// RTS (request to send)
		if (setRTS == JNI_TRUE) {
			lineStatus |= TIOCM_RTS;
		} else {
			lineStatus &= ~TIOCM_RTS;
		}
		// DTR (data terminal ready)
		if (setDTR == JNI_TRUE) {
			lineStatus |= TIOCM_DTR;
		} else {
			lineStatus &= ~TIOCM_DTR;
		}
		if (ioctl(handle, TIOCMSET, &lineStatus) >= 0) {
			result = JNI_TRUE;
		}
	}

	return result;
}



// natGetInputBytesCount()
JNIEXPORT jlong JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natGetInputBytesCount
(JNIEnv *env, jobject obj, jlong handle) {
	if (handle <= 0) {
		return handle;
	}
	jint result = -1;
	ioctl(handle, FIONREAD, &result);
	return result;
}
// natGetOutputBytesCount()
JNIEXPORT jlong JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natGetOutputBytesCount
(JNIEnv *env, jobject obj, jlong handle) {
	if (handle <= 0) {
		return handle;
	}
	jint result = -1;
	ioctl(handle, TIOCOUTQ, &result);
	return result;
}



// natReadBytes()
JNIEXPORT jbyteArray JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natReadBytes
(JNIEnv *env, jobject obj, jlong handle, jint size) {
	fd_set read_fd_set;
	jbyte *buffer;
//	jbyte *buffer = (*env)->NewByte(env);
//	jbyte *buffer = new jbyte[size];
	int remaining = size;
fprintf(stderr, "READING FROM: %d\n", handle);
	while (remaining > 0) {
		FD_ZERO(&read_fd_set);
		FD_SET(handle, &read_fd_set);
		select(
			handle + 1,
			&read_fd_set,
			NULL,
			NULL,
			NULL
		);
		int result = read(
			handle,
			buffer + (size - remaining),
			remaining
		);
		if (result > 0) {
			remaining -= result;
		}
	}
	FD_CLR(handle, &read_fd_set);
	jbyteArray array = (*env)->NewByteArray(env, size);
	(*env)->SetByteArrayRegion(
		env,
		array,
		0,
		size,
		buffer
	);
	free(buffer);
	return array;
}



// natWriteBytes()
JNIEXPORT jlong JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natWriteBytes
(JNIEnv *env, jobject obj, jlong handle, jbyteArray bytes) {
	jbyte* buffer = (*env)->GetByteArrayElements(env, bytes, JNI_FALSE);
	jint size = (*env)->GetArrayLength(env, bytes);
fprintf(stderr, "WRITING: %s\n", buffer);
	jint result =
		write(
			handle,
			buffer,
			(size_t)size
		);
	(*env)->ReleaseByteArrayElements(
		env,
		bytes,
		buffer,
		0
	);
	return
		result == size
		? JNI_TRUE
		: JNI_FALSE;
}
