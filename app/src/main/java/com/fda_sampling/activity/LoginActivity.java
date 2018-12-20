package com.fda_sampling.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.fda_sampling.R;
import com.fda_sampling.model.LoginStatus;
import com.fda_sampling.service.Post_Login;
import com.fda_sampling.util.MyApplication;
import com.fda_sampling.util.UsedPath;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends Activity {

    private EditText et_mAccount, et_mPassword;
    private CheckBox remember_password;
    private View mProgressView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Button btn_login;
    private boolean isremember;
    private String account, password;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        type = intent.getIntExtra("login_type", 0);

        et_mAccount = findViewById(R.id.account);
        et_mPassword = findViewById(R.id.password);
        remember_password = findViewById(R.id.remember_password);
        btn_login = findViewById(R.id.btn_login);
        mProgressView = findViewById(R.id.login_progress);
        sharedPreferences = getSharedPreferences("Info", MODE_PRIVATE);
        isremember = sharedPreferences.getBoolean("isremember", false);
        if (isremember) {
            et_mAccount.setText(sharedPreferences.getString("Login_Name", ""));
            et_mPassword.setText(sharedPreferences.getString("Password", ""));
            remember_password.setChecked(true);
        } else {
            et_mAccount.setText(sharedPreferences.getString("Login_Name", ""));
            remember_password.setChecked(false);
        }
        if (type == 1) {
            attemptLogin();
        }
        btn_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        if (type == 1) {
            account = sharedPreferences.getString("Login_Name", "");
            password = sharedPreferences.getString("Password", "");
        } else {
            account = et_mAccount.getText().toString();
            password = et_mPassword.getText().toString();
        }
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
                mProgressView.setVisibility(View.VISIBLE);
                OkHttpClient client = new OkHttpClient.Builder()
                        .retryOnConnectionFailure(true)//默认重试一次，若需要重试N次，则要实现拦截器。
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .build();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(UsedPath.ip2)
                        .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析
                        .client(client)
                        .build();
                //创建 网络请求接口 的实例
                Post_Login request = retrofit.create(Post_Login.class);
                String data = "{\"Login_Name\":\"" + account + "\",\"Password\":\"" + password +
                        "\"}";
                Call<LoginStatus> call = request.getCall(data);
                //Log.v("call.request().url()", call.request().url().toString());
                call.enqueue(new Callback<LoginStatus>() {
                    @Override
                    public void onResponse(Call<LoginStatus> call, Response<LoginStatus> response) {
                        mProgressView.setVisibility(View.GONE);
                        if (response.body() != null) {
                            Log.v("请求成功!", "response.body() is not null");
                            if (response.body().getMESSAGE() == null) {
                                editor = sharedPreferences.edit();
                                if (remember_password.isChecked()) {
                                    editor.putBoolean("isremember", true);
                                    editor.putString("Login_Name", account);
                                    editor.putString("Password", password);
                                    editor.putString("TOKEN", response.body().getTOKEN());
                                    editor.putString("NAME", response.body().getNAME());
                                } else {
                                    editor.putBoolean("isremember", false);
                                    editor.putString("Login_Name", account);
                                    editor.putString("Password", password);
                                    editor.putString("TOKEN", response.body().getTOKEN());
                                    editor.putString("NAME", response.body().getNAME());
                                }
                                editor.commit();
                                ((MyApplication) getApplication()).setNAME(response.body()
                                        .getNAME());
                                ((MyApplication) getApplication()).setTOKEN(response.body()
                                        .getTOKEN());
                                if (type == 1) {
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT)
                                            .show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mProgressView.setVisibility(View.GONE);
                                            startActivity(new Intent(LoginActivity.this,
                                                    MainInfoActivity.class));
                                            finish();
                                        }
                                    }, 1500); /* 延时1s执行*/
                                }
                            } else {
                                et_mAccount.requestFocus();
                                et_mAccount.setError(response.body().getMESSAGE());
                            }
                        } else {
                            Log.v("请求成功!", "response.body() is null");
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginStatus> call, Throwable t) {
                        mProgressView.setVisibility(View.GONE);
                        Log.v("请求失败!", t.getMessage());
                    }
                });
            }
        }
    }
}

