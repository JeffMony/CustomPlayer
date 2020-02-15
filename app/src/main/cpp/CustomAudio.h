
#ifndef CUSTOM_AUDIO_H
#define CUSTOM_AUDIO_H

#include "CustomQueue.h"
#include "CustomPlaystatus.h"
#include "CustomCallJava.h"

extern "C"
{
#include <libavutil/time.h>
#include "libavcodec/avcodec.h"
#include <libswresample/swresample.h>
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
};

class CustomAudio {

public:
    int streamIndex = -1;
    AVCodecContext *avCodecContext = NULL;
    AVCodecParameters *codecpar = NULL;
    CustomQueue *queue = NULL;
    CustomPlaystatus *playstatus = NULL;
    CustomCallJava *callJava = NULL;

    pthread_t thread_play;
    AVPacket *avPacket = NULL;
    AVFrame *avFrame = NULL;
    int ret = 0;
    uint8_t *buffer = NULL;
    int data_size = 0;
    int sample_rate = 0;

    int duration = 0;
    AVRational time_base;
    double clock;//总的播放时长
    double now_time;//当前frame时间
    double last_tiem; //上一次调用时间


    // 引擎接口
    SLObjectItf engineObject = NULL;
    SLEngineItf engineEngine = NULL;

    //混音器
    SLObjectItf outputMixObject = NULL;
    SLEnvironmentalReverbItf outputMixEnvironmentalReverb = NULL;
    SLEnvironmentalReverbSettings reverbSettings = SL_I3DL2_ENVIRONMENT_PRESET_STONECORRIDOR;

    //pcm
    SLObjectItf pcmPlayerObject = NULL;
    SLPlayItf pcmPlayerPlay = NULL;

    //缓冲器队列接口
    SLAndroidSimpleBufferQueueItf pcmBufferQueue = NULL;

    pthread_mutex_t codecMutex;

public:
    CustomAudio(CustomPlaystatus *playstatus, int sample_rate, CustomCallJava *callJava);
    ~CustomAudio();

    void play();
    int resampleAudio();

    void initOpenSLES();

    int getCurrentSampleRateForOpensles(int sample_rate);

    void pause();

    void resume();

    void stop();

    void release();


};


#endif //CUSTOM_AUDIO_H
