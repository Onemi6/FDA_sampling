package com.fda_sampling.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.fda_sampling.R;

import java.io.File;
import java.io.IOException;

public class SignatureActivity extends AppCompatActivity {
    private SignatureView signatureView;
    private Button btn_signature_clear, btn_signature_save;
    private String NO, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        Intent intent = getIntent();
        NO = intent.getStringExtra("NO");
        title = intent.getStringExtra("type");

        Toolbar toolbar = findViewById(R.id.toolbar_Signature);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //设置toolbar
        setSupportActionBar(toolbar);
        //左边的小箭头（注意需要在setSupportActionBar(toolbar)之后才有效果）
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white);
        //菜单点击事件（注意需要在setSupportActionBar(toolbar)之后才有效果）
        //toolbar.setOnMenuItemClickListener(onMenuItemClick);

        signatureView = findViewById(R.id.signatureView);
        btn_signature_clear = findViewById(R.id.btn_signature_clear);
        btn_signature_save = findViewById(R.id.btn_signature_save);

        //修改背景、笔宽、颜色
        signatureView.setBackColor(Color.WHITE);
        signatureView.setPaintWidth(20);
        signatureView.setPenColor(Color.BLACK);
        //清除
        btn_signature_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.clear();
                signatureView.setBackColor(Color.WHITE);
                signatureView.setPaintWidth(20);
                signatureView.setPenColor(Color.BLACK);
            }
        });
        //保存
        btn_signature_save.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (signatureView.getTouched()) {
                    String path = Environment.getExternalStorageDirectory() + "/FDA/Signature/" +
                            NO;
                    if (title.equals("抽样员本人")) {
                        path = path + "-1.jpg";
                    } else if (title.equals("被抽样单位人员")) {
                        path = path + "-2.jpg";
                    } else if (title.equals("同行抽样人员")) {
                        path = path + "-3-";
                        while (true) {
                            int i = 0;
                            i++;
                            if (!new File(path + String.valueOf(i) + ".jpg").exists()) {
                                path = path + String.valueOf(i) + ".jpg";
                                break;
                            }
                        }
                    }
                    try {
                        signatureView.save(path, true, 10);
                        Intent data = new Intent();
                        //data.putStringArrayListExtra()
                        data.putExtra("type", title);
                        data.putExtra("path", path);
                        setResult(RESULT_OK, data);
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Snackbar.make(btn_signature_save, "您没有签名~", Snackbar.LENGTH_LONG).setAction
                            ("Action", null).show();
                }
            }
        });
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
