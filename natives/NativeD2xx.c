/* PoiXson SerialPlus 1.x
 * copyright 2017
 * license GPL-3
 * lorenzo at poixson.com
 * http://poixson.com/
 */

#include <jni.h>


//#include "ftd2xx.h"


static char* vendor_ids[] = {
	"0403"
};

static char* product_ids[] = {
	"6001"
};



// natScanDevices()
JNIEXPORT jint JNICALL
Java_com_poixson_serialplus_natives_NativeD2xx_natScanDevices
(JNIEnv *env, jobject obj) {
fprintf(stderr, "SCANNING D2XX..\n");
return 0;
}




