package com.example.swiftly;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class ToolbarManager {
    //variáveis
    private Toolbar barraFerramentas;
    private ProgressBar barraProgresso;
    private ImageView iconeSite;
    private EditText endereco;
    private TextView nomeSite;
    private NavigationManager navigationManager;

    //construtor
    ToolbarManager(Toolbar barraFerramentas, ProgressBar barraProgresso, ImageView iconeSite, EditText endereco, TextView nomeSite, NavigationManager navigationManager){
        this.barraFerramentas = barraFerramentas;
        this.barraProgresso = barraProgresso;
        this.iconeSite = iconeSite;
        this.endereco = endereco;
        this.nomeSite = nomeSite;
        this.navigationManager = navigationManager;
    }

    public void alterarProgresso(int progresso){
        this.barraProgresso.setVisibility(View.VISIBLE);
        this.barraProgresso.setProgress(progresso);

        if(this.barraProgresso.getProgress() == 100){
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    barraProgresso.setProgress(0);
                    barraProgresso.setVisibility(View.GONE);
                }
            }, 1000);
        }
    }

    public void definirImagem(Bitmap icone){
        iconeSite.setImageBitmap(icone);
    }

    public void definirConfiguracoes(WebView webView){
        nomeSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nomeSite.setVisibility(View.GONE);
                endereco.setVisibility(View.VISIBLE);
                endereco.setText(webView.getUrl());
            }
        });

        barraProgresso.setMax(100);
        endereco.setVisibility(View.GONE);

        endereco.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_NEXT){
                    navigationManager.carregarUrl(endereco.getText().toString());
                }
                return false;
            }
        });

        endereco.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                endereco.getText().clear();
                return true;
            }
        });
    }

    public void nomeSiteRecebido(String titulo){
        endereco.setVisibility(View.GONE);
        nomeSite.setText(titulo);
        nomeSite.setVisibility(View.VISIBLE);
    }

    public void mudarModo(Context contexto, boolean estado, WebSettings config, MenuItem itemModo){
        String novoUserAgent, modo;
        if(estado){
            novoUserAgent = contexto.getString(R.string.user_agent_modoComputador);
            modo = contexto.getString(R.string.botao_modoComp);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    itemModo.setTitle(contexto.getString(R.string.botao_modoCelular));
                    itemModo.setIcon(R.drawable.celular_icone);
                }
            }, 350);
        }else{
            novoUserAgent = null;
            modo = contexto.getString(R.string.botao_modoCelular);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    itemModo.setTitle(contexto.getString(R.string.botao_modoComp));
                    itemModo.setIcon(R.drawable.comp_icone);
                }
            }, 350);
        }
        config.setUserAgentString(novoUserAgent);
        config.setUseWideViewPort(estado);
        config.setLoadWithOverviewMode(estado);
        config.setBuiltInZoomControls(estado);
        config.setDisplayZoomControls(!estado);

        Toast.makeText(contexto, contexto.getString(R.string.avisoModoAtivo, modo), Toast.LENGTH_SHORT).show();

        navigationManager.recarregar();
    }

    public Toolbar obterToolbar(){
        return barraFerramentas;
    }

    public String obterUrlDigitada(){
        return endereco.getText().toString();
    }
}
