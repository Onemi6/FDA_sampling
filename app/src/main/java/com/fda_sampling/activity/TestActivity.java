package com.fda_sampling.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fda_sampling.R;
import com.fda_sampling.html.JavascriptInterfaceImpl;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class TestActivity extends Activity {

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        webview = findViewById(R.id.wv_content);
        // 得到设置属性的对象
        WebSettings webSettings = webview.getSettings();
        // 使能JavaScript
        webSettings.setJavaScriptEnabled(true);
        // 支持中文，否则页面中中文显示乱码
        webSettings.setDefaultTextEncodingName("UTF-8");

        // 传入一个Java对象和一个接口名,在JavaScript代码中用这个接口名代替这个对象,通过接口名调用Android接口的方法
        webview.addJavascriptInterface(new JavascriptInterfaceImpl(this, this,
                webview), "Android");

        // WebViewClient 主要帮助WebView处理各种通知、请求事件的
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        // WebChromeClient主要用来辅助WebView处理Javascript的对话框、网站图标、网站标题以及网页加载进度等
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        // 载入页面：本地html资源文件
        webview.loadUrl("file:///android_asset/test.html");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode,
                data);
        if (scanResult != null) {
            String result = "扫描结果:" + scanResult.getContents();
            Log.v("result", result);
            webview.loadUrl("javascript:GetResult('" + result + "')");
        }
    }
}
