package com.fda_sampling.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.fda_sampling.R;
import com.fda_sampling.service.HttpUtils;
import com.fda_sampling.util.MyApplication;

public class TaskPlanActivity extends AppCompatActivity {
    private String Emp_No, base_url, other_url, full_url;
    private Spinner sp_task_mon;
    private WebView wv_task_plan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_task_plan);
        Context context = this;
        SharedPreferences sharedPreferences = getSharedPreferences("Info", MODE_PRIVATE);
        if (((MyApplication) getApplication()).getNO() == null) {
            Emp_No = sharedPreferences.getString("NO", null);
        } else {
            Emp_No = ((MyApplication) getApplication()).getNO();
        }
        base_url = HttpUtils.getIp_WebReport() + "ReportServer?reportlet=";

        Toolbar toolbar = findViewById(R.id.toolbar_task_plan);
        sp_task_mon = findViewById(R.id.spinner_task_mon);
        Button btn_task_plan = findViewById(R.id.btn_task_plan);
        wv_task_plan = findViewById(R.id.wv_task_plan);

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

        wv_task_plan.getSettings().setJavaScriptEnabled(true);
        wv_task_plan.getSettings().setDomStorageEnabled(true);//DOM Storage*/
        wv_task_plan.getSettings().setAllowFileAccess(true);
        wv_task_plan.getSettings().setAppCacheEnabled(true);//是否使用缓存
        wv_task_plan.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        wv_task_plan.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        wv_task_plan.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        wv_task_plan.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        wv_task_plan.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        //过滤https中夹杂http请求
        wv_task_plan.setWebViewClient(new myWebViewClient());

        btn_task_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (sp_task_mon.getSelectedItem().toString()) {
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
                    case "全年":
                        other_url = "抽样年度任务.cpt&EMP_NO=" + Emp_No;
                        break;
                    default:
                        break;
                }
                full_url = base_url + other_url;
                Log.v("full_url", full_url);
                wv_task_plan.loadUrl(full_url);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wv_task_plan != null) {
            wv_task_plan.destroy();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
