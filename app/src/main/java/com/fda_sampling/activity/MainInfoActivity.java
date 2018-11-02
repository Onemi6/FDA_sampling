package com.fda_sampling.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.fda_sampling.R;
import com.fda_sampling.model.InfoList;
import com.fda_sampling.model.Info_Detail;
import com.fda_sampling.util.CrashHandler;
import com.fda_sampling.util.FileRW;
import com.leon.lfilepickerlibrary.LFilePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainInfoActivity extends AppCompatActivity {

    private int pos;
    private long mExitTime;
    private FloatingActionButton fab;
    private RecyclerView rv_add;
    private LinearLayoutManager layoutmanager;
    private MaininfoAdapter adapter;
    private List<Info_Detail> list_add = new ArrayList<Info_Detail>();

    int REQUESTCODE_FROM_ACTIVITY = 1000;
    //一个整形常量
    public static final int MY_PERMISSIONS_REQUEST = 3000;
    //定义一个list，用于存储需要申请的权限
    private ArrayList<String> permissionList = new ArrayList<>();
    private List<String> paths = new ArrayList<>();
    private String filepath = null;
    private Context context;
    private TextView tv_filename;

    private SharedPreferences ishavefile;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_info);
        context = this;

        permissionList.add(Manifest.permission.INTERNET);
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //permissionList.add(Manifest.permission.CAMERA);
        checkAndRequestPermissions(permissionList);

        ishavefile = getSharedPreferences("filepath", MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CrashHandler.getInstance().init(context);
        fab = findViewById(R.id.fab);
        tv_filename = findViewById(R.id.tv_filename);
        //初始化RecyclerView
        rv_add = findViewById(R.id.rv_maininfo_add);
        //rv_add.setHasFixedSize(true);

        //创建LinearLayoutManager 对象 这里使用 LinearLayoutManager 是线性布局的意思
        layoutmanager = new LinearLayoutManager(this);
        //设置RecyclerView 布局
        rv_add.setLayoutManager(layoutmanager);
        //设置Adapter
        adapter = new MaininfoAdapter(this, list_add);
        rv_add.setAdapter(adapter);
        adapter.setOnClickListener(new MaininfoAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(context, DetailsActivity.class);
                InfoList.position = position;
                startActivity(intent);
            }
        });

        adapter.setOnLongClickListener(new MaininfoAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                //Snackbar.make(view, "长按了第" + (position + 1) + "行", Snackbar.LENGTH_LONG)
                //   .setAction("Action", null).show();
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
                                    Toast.makeText(context, "删除成功",
                                            Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    // TODO 自动生成的 catch 块
                                    e.printStackTrace();
                                    Toast.makeText(context, "删除失败",
                                            Toast.LENGTH_LONG).show();
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LFilePicker()
                        .withActivity(MainInfoActivity.this)
                        .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                        .withTitle("选择一个SMPLJSON文件")//标题文字
                        .withStartPath("/storage/emulated/0/Download")//指定初始显示路径
                        .withMutilyMode(false) //单选
                        .withFileFilter(new String[]{".SMPLJSON"})//限制文件类型
                        .start();
            }
        });
        //初始化List数据
        initdata();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_ACTIVITY) {
                //如果是文件选择模式，需要获取选择的所有文件的路径集合
                paths = data.getStringArrayListExtra("paths");
                Log.v("paths", paths.get(0));
                filepath = paths.get(0);
                //如果是文件夹选择模式，需要获取选择的文件夹路径
                /*String path = data.getStringExtra("path");
                Toast.makeText(getApplicationContext(), "选中的路径为" + path, Toast.LENGTH_SHORT).show
                ();*/
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (filepath != null) {
            //判断是否为SMPLJSON文件
            /*用File的getName()方法，获取到文件名后，
            用String的endsWith(".java")方法判断是否问指定文件类型。*/
            File f = new File(filepath);
            String fileName = f.getName();
            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
            //Log.v("文件后缀", prefix);
            if (prefix.equals("SMPLJSON")) {
                list_add = FileRW.readFile(filepath);
                if (list_add == null) {
                    InfoList.outpath = null;
                    Snackbar.make(fab, "文件选择失败", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    InfoList.list_info = list_add;
                    adapter.changList_add(list_add);
                    InfoList.outpath = filepath;
                    tv_filename.setText(filepath);
                    //保存选择的文件路径
                    editor = ishavefile.edit();
                    editor.putString("filepath", filepath);
                    editor.commit();
                    Snackbar.make(fab, "文件选择成功", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            } else {
                InfoList.outpath = null;
                Toast.makeText(this, "请选择一个SMPLJSON文件", Toast.LENGTH_LONG).show();
            }
            filepath = null;
        }
    }

    public void initdata() {

        if (ishavefile != null && ishavefile.getString("filepath", "") != "") {
            list_add = FileRW.readFile(ishavefile.getString("filepath", ""));
            if (list_add == null) {
                filepath = null;
                InfoList.outpath = null;
            } else {
                /*File f = new File(filepath);
                String fileName = f.getName();*/
                InfoList.list_info = list_add;
                adapter.changList_add(list_add);
                InfoList.outpath = ishavefile.getString("filepath", "");
                tv_filename.setText(ishavefile.getString("filepath", ""));
                filepath = null;
            }
        }
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
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}