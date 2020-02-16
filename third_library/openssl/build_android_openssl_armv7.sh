#!/bin/bash
export NDK_ROOT=/Users/tianpeng/tools/android-ndk-r14b # 修改自己本地的ndk路径

## build armv7
CPU=arm
make clean
echo "-----------------------"
echo $(pwd)/android/$CPU
echo "-----------------------"
rm -rf $(pwd)/android/$CPU
export ANDROID_NDK_HOME=$NDK_ROOT
PATH=$ANDROID_NDK_HOME/toolchains/$CPU-linux-androideabi-4.9/prebuilt/darwin-x86_64/bin:$PATH
./Configure android-$CPU -D__ANDROID_API__=24 no-shared no-ssl2 no-ssl3 no-comp no-hw no-engine --prefix=$(pwd)/android/$CPU --openssldir=$(pwd)/android/$CPU
make
make install