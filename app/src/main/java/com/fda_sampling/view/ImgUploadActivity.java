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
import com.fda_sampling.model.ImageInfoAdd;
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
import java.util.Iterator;
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
    private List<String> status = new ArrayList<>();
    private List<ImageInfo> imageInfoList = new ArrayList<>(), picList_1 = new ArrayList<>(),
            picList_2 = new ArrayList<>(), picList_3 = new ArrayList<>();
    private List<ImageInfoAdd> picList_1_add = new ArrayList<>(), picList_2_add = new ArrayList<>
            (), picList_3_add = new ArrayList<>();
    private ImgAdapter adapter_img_1, adapter_img_2, adapter_img_3;
    private SignatureImgAdapter adapter_img_add_2;
    private AddImgAdapter adapter_img_add_1, adapter_img_add_3;
    private int fail_num, finish, picNum;
    private BuildBean dialog_ImgUpload;
    private SharedPreferences sharedPreferences;

    private static final int TYPE_IMAGE_1 = 1;
    private static final int TYPE_IMAGE_2 = 2;
    private static final int TYPE_IMAGE_3 = 3;

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

        initView();
        initData();
        ViewAction();
    }

    public void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_ImgUpload);
        toolbar.setTitle("图片上传");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //设置toolbar
        setSupportActionBar(toolbar);
        //左边的小箭头（注意需要在setSupportActionBar(toolbar)之后才有效果）
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white);
        //菜单点击事件（注意需要在setSupportActionBar(toolbar)之后才有效果）
        //toolbar.setOnMenuItemClickListener(onMenuItemClick);

        //已上传的三种图片
        RecyclerView rv_img_1 = findViewById(R.id.rv_img_1);
        RecyclerView rv_img_2 = findViewById(R.id.rv_img_2);
        RecyclerView rv_img_3 = findViewById(R.id.rv_img_3);
        //待上传的三种图片
        RecyclerView rv_img_1_add = findViewById(R.id.rv_img_1_add);

        RecyclerView rv_img_2_add = findViewById(R.id.rv_img_2_add);

        RecyclerView rv_img_3_add = findViewById(R.id.rv_img_3_add);

        btn_uploadImg = findViewById(R.id.btn_uploadImage);

        //已上传
        //GridLayoutManager 对象 这里使用 GridLayoutManager 是网格布局的意思
        LinearLayoutManager layoutManager_1 = new LinearLayoutManager(this);
        layoutManager_1.setOrientation(LinearLayoutManager.HORIZONTAL);
        //设置RecyclerView 布局
        rv_img_1.setLayoutManager(layoutManager_1);
        //设置Adapter
        adapter_img_1 = new ImgAdapter(this, picList_1);
        rv_img_1.setAdapter(adapter_img_1);


        LinearLayoutManager layoutManager_2 = new LinearLayoutManager(this);
        layoutManager_2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_2.setLayoutManager(layoutManager_2);
        adapter_img_2 = new ImgAdapter(this, picList_2);
        rv_img_2.setAdapter(adapter_img_2);


        LinearLayoutManager layoutManager_3 = new LinearLayoutManager(this);
        layoutManager_3.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_3.setLayoutManager(layoutManager_3);
        adapter_img_3 = new ImgAdapter(this, picList_3);
        rv_img_3.setAdapter(adapter_img_3);


        //待上传
        LinearLayoutManager layoutManager_add_1 = new LinearLayoutManager(this);
        layoutManager_add_1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_1_add.setLayoutManager(layoutManager_add_1);
        adapter_img_add_1 = new AddImgAdapter(this, picList_1_add);
        rv_img_1_add.setAdapter(adapter_img_add_1);


        LinearLayoutManager layoutManager_add_2 = new LinearLayoutManager(this);
        layoutManager_add_2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_2_add.setLayoutManager(layoutManager_add_2);
        adapter_img_add_2 = new SignatureImgAdapter(this, picList_2_add);
        rv_img_2_add.setAdapter(adapter_img_add_2);


        LinearLayoutManager layoutManager_add_3 = new LinearLayoutManager(this);
        layoutManager_add_3.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_3_add.setLayoutManager(layoutManager_add_3);
        adapter_img_add_3 = new AddImgAdapter(this, picList_3_add);
        rv_img_3_add.setAdapter(adapter_img_add_3);

    }

    public void initData() {
        picList_1_add.add(new ImageInfoAdd("现场照片", "加号"));
        picList_2_add.add(new ImageInfoAdd("抽样员本人", "加号"));
        picList_2_add.add(new ImageInfoAdd("被抽样单位人员", "加号"));
        picList_2_add.add(new ImageInfoAdd("同行抽样人员", "加号"));
        picList_3_add.add(new ImageInfoAdd("告知书&抽样单", "加号"));
        attemptImageInfo();
    }

    public void ViewAction() {
        //已上传照片的点击事件
        adapter_img_1.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgOnClick(picList_1.get(position).getPATH());
            }
        });

        adapter_img_1.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgOnLongClick(position, picList_1.get(position));
            }
        });

        adapter_img_2.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgOnClick(picList_2.get(position).getPATH());
            }
        });

        adapter_img_2.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgOnLongClick(position, picList_2.get(position));
            }
        });

        adapter_img_3.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgOnClick(picList_3.get(position).getPATH());
            }
        });

        adapter_img_3.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgOnLongClick(position, picList_3.get(position));
            }
        });
        //待上传照片的点击事件

        adapter_img_add_1.setOnClickListener(new AddImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgAddOnClick(position, picList_1_add.get(position));
            }
        });

        adapter_img_add_1.setOnLongClickListener(new AddImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgAddOnLongClick(position, picList_1_add.get(position));
            }
        });

        adapter_img_add_2.setOnClickListener(new SignatureImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                SignatureOnClick(position);
            }
        });

        adapter_img_add_2.setOnLongClickListener(new SignatureImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                //ImgAddOnLongClick(position, TYPE_IMAGE_0);
            }
        });

        adapter_img_add_3.setOnClickListener(new AddImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgAddOnClick(position, picList_3_add.get(position));
            }
        });

        adapter_img_add_3.setOnLongClickListener(new AddImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgAddOnLongClick(position, picList_3_add.get(position));
            }
        });

        //上传按钮点击事件
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<String> selectPaths;
            List<ImageInfoAdd> imageInfoAddList = new ArrayList<>();
            selectPaths = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
            switch (requestCode) {
                case TYPE_IMAGE_1:
                    for (String path : selectPaths) {
                        imageInfoAddList.add(new ImageInfoAdd("现场照片", path));
                    }
                    picList_1_add.addAll(imageInfoAddList);
                    break;
                case TYPE_IMAGE_2:
                    //Log.v("path", data.getStringExtra("path"));
                    if (data.getStringExtra("type").equals("抽样员本人")) {
                        ImageInfoAdd imageInfoAdd = new ImageInfoAdd(data.getStringExtra("type"),
                                data.getStringExtra("path"));
                        picList_2_add.set(0, imageInfoAdd);
                    } else if (data.getStringExtra("type").equals("被抽样单位人员")) {
                        ImageInfoAdd imageInfoAdd = new ImageInfoAdd(data.getStringExtra("type"),
                                data.getStringExtra("path"));
                        picList_2_add.set(1, imageInfoAdd);
                    } else if (data.getStringExtra("type").equals("同行抽样人员")) {
                        ImageInfoAdd imageInfoAdd = new ImageInfoAdd(data.getStringExtra("type"),
                                data.getStringExtra("path"));
                        picList_2_add.add(picList_2_add.size() - 1, imageInfoAdd);
                    }
                    break;
                case TYPE_IMAGE_3:
                    for (String path : selectPaths) {
                        imageInfoAddList.add(new ImageInfoAdd("告知书&抽样单", path));
                    }
                    if (imageInfoAddList.size() > 2) {
                        imageInfoAddList = imageInfoAddList.subList(0, 2);
                    }
                    if (picList_3_add.size() > 2 || (picList_3_add.size() + imageInfoAddList.size()) > 3) {
                        picList_3_add.clear();
                        picList_3_add.add(new ImageInfoAdd("告知书&抽样单", "加号"));
                    }
                    picList_3_add.addAll(imageInfoAddList);
                    break;
                default:
                    break;
            }
            /*selectPaths = null;-
            imageInfoAddList = null;*/
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter_img_add_1.changeList_add(picList_1_add);
        adapter_img_add_2.changeList_add(picList_2_add);
        adapter_img_add_3.changeList_add(picList_3_add);
    }

    public void attemptImageInfo() {
        if (NetworkUtil.isNetworkAvailable(_context)) {
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
                                for (ImageInfo imageInfo : imageInfoList) {
                                    attemptImage(imageInfo);
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

    public void attemptImage(final ImageInfo imageInfo) {
        final String image_path = Environment.getExternalStorageDirectory() + "/FDA/Image/" +
                imageInfo.getID() + ".jpg";
        imageInfo.setPATH(image_path);
        final File image = new File(image_path);
        if (image.exists()) {
            Log.v("图片", "已经存在");
            picListChange(imageInfo);
        } else {
            if (NetworkUtil.isNetworkAvailable(_context)) {
                FDA_API request = HttpUtils.JsonApi();
                if (((MyApplication) getApplication()).getTOKEN() == null) {
                    token = sharedPreferences.getString("TOKEN", null);
                } else {
                    token = ((MyApplication) getApplication()).getTOKEN();
                }
                Call<ResponseBody> call = request.Image(token, imageInfo.getID());
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
                                    picListChange(imageInfo);
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

    public void picListChange(ImageInfo imageInfo) {
        switch (imageInfo.getIMG_TYPE()) {
            case "现场照片":
                picList_1.add(imageInfo);
                adapter_img_1.changeList_add(picList_1);
                break;
            case "签名":
                picList_2.add(imageInfo);
                adapter_img_2.changeList_add(picList_2);
                break;
            case "告知书&抽样单":
                picList_3.add(imageInfo);
                adapter_img_3.changeList_add(picList_3);
                break;
            default:
                break;
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

    public void ImgOnClick(String path) {
        showImage(path);
    }

    public void ImgOnLongClick(final int pos, final ImageInfo imageInfo) {
        // 通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        // 设置Title的图标
        builder.setIcon(R.mipmap.ic_launcher);
        // 设置Title的内容
        builder.setTitle("提示");
        // 设置Content来显示一个信息
        builder.setMessage("确定删除第" + (pos + 1) + "张图片?");
        // 设置一个PositiveButton
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                        delSamplingImg(pos, imageInfo);
                    }
                });
        // 设置一个NegativeButton
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                    }
                });
        // 显示出该对话框
        builder.show();
    }

    public void SignatureOnClick(int pos) {
        Intent intent_signature = new Intent();
        intent_signature.setClass(ImgUploadActivity.this, SignatureActivity.class);
        intent_signature.putExtra("NO", number);
        if (pos < picList_2_add.size() - 1 && pos > 1) {
            showImage(picList_2_add.get(pos).getPATH());
        } else {
            intent_signature.putExtra("type", picList_2_add.get(pos).getIMG_TYPE());
            /*if (pos == 0) {
                intent_signature.putExtra("type", picList_0_add.get(pos).getIMG_TYPE());
            } else if (pos == 1) {
                intent_signature.putExtra("type", "被抽样单位人员");
            } else {
                intent_signature.putExtra("type", "同行抽样人员");
            }*/
            startActivityForResult(intent_signature, TYPE_IMAGE_2);
        }
    }

    public void ImgAddOnClick(int pos, ImageInfoAdd imageInfoAdd) {
        if (pos == 0) {
            int TYPE_IMAGE;
            switch (imageInfoAdd.getIMG_TYPE()) {
                case "现场照片":
                    TYPE_IMAGE = TYPE_IMAGE_1;
                    break;
                case "告知书&抽样单":
                    TYPE_IMAGE = TYPE_IMAGE_3;
                    break;
                default:
                    TYPE_IMAGE = 0;
                    break;
            }
            if (TYPE_IMAGE == TYPE_IMAGE_1 || TYPE_IMAGE == TYPE_IMAGE_3)
                MultiImageSelector.create()
                        .multi()
                        .start(ImgUploadActivity.this, TYPE_IMAGE);
        } else {
            showImage(imageInfoAdd.getPATH());
        }
    }

    public void ImgAddOnLongClick(final int pos, final ImageInfoAdd imageInfoAdd) {
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
                        public void onClick(DialogInterface dialog, int which) {
                            //dialog.dismiss();
                            imgAddDel(pos, imageInfoAdd);
                            Snackbar.make(btn_uploadImg, "删除图片成功", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });
            // 设置一个NegativeButton
            builder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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

        picList_1_add = adapter_img_add_1.getImgList();
        picList_2_add = adapter_img_add_2.getImgList();
        picList_3_add = adapter_img_add_3.getImgList();

        //签名的张数
        int pic_0_num = 0;
        for (ImageInfoAdd pic : picList_2_add) {
            if (!pic.getPATH().equals("加号")) {
                pic_0_num++;
            }
        }

        if (picList_1_add.size() == 1 && pic_0_num == 0 && picList_3_add.size() == 1) {
            Snackbar.make(btn_uploadImg, "没有上传任何图片", Snackbar.LENGTH_LONG).setAction("Action",
                    null).show();
        } else if (picList_1_add.size() == 1 && picList_1.size() == 0) {
            Snackbar.make(btn_uploadImg, getResources().getString(R.string.img_type_1) +
                    "至少选择一张", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else if (picList_3_add.size() == 1 && picList_3.size() == 0) {
            Snackbar.make(btn_uploadImg, getResources().getString(R.string.img_type_3) +
                    "至少选择一张", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            picNum = picList_1_add.size() + picList_3_add.size() - 2 + pic_0_num;
            for (ImageInfoAdd pic : picList_1_add) {
                if (!pic.getPATH().equals("加号")) {
                    pic_compress(getResources().getString(R.string.img_type_1), pic.getPATH());
                }
            }
            for (ImageInfoAdd pic : picList_2_add) {
                if (!pic.getPATH().equals("加号")) {
                    pic_compress(getResources().getString(R.string.img_type_2), pic.getPATH());
                }
            }
            for (ImageInfoAdd pic : picList_3_add) {
                if (!pic.getPATH().equals("加号")) {
                    pic_compress(getResources().getString(R.string.img_type_3), pic.getPATH());
                }
            }
        }
    }

    public void pic_compress(final String type, final String filePath) {
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
                switch (type) {
                    case "现场照片":
                        for (int i = 0; i < picList_1_add.size(); i++) {
                            if (filePath.equals(picList_1_add.get(i).getPATH())) {
                                picList_1_add.get(i).setPATH(outfile);
                                adapter_img_add_1.changeList_add(picList_1_add);
                                break;
                            }
                        }
                        break;
                    case "签名":
                        for (int i = 0; i < picList_2_add.size(); i++) {
                            if (filePath.equals(picList_2_add.get(i).getPATH())) {
                                picList_2_add.get(i).setPATH(outfile);
                                adapter_img_add_2.changeList_add(picList_2_add);
                                break;
                            }
                        }
                        break;
                    case "告知书&抽样单":
                        for (int i = 0; i < picList_3_add.size(); i++) {
                            if (filePath.equals(picList_3_add.get(i).getPATH())) {
                                picList_3_add.get(i).setPATH(outfile);
                                adapter_img_add_3.changeList_add(picList_3_add);
                                break;
                            }
                        }
                        break;
                    default:
                        break;
                }
                attemptImgUpload(type, outfile);
            }
        });
    }

    public void attemptImgUpload(final String image_type, final String image_path) {
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
                                //上传成功将图片移至已上传
                                imgUploadSuccess(new ImageInfo(image_type, image_path),
                                        new ImageInfoAdd(image_type, image_path));
                                Log.v("图片", response.body().getMessage());
                            } else {
                                status.add("0");
                                fail_num++;
                                Log.v("图片", response.body().getMessage());
                            }
                            DialogUIUtils.dismiss(dialog_ImgUpload);
                            dialog_ImgUpload = DialogUIUtils.showLoading(_context, "已上传 " +
                                    status.size() + "/" + picNum, false, true, false, false);
                            dialog_ImgUpload.show();
                        } else {
                            Log.v("ImgUpload请求成功!", "response.body is null");
                        }
                        if ((status.size() + 8) >= (picList_1_add.size() + picList_2_add.size() +
                                picList_3_add.size())) {
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

    public void delSamplingImg(final int pos, final ImageInfo imageInfo) {
        if (NetworkUtil.isNetworkAvailable(_context)) {
            FDA_API request = HttpUtils.JsonApi();
            if (((MyApplication) getApplication()).getTOKEN() == null) {
                token = sharedPreferences.getString("TOKEN", null);
            } else {
                token = ((MyApplication) getApplication()).getTOKEN();
            }
            Call<DelImgStatus> call = request.delSamplingImg(token, imageInfo.getID());
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
                                imgDel(pos, imageInfo);
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

    public void imgDel(int pos, ImageInfo imageInfo) {
        switch (imageInfo.getIMG_TYPE()) {
            case "现场照片":
                adapter_img_1.removeItem(pos);
                break;
            case "签名":
                adapter_img_2.removeItem(pos);
                break;
            case "告知书&抽样单":
                adapter_img_3.removeItem(pos);
                break;
            default:
                break;
        }
    }

    public void imgAddDel(int pos, ImageInfoAdd imageInfo) {
        switch (imageInfo.getIMG_TYPE()) {
            case "现场照片":
                adapter_img_add_1.removeItem(pos);
                break;
            case "签名":
                adapter_img_add_2.removeItem(pos);
                break;
            case "告知书&抽样单":
                adapter_img_add_3.removeItem(pos);
                break;
            default:
                break;
        }
    }

    public void imgUploadSuccess(ImageInfo imageInfo, ImageInfoAdd imageInfoAdd) {
        picListChange(imageInfo);
        picListAddChange(imageInfoAdd);
    }

    public void picListAddChange(ImageInfoAdd imageInfoAdd) {
        switch (imageInfoAdd.getIMG_TYPE()) {
            case "现场照片":
                Iterator<ImageInfoAdd> iterator1 = picList_1_add.iterator();
                while (iterator1.hasNext()) {
                    ImageInfoAdd i = iterator1.next();
                    if (imageInfoAdd.getPATH().equals(i.getPATH())) {
                        iterator1.remove();//使用迭代器的删除方法删除
                    }
                }
                adapter_img_add_1.changeList_add(picList_1_add);
                break;
            case "签名":
                Iterator<ImageInfoAdd> iterator2 = picList_2_add.iterator();
                while (iterator2.hasNext()) {
                    ImageInfoAdd i = iterator2.next();
                    if (imageInfoAdd.getPATH().equals(i.getPATH())) {
                        iterator2.remove();//使用迭代器的删除方法删除
                    }
                }
                adapter_img_add_2.changeList_add(picList_2_add);
                break;
            case "告知书&抽样单":
                Iterator<ImageInfoAdd> iterator3 = picList_3_add.iterator();
                while (iterator3.hasNext()) {
                    ImageInfoAdd i = iterator3.next();
                    if (imageInfoAdd.getPATH().equals(i.getPATH())) {
                        iterator3.remove();//使用迭代器的删除方法删除
                    }
                }
                adapter_img_add_3.changeList_add(picList_3_add);
                break;
            default:
                break;
        }
    }
}