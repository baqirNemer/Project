package com.example.project;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadService extends Service {
    private static final String TAG = "DownloadService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("PDF_URL")) {
            String pdfUrl = intent.getStringExtra("PDF_URL");
            Context context = (Context) intent.getSerializableExtra("CONTEXT");
            new DownloadPdfTask().execute(pdfUrl);
        } else {
            Log.e(TAG, "No PDF URL provided");
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    private class DownloadPdfTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String pdfUrl = urls[0];
            try {
                URL url = new URL(pdfUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    String pdfFilePath = savePdfToFile(inputStream);
                    inputStream.close();
                    return pdfFilePath;
                }
            } catch (IOException e) {
                Log.e(TAG, "Error downloading PDF: " + e.getMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(String pdfFilePath) {
            if (pdfFilePath != null) {
                Toast.makeText(DownloadService.this, "PDF saved to: " + pdfFilePath, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(DownloadService.this, "Failed to download PDF", Toast.LENGTH_LONG).show();
            }
            stopSelf();
        }
        private String savePdfToFile(InputStream inputStream) {
            try {
                String timeStamp = String.valueOf(System.currentTimeMillis());
                String pdfFileName = "Medical_PDF_" + timeStamp + ".pdf";

                File directory = new File(getExternalFilesDir(null) + "/PDFs/");
                if (!directory.exists()) {
                    if (!directory.mkdirs()) {
                        Log.e(TAG, "Failed to create directory");
                        return null;
                    }
                }

                FileOutputStream outputStream = new FileOutputStream(directory.getPath() + "/" + pdfFileName);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                return directory.getPath() + "/" + pdfFileName;
            } catch (IOException e) {
                Log.e(TAG, "Error saving PDF: " + e.getMessage());
                return null;
            }
        }
    }
}