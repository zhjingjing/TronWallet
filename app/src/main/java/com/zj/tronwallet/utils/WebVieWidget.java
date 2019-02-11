package com.zj.tronwallet.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zj.tronwallet.R;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


/**
 * 封装WebView显示逻辑
 * <p>
 * Created by jiang on 2017/5/6.
 */

public class WebVieWidget extends FrameLayout implements View.OnClickListener {
    private WebView webView;
    private TextView errorView;
    private ProgressBar progressBar;
    private boolean receivedError;
    private WebLoadListener loadListener;
    private LoadingDialog loadingDialog;
    private boolean showIndeterminateProgress;
    private Activity activity;
    public WebVieWidget(@NonNull Context context) {
        this(context, null);
    }

    public WebVieWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public WebVieWidget(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            return;
        }
        LayoutInflater.from(context).inflate(R.layout.view_web, this, true);
        progressBar = findViewById(R.id.web_progress);
        errorView = findViewById(R.id.web_error_hint);
        webView = findViewById(R.id.web_view);
        webView.setFocusable(false);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //缩放开关，设置此属性，仅支持双击缩放，不支持触摸缩放
        settings.setSupportZoom(false);
        settings.setDisplayZoomControls(false); //隐藏webview缩放按钮


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
        errorView.setOnClickListener(this);
        loadingDialog = new LoadingDialog(context);
    }



    @SuppressLint("SetJavaScriptEnabled")
    public WebSettings getSetting(){
        return  webView.getSettings();
    }
    @SuppressLint("SetJavaScriptEnabled")
    public void setWebSetting(String ua){
        webView.getSettings().setUserAgentString(ua);
    }
    @SuppressLint("SetJavaScriptEnabled")
    public void loadUrl(String url,Activity activity) {
        this.activity=activity;
        webView.loadUrl(url);
    }
    @SuppressLint("SetJavaScriptEnabled")
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        webView.loadUrl(url, additionalHttpHeaders);
    }
    @SuppressLint("SetJavaScriptEnabled")
    public void loadData(String data) {
        webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
    }
    @SuppressLint("SetJavaScriptEnabled")
    public void onResume() {
        webView.onResume();
    }
    @SuppressLint("SetJavaScriptEnabled")
    public void onPause() {
        webView.onPause();
    }
    @SuppressLint("SetJavaScriptEnabled")
    public void setWebLoadListener(WebLoadListener loadListener) {
        this.loadListener = loadListener;
    }
    @SuppressLint("SetJavaScriptEnabled")
    public void showIndeterminateProgress(boolean b) {
        showIndeterminateProgress = b;
    }
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onClick(View v) {
        if (v == errorView) {
            receivedError = false;
            webView.reload();
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    public void goBack() {
        webView.goBack();
    }
    @SuppressLint("SetJavaScriptEnabled")
    public boolean canGoBack() {
        return webView.canGoBack();
    }

    @SuppressLint("JavascriptInterface")
    public void addJavascriptInterface(Object object, String name) {
        webView.addJavascriptInterface(object, name);
    }


    private void injectScriptFile(WebView view, String scriptFile) {
        InputStream input;
        try {
            input = activity.getAssets().open(scriptFile);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();

            // String-ify the script byte-array using BASE64 encoding !!!
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            view.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "script.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(script)" +
                    "})()");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (!showIndeterminateProgress) {
                progressBar.setProgress(newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (loadListener != null) {
                loadListener.onReceivedTitle(view, title);
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return loadListener != null && loadListener.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (showIndeterminateProgress) {
                loadingDialog.show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
            if (loadListener != null) {
                loadListener.onPageStarted(view, url, favicon);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (showIndeterminateProgress) {
                loadingDialog.dismiss();
            } else {
                progressBar.setVisibility(View.GONE);
            }
            if (!receivedError && errorView.getVisibility() == VISIBLE) {
                errorView.setVisibility(GONE);
            }
            if (loadListener != null) {
                if (!receivedError) {
                    loadListener.onPageSuccess(view, url);
                }
                loadListener.onPageFinished(view, url);
            }
        }



        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            receivedError = true;
            errorView.setVisibility(VISIBLE);
            if (loadListener != null) {
                loadListener.onReceivedError(view, errorCode, description, failingUrl);
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    public interface WebLoadListener {
        void onReceivedTitle(WebView view, String title);

        boolean shouldOverrideUrlLoading(WebView view, String url);

        void onPageStarted(WebView view, String url, Bitmap favicon);

        void onPageSuccess(WebView view, String url);

        void onReceivedError(WebView view, int errorCode, String description, String failingUrl);

        void onPageFinished(WebView view, String url);
    }
    @SuppressLint("SetJavaScriptEnabled")
    public static class SimpleLoadListener implements WebLoadListener {

        @Override
        public void onReceivedTitle(WebView view, String title) {

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
//            view.loadUrl(url);
//            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }

        @Override
        public void onPageSuccess(WebView view, String url) {

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

        }
        @SuppressLint("SetJavaScriptEnabled")
        @Override
        public void onPageFinished(WebView view, String url) {

        }
    }

    public void setWebViewClient(WebViewClient webViewClient){
        webView.setWebViewClient(webViewClient);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        webView.removeAllViews();
        webView.destroy();
        webView = null;
    }
}
