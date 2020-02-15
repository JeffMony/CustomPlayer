package com.android.customplayer.model;

public class VideoSizeInfo {

    private int mWidth;
    private int mHeight;

    public VideoSizeInfo(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }
}
