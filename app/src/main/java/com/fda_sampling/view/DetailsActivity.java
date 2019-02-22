package com.fda_sampling.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.fda_sampling.R;
import com.fda_sampling.model.SubmitStatus;
import com.fda_sampling.model.Task;
import com.fda_sampling.model.Tasks;
import com.fda_sampling.service.FDA_API;
import com.fda_sampling.service.HttpUtils;
import com.fda_sampling.util.ClickUtil;
import com.fda_sampling.util.DatePickerDialog;
import com.fda_sampling.util.MyApplication;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private Task task;
    private Context context;
    private Toolbar toolbar;
    private Spinner sp_SAMPLE_TYPE, sp_DOMESTIC_AREA, sp_PERMIT_TYPE, sp_DRAW_ADDR,
            sp_SAMPLE_SOURCE,
            sp_SAMPLE_PROPERTY, sp_SAMPLE_STYLE, sp_DATE_PRODUCT_TYPE, sp_I_AND_O, sp_SAMPLE_STATUS,
            sp_PACK_TYPE, sp_SAVE_MODE, sp_PACK, sp_DRAW_METHOD;
    private ArrayAdapter ada_SAMPLE_TYPE, ada_DOMESTIC_AREA, ada_PERMIT_TYPE,
            ada_DRAW_ADDR, ada_SAMPLE_SOURCE,
            ada_SAMPLE_PROPERTY, ada_SAMPLE_STYLE, ada_DATE_PRODUCT_TYPE, ada_I_AND_O,
            ada_SAMPLE_STATUS,
            ada_PACK_TYPE, ada_SAVE_MODE, ada_PACK, ada_DRAW_METHOD;
    private LinearLayout tr_return;
    private TextView tv_STATE, tv_CHECK_INFO, tv_NO, tv_DATE_PRODUCT, tv_DRAW_DATE;
    private EditText et_CUSTOM_NO, et_GOODS_NAME, et_BUSINESS_SOURCE,
            et_SUPPLIER,
            et_SUPPLIER_ADDR, et_SUPPLIER_LEGAL, et_ANNUAL_SALES, et_BUSINESS_LICENCE,
            et_SUPPLIER_PERSON,
            et_PERMIT_NUM, et_SUPPLIER_PHONE, et_SUPPLIER_FAX, et_SUPPLIER_ZIPCODE,
            et_TRADEMARK, et_SAMPLE_MODEL, et_SAMPLE_NUMBER, et_EXPIRATIONDATE, et_TEST_FILE_NO,
            et_SAMPLE_CLASS, et_PRODUCTION_CERTIFICATE, et_UNIVALENT, et_DRAW_NUM, et_DRAW_AMOUNT,
            et_STORAGESITE, et_MANU_COMPANY, et_MANU_COMPANY_PHONE, et_MANU_COMPANY_ADDR,
            et_SAMPLE_CLOSE_DATE,
            et_SAMPLE_ADDR, et_DRAW_ORG, et_DRAW_ORG_ADDR, et_DRAW_PERSON, et_DRAW_PHONE,
            et_DRAW_FAX,
            et_DRAW_ZIPCODE, et_REMARK, et_GOODS_TYPE, et_DRAW_MAN;
    private SharedPreferences sharedPreferences;
    private String token, str_DATE_PRODUCT, str_DRAW_DATE;
    private ProgressDialog mypDialog;
    private int mYear, mMonth, mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_details);
        context = this;
        sharedPreferences = getSharedPreferences("Info", MODE_PRIVATE);
        tr_return = findViewById(R.id.details_return);
        tv_STATE = findViewById(R.id.details_STATE);
        tv_CHECK_INFO = findViewById(R.id.details_CHECK_INFO);

        et_CUSTOM_NO = findViewById(R.id.details_CUSTOM_NO);
        tv_NO = findViewById(R.id.details_NO);
        et_GOODS_NAME = findViewById(R.id.details_GOODS_NAME);
        et_BUSINESS_SOURCE = findViewById(R.id.details_BUSINESS_SOURCE);
        sp_SAMPLE_TYPE = findViewById(R.id.details_SAMPLE_TYPE);
        et_SUPPLIER = findViewById(R.id.details_SUPPLIER);
        sp_DOMESTIC_AREA = findViewById(R.id.details_DOMESTIC_AREA);
        et_SUPPLIER_ADDR = findViewById(R.id.details_SUPPLIER_ADDR);
        et_SUPPLIER_LEGAL = findViewById(R.id.details_SUPPLIER_LEGAL);
        et_ANNUAL_SALES = findViewById(R.id.details_ANNUAL_SALES);
        et_BUSINESS_LICENCE = findViewById(R.id.details_BUSINESS_LICENCE);
        et_SUPPLIER_PERSON = findViewById(R.id.details_SUPPLIER_PERSON);
        sp_PERMIT_TYPE = findViewById(R.id.details_PERMIT_TYPE);
        et_PERMIT_NUM = findViewById(R.id.details_PERMIT_NUM);
        et_SUPPLIER_PHONE = findViewById(R.id.details_SUPPLIER_PHONE);
        et_SUPPLIER_FAX = findViewById(R.id.details_SUPPLIER_FAX);
        et_SUPPLIER_ZIPCODE = findViewById(R.id.details_SUPPLIER_ZIPCODE);
        sp_DRAW_ADDR = findViewById(R.id.details_DRAW_ADDR);
        sp_SAMPLE_SOURCE = findViewById(R.id.details_SAMPLE_SOURCE);
        sp_SAMPLE_PROPERTY = findViewById(R.id.details_SAMPLE_PROPERTY);
        sp_SAMPLE_STYLE = findViewById(R.id.details_SAMPLE_STYLE);
        et_TRADEMARK = findViewById(R.id.details_TRADEMARK);
        sp_DATE_PRODUCT_TYPE = findViewById(R.id.details_DATE_PRODUCT_TYPE);
        tv_DATE_PRODUCT = findViewById(R.id.details_DATE_PRODUCT);
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
        tv_DRAW_DATE = findViewById(R.id.details_DRAW_DATE);
        et_GOODS_TYPE = findViewById(R.id.details_GOODS_TYPE);
        et_DRAW_MAN = findViewById(R.id.details_DRAW_MAN);

        ada_SAMPLE_TYPE = ArrayAdapter.createFromResource(context, R.array.SAMPLE_TYPE, android.R
                .layout.simple_spinner_dropdown_item);
        sp_SAMPLE_TYPE.setAdapter(ada_SAMPLE_TYPE);
        ada_DOMESTIC_AREA = ArrayAdapter.createFromResource(context, R.array.DOMESTIC_AREA,
                android.R.layout.simple_spinner_dropdown_item);
        sp_DOMESTIC_AREA.setAdapter(ada_DOMESTIC_AREA);
        ada_PERMIT_TYPE = ArrayAdapter.createFromResource(context, R.array
                .PERMIT_TYPE, android.R.layout.simple_spinner_dropdown_item);
        sp_PERMIT_TYPE.setAdapter(ada_PERMIT_TYPE);
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

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        tv_DRAW_DATE.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 自动生成的方法存
                new DatePickerDialog(context, 0,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker DatePicker,
                                                  int Year, int MonthOfYear, int DayOfMonth) {
                                /*str_DRAW_DATE = Year + "-" + String.format("%02d",
                                        (MonthOfYear + 1)) + "-" + String.format("%02d",
                                        DayOfMonth);*/
                                str_DRAW_DATE = String.format(Locale.CHINA, "%02d-%02d-%02d",
                                        Year, (MonthOfYear + 1), DayOfMonth);
                                tv_DRAW_DATE.setText(str_DRAW_DATE);
                            }
                        }, mYear, mMonth, mDay, true).show();
            }
        });

        tv_DATE_PRODUCT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 自动生成的方法存
                new DatePickerDialog(context, 0,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker DatePicker,
                                                  int Year, int MonthOfYear, int DayOfMonth) {
                                /*str_DATE_PRODUCT = Year + "-" + String.format("%02d",
                                        (MonthOfYear + 1)) + "-" + String.format("%02d",
                                        DayOfMonth);*/
                                str_DATE_PRODUCT = String.format(Locale.CHINA, "%02d-%02d-%02d",
                                        Year, (MonthOfYear + 1), DayOfMonth);
                                tv_DATE_PRODUCT.setText(str_DATE_PRODUCT);
                            }
                        }, mYear, mMonth, mDay, true).show();
            }
        });
    }

    public void doSaveData() {
        task.setCUSTOM_NO(et_CUSTOM_NO.getText().toString());
        //task.setNO(tv_NO.getText().toString());
        task.setGOODS_NAME(et_GOODS_NAME.getText().toString());
        task.setBUSINESS_SOURCE(et_BUSINESS_SOURCE.getText().toString());
        task.setSAMPLE_TYPE(sp_SAMPLE_TYPE.getSelectedItem().toString());
        task.setSUPPLIER(et_SUPPLIER.getText().toString());
        task.setDOMESTIC_AREA(sp_DOMESTIC_AREA.getSelectedItem().toString());
        task.setSUPPLIER_ADDR(et_SUPPLIER_ADDR.getText().toString());
        task.setSUPPLIER_LEGAL(et_SUPPLIER_LEGAL.getText().toString());
        task.setANNUAL_SALES(et_ANNUAL_SALES.getText().toString());
        task.setBUSINESS_LICENCE(et_BUSINESS_LICENCE.getText().toString());
        task.setSUPPLIER_PERSON(et_SUPPLIER_PERSON.getText().toString());
        task.setPERMIT_TYPE(sp_PERMIT_TYPE.getSelectedItem()
                .toString());
        task.setPERMIT_NUM(et_PERMIT_NUM.getText().toString());
        task.setSUPPLIER_PHONE(et_SUPPLIER_PHONE.getText().toString());
        task.setSUPPLIER_FAX(et_SUPPLIER_FAX.getText().toString());
        task.setSUPPLIER_ZIPCODE(et_SUPPLIER_ZIPCODE.getText().toString());
        task.setDRAW_ADDR(sp_DRAW_ADDR.getSelectedItem().toString());
        task.setSAMPLE_SOURCE(sp_SAMPLE_SOURCE.getSelectedItem().toString());
        task.setSAMPLE_PROPERTY(sp_SAMPLE_PROPERTY.getSelectedItem().toString());
        task.setSAMPLE_STYLE(sp_SAMPLE_STYLE.getSelectedItem().toString());
        task.setTRADEMARK(et_TRADEMARK.getText().toString());
        task.setDATE_PRODUCT_TYPE(sp_DATE_PRODUCT_TYPE.getSelectedItem().toString());
        task.setDATE_PRODUCT(tv_DATE_PRODUCT.getText().toString());
        task.setSAMPLE_MODEL(et_SAMPLE_MODEL.getText().toString());
        task.setSAMPLE_NUMBER(et_SAMPLE_NUMBER.getText().toString());
        task.setEXPIRATIONDATE(et_EXPIRATIONDATE.getText().toString());
        task.setTEST_FILE_NO(et_TEST_FILE_NO.getText().toString());
        task.setSAMPLE_CLASS(et_SAMPLE_CLASS.getText().toString());
        task.setPRODUCTION_CERTIFICATE(et_PRODUCTION_CERTIFICATE.getText().toString());
        task.setUNIVALENT(et_UNIVALENT.getText().toString());
        task.setI_AND_O(sp_I_AND_O.getSelectedItem().toString());
        task.setDRAW_NUM(et_DRAW_NUM.getText().toString());
        task.setDRAW_AMOUNT(et_DRAW_AMOUNT.getText().toString());
        task.setSTORAGESITE(et_STORAGESITE.getText().toString());
        task.setSAMPLE_STATUS(sp_SAMPLE_STATUS.getSelectedItem().toString());
        task.setPACK_TYPE(sp_PACK_TYPE.getSelectedItem().toString());
        task.setMANU_COMPANY(et_MANU_COMPANY.getText().toString());
        task.setMANU_COMPANY_PHONE(et_MANU_COMPANY_PHONE.getText().toString());
        task.setMANU_COMPANY_ADDR(et_MANU_COMPANY_ADDR.getText().toString());
        task.setSAVE_MODE(sp_SAVE_MODE.getSelectedItem().toString());
        task.setSAMPLE_CLOSE_DATE(et_SAMPLE_CLOSE_DATE.getText().toString());
        task.setSAMPLE_ADDR(et_SAMPLE_ADDR.getText().toString());
        task.setPACK(sp_PACK.getSelectedItem().toString());
        task.setDRAW_METHOD(sp_DRAW_METHOD.getSelectedItem().toString());
        task.setDRAW_ORG(et_DRAW_ORG.getText().toString());
        task.setDRAW_ORG_ADDR(et_DRAW_ORG_ADDR.getText().toString());
        task.setDRAW_PERSON(et_DRAW_PERSON.getText().toString());
        task.setDRAW_PHONE(et_DRAW_PHONE.getText().toString());
        task.setDRAW_FAX(et_DRAW_FAX.getText().toString());
        task.setDRAW_ZIPCODE(et_DRAW_ZIPCODE.getText().toString());
        task.setREMARK(et_REMARK.getText().toString());
        task.setDRAW_DATE(tv_DRAW_DATE.getText().toString());
        task.setGOODS_TYPE(et_GOODS_TYPE.getText().toString());
        task.setDRAW_MAN(et_DRAW_MAN.getText().toString());
        //更新列表
        Tasks.list_task.set(Tasks.position, task);
    }

    public void attempSubmit() {
        mypDialog = new ProgressDialog(DetailsActivity.this);
        // 实例化
        mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置进度条风格，风格为圆形，旋转的
        mypDialog.setTitle("提交信息中...");
        // 设置ProgressDialog 标题
        mypDialog.setIndeterminate(false);
        // 设置ProgressDialog 的进度条是否不明确
        mypDialog.setCancelable(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        mypDialog.show();
        // 让ProgressDialog显示
        if (((MyApplication) getApplication()).getTOKEN() == null) {
            token = sharedPreferences.getString("TOKEN", null);
        } else {
            token = ((MyApplication) getApplication()).getTOKEN();
        }
        final Task task_submit = Tasks.list_task.get(Tasks.position);
        Gson gson = new Gson();
        String data = gson.toJson(task_submit);
        FDA_API request = HttpUtils.GsonApi();
        Call<SubmitStatus> call = request.Submit(token, data);
        call.enqueue(new Callback<SubmitStatus>() {
            @Override
            public void onResponse(Call<SubmitStatus> call, Response<SubmitStatus> response) {
                if (response.code() == 401) {
                    Log.v("Submit请求", "token过期");
                    Intent intent_login = new Intent();
                    intent_login.setClass(DetailsActivity.this,
                            LoginActivity.class);
                    intent_login.putExtra("login_type", 1);
                    mypDialog.dismiss();
                    startActivity(intent_login);
                } else if (response.code() == 200) {
                    if (response.body() != null) {
                        Log.v("Submit请求成功!", "response.body is not null");
                        SubmitStatus submitStatus = response.body();
                        if (submitStatus.getMessage().equals("执行完成！")) {
                            Snackbar.make(toolbar, "提交成功!",
                                    Snackbar.LENGTH_LONG).setAction("Action", null)
                                    .show();
                        } else {
                            Snackbar.make(toolbar, submitStatus.getMessage(),
                                    Snackbar.LENGTH_LONG).setAction("Action", null)
                                    .show();
                        }
                    } else {
                        Log.v("Submit请求成功!", "response.body is null");
                        Snackbar.make(toolbar, "提交失败!(response.body is null)请稍后再试",
                                Snackbar.LENGTH_LONG).setAction("Action", null)
                                .show();
                    }
                } else {
                    Log.v("Submit请求成功!", "提交失败!(" + response.code() + ")请稍后再试");
                    Snackbar.make(toolbar, "" + response.code(),
                            Snackbar.LENGTH_LONG).setAction("Action", null)
                            .show();
                }
                mypDialog.dismiss();
            }

            @Override
            public void onFailure(Call<SubmitStatus> call, Throwable t) {
                Log.v("Submit请求失败!", t.getMessage());
                Snackbar.make(toolbar, "提交失败!(Submit请求失败)请稍后再试",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
                mypDialog.dismiss();
            }
        });
    }

    public void doPrintfWord() {
        /*str_outname = task.getCUSTOM_NO() + ".doc";
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
        map.put("CUSTOM_NO", task.getCUSTOM_NO());
        map.put("DRAW_NO", task.getDRAW_NO());
        map.put("BUSINESS_SOURCE", task.getBUSINESS_SOURCE());
        map.put("SUPPLIER_NAME", task.getSUPPLIER());
        map.put("SUPPLIER_ADDR", task.getSUPPLIER_ADDR());
        map.put("SUPPLIER_LEGAL", task.getSUPPLIER_LEGAL());
        map.put("ANNUAL_SALES", task.getANNUAL_SALES());
        map.put("BUSINESS_LICENCE", task.getBUSINESS_LICENCE());
        map.put("SUPPLIER_PERSON", task.getSUPPLIER_PERSON());
        map.put("PERMIT_NUM", task.getPERMIT_NUM());
        map.put("SUPPLIER_PHONE", task.getSUPPLIER_PHONE());
        map.put("SUPPLIER_FAX", task.getSUPPLIER_FAX());
        map.put("SUPPLIER_ZIPCODE", task.getSUPPLIER_ZIPCODE());
        map.put("GOODS_NAME", task.getGOODS_NAME());
        map.put("TRADEMARK", task.getTRADEMARK());
        map.put("DATE_PRODUCT", task.getDATE_PRODUCT());
        map.put("SAMPLE_MODEL", task.getSAMPLE_MODEL());
        map.put("SAMPLE_NUMBER", task.getSAMPLE_NUMBER());
        map.put("EXPIRATIONDATE", task.getEXPIRATIONDATE());
        map.put("TEST_FILE_NO", task.getTEST_FILE_NO());
        map.put("SAMPLE_CLASS", task.getSAMPLE_CLASS());
        map.put("PRODUCTION_CERTIFICATE", task.getPRODUCTION_CERTIFICATE());
        map.put("UNIVALENT", task.getUNIVALENT());
        map.put("DRAW_NUM", task.getDRAW_NUM());
        map.put("DRAW_AMOUNT", task.getDRAW_AMOUNT());
        map.put("STORAGESITE", task.getSTORAGESITE());
        map.put("MANU_COMPANY_NAME", task.getMANU_COMPANY());
        map.put("MANU_COMPANY_ADDR", task.getMANU_COMPANY_ADDR());
        map.put("MANU_COMPANY_PHONE", task.getMANU_COMPANY_PHONE());
        map.put("SAMPLE_CLOSE_DATE", task.getSAMPLE_CLOSE_DATE());
        map.put("REMARK", task.getREMARK());
        map.put("DRAW_DATE", task.getDRAW_DATE());

        map.put("01-01", task.get任务类别_监督抽检());
        map.put("01-02", task.get任务类别_风险监测());
        map.put("02-01", task.get区域类型_城市());
        map.put("02-02", task.get区域类型_乡村());
        map.put("02-03", task.get区域类型_景点());
        map.put("03-01", task.get许可证_流通许可证());
        map.put("03-02", task.get许可证_餐饮服务许可证());
        map.put("04-01", task.get抽样地点_原辅料库());
        map.put("04-02", task.get抽样地点_生产线());
        map.put("04-03", task.get抽样地点_半成品库());
        map.put("04-04", task.get抽样地点_成品库_待检());
        map.put("04-05", task.get抽样地点_成品库_已检());
        map.put("04-06", task.get抽样地点_农贸市场());
        map.put("04-07", task.get抽样地点_菜市场());
        map.put("04-08", task.get抽样地点_批发市场());
        map.put("04-09", task.get抽样地点_商场());
        map.put("04-10", task.get抽样地点_超市());
        map.put("04-11", task.get抽样地点_小食杂店());
        map.put("04-12", task.get抽样地点_网购());
        map.put("04-13", task.get抽样地点_流通环节_其他());
        map.put("04-str1", task.get抽样地点_流通环节_其他_TXT());
        map.put("04-14", task.get抽样地点_餐馆_特大型());
        map.put("04-15", task.get抽样地点_餐馆_大型());
        map.put("04-16", task.get抽样地点_餐馆_中型());
        map.put("04-17", task.get抽样地点_餐馆_小型());
        map.put("04-18", task.get抽样地点_机关食堂());
        map.put("04-19", task.get抽样地点_学校食堂());
        map.put("04-20", task.get抽样地点_企事业单位食堂());
        map.put("04-21", task.get抽样地点_建筑工地食堂());
        map.put("04-22", task.get抽样地点_小吃店());
        map.put("04-23", task.get抽样地点_快餐店());
        //map.put("04-24", task.get抽样地点_饮品店);
        map.put("04-25", task.get抽样地点_配送单位());
        map.put("04-26", task.get抽样地点_中央厨房());
        map.put("04-27", task.get抽样地点_餐饮环节_其他());
        map.put("04-str2", task.get抽样地点_餐饮环节_其他_TXT());
        map.put("05-01", task.get样品来源_加工自制());
        map.put("05-02", task.get样品来源_委托生产());
        map.put("05-03", task.get样品来源_外购());
        map.put("05-04", task.get样品来源_其他());
        map.put("06-01", task.get样品属性_普通食品());
        map.put("06-02", task.get样品属性_特殊膳食食品());
        map.put("06-03", task.get样品属性_节令食品());
        map.put("06-04", task.get样品属性_重大活动保障食品());
        map.put("07-01", task.get样品类型_食品农产品());
        map.put("07-02", task.get样品类型_工业加工食品());
        map.put("07-03", task.get样品类型_餐饮加工食品());
        map.put("07-04", task.get样品类型_食品添加剂());
        map.put("07-05", task.get样品类型_食品相关产品());
        map.put("07-06", task.get样品类型_其他());
        map.put("07-str1", " ");
        //map.put("07-str1", task.getSAMPLE_STYLE());
        map.put("08-01", task.get样品信息_生产日期());
        map.put("08-02", task.get样品信息_加工日期());
        map.put("08-03", task.get样品信息_购进日期());
        map.put("09-01", task.get是否出口_是());
        map.put("09-02", task.get是否出口_否());
        map.put("10-01", task.get样品形态_固体());
        map.put("10-02", task.get样品形态_半固体());
        map.put("10-03", task.get样品形态_液体());
        map.put("10-04", task.get样品形态_气体());
        map.put("11-01", task.get包装分类_散装());
        map.put("11-02", task.get包装分类_预包装());
        map.put("12-01", task.get样品储存条件_常温());
        map.put("12-02", task.get样品储存条件_冷藏());
        map.put("12-03", task.get样品储存条件_冷冻());
        map.put("12-04", task.get样品储存条件_避光());
        map.put("12-05", task.get样品储存条件_密闭());
        map.put("12-06", task.get样品储存条件_其他());
        map.put("12-str1", task.get样品储存条件_其他_TXT());
        map.put("13-01", task.get抽样样品包装_玻璃瓶());
        map.put("13-02", task.get抽样样品包装_塑料瓶());
        map.put("13-03", task.get抽样样品包装_塑料袋());
        map.put("13-04", task.get抽样样品包装_无菌袋());
        map.put("13-05", task.get抽样样品包装_其他());
        map.put("13-str1", task.get抽样样品包装_其他_TXT());
        map.put("14-01", task.get抽样方式_无菌抽样());
        map.put("14-02", task.get抽样方式_非无菌抽样());
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
        }*/
    }

    public void initdata() {
        if (Tasks.position != -1) {
            task = Tasks.list_task.get(Tasks.position);
        }
        try {
            FormatData(1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.v("FormatData()", "NoSuchMethodException");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.v("FormatData()", "InvocationTargetException");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.v("FormatData()", "IllegalAccessException");
        }
        //赋值
        if (task != null) {
            if (task.getSTATE() == null || task.getSTATE().equals("")) {
                tr_return.setVisibility(View.GONE);
            } else {
                tv_STATE.setText(task.getSTATE());
                tv_CHECK_INFO.setText(task.getCHECK_INFO());
            }
            et_CUSTOM_NO.setText(task.getCUSTOM_NO());
            tv_NO.setText(task.getNO());
            et_GOODS_NAME.setText(task.getGOODS_NAME());
            et_BUSINESS_SOURCE.setText(task.getBUSINESS_SOURCE());
            et_SUPPLIER.setText(task.getSUPPLIER());
            et_SUPPLIER_ADDR.setText(task.getSUPPLIER_ADDR());
            et_SUPPLIER_LEGAL.setText(task.getSUPPLIER_LEGAL());
            et_ANNUAL_SALES.setText(task.getANNUAL_SALES());
            et_BUSINESS_LICENCE.setText(task.getBUSINESS_LICENCE());
            et_SUPPLIER_PERSON.setText(task.getSUPPLIER_PERSON());
            et_PERMIT_NUM.setText(task.getPERMIT_NUM());
            et_SUPPLIER_PHONE.setText(task.getSUPPLIER_PHONE());
            et_SUPPLIER_FAX.setText(task.getSUPPLIER_FAX());
            et_SUPPLIER_ZIPCODE.setText(task.getSUPPLIER_ZIPCODE());
            et_TRADEMARK.setText(task.getTRADEMARK());
            //tv_DATE_PRODUCT.setText(task.getDATE_PRODUCT());
            et_SAMPLE_MODEL.setText(task.getSAMPLE_MODEL());
            et_SAMPLE_NUMBER.setText(task.getSAMPLE_NUMBER());
            et_EXPIRATIONDATE.setText(task.getEXPIRATIONDATE());
            et_TEST_FILE_NO.setText(task.getTEST_FILE_NO());
            et_SAMPLE_CLASS.setText(task.getSAMPLE_CLASS());
            et_PRODUCTION_CERTIFICATE.setText(task.getPRODUCTION_CERTIFICATE());
            et_UNIVALENT.setText(task.getUNIVALENT());
            et_DRAW_NUM.setText(task.getDRAW_NUM());
            et_DRAW_AMOUNT.setText(task.getDRAW_AMOUNT());
            et_STORAGESITE.setText(task.getSTORAGESITE());
            et_MANU_COMPANY.setText(task.getMANU_COMPANY());
            et_MANU_COMPANY_PHONE.setText(task.getMANU_COMPANY_PHONE());
            et_MANU_COMPANY_ADDR.setText(task.getMANU_COMPANY_ADDR());
            et_SAMPLE_CLOSE_DATE.setText(task.getSAMPLE_CLOSE_DATE());
            et_SAMPLE_ADDR.setText(task.getSAMPLE_ADDR());
            et_DRAW_ORG.setText(task.getDRAW_ORG());
            et_DRAW_ORG_ADDR.setText(task.getDRAW_ORG_ADDR());
            et_DRAW_PERSON.setText(task.getDRAW_PERSON());
            et_DRAW_PHONE.setText(task.getDRAW_PHONE());
            et_DRAW_FAX.setText(task.getDRAW_FAX());
            et_DRAW_ZIPCODE.setText(task.getDRAW_ZIPCODE());
            et_REMARK.setText(task.getREMARK());
            //tv_DRAW_DATE.setText(task.getDRAW_DATE());
            et_GOODS_TYPE.setText(task.getGOODS_TYPE());
            et_DRAW_MAN.setText(task.getDRAW_MAN());

            str_DATE_PRODUCT = str_DRAW_DATE = new SimpleDateFormat("yyyy-MM-dd", Locale
                    .getDefault()).format(Calendar.getInstance().getTime());
            if (task.getDATE_PRODUCT().equals("")) {
                tv_DATE_PRODUCT.setText(str_DATE_PRODUCT);
            } else {
                tv_DATE_PRODUCT.setText(task.getDATE_PRODUCT());
            }
            if (task.getDRAW_DATE().equals("")) {
                tv_DRAW_DATE.setText(str_DRAW_DATE);
            } else {
                tv_DRAW_DATE.setText(task.getDRAW_DATE());
            }

            SpinnerAdapter adapter1 = sp_SAMPLE_TYPE.getAdapter();
            for (int i = 0; i < adapter1.getCount(); i++) {
                if (task.getSAMPLE_TYPE().equals(adapter1.getItem(i).toString())) {
                    sp_SAMPLE_TYPE.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter2 = sp_DOMESTIC_AREA.getAdapter();
            for (int i = 0; i < adapter2.getCount(); i++) {
                if (task.getDOMESTIC_AREA().equals(adapter2.getItem(i).toString())) {
                    sp_DOMESTIC_AREA.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter3 = sp_PERMIT_TYPE.getAdapter();
            for (int i = 0; i < adapter3.getCount(); i++) {
                if (task.getPERMIT_TYPE().equals(adapter3.getItem(i).toString())) {
                    sp_PERMIT_TYPE.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter4 = sp_DRAW_ADDR.getAdapter();
            for (int i = 0; i < adapter4.getCount(); i++) {
                if (task.getDRAW_ADDR().equals(adapter4.getItem(i).toString())) {
                    sp_DRAW_ADDR.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter5 = sp_SAMPLE_SOURCE.getAdapter();
            for (int i = 0; i < adapter5.getCount(); i++) {
                if (task.getSAMPLE_SOURCE().equals(adapter5.getItem(i).toString())) {
                    sp_SAMPLE_SOURCE.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter6 = sp_SAMPLE_PROPERTY.getAdapter();
            for (int i = 0; i < adapter6.getCount(); i++) {
                if (task.getSAMPLE_PROPERTY().equals(adapter6.getItem(i).toString())) {
                    sp_SAMPLE_PROPERTY.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter7 = sp_SAMPLE_STYLE.getAdapter();
            for (int i = 0; i < adapter7.getCount(); i++) {
                if (task.getSAMPLE_STYLE().equals(adapter7.getItem(i).toString())) {
                    sp_SAMPLE_STYLE.setSelection(i, true);
                    break;
                }
            }


            SpinnerAdapter adapter8 = sp_DATE_PRODUCT_TYPE.getAdapter();
            for (int i = 0; i < adapter8.getCount(); i++) {
                if (task.getDATE_PRODUCT_TYPE().equals(adapter8.getItem(i).toString())) {
                    sp_DATE_PRODUCT_TYPE.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter9 = sp_I_AND_O.getAdapter();
            for (int i = 0; i < adapter9.getCount(); i++) {
                if (task.getI_AND_O().equals(adapter9.getItem(i).toString())) {
                    sp_I_AND_O.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter10 = sp_SAMPLE_STATUS.getAdapter();
            for (int i = 0; i < adapter10.getCount(); i++) {
                if (task.getSAMPLE_STATUS().equals(adapter10.getItem(i).toString())) {
                    sp_SAMPLE_STATUS.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter11 = sp_PACK_TYPE.getAdapter();
            for (int i = 0; i < adapter11.getCount(); i++) {
                if (task.getPACK_TYPE().equals(adapter11.getItem(i).toString())) {
                    sp_PACK_TYPE.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter12 = sp_SAVE_MODE.getAdapter();
            for (int i = 0; i < adapter12.getCount(); i++) {
                if (task.getSAVE_MODE().equals(adapter12.getItem(i).toString())) {
                    sp_SAVE_MODE.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter13 = sp_PACK.getAdapter();
            for (int i = 0; i < adapter13.getCount(); i++) {
                if (task.getPACK().equals(adapter13.getItem(i).toString())) {
                    sp_PACK.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter14 = sp_DRAW_METHOD.getAdapter();
            for (int i = 0; i < adapter14.getCount(); i++) {
                if (task.getDRAW_METHOD().equals(adapter14.getItem(i).toString())) {
                    sp_DRAW_METHOD.setSelection(i, true);
                    break;
                }
            }
        }
    }

    public void FormatData(int type) throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        Field[] fields = task.getClass().getDeclaredFields();
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
            /*name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1)
                    .toUpperCase());*/
            if (type == 1) {
                if (!name.equals("Samples") || !name.equals("$change")) {
                    Method getMethod = task.getClass().getMethod("get" + name);
                    //Log.v("getMethod", getMethod.getName().toString());
                    value = getMethod.invoke(task);
                    if (value == null) {
                        Method setMethod = task.getClass().getMethod("set" + name, String.class);
                        setMethod.invoke(task, "");
                    }
                }
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
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_submit:
                if (ClickUtil.isFastClick()) {
                    doSaveData();
                    attempSubmit();
                } else {
                    Snackbar.make(toolbar, "提交太快了，请稍后再试",
                            Snackbar.LENGTH_LONG).setAction("Action", null)
                            .show();
                }
                break;
            case R.id.action_uploadimg:
                Intent intent_uplodimg = new Intent();
                intent_uplodimg.setClass(DetailsActivity.this,
                        ImgUploadActivity.class);
                intent_uplodimg.putExtra("NO", tv_NO.getText().toString());
                //finish();// 结束当前活动
                startActivity(intent_uplodimg);
                break;
            //case R.id.action_printf:
            //doPrintfWord();
            // break;
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

    @Override
    public void onPause() {
        doSaveData();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        doSaveData();
        super.onDestroy();
    }
}
