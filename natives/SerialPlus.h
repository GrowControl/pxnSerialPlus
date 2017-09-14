

#ifndef SERIALPLUS_H
#define SERIALPLUS_H 1

#include "SerialPlus.c"

#endif



#include <jni.h>

#include <termios.h>



speed_t GetBaudByNumber(jint baud);
int GetByteSizeByNumber(jint byteSize);
