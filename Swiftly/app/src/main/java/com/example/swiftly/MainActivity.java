package com.example.swiftly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private ToolbarManager toolbarManager;
    private WebViewManager webViewManager;
    private FilePickerManager filePickerManager;
    private MenuItem itemModo;
    private boolean estado = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        filePickerManager.respostaFilePicker(resultCode, requestCode, intent);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = findViewById(R.id.webView);

        navigationManager = new NavigationManager(webView, getApplicationContext());
        toolbarManager = new ToolbarManager(
                findViewById(R.id.toolbar),
                findViewById(R.id.barraCarregamento),
                findViewById(R.id.imgSite),
                findViewById(R.id.editTxtUrl),
                findViewById(R.id.txtNomeSite),
                navigationManager);
        DownloadManager downloadManager = new DownloadManager(getApplicationContext());
        filePickerManager = new FilePickerManager(this);
        webViewManager = new WebViewManager(webView, toolbarManager, downloadManager, filePickerManager, this);

        toolbarManager.definirConfiguracoes(webViewManager.obterWebView());
        setSupportActionBar(toolbarManager.obterToolbar());
        webViewManager.definirConfiguracoes();

        webViewManager.modoEscuro(getResources().getConfiguration().uiMode, Configuration.UI_MODE_NIGHT_MASK);
        navigationManager.carregarUrl(getString(R.string.urlGoogle));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        itemModo = menu.findItem(R.id.modoVisual);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.inicio:
                navigationManager.carregarUrl(getString(R.string.urlGoogle));
                break;
            case R.id.acessarPagina:
                navigationManager.carregarUrl(toolbarManager.obterUrlDigitada());
                break;
            case R.id.paginaSeguinte:
                navigationManager.proximaUrl();
                break;
            case R.id.paginaAnterior:
                navigationManager.voltarUrl(0, this);
                break;
            case R.id.recarregar:
                navigationManager.recarregar();
                break;
            case R.id.limparHistorico:
                webViewManager.excluirDados(getApplicationContext());
                break;
            case R.id.modoVisual:
                estado = !estado;
                toolbarManager.mudarModo(getApplicationContext(), estado, webViewManager.obterConfiguracoes(), itemModo);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        navigationManager.voltarUrl(1, this);
    }
}
