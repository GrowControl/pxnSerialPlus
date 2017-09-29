/* PoiXson SerialPlus 1.x
 * copyright 2017
 * license GPL-3
 * lorenzo at poixson.com
 * http://poixson.com/
 */

#include <jni.h>

#include "SerialPlus.h"

#include <stdio.h>
#include <stdlib.h>
#include <termios.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <sys/select.h>
#include <errno.h>

#ifdef __SunOS
#include <sys/filio.h> // for FIONREAD in Solaris
// # include <string.h>    // for select() function
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



// natScanDevices()
JNIEXPORT jint JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natScanDevices
(JNIEnv *env, jobject obj) {
	return 0;
}



// natOpenPort(port-name)
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
//TODO: should this be used?
//	// set non-blocking
//	fcntl(handle, F_SETFL, FNDELAY);
	(*env)->ReleaseStringUTFChars(env, portName, port);
	return handle;
}



// natClosePort(handle)
JNIEXPORT jboolean JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natClosePort
(JNIEnv *env, jobject obj, jlong handle) {
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



// natSetParams(handle, baud, byte-size, stop-bits, parity, flags)
JNIEXPORT jlong JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natSetParams
(JNIEnv *env, jobject obj, jlong handle, jint baud,
jint byteSize, jint stopBits, jint parity, jint flags) {
	if (handle <= 0) {
		return handle;
	}
	speed_t baudValue = GetBaudByNumber(baud);
	int byteSizeValue = GetByteSizeByNumber(byteSize);
	// get current options
	struct termios tty;
//TODO: is this needed?
//memset (&tty, 0, sizeof tty);
	if (tcgetattr(handle, &tty) != 0) {
		fprintf(stderr, "Failed to get port attributes\n");
		close(handle);
		handle = NativeSerial_ERR_INCORRECT_SERIAL_PORT; // -5
		return handle;
	}
	tty.c_cflag |= (CLOCAL | CREAD);
	tty.c_cflag &= ~CRTSCTS;
	tty.c_lflag &= ~(ICANON | ECHO | ECHOE | ECHOK | ECHONL | ECHOCTL | ECHOPRT | ECHOKE | ISIG | IEXTEN);
	tty.c_iflag &= ~(IXON | IXOFF | IXANY | INPCK | IGNPAR | PARMRK | ISTRIP | IGNBRK | BRKINT | INLCR | IGNCR| ICRNL);
#ifdef IUCLC
	tty.c_iflag &= ~IUCLC;
#endif
	tty.c_oflag &= ~OPOST;
	if ( (flags & PARAMS_FLAG_IGNPAR) == PARAMS_FLAG_IGNPAR ) {
		tty.c_iflag |= IGNPAR;
	}
	if ( (flags & PARAMS_FLAG_PARMRK) == PARAMS_FLAG_PARMRK ) {
		tty.c_iflag |= PARMRK;
	}

	// set baud rate
	if (baudValue != -1) {
		if (cfsetispeed(&tty, baudValue) < 0 ||
		cfsetospeed(&tty, baudValue) < 0) {
			fprintf(stderr, "Failed to set baud rate\n");
			close(handle);
			handle = NativeSerial_ERR_INCORRECT_SERIAL_PORT; // -5
			return handle;
		}
	}

	// set data bits
	if (byteSizeValue != -1) {
		tty.c_cflag &= ~CSIZE;
		tty.c_cflag |= byteSizeValue;
	}

	// set stop bits
	if (stopBits == 0) { // 1 bit
		tty.c_cflag &= ~CSTOPB;
	} else
	// 1 = 1.5 bits ; 2 = 2 bits
	if ( (stopBits == 1) || (stopBits == 2) ) {
		tty.c_cflag |= CSTOPB;
	}

	// clear parity
#ifdef PAREXT
	tty.c_cflag &= ~(PARENB | PARODD | PAREXT);
#elif defined CMSPAR
	tty.c_cflag &= ~(PARENB | PARODD | CMSPAR);
#else
	tty.c_cflag &= ~(PARENB | PARODD);
#endif
	// odd parity
	if (parity == 1) {
		tty.c_cflag |= (PARENB | PARODD);
		tty.c_iflag |= INPCK;
	} else
	// even parity
	if (parity == 2) {
		tty.c_cflag |= PARENB;
		tty.c_iflag |= INPCK;
	} else
	// mark parity
	if (parity == 3) {
#ifdef PAREXT
		tty.c_cflag |= (PARENB | PARODD | PAREXT);
		tty.c_iflag |= INPCK;
#elif defined CMSPAR
		tty.c_cflag |= (PARENB | PARODD | CMSPAR);
		tty.c_iflag |= INPCK;
#endif
	} else
	// space parity
	if (parity == 4) {
#ifdef PAREXT
		tty.c_cflag |= (PARENB | PAREXT);
		tty.c_iflag |= INPCK;
#elif defined CMSPAR
		tty.c_cflag |= (PARENB | CMSPAR);
		tty.c_iflag |= INPCK;
#endif
	}

	// set the settings
	if (tcsetattr(handle, TCSAFLUSH, &tty) != 0) {
		fprintf(stderr, "Failed to set port attributes\n");
		close(handle);
		handle = NativeSerial_ERR_INCORRECT_SERIAL_PORT; // -5
		return handle;
	}
	tcflush(handle, TCIOFLUSH);
	return handle;
}



// natSetBlocking(handle, blocking) - blocking/non-blocking
JNIEXPORT jlong JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natSetBlocking
(JNIEnv *env, jobject obj, jlong handle, jboolean blocking) {
	if (handle <= 0) {
		return handle;
	}
	// get current options
	struct termios tty;
	if (tcgetattr(handle, &tty) != 0) {
		fprintf(stderr, "Failed to get port attributes\n");
		close(handle);
		handle = NativeSerial_ERR_INCORRECT_SERIAL_PORT; // -5
		return handle;
	}

	// set blocking/timeout
	tty.c_cc[VMIN]  = (blocking == JNI_FALSE ? 0 : 1); // Minimum number of characters to read
	tty.c_cc[VTIME] = (blocking == JNI_FALSE ? 0 : 1); // Time to wait for data (tenths of seconds)

	// set the settings
	if (tcsetattr(handle, TCSAFLUSH, &tty) != 0) {
		fprintf(stderr, "Failed to set port attributes\n");
		close(handle);
		handle = NativeSerial_ERR_INCORRECT_SERIAL_PORT; // -5
		return handle;
	}
	tcflush(handle, TCIOFLUSH);
	return handle;
}



// natSetVMinVTime(handle, vMin, vTime)
JNIEXPORT jlong JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natSetVMinVTime
(JNIEnv *env, jobject obj, jlong handle, jint vMin, jint vTime) {
	if (handle <= 0) {
		return handle;
	}
	// get current options
	struct termios tty;
	if (tcgetattr(handle, &tty) != 0) {
		fprintf(stderr, "Failed to get port attributes\n");
		close(handle);
		handle = NativeSerial_ERR_INCORRECT_SERIAL_PORT; // -5
		return handle;
	}

	// Minimum number of characters to read
	if (vMin  > 0) {
		tty.c_cc[VMIN] = vMin;
	}
	// Time to wait for data (tenths of seconds)
	if (vTime > 0) {
		tty.c_cc[VTIME] = vTime;
	}

	// set the settings
	if (tcsetattr(handle, TCSAFLUSH, &tty) != 0) {
		fprintf(stderr, "Failed to set port attributes\n");
		close(handle);
		handle = NativeSerial_ERR_INCORRECT_SERIAL_PORT; // -5
		return handle;
	}
	tcflush(handle, TCIOFLUSH);
	return handle;
}



// natSetLineStatus(handle, rts, dtr)
JNIEXPORT jlong JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natSetLineStatus
(JNIEnv *env, jobject obj,
jlong handle, jboolean setRTS, jboolean setDTR) {
	if (handle <= 0) {
		return handle;
	}
	// get current options
	struct termios tty;
	if (tcgetattr(handle, &tty) != 0) {
		fprintf(stderr, "Failed to get port attributes\n");
		close(handle);
		handle = NativeSerial_ERR_INCORRECT_SERIAL_PORT; // -5
		return handle;
	}

	int lineStatus;
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
			return JNI_TRUE;
		}
	}

	// set the settings
	if (tcsetattr(handle, TCSAFLUSH, &tty) != 0) {
		fprintf(stderr, "Failed to set port attributes\n");
		close(handle);
		handle = NativeSerial_ERR_INCORRECT_SERIAL_PORT; // -5
		return handle;
	}
	tcflush(handle, TCIOFLUSH);
	return JNI_FALSE;
}



// natGetInputBytesCount(handle)
JNIEXPORT jint JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natGetInputBytesCount
(JNIEnv *env, jobject obj, jlong handle) {
	if (handle <= 0) {
		return handle;
	}
	jint result = -1;
	ioctl(handle, FIONREAD, &result);
	return result;
}
// natGetOutputBytesCount(handle)
JNIEXPORT jint JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natGetOutputBytesCount
(JNIEnv *env, jobject obj, jlong handle) {
	if (handle <= 0) {
		return handle;
	}
	jint result = -1;
	ioctl(handle, TIOCOUTQ, &result);
	return result;
}



// natReadBytes(handle, bytes, length)
JNIEXPORT jint JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natReadBytes
(JNIEnv *env, jobject obj,
jlong handle, jbyteArray bytes, jint length) {
	jbyte *buf = (jbyte*) malloc(length);
	jint result = (jint) read(
		handle,
		buf,
		length
	);
	if (result > 0) {
		(*env)->SetByteArrayRegion(
			env,
			bytes,
			0,
			result,
			buf
		);
	}
	free(buf);
	return result;
}



// natWriteBytes(handle, bytes)
JNIEXPORT jlong JNICALL
Java_com_poixson_serialplus_natives_NativeSerial_natWriteBytes
(JNIEnv *env, jobject obj, jlong handle, jbyteArray bytes) {
	jbyte* buffer = (*env)->GetByteArrayElements(env, bytes, JNI_FALSE);
	jint size = (*env)->GetArrayLength(env, bytes);
//TODO: remove this
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
