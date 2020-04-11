package com.easycollect.webapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

    private WebView mWebView;
    private ProgressBar loadingSpinner;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isFirstTime = ColligoPreferences.isFirst(MainActivity.this);
        if (isFirstTime) {
            TutorialActivity.showTutorial(MainActivity.this);
        }

        setContentView(R.layout.activity_main);
        mWebView = findViewById(R.id.activity_main_webview);

        // OPTIMIZED WEB SETTINGS FOR EMBEDDED VUE.JS
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);

        loadingSpinner = findViewById(R.id.loading_spinner);


        // SET CLIENTS
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.notification_error_ssl_cert_invalid);
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //Hide the spinner
                loadingSpinner.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading (WebView view, String url){
                //Code to parse url
                //System.out.println(url);
                if (url.contains("#/utenti")){
                    TutorialActivity.showTutorial(MainActivity.this);
                    return true;
                }
                if(url.startsWith("tel:") || url.startsWith("whatsapp:") || url.contains("me")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }

                return false;
            }

            @Override
            public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
                //Clearing the WebView
                try {
                    webView.stopLoading();
                } catch (Exception e) {
                }
                try {
                    webView.clearView();
                } catch (Exception e) {
                }
                if (webView.canGoBack()) {
                    webView.goBack();
                }

                Intent startIntent = new Intent(getApplicationContext(), WebErrorActivity.class);
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(startIntent);



            }
        });
        mWebView.setWebChromeClient(new WebChromeClient());

        // REMOTE RESOURCE
        mWebView.loadUrl("https://colligo.shop");
    }



    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
