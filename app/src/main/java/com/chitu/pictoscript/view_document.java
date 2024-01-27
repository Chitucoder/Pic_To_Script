package com.chitu.pictoscript;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URLEncoder;

public class view_document extends AppCompatActivity {

    WebView documentview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_document);


        documentview=(WebView) findViewById(R.id.viewDocument);
        documentview.getSettings().setJavaScriptEnabled(true);

        String filename=getIntent().getStringExtra("filename");
        String fileurl=getIntent().getStringExtra("fileurl");

        ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle(filename);
        pd.setMessage("Opening file");

        Uri uri = Uri.parse(fileurl);
        Intent intent = new Intent(Intent.ACTION_VIEW);


        documentview.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon )
            {
                super.onPageStarted(view, url, favicon);
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pd.dismiss();
            }
        });

        String url="";
        try {
            url = URLEncoder.encode(fileurl, "UTF-8");
        }catch (Exception ex){}

        documentview.loadUrl("http://docs.google.com/gview?embedded=true&url="+url);

    }
}