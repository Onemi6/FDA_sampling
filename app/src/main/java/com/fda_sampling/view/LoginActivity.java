package com.fda_sampling.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.fda_sampling.R;
import com.fda_sampling.model.LoginStatus;
import com.fda_sampling.service.FDA_API;
import com.fda_sampling.service.HttpUtils;
import com.fda_sampling.util.MyApplication;
import com.fda_sampling.util.NetworkUtil;
import com.fda_sampling.util.RSAUtil;

import java.security.PublicKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    private EditText et_mAccount, et_mPassword;
    private CheckBox remember_password;
    private ProgressBar mProgressView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Button btn_login;
    private String account, password;
    private int login_type;
    private Context _context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _context = this;
        Intent intent = getIntent();
        login_type = intent.getIntExtra("login_type", 0);
        sharedPreferences = getSharedPreferences("Info", MODE_PRIVATE);
        initView();
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
        mProgressView = findViewById(R.id.login_progress);
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
        mProgressView.setVisibility(View.VISIBLE);
        try {
            // 从字符串中得到公钥
            PublicKey publicKey = RSAUtil.loadPublicKey(RSAUtil.PUCLIC_KEY);
            // 从文件中得到公钥
            //InputStream inPublic = getResources().getAssets().open("rsa_public_key.pem");
            //PublicKey publicKey = RSAUtils.loadPublicKey(inPublic);
            // 加密
            byte[] encryptByte = RSAUtil.encryptData(password.getBytes(), publicKey);
            // 为了方便观察吧加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
            String password_afterEncrypt = Base64.encodeToString(encryptByte, Base64.DEFAULT);
            //String password_URLEncoder = URLEncoder.encode(password_afterEncrypt, "UTF-8");
            //Log.e("rsa", "原文:" + password + "密文:" + password_afterEncrypt);
            /*Map<String, Object> map = new HashMap<>();
            map.put("Login_Name", account);
            map.put("Password", password_afterEncrypt);
            Gson gson = new Gson();
            String data = gson.toJson(map);*/
            String data = "{\"Login_Name\":\"" + account + "\"," + "\"Password\":\"" +
                    password_afterEncrypt + "\"}";
            //Log.e("data=", data);
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
                                Snackbar.make(btn_login, "登录成功", Snackbar.LENGTH_LONG).setAction
                                        ("Action", null).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressView.setVisibility(View.GONE);
                                        startActivity(new Intent(LoginActivity.this,
                                                MainActivity.class));
                                        finish();
                                    }
                                }, 1500);  //延时1s执行
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
                    mProgressView.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<LoginStatus> call, Throwable t) {
                    mProgressView.setVisibility(View.GONE);
                    Log.v("Login请求成功!", t.getMessage());
                    Snackbar.make(btn_login, "登录失败(请求失败)", Snackbar.LENGTH_LONG).setAction
                            ("Action", null).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
