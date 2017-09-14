/* PoiXson SerialPlus 1.x
 * copyright 2017
 * license GPL-3
 * lorenzo at poixson.com
 * http://poixson.com/
 */

#include <jni.h>

#include <termios.h>


speed_t GetBaudByNumber(jint baud) {
	if (baud <= 0) {
		return -1;
	}
	if (baud <= 50) {
		return B50;
	}
	if (baud <= 75) {
		return B75;
	}
	if (baud <= 110) {
		return B110;
	}
	if (baud <= 134) {
		return B134;
	}
	if (baud <= 150) {
		return B150;
	}
	if (baud <= 200) {
		return B200;
	}
	if (baud <= 300) {
		return B300;
	}
	if (baud <= 600) {
		return B600;
	}
	if (baud <= 1200) {
		return B1200;
	}
	if (baud <= 1800) {
		return B1800;
	}
	if (baud <= 2400) {
		return B2400;
	}
	if (baud <= 4800) {
		return B4800;
	}
	if (baud <= 9600) {
		return B9600;
	}
	if (baud <= 19200) {
		return B19200;
	}
	if (baud <= 38400) {
		return B38400;
	}
#ifdef B57600
	if (baud <= 57600) {
		return B57600;
	}
#endif
#ifdef B115200
	if (baud <= 115200) {
		return B115200;
	}
#endif
#ifdef B230400
	if (baud <= 230400) {
		return B230400;
	}
#endif
#ifdef B460800
	if (baud <= 460800) {
		return B460800;
	}
#endif
#ifdef B500000
	if (baud <= 500000) {
		return B500000;
	}
#endif
#ifdef B576000
	if (baud <= 576000) {
		return B576000;
	}
#endif
#ifdef B921600
	if (baud <= 921600) {
		return B921600;
	}
#endif
#ifdef B1000000
	if (baud <= 1000000) {
		return B1000000;
	}
#endif
#ifdef B1152000
	if (baud <= 1152000) {
		return B1152000;
	}
#endif
#ifdef B1500000
	if (baud <= 1500000) {
		return B1500000;
	}
#endif
#ifdef B2000000
	if (baud <= 2000000) {
		return B2000000;
	}
#endif
#ifdef B2500000
	if (baud <= 2500000) {
		return B2500000;
	}
#endif
#ifdef B3000000
	if (baud <= 3000000) {
		return B3000000;
	}
#endif
#ifdef B3500000
	if (baud <= 3500000) {
		return B3500000;
	}
#endif
#ifdef B4000000
	if (baud <= 4000000) {
		return B4000000;
	}
#endif
	return -1;
}


int GetByteSizeByNumber(jint byteSize) {
	if (byteSize == 5) {
		return CS5;
	}
	if (byteSize == 6) {
		return CS6;
	}
	if (byteSize == 7) {
		return CS7;
	}
	if (byteSize == 8) {
		return CS8;
	}
	return -1;
}
