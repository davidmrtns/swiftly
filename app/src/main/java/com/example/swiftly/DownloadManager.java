package com.example.swiftly;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

public class DownloadManager {
    private Context contexto;

    DownloadManager(Context contexto){
        this.contexto = contexto;
    }

    public void baixarArquivo(String nomeArquivo, String url){
        android.app.DownloadManager.Request request = new android.app.DownloadManager.Request(Uri.parse(url));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nomeArquivo);

        android.app.DownloadManager manager = (android.app.DownloadManager) contexto.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        Toast.makeText(contexto, R.string.avisoDownload, Toast.LENGTH_SHORT).show();

    }
}
