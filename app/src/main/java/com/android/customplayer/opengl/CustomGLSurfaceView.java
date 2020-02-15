package com.android.customplayer.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class CustomGLSurfaceView extends GLSurfaceView {

    private CustomRender mRender;

    public CustomGLSurfaceView(Context context) {
        this(context, null);
    }

    public CustomGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        mRender = new CustomRender(context);
        setRenderer(mRender);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        mRender.setOnRenderListener(new CustomRender.OnRenderListener() {
            @Override
            public void onRender() {
                requestRender();
            }
        });
    }

    public void setYUVData(int width, int height, byte[] y, byte[] u, byte[] v)
    {
        if(mRender != null)
        {
            mRender.setYUVRenderData(width, height, y, u, v);
            requestRender();
        }
    }

    public CustomRender getRender() {
        return mRender;
    }
}
