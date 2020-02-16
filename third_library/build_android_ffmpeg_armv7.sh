#!/bin/bash
CPU=armv7-a
API=24
ARCH=arm
PLATFORM=arm-linux-androideabi
SYSROOT=$NDK_ROOT/platforms/android-$API/arch-$ARCH/
CROSS_PREFIX=$NDK_ROOT/toolchains/$PLATFORM-4.9/prebuilt/darwin-x86_64/bin/$PLATFORM-
PREFIX=$(pwd)/android/$CPU #自己指定一个输出目录
rm -rf $(pwd)/android/$CPU

echo "开始编译ffmpeg $CPU so"
./configure \
--prefix=$PREFIX \
--disable-doc \
--enable-shared \
--disable-static \
--disable-x86asm \
--disable-asm \
--disable-symver \
--enable-gpl \
--disable-ffmpeg \
--disable-ffplay \
--disable-ffprobe \
--enable-small \
--enable-cross-compile \
--cross-prefix=$CROSS_PREFIX \
--target-os=android \
--arch=$ARCH \
--sysroot=$SYSROOT

make clean
make -j4
make install

echo "完成编译..."
