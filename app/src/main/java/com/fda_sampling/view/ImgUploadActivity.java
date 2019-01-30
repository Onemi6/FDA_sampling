package com.fda_sampling.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.fda_sampling.R;
import com.fda_sampling.model.UploadImg;
import com.fda_sampling.service.FDA_API;
import com.fda_sampling.service.HttpUtils;
import com.fda_sampling.util.MyApplication;
import com.fda_sampling.util.NetworkUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImgUploadActivity extends AppCompatActivity {

    private Context _context;
    private Button btn_uploadimg;
    private Spinner sp_imgtype;
    private ArrayAdapter ada_img_type;
    private String img_type = null, number = null, picPath, token;
    private List<String> selectPaths = new ArrayList<>(), picList = new ArrayList<>(),
            status = new ArrayList<>();
    private RecyclerView rv_add_img;
    private GridLayoutManager layoutmanager;
    private ImgAdapter adapter_img;
    private int pos, fail_num = 0;
    private ProgressDialog mypDialog;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;

    private static final int REQUEST_IMAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_img_upload);
        _context = this;
        Intent intent = getIntent();
        number = intent.getStringExtra("custom_no");
        sharedPreferences = getSharedPreferences("Info", MODE_PRIVATE);

        sp_imgtype = findViewById(R.id.spinner_img_type);
        btn_uploadimg = findViewById(R.id.uploadImage);
        rv_add_img = findViewById(R.id.rv_img_add);

        ada_img_type = ArrayAdapter.createFromResource(_context, R.array.IMG_TYPE, android.R
                .layout.simple_spinner_dropdown_item);
        sp_imgtype.setAdapter(ada_img_type);

        toolbar = findViewById(R.id.toolbar_imgupload);
        toolbar.setTitle("上传图片");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //设置toolbar
        setSupportActionBar(toolbar);
        //左边的小箭头（注意需要在setSupportActionBar(toolbar)之后才有效果）
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white);
        //菜单点击事件（注意需要在setSupportActionBar(toolbar)之后才有效果）
        //toolbar.setOnMenuItemClickListener(onMenuItemClick);

        initdata();

        //GridLayoutManager 对象 这里使用 GridLayoutManager 是网格布局的意思
        layoutmanager = new GridLayoutManager(this, 3);
        layoutmanager.setOrientation(GridLayoutManager.VERTICAL);
        //设置RecyclerView 布局
        rv_add_img.setLayoutManager(layoutmanager);
        //设置Adapter
        adapter_img = new ImgAdapter(this, picList);
        rv_add_img.setAdapter(adapter_img);

        adapter_img.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position != 0) {
                    String path = picList.get(position);
                    if (path != null) {
                        Options opt = new Options();
                        opt.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(path, opt);
                        int imageHeight = opt.outHeight;
                        int imageWidth = opt.outWidth;
                        Display display = getWindowManager().getDefaultDisplay();
                        Point point = new Point();
                        display.getRealSize(point);
                        int screenHeight = point.y;
                        int screenWidth = point.x;
                        int scale = 1;
                        int scaleWidth = imageWidth / screenWidth;
                        int scaleHeigh = imageHeight / screenHeight;
                        if (scaleWidth >= scaleHeigh && scaleWidth > 1) {
                            scale = scaleWidth;
                        } else if (scaleWidth < scaleHeigh && scaleHeigh > 1) {
                            scale = scaleHeigh;
                        }
                        opt.inSampleSize = scale;
                        opt.inJustDecodeBounds = false;
                        Bitmap bm = BitmapFactory.decodeFile(path, opt);
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.item_img,
                                (ViewGroup) findViewById(R.id.dialog_layout));
                        ImageView imageview = layout
                                .findViewById(R.id.imageView);
                        imageview.setImageBitmap(bm);
                        AlertDialog.Builder dialog_img = new AlertDialog.Builder(
                                ImgUploadActivity.this).setView(layout)
                                .setPositiveButton("确定", null);
                        dialog_img.show();
                    }
                }
            }
        });

        adapter_img.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                if (position != 0) {
                    pos = position;
                    // 通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            _context);
                    // 设置Title的图标
                    builder.setIcon(R.mipmap.ic_launcher);
                    // 设置Title的内容
                    builder.setTitle("提示");
                    // 设置Content来显示一个信息
                    builder.setMessage("确定删除第" + position + "张图片?");
                    // 设置一个PositiveButton
                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    //dialog.dismiss();
                                    try {
                                        adapter_img.removeItem(pos);
                                        Toast.makeText(_context, "删除图片成功",
                                                Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        // TODO 自动生成的 catch 块
                                        e.printStackTrace();
                                        Toast.makeText(_context, "删除图片失败",
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
                                    //dialog.dismiss();
                                }
                            });
                    // 显示出该对话框
                    builder.show();
                }
            }
        });

        btn_uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mypDialog = new ProgressDialog(ImgUploadActivity.this);
                // 实例化
                mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                // 设置进度条风格，风格为圆形，旋转的
                mypDialog.setTitle("上传图片中...");
                // 设置ProgressDialog 标题
                mypDialog.setIndeterminate(false);
                // 设置ProgressDialog 的进度条是否不明确
                mypDialog.setCancelable(false);
                // 设置ProgressDialog 是否可以按退回按键取消
                mypDialog.show();
                // 让ProgressDialog显示
                img_type = sp_imgtype.getSelectedItem().toString();
                if (adapter_img.getImgList().size() > 1) {
                    picList = adapter_img.getImgList();
                    for (String onepath : picList) {
                        picPath = onepath;
                        if (!picPath.equals("加号")) {
                            if (picPath != null) {
                                attempImgUpload();
                            } else {
                                Snackbar.make(btn_uploadimg, "上传的文件路径出错",
                                        Snackbar.LENGTH_LONG).setAction("Action", null)
                                        .show();
                            }
                        }
                    }
                    /*new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1500); // 延时1s执行*/
                } else if (adapter_img.getImgList().size() == 1) {
                    mypDialog.dismiss();
                    Snackbar.make(btn_uploadimg, "至少选择一张图片",
                            Snackbar.LENGTH_LONG).setAction("Action", null)
                            .show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                selectPaths = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                picList.addAll(selectPaths);
            }
            selectPaths = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter_img.changList_add(picList);
    }

    public void initdata() {
        picList.add("加号");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

    public void attempImgUpload() {
        if (NetworkUtil.isNetworkAvailable(_context)) {
            FDA_API request = HttpUtils.GsonApi();
            if (((MyApplication) getApplication()).getTOKEN() == null) {
                token = sharedPreferences.getString("TOKEN", null);
            } else {
                token = ((MyApplication) getApplication()).getTOKEN();
            }
            File file = new File(picPath);
            RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), number);
            RequestBody type = RequestBody.create(MediaType.parse("multipart/form-data"), img_type);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part fileData = MultipartBody.Part.createFormData("file", picPath,
                    requestFile);
            Call<UploadImg> call = request.ImageUpload(token, id, type, fileData);
            call.enqueue(new Callback<UploadImg>() {
                @Override
                public void onResponse(Call<UploadImg> call, Response<UploadImg> response) {
                    if (response.code() == 401) {
                        Log.v("ImgUpload请求", "token过期");
                        Intent intent_login = new Intent();
                        intent_login.setClass(ImgUploadActivity.this,
                                LoginActivity.class);
                        intent_login.putExtra("login_type", 1);
                        mypDialog.dismiss();
                        startActivity(intent_login);
                    } else if (response.code() == 200) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("success")) {
                                status.add("1");
                                Log.v("图片上传成功", response.body().getMessage());
                            } else {
                                status.add("0");
                                fail_num++;
                                Log.v("图片上传失败", response.body().getMessage());
                            }
                        } else {
                            Log.v("ImgUpload请求成功!", "response.body is null");
                        }
                        if (status.size() >= (picList.size() - 1)) {
                            mypDialog.dismiss();
                            Toast.makeText(
                                    ImgUploadActivity.this,
                                    "共上传" + (picList.size() - 1) + "张图片,其中失败"
                                            + fail_num + "张",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UploadImg> call, Throwable t) {
                    mypDialog.dismiss();
                    Log.v("ImgUpload请求失败!", t.getMessage());
                }
            });
        } else {
            mypDialog.dismiss();
            Toast.makeText(ImgUploadActivity.this, "当前无网络", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
