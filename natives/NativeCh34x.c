/* PoiXson SerialPlus 1.x
 * copyright 2017
 * license GPL-3
 * lorenzo at poixson.com
 * http://poixson.com/
 */

#include <jni.h>



// natScanDevices()
JNIEXPORT jint JNICALL
Java_com_poixson_serialplus_natives_NativeCh34x_natScanDevices
(JNIEnv *env, jobject obj) {
return 0;
}



// natOpenPort(port-name)
JNIEXPORT jlong JNICALL
Java_com_poixson_serialplus_natives_NativeCh34x_natOpenPort
(JNIEnv *env, jobject obj, jstring portName) {
return 0;
}



// natClosePort(handle)
JNIEXPORT jboolean JNICALL
Java_com_poixson_serialplus_natives_NativeCh34x_natClosePort
(JNIEnv *env, jobject obj, jlong handle) {
return JNI_FALSE;
}



// natSetParams(handle, baud, byte-size, stop-bits, parity, flags)
JNIEXPORT jlong JNICALL
Java_com_poixson_serialplus_natives_NativeCh34x_natSetParams
(JNIEnv *env, jobject obj, jlong handle, jint baud,
jint byteSize, jint stopBits, jint parity, jint flags) {
return 0;
}



// natSetBlocking(handle, blocking) - blocking/non-blocking
JNIEXPORT jlong JNICALL
Java_com_poixson_serialplus_natives_NativeCh34x_natSetBlocking
(JNIEnv *env, jobject obj, jlong handle, jboolean blocking) {
return 0;
}



// natSetVMinVTime(handle, vMin, vTime)
JNIEXPORT jlong JNICALL
Java_com_poixson_serialplus_natives_NativeCh34x_natSetVMinVTime
(JNIEnv *env, jobject obj, jlong handle, jint vMin, jint vTime) {
return 0;
}



// natSetLineStatus(handle, rts, dtr)
JNIEXPORT jlong JNICALL
Java_com_poixson_serialplus_natives_NativeCh34x_natSetLineStatus
(JNIEnv *env, jobject obj,
jlong handle, jboolean setRTS, jboolean setDTR) {
return 0;
}



// natGetInputBytesCount(handle)
JNIEXPORT jint JNICALL
Java_com_poixson_serialplus_natives_NativeCh34x_natGetInputBytesCount
(JNIEnv *env, jobject obj, jlong handle) {
return 0;
}
// natGetOutputBytesCount(handle)
JNIEXPORT jint JNICALL
Java_com_poixson_serialplus_natives_NativeCh34x_natGetOutputBytesCount
(JNIEnv *env, jobject obj, jlong handle) {
return 0;
}



// natReadBytes(handle, bytes, length)
JNIEXPORT jint JNICALL
Java_com_poixson_serialplus_natives_NativeCh34x_natReadBytes
(JNIEnv *env, jobject obj,
jlong handle, jbyteArray bytes, jint length) {
return 0;
}



// natWriteBytes(handle, bytes)
JNIEXPORT jlong JNICALL
Java_com_poixson_serialplus_natives_NativeCh34x_natWriteBytes
(JNIEnv *env, jobject obj, jlong handle, jbyteArray bytes) {
return 0;
}
