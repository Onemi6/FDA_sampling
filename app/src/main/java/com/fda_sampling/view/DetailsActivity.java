package com.fda_sampling.view;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.fda_sampling.R;
import com.fda_sampling.model.City;
import com.fda_sampling.model.District;
import com.fda_sampling.model.FoodKind;
import com.fda_sampling.model.MeasureUnit;
import com.fda_sampling.model.Province;
import com.fda_sampling.model.SubmitStatus;
import com.fda_sampling.model.Task;
import com.fda_sampling.model.Tasks;
import com.fda_sampling.model.sampleEnterprise;
import com.fda_sampling.service.FDA_API;
import com.fda_sampling.service.HttpUtils;
import com.fda_sampling.util.ClickUtil;
import com.fda_sampling.util.DatePickerDialog;
import com.fda_sampling.util.MyApplication;
import com.fda_sampling.util.NetworkUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private Task task;
    private Context context;
    private Toolbar toolbar;
    private Spinner sp_SAMPLE_TYPE, sp_DOMESTIC_AREA, sp_PROVINCE, sp_CITY, sp_DISTRICT,
            sp_PERMIT_TYPE, sp_DRAW_ADDR, sp_SAMPLE_SOURCE, sp_SAMPLE_PROPERTY, sp_SAMPLE_STYLE,
            sp_DATE_PRODUCT_TYPE, sp_UNIVALENT_UNIT, sp_I_AND_O, sp_DRAW_NUM_UNIT,
            sp_DRAW_AMOUNT_UNIT, sp_STORAGESITE_UNIT, sp_SAMPLE_STATUS, sp_PACK_TYPE,
            sp_SAVE_MODE, sp_PACK, sp_DRAW_METHOD, sp_FOOD_KIND1, sp_FOOD_KIND2, sp_FOOD_KIND3,
            sp_FOOD_KIND4, sp_CHILD_FOOD_KIND_ID, sp_MANU_PROVINCE, sp_MANU_CITY, sp_MANU_DISTRICT;
    private ArrayAdapter<String> ada_UNIVALENT_UNIT, ada_AMOUNT_UNITS, ada_FOOD_KIND1,
            ada_FOOD_KIND2, ada_FOOD_KIND3, ada_FOOD_KIND4, ada_CHILD_FOOD_KIND_ID, ada_PROVINCE,
            ada_CITY, ada_DISTRICT, ada_MANU_PROVINCE, ada_MANU_CITY, ada_MANU_DISTRICT;
    private LinearLayout layout_return;
    private TextView tv_SUPPLIER_ADDR, tv_MANU_COMPANY_ADDR, tv_STATE, tv_CHECK_INFO, tv_NO,
            tv_DATE_PRODUCT, tv_DRAW_DATE, tv_CUSTOM_NO, tv_BUSINESS_SOURCE, tv_SAMPLE_ADDR,
            tv_DRAW_ORG_ADDR;
    private AutoCompleteTextView actv_SUPPLIER;
    private List<String> SUPPLIERS = new ArrayList<>();
    private List<sampleEnterprise> sampleEnterprises = new ArrayList<>();
    private sampleEnterprise oneSampleEnterprise;
    private List<Province> getProvinces = new ArrayList<>();
    private List<City> getCites = new ArrayList<>(), getCites_MANU = new ArrayList<>();
    private List<District> getDistricts = new ArrayList<>(), getDistricts_MANU = new ArrayList<>();
    private List<MeasureUnit> getMeasureUnit_AMOUNT_UNIT = new ArrayList<>(),
            getMeasureUnit_UNIVALENT_UNIT = new ArrayList<>();
    private List<FoodKind> getSamplingContract = new ArrayList<>(), getFoodKind1 = new
            ArrayList<>(), getFoodKind2 = new ArrayList<>(),
            getFoodKind3 = new ArrayList<>(), getFoodKind4 = new ArrayList<>();
    private String[] SamplingContracts = new String[]{"合同加载中"}, FoodKinds1 = new String[]{"分类加载中"},
            FoodKinds2 = new String[]{"分类加载中"}, FoodKinds3 = new String[]{"分类加载中"}, FoodKinds4 = new
            String[]{"分类加载中"}, AMOUNT_UNITS = new String[]{"单位加载中"}, UNIVALENT_UNITS = new
            String[]{"单位加载中"}, Provinces = new String[]{"省加载中"}, Cites = new String[]{"区加载中"},
            Cites_MANU = new String[]{"区加载中"}, Districts = new String[]{"县加载中"}, Districts_MANU =
            new String[]{"县加载中"};
    private EditText et_GOODS_NAME, et_SAMPLING_NOTICE_CODE, et_TOWN, et_SUPPLIER_ADDR_TXT,
            et_SUPPLIER_LEGAL, et_ANNUAL_SALES, et_BUSINESS_LICENCE, et_SUPPLIER_PERSON,
            et_PERMIT_NUM, et_SUPPLIER_PHONE, et_SUPPLIER_FAX, et_SUPPLIER_ZIPCODE,
            et_DRAW_ADDR_OTHER, et_TRADEMARK, et_SAMPLE_MODEL, et_SAMPLE_NUMBER,
            et_EXPIRATIONDATE, et_TEST_FILE_NO, et_SAMPLE_CLASS, et_PRODUCTION_CERTIFICATE,
            et_UNIVALENT, et_DRAW_NUM, et_DRAW_AMOUNT, et_STORAGESITE, et_MANU_COMPANY,
            et_MANU_TOWN, et_MANU_COMPANY_ADDR_TXT, et_SAVE_MODE_OTHER, et_MANU_COMPANY_PHONE,
            et_SAMPLE_CLOSE_DATE, et_PACK_OTHER, et_DRAW_ORG,
            et_DRAW_PERSON, et_DRAW_PHONE, et_DRAW_FAX, et_DRAW_ZIPCODE, et_REMARK,
            et_GOODS_TYPE, et_DRAW_MAN;
    private String token, DRAW_MAN_NO, str_DATE_PRODUCT, str_DRAW_DATE;
    private ProgressDialog mypDialog;
    private int mYear, mMonth, mDay, num_select = 0, Contract_ID, num_Cites, num_Cites_MANU,
            num_Districts, num_Districts_MANU, num_FoodKinds1, num_FoodKinds2, num_FoodKinds3,
            num_FoodKinds4, num_AMOUNT_UNITS, num_UNIVALENT_UNITS;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_details);
        context = this;
        sharedPreferences = getSharedPreferences("Info", MODE_PRIVATE);
        /*if (((MyApplication) getApplication()).getTOKEN() == null) {
            token = sharedPreferences.getString("TOKEN", null);
        } else {
            token = ((MyApplication) getApplication()).getTOKEN();
        }*/
        if (((MyApplication) getApplication()).getNO() == null) {
            DRAW_MAN_NO = sharedPreferences.getString("NO", null);
        } else {
            DRAW_MAN_NO = ((MyApplication) getApplication()).getNO();
        }
        num_Cites = num_Cites_MANU = num_Districts = num_Districts_MANU = num_FoodKinds1 =
                num_FoodKinds2 = num_FoodKinds3 = num_FoodKinds4 = num_AMOUNT_UNITS =
                        num_UNIVALENT_UNITS = 0;
        initView();
        initData();
        viewAction();
    }

    public void initView() {
        //退回layout
        layout_return = findViewById(R.id.details_return);
        tv_STATE = findViewById(R.id.details_STATE);
        tv_CHECK_INFO = findViewById(R.id.details_CHECK_INFO);

        tv_CUSTOM_NO = findViewById(R.id.details_CUSTOM_NO);
        tv_NO = findViewById(R.id.details_NO);
        et_GOODS_NAME = findViewById(R.id.details_GOODS_NAME);
        et_SAMPLING_NOTICE_CODE = findViewById(R.id.details_SAMPLING_NOTICE_CODE);
        tv_BUSINESS_SOURCE = findViewById(R.id.details_BUSINESS_SOURCE);
        sp_SAMPLE_TYPE = findViewById(R.id.details_SAMPLE_TYPE);
        actv_SUPPLIER = findViewById(R.id.details_SUPPLIER);
        sp_DOMESTIC_AREA = findViewById(R.id.details_DOMESTIC_AREA);
        tv_SUPPLIER_ADDR = findViewById(R.id.details_SUPPLIER_ADDR);

        sp_PROVINCE = findViewById(R.id.details_PROVINCE);
        sp_CITY = findViewById(R.id.details_CITY);
        sp_DISTRICT = findViewById(R.id.details_DISTRICT);

        et_TOWN = findViewById(R.id.details_TOWN);
        et_SUPPLIER_ADDR_TXT = findViewById(R.id.details_SUPPLIER_ADDR_TXT);
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
        et_DRAW_ADDR_OTHER = findViewById(R.id.details_DRAW_ADDR_OTHER);
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
        sp_UNIVALENT_UNIT = findViewById(R.id.details_UNIVALENT_UNIT);
        sp_I_AND_O = findViewById(R.id.details_I_AND_O);
        sp_DRAW_NUM_UNIT = findViewById(R.id.details_DRAW_NUM_UNIT);
        sp_DRAW_AMOUNT_UNIT = findViewById(R.id.details_DRAW_AMOUNT_UNIT);
        sp_STORAGESITE_UNIT = findViewById(R.id.details_STORAGESITE_UNIT);
        et_DRAW_NUM = findViewById(R.id.details_DRAW_NUM);
        et_DRAW_AMOUNT = findViewById(R.id.details_DRAW_AMOUNT);
        et_STORAGESITE = findViewById(R.id.details_STORAGESITE);
        sp_SAMPLE_STATUS = findViewById(R.id.details_SAMPLE_STATUS);
        sp_PACK_TYPE = findViewById(R.id.details_PACK_TYPE);
        et_MANU_COMPANY = findViewById(R.id.details_MANU_COMPANY);
        et_MANU_TOWN = findViewById(R.id.details_MANU_TOWN);
        et_MANU_COMPANY_ADDR_TXT = findViewById(R.id.details_MANU_COMPANY_ADDR_TXT);

        et_MANU_COMPANY_PHONE = findViewById(R.id.details_MANU_COMPANY_PHONE);
        tv_MANU_COMPANY_ADDR = findViewById(R.id.details_MANU_COMPANY_ADDR);
        sp_MANU_PROVINCE = findViewById(R.id.details_MANU_PROVINCE);
        sp_MANU_CITY = findViewById(R.id.details_MANU_CITY);
        sp_MANU_DISTRICT = findViewById(R.id.details_MANU_DISTRICT);
        sp_SAVE_MODE = findViewById(R.id.details_SAVE_MODE);
        et_SAVE_MODE_OTHER = findViewById(R.id.details_SAVE_MODE_OTHER);
        et_SAMPLE_CLOSE_DATE = findViewById(R.id.details_SAMPLE_CLOSE_DATE);
        tv_SAMPLE_ADDR = findViewById(R.id.details_SAMPLE_ADDR);
        sp_PACK = findViewById(R.id.details_PACK);
        et_PACK_OTHER = findViewById(R.id.details_PACK_OTHER);
        sp_DRAW_METHOD = findViewById(R.id.details_DRAW_METHOD);
        sp_FOOD_KIND1 = findViewById(R.id.details_FOOD_KIND1);
        sp_FOOD_KIND2 = findViewById(R.id.details_FOOD_KIND2);
        sp_FOOD_KIND3 = findViewById(R.id.details_FOOD_KIND3);
        sp_FOOD_KIND4 = findViewById(R.id.details_FOOD_KIND4);
        sp_CHILD_FOOD_KIND_ID = findViewById(R.id.details_CHILD_FOOD_KIND_ID);
        et_DRAW_ORG = findViewById(R.id.details_DRAW_ORG);
        tv_DRAW_ORG_ADDR = findViewById(R.id.details_DRAW_ORG_ADDR);
        et_DRAW_PERSON = findViewById(R.id.details_DRAW_PERSON);
        et_DRAW_PHONE = findViewById(R.id.details_DRAW_PHONE);
        et_DRAW_FAX = findViewById(R.id.details_DRAW_FAX);
        et_DRAW_ZIPCODE = findViewById(R.id.details_DRAW_ZIPCODE);
        et_REMARK = findViewById(R.id.details_REMARK);
        tv_DRAW_DATE = findViewById(R.id.details_DRAW_DATE);
        et_GOODS_TYPE = findViewById(R.id.details_GOODS_TYPE);
        et_DRAW_MAN = findViewById(R.id.details_DRAW_MAN);

        ArrayAdapter ada_SAMPLE_TYPE = ArrayAdapter.createFromResource(context, R.array
                .SAMPLE_TYPE, android.R.layout.simple_spinner_dropdown_item);
        sp_SAMPLE_TYPE.setAdapter(ada_SAMPLE_TYPE);
        ArrayAdapter ada_DOMESTIC_AREA = ArrayAdapter.createFromResource(context, R.array
                .DOMESTIC_AREA, android.R.layout.simple_spinner_dropdown_item);
        sp_DOMESTIC_AREA.setAdapter(ada_DOMESTIC_AREA);
        ArrayAdapter ada_PERMIT_TYPE = ArrayAdapter.createFromResource(context, R.array
                .PERMIT_TYPE, android.R.layout.simple_spinner_dropdown_item);
        sp_PERMIT_TYPE.setAdapter(ada_PERMIT_TYPE);
        ArrayAdapter ada_DRAW_ADDR = ArrayAdapter.createFromResource(context, R.array.DRAW_ADDR,
                android.R.layout.simple_spinner_dropdown_item);
        sp_DRAW_ADDR.setAdapter(ada_DRAW_ADDR);
        ArrayAdapter ada_SAMPLE_SOURCE = ArrayAdapter.createFromResource(context, R.array
                .SAMPLE_SOURCE, android.R.layout.simple_spinner_dropdown_item);
        sp_SAMPLE_SOURCE.setAdapter(ada_SAMPLE_SOURCE);
        ArrayAdapter ada_SAMPLE_PROPERTY = ArrayAdapter.createFromResource(context, R.array
                .SAMPLE_PROPERTY, android.R.layout.simple_spinner_dropdown_item);
        sp_SAMPLE_PROPERTY.setAdapter(ada_SAMPLE_PROPERTY);
        ArrayAdapter ada_SAMPLE_STYLE = ArrayAdapter.createFromResource(context, R.array
                .SAMPLE_STYLE, android.R.layout.simple_spinner_dropdown_item);
        sp_SAMPLE_STYLE.setAdapter(ada_SAMPLE_STYLE);
        ArrayAdapter ada_DATE_PRODUCT_TYPE = ArrayAdapter.createFromResource(context, R.array
                .DATE_PRODUCT_TYPE, android.R.layout.simple_spinner_dropdown_item);
        sp_DATE_PRODUCT_TYPE.setAdapter(ada_DATE_PRODUCT_TYPE);
        ArrayAdapter ada_I_AND_O = ArrayAdapter.createFromResource(context, R.array.I_AND_O,
                android.R.layout.simple_spinner_dropdown_item);
        sp_I_AND_O.setAdapter(ada_I_AND_O);
        ArrayAdapter ada_SAMPLE_STATUS = ArrayAdapter.createFromResource(context, R.array
                .SAMPLE_STATUS, android.R.layout.simple_spinner_dropdown_item);
        sp_SAMPLE_STATUS.setAdapter(ada_SAMPLE_STATUS);
        ArrayAdapter ada_PACK_TYPE = ArrayAdapter.createFromResource(context, R.array.PACK_TYPE,
                android.R.layout.simple_spinner_dropdown_item);
        sp_PACK_TYPE.setAdapter(ada_PACK_TYPE);
        ArrayAdapter ada_SAVE_MODE = ArrayAdapter.createFromResource(context, R.array.SAVE_MODE,
                android.R.layout.simple_spinner_dropdown_item);
        sp_SAVE_MODE.setAdapter(ada_SAVE_MODE);
        ArrayAdapter ada_PACK = ArrayAdapter.createFromResource(context, R.array.PACK,
                android.R.layout.simple_spinner_dropdown_item);
        sp_PACK.setAdapter(ada_PACK);
        ArrayAdapter ada_DRAW_METHOD = ArrayAdapter.createFromResource(context, R.array
                .DRAW_METHOD, android.R.layout.simple_spinner_dropdown_item);
        sp_DRAW_METHOD.setAdapter(ada_DRAW_METHOD);

        ada_PROVINCE = new ArrayAdapter<>(context, android.R.layout
                .simple_spinner_dropdown_item, Provinces);
        sp_PROVINCE.setAdapter(ada_PROVINCE);
        ada_CITY = new ArrayAdapter<>(context, android.R.layout
                .simple_spinner_dropdown_item, Cites);
        sp_CITY.setAdapter(ada_CITY);
        ada_DISTRICT = new ArrayAdapter<>(context, android.R.layout
                .simple_spinner_dropdown_item, Districts);
        sp_DISTRICT.setAdapter(ada_DISTRICT);

        ada_AMOUNT_UNITS = new ArrayAdapter<>(context, android.R.layout
                .simple_spinner_dropdown_item, AMOUNT_UNITS);
        sp_DRAW_NUM_UNIT.setAdapter(ada_AMOUNT_UNITS);
        sp_DRAW_AMOUNT_UNIT.setAdapter(ada_AMOUNT_UNITS);
        sp_STORAGESITE_UNIT.setAdapter(ada_AMOUNT_UNITS);
        ada_UNIVALENT_UNIT = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, UNIVALENT_UNITS);
        sp_UNIVALENT_UNIT.setAdapter(ada_UNIVALENT_UNIT);

        ada_FOOD_KIND1 = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, FoodKinds1);
        sp_FOOD_KIND1.setAdapter(ada_FOOD_KIND1);
        ada_FOOD_KIND2 = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, FoodKinds2);
        sp_FOOD_KIND2.setAdapter(ada_FOOD_KIND2);
        ada_FOOD_KIND3 = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, FoodKinds3);
        sp_FOOD_KIND3.setAdapter(ada_FOOD_KIND3);
        ada_FOOD_KIND4 = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, FoodKinds4);
        sp_FOOD_KIND4.setAdapter(ada_FOOD_KIND4);
        ada_CHILD_FOOD_KIND_ID = new ArrayAdapter<>
                (context, android.R.layout.simple_list_item_1, SamplingContracts);
        sp_CHILD_FOOD_KIND_ID.setAdapter(ada_CHILD_FOOD_KIND_ID);

        ada_MANU_PROVINCE = new ArrayAdapter<>(context, android.R.layout
                .simple_spinner_dropdown_item, Provinces);
        sp_MANU_PROVINCE.setAdapter(ada_MANU_PROVINCE);
        ada_MANU_CITY = new ArrayAdapter<>(context, android.R.layout
                .simple_spinner_dropdown_item, Cites_MANU);
        sp_MANU_CITY.setAdapter(ada_MANU_CITY);
        ada_MANU_DISTRICT = new ArrayAdapter<>(context, android.R.layout
                .simple_spinner_dropdown_item, Districts_MANU);
        sp_MANU_DISTRICT.setAdapter(ada_MANU_DISTRICT);

        toolbar = findViewById(R.id.toolbar_details);
        toolbar.setTitle("详细信息");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //设置toolbar
        setSupportActionBar(toolbar);
        //左边的小箭头（注意需要在setSupportActionBar(toolbar)之后才有效果）
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white);
        //菜单点击事件（注意需要在setSupportActionBar(toolbar)之后才有效果）
        //toolbar.setOnMenuItemClickListener(onMenuItemClick);

        attemptGetProvinces();
        attemptGetMeasureUnits("AMOUNT_UNIT");
        attemptGetMeasureUnits("UNIVALENT_UNIT");
        attemptGetSamplingContract();
    }

    public void initData() {
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
                layout_return.setVisibility(View.GONE);
            } else {
                tv_STATE.setText(task.getSTATE());
                tv_CHECK_INFO.setText(task.getCHECK_INFO());
            }
            tv_CUSTOM_NO.setText(task.getCUSTOM_NO());
            tv_NO.setText(task.getNO());
            et_GOODS_NAME.setText(task.getGOODS_NAME());
            et_SAMPLING_NOTICE_CODE.setText(task.getSAMPLING_NOTICE_CODE());
            tv_BUSINESS_SOURCE.setText(task.getBUSINESS_SOURCE());
            actv_SUPPLIER.setText(task.getSUPPLIER());
            tv_SUPPLIER_ADDR.setText(task.getSUPPLIER_ADDR());
            et_TOWN.setText(task.getTOWN());
            et_SUPPLIER_ADDR_TXT.setText(task.getSUPPLIER_ADDR_TXT());

            et_SUPPLIER_LEGAL.setText(task.getSUPPLIER_LEGAL());
            et_ANNUAL_SALES.setText(task.getANNUAL_SALES());
            et_BUSINESS_LICENCE.setText(task.getBUSINESS_LICENCE());
            et_SUPPLIER_PERSON.setText(task.getSUPPLIER_PERSON());
            et_PERMIT_NUM.setText(task.getPERMIT_NUM());
            et_SUPPLIER_PHONE.setText(task.getSUPPLIER_PHONE());
            et_SUPPLIER_FAX.setText(task.getSUPPLIER_FAX());
            et_SUPPLIER_ZIPCODE.setText(task.getSUPPLIER_ZIPCODE());
            et_DRAW_ADDR_OTHER.setText(task.getDRAW_ADDR_OTHER());
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

            et_MANU_TOWN.setText(task.getMANU_TOWN());
            et_MANU_COMPANY_ADDR_TXT.setText(task.getMANU_COMPANY_ADDR_TXT());

            et_MANU_COMPANY_PHONE.setText(task.getMANU_COMPANY_PHONE());
            tv_MANU_COMPANY_ADDR.setText(task.getMANU_COMPANY_ADDR());
            et_SAVE_MODE_OTHER.setText(task.getSAVE_MODE_OTHER());
            et_SAMPLE_CLOSE_DATE.setText(task.getSAMPLE_CLOSE_DATE());
            et_PACK_OTHER.setText(task.getPACK_OTHER());
            //tv_SAMPLE_ADDR.setText(task.getSAMPLE_ADDR());
            et_DRAW_ORG.setText(task.getDRAW_ORG());
            //tv_DRAW_ORG_ADDR.setText(task.getDRAW_ORG_ADDR());
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

            /*SpinnerAdapter adapter15 = sp_FOOD_KIND1.getAdapter();
            for (int i = 0; i < adapter15.getCount(); i++) {
                if (task.getFOOD_KIND1().equals(adapter15.getItem(i).toString())) {
                    sp_FOOD_KIND1.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter16 = sp_FOOD_KIND2.getAdapter();
            for (int i = 0; i < adapter16.getCount(); i++) {
                if (task.getFOOD_KIND2().equals(adapter16.getItem(i).toString())) {
                    sp_FOOD_KIND2.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter17 = sp_FOOD_KIND3.getAdapter();
            for (int i = 0; i < adapter17.getCount(); i++) {
                if (task.getFOOD_KIND3().equals(adapter17.getItem(i).toString())) {
                    sp_FOOD_KIND3.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter18 = sp_FOOD_KIND4.getAdapter();
            for (int i = 0; i < adapter18.getCount(); i++) {
                if (task.getFOOD_KIND4().equals(adapter18.getItem(i).toString())) {
                    sp_FOOD_KIND4.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter19 = sp_CHILD_FOOD_KIND_ID.getAdapter();
            for (int i = 0; i < adapter19.getCount(); i++) {
                if (task.getCHILD_FOOD_KIND_ID().equals(adapter19.getItem(i).toString())) {
                    sp_CHILD_FOOD_KIND_ID.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter20 = sp_PROVINCE.getAdapter();
            for (int i = 0; i < adapter20.getCount(); i++) {
                if (task.getPROVINCE().equals(adapter20.getItem(i).toString())) {
                    sp_PROVINCE.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter21 = sp_CITY.getAdapter();
            for (int i = 0; i < adapter21.getCount(); i++) {
                if (task.getCITY().equals(adapter21.getItem(i).toString())) {
                    sp_CITY.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter22 = sp_DISTRICT.getAdapter();
            for (int i = 0; i < adapter22.getCount(); i++) {
                if (task.getDISTRICT().equals(adapter22.getItem(i).toString())) {
                    sp_DISTRICT.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter23 = sp_MANU_PROVINCE.getAdapter();
            for (int i = 0; i < adapter23.getCount(); i++) {
                if (task.getMANU_PROVINCE().equals(adapter23.getItem(i).toString())) {
                    sp_MANU_PROVINCE.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter24 = sp_MANU_CITY.getAdapter();
            for (int i = 0; i < adapter24.getCount(); i++) {
                if (task.getMANU_CITY().equals(adapter24.getItem(i).toString())) {
                    sp_MANU_CITY.setSelection(i, true);
                    break;
                }
            }

            SpinnerAdapter adapter25 = sp_MANU_DISTRICT.getAdapter();
            for (int i = 0; i < adapter25.getCount(); i++) {
                if (task.getMANU_DISTRICT().equals(adapter25.getItem(i).toString())) {
                    sp_MANU_DISTRICT.setSelection(i, true);
                    break;
                }
            }
            */
        }
    }

    public void viewAction() {
        actv_SUPPLIER.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    Log.v("s.length()", "" + s.length());
                    long time = System.currentTimeMillis();
                    if (actv_SUPPLIER.getTag() == null || time - (long) actv_SUPPLIER.getTag() >
                            1000) {
                        if (s.length() < 10) {
                            Log.v("attempt", "1");
                            attemptSampleEnterprises(s.toString());
                        } else {
                            Log.v("isSelect", "1");
                        }
                    } else {
                        Log.v("attempt", "0");
                    }
                    actv_SUPPLIER.setTag(time);
                }
            }
        });

        actv_SUPPLIER.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = SUPPLIERS.indexOf(actv_SUPPLIER.getText().toString());
                oneSampleEnterprise = sampleEnterprises.get(pos);
                try {
                    FormatData(2);
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
                et_SUPPLIER_LEGAL.setText(oneSampleEnterprise.getLEGAL());
                et_SUPPLIER_FAX.setText(oneSampleEnterprise.getFAX());
                et_SUPPLIER_PERSON.setText(oneSampleEnterprise.getPERSON());
                tv_SUPPLIER_ADDR.setText(oneSampleEnterprise.getADDR());
                SpinnerAdapter adapter1 = sp_PROVINCE.getAdapter();
                for (int i = 0; i < adapter1.getCount(); i++) {
                    if (oneSampleEnterprise.getPROVINCE().equals(adapter1.getItem(i).toString()
                    )) {
                        sp_PROVINCE.setSelection(i, true);
                        break;
                    }
                }
                SpinnerAdapter adapter2 = sp_CITY.getAdapter();
                for (int i = 0; i < adapter2.getCount(); i++) {
                    if (oneSampleEnterprise.getCITY().equals(adapter2.getItem(i).toString()
                    )) {
                        sp_CITY.setSelection(i, true);
                        break;
                    }
                }
                SpinnerAdapter adapter3 = sp_DISTRICT.getAdapter();
                for (int i = 0; i < adapter3.getCount(); i++) {
                    if (oneSampleEnterprise.getDISTRICT().equals(adapter3.getItem(i).toString()
                    )) {
                        sp_DISTRICT.setSelection(i, true);
                        break;
                    }
                }
                et_TOWN.setText(oneSampleEnterprise.getTOWN());
                et_SUPPLIER_ADDR_TXT.setText(oneSampleEnterprise.getSUPPLIER_ADDR_TXT());
                et_SUPPLIER_PHONE.setText(oneSampleEnterprise.getPHONE());
                et_ANNUAL_SALES.setText(oneSampleEnterprise.getANNUAL_SALES());
                et_BUSINESS_LICENCE.setText(oneSampleEnterprise.getBUSINESS_LICENCE());
                SpinnerAdapter adapter4 = sp_PERMIT_TYPE.getAdapter();
                for (int i = 0; i < adapter4.getCount(); i++) {
                    if (oneSampleEnterprise.getPERMIT_TYPE().equals(adapter4.getItem(i).toString()
                    )) {
                        sp_PERMIT_TYPE.setSelection(i, true);
                        break;
                    }
                }
                et_PERMIT_NUM.setText(oneSampleEnterprise.getPERMIT_NUM());
                et_SUPPLIER_ZIPCODE.setText(oneSampleEnterprise.getZIPCODE());
            }
        });

        sp_CHILD_FOOD_KIND_ID.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO 自动生成的方法存根
                /*if (num_select > 10) {
                    setFocus(et_GOODS_NAME);
                } else {
                    num_select++;
                }*/
                if (getSamplingContract != null && getSamplingContract.size() > 0) {
                    Contract_ID = getSamplingContract.get(position).getID();
                    attemptGetFoodKind(Contract_ID, "TYPE1", "1");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO 自动生成的方法存根
            }
        });


        sp_FOOD_KIND1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO 自动生成的方法存根
                //Log.v("num_select", num_select + sp_FOOD_KIND1.getSelectedItem().toString() +
                // "1");
                if (num_select > 10) {
                    setFocus(et_MANU_COMPANY);
                } else {
                    num_select++;
                }
                et_GOODS_TYPE.setText(FoodKinds1[position]);
                attemptGetFoodKind(Contract_ID, "TYPE2", FoodKinds1[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO 自动生成的方法存根
            }
        });
        sp_FOOD_KIND2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO 自动生成的方法存根
                //.v("num_select", num_select + sp_FOOD_KIND2.getSelectedItem().toString() + "2");
                if (num_select > 10) {
                    setFocus(et_MANU_COMPANY);
                } else {
                    num_select++;
                }
                attemptGetFoodKind(Contract_ID, "TYPE3", FoodKinds2[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO 自动生成的方法存根
            }
        });
        sp_FOOD_KIND3.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO 自动生成的方法存根
                //Log.v("num_select", num_select + sp_FOOD_KIND3.getSelectedItem().toString() +
                // "3");
                if (num_select > 10) {
                    setFocus(et_MANU_COMPANY);
                } else {
                    num_select++;
                }
                attemptGetFoodKind(Contract_ID, "TYPE4", FoodKinds3[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO 自动生成的方法存根
            }
        });

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        tv_DRAW_DATE.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 自动生成的方法存
                new DatePickerDialog(context, 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker DatePicker,
                                          int Year, int MonthOfYear, int DayOfMonth) {
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
                                str_DATE_PRODUCT = String.format(Locale.CHINA, "%02d-%02d-%02d",
                                        Year, (MonthOfYear + 1), DayOfMonth);
                                tv_DATE_PRODUCT.setText(str_DATE_PRODUCT);
                            }
                        }, mYear, mMonth, mDay, true).show();
            }
        });

        sp_PROVINCE.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO 自动生成的方法存根
                if (getProvinces.size() > 0) {
                    attemptGetCitesByID(getProvinces.get(position).getID(), 1);
                }
                if (sp_PROVINCE.getSelectedItem() != null && sp_CITY.getSelectedItem() != null &&
                        sp_DISTRICT.getSelectedItem() != null) {
                    tv_SUPPLIER_ADDR.setText(String.format(getResources().getString(R.string
                            .supplier_addr), sp_PROVINCE.getSelectedItem().toString(), sp_CITY
                            .getSelectedItem().toString(), sp_DISTRICT.getSelectedItem()
                            .toString(), et_TOWN.getText().toString(), et_SUPPLIER_ADDR_TXT
                            .getText().toString()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO 自动生成的方法存根
            }
        });

        sp_CITY.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO 自动生成的方法存根
                if (getCites.size() > 0) {
                    attemptGetDistrictsByID(getCites.get(position).getID(), 1);
                }
                if (sp_PROVINCE.getSelectedItem() != null && sp_CITY.getSelectedItem() != null &&
                        sp_DISTRICT.getSelectedItem() != null) {
                    tv_SUPPLIER_ADDR.setText(String.format(getResources().getString(R.string
                            .supplier_addr), sp_PROVINCE.getSelectedItem().toString(), sp_CITY
                            .getSelectedItem().toString(), sp_DISTRICT.getSelectedItem()
                            .toString(), et_TOWN.getText().toString(), et_SUPPLIER_ADDR_TXT
                            .getText().toString()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO 自动生成的方法存根
            }
        });

        sp_DISTRICT.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO 自动生成的方法存根
                if (sp_PROVINCE.getSelectedItem() != null && sp_CITY.getSelectedItem() != null &&
                        sp_DISTRICT.getSelectedItem() != null) {
                    tv_SUPPLIER_ADDR.setText(String.format(getResources().getString(R.string
                            .supplier_addr), sp_PROVINCE.getSelectedItem().toString(), sp_CITY
                            .getSelectedItem().toString(), sp_DISTRICT.getSelectedItem().toString
                            (), et_TOWN.getText().toString(), et_SUPPLIER_ADDR_TXT.getText()
                            .toString()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO 自动生成的方法存根
            }
        });

        et_TOWN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_SUPPLIER_ADDR.setText(String.format(getResources().getString(R.string
                        .supplier_addr), sp_PROVINCE.getSelectedItem().toString(), sp_CITY
                        .getSelectedItem().toString(), sp_DISTRICT.getSelectedItem()
                        .toString(), s.toString(), et_SUPPLIER_ADDR_TXT.getText().toString()));
            }
        });

        et_SUPPLIER_ADDR_TXT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_SUPPLIER_ADDR.setText(String.format(getResources().getString(R.string
                        .supplier_addr), sp_PROVINCE.getSelectedItem().toString(), sp_CITY
                        .getSelectedItem().toString(), sp_DISTRICT.getSelectedItem()
                        .toString(), et_TOWN.getText().toString(), s.toString()));
            }
        });

        sp_MANU_PROVINCE.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO 自动生成的方法存根
                if (getProvinces.size() > 0) {
                    attemptGetCitesByID(getProvinces.get(position).getID(), 2);
                }
                if (et_MANU_COMPANY.getText().toString().equals("/")) {
                    tv_MANU_COMPANY_ADDR.setText("/");
                } else {
                    if (sp_MANU_PROVINCE.getSelectedItem() != null && sp_MANU_CITY.getSelectedItem()
                            != null && sp_MANU_DISTRICT.getSelectedItem() != null) {
                        tv_MANU_COMPANY_ADDR.setText(String.format(getResources().getString(R.string
                                        .manu_company_addr), sp_MANU_PROVINCE.getSelectedItem()
                                        .toString(), sp_MANU_CITY.getSelectedItem().toString(),
                                sp_MANU_DISTRICT
                                        .getSelectedItem().toString(), et_MANU_TOWN.getText()
                                        .toString(),
                                et_MANU_COMPANY_ADDR_TXT.getText().toString()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO 自动生成的方法存根
            }
        });

        sp_MANU_CITY.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO 自动生成的方法存根
                if (getCites_MANU.size() > 0) {
                    attemptGetDistrictsByID(getCites_MANU.get(position).getID(), 2);
                }
                if (et_MANU_COMPANY.getText().toString().equals("/")) {
                    tv_MANU_COMPANY_ADDR.setText("/");
                } else {
                    if (sp_MANU_PROVINCE.getSelectedItem() != null && sp_MANU_CITY.getSelectedItem()
                            != null && sp_MANU_DISTRICT.getSelectedItem() != null) {
                        tv_MANU_COMPANY_ADDR.setText(String.format(getResources().getString(R.string
                                        .manu_company_addr), sp_MANU_PROVINCE.getSelectedItem()
                                        .toString(), sp_MANU_CITY.getSelectedItem().toString(),
                                sp_MANU_DISTRICT
                                        .getSelectedItem().toString(), et_MANU_TOWN.getText()
                                        .toString(),
                                et_MANU_COMPANY_ADDR_TXT.getText().toString()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO 自动生成的方法存根
            }
        });

        sp_MANU_DISTRICT.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO 自动生成的方法存根
                if (et_MANU_COMPANY.getText().toString().equals("/")) {
                    tv_MANU_COMPANY_ADDR.setText("/");
                } else {
                    if (sp_MANU_PROVINCE.getSelectedItem() != null && sp_MANU_CITY.getSelectedItem()
                            != null && sp_MANU_DISTRICT.getSelectedItem() != null) {
                        tv_MANU_COMPANY_ADDR.setText(String.format(getResources().getString(R.string
                                        .manu_company_addr), sp_MANU_PROVINCE.getSelectedItem()
                                        .toString(), sp_MANU_CITY.getSelectedItem().toString(),
                                sp_MANU_DISTRICT
                                        .getSelectedItem().toString(), et_MANU_TOWN.getText()
                                        .toString(),
                                et_MANU_COMPANY_ADDR_TXT.getText().toString()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO 自动生成的方法存根
            }
        });


        et_MANU_TOWN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_MANU_COMPANY.getText().toString().equals("/")) {
                    tv_MANU_COMPANY_ADDR.setText("/");
                } else {
                    tv_MANU_COMPANY_ADDR.setText(String.format(getResources().getString(R.string
                                    .manu_company_addr), sp_MANU_PROVINCE.getSelectedItem()
                                    .toString(), sp_MANU_CITY.getSelectedItem().toString(),
                            sp_MANU_DISTRICT.getSelectedItem().toString(), s.toString(),
                            et_MANU_COMPANY_ADDR_TXT.getText().toString()));
                }
            }
        });

        et_MANU_COMPANY_ADDR_TXT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_MANU_COMPANY.getText().toString().equals("/")) {
                    tv_MANU_COMPANY_ADDR.setText("/");
                } else {
                    tv_MANU_COMPANY_ADDR.setText(String.format(getResources().getString(R.string
                                    .manu_company_addr), sp_MANU_PROVINCE.getSelectedItem()
                                    .toString(),
                            sp_MANU_CITY.getSelectedItem().toString(),
                            sp_MANU_DISTRICT.getSelectedItem().toString(),
                            et_MANU_TOWN.getText().toString(), s.toString()));
                }
            }
        });

        sp_DRAW_NUM_UNIT.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO 自动生成的方法存根
                sp_DRAW_AMOUNT_UNIT.setSelection(position, true);
                sp_STORAGESITE_UNIT.setSelection(position, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO 自动生成的方法存根
            }
        });

        sp_DRAW_AMOUNT_UNIT.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO 自动生成的方法存根
                sp_DRAW_NUM_UNIT.setSelection(position, true);
                sp_STORAGESITE_UNIT.setSelection(position, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO 自动生成的方法存根
            }
        });

        sp_STORAGESITE_UNIT.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO 自动生成的方法存根
                sp_DRAW_NUM_UNIT.setSelection(position, true);
                sp_DRAW_AMOUNT_UNIT.setSelection(position, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO 自动生成的方法存根
            }
        });
    }

    public void attemptSampleEnterprises(String key) {
        if (((MyApplication) getApplication()).getTOKEN() == null) {
            token = sharedPreferences.getString("TOKEN", null);
        } else {
            token = ((MyApplication) getApplication()).getTOKEN();
        }
        FDA_API request = HttpUtils.JsonApi();
        Call<List<sampleEnterprise>> call = request.sampleEnterprises(token, key);
        call.enqueue(new Callback<List<sampleEnterprise>>() {
            @Override
            public void onResponse(Call<List<sampleEnterprise>> call,
                                   Response<List<sampleEnterprise>> response) {
                if (response.code() == 401) {
                    Log.v("sampleEnterprises请求", "token过期");
                    Intent intent_login = new Intent();
                    intent_login.setClass(DetailsActivity.this,
                            LoginActivity.class);
                    intent_login.putExtra("login_type", 1);
                    startActivity(intent_login);
                } else if (response.code() == 200) {
                    if (response.body() != null) {
                        Log.v("SampleEnterprises请求成功!", "response.body is not null");
                        sampleEnterprises = response.body();
                        Log.v("sampleEnterprises.size", "" + sampleEnterprises.size());
                        SUPPLIERS.clear();
                        for (int i = 0; i < sampleEnterprises.size(); i++) {
                            SUPPLIERS.add(sampleEnterprises.get(i).getNAME());
                        }
                        Log.v("sampleEnterprises.size", "over");
                        /*需要一个适配器 初始化数据源--这个数据源去匹配文本框中输入的内容*/
                        ArrayAdapter ada_SUPPLIER = new ArrayAdapter<>(context, android.R.layout
                                .simple_list_item_1, SUPPLIERS);
                        /*将adpter与当前AutoCompleteTextView绑定*/
                        actv_SUPPLIER.setAdapter(ada_SUPPLIER);
                        ada_SUPPLIER.notifyDataSetChanged();
                    } else {
                        Log.v("SampleEnterprises请求成功!", "response.body is null");
                    }
                } else {
                    Log.v("SampleEnterprises请求成功!", "提交失败!(" + response.code() + ")请稍后再试");
                }
            }

            @Override
            public void onFailure(Call<List<sampleEnterprise>> call, Throwable t) {
                Log.v("SampleEnterprises请求失败!", t.getMessage());
            }
        });
    }

    public void attemptGetProvinces() {
        FDA_API request = HttpUtils.JsonApi();
        Call<List<Province>> call = request.getProvinces();
        call.enqueue(new Callback<List<Province>>() {
            @Override
            public void onResponse(Call<List<Province>> call, Response<List<Province>> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        Log.v("getProvinces请求成功!", "response.body is not null");
                        getProvinces = response.body();
                        if (getProvinces.size() > 0) {
                            Log.v("Provinces.size", "" + getProvinces.size());
                            Provinces = new String[getProvinces.size()];
                            for (int i = 0; i < getProvinces.size(); i++) {
                                Provinces[i] = getProvinces.get(i).getNAME();
                            }
                        } else {
                            Provinces = new String[]{"省加载失败"};
                        }
                        ada_PROVINCE = new ArrayAdapter<>(context, android.R.layout
                                .simple_spinner_dropdown_item, Provinces);
                        ada_MANU_PROVINCE = new ArrayAdapter<>(context, android.R.layout
                                .simple_spinner_dropdown_item, Provinces);
                        sp_PROVINCE.setAdapter(ada_PROVINCE);
                        sp_MANU_PROVINCE.setAdapter(ada_MANU_PROVINCE);
                        for (int i = 0; i < ada_PROVINCE.getCount(); i++) {
                            if (task.getPROVINCE().equals(ada_PROVINCE.getItem(i))) {
                                sp_PROVINCE.setSelection(i, true);
                                break;
                            }
                        }
                        for (int j = 0; j < ada_MANU_PROVINCE.getCount(); j++) {
                            if (task.getMANU_PROVINCE().equals(ada_MANU_PROVINCE.getItem(j))) {
                                sp_MANU_PROVINCE.setSelection(j, true);
                                break;
                            }
                        }
                    } else {
                        Log.v("getProvinces请求成功!", "response.body is null");
                    }
                } else {
                    Log.v("getProvinces请求失败!", "请求失败!(" + response.code() + ")请稍后再试");
                }
            }

            @Override
            public void onFailure(Call<List<Province>> call, Throwable t) {
                Log.v("getProvinces请求失败!", t.getMessage());
            }
        });
    }

    public void attemptGetCitesByID(int ID, final int type) {
        FDA_API request = HttpUtils.JsonApi();
        Call<List<City>> call = request.getCitesByID(ID);
        call.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        Log.v("getCites请求成功!", "response.body is not null");
                        if (type == 1) {
                            getCites = response.body();
                            if (getCites.size() > 0) {
                                Log.v("Cites.size", "" + getCites.size());
                                Cites = new String[getCites.size()];
                                for (int i = 0; i < getCites.size(); i++) {
                                    Cites[i] = getCites.get(i).getNAME();
                                }
                            } else {
                                Cites = new String[]{"区加载失败"};
                            }
                            ada_CITY = new ArrayAdapter<>(context, android.R.layout
                                    .simple_spinner_dropdown_item, Cites);
                            sp_CITY.setAdapter(ada_CITY);

                            if (num_Cites == 0) {
                                for (int i = 0; i < ada_CITY.getCount(); i++) {
                                    if (task.getCITY().equals(ada_CITY.getItem(i))) {
                                        sp_CITY.setSelection(i, true);
                                        break;
                                    }
                                }
                                num_Cites++;
                            }
                        } else if (type == 2) {
                            getCites_MANU = response.body();
                            if (getCites_MANU.size() > 0) {
                                Log.v("Cites_MANU.size", "" + getCites_MANU.size());
                                Cites_MANU = new String[getCites_MANU.size()];
                                for (int i = 0; i < getCites_MANU.size(); i++) {
                                    Cites_MANU[i] = getCites_MANU.get(i).getNAME();
                                }
                            } else {
                                Cites_MANU = new String[]{"区加载失败"};
                            }
                            ada_MANU_CITY = new ArrayAdapter<>(context, android.R.layout
                                    .simple_spinner_dropdown_item, Cites_MANU);
                            sp_MANU_CITY.setAdapter(ada_MANU_CITY);

                            if (num_Cites_MANU == 0) {
                                for (int i = 0; i < ada_MANU_CITY.getCount(); i++) {
                                    if (task.getMANU_CITY().equals(ada_MANU_CITY.getItem(i))) {
                                        sp_MANU_CITY.setSelection(i, true);
                                        break;
                                    }
                                }
                                num_Cites_MANU++;
                            }
                        }
                    } else {
                        Log.v("getCites请求成功!", "response.body is null");
                    }
                } else {
                    Log.v("getCites请求失败!", "请求失败!(" + response.code() + ")请稍后再试");
                }
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                Log.v("getCites请求失败!", t.getMessage());
            }
        });
    }

    public void attemptGetDistrictsByID(int ID, final int type) {
        FDA_API request = HttpUtils.JsonApi();
        Call<List<District>> call = request.getDistrictsByID(ID);
        call.enqueue(new Callback<List<District>>() {
            @Override
            public void onResponse(Call<List<District>> call, Response<List<District>> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        Log.v("getDistricts请求成功!", "response.body is not null");
                        if (type == 1) {
                            getDistricts = response.body();
                            if (getDistricts.size() > 0) {
                                Log.v("Districts.size", "" + getDistricts.size());
                                Districts = new String[getDistricts.size()];
                                for (int i = 0; i < getDistricts.size(); i++) {
                                    Districts[i] = getDistricts.get(i).getNAME();
                                }
                            } else {
                                Districts = new String[]{"区加载失败"};
                            }
                            ada_DISTRICT = new ArrayAdapter<>(context, android.R.layout
                                    .simple_spinner_dropdown_item, Districts);
                            sp_DISTRICT.setAdapter(ada_DISTRICT);

                            if (num_Districts == 0) {
                                for (int i = 0; i < ada_DISTRICT.getCount(); i++) {
                                    if (task.getDISTRICT().equals(ada_DISTRICT.getItem(i))) {
                                        sp_DISTRICT.setSelection(i, true);
                                        break;
                                    }
                                }
                                num_Districts++;
                            }
                        } else if (type == 2) {
                            getDistricts_MANU = response.body();
                            if (getDistricts_MANU.size() > 0) {
                                Log.v("Districts_MANU.size", "" + getDistricts_MANU.size());
                                Districts_MANU = new String[getDistricts_MANU.size()];
                                for (int i = 0; i < getDistricts_MANU.size(); i++) {
                                    Districts_MANU[i] = getDistricts_MANU.get(i).getNAME();
                                }
                            } else {
                                Districts_MANU = new String[]{"区加载失败"};
                            }
                            ada_MANU_DISTRICT = new ArrayAdapter<>(context, android.R.layout
                                    .simple_spinner_dropdown_item, Districts_MANU);
                            sp_MANU_DISTRICT.setAdapter(ada_MANU_DISTRICT);
                            if (num_Districts_MANU == 0) {
                                for (int i = 0; i < ada_MANU_DISTRICT.getCount(); i++) {
                                    if (task.getMANU_DISTRICT().equals(ada_MANU_DISTRICT.getItem
                                            (i))) {
                                        sp_MANU_DISTRICT.setSelection(i, true);
                                        break;
                                    }
                                }
                                num_Districts_MANU++;
                            }
                        }
                    } else {
                        Log.v("getDistricts请求成功!", "response.body is null");
                    }
                } else {
                    Log.v("getDistricts请求失败!", "请求失败!(" + response.code() + ")请稍后再试");
                }
            }

            @Override
            public void onFailure(Call<List<District>> call, Throwable t) {
                Log.v("getDistricts请求失败!", t.getMessage());
            }
        });
    }

    public void attemptGetMeasureUnits(final String type) {
        FDA_API request = HttpUtils.JsonApi();
        Call<List<MeasureUnit>> call = request.getMeasureUnits(type);
        call.enqueue(new Callback<List<MeasureUnit>>() {
            @Override
            public void onResponse(Call<List<MeasureUnit>> call, Response<List<MeasureUnit>>
                    response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        Log.v("getMeasureUnits请求成功!", "response.body is not null");
                        if (type.equals("AMOUNT_UNIT")) {
                            getMeasureUnit_AMOUNT_UNIT = response.body();
                            if (getMeasureUnit_AMOUNT_UNIT.size() > 0) {
                                Log.v("AMOUNT_UNITS.size", "" + getMeasureUnit_AMOUNT_UNIT
                                        .size());
                                AMOUNT_UNITS = new String[getMeasureUnit_AMOUNT_UNIT
                                        .size()];
                                for (int i = 0; i < getMeasureUnit_AMOUNT_UNIT.size(); i++) {
                                    if (getMeasureUnit_AMOUNT_UNIT.get(i).getUNIT_NAME() != null) {
                                        AMOUNT_UNITS[i] = getMeasureUnit_AMOUNT_UNIT
                                                .get(i).getUNIT_NAME();
                                    }
                                }
                            } else {
                                AMOUNT_UNITS = new String[]{"无任何单位"};
                            }
                            ada_AMOUNT_UNITS = new ArrayAdapter<>(context, android.R.layout
                                    .simple_spinner_dropdown_item, AMOUNT_UNITS);
                            sp_DRAW_NUM_UNIT.setAdapter(ada_AMOUNT_UNITS);
                            sp_DRAW_AMOUNT_UNIT.setAdapter(ada_AMOUNT_UNITS);
                            sp_STORAGESITE_UNIT.setAdapter(ada_AMOUNT_UNITS);

                            if (num_AMOUNT_UNITS == 0) {
                                for (int i = 0; i < ada_AMOUNT_UNITS.getCount(); i++) {
                                    if (task.getDRAW_AMOUNT_UNIT().equals(ada_AMOUNT_UNITS
                                            .getItem(i))) {
                                        sp_DRAW_NUM_UNIT.setSelection(i, true);
                                        sp_DRAW_AMOUNT_UNIT.setSelection(i, true);
                                        sp_STORAGESITE_UNIT.setSelection(i, true);
                                        break;
                                    }
                                }
                                num_AMOUNT_UNITS++;
                            }

                        } else if (type.equals("UNIVALENT_UNIT")) {
                            getMeasureUnit_UNIVALENT_UNIT = response.body();
                            if (getMeasureUnit_UNIVALENT_UNIT.size() > 0) {
                                Log.v("UNIVALENT_UNITS.size", "" + getMeasureUnit_UNIVALENT_UNIT
                                        .size());
                                UNIVALENT_UNITS = new String[getMeasureUnit_UNIVALENT_UNIT.size
                                        ()];
                                for (int i = 0; i < getMeasureUnit_UNIVALENT_UNIT.size(); i++) {
                                    if (getMeasureUnit_UNIVALENT_UNIT.get(i).getUNIT_NAME() !=
                                            null) {
                                        UNIVALENT_UNITS[i] = getMeasureUnit_UNIVALENT_UNIT.get(i)
                                                .getUNIT_NAME();
                                    }
                                }
                            } else {
                                UNIVALENT_UNITS = new String[]{"无任何单位"};
                            }
                            ada_UNIVALENT_UNIT = new ArrayAdapter<>(context,
                                    android.R.layout.simple_spinner_dropdown_item, UNIVALENT_UNITS);
                            sp_UNIVALENT_UNIT.setAdapter(ada_UNIVALENT_UNIT);

                            if (num_UNIVALENT_UNITS == 0) {
                                for (int i = 0; i < ada_UNIVALENT_UNIT.getCount(); i++) {
                                    if (task.getUNIVALENT_UNIT().equals(ada_UNIVALENT_UNIT
                                            .getItem(i))) {
                                        sp_UNIVALENT_UNIT.setSelection(i, true);
                                        break;
                                    }
                                }
                                num_UNIVALENT_UNITS++;
                            }
                        }
                    } else {
                        Log.v("getMeasureUnits请求成功!", "response.body is null");
                    }
                } else {
                    Log.v("getMeasureUnits请求失败!", "请求失败!(" + response.code() + ")请稍后再试");
                }
            }

            @Override
            public void onFailure(Call<List<MeasureUnit>> call, Throwable t) {
                Log.v("getMeasureUnits请求失败!", t.getMessage());
            }
        });
    }

    public void attemptGetSamplingContract() {
        if (((MyApplication) getApplication()).getTOKEN() == null) {
            token = sharedPreferences.getString("TOKEN", null);
        } else {
            token = ((MyApplication) getApplication()).getTOKEN();
        }
        FDA_API request = HttpUtils.JsonApi();
        Call<List<FoodKind>> call = request.getSamplingContract(token);
        call.enqueue(new Callback<List<FoodKind>>() {
            @Override
            public void onResponse(Call<List<FoodKind>> call, Response<List<FoodKind>> response) {
                if (response.code() == 401) {
                    Log.v("getSamplingContract请求", "token过期");
                    Intent intent_login = new Intent();
                    intent_login.setClass(DetailsActivity.this,
                            LoginActivity.class);
                    intent_login.putExtra("login_type", 1);
                    startActivity(intent_login);
                } else if (response.code() == 200) {
                    if (response.body() != null) {
                        Log.v("SamplingContract请求成功!", "response.body is not null");
                        getSamplingContract = response.body();
                        Log.v("SamplingContractsize", "" + getSamplingContract.size());
                        if (getSamplingContract.size() > 0) {
                            SamplingContracts = new String[getSamplingContract.size()];
                            for (int i = 0; i < getSamplingContract.size(); i++) {
                                SamplingContracts[i] = getSamplingContract.get(i).getNAME();
                            }
                        } else {
                            SamplingContracts = new String[]{"无任何合同"};
                        }
                        ada_CHILD_FOOD_KIND_ID = new ArrayAdapter<>
                                (context, android.R.layout.simple_list_item_1, SamplingContracts);
                        sp_CHILD_FOOD_KIND_ID.setAdapter(ada_CHILD_FOOD_KIND_ID);
                        for (int i = 0; i < ada_CHILD_FOOD_KIND_ID.getCount(); i++) {
                            if (task.getCHILD_FOOD_KIND_ID().equals("" + getSamplingContract.get(i)
                                    .getID())) {
                                sp_CHILD_FOOD_KIND_ID.setSelection(i, true);
                                break;
                            }
                        }
                    } else {
                        Log.v("SamplingContract请求成功!", "response.body is null");
                    }
                } else {
                    Log.v("SamplingContract请求成功!", "提交失败!(" + response.code() + ")请稍后再试");
                }
            }

            @Override
            public void onFailure(Call<List<FoodKind>> call, Throwable t) {
                Log.v("SamplingContract请求失败!", t.getMessage());
            }
        });
    }

    public void attemptGetFoodKind(int Contract_ID, final String Food_Kind_Type, String
            Parent_Food_Kind_Name) {
        if (((MyApplication) getApplication()).getTOKEN() == null) {
            token = sharedPreferences.getString("TOKEN", null);
        } else {
            token = ((MyApplication) getApplication()).getTOKEN();
        }
        FDA_API request = HttpUtils.JsonApi();
        Call<List<FoodKind>> call = request.getFoodKind(token, Contract_ID, Food_Kind_Type,
                Parent_Food_Kind_Name);
        call.enqueue(new Callback<List<FoodKind>>() {
            @Override
            public void onResponse(Call<List<FoodKind>> call,
                                   Response<List<FoodKind>> response) {
                if (response.code() == 401) {
                    Log.v("getFoodKind请求", "token过期");
                    Intent intent_login = new Intent();
                    intent_login.setClass(DetailsActivity.this,
                            LoginActivity.class);
                    intent_login.putExtra("login_type", 1);
                    startActivity(intent_login);
                } else if (response.code() == 200) {
                    if (response.body() != null) {
                        if (Food_Kind_Type.equals("TYPE1")) {
                            Log.v("getFoodKind1请求成功!", "response.body is not null");
                            getFoodKind1 = response.body();
                            Log.v("FoodKind1size", "" + getFoodKind1.size());
                            FoodKinds1 = new String[getFoodKind1.size()];
                            for (int i = 0; i < getFoodKind1.size(); i++) {
                                FoodKinds1[i] = getFoodKind1.get(i).getNAME();
                            }
                            ada_FOOD_KIND1 = new ArrayAdapter<>(context,
                                    android.R.layout.simple_list_item_1, FoodKinds1);
                            sp_FOOD_KIND1.setAdapter(ada_FOOD_KIND1);
                            if (num_FoodKinds1 == 0) {
                                for (int i = 0; i < ada_FOOD_KIND1.getCount(); i++) {
                                    if (task.getFOOD_KIND1().equals(ada_FOOD_KIND1.getItem(i))) {
                                        sp_FOOD_KIND1.setSelection(i, true);
                                        break;
                                    }
                                }
                                num_FoodKinds1++;
                            }
                            Log.v("FOOD_KIND1", task.getFOOD_KIND1());
                        } else if (Food_Kind_Type.equals("TYPE2")) {
                            Log.v("getFoodKind2请求成功!", "response.body is not null");
                            getFoodKind2 = response.body();
                            Log.v("FoodKind2size", "" + getFoodKind2.size());
                            FoodKinds2 = new String[getFoodKind2.size()];
                            for (int i = 0; i < getFoodKind2.size(); i++) {
                                FoodKinds2[i] = getFoodKind2.get(i).getNAME();
                            }
                            ada_FOOD_KIND2 = new ArrayAdapter<>(context,
                                    android.R.layout.simple_list_item_1, FoodKinds2);
                            sp_FOOD_KIND2.setAdapter(ada_FOOD_KIND2);
                            if (num_FoodKinds2 <= 1) {
                                for (int i = 0; i < ada_FOOD_KIND2.getCount(); i++) {
                                    if (task.getFOOD_KIND2().equals(ada_FOOD_KIND2.getItem(i))) {
                                        sp_FOOD_KIND2.setSelection(i, true);
                                        break;
                                    }
                                }
                                num_FoodKinds2++;
                            }
                            Log.v("FOOD_KIND2", task.getFOOD_KIND2());
                        } else if (Food_Kind_Type.equals("TYPE3")) {
                            Log.v("getFoodKind3请求成功!", "response.body is not null");
                            getFoodKind3 = response.body();
                            Log.v("FoodKind3size", "" + getFoodKind3.size());
                            FoodKinds3 = new String[getFoodKind3.size()];
                            for (int i = 0; i < getFoodKind3.size(); i++) {
                                FoodKinds3[i] = getFoodKind3.get(i).getNAME();
                            }
                            ada_FOOD_KIND3 = new ArrayAdapter<>(context,
                                    android.R.layout.simple_list_item_1, FoodKinds3);
                            sp_FOOD_KIND3.setAdapter(ada_FOOD_KIND3);
                            if (num_FoodKinds3 <= 1) {
                                for (int i = 0; i < ada_FOOD_KIND3.getCount(); i++) {
                                    if (task.getFOOD_KIND3().equals(ada_FOOD_KIND3.getItem(i))) {
                                        sp_FOOD_KIND3.setSelection(i, true);
                                        break;
                                    }
                                }
                                num_FoodKinds3++;
                            }
                            Log.v("FOOD_KIND3", task.getFOOD_KIND3());
                        } else if (Food_Kind_Type.equals("TYPE4")) {
                            Log.v("getFoodKind4请求成功!", "response.body is not null");
                            getFoodKind4 = response.body();
                            Log.v("FoodKind4size", "" + getFoodKind4.size());
                            FoodKinds4 = new String[getFoodKind4.size()];
                            for (int i = 0; i < getFoodKind4.size(); i++) {
                                FoodKinds4[i] = getFoodKind4.get(i).getNAME();
                            }
                            ada_FOOD_KIND4 = new ArrayAdapter<>(context,
                                    android.R.layout.simple_list_item_1, FoodKinds4);
                            sp_FOOD_KIND4.setAdapter(ada_FOOD_KIND4);
                            if (num_FoodKinds4 <= 1) {
                                for (int i = 0; i < ada_FOOD_KIND4.getCount(); i++) {
                                    if (task.getFOOD_KIND4().equals(ada_FOOD_KIND4.getItem(i))) {
                                        sp_FOOD_KIND4.setSelection(i, true);
                                        break;
                                    }
                                }
                                num_FoodKinds4++;
                            }
                            Log.v("FOOD_KIND4", task.getFOOD_KIND4());
                        }
                    } else {
                        Log.v("getFoodKind请求成功!", "response.body is null");
                    }
                } else {
                    Log.v("getFoodKind请求成功!", "提交失败!(" + response.code() + ")请稍后再试");
                }
            }

            @Override
            public void onFailure(Call<List<FoodKind>> call, Throwable t) {
                Log.v("getFoodKind请求失败!", t.getMessage());
            }
        });
    }

    public void doSaveData() {
        try {
            task.setCUSTOM_NO(tv_CUSTOM_NO.getText().toString());
            //task.setNO(tv_NO.getText().toString());
            task.setGOODS_NAME(et_GOODS_NAME.getText().toString());
            task.setSAMPLING_NOTICE_CODE(et_SAMPLING_NOTICE_CODE.getText().toString());
            task.setBUSINESS_SOURCE(tv_BUSINESS_SOURCE.getText().toString());
            task.setSAMPLE_TYPE(sp_SAMPLE_TYPE.getSelectedItem().toString());
            task.setSUPPLIER(actv_SUPPLIER.getText().toString());
            task.setDOMESTIC_AREA(sp_DOMESTIC_AREA.getSelectedItem().toString());
            task.setSUPPLIER_ADDR(tv_SUPPLIER_ADDR.getText().toString());
            task.setPROVINCE(sp_PROVINCE.getSelectedItem().toString());
            task.setCITY(sp_CITY.getSelectedItem().toString());
            task.setDISTRICT(sp_DISTRICT.getSelectedItem().toString());
            task.setTOWN(et_TOWN.getText().toString());
            task.setSUPPLIER_ADDR_TXT(et_SUPPLIER_ADDR_TXT.getText().toString());
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
            task.setDRAW_ADDR_OTHER(et_DRAW_ADDR_OTHER.getText().toString());
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
            task.setUNIVALENT_UNIT(sp_UNIVALENT_UNIT.getSelectedItem().toString());
            task.setI_AND_O(sp_I_AND_O.getSelectedItem().toString());
            task.setDRAW_NUM(et_DRAW_NUM.getText().toString());
            task.setDRAW_AMOUNT(et_DRAW_AMOUNT.getText().toString());
            task.setSTORAGESITE(et_STORAGESITE.getText().toString());
            task.setDRAW_AMOUNT_UNIT(sp_DRAW_AMOUNT_UNIT.getSelectedItem().toString());
            task.setSAMPLE_STATUS(sp_SAMPLE_STATUS.getSelectedItem().toString());
            task.setPACK_TYPE(sp_PACK_TYPE.getSelectedItem().toString());
            task.setMANU_COMPANY(et_MANU_COMPANY.getText().toString());
            task.setMANU_COMPANY_PHONE(et_MANU_COMPANY_PHONE.getText().toString());
            if (et_MANU_COMPANY.getText().toString().equals("/")) {
                task.setMANU_COMPANY_ADDR("/");
                task.setMANU_PROVINCE("/");
                task.setMANU_CITY("/");
                task.setMANU_DISTRICT("/");
                task.setMANU_TOWN("/");
                task.setMANU_COMPANY_ADDR_TXT("/");
            } else {
                task.setMANU_COMPANY_ADDR(tv_MANU_COMPANY_ADDR.getText().toString());
                task.setMANU_PROVINCE(sp_MANU_PROVINCE.getSelectedItem().toString());
                task.setMANU_CITY(sp_MANU_CITY.getSelectedItem().toString());
                task.setMANU_DISTRICT(sp_MANU_DISTRICT.getSelectedItem().toString());
                task.setMANU_TOWN(et_MANU_TOWN.getText().toString());
                task.setMANU_COMPANY_ADDR_TXT(et_MANU_COMPANY_ADDR_TXT.getText().toString());
            }
            task.setSAVE_MODE(sp_SAVE_MODE.getSelectedItem().toString());
            task.setSAVE_MODE_OTHER(et_SAVE_MODE_OTHER.getText().toString());
            task.setSAMPLE_CLOSE_DATE(et_SAMPLE_CLOSE_DATE.getText().toString());
            task.setSAMPLE_ADDR(tv_SAMPLE_ADDR.getText().toString());
            task.setPACK(sp_PACK.getSelectedItem().toString());
            task.setPACK_OTHER(et_PACK_OTHER.getText().toString());
            task.setDRAW_METHOD(sp_DRAW_METHOD.getSelectedItem().toString());
            task.setFOOD_KIND1(sp_FOOD_KIND1.getSelectedItem().toString());
            task.setFOOD_KIND2(sp_FOOD_KIND2.getSelectedItem().toString());
            task.setFOOD_KIND3(sp_FOOD_KIND3.getSelectedItem().toString());
            task.setFOOD_KIND4(sp_FOOD_KIND4.getSelectedItem().toString());
            for (int i = 0; i < getSamplingContract.size(); i++) {
                if (getSamplingContract.get(i).getNAME().equals(sp_CHILD_FOOD_KIND_ID
                        .getSelectedItem().toString())) {
                    task.setCHILD_FOOD_KIND_ID("" + getSamplingContract.get(i).getID());
                    Log.v("CHILD_FOOD_KIND_ID", "" + getSamplingContract.get(i).getID());
                    break;
                } else {
                    task.setCHILD_FOOD_KIND_ID("");
                    Log.v("CHILD_FOOD_KIND_ID", "空");
                }
            }
            task.setDRAW_ORG(et_DRAW_ORG.getText().toString());
            task.setDRAW_ORG_ADDR(tv_DRAW_ORG_ADDR.getText().toString());
            task.setDRAW_PERSON(et_DRAW_PERSON.getText().toString());
            task.setDRAW_PHONE(et_DRAW_PHONE.getText().toString());
            task.setDRAW_FAX(et_DRAW_FAX.getText().toString());
            task.setDRAW_ZIPCODE(et_DRAW_ZIPCODE.getText().toString());
            task.setREMARK(et_REMARK.getText().toString());
            task.setDRAW_DATE(tv_DRAW_DATE.getText().toString());
            task.setGOODS_TYPE(et_GOODS_TYPE.getText().toString());
            task.setDRAW_MAN(et_DRAW_MAN.getText().toString());
            task.setDRAW_MAN_NO(DRAW_MAN_NO);

            //更新列表
            Tasks.list_task.set(Tasks.position, task);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void attemptProvSubmit() {
        mypDialog = new ProgressDialog(DetailsActivity.this);
        // 实例化
        mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置进度条风格，风格为圆形，旋转的
        mypDialog.setTitle("暂存信息中...");
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
        //Log.v("data", data);
        FDA_API request = HttpUtils.JsonApi();
        Call<SubmitStatus> call = request.provSubmit(token, data);
        call.enqueue(new Callback<SubmitStatus>() {
            @Override
            public void onResponse(Call<SubmitStatus> call, Response<SubmitStatus> response) {
                if (response.code() == 401) {
                    Log.v("provSubmit请求", "token过期");
                    Intent intent_login = new Intent();
                    intent_login.setClass(DetailsActivity.this,
                            LoginActivity.class);
                    intent_login.putExtra("login_type", 1);
                    mypDialog.dismiss();
                    startActivity(intent_login);
                } else if (response.code() == 200) {
                    if (response.body() != null) {
                        Log.v("provSubmit请求成功!", "response.body is not null");
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
                        Log.v("provSubmit请求成功!", "response.body is null");
                        Snackbar.make(toolbar, "提交失败!(response.body is null)请稍后再试",
                                Snackbar.LENGTH_LONG).setAction("Action", null)
                                .show();
                    }
                } else {
                    Log.v("provSubmit请求成功!", "提交失败!(" + response.code() + ")请稍后再试");
                    Snackbar.make(toolbar, "" + response.code(),
                            Snackbar.LENGTH_LONG).setAction("Action", null)
                            .show();
                }
                mypDialog.dismiss();
            }

            @Override
            public void onFailure(Call<SubmitStatus> call, Throwable t) {
                Log.v("provSubmit请求失败!", t.getMessage());
                Snackbar.make(toolbar, "请求失败!请稍后再试",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
                mypDialog.dismiss();
            }
        });
    }


    public void attemptSubmit() {
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
        //Log.v("data", data);
        FDA_API request = HttpUtils.JsonApi();
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
                Snackbar.make(toolbar, "请求失败!请稍后再试",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
                mypDialog.dismiss();
            }
        });
    }


    public void attemptGetSamplingBill(String applyNo) {
        final BuildBean dialog_loading = DialogUIUtils.showLoading(context, "正在生成抽样单...", false,
                true, false,
                false);
        dialog_loading.show();
        String bill_path = Environment.getExternalStorageDirectory() + "/FDA/Bill/" + applyNo +
                ".pdf";
        final File bill_pdf = new File(bill_path);
        if (bill_pdf.exists()) {
            Log.v("pdf", "已经存在");
            bill_pdf.delete();
            //doPrintPdf(bill_pdf);
        }
        if (NetworkUtil.isNetworkAvailable(context)) {
            FDA_API request = HttpUtils.JsonApi();
            Call<ResponseBody> call = request.getSamplingBill(token, applyNo);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 401) {
                        Log.v("getSamplingBill请求", "token过期");
                        Intent intent_login = new Intent();
                        intent_login.setClass(DetailsActivity.this, LoginActivity.class);
                        intent_login.putExtra("login_type", 1);
                        startActivity(intent_login);
                    } else if (response.code() == 200) {
                        if (response.body() != null) {
                            try {
                                // 获取文件的输出流对象
                                FileOutputStream outStream = new FileOutputStream(bill_pdf);
                                // 获取字符串对象的byte数组并写入文件流
                                outStream.write(response.body().bytes());
                                // 最后关闭文件输出流
                                outStream.close();
                                Log.v("pdf", "下载成功");
                                doPrintPdf(bill_pdf);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Log.v("ResponseBody", "FileNotFoundException");
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.v("ResponseBody", "IOException");
                            }
                        } else {
                            Log.v("getSamplingBill请求成功!", "response.body is null");
                            Snackbar.make(toolbar, "获取失败!(请求成功)",
                                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    }
                    DialogUIUtils.dismiss(dialog_loading);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.v("getSamplingBill请求失败!", t.getMessage());
                    DialogUIUtils.dismiss(dialog_loading);
                    Snackbar.make(toolbar, "获取失败!(请求失败)",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });
        } else {
            DialogUIUtils.dismiss(dialog_loading);
            Snackbar.make(toolbar, "当前无网络",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private void doPrintPdf(final File file_pdf) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (appIsInstalled(context, "com.dynamixsoftware.printershare")) {
                    Intent intent = new Intent();
                    ComponentName comp = new ComponentName("com.dynamixsoftware.printershare",
                            "com.dynamixsoftware.printershare.ActivityPrintDocuments");
                    intent.setComponent(comp);
                    intent.setAction("android.intent.action.VIEW");
                    intent.setType("application/pdf");
                    intent.setData(Uri.fromFile(file_pdf));
                    startActivity(intent);
                } else {
                    Snackbar.make(toolbar, "未找到PrinterShare软件",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    /* 安装apk*/
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File file = getAssetFileToCacheDir(context, "PrinterShare.apk");
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android" +
                            ".package-archive");
                    startActivity(intent);
                }
            }
        }, 1000); /* 延时2s执行*/
    }

    // 判断apk是否安装
    public static boolean appIsInstalled(Context context, String pageName) {
        try {
            context.getPackageManager().getPackageInfo(pageName, 0);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    // 把Asset下的apk拷贝到sdcard下 /Android/data/你的包名/cache 目录下
    public File getAssetFileToCacheDir(Context context, String fileName) {
        try {
            File cacheDir = getCacheDir(context);
            final String cachePath = cacheDir.getAbsolutePath() + File.separator + fileName;
            InputStream is = context.getAssets().open(fileName);
            File file = new File(cachePath);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i;
            while ((i = is.read(temp)) > 0) fos.write(temp, 0, i);
            fos.close();
            is.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 获取sdcard中的缓存目录
    public static File getCacheDir(Context context) {
        String APP_DIR_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Android/data/";
        File dir = new File(APP_DIR_NAME + context.getPackageName() + "/cache/");
        if (!dir.exists()) dir.mkdirs();
        return dir;
    }

    public void FormatData(int type) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Field[] fields = null;
        if (type == 1) {
            fields = task.getClass().getDeclaredFields();
        } else if (type == 2) {
            fields = oneSampleEnterprise.getClass().getDeclaredFields();
        }
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
            } else if (type == 2) {
                if (!name.equals("$change")) {
                    Method getMethod = oneSampleEnterprise.getClass().getMethod("get" + name);
                    //Log.v("getMethod", getMethod.getName().toString());
                    value = getMethod.invoke(oneSampleEnterprise);
                    if (value == null) {
                        Method setMethod = oneSampleEnterprise.getClass().getMethod("set" + name,
                                String.class);
                        setMethod.invoke(oneSampleEnterprise, "");
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
            case R.id.action_prov_submit:
                if (ClickUtil.isFastClick()) {
                    doSaveData();
                    attemptProvSubmit();
                } else {
                    Snackbar.make(toolbar, "点击太快了，请稍后再试",
                            Snackbar.LENGTH_LONG).setAction("Action", null)
                            .show();
                }
                break;
            case R.id.action_submit:
                if (ClickUtil.isFastClick()) {
                    doSaveData();
                    attemptSubmit();
                } else {
                    Snackbar.make(toolbar, "提交太快了，请稍后再试",
                            Snackbar.LENGTH_LONG).setAction("Action", null)
                            .show();
                }
                break;
            case R.id.action_uploadImg:
                Intent intent_uploadImg = new Intent();
                intent_uploadImg.setClass(DetailsActivity.this,
                        ImgUploadActivity.class);
                intent_uploadImg.putExtra("NO", tv_NO.getText().toString());
                //finish();// 结束当前活动
                startActivity(intent_uploadImg);
                break;
            case R.id.action_SamplingBill:
                attemptGetSamplingBill(task.getNO());
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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

    public void setFocus(View v) {
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        v.requestFocus();//获取焦点 光标出现
    }
}
