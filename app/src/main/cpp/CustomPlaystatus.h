
#ifndef CUSTOM_PLAYSTATUS_H
#define CUSTOM_PLAYSTATUS_H


class CustomPlaystatus {

public:
    bool exit = false;
    bool load = true;
    bool seek = false;
    bool pause = false;

public:
    CustomPlaystatus();
    ~CustomPlaystatus();

};


#endif //CUSTOM_PLAYSTATUS_H
