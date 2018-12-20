package com.fda_sampling.html;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.fda_sampling.activity.ScanActivity;
import com.google.zxing.integration.android.IntentIntegrator;

/**
 * 自定义的Android代码和JavaScript代码之间的桥梁类
 *
 * @author 1
 */
public class JavascriptInterfaceImpl {

    private final String TAG = JavascriptInterfaceImpl.class.getSimpleName();
    private Context mContext;
    private WebView mWebView;
    private Handler mHandler;
    private Activity mActivity;

    /**
     * Instantiate the interface and set the context
     */
    public JavascriptInterfaceImpl(Context c, Activity a, WebView webView) {
        mContext = c;
        mActivity = a;
        mWebView = webView;
        mHandler = new Handler(Looper.getMainLooper());
    }

    // 如果target 大于等于API 17，则需要加上如下注解
    @JavascriptInterface
    public void Scanner1() {
        IntentIntegrator integrator = new IntentIntegrator(mActivity);
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setCaptureActivity(ScanActivity.class); //设置打开摄像头的Activity
        integrator.setPrompt("请扫描一维码"); //底部的提示文字，设为""可以置空
        integrator.setCameraId(0); //前置或者后置摄像头
        integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
        integrator.setBarcodeImageEnabled(true); // 扫完码之后生成二维码的图片
        integrator.initiateScan();
    }

    @JavascriptInterface
    public void Scanner2() {
        IntentIntegrator integrator = new IntentIntegrator(mActivity);
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setCaptureActivity(ScanActivity.class); //设置打开摄像头的Activity
        integrator.setPrompt("请扫描二维码"); //底部的提示文字，设为""可以置空
        integrator.setCameraId(0); //前置或者后置摄像头
        integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
        integrator.setBarcodeImageEnabled(true); // 扫完码之后生成二维码的图片
        integrator.initiateScan();
    }
}
