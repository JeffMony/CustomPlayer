
#ifndef CUSTOM_QUEUE_H
#define CUSTOM_QUEUE_H

#include "queue"
#include "pthread.h"
#include "AndroidLog.h"
#include "CustomPlaystatus.h"

extern "C"
{
#include "libavcodec/avcodec.h"
};


class CustomQueue {

public:
    std::queue<AVPacket *> queuePacket;
    pthread_mutex_t mutexPacket;
    pthread_cond_t condPacket;
    CustomPlaystatus *playstatus = NULL;

public:

    CustomQueue(CustomPlaystatus *playstatus);
    ~CustomQueue();

    int putAvpacket(AVPacket *packet);
    int getAvpacket(AVPacket *packet);

    int getQueueSize();

    void clearAvpacket();

    void noticeQueue();




};
#endif //CUSTOM_QUEUE_H
