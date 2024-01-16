package com.example.swiftly;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.PermissionRequest;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

public class WebViewManager {
    //variáveis
    private WebView webView;
    private ToolbarManager toolbarManager;
    private DownloadManager downloadManager;
    private FilePickerManager filePickerManager;
    private Activity activity;
    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private int mOriginalOrientation;
    private int mOriginalSystemUiVisibility;
    private static final int CONFIG_TELA_CHEIA = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;

    //construtor
    WebViewManager(WebView webView, ToolbarManager toolbarManager, DownloadManager downloadManager, FilePickerManager filePickerManager, Activity activity){
        this.webView = webView;
        this.toolbarManager = toolbarManager;
        this.downloadManager = downloadManager;
        this.filePickerManager = filePickerManager;
        this.activity = activity;
    }

    //retorna a URL acessada atualmente
    public String obterUrl(){
        return webView.getUrl();
    }

    public WebSettings obterConfiguracoes(){
        return webView.getSettings();
    }

    //define configurações da WebView
    @SuppressLint("SetJavaScriptEnabled")
    public void definirConfiguracoes(){
        WebSettings config = webView.getSettings();
        config.setJavaScriptEnabled(true);
        config.setDomStorageEnabled(true);
        config.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        config.setMediaPlaybackRequiresUserGesture(false);
        webView.setWebViewClient(new WebViewClient());
        definirChromeClient();
        definirDownloadListener();
    }

    //define WebChromeClient
    private void definirChromeClient(){
        this.webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                toolbarManager.alterarProgresso(newProgress);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                toolbarManager.definirImagem(icon);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                toolbarManager.nomeSiteRecebido(title);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                filePickerManager.mostrarFilePicker(filePathCallback);
                return true;
            }

            @Override
            public Bitmap getDefaultVideoPoster() {
                return Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                if (mCustomView != null)
                {
                    onHideCustomView();
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

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();

                activity.getWindowManager().removeView(mCustomView);
                mCustomView = null;
                activity.getWindow().getDecorView().setSystemUiVisibility(mOriginalSystemUiVisibility);
                activity.setRequestedOrientation(mOriginalOrientation);
                mCustomViewCallback.onCustomViewHidden();
                mCustomViewCallback = null;

                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            }

            @Override
            public void onPermissionRequest(PermissionRequest request) {
                String[] resources = request.getResources();
                for (int i = 0; i < resources.length; i++) {
                    if (PermissionRequest.RESOURCE_PROTECTED_MEDIA_ID.equals(resources[i])) {
                        request.grant(resources);
                        return;
                    }
                }
            }
        });
    }

    //define DownloadListener
    private void definirDownloadListener(){
        this.webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long l) {
                downloadManager.baixarArquivo(URLUtil.guessFileName(url, contentDisposition, mimetype), url);
            }
        });
    }

    public WebView obterWebView(){
        return webView;
    }

    public void excluirDados(Context contexto){
        CookieManager.getInstance().removeAllCookies(null);
        webView.clearHistory();
        webView.clearFormData();
        webView.clearCache(true);
        Toast.makeText(contexto, contexto.getString(R.string.avisoDadosExcluidos), Toast.LENGTH_SHORT).show();
    }

    public void modoEscuro(int uiMode, int uiModeMask){
        switch (uiMode & uiModeMask){
            case Configuration.UI_MODE_NIGHT_YES:
                if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                    WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
                }
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                    WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_OFF);
                }
                break;
        }
    }
}
