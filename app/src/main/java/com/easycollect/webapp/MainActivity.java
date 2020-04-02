package com.easycollect.webapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
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

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        loadingSpinner = findViewById(R.id.loading_spinner);

        // REMOTE RESOURCE
        mWebView.loadUrl("https://vetrina.cloud/");

        // old WebViewClient
        //mWebView.setWebViewClient(new MyWebViewClient());

        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed() ;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //Hide the spinner
                loadingSpinner.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading (WebView view, String url){
                //Code to parse url
                if (url.contains("#/utenti")){
                    TutorialActivity.showTutorial(MainActivity.this);
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

                /**
                 * Old web error logic, it showed up a Dialog.. now we have @WebErrorActivity
                 */

                /*

                webView.loadUrl("about:blank");

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Errore");
                alertDialog.setMessage("La pagina al momento non è disponibile, ci scusiamo per il disagio");
                alertDialog.setButton("Ricarica", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(getIntent());
                    }
                });

                alertDialog.show();

                //Don't forget to call supper!
                super.onReceivedError(webView, errorCode, description, failingUrl);

                */


            }
        });

        // LOCAL RESOURCE
        // mWebView.loadUrl("file:///android_asset/index.html");
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
