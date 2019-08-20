package com.fda_sampling.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.fda_sampling.R;
import com.fda_sampling.model.LoginStatus;
import com.fda_sampling.service.FDA_API;
import com.fda_sampling.service.HttpUtils;
import com.fda_sampling.util.MyApplication;
import com.fda_sampling.util.NetworkUtil;
import com.fda_sampling.util.RSAUtil;

import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    private EditText et_mAccount, et_mPassword;
    private CheckBox remember_password;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Button btn_login;
    private String account, password;
    private int login_type;
    private Context _context;
    public static final int MY_PERMISSIONS_REQUEST = 3000;
    //定义一个list，用于存储需要申请的权限
    private ArrayList<String> permissionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _context = this;
        Intent intent = getIntent();
        login_type = intent.getIntExtra("login_type", 0);
        sharedPreferences = getSharedPreferences("Info", MODE_PRIVATE);
        initView();
        getPermission();
        FileDir();
        if (login_type == 1) {
            account = sharedPreferences.getString("Login_Name", null);
            password = sharedPreferences.getString("Password", null);
            attemptLogin();
        }
        btn_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                account = et_mAccount.getText().toString();
                password = et_mPassword.getText().toString();
                if (TextUtils.isEmpty(account)) {
                    et_mAccount.requestFocus();
                    et_mAccount.setError("账号不能为空");
                } else {
                    et_mAccount.setError(null);
                    if (TextUtils.isEmpty(password)) {
                        et_mPassword.requestFocus();
                        et_mPassword.setError("密码不能为空");
                    } else {
                        et_mPassword.setError(null);
                        if (NetworkUtil.isNetworkAvailable(_context)) {
                            attemptLogin();
                        } else {
                            Snackbar.make(btn_login, "当前网络不可用",
                                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    }
                }
            }
        });
    }

    private void initView() {
        et_mAccount = findViewById(R.id.account);
        et_mPassword = findViewById(R.id.password);
        remember_password = findViewById(R.id.remember_password);
        btn_login = findViewById(R.id.btn_login);
        if (sharedPreferences != null) {
            boolean isRemember = sharedPreferences.getBoolean("isRemember", false);
            if (isRemember) {
                et_mAccount.setText(sharedPreferences.getString("Login_Name", null));
                et_mPassword.setText(sharedPreferences.getString("Password", null));
                remember_password.setChecked(true);
            } else {
                et_mAccount.setText(sharedPreferences.getString("Login_Name", null));
                //et_mPassword.setText(sharedPreferences.getString("Password", null));
                remember_password.setChecked(false);
            }
        }
    }

    private void attemptLogin() {
        final BuildBean dialog_login = DialogUIUtils.showLoading(_context, "登录中...", false, true,
                false,
                false);
        dialog_login.show();
        try {
            PublicKey publicKey = RSAUtil.loadPublicKey(RSAUtil.PUCLIC_KEY);
            // 加密
            byte[] encryptByte = RSAUtil.encryptData(password.getBytes(), publicKey);
            // 为了方便观察吧加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
            String password_afterEncrypt = Base64.encodeToString(encryptByte, Base64.DEFAULT);
            String data = "{\"Login_Name\":\"" + account + "\"," + "\"Password\":\"" +
                    password_afterEncrypt + "\"}";
            FDA_API request = HttpUtils.JsonApi();
            Call<LoginStatus> call = request.Login(data);
            call.enqueue(new Callback<LoginStatus>() {
                @Override
                public void onResponse(Call<LoginStatus> call, Response<LoginStatus> response) {
                    if (response.body() != null) {
                        Log.v("Login请求成功!", "response.body() is not null");
                        if (response.body().getMESSAGE() == null) {
                            editor = sharedPreferences.edit();
                            if (remember_password.isChecked()) {
                                editor.putBoolean("isRemember", true);
                                editor.putString("Login_Name", account);
                                editor.putString("Password", password);
                                editor.putString("TOKEN", response.body().getTOKEN());
                                editor.putString("NAME", response.body().getNAME());
                                editor.putString("NO", response.body().getNO());
                            } else {
                                editor.putBoolean("isRemember", false);
                                editor.putString("Login_Name", account);
                                editor.putString("Password", password);
                                editor.putString("TOKEN", response.body().getTOKEN());
                                editor.putString("NAME", response.body().getNAME());
                                editor.putString("NO", response.body().getNO());
                            }
                            editor.apply();
                            ((MyApplication) getApplication()).setTOKEN(response.body()
                                    .getTOKEN());
                            ((MyApplication) getApplication()).setNAME(response.body()
                                    .getNAME());
                            ((MyApplication) getApplication()).setNO(response.body()
                                    .getNO());
                            if (login_type == 1) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 500); // 延时1s执行
                            } else if (login_type == -1) {
                                DialogUIUtils.dismiss(dialog_login);
                                Snackbar.make(btn_login, "登录成功", Snackbar.LENGTH_LONG).setAction
                                        ("Action", null).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(LoginActivity.this,
                                                MainActivity.class));
                                        finish();
                                    }
                                }, 3000);  //延时3s执行
                            }
                        } else {
                            et_mAccount.requestFocus();
                            et_mAccount.setError(response.body().getMESSAGE());
                        }
                    } else {
                        Log.v("Login请求成功!", "response.body() is null");
                        Snackbar.make(btn_login, "登录失败(请求成功)", Snackbar.LENGTH_LONG).setAction
                                ("Action", null).show();
                    }
                    DialogUIUtils.dismiss(dialog_login);
                }

                @Override
                public void onFailure(Call<LoginStatus> call, Throwable t) {
                    Log.v("Login请求失败!", t.getMessage());
                    DialogUIUtils.dismiss(dialog_login);
                    Snackbar.make(btn_login, "登录失败(请求失败)", Snackbar.LENGTH_LONG).setAction
                            ("Action", null).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
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

    public void FileDir() {
        boolean sdCardExist = android.os.Environment.getExternalStorageState().equals(android.os
                .Environment.MEDIA_MOUNTED);
        String IMAGE_PATH, PAYMENT_PATH, SAMPLETAG_PATH, CRASH_PATH, BILL_PATH, FEEDBACK_PATH,
                TOLDBOOK_PATH;
        if (sdCardExist) {
            IMAGE_PATH = Environment.getExternalStorageDirectory() + "/FDA/Image/";
            PAYMENT_PATH = Environment.getExternalStorageDirectory() + "/FDA/payment/";
            SAMPLETAG_PATH = Environment.getExternalStorageDirectory() + "/FDA/sampletag/";
            CRASH_PATH = Environment.getExternalStorageDirectory() + "/FDA/crash/";
            BILL_PATH = Environment.getExternalStorageDirectory() + "/FDA/Bill/";
            FEEDBACK_PATH = Environment.getExternalStorageDirectory() + "/FDA/Feedback/";
            TOLDBOOK_PATH = Environment.getExternalStorageDirectory() + "/FDA/Toldbook/";
            //COMPRESS_PATH = Environment.getExternalStorageDirectory() + "/FDA/Compress/";
        } else {
            IMAGE_PATH = PAYMENT_PATH = SAMPLETAG_PATH = CRASH_PATH = BILL_PATH = FEEDBACK_PATH =
                    TOLDBOOK_PATH = this.getCacheDir().toString() + "/";
        }
        File image = new File(IMAGE_PATH), payment = new File(PAYMENT_PATH), sampletag = new File
                (SAMPLETAG_PATH), crash = new File(CRASH_PATH), bill = new File(BILL_PATH), feedback
                = new File(FEEDBACK_PATH), toldbook = new File(TOLDBOOK_PATH);
        if (!image.exists()) {
            image.mkdirs();
        }
        if (!payment.exists()) {
            payment.mkdirs();
        }
        if (!sampletag.exists()) {
            sampletag.mkdirs();
        }
        if (!crash.exists()) {
            crash.mkdirs();
        }
        if (!bill.exists()) {
            bill.mkdirs();
        }
        if (!feedback.exists()) {
            feedback.mkdirs();
        }
        if (!toldbook.exists()) {
            toldbook.mkdirs();
        }
    }
}
