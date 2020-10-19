package com.example.netscout;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;

    SharedPreferences sharedPref;
    private WebView webView;
    Button b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPref =getSharedPreferences("A", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView webView = findViewById(R.id.webwindow);

        sharedPref =getSharedPreferences("A", Context.MODE_PRIVATE);
        String userLink= sharedPref.getString("userland", "default");


        webView.setWebViewClient(new MyBrowser());

        webView.setWebChromeClient(new WebChromeClient()
        {

            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
            {

                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent intent;

                intent = fileChooserParams.createIntent();
                try
                {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e)
                {
                    uploadMessage = null;
                    return false;
                }
                return true;
            }


            protected void openFileChooser(ValueCallback<Uri> uploadMsg)
            {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }
        });


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        if(userLink.equals("default")){
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        }
        webView.loadUrl(userLink);

        b3= findViewById(R.id.button3);
        b3.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                sharedPref =getSharedPreferences("A", Context.MODE_PRIVATE);
                String userLink = sharedPref.getString("userland", "default");
                if (userLink.equals("default")){
                    Intent intent = new Intent(MainActivity.this, Settings.class);
                    startActivity(intent);
                    setContentView(R.layout.activity_settings);
                } else {
                    webSettings.setJavaScriptEnabled(true);
                    webView.getSettings().setLoadWithOverviewMode(true);
                    webView.getSettings().setUseWideViewPort(true);
                    webView.loadUrl(userLink);
                    webView.loadUrl(userLink);
                }
            }

        });


    }

    @Override
    public void onBackPressed() {
        WebView webView = findViewById(R.id.webwindow);
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_SELECT_FILE) {
            if (uploadMessage == null)
                return;
            uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
            uploadMessage = null;
        }



    }


    public void gotoSettings(View view){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return true;
        }
    }




}