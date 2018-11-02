package com.fda_sampling.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.fda_sampling.R;
import com.fda_sampling.model.InfoList;
import com.fda_sampling.model.Info_Detail;
import com.fda_sampling.util.FileRW;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    private Info_Detail info;
    private int pos;
    private String str_outname = null;
    private Context context;
    private Toolbar toolbar;
    private Spinner sp_SAMPLE_TYPE, sp_DOMESTIC_AREA, sp_SUPPLIER_PERMIT_TYPE, sp_DRAW_ADDR,
            sp_SAMPLE_SOURCE,
            sp_SAMPLE_PROPERTY, sp_SAMPLE_STYLE, sp_DATE_PRODUCT_TYPE, sp_I_AND_O, sp_SAMPLE_STATUS,
            sp_PACK_TYPE, sp_SAVE_MODE, sp_PACK, sp_DRAW_METHOD;
    private ArrayAdapter ada_SAMPLE_TYPE, ada_DOMESTIC_AREA, ada_SUPPLIER_PERMIT_TYPE,
            ada_DRAW_ADDR, ada_SAMPLE_SOURCE,
            ada_SAMPLE_PROPERTY, ada_SAMPLE_STYLE, ada_DATE_PRODUCT_TYPE, ada_I_AND_O,
            ada_SAMPLE_STATUS,
            ada_PACK_TYPE, ada_SAVE_MODE, ada_PACK, ada_DRAW_METHOD;
    private EditText et_CUSTOM_NO, et_NO, et_GOODS_NAME, et_BUSINESS_SOURCE, et_DRAW_NO,
            et_SUPPLIER,
            et_SUPPLIER_ADDR, et_SUPPLIER_LEGAL, et_ANNUAL_SALES, et_BUSINESS_LICENCE,
            et_SUPPLIER_PERSON,
            et_SUPPLIER_PERMIT_CODE, et_SUPPLIER_PHONE, et_SUPPLIER_FAX, et_SUPPLIER_ZIPCODE,
            et_TRADEMARK,
            et_DATE_PRODUCT, et_SAMPLE_MODEL, et_SAMPLE_NUMBER, et_EXPIRATIONDATE, et_TEST_FILE_NO,
            et_SAMPLE_CLASS, et_PRODUCTION_CERTIFICATE, et_UNIVALENT, et_DRAW_NUM, et_DRAW_AMOUNT,
            et_STORAGESITE, et_MANU_COMPANY, et_MANU_COMPANY_PHONE, et_MANU_COMPANY_ADDR,
            et_SAMPLE_CLOSE_DATE,
            et_SAMPLE_ADDR, et_DRAW_ORG, et_DRAW_ORG_ADDR, et_DRAW_PERSON, et_DRAW_PHONE,
            et_DRAW_FAX,
            et_DRAW_ZIPCODE, et_REMARK, et_DRAW_DATE, et_GOODS_TYPE, et_DRAW_MAN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_details);
        context = this;
        pos = InfoList.position;
        if (pos >= 0) {
            info = InfoList.list_info.get(pos);
        }
        et_CUSTOM_NO = findViewById(R.id.details_CUSTOM_NO);
        et_NO = findViewById(R.id.details_NO);
        et_GOODS_NAME = findViewById(R.id.details_GOODS_NAME);
        et_BUSINESS_SOURCE = findViewById(R.id.details_BUSINESS_SOURCE);
        et_DRAW_NO = findViewById(R.id.details_DRAW_NO);
        sp_SAMPLE_TYPE = findViewById(R.id.details_SAMPLE_TYPE);
        et_SUPPLIER = findViewById(R.id.details_SUPPLIER);
        sp_DOMESTIC_AREA = findViewById(R.id.details_DOMESTIC_AREA);
        et_SUPPLIER_ADDR = findViewById(R.id.details_SUPPLIER_ADDR);
        et_SUPPLIER_LEGAL = findViewById(R.id.details_SUPPLIER_LEGAL);
        et_ANNUAL_SALES = findViewById(R.id.details_ANNUAL_SALES);
        et_BUSINESS_LICENCE = findViewById(R.id.details_BUSINESS_LICENCE);
        et_SUPPLIER_PERSON = findViewById(R.id.details_SUPPLIER_PERSON);
        sp_SUPPLIER_PERMIT_TYPE = findViewById(R.id.details_SUPPLIER_PERMIT_TYPE);
        et_SUPPLIER_PERMIT_CODE = findViewById(R.id.details_SUPPLIER_PERMIT_CODE);
        et_SUPPLIER_PHONE = findViewById(R.id.details_SUPPLIER_PHONE);
        et_SUPPLIER_FAX = findViewById(R.id.details_SUPPLIER_FAX);
        et_SUPPLIER_ZIPCODE = findViewById(R.id.details_SUPPLIER_ZIPCODE);
        sp_DRAW_ADDR = findViewById(R.id.details_DRAW_ADDR);
        sp_SAMPLE_SOURCE = findViewById(R.id.details_SAMPLE_SOURCE);
        sp_SAMPLE_PROPERTY = findViewById(R.id.details_SAMPLE_PROPERTY);
        sp_SAMPLE_STYLE = findViewById(R.id.details_SAMPLE_STYLE);
        et_TRADEMARK = findViewById(R.id.details_TRADEMARK);
        sp_DATE_PRODUCT_TYPE = findViewById(R.id.details_DATE_PRODUCT_TYPE);
        et_DATE_PRODUCT = findViewById(R.id.details_DATE_PRODUCT);
        et_SAMPLE_MODEL = findViewById(R.id.details_SAMPLE_MODEL);
        et_SAMPLE_NUMBER = findViewById(R.id.details_SAMPLE_NUMBER);
        et_EXPIRATIONDATE = findViewById(R.id.details_EXPIRATIONDATE);
        et_TEST_FILE_NO = findViewById(R.id.details_TEST_FILE_NO);
        et_SAMPLE_CLASS = findViewById(R.id.details_SAMPLE_CLASS);
        et_PRODUCTION_CERTIFICATE = findViewById(R.id.details_PRODUCTION_CERTIFICATE);
        et_UNIVALENT = findViewById(R.id.details_UNIVALENT);
        sp_I_AND_O = findViewById(R.id.details_I_AND_O);
        et_DRAW_NUM = findViewById(R.id.details_DRAW_NUM);
        et_DRAW_AMOUNT = findViewById(R.id.details_DRAW_AMOUNT);
        et_STORAGESITE = findViewById(R.id.details_STORAGESITE);
        sp_SAMPLE_STATUS = findViewById(R.id.details_SAMPLE_STATUS);
        sp_PACK_TYPE = findViewById(R.id.details_PACK_TYPE);
        et_MANU_COMPANY = findViewById(R.id.details_MANU_COMPANY);
        et_MANU_COMPANY_PHONE = findViewById(R.id.details_MANU_COMPANY_PHONE);
        et_MANU_COMPANY_ADDR = findViewById(R.id.details_MANU_COMPANY_ADDR);
        sp_SAVE_MODE = findViewById(R.id.details_SAVE_MODE);
        et_SAMPLE_CLOSE_DATE = findViewById(R.id.details_SAMPLE_CLOSE_DATE);
        et_SAMPLE_ADDR = findViewById(R.id.details_SAMPLE_ADDR);
        sp_PACK = findViewById(R.id.details_PACK);
        sp_DRAW_METHOD = findViewById(R.id.details_DRAW_METHOD);
        et_DRAW_ORG = findViewById(R.id.details_DRAW_ORG);
        et_DRAW_ORG_ADDR = findViewById(R.id.details_DRAW_ORG_ADDR);
        et_DRAW_PERSON = findViewById(R.id.details_DRAW_PERSON);
        et_DRAW_PHONE = findViewById(R.id.details_DRAW_PHONE);
        et_DRAW_FAX = findViewById(R.id.details_DRAW_FAX);
        et_DRAW_ZIPCODE = findViewById(R.id.details_DRAW_ZIPCODE);
        et_REMARK = findViewById(R.id.details_REMARK);
        et_DRAW_DATE = findViewById(R.id.details_DRAW_DATE);
        et_GOODS_TYPE = findViewById(R.id.details_GOODS_TYPE);
        et_DRAW_MAN = findViewById(R.id.details_DRAW_MAN);

        ada_SAMPLE_TYPE = ArrayAdapter.createFromResource(context, R.array.SAMPLE_TYPE, android.R
                .layout.simple_spinner_dropdown_item);
        sp_SAMPLE_TYPE.setAdapter(ada_SAMPLE_TYPE);
        ada_DOMESTIC_AREA = ArrayAdapter.createFromResource(context, R.array.DOMESTIC_AREA,
                android.R.layout.simple_spinner_dropdown_item);
        sp_DOMESTIC_AREA.setAdapter(ada_DOMESTIC_AREA);
        ada_SUPPLIER_PERMIT_TYPE = ArrayAdapter.createFromResource(context, R.array
                .SUPPLIER_PERMIT_TYPE, android.R.layout.simple_spinner_dropdown_item);
        sp_SUPPLIER_PERMIT_TYPE.setAdapter(ada_SUPPLIER_PERMIT_TYPE);
        ada_DRAW_ADDR = ArrayAdapter.createFromResource(context, R.array.DRAW_ADDR, android.R
                .layout.simple_spinner_dropdown_item);
        sp_DRAW_ADDR.setAdapter(ada_DRAW_ADDR);
        ada_SAMPLE_SOURCE = ArrayAdapter.createFromResource(context, R.array.SAMPLE_SOURCE,
                android.R.layout.simple_spinner_dropdown_item);
        sp_SAMPLE_SOURCE.setAdapter(ada_SAMPLE_SOURCE);
        ada_SAMPLE_PROPERTY = ArrayAdapter.createFromResource(context, R.array.SAMPLE_PROPERTY,
                android.R.layout.simple_spinner_dropdown_item);
        sp_SAMPLE_PROPERTY.setAdapter(ada_SAMPLE_PROPERTY);
        ada_SAMPLE_STYLE = ArrayAdapter.createFromResource(context, R.array.SAMPLE_STYLE, android
                .R.layout.simple_spinner_dropdown_item);
        sp_SAMPLE_STYLE.setAdapter(ada_SAMPLE_STYLE);
        ada_DATE_PRODUCT_TYPE = ArrayAdapter.createFromResource(context, R.array
                .DATE_PRODUCT_TYPE, android.R.layout.simple_spinner_dropdown_item);
        sp_DATE_PRODUCT_TYPE.setAdapter(ada_DATE_PRODUCT_TYPE);
        ada_I_AND_O = ArrayAdapter.createFromResource(context, R.array.I_AND_O, android.R.layout
                .simple_spinner_dropdown_item);
        sp_I_AND_O.setAdapter(ada_I_AND_O);
        ada_SAMPLE_STATUS = ArrayAdapter.createFromResource(context, R.array.SAMPLE_STATUS,
                android.R.layout.simple_spinner_dropdown_item);
        sp_SAMPLE_STATUS.setAdapter(ada_SAMPLE_STATUS);
        ada_PACK_TYPE = ArrayAdapter.createFromResource(context, R.array.PACK_TYPE, android.R
                .layout.simple_spinner_dropdown_item);
        sp_PACK_TYPE.setAdapter(ada_PACK_TYPE);
        ada_SAVE_MODE = ArrayAdapter.createFromResource(context, R.array.SAVE_MODE, android.R
                .layout.simple_spinner_dropdown_item);
        sp_SAVE_MODE.setAdapter(ada_SAVE_MODE);
        ada_PACK = ArrayAdapter.createFromResource(context, R.array.PACK, android.R.layout
                .simple_spinner_dropdown_item);
        sp_PACK.setAdapter(ada_PACK);
        ada_DRAW_METHOD = ArrayAdapter.createFromResource(context, R.array.DRAW_METHOD, android.R
                .layout.simple_spinner_dropdown_item);
        sp_DRAW_METHOD.setAdapter(ada_DRAW_METHOD);

        toolbar = findViewById(R.id.toolbar_details);
        toolbar.setTitle("详细信息");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //设置toolbar
        setSupportActionBar(toolbar);
        //左边的小箭头（注意需要在setSupportActionBar(toolbar)之后才有效果）
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white);
        //菜单点击事件（注意需要在setSupportActionBar(toolbar)之后才有效果）
        //toolbar.setOnMenuItemClickListener(onMenuItemClick);

        initdata();
    }

    public void dodoc(String name_in, String name_out, Map<String, Object> map) throws Exception {
        String ALBUM_PATH;
        boolean sdCardExist = android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            ALBUM_PATH = Environment.getExternalStorageDirectory() + "/FDA_doc/";
        } else {
            ALBUM_PATH = this.getCacheDir().toString() + "/";
        }
        File dir = new File(ALBUM_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String outpath = Environment.getExternalStorageDirectory() + "/FDA_doc/"
                + name_out;
        //InputStream is = new FileInputStream(path);
        InputStream is = getClass().getResourceAsStream(
                "/assets/" + name_in);
        HWPFDocument doc = new HWPFDocument(is);
        Range range = doc.getRange();
        // 替换文本内容
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if ((entry.getValue()) instanceof String) {
                // 替换文本
                range.replaceText(entry.getKey(), entry.getValue()
                        .toString());
            }
        }

        OutputStream os = new FileOutputStream(outpath);
        doc.write(os);
        //Log.v("doc", doc.getRange().text());
        this.closeStream(is);
        this.closeStream(os);
    }

    public void FormatData() throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        Field[] fields = info.getClass().getDeclaredFields();
        String name;
        Field f;
        Object value;
        for (int i = 0; i < fields.length; i++) {
            f = fields[i];
            f.setAccessible(true);
            // 获取属性的名字
            name = f.getName();
            // 获取属性类型
            //type = f.getGenericType().toString();
            // 将属性的首字母大写
            name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1)
                    .toUpperCase());
            //if (type.equals("class java.lang.Boolean") || type.equals("boolean")) {
            //Log.v(name, "" + type);
            Method getMethod = info.getClass().getMethod("get" + name);
            //Log.v("getMethod", getMethod.getName().toString());
            value = getMethod.invoke(info);
            if (value != null && (value.equals("true") || value.equals("false"))) {
                Method setMethod = info.getClass().getMethod("set" + name, String.class);
                if (value.equals("true")) {
                    //执行该set方法
                    setMethod.invoke(info, "☑");
                    //value = getMethod.invoke(info);
                } else if (value.equals("false")) {
                    //执行该set方法
                    setMethod.invoke(info, "□");
                    //value = getMethod.invoke(info);
                }
                //Log.v(name, "" + value);
            } else if (value == null) {
                Method setMethod = info.getClass().getMethod("set" + name, String.class);
                setMethod.invoke(info, " ");
            }
        }
    }

    public void ChangeValue(int num, String str) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Field[] fields = info.getClass().getDeclaredFields();
        String name;
        Field f;
        switch (num) {
            case 1:
                for (int i = 0; i < fields.length; i++) {
                    f = fields[i];
                    f.setAccessible(true);
                    name = f.getName();
                    if (name.indexOf("任务类别") != -1) {
                        Method setMethod = info.getClass().getMethod("set" + name, String.class);
                        if (name.indexOf(str) != -1) {
                            //执行该set方法
                            setMethod.invoke(info, "true");
                        } else {
                            //执行该set方法
                            setMethod.invoke(info, "false");
                        }
                    }
                }
                break;
            case 2:
                for (int i = 0; i < fields.length; i++) {
                    f = fields[i];
                    f.setAccessible(true);
                    name = f.getName();
                    if (name.indexOf("区域类型") != -1) {
                        Method setMethod = info.getClass().getMethod("set" + name, String.class);
                        if (name.indexOf(str) != -1) {
                            //执行该set方法
                            setMethod.invoke(info, "true");
                        } else {
                            //执行该set方法
                            setMethod.invoke(info, "false");
                        }
                    }
                }
                break;
            case 3:
                for (int i = 0; i < fields.length; i++) {
                    f = fields[i];
                    f.setAccessible(true);
                    name = f.getName();
                    if (name.indexOf("许可证") != -1) {
                        Method setMethod = info.getClass().getMethod("set" + name, String.class);
                        if (name.indexOf(str) != -1) {
                            //执行该set方法
                            setMethod.invoke(info, "true");
                        } else {
                            //执行该set方法
                            setMethod.invoke(info, "false");
                        }
                    }
                }
                break;
            case 4:
                String[] strs = str.split(":");
                if (strs[1].equals("其他")) {
                    if (strs[0].equals("流通环节")) {
                        str = "流通环节_其他";
                    } else if (strs[0].equals("餐饮环节")) {
                        str = "餐饮环节_其他";
                    }
                } else if (strs[1].equals("成品库(待检区)")) {
                    str = "成品库_待检";
                } else if (strs[1].equals("成品库(已检区)")) {
                    str = "成品库_已检";
                } else if (strs[1].equals("餐馆(特大型餐馆)")) {
                    str = "餐馆_特大型";
                } else if (strs[1].equals("餐馆(大型餐馆)")) {
                    str = "餐馆_大型";
                } else if (strs[1].equals("餐馆(中型餐馆)")) {
                    str = "餐馆_中型";
                } else if (strs[1].equals("餐馆(小型餐馆)")) {
                    str = "餐馆_小型";
                } else if (strs[1].equals("食堂(机关食堂)")) {
                    str = "机关食堂";
                } else if (strs[1].equals("食堂(学校/托幼食堂)")) {
                    str = "学校食堂";
                } else if (strs[1].equals("食堂(企事业单位食堂)")) {
                    str = "企事业单位食堂";
                } else if (strs[1].equals("食堂(建筑工地食堂)")) {
                    str = "建筑工地食堂";
                } else {
                    str = strs[1];
                }
                for (int i = 0; i < fields.length; i++) {
                    f = fields[i];
                    f.setAccessible(true);
                    name = f.getName();
                    if (name.indexOf("抽样地点") != -1) {
                        Method setMethod = info.getClass().getMethod("set" + name, String.class);
                        if (name.indexOf(str) != -1) {
                            setMethod.invoke(info, "true");
                        } else {
                            setMethod.invoke(info, "false");
                        }
                    }
                }
                break;
            case 5:
                for (int i = 0; i < fields.length; i++) {
                    f = fields[i];
                    f.setAccessible(true);
                    name = f.getName();
                    if (name.indexOf("样品来源") != -1) {
                        Method setMethod = info.getClass().getMethod("set" + name, String.class);
                        if (name.indexOf(str) != -1) {
                            //执行该set方法
                            setMethod.invoke(info, "true");
                        } else {
                            //执行该set方法
                            setMethod.invoke(info, "false");
                        }
                    }
                }
                break;
            case 6:
                for (int i = 0; i < fields.length; i++) {
                    f = fields[i];
                    f.setAccessible(true);
                    name = f.getName();
                    if (name.indexOf("样品属性") != -1) {
                        Method setMethod = info.getClass().getMethod("set" + name, String.class);
                        if (name.indexOf(str) != -1) {
                            //执行该set方法
                            setMethod.invoke(info, "true");
                        } else {
                            //执行该set方法
                            setMethod.invoke(info, "false");
                        }
                    }
                }
                break;
            case 7:
                for (int i = 0; i < fields.length; i++) {
                    f = fields[i];
                    f.setAccessible(true);
                    name = f.getName();
                    if (name.indexOf("样品类型") != -1) {
                        Method setMethod = info.getClass().getMethod("set" + name, String.class);
                        if (name.indexOf(str) != -1) {
                            //执行该set方法
                            setMethod.invoke(info, "true");
                        } else {
                            //执行该set方法
                            setMethod.invoke(info, "false");
                        }
                    }
                }
                break;
            case 8:
                for (int i = 0; i < fields.length; i++) {
                    f = fields[i];
                    f.setAccessible(true);
                    name = f.getName();
                    if (name.indexOf("样品信息") != -1) {
                        Method setMethod = info.getClass().getMethod("set" + name, String.class);
                        if (name.indexOf(str) != -1) {
                            //执行该set方法
                            setMethod.invoke(info, "true");
                        } else {
                            //执行该set方法
                            setMethod.invoke(info, "false");
                        }
                    }
                }
                break;
            case 9:
                for (int i = 0; i < fields.length; i++) {
                    f = fields[i];
                    f.setAccessible(true);
                    name = f.getName();
                    if (name.indexOf("是否出口") != -1) {
                        Method setMethod = info.getClass().getMethod("set" + name, String.class);
                        if (name.indexOf("_" + str) != -1) {
                            //执行该set方法
                            setMethod.invoke(info, "true");
                        } else {
                            //执行该set方法
                            setMethod.invoke(info, "false");
                        }
                    }
                }
                break;
            case 10:
                for (int i = 0; i < fields.length; i++) {
                    f = fields[i];
                    f.setAccessible(true);
                    name = f.getName();
                    if (name.indexOf("样品形态") != -1) {
                        Method setMethod = info.getClass().getMethod("set" + name, String.class);
                        if (name.indexOf("_" + str) != -1) {
                            //执行该set方法
                            setMethod.invoke(info, "true");
                        } else {
                            //执行该set方法
                            setMethod.invoke(info, "false");
                        }
                    }
                }
                break;
            case 11:
                for (int i = 0; i < fields.length; i++) {
                    f = fields[i];
                    f.setAccessible(true);
                    name = f.getName();
                    if (name.indexOf("包装分类") != -1) {
                        Method setMethod = info.getClass().getMethod("set" + name, String.class);
                        if (name.indexOf(str) != -1) {
                            //执行该set方法
                            setMethod.invoke(info, "true");
                        } else {
                            //执行该set方法
                            setMethod.invoke(info, "false");
                        }
                    }
                }
                break;
            case 12:
                for (int i = 0; i < fields.length; i++) {
                    f = fields[i];
                    f.setAccessible(true);
                    name = f.getName();
                    if (name.indexOf("样品储存条件") != -1) {
                        Method setMethod = info.getClass().getMethod("set" + name, String.class);
                        if (name.indexOf(str) != -1) {
                            //执行该set方法
                            setMethod.invoke(info, "true");
                        } else {
                            //执行该set方法
                            setMethod.invoke(info, "false");
                        }
                    }
                }
                break;
            case 13:
                for (int i = 0; i < fields.length; i++) {
                    f = fields[i];
                    f.setAccessible(true);
                    name = f.getName();
                    if (name.indexOf("抽样样品包装") != -1) {
                        Method setMethod = info.getClass().getMethod("set" + name, String.class);
                        if (name.indexOf(str) != -1) {
                            //执行该set方法
                            setMethod.invoke(info, "true");
                        } else {
                            //执行该set方法
                            setMethod.invoke(info, "false");
                        }
                    }
                }
                break;
            case 14:
                for (int i = 0; i < fields.length; i++) {
                    f = fields[i];
                    f.setAccessible(true);
                    name = f.getName();
                    if (name.indexOf("抽样方式") != -1) {
                        Method setMethod = info.getClass().getMethod("set" + name, String.class);
                        if (name.indexOf("_" + str) != -1) {
                            //执行该set方法
                            setMethod.invoke(info, "true");
                        } else {
                            //执行该set方法
                            setMethod.invoke(info, "false");
                        }
                    }
                }
                break;
        }
    }

    public void doOpenWord() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        String fileMimeType = "application/msword";
        intent.setDataAndType(
                Uri.fromFile(new File("/mnt/sdcard/FDA_doc/" + str_outname)),
                fileMimeType);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // 检测到系统尚未安装OliveOffice的apk程序
            Toast.makeText(context, "未找到任何软件", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void doSaveData() {
        try {
            info.setCUSTOM_NO(et_CUSTOM_NO.getText().toString());
            info.setNO(et_NO.getText().toString());
            info.setGOODS_NAME(et_GOODS_NAME.getText().toString());
            info.setBUSINESS_SOURCE(et_BUSINESS_SOURCE.getText().toString());
            info.setDRAW_NO(et_DRAW_NO.getText().toString());
            info.setSAMPLE_TYPE(sp_SAMPLE_TYPE.getSelectedItem().toString());
            ChangeValue(1, sp_SAMPLE_TYPE.getSelectedItem().toString());
            info.setSUPPLIER(et_SUPPLIER.getText().toString());
            info.setDOMESTIC_AREA(sp_DOMESTIC_AREA.getSelectedItem().toString());
            ChangeValue(2, sp_DOMESTIC_AREA.getSelectedItem().toString());
            info.setSUPPLIER_ADDR(et_SUPPLIER_ADDR.getText().toString());
            info.setSUPPLIER_LEGAL(et_SUPPLIER_LEGAL.getText().toString());
            info.setANNUAL_SALES(et_ANNUAL_SALES.getText().toString());
            info.setBUSINESS_LICENCE(et_BUSINESS_LICENCE.getText().toString());
            info.setSUPPLIER_PERSON(et_SUPPLIER_PERSON.getText().toString());
            info.setSUPPLIER_PERMIT_TYPE(sp_SUPPLIER_PERMIT_TYPE.getSelectedItem()
                    .toString());
            ChangeValue(3, sp_SUPPLIER_PERMIT_TYPE.getSelectedItem().toString());
            info.setSUPPLIER_PERMIT_CODE(et_SUPPLIER_PERMIT_CODE.getText().toString());
            info.setSUPPLIER_PHONE(et_SUPPLIER_PHONE.getText().toString());
            info.setSUPPLIER_FAX(et_SUPPLIER_FAX.getText().toString());
            info.setSUPPLIER_ZIPCODE(et_SUPPLIER_ZIPCODE.getText().toString());
            info.setDRAW_ADDR(sp_DRAW_ADDR.getSelectedItem().toString());
            ChangeValue(4, sp_DRAW_ADDR.getSelectedItem().toString());
            info.setSAMPLE_SOURCE(sp_SAMPLE_SOURCE.getSelectedItem().toString());
            ChangeValue(5, sp_SAMPLE_SOURCE.getSelectedItem().toString());
            info.setSAMPLE_PROPERTY(sp_SAMPLE_PROPERTY.getSelectedItem().toString());
            ChangeValue(6, sp_SAMPLE_PROPERTY.getSelectedItem().toString());
            info.setSAMPLE_STYLE(sp_SAMPLE_STYLE.getSelectedItem().toString());
            ChangeValue(7, sp_SAMPLE_STYLE.getSelectedItem().toString());
            info.setTRADEMARK(et_TRADEMARK.getText().toString());
            info.setDATE_PRODUCT_TYPE(sp_DATE_PRODUCT_TYPE.getSelectedItem().toString());
            ChangeValue(8, sp_DATE_PRODUCT_TYPE.getSelectedItem().toString());
            info.setDATE_PRODUCT(et_DATE_PRODUCT.getText().toString());
            info.setSAMPLE_MODEL(et_SAMPLE_MODEL.getText().toString());
            info.setSAMPLE_NUMBER(et_SAMPLE_NUMBER.getText().toString());
            info.setEXPIRATIONDATE(et_EXPIRATIONDATE.getText().toString());
            info.setTEST_FILE_NO(et_TEST_FILE_NO.getText().toString());
            info.setSAMPLE_CLASS(et_SAMPLE_CLASS.getText().toString());
            info.setPRODUCTION_CERTIFICATE(et_PRODUCTION_CERTIFICATE.getText().toString());
            info.setUNIVALENT(et_UNIVALENT.getText().toString());
            info.setI_AND_O(sp_I_AND_O.getSelectedItem().toString());
            ChangeValue(9, sp_I_AND_O.getSelectedItem().toString());
            info.setDRAW_NUM(et_DRAW_NUM.getText().toString());
            info.setDRAW_AMOUNT(et_DRAW_AMOUNT.getText().toString());
            info.setSTORAGESITE(et_STORAGESITE.getText().toString());
            info.setSAMPLE_STATUS(sp_SAMPLE_STATUS.getSelectedItem().toString());
            ChangeValue(10, sp_SAMPLE_STATUS.getSelectedItem().toString());
            info.setPACK_TYPE(sp_PACK_TYPE.getSelectedItem().toString());
            ChangeValue(11, sp_PACK_TYPE.getSelectedItem().toString());
            info.setMANU_COMPANY(et_MANU_COMPANY.getText().toString());
            info.setMANU_COMPANY_PHONE(et_MANU_COMPANY_PHONE.getText().toString());
            info.setMANU_COMPANY_ADDR(et_MANU_COMPANY_ADDR.getText().toString());
            info.setSAVE_MODE(sp_SAVE_MODE.getSelectedItem().toString());
            ChangeValue(12, sp_SAVE_MODE.getSelectedItem().toString());
            info.setSAMPLE_CLOSE_DATE(et_SAMPLE_CLOSE_DATE.getText().toString());
            info.setSAMPLE_ADDR(et_SAMPLE_ADDR.getText().toString());
            info.setPACK(sp_PACK.getSelectedItem().toString());
            ChangeValue(13, sp_PACK.getSelectedItem().toString());
            info.setDRAW_METHOD(sp_DRAW_METHOD.getSelectedItem().toString());
            ChangeValue(14, sp_DRAW_METHOD.getSelectedItem().toString());
            info.setDRAW_ORG(et_DRAW_ORG.getText().toString());
            info.setDRAW_ORG_ADDR(et_DRAW_ORG_ADDR.getText().toString());
            info.setDRAW_PERSON(et_DRAW_PERSON.getText().toString());
            info.setDRAW_PHONE(et_DRAW_PHONE.getText().toString());
            info.setDRAW_FAX(et_DRAW_FAX.getText().toString());
            info.setDRAW_ZIPCODE(et_DRAW_ZIPCODE.getText().toString());
            info.setREMARK(et_REMARK.getText().toString());
            info.setDRAW_DATE(et_DRAW_DATE.getText().toString());
            info.setGOODS_TYPE(et_GOODS_TYPE.getText().toString());
            info.setDRAW_MAN(et_DRAW_MAN.getText().toString());
            //更新列表
            InfoList.list_info.set(pos, info);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        FileRW.writerFile(InfoList.outpath, InfoList.list_info, context);
    }

    public void doPrintfWord() {
        str_outname = info.getCUSTOM_NO() + ".doc";
        try {
            FormatData();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("CUSTOM_NO", info.getCUSTOM_NO());
        map.put("DRAW_NO", info.getDRAW_NO());
        map.put("BUSINESS_SOURCE", info.getBUSINESS_SOURCE());
        map.put("SUPPLIER_NAME", info.getSUPPLIER());
        map.put("SUPPLIER_ADDR", info.getSUPPLIER_ADDR());
        map.put("SUPPLIER_LEGAL", info.getSUPPLIER_LEGAL());
        map.put("ANNUAL_SALES", info.getANNUAL_SALES());
        map.put("BUSINESS_LICENCE", info.getBUSINESS_LICENCE());
        map.put("SUPPLIER_PERSON", info.getSUPPLIER_PERSON());
        map.put("SUPPLIER_PERMIT_CODE", info.getSUPPLIER_PERMIT_CODE());
        map.put("SUPPLIER_PHONE", info.getSUPPLIER_PHONE());
        map.put("SUPPLIER_FAX", info.getSUPPLIER_FAX());
        map.put("SUPPLIER_ZIPCODE", info.getSUPPLIER_ZIPCODE());
        map.put("GOODS_NAME", info.getGOODS_NAME());
        map.put("TRADEMARK", info.getTRADEMARK());
        map.put("DATE_PRODUCT", info.getDATE_PRODUCT());
        map.put("SAMPLE_MODEL", info.getSAMPLE_MODEL());
        map.put("SAMPLE_NUMBER", info.getSAMPLE_NUMBER());
        map.put("EXPIRATIONDATE", info.getEXPIRATIONDATE());
        map.put("TEST_FILE_NO", info.getTEST_FILE_NO());
        map.put("SAMPLE_CLASS", info.getSAMPLE_CLASS());
        map.put("PRODUCTION_CERTIFICATE", info.getPRODUCTION_CERTIFICATE());
        map.put("UNIVALENT", info.getUNIVALENT());
        map.put("DRAW_NUM", info.getDRAW_NUM());
        map.put("DRAW_AMOUNT", info.getDRAW_AMOUNT());
        map.put("STORAGESITE", info.getSTORAGESITE());
        map.put("MANU_COMPANY_NAME", info.getMANU_COMPANY());
        map.put("MANU_COMPANY_ADDR", info.getMANU_COMPANY_ADDR());
        map.put("MANU_COMPANY_PHONE", info.getMANU_COMPANY_PHONE());
        map.put("SAMPLE_CLOSE_DATE", info.getSAMPLE_CLOSE_DATE());
        map.put("REMARK", info.getREMARK());
        map.put("DRAW_DATE", info.getDRAW_DATE());

        map.put("01-01", info.get任务类别_监督抽检());
        map.put("01-02", info.get任务类别_风险监测());
        map.put("02-01", info.get区域类型_城市());
        map.put("02-02", info.get区域类型_乡村());
        map.put("02-03", info.get区域类型_景点());
        map.put("03-01", info.get许可证_流通许可证());
        map.put("03-02", info.get许可证_餐饮服务许可证());
        map.put("04-01", info.get抽样地点_原辅料库());
        map.put("04-02", info.get抽样地点_生产线());
        map.put("04-03", info.get抽样地点_半成品库());
        map.put("04-04", info.get抽样地点_成品库_待检());
        map.put("04-05", info.get抽样地点_成品库_已检());
        map.put("04-06", info.get抽样地点_农贸市场());
        map.put("04-07", info.get抽样地点_菜市场());
        map.put("04-08", info.get抽样地点_批发市场());
        map.put("04-09", info.get抽样地点_商场());
        map.put("04-10", info.get抽样地点_超市());
        map.put("04-11", info.get抽样地点_小食杂店());
        map.put("04-12", info.get抽样地点_网购());
        map.put("04-13", info.get抽样地点_流通环节_其他());
        map.put("04-str1", info.get抽样地点_流通环节_其他_TXT());
        map.put("04-14", info.get抽样地点_餐馆_特大型());
        map.put("04-15", info.get抽样地点_餐馆_大型());
        map.put("04-16", info.get抽样地点_餐馆_中型());
        map.put("04-17", info.get抽样地点_餐馆_小型());
        map.put("04-18", info.get抽样地点_机关食堂());
        map.put("04-19", info.get抽样地点_学校食堂());
        map.put("04-20", info.get抽样地点_企事业单位食堂());
        map.put("04-21", info.get抽样地点_建筑工地食堂());
        map.put("04-22", info.get抽样地点_小吃店());
        map.put("04-23", info.get抽样地点_快餐店());
        //map.put("04-24", info.get抽样地点_饮品店);
        map.put("04-25", info.get抽样地点_配送单位());
        map.put("04-26", info.get抽样地点_中央厨房());
        map.put("04-27", info.get抽样地点_餐饮环节_其他());
        map.put("04-str2", info.get抽样地点_餐饮环节_其他_TXT());
        map.put("05-01", info.get样品来源_加工自制());
        map.put("05-02", info.get样品来源_委托生产());
        map.put("05-03", info.get样品来源_外购());
        map.put("05-04", info.get样品来源_其他());
        map.put("06-01", info.get样品属性_普通食品());
        map.put("06-02", info.get样品属性_特殊膳食食品());
        map.put("06-03", info.get样品属性_节令食品());
        map.put("06-04", info.get样品属性_重大活动保障食品());
        map.put("07-01", info.get样品类型_食品农产品());
        map.put("07-02", info.get样品类型_工业加工食品());
        map.put("07-03", info.get样品类型_餐饮加工食品());
        map.put("07-04", info.get样品类型_食品添加剂());
        map.put("07-05", info.get样品类型_食品相关产品());
        map.put("07-06", info.get样品类型_其他());
        map.put("07-str1", " ");
        //map.put("07-str1", info.getSAMPLE_STYLE());
        map.put("08-01", info.get样品信息_生产日期());
        map.put("08-02", info.get样品信息_加工日期());
        map.put("08-03", info.get样品信息_购进日期());
        map.put("09-01", info.get是否出口_是());
        map.put("09-02", info.get是否出口_否());
        map.put("10-01", info.get样品形态_固体());
        map.put("10-02", info.get样品形态_半固体());
        map.put("10-03", info.get样品形态_液体());
        map.put("10-04", info.get样品形态_气体());
        map.put("11-01", info.get包装分类_散装());
        map.put("11-02", info.get包装分类_预包装());
        map.put("12-01", info.get样品储存条件_常温());
        map.put("12-02", info.get样品储存条件_冷藏());
        map.put("12-03", info.get样品储存条件_冷冻());
        map.put("12-04", info.get样品储存条件_避光());
        map.put("12-05", info.get样品储存条件_密闭());
        map.put("12-06", info.get样品储存条件_其他());
        map.put("12-str1", info.get样品储存条件_其他_TXT());
        map.put("13-01", info.get抽样样品包装_玻璃瓶());
        map.put("13-02", info.get抽样样品包装_塑料瓶());
        map.put("13-03", info.get抽样样品包装_塑料袋());
        map.put("13-04", info.get抽样样品包装_无菌袋());
        map.put("13-05", info.get抽样样品包装_其他());
        map.put("13-str1", info.get抽样样品包装_其他_TXT());
        map.put("14-01", info.get抽样方式_无菌抽样());
        map.put("14-02", info.get抽样方式_非无菌抽样());
        try {
            dodoc("yuan.doc", str_outname, map);
            Toast.makeText(context, "生成抽样单" + str_outname + "成功", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // do something
                    doOpenWord();
                }
            }, 2000); // 延时3s执行
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initdata() {
        //赋值
        et_CUSTOM_NO.setText(info.getCUSTOM_NO());
        et_NO.setText(info.getNO());
        et_GOODS_NAME.setText(info.getGOODS_NAME());
        et_BUSINESS_SOURCE.setText(info.getBUSINESS_SOURCE());
        et_DRAW_NO.setText(info.getDRAW_NO());
        et_SUPPLIER.setText(info.getSUPPLIER());
        et_SUPPLIER_ADDR.setText(info.getSUPPLIER_ADDR());
        et_SUPPLIER_LEGAL.setText(info.getSUPPLIER_LEGAL());
        et_ANNUAL_SALES.setText(info.getANNUAL_SALES());
        et_BUSINESS_LICENCE.setText(info.getBUSINESS_LICENCE());
        et_SUPPLIER_PERSON.setText(info.getSUPPLIER_PERSON());
        et_SUPPLIER_PERMIT_CODE.setText(info.getSUPPLIER_PERMIT_CODE());
        et_SUPPLIER_PHONE.setText(info.getSUPPLIER_PHONE());
        et_SUPPLIER_FAX.setText(info.getSUPPLIER_FAX());
        et_SUPPLIER_ZIPCODE.setText(info.getSUPPLIER_ZIPCODE());
        et_TRADEMARK.setText(info.getTRADEMARK());
        et_DATE_PRODUCT.setText(info.getDATE_PRODUCT());
        et_SAMPLE_MODEL.setText(info.getSAMPLE_MODEL());
        et_SAMPLE_NUMBER.setText(info.getSAMPLE_NUMBER());
        et_EXPIRATIONDATE.setText(info.getEXPIRATIONDATE());
        et_TEST_FILE_NO.setText(info.getTEST_FILE_NO());
        et_SAMPLE_CLASS.setText(info.getSAMPLE_CLASS());
        et_PRODUCTION_CERTIFICATE.setText(info.getPRODUCTION_CERTIFICATE());
        et_UNIVALENT.setText(info.getUNIVALENT());
        et_DRAW_NUM.setText(info.getDRAW_NUM());
        et_DRAW_AMOUNT.setText(info.getDRAW_AMOUNT());
        et_STORAGESITE.setText(info.getSTORAGESITE());
        et_MANU_COMPANY.setText(info.getMANU_COMPANY());
        et_MANU_COMPANY_PHONE.setText(info.getMANU_COMPANY_PHONE());
        et_MANU_COMPANY_ADDR.setText(info.getMANU_COMPANY_ADDR());
        et_SAMPLE_CLOSE_DATE.setText(info.getSAMPLE_CLOSE_DATE());
        et_SAMPLE_ADDR.setText(info.getSAMPLE_ADDR());
        et_DRAW_ORG.setText(info.getDRAW_ORG());
        et_DRAW_ORG_ADDR.setText(info.getDRAW_ORG_ADDR());
        et_DRAW_PERSON.setText(info.getDRAW_PERSON());
        et_DRAW_PHONE.setText(info.getDRAW_PHONE());
        et_DRAW_FAX.setText(info.getDRAW_FAX());
        et_DRAW_ZIPCODE.setText(info.getDRAW_ZIPCODE());
        et_REMARK.setText(info.getREMARK());
        et_DRAW_DATE.setText(info.getDRAW_DATE());
        et_GOODS_TYPE.setText(info.getGOODS_TYPE());
        et_DRAW_MAN.setText(info.getDRAW_MAN());

        SpinnerAdapter adapter1 = sp_SAMPLE_TYPE.getAdapter();
        for (int i = 0; i < adapter1.getCount(); i++) {
            if (info.getSAMPLE_TYPE().equals(adapter1.getItem(i).toString())) {
                sp_SAMPLE_TYPE.setSelection(i, true);
                break;
            }
        }

        SpinnerAdapter adapter2 = sp_DOMESTIC_AREA.getAdapter();
        for (int i = 0; i < adapter2.getCount(); i++) {
            if (info.getDOMESTIC_AREA().equals(adapter2.getItem(i).toString())) {
                sp_DOMESTIC_AREA.setSelection(i, true);
                break;
            }
        }

        SpinnerAdapter adapter3 = sp_SUPPLIER_PERMIT_TYPE.getAdapter();
        for (int i = 0; i < adapter3.getCount(); i++) {
            if (info.getSUPPLIER_PERMIT_TYPE().equals(adapter3.getItem(i).toString())) {
                sp_SUPPLIER_PERMIT_TYPE.setSelection(i, true);
                break;
            }
        }

        SpinnerAdapter adapter4 = sp_DRAW_ADDR.getAdapter();
        for (int i = 0; i < adapter4.getCount(); i++) {
            if (info.getDRAW_ADDR().equals(adapter4.getItem(i).toString())) {
                sp_DRAW_ADDR.setSelection(i, true);
                break;
            }
        }

        SpinnerAdapter adapter5 = sp_SAMPLE_SOURCE.getAdapter();
        for (int i = 0; i < adapter5.getCount(); i++) {
            if (info.getSAMPLE_SOURCE().equals(adapter5.getItem(i).toString())) {
                sp_SAMPLE_SOURCE.setSelection(i, true);
                break;
            }
        }

        SpinnerAdapter adapter6 = sp_SAMPLE_PROPERTY.getAdapter();
        for (int i = 0; i < adapter6.getCount(); i++) {
            if (info.getSAMPLE_PROPERTY().equals(adapter6.getItem(i).toString())) {
                sp_SAMPLE_PROPERTY.setSelection(i, true);
                break;
            }
        }

        SpinnerAdapter adapter7 = sp_SAMPLE_STYLE.getAdapter();
        for (int i = 0; i < adapter7.getCount(); i++) {
            if (info.getSAMPLE_STYLE().equals(adapter7.getItem(i).toString())) {
                sp_SAMPLE_STYLE.setSelection(i, true);
                break;
            }
        }


        SpinnerAdapter adapter8 = sp_DATE_PRODUCT_TYPE.getAdapter();
        for (int i = 0; i < adapter8.getCount(); i++) {
            if (info.getDATE_PRODUCT_TYPE().equals(adapter8.getItem(i).toString())) {
                sp_DATE_PRODUCT_TYPE.setSelection(i, true);
                break;
            }
        }

        SpinnerAdapter adapter9 = sp_I_AND_O.getAdapter();
        for (int i = 0; i < adapter9.getCount(); i++) {
            if (info.getI_AND_O().equals(adapter9.getItem(i).toString())) {
                sp_I_AND_O.setSelection(i, true);
                break;
            }
        }

        SpinnerAdapter adapter10 = sp_SAMPLE_STATUS.getAdapter();
        for (int i = 0; i < adapter10.getCount(); i++) {
            if (info.getSAMPLE_STATUS().equals(adapter10.getItem(i).toString())) {
                sp_SAMPLE_STATUS.setSelection(i, true);
                break;
            }
        }

        SpinnerAdapter adapter11 = sp_PACK_TYPE.getAdapter();
        for (int i = 0; i < adapter11.getCount(); i++) {
            if (info.getPACK_TYPE().equals(adapter11.getItem(i).toString())) {
                sp_PACK_TYPE.setSelection(i, true);
                break;
            }
        }

        SpinnerAdapter adapter12 = sp_SAVE_MODE.getAdapter();
        for (int i = 0; i < adapter12.getCount(); i++) {
            if (info.getSAVE_MODE().equals(adapter12.getItem(i).toString())) {
                sp_SAVE_MODE.setSelection(i, true);
                break;
            }
        }

        SpinnerAdapter adapter13 = sp_PACK.getAdapter();
        for (int i = 0; i < adapter13.getCount(); i++) {
            if (info.getPACK().equals(adapter13.getItem(i).toString())) {
                sp_PACK.setSelection(i, true);
                break;
            }
        }

        SpinnerAdapter adapter14 = sp_DRAW_METHOD.getAdapter();
        for (int i = 0; i < adapter14.getCount(); i++) {
            if (info.getDRAW_METHOD().equals(adapter14.getItem(i).toString())) {
                sp_DRAW_METHOD.setSelection(i, true);
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
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
            case R.id.action_save:
                doSaveData();
                break;
            case R.id.action_uploadimg:
                Intent intent_uplodimg = new Intent();
                intent_uplodimg.setClass(DetailsActivity.this,
                        ImgUploadActivity.class);
                intent_uplodimg.putExtra("custom_no", et_CUSTOM_NO.getText().toString());
                //finish();// 结束当前活动
                startActivity(intent_uplodimg);
                break;
            case R.id.action_printf:
                doPrintfWord();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void closeStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeStream(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
