package com.fda_sampling.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.fda_sampling.R;
import com.fda_sampling.service.HttpUtils;
import com.fda_sampling.util.ClickUtil;
import com.fda_sampling.util.MyApplication;

public class TaskQueryActivity extends AppCompatActivity {
    private String Emp_No, base_url, other_url;
    private Spinner sp_task_mon;
    private WebView wv_task_query;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_task_query);
        Context context = this;
        SharedPreferences sharedPreferences = getSharedPreferences("Info", MODE_PRIVATE);
        if (((MyApplication) getApplication()).getNO() == null) {
            Emp_No = sharedPreferences.getString("NO", null);
        } else {
            Emp_No = ((MyApplication) getApplication()).getNO();
        }
        base_url = HttpUtils.getIp_WebReport() + "ReportServer?reportlet=";

        toolbar = findViewById(R.id.toolbar_task_plan);
        sp_task_mon = findViewById(R.id.spinner_task_mon);
        wv_task_query = findViewById(R.id.wv_task_query);

        toolbar.setTitle("任务查询");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //设置toolbar
        setSupportActionBar(toolbar);
        //左边的小箭头（注意需要在setSupportActionBar(toolbar)之后才有效果）
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white);
        //菜单点击事件（注意需要在setSupportActionBar(toolbar)之后才有效果）
        //toolbar.setOnMenuItemClickListener(onMenuItemClick);

        ArrayAdapter ada_task_mon = ArrayAdapter.createFromResource(context, R.array
                .TASk_MON, android.R.layout.simple_spinner_dropdown_item);
        sp_task_mon.setAdapter(ada_task_mon);

        /*settings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()
        ，默认为false*/

        wv_task_query.getSettings().setJavaScriptEnabled(true);
        wv_task_query.getSettings().setDomStorageEnabled(true);//DOM Storage*/
        wv_task_query.getSettings().setAllowFileAccess(true);
        wv_task_query.getSettings().setAppCacheEnabled(true);//是否使用缓存
        wv_task_query.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        wv_task_query.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        wv_task_query.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        wv_task_query.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)
        // 一起解决网页自适应问题
        wv_task_query.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        //过滤https中夹杂http请求
        wv_task_query.setWebViewClient(new myWebViewClient());

        sp_task_mon.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO 自动生成的方法存根
                attemptTaskQuery(sp_task_mon.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO 自动生成的方法存根
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wv_task_query != null) {
            wv_task_query.destroy();
        }
    }

    public class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        //接受所有的证书
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.taskquery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_task_query:
                if (ClickUtil.isFastClick()) {
                    attemptTaskQuery(sp_task_mon.getSelectedItem().toString());
                } else {
                    Snackbar.make(toolbar, "点击太快了，请稍后再试",
                            Snackbar.LENGTH_LONG).setAction("Action", null)
                            .show();
                }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void attemptTaskQuery(String mon) {
        switch (mon) {
            case "全年":
                other_url = "抽样年度任务.cpt&EMP_NO=" + Emp_No;
                break;
            case "一月":
                other_url = "抽样月度任务.cpt&EMP_NO=" + Emp_No + "&MON=01";
                break;
            case "二月":
                other_url = "抽样月度任务.cpt&EMP_NO=" + Emp_No + "&MON=02";
                break;
            case "三月":
                other_url = "抽样月度任务.cpt&EMP_NO=" + Emp_No + "&MON=03";
                break;
            case "四月":
                other_url = "抽样月度任务.cpt&EMP_NO=" + Emp_No + "&MON=04";
                break;
            case "五月":
                other_url = "抽样月度任务.cpt&EMP_NO=" + Emp_No + "&MON=05";
                break;
            case "六月":
                other_url = "抽样月度任务.cpt&EMP_NO=" + Emp_No + "&MON=06";
                break;
            case "七月":
                other_url = "抽样月度任务.cpt&EMP_NO=" + Emp_No + "&MON=07";
                break;
            case "八月":
                other_url = "抽样月度任务.cpt&EMP_NO=" + Emp_No + "&MON=08";
                break;
            case "九月":
                other_url = "抽样月度任务.cpt&EMP_NO=" + Emp_No + "&MON=09";
                break;
            case "十月":
                other_url = "抽样月度任务.cpt&EMP_NO=" + Emp_No + "&MON=10";
                break;
            case "十一月":
                other_url = "抽样月度任务.cpt&EMP_NO=" + Emp_No + "&MON=11";
                break;
            case "十二月":
                other_url = "抽样月度任务.cpt&EMP_NO=" + Emp_No + "&MON=12";
                break;
            default:
                break;
        }
        Log.v("full_url", base_url + other_url);
        wv_task_query.loadUrl(base_url + other_url);
    }
}
