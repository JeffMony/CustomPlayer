
#ifndef CUSTOM_VIDEO_H
#define CUSTOM_VIDEO_H


#include "CustomQueue.h"
#include "CustomCallJava.h"
#include "CustomAudio.h"

#define CODEC_YUV 0
#define CODEC_MEDIACODEC 1

extern "C"
{
#include <libswscale/swscale.h>
#include <libavutil/imgutils.h>
#include <libavutil/time.h>
#include <libavcodec/avcodec.h>
};

class CustomVideo {

public:
    int streamIndex = -1;
    AVCodecContext *avCodecContext = NULL;
    AVCodecParameters *codecpar = NULL;
    CustomQueue *queue = NULL;
    CustomPlaystatus *playstatus = NULL;
    CustomCallJava *wlCallJava = NULL;
    AVRational time_base;
    pthread_t thread_play;
    CustomAudio *audio = NULL;
    double clock = 0;
    double delayTime = 0;
    double defaultDelayTime = 0.04;
    pthread_mutex_t codecMutex;

    int codectype = CODEC_YUV;

    AVBSFContext *abs_ctx = NULL;


public:
    CustomVideo(CustomPlaystatus *playstatus, CustomCallJava *wlCallJava);
    ~CustomVideo();

    void play();

    void release();

    double getFrameDiffTime(AVFrame *avFrame, AVPacket *avPacket);

    double getDelayTime(double diff);




};


#endif //MYMUSIC_WLVIDEO_H
