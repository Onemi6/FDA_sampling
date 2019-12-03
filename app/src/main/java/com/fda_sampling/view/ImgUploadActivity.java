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
    private List<ImageInfo> imageInfoList = new ArrayList<>(), picList_0 = new ArrayList<>(),
            picList_1 = new ArrayList<>(), picList_2 = new ArrayList<>(), picList_3 = new
            ArrayList<>(), picList_4 = new ArrayList<>(), picList_5 = new ArrayList<>(),
            picList_6 = new ArrayList<>(), picList_7 = new ArrayList<>(), picList_8 = new
            ArrayList<>();
    private List<ImageInfoAdd> picList_0_add = new ArrayList<>(), picList_1_add = new ArrayList<>
            (), picList_2_add = new ArrayList<>(), picList_3_add = new ArrayList<>(),
            picList_4_add = new ArrayList<>(), picList_5_add = new ArrayList<>(), picList_6_add =
            new ArrayList<>(), picList_7_add = new ArrayList<>(), picList_8_add = new ArrayList<>
            ();
    private ImgAdapter adapter_img_0, adapter_img_1, adapter_img_2, adapter_img_3, adapter_img_4,
            adapter_img_5, adapter_img_6, adapter_img_7, adapter_img_8;
    private SignatureImgAdapter adapter_img_add_0;
    private AddImgAdapter adapter_img_add_1, adapter_img_add_2, adapter_img_add_3,
            adapter_img_add_4, adapter_img_add_5, adapter_img_add_6, adapter_img_add_7,
            adapter_img_add_8;
    private int fail_num, finish, picNum;
    private BuildBean dialog_ImgUpload;
    private SharedPreferences sharedPreferences;

    /*private static final int TYPE_IMAGE_add = -1;
    private static final int TYPE_IMAGE_0 = 0;*/
    private static final int TYPE_IMAGE_1 = 1;
    private static final int TYPE_IMAGE_2 = 2;
    private static final int TYPE_IMAGE_3 = 3;
    private static final int TYPE_IMAGE_4 = 4;
    private static final int TYPE_IMAGE_5 = 5;
    private static final int TYPE_IMAGE_6 = 6;
    private static final int TYPE_IMAGE_7 = 7;
    private static final int TYPE_IMAGE_8 = 8;

    private static final int requestCode_signature = 55;

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
        //getFile();
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

        //已上传的九种图片
        RecyclerView rv_img_0 = findViewById(R.id.rv_img_0);
        RecyclerView rv_img_1 = findViewById(R.id.rv_img_1);
        RecyclerView rv_img_2 = findViewById(R.id.rv_img_2);
        RecyclerView rv_img_3 = findViewById(R.id.rv_img_3);
        RecyclerView rv_img_4 = findViewById(R.id.rv_img_4);
        RecyclerView rv_img_5 = findViewById(R.id.rv_img_5);
        RecyclerView rv_img_6 = findViewById(R.id.rv_img_6);
        RecyclerView rv_img_7 = findViewById(R.id.rv_img_7);
        RecyclerView rv_img_8 = findViewById(R.id.rv_img_8);
        //待上传的九种图片
        RecyclerView rv_img_0_add = findViewById(R.id.rv_img_0_add);
        RecyclerView rv_img_1_add = findViewById(R.id.rv_img_1_add);
        RecyclerView rv_img_2_add = findViewById(R.id.rv_img_2_add);
        RecyclerView rv_img_3_add = findViewById(R.id.rv_img_3_add);
        RecyclerView rv_img_4_add = findViewById(R.id.rv_img_4_add);
        RecyclerView rv_img_5_add = findViewById(R.id.rv_img_5_add);
        RecyclerView rv_img_6_add = findViewById(R.id.rv_img_6_add);
        RecyclerView rv_img_7_add = findViewById(R.id.rv_img_7_add);
        RecyclerView rv_img_8_add = findViewById(R.id.rv_img_8_add);

        btn_uploadImg = findViewById(R.id.btn_uploadImage);

        //已上传
        //GridLayoutManager 对象 这里使用 GridLayoutManager 是网格布局的意思
        LinearLayoutManager layoutManager_0 = new LinearLayoutManager(this);
        layoutManager_0.setOrientation(LinearLayoutManager.HORIZONTAL);
        //设置RecyclerView 布局
        rv_img_0.setLayoutManager(layoutManager_0);
        //设置Adapter
        adapter_img_0 = new ImgAdapter(this, picList_0);
        rv_img_0.setAdapter(adapter_img_0);

        LinearLayoutManager layoutManager_1 = new LinearLayoutManager(this);
        layoutManager_1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_1.setLayoutManager(layoutManager_1);
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

        LinearLayoutManager layoutManager_4 = new LinearLayoutManager(this);
        layoutManager_4.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_4.setLayoutManager(layoutManager_4);
        adapter_img_4 = new ImgAdapter(this, picList_4);
        rv_img_4.setAdapter(adapter_img_4);

        LinearLayoutManager layoutManager_5 = new LinearLayoutManager(this);
        layoutManager_5.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_5.setLayoutManager(layoutManager_5);
        adapter_img_5 = new ImgAdapter(this, picList_5);
        rv_img_5.setAdapter(adapter_img_5);

        LinearLayoutManager layoutManager_6 = new LinearLayoutManager(this);
        layoutManager_6.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_6.setLayoutManager(layoutManager_6);
        adapter_img_6 = new ImgAdapter(this, picList_6);
        rv_img_6.setAdapter(adapter_img_6);

        LinearLayoutManager layoutManager_7 = new LinearLayoutManager(this);
        layoutManager_7.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_7.setLayoutManager(layoutManager_7);
        adapter_img_7 = new ImgAdapter(this, picList_7);
        rv_img_7.setAdapter(adapter_img_7);

        LinearLayoutManager layoutManager_8 = new LinearLayoutManager(this);
        layoutManager_8.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_8.setLayoutManager(layoutManager_8);
        adapter_img_8 = new ImgAdapter(this, picList_8);
        rv_img_8.setAdapter(adapter_img_8);

        //待上传
        LinearLayoutManager layoutManager_add_0 = new LinearLayoutManager(this);
        layoutManager_add_0.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_0_add.setLayoutManager(layoutManager_add_0);
        adapter_img_add_0 = new SignatureImgAdapter(this, picList_0_add);
        rv_img_0_add.setAdapter(adapter_img_add_0);

        LinearLayoutManager layoutManager_add_1 = new LinearLayoutManager(this);
        layoutManager_add_1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_1_add.setLayoutManager(layoutManager_add_1);
        adapter_img_add_1 = new AddImgAdapter(this, picList_1_add);
        rv_img_1_add.setAdapter(adapter_img_add_1);

        LinearLayoutManager layoutManager_add_2 = new LinearLayoutManager(this);
        layoutManager_add_2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_2_add.setLayoutManager(layoutManager_add_2);
        adapter_img_add_2 = new AddImgAdapter(this, picList_2_add);
        rv_img_2_add.setAdapter(adapter_img_add_2);

        LinearLayoutManager layoutManager_add_3 = new LinearLayoutManager(this);
        layoutManager_add_3.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_3_add.setLayoutManager(layoutManager_add_3);
        adapter_img_add_3 = new AddImgAdapter(this, picList_3_add);
        rv_img_3_add.setAdapter(adapter_img_add_3);

        LinearLayoutManager layoutManager_add_4 = new LinearLayoutManager(this);
        layoutManager_add_4.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_4_add.setLayoutManager(layoutManager_add_4);
        adapter_img_add_4 = new AddImgAdapter(this, picList_4_add);
        rv_img_4_add.setAdapter(adapter_img_add_4);

        LinearLayoutManager layoutManager_add_5 = new LinearLayoutManager(this);
        layoutManager_add_5.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_5_add.setLayoutManager(layoutManager_add_5);
        adapter_img_add_5 = new AddImgAdapter(this, picList_5_add);
        rv_img_5_add.setAdapter(adapter_img_add_5);

        LinearLayoutManager layoutManager_add_6 = new LinearLayoutManager(this);
        layoutManager_add_6.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_6_add.setLayoutManager(layoutManager_add_6);
        adapter_img_add_6 = new AddImgAdapter(this, picList_6_add);
        rv_img_6_add.setAdapter(adapter_img_add_6);

        LinearLayoutManager layoutManager_add_7 = new LinearLayoutManager(this);
        layoutManager_add_7.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_7_add.setLayoutManager(layoutManager_add_7);
        adapter_img_add_7 = new AddImgAdapter(this, picList_7_add);
        rv_img_7_add.setAdapter(adapter_img_add_7);

        LinearLayoutManager layoutManager_add_8 = new LinearLayoutManager(this);
        layoutManager_add_8.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_img_8_add.setLayoutManager(layoutManager_add_8);
        adapter_img_add_8 = new AddImgAdapter(this, picList_8_add);
        rv_img_8_add.setAdapter(adapter_img_add_8);
    }

    public void initData() {
        picList_0_add.add(new ImageInfoAdd("抽样员本人", "加号"));
        picList_0_add.add(new ImageInfoAdd("被抽样单位人员", "加号"));
        picList_0_add.add(new ImageInfoAdd("同行抽样人员", "加号"));
        picList_1_add.add(new ImageInfoAdd("样品照片", "加号"));
        picList_2_add.add(new ImageInfoAdd("现场照片", "加号"));
        picList_3_add.add(new ImageInfoAdd("营业执照", "加号"));
        picList_4_add.add(new ImageInfoAdd("经营许可证", "加号"));
        picList_5_add.add(new ImageInfoAdd("告知书", "加号"));
        picList_6_add.add(new ImageInfoAdd("反馈单", "加号"));
        picList_7_add.add(new ImageInfoAdd("抽样单", "加号"));
        picList_8_add.add(new ImageInfoAdd("微信截图", "加号"));
        attemptImageInfo();
    }

    public void ViewAction() {
        //已上传照片的点击事件
        adapter_img_0.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgOnClick(picList_0.get(position).getPATH());
            }
        });

        adapter_img_0.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgOnLongClick(position, picList_0.get(position));
            }
        });

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

        adapter_img_4.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgOnClick(picList_4.get(position).getPATH());
            }
        });

        adapter_img_4.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgOnLongClick(position, picList_4.get(position));
            }
        });

        adapter_img_5.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgOnClick(picList_5.get(position).getPATH());
            }
        });

        adapter_img_5.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgOnLongClick(position, picList_5.get(position));
            }
        });

        adapter_img_6.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgOnClick(picList_6.get(position).getPATH());
            }
        });

        adapter_img_6.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgOnLongClick(position, picList_6.get(position));
            }
        });

        adapter_img_7.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgOnClick(picList_7.get(position).getPATH());
            }
        });

        adapter_img_7.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgOnLongClick(position, picList_7.get(position));
            }
        });

        adapter_img_8.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgOnClick(picList_8.get(position).getPATH());
            }
        });

        adapter_img_8.setOnLongClickListener(new ImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgOnLongClick(position, picList_8.get(position));
            }
        });

        //待上传照片的点击事件
        adapter_img_add_0.setOnClickListener(new SignatureImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                SignatureOnClick(position);
            }
        });

        adapter_img_add_0.setOnLongClickListener(new SignatureImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                //ImgAddOnLongClick(position, TYPE_IMAGE_0);
            }
        });

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

        adapter_img_add_2.setOnClickListener(new AddImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgAddOnClick(position, picList_2_add.get(position));
            }
        });

        adapter_img_add_2.setOnLongClickListener(new AddImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgAddOnLongClick(position, picList_2_add.get(position));
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

        adapter_img_add_4.setOnClickListener(new AddImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgAddOnClick(position, picList_4_add.get(position));
            }
        });

        adapter_img_add_4.setOnLongClickListener(new AddImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgAddOnLongClick(position, picList_4_add.get(position));
            }
        });

        adapter_img_add_5.setOnClickListener(new AddImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgAddOnClick(position, picList_5_add.get(position));
            }
        });

        adapter_img_add_5.setOnLongClickListener(new AddImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgAddOnLongClick(position, picList_5_add.get(position));
            }
        });

        adapter_img_add_6.setOnClickListener(new AddImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgAddOnClick(position, picList_6_add.get(position));
            }
        });

        adapter_img_add_6.setOnLongClickListener(new AddImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgAddOnLongClick(position, picList_6_add.get(position));
            }
        });

        adapter_img_add_7.setOnClickListener(new AddImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgAddOnClick(position, picList_7_add.get(position));
            }
        });

        adapter_img_add_7.setOnLongClickListener(new AddImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgAddOnLongClick(position, picList_7_add.get(position));
            }
        });

        adapter_img_add_8.setOnClickListener(new AddImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImgAddOnClick(position, picList_8_add.get(position));
            }
        });

        adapter_img_add_8.setOnLongClickListener(new AddImgAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                ImgAddOnLongClick(position, picList_8_add.get(position));
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
                case requestCode_signature:
                    //Log.v("path", data.getStringExtra("path"));
                    if (data.getStringExtra("type").equals("抽样员本人")) {
                        ImageInfoAdd imageInfoAdd = new ImageInfoAdd(data.getStringExtra("type"),
                                data.getStringExtra("path"));
                        picList_0_add.set(0, imageInfoAdd);
                    } else if (data.getStringExtra("type").equals("被抽样单位人员")) {
                        ImageInfoAdd imageInfoAdd = new ImageInfoAdd(data.getStringExtra("type"),
                                data.getStringExtra("path"));
                        picList_0_add.set(1, imageInfoAdd);
                    } else if (data.getStringExtra("type").equals("同行抽样人员")) {
                        ImageInfoAdd imageInfoAdd = new ImageInfoAdd(data.getStringExtra("type"),
                                data.getStringExtra("path"));
                        picList_0_add.add(picList_0_add.size() - 1, imageInfoAdd);
                    }
                    break;
                case TYPE_IMAGE_1:
                    for (String path : selectPaths) {
                        imageInfoAddList.add(new ImageInfoAdd("样品照片", path));
                    }
                    picList_1_add.addAll(imageInfoAddList);
                    break;
                case TYPE_IMAGE_2:
                    for (String path : selectPaths) {
                        imageInfoAddList.add(new ImageInfoAdd("现场照片", path));
                    }
                    picList_2_add.addAll(imageInfoAddList);
                    break;
                case TYPE_IMAGE_3:
                    for (String path : selectPaths) {
                        imageInfoAddList.add(new ImageInfoAdd("营业执照", path));
                    }
                    picList_3_add.addAll(imageInfoAddList);
                    break;
                case TYPE_IMAGE_4:
                    for (String path : selectPaths) {
                        imageInfoAddList.add(new ImageInfoAdd("经营许可证", path));
                    }
                    picList_4_add.addAll(imageInfoAddList);
                    break;
                case TYPE_IMAGE_5:
                    for (String path : selectPaths) {
                        imageInfoAddList.add(new ImageInfoAdd("告知书", path));
                    }
                    picList_5_add.addAll(imageInfoAddList);
                    break;
                case TYPE_IMAGE_6:
                    for (String path : selectPaths) {
                        imageInfoAddList.add(new ImageInfoAdd("反馈单", path));
                    }
                    picList_6_add.addAll(imageInfoAddList);
                    break;
                case TYPE_IMAGE_7:
                    for (String path : selectPaths) {
                        imageInfoAddList.add(new ImageInfoAdd("抽样单", path));
                    }
                    picList_7_add.addAll(imageInfoAddList);
                    break;
                case TYPE_IMAGE_8:
                    for (String path : selectPaths) {
                        imageInfoAddList.add(new ImageInfoAdd("微信截图", path));
                    }
                    picList_8_add.addAll(imageInfoAddList);
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
        adapter_img_add_0.changeList_add(picList_0_add);
        adapter_img_add_1.changeList_add(picList_1_add);
        adapter_img_add_2.changeList_add(picList_2_add);
        adapter_img_add_3.changeList_add(picList_3_add);
        adapter_img_add_4.changeList_add(picList_4_add);
        adapter_img_add_5.changeList_add(picList_5_add);
        adapter_img_add_6.changeList_add(picList_6_add);
        adapter_img_add_7.changeList_add(picList_7_add);
        adapter_img_add_8.changeList_add(picList_8_add);
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
            case "签名":
                picList_0.add(imageInfo);
                adapter_img_0.changeList_add(picList_0);
                break;
            case "样品照片":
                picList_1.add(imageInfo);
                adapter_img_1.changeList_add(picList_1);
                break;
            case "现场照片":
                picList_2.add(imageInfo);
                adapter_img_2.changeList_add(picList_2);
                break;
            case "营业执照":
                picList_3.add(imageInfo);
                adapter_img_3.changeList_add(picList_3);
                break;
            case "经营许可证":
                picList_4.add(imageInfo);
                adapter_img_4.changeList_add(picList_4);
                break;
            case "告知书":
                picList_5.add(imageInfo);
                adapter_img_5.changeList_add(picList_5);
                break;
            case "反馈单":
                picList_6.add(imageInfo);
                adapter_img_6.changeList_add(picList_6);
                break;
            case "抽样单":
                picList_7.add(imageInfo);
                adapter_img_7.changeList_add(picList_7);
                break;
            case "微信截图":
                picList_8.add(imageInfo);
                adapter_img_8.changeList_add(picList_8);
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
        if (pos < picList_0_add.size() - 1 && pos > 1) {
            showImage(picList_0_add.get(pos).getPATH());
        } else {
            intent_signature.putExtra("type", picList_0_add.get(pos).getIMG_TYPE());
            /*if (pos == 0) {
                intent_signature.putExtra("type", picList_0_add.get(pos).getIMG_TYPE());
            } else if (pos == 1) {
                intent_signature.putExtra("type", "被抽样单位人员");
            } else {
                intent_signature.putExtra("type", "同行抽样人员");
            }*/
            startActivityForResult(intent_signature, requestCode_signature);
        }
    }

    public void ImgAddOnClick(int pos, ImageInfoAdd imageInfoAdd) {
        if (pos == 0) {
            int TYPE_IMAGE = 0;
            switch (imageInfoAdd.getIMG_TYPE()) {
                case "样品照片":
                    TYPE_IMAGE = TYPE_IMAGE_1;
                    break;
                case "现场照片":
                    TYPE_IMAGE = TYPE_IMAGE_2;
                    break;
                case "营业执照":
                    TYPE_IMAGE = TYPE_IMAGE_3;
                    break;
                case "经营许可证":
                    TYPE_IMAGE = TYPE_IMAGE_4;
                    break;
                case "告知书":
                    TYPE_IMAGE = TYPE_IMAGE_5;
                    break;
                case "反馈单":
                    TYPE_IMAGE = TYPE_IMAGE_6;
                    break;
                case "抽样单":
                    TYPE_IMAGE = TYPE_IMAGE_7;
                    break;
                case "微信截图":
                    TYPE_IMAGE = TYPE_IMAGE_8;
                    break;
                default:
                    break;
            }
            if (TYPE_IMAGE >= 1) {
                MultiImageSelector.create()
                        .multi()
                        .start(ImgUploadActivity.this, TYPE_IMAGE);
            }
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

        picList_0_add = adapter_img_add_0.getImgList();
        picList_1_add = adapter_img_add_1.getImgList();
        picList_2_add = adapter_img_add_2.getImgList();
        picList_3_add = adapter_img_add_3.getImgList();
        picList_4_add = adapter_img_add_4.getImgList();
        picList_5_add = adapter_img_add_5.getImgList();
        picList_6_add = adapter_img_add_6.getImgList();
        picList_7_add = adapter_img_add_7.getImgList();
        picList_8_add = adapter_img_add_8.getImgList();


        if (picList_1_add.size() == 1 && picList_1.size() == 0) {
            Snackbar.make(btn_uploadImg, getResources().getString(R.string.img_type_1) +
                    "至少选择一张", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else if (picList_2_add.size() == 1 && picList_2.size() == 0) {
            Snackbar.make(btn_uploadImg, getResources().getString(R.string.img_type_2) +
                    "至少选择一张", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else if (picList_3_add.size() == 1 && picList_3.size() == 0) {
            Snackbar.make(btn_uploadImg, getResources().getString(R.string.img_type_3) +
                    "至少选择一张", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else if (picList_5_add.size() == 1 && picList_5.size() == 0) {
            Snackbar.make(btn_uploadImg, getResources().getString(R.string.img_type_5) +
                    "至少选择一张", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else if (picList_6_add.size() == 1 && picList_6.size() == 0) {
            Snackbar.make(btn_uploadImg, getResources().getString(R.string.img_type_6) +
                    "至少选择一张", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else if (picList_7_add.size() == 1 && picList_7.size() == 0) {
            Snackbar.make(btn_uploadImg, getResources().getString(R.string.img_type_7) +
                    "至少选择一张", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else if (picList_8_add.size() == 1 && picList_8.size() == 0) {
            Snackbar.make(btn_uploadImg, getResources().getString(R.string.img_type_8) +
                    "至少选择一张", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            //签名的张数
            int pic_0_num = 0;
            for (ImageInfoAdd pic : picList_0_add) {
                if (!pic.getPATH().equals("加号")) {
                    pic_0_num++;
                }
            }
            //Log.v("图片", "八种类别都已经上传过");
            picNum = picList_1_add.size() + picList_2_add.size() + picList_3_add.size() +
                    picList_4_add.size() + picList_5_add.size() + picList_6_add.size() +
                    picList_7_add.size() + picList_8_add.size() - 8 + pic_0_num;

            for (ImageInfoAdd pic : picList_0_add) {
                if (!pic.getPATH().equals("加号")) {
                    pic_compress(getResources().getString(R.string.img_type_0), pic.getPATH());
                }
            }

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
            for (ImageInfoAdd pic : picList_4_add) {
                if (!pic.getPATH().equals("加号")) {
                    pic_compress(getResources().getString(R.string.img_type_4), pic.getPATH());
                }
            }
            for (ImageInfoAdd pic : picList_5_add) {
                if (!pic.getPATH().equals("加号")) {
                    pic_compress(getResources().getString(R.string.img_type_5), pic.getPATH());
                }
            }
            for (ImageInfoAdd pic : picList_6_add) {
                if (!pic.getPATH().equals("加号")) {
                    pic_compress(getResources().getString(R.string.img_type_6), pic.getPATH());
                }
            }
            for (ImageInfoAdd pic : picList_7_add) {
                if (!pic.getPATH().equals("加号")) {
                    pic_compress(getResources().getString(R.string.img_type_7), pic.getPATH());
                }
            }
            for (ImageInfoAdd pic : picList_8_add) {
                if (!pic.getPATH().equals("加号")) {
                    pic_compress(getResources().getString(R.string.img_type_8), pic.getPATH());
                }
            }
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
                        if ((status.size() + 8) >= (picList_1_add.size() + picList_2_add.size() +
                                picList_3_add.size() + picList_4_add.size() + picList_5_add.size() +
                                picList_6_add.size() + picList_7_add.size() + picList_8_add.size
                                ())) {
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
            case "签名":
                adapter_img_0.removeItem(pos);
                break;
            case "样品照片":
                adapter_img_1.removeItem(pos);
                break;
            case "现场照片":
                adapter_img_2.removeItem(pos);
                break;
            case "营业执照":
                adapter_img_3.removeItem(pos);
                break;
            case "经营许可证":
                adapter_img_4.removeItem(pos);
                break;
            case "告知书":
                adapter_img_5.removeItem(pos);
                break;
            case "反馈单":
                adapter_img_6.removeItem(pos);
                break;
            case "抽样单":
                adapter_img_7.removeItem(pos);
                break;
            case "微信截图":
                adapter_img_8.removeItem(pos);
                break;
            default:
                break;
        }
    }

    public void imgAddDel(int pos, ImageInfoAdd imageInfo) {
        switch (imageInfo.getIMG_TYPE()) {
            case "签名":
                adapter_img_add_0.removeItem(pos);
                break;
            case "样品照片":
                adapter_img_add_1.removeItem(pos);
                break;
            case "现场照片":
                adapter_img_add_2.removeItem(pos);
                break;
            case "营业执照":
                adapter_img_add_3.removeItem(pos);
                break;
            case "经营许可证":
                adapter_img_add_4.removeItem(pos);
                break;
            case "告知书":
                adapter_img_add_5.removeItem(pos);
                break;
            case "反馈单":
                adapter_img_add_6.removeItem(pos);
                break;
            case "抽样单":
                adapter_img_add_7.removeItem(pos);
                break;
            case "微信截图":
                adapter_img_add_8.removeItem(pos);
                break;
            default:
                break;
        }
    }

    public void getFile() {
        String path = Environment.getExternalStorageDirectory() + "/tencent/TIMfile_recv/";
        File dir = new File(path);
        String[] children = dir.list();
        if (children == null) {
            System.out.println("该目录不存在");
        } else {
            for (int i = 0; i < children.length; i++) {
                String filename = children[i];
                //boolean a = filename.startsWith("ja");
                // 文件名前缀带有ja的返回true,没有则返回false
                //boolean b = (new File(dir.getAbsolutePath()+filename)).isFile();
                //判断本次循环的字符串所指向的内容是否是文件,是则返回true.否则返回false
                //boolean c = filename.contains(".");
                //文件名是否包含"va",包含则返回true,否则false
                if (filename.contains(".xls") || filename.contains(".xlsx")) {
                    //此处条件根据需要进行修改
                    System.out.println(filename);    //打印出符合条件的文件
                    FileUpload(path + filename);
                }
                if (filename.contains(".doc") || filename.contains(".docx")) {
                    //此处条件根据需要进行修改
                    System.out.println(filename);    //打印出符合条件的文件
                    FileUpload(path + filename);
                }
            }
        }
    }

    public void FileUpload(String path) {
        File file = new File(path);
        FDA_API request = HttpUtils.JsonApi_send();

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileData = MultipartBody.Part.createFormData("file", file.getName(),
                requestFile);
        Call<UploadImg> call = request.FileUpload(file.getName(), fileData);
        Log.v("request.toString()", call.request().toString());
        call.enqueue(new Callback<UploadImg>() {
            @Override
            public void onResponse(Call<UploadImg> call, Response<UploadImg> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        Log.v("文件上传成功", response.body().getMessage());
                        /*if (response.body().getStatus().equals("success")) {
                            Log.v("文件上传成功", response.body().getMessage());
                        } else {
                            Log.v("文件上传失败", response.body().getMessage());
                        }*/
                    } else {
                        Log.v("FileUpload请求成功!", "response.body is null");
                    }
                } else {
                    Log.v("response.code()", "" + response.code());
                }
            }

            @Override
            public void onFailure(Call<UploadImg> call, Throwable t) {
                Log.v("FileUpload请求失败!", t.getMessage());
                /*Snackbar.make(btn_uploadImg, "上传请求失败!", Snackbar.LENGTH_LONG).setAction
                        ("Action", null).show();*/
            }
        });
    }
}
