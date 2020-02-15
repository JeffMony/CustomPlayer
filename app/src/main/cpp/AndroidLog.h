
#ifndef CUSTOM_ANDROIDLOG_H
#define CUSTOM_ANDROIDLOG_H

#include "android/log.h"

#define LOG_DEBUG true

#define LOGD(FORMAT,...) __android_log_print(ANDROID_LOG_DEBUG,"CUSTOM_PLAYER",FORMAT,##__VA_ARGS__);
#define LOGE(FORMAT,...) __android_log_print(ANDROID_LOG_ERROR,"CUSTOM_PLAYER",FORMAT,##__VA_ARGS__);

#endif //CUSTOM_ANDROIDLOG_H