package com.fda_sampling.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fda_sampling.R;
import com.fda_sampling.model.Task;
import com.fda_sampling.model.Tasks;
import com.fda_sampling.model.UpdateInfo;
import com.fda_sampling.service.FDA_API;
import com.fda_sampling.service.HttpUtils;
import com.fda_sampling.util.ClickUtil;
import com.fda_sampling.util.CrashHandler;
import com.fda_sampling.util.MyApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private int pos;
    private long mExitTime;
    private RecyclerView rv_tasks;
    private ProgressBar mProgressView;
    private LinearLayoutManager layoutmanager;
    private MaininfoAdapter adapter;
    public static final int MY_PERMISSIONS_REQUEST = 3000;
    private String name, token, Emp_No, TAG_UPDATE = "update";
    private Context context;
    private SharedPreferences sharedPreferences;
    private UpdateInfo info;
    private ProgressDialog pd;
    private long mId;

    //定义一个list，用于存储需要申请的权限
    private ArrayList<String> permissionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        context = this;
        Toolbar toolbar = findViewById(R.id.toolbar_maininfo);
        mProgressView = findViewById(R.id.tasks_progress);
        sharedPreferences = getSharedPreferences("Info", MODE_PRIVATE);
        name = sharedPreferences.getString("NAME", null);
        if (name == null) {
            Intent intent_login = new Intent();
            intent_login.setClass(MainActivity.this, LoginActivity.class);
            intent_login.putExtra("login_type", -1);
            finish();
            startActivity(intent_login);
        } else {
            toolbar.setSubtitle(name);
            //toolbar.setTitle("未登录");
            //toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            //设置toolbar
            setSupportActionBar(toolbar);
            //左边的小箭头（注意需要在setSupportActionBar(toolbar)之后才有效果）
            //toolbar.setNavigationIcon(R.drawable.ic_account_circle_white);
            //菜单点击事件（注意需要在setSupportActionBar(toolbar)之后才有效果）
            //toolbar.setOnMenuItemClickListener(onMenuItemClick);

            CrashHandler.getInstance().init(context);
            //初始化RecyclerView
            rv_tasks = findViewById(R.id.rv_maininfo_add);
            //rv_tasks.setHasFixedSize(true);

            //创建LinearLayoutManager 对象 这里使用 LinearLayoutManager 是线性布局的意思
            layoutmanager = new LinearLayoutManager(this);
            //设置RecyclerView 布局
            rv_tasks.setLayoutManager(layoutmanager);
            //设置Adapter
            adapter = new MaininfoAdapter(this, Tasks.list_task);
            rv_tasks.setAdapter(adapter);
            adapter.setOnClickListener(new MaininfoAdapter.OnClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    Tasks.position = position;
                    startActivity(intent);
                }
            });

            adapter.setOnLongClickListener(new MaininfoAdapter.OnLongClickListener() {
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
            });
            attempUpdate();
            getPermission();
            //初始化List数据
            attempgetTasks();
        }
    }

    public void attempgetTasks() {
        mProgressView.setVisibility(View.VISIBLE);
        FDA_API request = HttpUtils.GsonApi();
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
                    mProgressView.setVisibility(View.GONE);
                    startActivity(intent_login);
                } else if (response.code() == 200) {
                    if (response.body() != null) {
                        int num = response.body().size();
                        List<Task> getTasks;
                        getTasks = response.body();
                        if (num == 0) {
                            Tasks.list_task.clear();
                            adapter.changList_add(Tasks.list_task);
                            Snackbar.make(rv_tasks, "暂时没有检测任务",
                                    Snackbar.LENGTH_LONG).setAction("Action", null)
                                    .show();
                        } else {
                            Tasks.list_task = getTasks;
                            adapter.changList_add(Tasks.list_task);
                            Snackbar.make(rv_tasks, "更新检测任务成功!",
                                    Snackbar.LENGTH_LONG).setAction("Action", null)
                                    .show();
                        }
                    } else {
                        Log.v("getTasks请求成功!", "response.body is null");
                        Snackbar.make(rv_tasks, "更新检测任务失败!",
                                Snackbar.LENGTH_LONG).setAction("Action", null)
                                .show();
                    }
                }
                mProgressView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Log.v("getTasks请求失败!", t.getMessage());
                Snackbar.make(rv_tasks, "更新检测任务失败!",
                        Snackbar.LENGTH_LONG).setAction("Action", null)
                        .show();
                mProgressView.setVisibility(View.GONE);
            }
        });
    }

    public void attempUpdate() {
        //创建 网络请求接口 的实例
        FDA_API request = HttpUtils.XmlApi();
        Call<UpdateInfo> call = request.UpdateXML();
        call.enqueue(new Callback<UpdateInfo>() {
            @Override
            public void onResponse(Call<UpdateInfo> call, Response<UpdateInfo> response) {
                if (response.body() != null) {
                    try {
                        /* 获取packagemanager的实例*/
                        PackageManager packageManager = getPackageManager();
                        /* getPackageName()是你当前类的包名，0代表是获取版本信息*/
                        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
                        String versionname = packInfo.versionName;
                        info = response.body();
                        if (Double.parseDouble(info.getVersion()) > Double
                                .parseDouble(versionname)) {
                            Log.i(TAG_UPDATE, "服务器版本号大于本地 ,提示用户升级 ");
                            showUpdataDialog();
                        } else if (Double.parseDouble(info.getVersion()) == Double
                                .parseDouble(versionname)) {
                            Log.i(TAG_UPDATE, "无需升级");
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        Log.i(TAG_UPDATE, "获取APP版本出错");
                        e.printStackTrace();
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

    /* 弹出更新对话框 */
    public void showUpdataDialog() {
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

    public void getPermission() {
        permissionList.add(Manifest.permission.INTERNET);
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.CAMERA);
        checkAndRequestPermissions(permissionList);
    }

    //调用封装好的申请权限的方法
    private void checkAndRequestPermissions(ArrayList<String> permissionList) {
        ArrayList<String> list = new ArrayList<>(permissionList);
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String permission = it.next();
            //检查权限是否已经申请
            int hasPermission = ContextCompat.checkSelfPermission(this, permission);
            if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                it.remove();
            }
        }
        /**
         *补充说明：ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
         * .RECORD_AUDIO);
         *对于原生Android，如果用户选择了“不再提示”，那么shouldShowRequestPermissionRationale就会为true。
         *此时，用户可以弹出一个对话框，向用户解释为什么需要这项权限。
         *对于一些深度定制的系统，如果用户选择了“不再提示”，那么shouldShowRequestPermissionRationale永远为false
         *
         */
        if (list.size() == 0) {
            return;
        }
        String[] permissions = list.toArray(new String[0]);
        //正式请求权限
        ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            /*case R.id.action_scanner:
                Intent intent_scan = new Intent();
                intent_scan.setClass(MainActivity.this,
                        TestActivity.class);
                //finish();// 结束当前活动
                startActivity(intent_scan);
                break;*/
            case R.id.action_refresh:
                if (ClickUtil.isFastClick()) {
                    attempgetTasks();
                } else {
                    Snackbar.make(rv_tasks, "刷新太快了，请稍后再试",
                            Snackbar.LENGTH_LONG).setAction("Action", null)
                            .show();
                }
                break;
            case android.R.id.home:
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
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.changList_add(Tasks.list_task);
    }
}