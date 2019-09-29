package com.fda_sampling.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.fda_sampling.R;
import com.fda_sampling.model.DelImgStatus;
import com.fda_sampling.model.ImageInfo;
import com.fda_sampling.model.UploadImg;
import com.fda_sampling.service.FDA_API;
import com.fda_sampling.service.HttpUtils;
import com.fda_sampling.util.ClickUtil;
import com.fda_sampling.util.MyApplication;
import com.fda_sampling.util.NetworkUtil;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImgUploadActivity extends AppCompatActivity {

    private Context _context;
    private Button btn_uploadImg;
    private String number = null, token;
    private List<String> picList_1 = new ArrayList<>(), picList_2 = new ArrayList<>(), picList_3
            = new ArrayList<>(), picList_4 = new ArrayList<>(), picList_5 = new ArrayList<>(),
            picList_6 = new ArrayList<>(), picList_7 = new ArrayList<>(), picList_8 = new
            ArrayList<>(), uploadImage = new ArrayList<>(), status = new ArrayList<>(),
            type_exists = new ArrayList<>();
    private List<ImageInfo> imageInfoList = new ArrayList<>();
    private ImgAdapter adapter_img_1, adapter_img_2, adapter_img_3, adapter_img_4, adapter_img_5,
            adapter_img_6, adapter_img_7, adapter_img_8, adapter_uploadImg;
    private int fail_num, finish, picNum;
    private BuildBean dialog_ImgUpload;
    private SharedPreferences sharedPreferences;

    private static final int TYPE_IMAGE_0 = 0;
    private static final int TYPE_IMAGE_1 = 1;
    private static final int TYPE_IMAGE_2 = 2;
    private static final int TYPE_IMAGE_3 = 3;
    private static final int TYPE_IMAGE_4 = 4;
    private static final int TYPE_IMAGE_5 = 5;
    private static final int TYPE_IMAGE_6 = 6;
    private static final int TYPE_IMAGE_7 = 7;
    private static final int TYPE_IMAGE_8 = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_img_upload);
        _context = this;
        Intent intent = getIntent();
        number = intent.getStringExtra("NO");
        sharedPreferences = getSharedPreferences("Info", MODE_PRIVATE);
        Tiny.getInstance().init(getApplication());

        RecyclerView rv_add_img_1 = findViewById(R.id.rv_img_add_1);
        RecyclerView rv_add_img_2 = findViewById(R.id.rv_img_add_2);
        RecyclerView rv_add_img_3 = findViewById(R.id.rv_img_add_3);
        RecyclerView rv_add_img_4 = findViewById(R.id.rv_img_add_4);
        RecyclerView rv_add_img_5 = findViewById(R.id.rv_img_add_5);
        RecyclerView rv_add_img_6 = findViewById(R.id.rv_img_add_6);
        RecyclerView rv_add_img_7 = findViewById(R.id.rv_img_add_7);
        RecyclerView rv_add_img_8 = findViewById(R.id.rv_img_add_8);

        RecyclerView rv_upload_img = findViewById(R.id.rv_upload_img);
        btn_uploadImg = findViewById(R.id.btn_uploadImage);

        Toolbar toolbar = findViewById(R.id.toolbar_ImgUpload);
        toolbar.setTitle("图片上传");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //设置toolbar
        setSupportActionBar(toolbar);
        //左边的小箭头（注意需要在setSupportActionBar(toolbar)之后才有效果）
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white);
        //菜单点击事件（注意需要在setSupportActionBar(toolbar)之后才有效果）
        //toolbar.setOnMenuItemClickListener(onMenuItemClick);

        initData();

        //GridLayoutManager 对象 这里使用 GridLayoutManager 是网格布局的意思
        LinearLayoutManager layoutManager_1 = new LinearLayoutManager(this);
        layoutManager_1.setOrientation(LinearLayoutManager.HORIZONTAL);
        //设置RecyclerView 布局
        rv_add_img_1.setLayoutManager(layoutManager_1);
        //设置Adapter
        adapter_img_1 = new ImgAdapter(this, picList_1);
        rv_add_img_1.setAdapter(adapter_img_1);

        LinearLayoutManager layoutManager_2 = new LinearLayoutManager(this);
        layoutManager_2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_add_img_2.setLayoutManager(layoutManager_2);
        adapter_img_2 = new ImgAdapter(this, picList_2);
        rv_add_img_2.setAdapter(adapter_img_2);

        LinearLayoutManager layoutManager_3 = new LinearLayoutManager(this);
        layoutManager_3.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_add_img_3.setLayoutManager(layoutManager_3);
        adapter_img_3 = new ImgAdapter(this, picList_3);
        rv_add_img_3.setAdapter(adapter_img_3);

        LinearLayoutManager layoutManager_4 = new LinearLayoutManager(this);
        layoutManager_4.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_add_img_4.setLayoutManager(layoutManager_4);
        adapter_img_4 = new ImgAdapter(this, picList_4);
        rv_add_img_4.setAdapter(adapter_img_4);

        LinearLayoutManager layoutManager_5 = new LinearLayoutManager(this);
        layoutManager_5.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_add_img_5.setLayoutManager(layoutManager_5);
        adapter_img_5 = new ImgAdapter(this, picList_5);
        rv_add_img_5.setAdapter(adapter_img_5);

        LinearLayoutManager layoutManager_6 = new LinearLayoutManager(this);
        layoutManager_6.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_add_img_6.setLayoutManager(layoutManager_6);
        adapter_img_6 = new ImgAdapter(this, picList_6);
        rv_add_img_6.setAdapter(adapter_img_6);

        LinearLayoutManager layoutManager_7 = new LinearLayoutManager(this);
        layoutManager_7.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_add_img_7.setLayoutManager(layoutManager_7);
        adapter_img_7 = new ImgAdapter(this, picList_7);
        rv_add_img_7.setAdapter(adapter_img_7);

        LinearLayoutManager layoutManager_8 = new LinearLayoutManager(this);
        layoutManager_8.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_add_img_8.setLayoutManager(layoutManager_8);
        adapter_img_8 = new ImgAdapter(this, picList_8);
        rv_add_img_8.setAdapter(adapter_img_8);

        GridLayoutManager layoutManager_upload = new GridLayoutManager(this, 4);
        layoutManager_upload.setOrientation(GridLayoutManager.VERTICAL);
        rv_upload_img.setLayoutManager(layoutManager_upload);
        adapter_uploadImg = new ImgAdapter(this, uploadImage);
        rv_upload_img.setAdapter(adapter_uploadImg);

        adapter_img_1.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgOnClick(position, TYPE_IMAGE_1);
            }
        });

        adapter_img_1.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgOnLongClick(position, TYPE_IMAGE_1);
            }
        });


        adapter_img_2.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgOnClick(position, TYPE_IMAGE_2);
            }
        });

        adapter_img_2.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgOnLongClick(position, TYPE_IMAGE_2);
            }
        });


        adapter_img_3.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgOnClick(position, TYPE_IMAGE_3);
            }
        });

        adapter_img_3.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgOnLongClick(position, TYPE_IMAGE_3);
            }
        });

        adapter_img_4.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgOnClick(position, TYPE_IMAGE_4);
            }
        });

        adapter_img_4.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgOnLongClick(position, TYPE_IMAGE_4);
            }
        });

        adapter_img_5.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgOnClick(position, TYPE_IMAGE_5);
            }
        });

        adapter_img_5.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgOnLongClick(position, TYPE_IMAGE_5);
            }
        });


        adapter_img_6.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgOnClick(position, TYPE_IMAGE_6);
            }
        });

        adapter_img_6.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgOnLongClick(position, TYPE_IMAGE_6);
            }
        });

        adapter_img_7.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgOnClick(position, TYPE_IMAGE_7);
            }
        });

        adapter_img_7.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgOnLongClick(position, TYPE_IMAGE_7);
            }
        });

        adapter_img_8.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgOnClick(position, TYPE_IMAGE_8);
            }
        });

        adapter_img_8.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgOnLongClick(position, TYPE_IMAGE_8);
            }
        });

        adapter_uploadImg.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgOnClick(position, TYPE_IMAGE_0);
            }
        });

        adapter_uploadImg.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, final int position) {

                // 通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                // 设置Title的图标
                builder.setIcon(R.mipmap.ic_launcher);
                // 设置Title的内容
                builder.setTitle("提示");
                // 设置Content来显示一个信息
                builder.setMessage("确定删除第" + (position + 1) + "张图片?");
                // 设置一个PositiveButton
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                int id = imageInfoList.get(position).getID();
                                //Log.v("del_id", "" + id);
                                delSamplingImg(id, position);
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
        });

        btn_uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtil.isFastClick()) {
                    ImgUpload();
                } else {
                    Snackbar.make(btn_uploadImg, "点击太快了，请稍后再试",
                            Snackbar.LENGTH_LONG).setAction("Action", null)
                            .show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<String> selectPaths;
            switch (requestCode) {
                case TYPE_IMAGE_1:
                    selectPaths = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    picList_1.addAll(selectPaths);
                    break;
                case TYPE_IMAGE_2:
                    selectPaths = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    picList_2.addAll(selectPaths);
                    break;
                case TYPE_IMAGE_3:
                    selectPaths = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    picList_3.addAll(selectPaths);
                    break;
                case TYPE_IMAGE_4:
                    selectPaths = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    picList_4.addAll(selectPaths);
                    break;
                case TYPE_IMAGE_5:
                    selectPaths = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    picList_5.addAll(selectPaths);
                    break;
                case TYPE_IMAGE_6:
                    selectPaths = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    picList_6.addAll(selectPaths);
                    break;
                case TYPE_IMAGE_7:
                    selectPaths = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    picList_7.addAll(selectPaths);
                    break;
                case TYPE_IMAGE_8:
                    selectPaths = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    picList_8.addAll(selectPaths);
                    break;
                default:
                    break;
            }
            selectPaths = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter_img_1.changList_add(picList_1);
        adapter_img_2.changList_add(picList_2);
        adapter_img_3.changList_add(picList_3);
        adapter_img_4.changList_add(picList_4);
        adapter_img_5.changList_add(picList_5);
        adapter_img_6.changList_add(picList_6);
        adapter_img_7.changList_add(picList_7);
        adapter_img_8.changList_add(picList_8);
    }

    public void initData() {
        picList_1.add("加号");
        picList_2.add("加号");
        picList_3.add("加号");
        picList_4.add("加号");
        picList_5.add("加号");
        picList_6.add("加号");
        picList_7.add("加号");
        picList_8.add("加号");
        //uploadImage.add("空白");
        attemptImageInfo();
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

    public void attemptImgUpload(String image_type, String image_path) {
        if (NetworkUtil.isNetworkAvailable(_context)) {
            FDA_API request = HttpUtils.JsonApi();
            if (((MyApplication) getApplication()).getTOKEN() == null) {
                token = sharedPreferences.getString("TOKEN", null);
            } else {
                token = ((MyApplication) getApplication()).getTOKEN();
            }
            File file = new File(image_path);
            RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), number);
            RequestBody type = RequestBody.create(MediaType.parse("multipart/form-data"),
                    image_type);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part fileData = MultipartBody.Part.createFormData("file", image_path,
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
                        DialogUIUtils.dismiss(dialog_ImgUpload);
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
                            DialogUIUtils.dismiss(dialog_ImgUpload);
                            dialog_ImgUpload = DialogUIUtils.showLoading(_context, "已上传 " +
                                    status.size() + "/" + picNum, false, true, false, false);
                            dialog_ImgUpload.show();
                        } else {
                            Log.v("ImgUpload请求成功!", "response.body is null");
                        }
                        if ((status.size() + 8) >= (picList_1.size() + picList_2.size() +
                                picList_3.size() + picList_4.size() + picList_5.size() +
                                picList_6.size() + picList_7.size() + picList_8.size())) {
                            finish = 1;
                        }
                        if (finish == 1) {
                            DialogUIUtils.dismiss(dialog_ImgUpload);
                            Snackbar.make(btn_uploadImg, "共上传" + status.size() + "张图片,其中失败" +
                                    fail_num + "张", Snackbar.LENGTH_LONG).setAction("Action",
                                    null).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UploadImg> call, Throwable t) {
                    Log.v("ImgUpload请求失败!", t.getMessage());
                    DialogUIUtils.dismiss(dialog_ImgUpload);
                    Snackbar.make(btn_uploadImg, "上传请求失败!", Snackbar.LENGTH_LONG).setAction
                            ("Action", null).show();
                }
            });
        } else {
            DialogUIUtils.dismiss(dialog_ImgUpload);
            Snackbar.make(btn_uploadImg, "当前无网络", Snackbar.LENGTH_LONG).setAction("Action", null)
                    .show();
        }
    }

    public void attemptImageInfo() {
        if (NetworkUtil.isNetworkAvailable(_context)) {
            type_exists.add("经营许可证");
            FDA_API request = HttpUtils.JsonApi();
            if (((MyApplication) getApplication()).getTOKEN() == null) {
                token = sharedPreferences.getString("TOKEN", null);
            } else {
                token = ((MyApplication) getApplication()).getTOKEN();
            }
            Call<List<ImageInfo>> call = request.ImageInfo(token, number);
            call.enqueue(new Callback<List<ImageInfo>>() {
                @Override
                public void onResponse(Call<List<ImageInfo>> call, Response<List<ImageInfo>>
                        response) {
                    if (response.code() == 401) {
                        Log.v("ImageInfo请求", "token过期");
                        Intent intent_login = new Intent();
                        intent_login.setClass(ImgUploadActivity.this,
                                LoginActivity.class);
                        intent_login.putExtra("login_type", 1);
                        startActivity(intent_login);
                    } else if (response.code() == 200) {
                        if (response.body() != null) {
                            imageInfoList = response.body();
                            Log.v("imageInfoList.size()", "" + imageInfoList.size());
                            if (imageInfoList.size() > 0) {
                                for (int i = 0; i < imageInfoList.size(); i++) {
                                    int id = imageInfoList.get(i).getID();
                                    if (!type_exists.contains(imageInfoList.get(i).getIMG_TYPE())) {
                                        type_exists.add(imageInfoList.get(i).getIMG_TYPE());
                                    }
                                    //Log.v("id", "" + id);
                                    attemptImage(id);
                                }
                            }
                        } else {
                            Log.v("ImageInfo请求成功!", "response.body is null");
                        }

                    } else {
                        Log.v("ImageInfo请求成功!", "" + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<ImageInfo>> call, Throwable t) {
                    Log.v("ImageInfo请求失败!", t.getMessage());
                }
            });
        } else {
            Snackbar.make(btn_uploadImg, "当前无网络", Snackbar.LENGTH_LONG).setAction("Action", null)
                    .show();
        }
    }

    public void attemptImage(final int id) {
        final String image_path = Environment.getExternalStorageDirectory() + "/FDA/Image/" + id
                + ".jpg";
        final File image = new File(image_path);
        if (image.exists()) {
            Log.v("图片", "已经存在");
            uploadImage.add(image_path);
            adaChang();
            //adapter_uploadImg.changList_add(uploadImage);
        } else {
            if (NetworkUtil.isNetworkAvailable(_context)) {
                FDA_API request = HttpUtils.JsonApi();
                if (((MyApplication) getApplication()).getTOKEN() == null) {
                    token = sharedPreferences.getString("TOKEN", null);
                } else {
                    token = ((MyApplication) getApplication()).getTOKEN();
                }
                Call<ResponseBody> call = request.Image(token, id);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody>
                            response) {
                        if (response.code() == 401) {
                            Log.v("Image请求", "token过期");
                            Intent intent_login = new Intent();
                            intent_login.setClass(ImgUploadActivity.this,
                                    LoginActivity.class);
                            intent_login.putExtra("login_type", 1);
                            startActivity(intent_login);
                        } else if (response.code() == 200) {
                            if (response.body() != null) {
                                try {
                                    // 获取文件的输出流对象
                                    FileOutputStream outStream = new FileOutputStream(image);
                                    // 获取字符串对象的byte数组并写入文件流
                                    outStream.write(response.body().bytes());
                                    // 最后关闭文件输出流
                                    outStream.close();
                                    Log.v("图片", "下载成功");
                                    uploadImage.add(image_path);
                                    adaChang();
                                    //adapter_uploadImg.changList_add(uploadImage);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                    Log.v("ResponseBody", "FileNotFoundException");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Log.v("ResponseBody", "IOException");
                                }
                            } else {
                                Log.v("Image请求成功!", "response.body is null");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("Image请求失败!", t.getMessage());
                    }
                });
            } else {
                Snackbar.make(btn_uploadImg, "当前无网络", Snackbar.LENGTH_LONG).setAction("Action",
                        null).show();
            }
        }
    }

    public void ImgOnClick(int pos, int TYPE_IMAGE) {
        if (pos == 0 && TYPE_IMAGE != TYPE_IMAGE_0) {
            MultiImageSelector.create()
                    .multi()
                    .start(ImgUploadActivity.this, TYPE_IMAGE);
        } else {
            String path = null;
            if (TYPE_IMAGE == TYPE_IMAGE_0) {
                path = uploadImage.get(pos);
            } else if (TYPE_IMAGE == TYPE_IMAGE_1) {
                path = picList_1.get(pos);
            } else if (TYPE_IMAGE == TYPE_IMAGE_2) {
                path = picList_2.get(pos);
            } else if (TYPE_IMAGE == TYPE_IMAGE_3) {
                path = picList_3.get(pos);
            } else if (TYPE_IMAGE == TYPE_IMAGE_4) {
                path = picList_4.get(pos);
            } else if (TYPE_IMAGE == TYPE_IMAGE_5) {
                path = picList_5.get(pos);
            } else if (TYPE_IMAGE == TYPE_IMAGE_6) {
                path = picList_6.get(pos);
            } else if (TYPE_IMAGE == TYPE_IMAGE_7) {
                path = picList_7.get(pos);
            } else if (TYPE_IMAGE == TYPE_IMAGE_8) {
                path = picList_8.get(pos);
            }
            showImage(path);
        }
    }

    public void ImgOnLongClick(final int pos, final int TYPE_IMAGE) {
        if (pos != 0) {
            // 通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
            AlertDialog.Builder builder = new AlertDialog.Builder(_context);
            // 设置Title的图标
            builder.setIcon(R.mipmap.ic_launcher);
            // 设置Title的内容
            builder.setTitle("提示");
            // 设置Content来显示一个信息
            builder.setMessage("确定删除第" + pos + "张图片?");
            // 设置一个PositiveButton
            builder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            //dialog.dismiss();
                            try {
                                if (TYPE_IMAGE == TYPE_IMAGE_1) {
                                    adapter_img_1.removeItem(pos);
                                } else if (TYPE_IMAGE == TYPE_IMAGE_2) {
                                    adapter_img_2.removeItem(pos);
                                } else if (TYPE_IMAGE == TYPE_IMAGE_3) {
                                    adapter_img_3.removeItem(pos);
                                } else if (TYPE_IMAGE == TYPE_IMAGE_4) {
                                    adapter_img_4.removeItem(pos);
                                } else if (TYPE_IMAGE == TYPE_IMAGE_5) {
                                    adapter_img_5.removeItem(pos);
                                } else if (TYPE_IMAGE == TYPE_IMAGE_6) {
                                    adapter_img_6.removeItem(pos);
                                } else if (TYPE_IMAGE == TYPE_IMAGE_7) {
                                    adapter_img_7.removeItem(pos);
                                } else if (TYPE_IMAGE == TYPE_IMAGE_8) {
                                    adapter_img_8.removeItem(pos);
                                }
                                Snackbar.make(btn_uploadImg, "删除图片成功", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            } catch (Exception e) {
                                // TODO 自动生成的 catch 块
                                e.printStackTrace();
                                Snackbar.make(btn_uploadImg, "删除图片失败", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
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

    public void ImgUpload() {
        fail_num = 0;
        finish = 0;
        picNum = 0;
        status.clear();

        picList_1 = adapter_img_1.getImgList();
        picList_2 = adapter_img_2.getImgList();
        picList_3 = adapter_img_3.getImgList();
        picList_4 = adapter_img_4.getImgList();
        picList_5 = adapter_img_5.getImgList();
        picList_6 = adapter_img_6.getImgList();
        picList_7 = adapter_img_7.getImgList();
        picList_8 = adapter_img_8.getImgList();

        if (picList_1.size() == 1 && !type_exists.contains(getResources()
                .getString(R.string.img_type_1))) {
            Snackbar.make(btn_uploadImg, getResources().getString(R.string.img_type_1) +
                    "至少选择一张", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else if (picList_2.size() == 1 && !type_exists.contains(getResources()
                .getString(R.string.img_type_2))) {
            Snackbar.make(btn_uploadImg, getResources().getString(R.string.img_type_2) +
                    "至少选择一张", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else if (picList_3.size() == 1 && !type_exists.contains(getResources()
                .getString(R.string.img_type_3))) {
            Snackbar.make(btn_uploadImg, getResources().getString(R.string.img_type_3) +
                    "至少选择一张", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else if (picList_5.size() == 1 && !type_exists.contains(getResources()
                .getString(R.string.img_type_5))) {
            Snackbar.make(btn_uploadImg, getResources().getString(R.string.img_type_5) +
                    "至少选择一张", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else if (picList_6.size() == 1 && !type_exists.contains(getResources()
                .getString(R.string.img_type_6))) {
            Snackbar.make(btn_uploadImg, getResources().getString(R.string.img_type_6) +
                    "至少选择一张", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else if (picList_7.size() == 1 && !type_exists.contains(getResources()
                .getString(R.string.img_type_7))) {
            Snackbar.make(btn_uploadImg, getResources().getString(R.string.img_type_7) +
                    "至少选择一张", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else if (picList_8.size() == 1 && !type_exists.contains(getResources()
                .getString(R.string.img_type_8))) {
            Snackbar.make(btn_uploadImg, getResources().getString(R.string.img_type_8) +
                    "至少选择一张", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            Log.v("图片", "八种类别都已经上传过");
            picNum = picList_1.size() + picList_2.size() + picList_3.size() + picList_4.size() +
                    picList_5.size() + picList_6.size() + picList_7.size() + picList_8.size() - 8;

            for (String pic : picList_1) {
                if (!pic.equals("加号")) {
                    pic_compress(getResources().getString(R.string.img_type_1), pic);
                    //pic_compress("样品照片", pic);
                    //attemptImgUpload("样品照片", pic);
                }
            }
            for (String pic : picList_2) {
                if (!pic.equals("加号")) {
                    pic_compress(getResources().getString(R.string.img_type_2), pic);
                    //pic_compress("样品照片", pic);
                    //attemptImgUpload("样品照片", pic);
                }
            }
            for (String pic : picList_3) {
                if (!pic.equals("加号")) {
                    pic_compress(getResources().getString(R.string.img_type_3), pic);
                    //pic_compress("样品照片", pic);
                    //attemptImgUpload("样品照片", pic);
                }
            }
            for (String pic : picList_4) {
                if (!pic.equals("加号")) {
                    pic_compress(getResources().getString(R.string.img_type_4), pic);
                    //pic_compress("经营许可证", pic);
                    //attemptImgUpload("经营许可证", pic);
                }
            }
            for (String pic : picList_5) {
                if (!pic.equals("加号")) {
                    pic_compress(getResources().getString(R.string.img_type_5), pic);
                    //pic_compress("告知书", pic);
                    //attemptImgUpload("告知书", pic);
                }
            }
            for (String pic : picList_6) {
                if (!pic.equals("加号")) {
                    pic_compress(getResources().getString(R.string.img_type_6), pic);
                    //pic_compress("反馈单", pic);
                    //attemptImgUpload("反馈单", pic);
                }
            }
            for (String pic : picList_7) {
                if (!pic.equals("加号")) {
                    pic_compress(getResources().getString(R.string.img_type_7), pic);
                    //pic_compress("抽样单", pic);
                    //attemptImgUpload("抽样单", pic);
                }
            }
            for (String pic : picList_8) {
                if (!pic.equals("加号")) {
                    pic_compress(getResources().getString(R.string.img_type_8), pic);
                    //pic_compress("微信截图", pic);
                    //attemptImgUpload("微信截图", pic);
                }
            }
        }
    }

    public void showImage(String path) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.item_img_show,
                (ViewGroup) findViewById(R.id.dialog_layout));
        ImageView imageview = layout
                .findViewById(R.id.imageView);
        AlertDialog.Builder dialog_img = new AlertDialog.Builder(
                ImgUploadActivity.this).setView(layout)
                .setPositiveButton("确定", null);
        dialog_img.show();
        Glide.with(_context).load(path)
                .placeholder(R.mipmap.logo)
                .error(R.mipmap.error)
                .into(imageview);
    }

    public void adaChang() {
        if (uploadImage.size() >= imageInfoList.size()) {
            adapter_uploadImg.changList_add(uploadImage);
        }
    }

    public void pic_compress(final String type, String filePath) {
        /*1、quality-压缩质量，默认为76
        2、isKeepSampling-是否保持原数据源图片的宽高
        3、fileSize-压缩后文件大小
        4、outfile-压缩后文件存储路径
        如果不配置，Tiny内部会根据默认压缩质量进行压缩，压缩后文件默认存储在：
        ExternalStorage/Android/data/${packageName}/tiny/目录下*/
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        Tiny.getInstance().source(filePath).asFile().withOptions(options).compress(new FileCallback() {
            @Override
            public void callback(boolean isSuccess, String outfile) {
                //return the compressed file path
                //Log.v("压缩图片", outfile);
                attemptImgUpload(type, outfile);
            }
        });
    }

    public void delSamplingImg(final int id, final int pos) {
        if (NetworkUtil.isNetworkAvailable(_context)) {
            FDA_API request = HttpUtils.JsonApi();
            if (((MyApplication) getApplication()).getTOKEN() == null) {
                token = sharedPreferences.getString("TOKEN", null);
            } else {
                token = ((MyApplication) getApplication()).getTOKEN();
            }
            Call<DelImgStatus> call = request.delSamplingImg(token, id);
            call.enqueue(new Callback<DelImgStatus>() {
                @Override
                public void onResponse(Call<DelImgStatus> call, Response<DelImgStatus>
                        response) {
                    if (response.code() == 401) {
                        Log.v("delImage请求", "token过期");
                        Intent intent_login = new Intent();
                        intent_login.setClass(ImgUploadActivity.this,
                                LoginActivity.class);
                        intent_login.putExtra("login_type", 1);
                        startActivity(intent_login);
                    } else if (response.code() == 200) {
                        if (response.body() != null) {
                            DelImgStatus delImgStatus = response.body();
                            if (delImgStatus.getMessage().equals("执行完成！")) {
                                adapter_uploadImg.removeItem(pos);
                                Snackbar.make(btn_uploadImg, "删除成功!",
                                        Snackbar.LENGTH_LONG).setAction("Action", null)
                                        .show();
                            } else {
                                Snackbar.make(btn_uploadImg, delImgStatus.getMessage(),
                                        Snackbar.LENGTH_LONG).setAction("Action", null)
                                        .show();
                            }
                        } else {
                            Log.v("delImage请求成功!", "response.body is null");
                        }
                    }
                }

                @Override
                public void onFailure(Call<DelImgStatus> call, Throwable t) {
                    Log.v("delImage请求失败!", t.getMessage());
                }
            });
        } else {
            Snackbar.make(btn_uploadImg, "当前无网络", Snackbar.LENGTH_LONG).setAction("Action",
                    null).show();
        }
    }
}
