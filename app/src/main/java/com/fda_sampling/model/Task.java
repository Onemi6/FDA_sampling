
package com.fda_sampling.model;

public class Task implements Cloneable {

    private int isSelect;
    private int ID;

    private String NO;

    private String GOODS_NAME;

    private String CLIENT;

    private String CLIENT_PERSON;

    private String CLIENT_ADDR;

    private String BUSINESS_SOURCE;

    private String SAMPLING_NOTICE_CODE;

    private String FOOD_BUSINESS_PERMIT;

    private String SAMPLE_TYPE;

    private String SUPPLIER;

    private String DOMESTIC_AREA;

    private String DOMESTIC_AREA_OTHER;

    private String SUPPLIER_ADDR;

    private String PROVINCE;

    private String CITY;

    private String DISTRICT;

    private String TOWN;

    private String SUPPLIER_ADDR_TXT;

    private String SUPPLIER_LEGAL;

    private String ANNUAL_SALES;

    private String BUSINESS_LICENCE;

    private String SUPPLIER_PERSON;

    private String SUPPLIER_PHONE;

    private String SUPPLIER_FAX;

    private String SUPPLIER_ZIPCODE;

    private String DRAW_ADDR;

    private String DRAW_ADDR_OTHER;

    private String SAMPLE_SOURCE;

    private String SAMPLE_SOURCE_OTHER;

    private String SAMPLE_PROPERTY;

    private String SAMPLE_PROPERTY_OTHER;

    private String SAMPLE_STYLE;

    private String SAMPLE_STYLE_OTHER;

    private String TRADEMARK;

    private String DATE_PRODUCT_TYPE;

    private String DATE_PRODUCT;

    private String SAMPLE_MODEL;

    private String SAMPLE_NUMBER;

    private String EXPIRATIONDATE;

    private String TEST_FILE_NO;

    private String SAMPLE_CLASS;

    private String PRODUCTION_CERTIFICATE;

    private String UNIVALENT;

    private String UNIVALENT_UNIT;

    private String I_AND_O;

    private String DRAW_NUM;

    private String DRAW_AMOUNT;

    private String DRAW_AMOUNT_UNIT;

    private String STORAGESITE;

    private String SAMPLE_STATUS;

    private String PACK_TYPE;

    private String MANU_COMPANY;

    private String MANU_COMPANY_PHONE;

    private String MANU_COMPANY_ADDR;

    private String MANU_PROVINCE;

    private String MANU_CITY;

    private String MANU_DISTRICT;

    private String MANU_TOWN;

    private String MANU_COMPANY_ADDR_TXT;

    private String SAVE_MODE;

    private String SAVE_MODE_OTHER;

    private String SAMPLE_CLOSE_DATE;

    private String SAMPLE_ADDR;

    private String PACK;

    private String PACK_OTHER;

    private String DRAW_METHOD;

    private String DRAW_ORG;

    private String DRAW_ORG_ADDR;

    private String DRAW_PERSON;

    private String DRAW_PHONE;

    private String DRAW_FAX;

    private String DRAW_ZIPCODE;

    private String CHECKSEALED;

    private String REMARK;

    private String DRAW_DATE;

    private String GOODS_TYPE;

    private String DRAW_MAN;

    private String DRAW_MAN_NO;

    private String GOODS_TYPE_NO;

    private String PERMIT_NUM;

    private String PERMIT_TYPE;

    private String CUSTOM_NO;

    private String APPLY_KIND_NO;

    private String LAB_NO;

    private String RECORDER;

    private String FOOD_KIND1;

    private String FOOD_KIND2;

    private String FOOD_KIND3;

    private String FOOD_KIND4;

    private String CHILD_FOOD_KIND_ID;

    private int PLAN_EXEC_ID;

    private String STATE;

    private String CHECK_INFO;

    private String SAMPLE_MARK;

    private String SAMPLE_AMOUNT;

    private String BARCODE;

    private String ORIGIN_COUNTRY;

    private String THIRD_NAME;

    private String THIRD_ADDR;

    private String THIRD_NATURE;

    private String THIRD_NATURE_OTHER;

    private String THIRD_CODE;

    private String THIRD_PHONE;


    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

    public void setNO(String NO) {
        this.NO = NO;
    }

    public String getNO() {
        return this.NO;
    }

    public void setGOODS_NAME(String GOODS_NAME) {
        this.GOODS_NAME = GOODS_NAME;
    }

    public String getGOODS_NAME() {
        return this.GOODS_NAME;
    }

    public void setCLIENT(String CLIENT) {
        this.CLIENT = CLIENT;
    }

    public String getCLIENT() {
        return this.CLIENT;
    }

    public void setCLIENT_PERSON(String CLIENT_PERSON) {
        this.CLIENT_PERSON = CLIENT_PERSON;
    }

    public String getCLIENT_PERSON() {
        return this.CLIENT_PERSON;
    }

    public void setCLIENT_ADDR(String CLIENT_ADDR) {
        this.CLIENT_ADDR = CLIENT_ADDR;
    }

    public String getCLIENT_ADDR() {
        return this.CLIENT_ADDR;
    }

    public void setBUSINESS_SOURCE(String BUSINESS_SOURCE) {
        this.BUSINESS_SOURCE = BUSINESS_SOURCE;
    }

    public String getBUSINESS_SOURCE() {
        return this.BUSINESS_SOURCE;
    }

    public void setSAMPLING_NOTICE_CODE(String SAMPLING_NOTICE_CODE) {
        this.SAMPLING_NOTICE_CODE = SAMPLING_NOTICE_CODE;
    }

    public String getSAMPLING_NOTICE_CODE() {
        return this.SAMPLING_NOTICE_CODE;
    }

    public void setFOOD_BUSINESS_PERMIT(String FOOD_BUSINESS_PERMIT) {
        this.FOOD_BUSINESS_PERMIT = FOOD_BUSINESS_PERMIT;
    }

    public String getFOOD_BUSINESS_PERMIT() {
        return this.FOOD_BUSINESS_PERMIT;
    }

    public void setSAMPLE_TYPE(String SAMPLE_TYPE) {
        this.SAMPLE_TYPE = SAMPLE_TYPE;
    }

    public String getSAMPLE_TYPE() {
        return this.SAMPLE_TYPE;
    }

    public void setSUPPLIER(String SUPPLIER) {
        this.SUPPLIER = SUPPLIER;
    }

    public String getSUPPLIER() {
        return this.SUPPLIER;
    }

    public void setDOMESTIC_AREA(String DOMESTIC_AREA) {
        this.DOMESTIC_AREA = DOMESTIC_AREA;
    }

    public String getDOMESTIC_AREA() {
        return this.DOMESTIC_AREA;
    }

    public void setDOMESTIC_AREA_OTHER(String DOMESTIC_AREA_OTHER) {
        this.DOMESTIC_AREA_OTHER = DOMESTIC_AREA_OTHER;
    }

    public String getDOMESTIC_AREA_OTHER() {
        return this.DOMESTIC_AREA_OTHER;
    }

    public void setSUPPLIER_ADDR(String SUPPLIER_ADDR) {
        this.SUPPLIER_ADDR = SUPPLIER_ADDR;
    }

    public String getSUPPLIER_ADDR() {
        return this.SUPPLIER_ADDR;
    }

    public void setPROVINCE(String PROVINCE) {
        this.PROVINCE = PROVINCE;
    }

    public String getPROVINCE() {
        return this.PROVINCE;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }

    public String getCITY() {
        return this.CITY;
    }

    public void setDISTRICT(String DISTRICT) {
        this.DISTRICT = DISTRICT;
    }

    public String getDISTRICT() {
        return this.DISTRICT;
    }

    public void setTOWN(String TOWN) {
        this.TOWN = TOWN;
    }

    public String getTOWN() {
        return this.TOWN;
    }

    public void setSUPPLIER_ADDR_TXT(String SUPPLIER_ADDR_TXT) {
        this.SUPPLIER_ADDR_TXT = SUPPLIER_ADDR_TXT;
    }

    public String getSUPPLIER_ADDR_TXT() {
        return this.SUPPLIER_ADDR_TXT;
    }

    public void setSUPPLIER_LEGAL(String SUPPLIER_LEGAL) {
        this.SUPPLIER_LEGAL = SUPPLIER_LEGAL;
    }

    public String getSUPPLIER_LEGAL() {
        return this.SUPPLIER_LEGAL;
    }

    public void setANNUAL_SALES(String ANNUAL_SALES) {
        this.ANNUAL_SALES = ANNUAL_SALES;
    }

    public String getANNUAL_SALES() {
        return this.ANNUAL_SALES;
    }

    public void setBUSINESS_LICENCE(String BUSINESS_LICENCE) {
        this.BUSINESS_LICENCE = BUSINESS_LICENCE;
    }

    public String getBUSINESS_LICENCE() {
        return this.BUSINESS_LICENCE;
    }

    public void setSUPPLIER_PERSON(String SUPPLIER_PERSON) {
        this.SUPPLIER_PERSON = SUPPLIER_PERSON;
    }

    public String getSUPPLIER_PERSON() {
        return this.SUPPLIER_PERSON;
    }

    public void setSUPPLIER_PHONE(String SUPPLIER_PHONE) {
        this.SUPPLIER_PHONE = SUPPLIER_PHONE;
    }

    public String getSUPPLIER_PHONE() {
        return this.SUPPLIER_PHONE;
    }

    public void setSUPPLIER_FAX(String SUPPLIER_FAX) {
        this.SUPPLIER_FAX = SUPPLIER_FAX;
    }

    public String getSUPPLIER_FAX() {
        return this.SUPPLIER_FAX;
    }

    public void setSUPPLIER_ZIPCODE(String SUPPLIER_ZIPCODE) {
        this.SUPPLIER_ZIPCODE = SUPPLIER_ZIPCODE;
    }

    public String getSUPPLIER_ZIPCODE() {
        return this.SUPPLIER_ZIPCODE;
    }

    public void setDRAW_ADDR(String DRAW_ADDR) {
        this.DRAW_ADDR = DRAW_ADDR;
    }

    public String getDRAW_ADDR() {
        return this.DRAW_ADDR;
    }

    public void setDRAW_ADDR_OTHER(String DRAW_ADDR_OTHER) {
        this.DRAW_ADDR_OTHER = DRAW_ADDR_OTHER;
    }

    public String getDRAW_ADDR_OTHER() {
        return this.DRAW_ADDR_OTHER;
    }

    public void setSAMPLE_SOURCE(String SAMPLE_SOURCE) {
        this.SAMPLE_SOURCE = SAMPLE_SOURCE;
    }

    public String getSAMPLE_SOURCE() {
        return this.SAMPLE_SOURCE;
    }

    public void setSAMPLE_SOURCE_OTHER(String SAMPLE_SOURCE_OTHER) {
        this.SAMPLE_SOURCE_OTHER = SAMPLE_SOURCE_OTHER;
    }

    public String getSAMPLE_SOURCE_OTHER() {
        return this.SAMPLE_SOURCE_OTHER;
    }

    public void setSAMPLE_PROPERTY(String SAMPLE_PROPERTY) {
        this.SAMPLE_PROPERTY = SAMPLE_PROPERTY;
    }

    public String getSAMPLE_PROPERTY() {
        return this.SAMPLE_PROPERTY;
    }

    public void setSAMPLE_PROPERTY_OTHER(String SAMPLE_PROPERTY_OTHER) {
        this.SAMPLE_PROPERTY_OTHER = SAMPLE_PROPERTY_OTHER;
    }

    public String getSAMPLE_PROPERTY_OTHER() {
        return this.SAMPLE_PROPERTY_OTHER;
    }

    public void setSAMPLE_STYLE(String SAMPLE_STYLE) {
        this.SAMPLE_STYLE = SAMPLE_STYLE;
    }

    public String getSAMPLE_STYLE() {
        return this.SAMPLE_STYLE;
    }

    public void setSAMPLE_STYLE_OTHER(String SAMPLE_STYLE_OTHER) {
        this.SAMPLE_STYLE_OTHER = SAMPLE_STYLE_OTHER;
    }

    public String getSAMPLE_STYLE_OTHER() {
        return this.SAMPLE_STYLE_OTHER;
    }

    public void setTRADEMARK(String TRADEMARK) {
        this.TRADEMARK = TRADEMARK;
    }

    public String getTRADEMARK() {
        return this.TRADEMARK;
    }

    public void setDATE_PRODUCT_TYPE(String DATE_PRODUCT_TYPE) {
        this.DATE_PRODUCT_TYPE = DATE_PRODUCT_TYPE;
    }

    public String getDATE_PRODUCT_TYPE() {
        return this.DATE_PRODUCT_TYPE;
    }

    public void setDATE_PRODUCT(String DATE_PRODUCT) {
        this.DATE_PRODUCT = DATE_PRODUCT;
    }

    public String getDATE_PRODUCT() {
        return this.DATE_PRODUCT;
    }

    public void setSAMPLE_MODEL(String SAMPLE_MODEL) {
        this.SAMPLE_MODEL = SAMPLE_MODEL;
    }

    public String getSAMPLE_MODEL() {
        return this.SAMPLE_MODEL;
    }

    public void setSAMPLE_NUMBER(String SAMPLE_NUMBER) {
        this.SAMPLE_NUMBER = SAMPLE_NUMBER;
    }

    public String getSAMPLE_NUMBER() {
        return this.SAMPLE_NUMBER;
    }

    public void setEXPIRATIONDATE(String EXPIRATIONDATE) {
        this.EXPIRATIONDATE = EXPIRATIONDATE;
    }

    public String getEXPIRATIONDATE() {
        return this.EXPIRATIONDATE;
    }

    public void setTEST_FILE_NO(String TEST_FILE_NO) {
        this.TEST_FILE_NO = TEST_FILE_NO;
    }

    public String getTEST_FILE_NO() {
        return this.TEST_FILE_NO;
    }

    public void setSAMPLE_CLASS(String SAMPLE_CLASS) {
        this.SAMPLE_CLASS = SAMPLE_CLASS;
    }

    public String getSAMPLE_CLASS() {
        return this.SAMPLE_CLASS;
    }

    public void setPRODUCTION_CERTIFICATE(String PRODUCTION_CERTIFICATE) {
        this.PRODUCTION_CERTIFICATE = PRODUCTION_CERTIFICATE;
    }

    public String getPRODUCTION_CERTIFICATE() {
        return this.PRODUCTION_CERTIFICATE;
    }

    public void setUNIVALENT(String UNIVALENT) {
        this.UNIVALENT = UNIVALENT;
    }

    public String getUNIVALENT() {
        return this.UNIVALENT;
    }

    public void setUNIVALENT_UNIT(String UNIVALENT_UNIT) {
        this.UNIVALENT_UNIT = UNIVALENT_UNIT;
    }

    public String getUNIVALENT_UNIT() {
        return this.UNIVALENT_UNIT;
    }

    public void setI_AND_O(String I_AND_O) {
        this.I_AND_O = I_AND_O;
    }

    public String getI_AND_O() {
        return this.I_AND_O;
    }

    public void setDRAW_NUM(String DRAW_NUM) {
        this.DRAW_NUM = DRAW_NUM;
    }

    public String getDRAW_NUM() {
        return this.DRAW_NUM;
    }

    public void setDRAW_AMOUNT(String DRAW_AMOUNT) {
        this.DRAW_AMOUNT = DRAW_AMOUNT;
    }

    public String getDRAW_AMOUNT() {
        return this.DRAW_AMOUNT;
    }

    public void setDRAW_AMOUNT_UNIT(String DRAW_AMOUNT_UNIT) {
        this.DRAW_AMOUNT_UNIT = DRAW_AMOUNT_UNIT;
    }

    public String getDRAW_AMOUNT_UNIT() {
        return this.DRAW_AMOUNT_UNIT;
    }

    public void setSTORAGESITE(String STORAGESITE) {
        this.STORAGESITE = STORAGESITE;
    }

    public String getSTORAGESITE() {
        return this.STORAGESITE;
    }

    public void setSAMPLE_STATUS(String SAMPLE_STATUS) {
        this.SAMPLE_STATUS = SAMPLE_STATUS;
    }

    public String getSAMPLE_STATUS() {
        return this.SAMPLE_STATUS;
    }

    public void setPACK_TYPE(String PACK_TYPE) {
        this.PACK_TYPE = PACK_TYPE;
    }

    public String getPACK_TYPE() {
        return this.PACK_TYPE;
    }

    public void setMANU_COMPANY(String MANU_COMPANY) {
        this.MANU_COMPANY = MANU_COMPANY;
    }

    public String getMANU_COMPANY() {
        return this.MANU_COMPANY;
    }

    public void setMANU_COMPANY_PHONE(String MANU_COMPANY_PHONE) {
        this.MANU_COMPANY_PHONE = MANU_COMPANY_PHONE;
    }

    public String getMANU_COMPANY_PHONE() {
        return this.MANU_COMPANY_PHONE;
    }

    public void setMANU_COMPANY_ADDR(String MANU_COMPANY_ADDR) {
        this.MANU_COMPANY_ADDR = MANU_COMPANY_ADDR;
    }

    public String getMANU_COMPANY_ADDR() {
        return this.MANU_COMPANY_ADDR;
    }

    public void setMANU_PROVINCE(String MANU_PROVINCE) {
        this.MANU_PROVINCE = MANU_PROVINCE;
    }

    public String getMANU_PROVINCE() {
        return this.MANU_PROVINCE;
    }

    public void setMANU_CITY(String MANU_CITY) {
        this.MANU_CITY = MANU_CITY;
    }

    public String getMANU_CITY() {
        return this.MANU_CITY;
    }

    public void setMANU_DISTRICT(String MANU_DISTRICT) {
        this.MANU_DISTRICT = MANU_DISTRICT;
    }

    public String getMANU_DISTRICT() {
        return this.MANU_DISTRICT;
    }

    public void setMANU_TOWN(String MANU_TOWN) {
        this.MANU_TOWN = MANU_TOWN;
    }

    public String getMANU_TOWN() {
        return this.MANU_TOWN;
    }

    public void setMANU_COMPANY_ADDR_TXT(String MANU_COMPANY_ADDR_TXT) {
        this.MANU_COMPANY_ADDR_TXT = MANU_COMPANY_ADDR_TXT;
    }

    public String getMANU_COMPANY_ADDR_TXT() {
        return this.MANU_COMPANY_ADDR_TXT;
    }

    public void setSAVE_MODE(String SAVE_MODE) {
        this.SAVE_MODE = SAVE_MODE;
    }

    public String getSAVE_MODE() {
        return this.SAVE_MODE;
    }

    public void setSAVE_MODE_OTHER(String SAVE_MODE_OTHER) {
        this.SAVE_MODE_OTHER = SAVE_MODE_OTHER;
    }

    public String getSAVE_MODE_OTHER() {
        return this.SAVE_MODE_OTHER;
    }

    public void setSAMPLE_CLOSE_DATE(String SAMPLE_CLOSE_DATE) {
        this.SAMPLE_CLOSE_DATE = SAMPLE_CLOSE_DATE;
    }

    public String getSAMPLE_CLOSE_DATE() {
        return this.SAMPLE_CLOSE_DATE;
    }

    public void setSAMPLE_ADDR(String SAMPLE_ADDR) {
        this.SAMPLE_ADDR = SAMPLE_ADDR;
    }

    public String getSAMPLE_ADDR() {
        return this.SAMPLE_ADDR;
    }

    public void setPACK(String PACK) {
        this.PACK = PACK;
    }

    public String getPACK() {
        return this.PACK;
    }

    public void setPACK_OTHER(String PACK_OTHER) {
        this.PACK_OTHER = PACK_OTHER;
    }

    public String getPACK_OTHER() {
        return this.PACK_OTHER;
    }

    public void setDRAW_METHOD(String DRAW_METHOD) {
        this.DRAW_METHOD = DRAW_METHOD;
    }

    public String getDRAW_METHOD() {
        return this.DRAW_METHOD;
    }

    public void setDRAW_ORG(String DRAW_ORG) {
        this.DRAW_ORG = DRAW_ORG;
    }

    public String getDRAW_ORG() {
        return this.DRAW_ORG;
    }

    public void setDRAW_ORG_ADDR(String DRAW_ORG_ADDR) {
        this.DRAW_ORG_ADDR = DRAW_ORG_ADDR;
    }

    public String getDRAW_ORG_ADDR() {
        return this.DRAW_ORG_ADDR;
    }

    public void setDRAW_PERSON(String DRAW_PERSON) {
        this.DRAW_PERSON = DRAW_PERSON;
    }

    public String getDRAW_PERSON() {
        return this.DRAW_PERSON;
    }

    public void setDRAW_PHONE(String DRAW_PHONE) {
        this.DRAW_PHONE = DRAW_PHONE;
    }

    public String getDRAW_PHONE() {
        return this.DRAW_PHONE;
    }

    public void setDRAW_FAX(String DRAW_FAX) {
        this.DRAW_FAX = DRAW_FAX;
    }

    public String getDRAW_FAX() {
        return this.DRAW_FAX;
    }

    public void setDRAW_ZIPCODE(String DRAW_ZIPCODE) {
        this.DRAW_ZIPCODE = DRAW_ZIPCODE;
    }

    public String getDRAW_ZIPCODE() {
        return this.DRAW_ZIPCODE;
    }

    public void setCHECKSEALED(String CHECKSEALED) {
        this.CHECKSEALED = CHECKSEALED;
    }

    public String getCHECKSEALED() {
        return this.CHECKSEALED;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getREMARK() {
        return this.REMARK;
    }

    public void setDRAW_DATE(String DRAW_DATE) {
        this.DRAW_DATE = DRAW_DATE;
    }

    public String getDRAW_DATE() {
        return this.DRAW_DATE;
    }

    public void setGOODS_TYPE(String GOODS_TYPE) {
        this.GOODS_TYPE = GOODS_TYPE;
    }

    public String getGOODS_TYPE() {
        return this.GOODS_TYPE;
    }

    public void setDRAW_MAN(String DRAW_MAN) {
        this.DRAW_MAN = DRAW_MAN;
    }

    public String getDRAW_MAN() {
        return this.DRAW_MAN;
    }

    public void setDRAW_MAN_NO(String DRAW_MAN_NO) {
        this.DRAW_MAN_NO = DRAW_MAN_NO;
    }

    public String getDRAW_MAN_NO() {
        return this.DRAW_MAN_NO;
    }

    public void setGOODS_TYPE_NO(String GOODS_TYPE_NO) {
        this.GOODS_TYPE_NO = GOODS_TYPE_NO;
    }

    public String getGOODS_TYPE_NO() {
        return this.GOODS_TYPE_NO;
    }

    public void setPERMIT_NUM(String PERMIT_NUM) {
        this.PERMIT_NUM = PERMIT_NUM;
    }

    public String getPERMIT_NUM() {
        return this.PERMIT_NUM;
    }

    public void setPERMIT_TYPE(String PERMIT_TYPE) {
        this.PERMIT_TYPE = PERMIT_TYPE;
    }

    public String getPERMIT_TYPE() {
        return this.PERMIT_TYPE;
    }

    public void setCUSTOM_NO(String CUSTOM_NO) {
        this.CUSTOM_NO = CUSTOM_NO;
    }

    public String getCUSTOM_NO() {
        return this.CUSTOM_NO;
    }

    public void setAPPLY_KIND_NO(String APPLY_KIND_NO) {
        this.APPLY_KIND_NO = APPLY_KIND_NO;
    }

    public String getAPPLY_KIND_NO() {
        return this.APPLY_KIND_NO;
    }

    public void setLAB_NO(String LAB_NO) {
        this.LAB_NO = LAB_NO;
    }

    public String getLAB_NO() {
        return this.LAB_NO;
    }

    public void setRECORDER(String RECORDER) {
        this.RECORDER = RECORDER;
    }

    public String getRECORDER() {
        return this.RECORDER;
    }

    public void setFOOD_KIND1(String FOOD_KIND1) {
        this.FOOD_KIND1 = FOOD_KIND1;
    }

    public String getFOOD_KIND1() {
        return this.FOOD_KIND1;
    }

    public void setFOOD_KIND2(String FOOD_KIND2) {
        this.FOOD_KIND2 = FOOD_KIND2;
    }

    public String getFOOD_KIND2() {
        return this.FOOD_KIND2;
    }

    public void setFOOD_KIND3(String FOOD_KIND3) {
        this.FOOD_KIND3 = FOOD_KIND3;
    }

    public String getFOOD_KIND3() {
        return this.FOOD_KIND3;
    }

    public void setFOOD_KIND4(String FOOD_KIND4) {
        this.FOOD_KIND4 = FOOD_KIND4;
    }

    public String getFOOD_KIND4() {
        return this.FOOD_KIND4;
    }

    public void setCHILD_FOOD_KIND_ID(String CHILD_FOOD_KIND_ID) {
        this.CHILD_FOOD_KIND_ID = CHILD_FOOD_KIND_ID;
    }

    public String getCHILD_FOOD_KIND_ID() {
        return this.CHILD_FOOD_KIND_ID;
    }

    public void setPLAN_EXEC_ID(int PLAN_EXEC_ID) {
        this.PLAN_EXEC_ID = PLAN_EXEC_ID;
    }

    public int getPLAN_EXEC_ID() {
        return this.PLAN_EXEC_ID;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getSTATE() {
        return this.STATE;
    }

    public void setCHECK_INFO(String CHECK_INFO) {
        this.CHECK_INFO = CHECK_INFO;
    }

    public String getCHECK_INFO() {
        return this.CHECK_INFO;
    }

    public void setSAMPLE_MARK(String SAMPLE_MARK) {
        this.SAMPLE_MARK = SAMPLE_MARK;
    }

    public String getSAMPLE_MARK() {
        return this.SAMPLE_MARK;
    }

    public void setSAMPLE_AMOUNT(String SAMPLE_AMOUNT) {
        this.SAMPLE_AMOUNT = SAMPLE_AMOUNT;
    }

    public String getSAMPLE_AMOUNT() {
        return this.SAMPLE_AMOUNT;
    }

    public void setBARCODE(String BARCODE) {
        this.BARCODE = BARCODE;
    }

    public String getBARCODE() {
        return this.BARCODE;
    }

    public void setORIGIN_COUNTRY(String ORIGIN_COUNTRY) {
        this.ORIGIN_COUNTRY = ORIGIN_COUNTRY;
    }

    public String getORIGIN_COUNTRY() {
        return this.ORIGIN_COUNTRY;
    }

    public void setTHIRD_NAME(String THIRD_NAME) {
        this.THIRD_NAME = THIRD_NAME;
    }

    public String getTHIRD_NAME() {
        return this.THIRD_NAME;
    }

    public void setTHIRD_ADDR(String THIRD_ADDR) {
        this.THIRD_ADDR = THIRD_ADDR;
    }

    public String getTHIRD_ADDR() {
        return this.THIRD_ADDR;
    }

    public void setTHIRD_NATURE(String THIRD_NATURE) {
        this.THIRD_NATURE = THIRD_NATURE;
    }

    public String getTHIRD_NATURE() {
        return this.THIRD_NATURE;
    }

    public void setTHIRD_NATURE_OTHER(String THIRD_NATURE_OTHER) {
        this.THIRD_NATURE_OTHER = THIRD_NATURE_OTHER;
    }

    public String getTHIRD_NATURE_OTHER() {
        return this.THIRD_NATURE_OTHER;
    }

    public void setTHIRD_CODE(String THIRD_CODE) {
        this.THIRD_CODE = THIRD_CODE;
    }

    public String getTHIRD_CODE() {
        return this.THIRD_CODE;
    }

    public void setTHIRD_PHONE(String THIRD_PHONE) {
        this.THIRD_PHONE = THIRD_PHONE;
    }

    public String getTHIRD_PHONE() {
        return this.THIRD_PHONE;
    }

    @Override
    public Object clone() {
        Task task = null;
        try {
            task = (Task) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return task;
    }
}