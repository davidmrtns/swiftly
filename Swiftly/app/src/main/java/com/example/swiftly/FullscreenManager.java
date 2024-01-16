package com.example.swiftly;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;

public class FullscreenManager {
    private View mCustomView;
    private int mOriginalSystemUiVisibility;
    private int mOriginalOrientation;
    private WebChromeClient webChromeClient;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private Activity activity;
    private static final int CONFIG_TELA_CHEIA = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
    //construtor
    FullscreenManager(View mCustomView, int mOriginalSystemUiVisibility, int mOriginalOrientation, WebChromeClient webChromeClient, WebChromeClient.CustomViewCallback mCustomViewCallback, Activity activity){
        this.mCustomView = mCustomView;
        this.mOriginalSystemUiVisibility = mOriginalSystemUiVisibility;
        this.mOriginalOrientation = mOriginalOrientation;
        this.webChromeClient = webChromeClient;
        this.mCustomViewCallback = mCustomViewCallback;
        this.activity = activity;
    }

    public void exibirTelaCheia(View view, WebChromeClient.CustomViewCallback callback){
        if (mCustomView != null)
        {
            webChromeClient.onHideCustomView();
            return;
        }
        mCustomView = view;
        mOriginalSystemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
        mOriginalOrientation = activity.getRequestedOrientation();
        mCustomViewCallback = callback;

        activity.getWindowManager().addView(mCustomView, new WindowManager.LayoutParams(1, 1024));
        activity.getWindow().getDecorView().setSystemUiVisibility(CONFIG_TELA_CHEIA);
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
    }
}
