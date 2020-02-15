package com.android.customplayer.model;

public class VideoSizeInfo {

    private int mWidth;
    private int mHeight;
    private float mDar;

    public VideoSizeInfo(int width, int height, float dar) {
        this.mWidth = width;
        this.mHeight = height;
        this.mDar = dar;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public float getDar() {
        return mDar;
    }
}
