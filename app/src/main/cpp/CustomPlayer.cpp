#include <jni.h>
#include <string>
#include "CustomFFmpeg.h"
#include "CustomPlaystatus.h"

extern "C"
{
#include <libavformat/avformat.h>
}


_JavaVM *javaVM = NULL;
CustomCallJava *callJava = NULL;
CustomFFmpeg *fFmpeg = NULL;
CustomPlaystatus *playstatus = NULL;

bool nexit = true;
pthread_t thread_start;

extern "C"
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved)
{
    jint result = -1;
    javaVM = vm;
    JNIEnv *env;
    if(vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK)
    {

        return result;
    }
    return JNI_VERSION_1_4;

}

extern "C" JNIEXPORT void JNICALL
Java_com_android_customplayer_CustomPlayer__1prepare(
        JNIEnv *env,
        jobject /* this */obj,
        jstring url) {
    const char *m_url = env->GetStringUTFChars(url, 0);

    if(fFmpeg == NULL)
    {
        if(callJava == NULL)
        {
            callJava = new CustomCallJava(javaVM, env, &obj);
        }
        callJava->onCallLoad(MAIN_THREAD, true);
        playstatus = new CustomPlaystatus();
        fFmpeg = new CustomFFmpeg(playstatus, callJava, m_url);
        fFmpeg->parpared();
    }
}

void *startCallBack(void *data)
{
    CustomFFmpeg *fFmpeg = (CustomFFmpeg *) data;
    fFmpeg->start();
    return 0;
}

extern "C" JNIEXPORT void JNICALL
Java_com_android_customplayer_CustomPlayer__1start(
        JNIEnv *env,
        jobject /* this */) {
    if (fFmpeg != NULL) {
        pthread_create(&thread_start, NULL, startCallBack, fFmpeg);
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_android_customplayer_CustomPlayer__1pause(
        JNIEnv *env,
        jobject /* this */) {
    if(fFmpeg != NULL)
    {
        fFmpeg->pause();
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_android_customplayer_CustomPlayer__1resume(
        JNIEnv *env,
        jobject /* this */) {
    if(fFmpeg != NULL)
    {
        fFmpeg->resume();
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_android_customplayer_CustomPlayer__1seek(
        JNIEnv *env,
        jobject /* this */,
        jint secds) {
    if(fFmpeg != NULL)
    {
        fFmpeg->seek(secds);
    }

}

extern "C" JNIEXPORT void JNICALL
Java_com_android_customplayer_CustomPlayer__1stop(
        JNIEnv *env,
        jobject /* this */obj) {

    if(!nexit)
    {
        return;
    }

    jclass clz = env->GetObjectClass(obj);
    jmethodID jmid_next = env->GetMethodID(clz, "onCallNext", "()V");

    nexit = false;
    if(fFmpeg != NULL)
    {
        fFmpeg->release();
        pthread_join(thread_start, NULL);
        delete(fFmpeg);
        fFmpeg = NULL;
        if(callJava != NULL)
        {
            delete(callJava);
            callJava = NULL;
        }
        if(playstatus != NULL)
        {
            delete(playstatus);
            playstatus = NULL;
        }
    }
    nexit = true;
    env->CallVoidMethod(obj, jmid_next);

}
