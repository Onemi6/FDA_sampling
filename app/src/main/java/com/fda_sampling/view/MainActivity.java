package com.fda_sampling.view;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.fda_sampling.R;
import com.fda_sampling.model.Task;
import com.fda_sampling.model.Tasks;
import com.fda_sampling.model.UpdateInfo;
import com.fda_sampling.service.FDA_API;
import com.fda_sampling.service.HttpUtils;
import com.fda_sampling.util.ClickUtil;
import com.fda_sampling.util.CrashHandler;
import com.fda_sampling.util.MyApplication;
import com.fda_sampling.util.NetworkUtil;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    private int select_num, isHome = 0, last_pos = -1;
    private long mExitTime, mId;
    private Toolbar toolbar;
    private RecyclerView rv_tasks;
    private LinearLayout dialog_select;
    private TextView tv_select_title, tv_select_num, tv_select_all;
    private Button btn_select_confirm;
    private MainInfoAdapter adapter;
    private String TAG_UPDATE = "update", versionName, token;
    private Context context;
    private SharedPreferences sharedPreferences;
    private UpdateInfo info;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        context = this;
        CrashHandler.getInstance().init(context);
        toolbar = findViewById(R.id.toolbar_mainInfo);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //获取头布局文件
        //View headerLayout=navigationView.inflateHeaderView(R.layout.nav_header);
        View headerView = navigationView.getHeaderView(0);
        TextView tv_user_name = headerView.findViewById(R.id.user_name);
        TextView tv_app_versionName = headerView.findViewById(R.id.app_versionName);

        dialog_select = findViewById(R.id.dialog_select);
        tv_select_title = findViewById(R.id.tv_select_title);
        tv_select_num = findViewById(R.id.tv_select_num);
        TextView tv_select_invert = findViewById(R.id.tv_select_invert);
        tv_select_all = findViewById(R.id.tv_select_all);
        btn_select_confirm = findViewById(R.id.btn_select_confirm);

        tv_select_invert.setOnClickListener(this);
        tv_select_all.setOnClickListener(this);
        btn_select_confirm.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("Info", MODE_PRIVATE);
        String name = sharedPreferences.getString("NAME", null);
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            if (name == null) {
                Intent intent_login = new Intent();
                intent_login.setClass(MainActivity.this, LoginActivity.class);
                intent_login.putExtra("login_type", -1);
                finish();
                startActivity(intent_login);
            } else {
                //设置toolbar
                setSupportActionBar(toolbar);
                //左边的小箭头（注意需要在setSupportActionBar(toolbar)之后才有效果）
                //toolbar.setNavigationIcon(R.drawable.ic_account_circle_white);
                //菜单点击事件（注意需要在setSupportActionBar(toolbar)之后才有效果）
                //toolbar.setOnMenuItemClickListener(onMenuItemClick);

                tv_user_name.setText(String.format(getResources().getString(R.string.user_name),
                        name));
                tv_app_versionName.setText(String.format(getResources().getString(R.string
                        .app_versionName), versionName));
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, 0, 0);
                drawer.addDrawerListener(toggle);
                toggle.syncState();
                navigationView.setNavigationItemSelectedListener(this);

                //初始化RecyclerView
                rv_tasks = findViewById(R.id.rv_mainInfo_add);
                //rv_tasks.setHasFixedSize(true);

                //创建LinearLayoutManager 对象 这里使用 LinearLayoutManager 是线性布局的意思
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                //设置RecyclerView 布局
                rv_tasks.setLayoutManager(layoutManager);
                //设置Adapter
                adapter = new MainInfoAdapter(this, Tasks.list_task);
                rv_tasks.setAdapter(adapter);

                viewAction();
                attemptUpdate();
                //初始化List数据
                attemptGetTasks();
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void viewAction() {
        adapter.setOnClickListener(new MainInfoAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (adapter.getMode() == 0) {
                    switch (view.getId()) {
                        case R.id.item_select_task:
                            Intent intent = new Intent(context, DetailsActivity.class);
                            Tasks.position = position;
                            Tasks.pos_copy = -1;
                            startActivity(intent);
                            break;
                        case R.id.btn_copy:
                            if (last_pos != -1 && last_pos != position) {
                                adapter.Refresh_item(last_pos);
                            }
                            DataCopy(position, view);
                            break;
                        case R.id.btn_paste:
                            DataPaste(Tasks.pos_copy, position);
                            break;
                        default:
                            /*Intent intent = new Intent(context, DetailsActivity.class);
                            Tasks.position = position;
                            startActivity(intent);*/
                            break;
                    }
                } else if (adapter.getMode() == 1)

                {
                    if (Tasks.list_task.get(position).getIsSelect() == 0) {
                        Tasks.list_task.get(position).setIsSelect(1);
                    } else if (Tasks.list_task.get(position).getIsSelect() == 1) {
                        Tasks.list_task.get(position).setIsSelect(0);
                    }
                    adapter.changList_add(Tasks.list_task);
                    select_num = 0;
                    for (int i = 0; i < Tasks.list_task.size(); i++) {
                        if (Tasks.list_task.get(i).getIsSelect() == 1)
                            select_num++;
                    }
                    tv_select_num.setText(String.format(getResources().getString(R.string
                            .select_num), String.valueOf(select_num)));
                    setBtnEnabled(select_num);
                }
            }
        });

        /*adapter.setOnLongClickListener(new MainInfoAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                pos = position;
                // 通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        context);
                // 设置Title的图标
                builder.setIcon(R.mipmap.ic_launcher);
                // 设置Title的内容
                // builder.setTitle("弹出警告框");
                // 设置Content来显示一个信息
                builder.setMessage("确定删除？");
                // 设置一个PositiveButton
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                try {
                                    adapter.removeItem(pos);
                                    Snackbar.make(rv_tasks, "删除成功",
                                            Snackbar.LENGTH_LONG).setAction("Action", null)
                                            .show();

                                } catch (Exception e) {
                                    // TODO 自动生成的 catch 块
                                    e.printStackTrace();
                                    Snackbar.make(rv_tasks, "删除失败",
                                            Snackbar.LENGTH_LONG).setAction("Action", null)
                                            .show();
                                }
                            }
                        });
                // 设置一个NegativeButton
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        });
                // 显示出该对话框
                builder.show();
            }
        });*/
    }

    public void attemptGetTasks() {
        /**
         *加载框
         * @param context          上下文
         * @param msg              提示文本
         * @param isVertical
         * @param cancleable       true为可以取消false为不可取消
         * @param outsideTouchable true为可以点击空白区域false为不可点击
         * @param isWhiteBg        true为白色背景false为灰色背景
         */
        final BuildBean dialog_task = DialogUIUtils.showLoading(context, "任务刷新中...", false, true,
                false,
                false);
        dialog_task.show();
        String Emp_No;
        FDA_API request = HttpUtils.JsonApi();
        if (((MyApplication) getApplication()).getTOKEN() == null) {
            token = sharedPreferences.getString("TOKEN", null);
        } else {
            token = ((MyApplication) getApplication()).getTOKEN();
        }
        if (((MyApplication) getApplication()).getNO() == null) {
            Emp_No = sharedPreferences.getString("NO", null);
        } else {
            Emp_No = ((MyApplication) getApplication()).getNO();
        }
        Call<List<Task>> call = request.getTasks(token, Emp_No);
        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.code() == 401) {
                    Log.v("getTasks请求", "token过期");
                    Intent intent_login = new Intent();
                    intent_login.setClass(MainActivity.this,
                            LoginActivity.class);
                    intent_login.putExtra("login_type", 1);
                    startActivity(intent_login);
                } else if (response.code() == 200) {
                    if (response.body() != null) {
                        int num = response.body().size();
                        List<Task> getTasks = response.body();
                        if (num == 0) {
                            Tasks.list_task.clear();
                            adapter.changList_add(Tasks.list_task);
                            Snackbar.make(rv_tasks, "暂时没有检测任务",
                                    Snackbar.LENGTH_LONG).setAction("Action", null)
                                    .show();
                        } else {
                            Tasks.list_task = getTasks;
                            Tasks.init();
                            adapter.changList_add(Tasks.list_task);
                            Snackbar.make(rv_tasks, "更新检测任务成功!",
                                    Snackbar.LENGTH_LONG).setAction("Action", null)
                                    .show();
                        }
                    } else {
                        Log.v("getTasks请求成功!", "response.body is null");
                        Snackbar.make(rv_tasks, "更新检测任务失败!",
                                Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
                DialogUIUtils.dismiss(dialog_task);
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Log.v("getTasks请求失败!", t.getMessage());
                DialogUIUtils.dismiss(dialog_task);
                Snackbar.make(rv_tasks, "检测任务请求失败!",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    public void attemptUpdate() {
        //创建 网络请求接口 的实例
        FDA_API request = HttpUtils.XmlApi();
        Call<UpdateInfo> call = request.UpdateXML();
        call.enqueue(new Callback<UpdateInfo>() {
            @Override
            public void onResponse(Call<UpdateInfo> call, Response<UpdateInfo> response) {
                if (response.body() != null) {
                    info = response.body();
                    /*try {
                        versionName = getPackageManager().getPackageInfo(getPackageName(), 0)
                                .versionName;
                    } catch (NameNotFoundException e) {
                        e.printStackTrace();
                    }*/
                    if (Double.parseDouble(info.getVersion()) > Double
                            .parseDouble(versionName)) {
                        Log.i(TAG_UPDATE, "服务器版本号大于本地 ,提示用户升级 ");
                        showUpdateDialog();
                    } else if (Double.parseDouble(info.getVersion()) == Double
                            .parseDouble(versionName)) {
                        Log.i(TAG_UPDATE, "无需升级");
                    }
                } else {
                    Log.v("update请求成功!", "response.body is null");
                }
            }

            @Override
            public void onFailure(Call<UpdateInfo> call, Throwable t) {
                Log.v("update请求失败!", t.getMessage());
            }
        });
    }

    public void select_all() {
        if (tv_select_all.getText().equals("全选")) {
            Tasks.select_all();
            tv_select_all.setText("取消全选");
        } else if (tv_select_all.getText().equals("取消全选")) {
            Tasks.select_clear();
            tv_select_all.setText("全选");
        }
        adapter.changList_add(Tasks.list_task);
        select_num = 0;
        for (int i = 0; i < Tasks.list_task.size(); i++) {
            if (Tasks.list_task.get(i).getIsSelect() == 1)
                select_num++;
        }
        tv_select_num.setText("" + select_num);
        setBtnEnabled(select_num);
    }

    public void select_invert() {
        Tasks.select_invert();
        adapter.changList_add(Tasks.list_task);
        select_num = 0;
        for (int i = 0; i < Tasks.list_task.size(); i++) {
            if (Tasks.list_task.get(i).getIsSelect() == 1)
                select_num++;
        }
        tv_select_num.setText("" + select_num);
        setBtnEnabled(select_num);
    }

    public void select_confirm() {
        if (select_num == 0) {
            btn_select_confirm.setEnabled(false);
            return;
        }
        BuildBean dialog_loading = DialogUIUtils.showLoading(context, "加载中...", false, true, false,
                false);
        dialog_loading.show();
        String applyNo = "";
        for (int i = 0; i < Tasks.list_task.size(); i++) {
            if (Tasks.list_task.get(i).getIsSelect() == 1)
                if (applyNo.equals("")) {
                    applyNo = Tasks.list_task.get(i).getNO();
                } else {
                    applyNo = applyNo + "," + Tasks.list_task.get(i).getNO();
                }
        }
        if (tv_select_title.getText().equals("付款凭证")) {
            attemptReportServer(1, applyNo, dialog_loading);
        } else if (tv_select_title.getText().equals("样品标签")) {
            attemptReportServer(2, applyNo, dialog_loading);
        } else if (tv_select_title.getText().equals("反馈单")) {
            attemptReportServer(3, applyNo, dialog_loading);
        } else if (tv_select_title.getText().equals("告知书")) {
            attemptReportServer(4, applyNo, dialog_loading);
        }
        /*else if (tv_select_title.getText().equals("反馈单")) {
            attemptGetSamplingFeedback(applyNo, dialog_loading);
        } else if (tv_select_title.getText().equals("告知书")) {
            attemptGetSamplingToldbook(applyNo, dialog_loading);
        }*/
    }

    private void setBtnEnabled(int select_num) {
        if (select_num != 0) {
            btn_select_confirm.setBackgroundResource(R.drawable.button_brown_bright);
            btn_select_confirm.setEnabled(true);
            btn_select_confirm.setTextColor(Color.WHITE);
        } else {
            btn_select_confirm.setBackgroundResource(R.drawable.button_brown_dark);
            btn_select_confirm.setEnabled(false);
            btn_select_confirm.setTextColor(ContextCompat.getColor(this, R.color.color_b7b8bd));
        }
    }

    private void attemptReportServer(int type, String applyNo, final BuildBean dialog) {
        String pdf_path = null, reportlet = null;
        if (type == 1) {
            pdf_path = Environment.getExternalStorageDirectory() + "/FDA/payment/" + applyNo +
                    ".pdf";
            reportlet = "付款凭证.cpt";
        } else if (type == 2) {
            pdf_path = Environment.getExternalStorageDirectory() + "/FDA/sampletag/" + applyNo +
                    ".pdf";
            reportlet = "样品标签.cpt";
        } else if (type == 3) {
            pdf_path = Environment.getExternalStorageDirectory() + "/FDA/Feedback/" + applyNo +
                    ".pdf";
            reportlet = "反馈单.cpt";
        } else if (type == 4) {
            pdf_path = Environment.getExternalStorageDirectory() + "/FDA/Toldbook/" + applyNo +
                    ".pdf";
            reportlet = "告知书.cpt";
        }

        final File file_pdf = new File(pdf_path);
        if (file_pdf.exists()) {
            Log.v("pdf", "已经存在");
            file_pdf.delete();
        }
        if (NetworkUtil.isNetworkAvailable(context)) {
            FDA_API request = HttpUtils.ReportApi();
            Map<String, String> params = new HashMap<>();
            params.put("reportlet", reportlet);
            params.put("applyNo", applyNo);
            params.put("op", "export");
            params.put("format", "pdf");
            Call<ResponseBody> call = request.ReportServer(params);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            try {
                                // 获取文件的输出流对象
                                FileOutputStream outStream = new FileOutputStream(file_pdf);
                                // 获取字符串对象的byte数组并写入文件流
                                outStream.write(response.body().bytes());
                                // 最后关闭文件输出流
                                outStream.close();
                                Log.v("pdf", "下载成功");
                                doOpenWord(file_pdf);
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
                    DialogUIUtils.dismiss(dialog);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.v("Report请求失败!", t.getMessage());
                    DialogUIUtils.dismiss(dialog);
                }
            });
        } else {
            DialogUIUtils.dismiss(dialog);
            Snackbar.make(toolbar, "当前无网络", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    public void attemptGetSamplingFeedback(String applyNo, final BuildBean dialog) {
        String feedback_path = Environment.getExternalStorageDirectory() + "/FDA/Feedback/" +
                applyNo + ".pdf";
        final File feedback_pdf = new File(feedback_path);
        if (feedback_pdf.exists()) {
            Log.v("pdf", "已经存在");
            feedback_pdf.delete();
        }
        if (NetworkUtil.isNetworkAvailable(context)) {
            if (((MyApplication) getApplication()).getTOKEN() == null) {
                token = sharedPreferences.getString("TOKEN", null);
            } else {
                token = ((MyApplication) getApplication()).getTOKEN();
            }
            FDA_API request = HttpUtils.JsonApi();
            Call<ResponseBody> call = request.getSamplingFeedback(token, applyNo);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 401) {
                        Log.v("Feedback请求", "token过期");
                        Intent intent_login = new Intent();
                        intent_login.setClass(MainActivity.this, LoginActivity.class);
                        intent_login.putExtra("login_type", 1);
                        startActivity(intent_login);
                    } else if (response.code() == 200) {
                        if (response.body() != null) {
                            try {
                                // 获取文件的输出流对象
                                FileOutputStream outStream = new FileOutputStream(feedback_pdf);
                                // 获取字符串对象的byte数组并写入文件流
                                outStream.write(response.body().bytes());
                                // 最后关闭文件输出流
                                outStream.close();
                                Log.v("pdf", "下载成功");
                                doPrintPdf(feedback_pdf);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Log.v("ResponseBody", "FileNotFoundException");
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.v("ResponseBody", "IOException");
                            }
                        } else {
                            Log.v("Feedback请求成功!", "response.body is null");
                            Snackbar.make(toolbar, "获取失败!(请求成功)",
                                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    }
                    DialogUIUtils.dismiss(dialog);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.v("Feedback请求失败!", t.getMessage());
                    DialogUIUtils.dismiss(dialog);
                    /*Snackbar.make(toolbar, "获取失败!(请求失败)",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();*/
                    Snackbar.make(toolbar, "获取失败!(只能选择一条数据)",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });
        } else {
            DialogUIUtils.dismiss(dialog);
            Snackbar.make(toolbar, "当前无网络",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    public void attemptGetSamplingToldbook(String applyNo, final BuildBean dialog) {
        String feedback_path = Environment.getExternalStorageDirectory() + "/FDA/Toldbook/" +
                applyNo + ".pdf";
        final File toldbook_pdf = new File(feedback_path);
        if (toldbook_pdf.exists()) {
            Log.v("pdf", "已经存在");
            toldbook_pdf.delete();
        }
        if (NetworkUtil.isNetworkAvailable(context)) {
            if (((MyApplication) getApplication()).getTOKEN() == null) {
                token = sharedPreferences.getString("TOKEN", null);
            } else {
                token = ((MyApplication) getApplication()).getTOKEN();
            }
            FDA_API request = HttpUtils.JsonApi();
            Call<ResponseBody> call = request.getSamplingToldbook(token, applyNo);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody>
                        response) {
                    if (response.code() == 401) {
                        Log.v("Toldbook请求", "token过期");
                        Intent intent_login = new Intent();
                        intent_login.setClass(MainActivity.this, LoginActivity.class);
                        intent_login.putExtra("login_type", 1);
                        startActivity(intent_login);
                    } else if (response.code() == 200) {
                        if (response.body() != null) {
                            try {
                                // 获取文件的输出流对象
                                FileOutputStream outStream = new FileOutputStream(toldbook_pdf);
                                // 获取字符串对象的byte数组并写入文件流
                                outStream.write(response.body().bytes());
                                // 最后关闭文件输出流
                                outStream.close();
                                Log.v("pdf", "下载成功");
                                doPrintPdf(toldbook_pdf);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Log.v("ResponseBody", "FileNotFoundException");
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.v("ResponseBody", "IOException");
                            }
                        } else {
                            Log.v("Toldbook请求成功!", "response.body is null");
                            Snackbar.make(toolbar, "获取失败!(请求成功)",
                                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    }
                    DialogUIUtils.dismiss(dialog);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.v("Toldbook请求失败!", t.getMessage());
                    DialogUIUtils.dismiss(dialog);
                    /*Snackbar.make(toolbar, "获取失败!(请求失败)",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();*/
                    Snackbar.make(toolbar, "获取失败!(只能选择一条数据)",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });
        } else {
            DialogUIUtils.dismiss(dialog);
            Snackbar.make(toolbar, "当前无网络",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    //调用手机中安装的可打开word的软件
    private void doOpenWord(final File file_pdf) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setDataAndType(Uri.fromFile(file_pdf), "application/pdf");
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    MainActivity.this.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    /* 检测到系统尚未安装OliveOffice的apk程序*/
                    Snackbar.make(rv_tasks, "未找到可用软件",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        }, 1500);
        /* 延时1.5s执行*/
    }

    private void doPrintPdf(final File file_pdf) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (appIsInstalled(context, "com.dynamixsoftware.printershare")) {
                    Intent intent = new Intent();
                    ComponentName comp = new ComponentName("com.dynamixsoftware.printershare",
                            "com.dynamixsoftware.printershare.ActivityPrintDocuments");
                    intent.setComponent(comp);
                    intent.setAction("android.intent.action.VIEW");
                    intent.setType("application/pdf");
                    intent.setData(Uri.fromFile(file_pdf));
                    startActivity(intent);
                } else {
                    Snackbar.make(rv_tasks, "未找到PrinterShare软件",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    /* 安装apk*/
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File file = getAssetFileToCacheDir(context, "PrinterShare.apk");
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd" +
                            ".android.package-archive");
                    MainActivity.this.startActivity(intent);
                }
            }
        }, 1000); /* 延时1s执行*/
    }

    // 判断apk是否安装
    public static boolean appIsInstalled(Context context, String pageName) {
        try {
            context.getPackageManager().getPackageInfo(pageName, 0);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    // 把Asset下的apk拷贝到sdcard下 /Android/data/你的包名/cache 目录下
    public File getAssetFileToCacheDir(Context context, String fileName) {
        try {
            File cacheDir = getCacheDir(context);
            final String cachePath = cacheDir.getAbsolutePath() + File.separator + fileName;
            InputStream is = context.getAssets().open(fileName);
            File file = new File(cachePath);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) fos.write(temp, 0, i);
            fos.close();
            is.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取sdcard中的缓存目录
    public static File getCacheDir(Context context) {
        String APP_DIR_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Android/data/";
        File dir = new File(APP_DIR_NAME + context.getPackageName() + "/cache/");
        if (!dir.exists()) dir.mkdirs();
        return dir;
    }

    private void ShareAppCode() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.item_img_add, (ViewGroup) findViewById(R
                .id.dialog_layout));
        ImageView imageview = layout.findViewById(R.id.imageView);
        imageview.setImageResource(R.mipmap.appcode);
        AlertDialog.Builder dialog_img = new AlertDialog.Builder(context).setView(layout)
                .setPositiveButton("确定", null);
        dialog_img.show();
    }

    /* 弹出更新对话框 */
    public void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog_update);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_update, null);
        TextView tv_title = v.findViewById(R.id.tv_title);
        TextView tv_msg = v.findViewById(R.id.tv_msg);
        Button btn_commit = v.findViewById(R.id.btn_commit);
        tv_title.setText(String.format(getResources().getString(R.string.update_title), info
                .getVersion()));
        tv_msg.setText(info.getDescription());
        //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        dialog.setCanceledOnTouchOutside(false);//点击对话框以外的区域，对话框不消失
        dialog.setCancelable(false);//点击返回键，对话框不消失
        btn_commit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                File apkFile = getExternalFilesDir("DownLoad/FDA_sampling.apk");
                if (apkFile.exists()) {
                    apkFile.delete();
                }
                downloadApp();
            }
        });
    }

    public void downloadApp() {
        //此处使用DownLoadManager开启下载任务
        DownloadManager mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(info.getUrl()));
        // 下载过程和下载完成后通知栏有通知消息。
        request.setNotificationVisibility(DownloadManager.Request
                .VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("下载");
        request.setDescription("apk正在下载");
        //设置保存目录  /storage/emulated/0/Android/包名/files/Download
        request.setDestinationInExternalFilesDir(MainActivity.this, Environment
                .DIRECTORY_DOWNLOADS, "FDA_sampling.apk");
        mId = mDownloadManager.enqueue(request);

        //注册内容观察者，实时显示进度
        MainActivity.MyContentObserver downloadChangeObserver = new MainActivity
                .MyContentObserver(null);
        getContentResolver().registerContentObserver(Uri.parse
                ("content://downloads/my_downloads"), true, downloadChangeObserver);

        //广播监听下载完成
        listener(mId);
        //弹出进度条，先隐藏前一个dialog
        //dialog.dismiss();
        //显示进度的对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        pd.setCancelable(false);// 设置是否可以通过点击Back键取消
        pd.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        pd.setIcon(R.mipmap.logo);// 设置提示的title的图标，默认是没有的
        pd.setTitle("提示");
        pd.setMessage("玩命儿下载中,请稍后...");
        pd.show();
    }

    // 安装apk
    public void installApk(File file) {

        if (file != null) {   // file 即 apk文件
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
            if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri apkUri =
                        FileProvider.getUriForFile(context, "com.fda_sampling.fileprovider",
                                file);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.android.package-archive");
            }
            startActivity(intent);
            finish();
        }
    }

    private void listener(final long id) {
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final File apkFile = getExternalFilesDir("DownLoad/FDA_sampling.apk");
                long longExtra = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id == longExtra) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            installApk(apkFile);
                        }
                    }, 1000); /* 延时1s执行*/
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    public class MyContentObserver extends ContentObserver {

        private MyContentObserver(Handler handler) {
            super(handler);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public void onChange(boolean selfChange) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(mId);
            DownloadManager dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            final Cursor cursor = dManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                final int totalColumn = cursor.getColumnIndex(DownloadManager
                        .COLUMN_TOTAL_SIZE_BYTES);
                final int currentColumn = cursor.getColumnIndex(DownloadManager
                        .COLUMN_BYTES_DOWNLOADED_SO_FAR);
                int totalSize = cursor.getInt(totalColumn);
                int currentSize = cursor.getInt(currentColumn);
                float percent = (float) currentSize / (float) totalSize;
                float progress = (float) Math.floor(percent * 100);
                pd.setProgress((int) progress);
                if (progress == 100) {
                    pd.dismiss();
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_invert:
                select_invert();
                break;
            case R.id.tv_select_all:
                select_all();
                break;
            case R.id.btn_select_confirm:
                select_confirm();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                // Object mHelperUtils;
                Snackbar.make(rv_tasks, "再按一次退出采样平台",
                        Snackbar.LENGTH_LONG).setAction("Action", null)
                        .show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                isHome = 1;
                // 通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        context);
                // 设置Title的图标
                builder.setIcon(R.mipmap.ic_launcher);
                // 设置Title的内容
                // builder.setTitle("弹出警告框");
                // 设置Content来显示一个信息
                builder.setMessage("确定切换账号？");
                // 设置一个PositiveButton
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent intent_login = new Intent();
                                intent_login.setClass(MainActivity.this,
                                        LoginActivity.class);
                                intent_login.putExtra("login_type", -1);
                                startActivity(intent_login);
                            }
                        });
                // 设置一个NegativeButton
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        });
                // 显示出该对话框
                builder.show();
            case R.id.action_refresh:
                if (isHome == 0) {
                    if (ClickUtil.isFastClick()) {
                        attemptGetTasks();
                    } else {
                        Snackbar.make(rv_tasks, "刷新太快了，请稍后再试",
                                Snackbar.LENGTH_LONG).setAction("Action", null)
                                .show();
                    }
                }
                isHome = 0;
                break;
            case R.id.action_payment:
                dialog_select.setVisibility(View.VISIBLE);
                tv_select_title.setText("付款凭证");
                adapter.setMode(1);
                toolbar.getMenu().findItem(R.id.action_refresh).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_cancel).setVisible(true);
                break;
            case R.id.action_sampleTag:
                dialog_select.setVisibility(View.VISIBLE);
                tv_select_title.setText("样品标签");
                adapter.setMode(1);
                toolbar.getMenu().findItem(R.id.action_refresh).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_cancel).setVisible(true);
                break;
            case R.id.action_SamplingFeedback:
                dialog_select.setVisibility(View.VISIBLE);
                tv_select_title.setText("反馈单");
                adapter.setMode(1);
                toolbar.getMenu().findItem(R.id.action_refresh).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_cancel).setVisible(true);
                break;
            case R.id.action_Notification:
                dialog_select.setVisibility(View.VISIBLE);
                tv_select_title.setText("告知书");
                adapter.setMode(1);
                toolbar.getMenu().findItem(R.id.action_refresh).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_cancel).setVisible(true);
                break;
            case R.id.action_cancel:
                tv_select_num.setText("0");
                dialog_select.setVisibility(View.GONE);
                adapter.setMode(0);
                toolbar.getMenu().findItem(R.id.action_refresh).setVisible(true);
                toolbar.getMenu().findItem(R.id.action_cancel).setVisible(false);
                Tasks.init();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_change_account:
                isHome = 1;
                // 通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        context);
                // 设置Title的图标
                builder.setIcon(R.mipmap.ic_launcher);
                // 设置Title的内容
                // builder.setTitle("弹出警告框");
                // 设置Content来显示一个信息
                builder.setMessage("确定切换账号？");
                // 设置一个PositiveButton
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent intent_login = new Intent();
                                intent_login.setClass(MainActivity.this,
                                        LoginActivity.class);
                                intent_login.putExtra("login_type", -1);
                                startActivity(intent_login);
                            }
                        });
                // 设置一个NegativeButton
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        });
                // 显示出该对话框
                builder.show();
                break;
            case R.id.nav_task_query:
                startActivity(new Intent(this, TaskQueryActivity.class));
                break;
            case R.id.nav_share:
                ShareAppCode();
                break;
            /*case R.id.nav_send:
                //startActivity(new Intent(this, testActivity.class));
                Snackbar.make(rv_tasks, "暂无此功能",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
                break;*/
            default:
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.changList_add(Tasks.list_task);
    }

    public void DataCopy(int pos, View view) {
        Task task_copy = Tasks.list_task.get(pos);
        Gson gson = new Gson();
        String data = gson.toJson(task_copy);
        FileOutputStream out;
        BufferedWriter writer = null;
        try {
            // MODE_PRIVATE 覆盖内容
            // MODE_APPEND 追加内容
            out = openFileOutput("data.txt", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(data);
            Tasks.pos_copy = last_pos = pos;
            ((Button) view).setText("已复制");
            Snackbar.make(toolbar, "复制成功", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void DataPaste(int c, int p) {
        if (c == -1) {
            Snackbar.make(toolbar, "请先选择要复制的数据", Snackbar.LENGTH_LONG).setAction("Action", null)
                    .show();
        } else if (c == p) {
            Snackbar.make(toolbar, "复制的是同一条数据", Snackbar.LENGTH_LONG).setAction("Action", null)
                    .show();
        } else {
            Tasks.Task_copy(c, p);
            adapter.Refresh_all();
            Tasks.pos_copy = -1;
            Snackbar.make(toolbar, "粘贴成功", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }
}