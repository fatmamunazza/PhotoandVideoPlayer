package com.example.photoandvideoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Video extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        WebView containerWebview=(WebView)findViewById(R.id.you_tube);
        containerWebview.setWebViewClient(new MyWebViewClient());
        String url="https://m.youtube.com/";
        containerWebview.getSettings().setJavaScriptEnabled(true);
        containerWebview.loadUrl(url);
    }
    private class MyWebViewClient extends WebViewClient{
        public boolean shouldOverrideUrlLoading(WebView view,String url){
            view.loadUrl(url);
            return true;
        }
    }
}
