package com.fda_sampling.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.fda_sampling.R;
import com.fda_sampling.service.FDA_API;
import com.fda_sampling.service.HttpUtils;
import com.fda_sampling.util.ClickUtil;
import com.fda_sampling.util.MyApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskQueryActivity2 extends AppCompatActivity {
    private String Emp_No, png_path;
    private Spinner sp_task_mon;
    private ZoomImageView ziv_task_query;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_task_query2);
        Context context = this;
        SharedPreferences sharedPreferences = getSharedPreferences("Info", MODE_PRIVATE);
        if (((MyApplication) getApplication()).getNO() == null) {
            Emp_No = sharedPreferences.getString("NO", null);
        } else {
            Emp_No = ((MyApplication) getApplication()).getNO();
        }

        toolbar = findViewById(R.id.toolbar_task_plan);
        sp_task_mon = findViewById(R.id.spinner_task_mon);
        ziv_task_query = findViewById(R.id.ziv_task_query);

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
        String reportlet, MON;
        png_path = Environment.getExternalStorageDirectory() + "/FDA/Task/";
        if (mon.equals("全年")) {
            MON = "00";
            reportlet = "抽样年度任务.cpt";
            png_path = png_path + Emp_No + "-年度.png";
        } else {
            reportlet = "抽样月度任务.cpt";
            switch (mon) {
                case "一月":
                    MON = "01";
                    break;
                case "二月":
                    MON = "02";
                    break;
                case "三月":
                    MON = "03";
                    break;
                case "四月":
                    MON = "04";
                    break;
                case "五月":
                    MON = "05";
                    break;
                case "六月":
                    MON = "06";
                    break;
                case "七月":
                    MON = "07";
                    break;
                case "八月":
                    MON = "08";
                    break;
                case "九月":
                    MON = "09";
                    break;
                case "十月":
                    MON = "10";
                    break;
                case "十一月":
                    MON = "11";
                    break;
                case "十二月":
                    MON = "12";
                    break;
                default:
                    MON = "00";
                    break;
            }
            png_path = png_path + Emp_No + "-月度-" + MON + ".png";
        }

        /*Log.v("full_url", base_url + other_url);
        wv_task_query.loadUrl(base_url + other_url);*/

        final File file_png = new File(png_path);
        if (file_png.exists()) {
            file_png.delete();
        }

        FDA_API request = HttpUtils.ReportApi();
        Map<String, String> params = new HashMap<>();
        params.put("reportlet", reportlet);
        params.put("EMP_NO", Emp_No);
        params.put("MON", MON);
        params.put("op", "export");
        params.put("format", "image");
        params.put("extype", "PNG");
        Call<ResponseBody> call = request.ReportServer(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            // 获取文件的输出流对象
                            FileOutputStream outStream = new FileOutputStream(file_png);
                            // 获取字符串对象的byte数组并写入文件流
                            outStream.write(response.body().bytes());
                            // 最后关闭文件输出流
                            outStream.close();
                            Log.v("png", "下载成功");

                            Glide.with(TaskQueryActivity2.this).load(png_path)
                                    .placeholder(R.mipmap.logo)
                                    .error(R.mipmap.error)
                                    .into(ziv_task_query);

                            //doPrintPdf(file_pdf);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Log.v("ResponseBody", "FileNotFoundException");
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.v("ResponseBody", "IOException");
                        }
                    } else {
                        Log.v("Report请求成功!", "response.body is null");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("Report请求失败!", t.getMessage());
            }
        });
    }
}
