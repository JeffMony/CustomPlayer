
#ifndef CUSTOM_FFMPEG_H
#define CUSTOM_FFMPEG_H

#include "CustomCallJava.h"
#include "pthread.h"
#include "CustomAudio.h"
#include "CustomPlaystatus.h"
#include "CustomVideo.h"

extern "C"
{
#include "libavformat/avformat.h"
#include "libavutil/rational.h"
#include <libavutil/time.h>
};


class CustomFFmpeg {

public:
    CustomCallJava *callJava = NULL;
    const char* url = NULL;
    pthread_t decodeThread;
    AVFormatContext *pFormatCtx = NULL;
    CustomAudio *audio = NULL;
    CustomVideo *video = NULL;
    CustomPlaystatus *playstatus = NULL;
    pthread_mutex_t init_mutex;
    bool exit = false;
    int duration = 0;
    pthread_mutex_t seek_mutex;
    bool supportMediacodec = false;

    const AVBitStreamFilter *bsFilter = NULL;

public:
    CustomFFmpeg(CustomPlaystatus *playstatus, CustomCallJava *callJava, const char *url);
    ~CustomFFmpeg();

    void prepare();
    void decodeFFmpegThread();
    void start();

    void pause();

    void resume();

    void release();

    void seek(int64_t secds);

    int getCodecContext(AVCodecParameters *codecpar, AVCodecContext **avCodecContext);

};


#endif //CUSTOM_FFMPEG_H
