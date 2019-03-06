package com.fda_sampling.service;

import com.fda_sampling.model.ChildFoodKind;
import com.fda_sampling.model.FoodKind;
import com.fda_sampling.model.ImageInfo;
import com.fda_sampling.model.LoginStatus;
import com.fda_sampling.model.SubmitStatus;
import com.fda_sampling.model.Task;
import com.fda_sampling.model.Unit;
import com.fda_sampling.model.UpdateInfo;
import com.fda_sampling.model.UploadImg;
import com.fda_sampling.model.sampleEnterprise;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface FDA_API {

    @GET("api/Apply/getTasks")
    Call<List<Task>> getTasks(@Header("LIMS_Token") String LIMS_Token, @Query("Emp_No") String
            Emp_No);

    @GET("update.xml")
    Call<UpdateInfo> UpdateXML();

    @GET("ReportServer")
    Call<ResponseBody> ReportServer(@QueryMap Map<String, String> params);

    @POST("api/Login")
    Call<LoginStatus> Login(@Query("data") String data);

    @POST("api/Apply/Submit")
    Call<SubmitStatus> Submit(@Header("LIMS_Token") String LIMS_Token, @Query("data") String data);

    @Multipart
    @POST("api/Apply/ImageUpload")
    Call<UploadImg> ImageUpload(@Header("LIMS_Token") String LIMS_Token, @Part("id") RequestBody
            id, @Part("type") RequestBody type, @Part
                                        MultipartBody.Part file);

    @POST("api/Apply/ImageInfo")
    Call<List<ImageInfo>> ImageInfo(@Header("LIMS_Token") String LIMS_Token, @Query("Apply_No")
            String Apply_No);

    @POST("api/Apply/Image")
    Call<ResponseBody> Image(@Header("LIMS_Token") String LIMS_Token, @Query("id") int id);

    @POST("api/Apply/sampleEnterprises")
    Call<List<sampleEnterprise>> sampleEnterprises(@Header("LIMS_Token") String LIMS_Token,
                                                   @Query("Key") String Key);

    @POST("api/Apply/allSampleEnterprises")
    Call<List<sampleEnterprise>> allSampleEnterprises(@Header("LIMS_Token") String LIMS_Token);

    @POST("api/Apply/getUnit")
    Call<List<Unit>> getUnit(@Header("LIMS_Token") String LIMS_Token, @Query("Unit_Type") String
            Unit_Type);

    @POST("api/Apply/getFoodKind")
    Call<List<FoodKind>> getFoodKind(@Header("LIMS_Token") String LIMS_Token, @Query
            ("Food_Kind_Type") String Food_Kind_Type, @Query("Parent_Food_Kind_Name") String
                                             Parent_Food_Kind_Name);

    @POST("api/Apply/getChildFoodKind")
    Call<List<ChildFoodKind>> getChildFoodKind(@Header("LIMS_Token") String LIMS_Token, @Query
            ("PFK_ID") int PFK_ID);

}
