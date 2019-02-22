package com.fda_sampling.service;

import com.fda_sampling.model.ImageInfo;
import com.fda_sampling.model.LoginStatus;
import com.fda_sampling.model.SubmitStatus;
import com.fda_sampling.model.Task;
import com.fda_sampling.model.UpdateInfo;
import com.fda_sampling.model.UploadImg;

import java.util.List;

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

public interface FDA_API {

    @GET("api/Apply/getTasks")
    Call<List<Task>> getTasks(@Header("LIMS_Token") String LIMS_Token, @Query("Emp_No") String
            Emp_No);

    @GET("update.xml")
    Call<UpdateInfo> UpdateXML();

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
}
