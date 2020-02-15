package com.android.customplayer.model;

public class CustomTimeInfo {

    private int mCurrentTime;
    private int mTotalTime;

    public int getCurrentTime() {
        return mCurrentTime;
    }

    public void setCurrentTime(int currentTime) {
        mCurrentTime = currentTime;
    }

    public int getTotalTime() {
        return mTotalTime;
    }

    public void setTotalTime(int totalTime) {
        mTotalTime = totalTime;
    }

    public String toString() {
        return "TimeInfo[currentTime="+mCurrentTime+", totalTime="+mTotalTime+"]";
    }
}
