package com.example.swiftly;

import android.app.Activity;
import android.content.Context;
import android.webkit.WebView;
import android.widget.Toast;

public class NavigationManager {
    WebView webView;
    Context contexto;

    NavigationManager(WebView webView, Context contexto){
        this.webView = webView;
        this.contexto = contexto;
    }

    public void carregarUrl(String url){
        if(!url.startsWith(contexto.getString(R.string.urlInicio))){
            url = contexto.getString(R.string.urlInicio) + url;
        }
        webView.loadUrl(url);
    }

    public void voltarUrl(int id, Activity activity){
        if(webView.canGoBack() && (id == 0 || id == 1)){
            webView.goBack();
        }else if(!webView.canGoBack() && id == 0){
            Toast.makeText(contexto, contexto.getString(R.string.avisoImpossivelVoltar), Toast.LENGTH_SHORT).show();
        }else if(!webView.canGoBack() && id == 1){
            activity.finish();
        }
    }

    public void proximaUrl(){
        if(webView.canGoForward()){
            webView.goForward();
        }else if(!webView.canGoForward()){
            Toast.makeText(contexto, contexto.getString(R.string.avisoImpossivelAvancar), Toast.LENGTH_SHORT).show();
        }
    }

    public void recarregar(){
        webView.reload();
    }
}
