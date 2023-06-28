package com.example.shreejicabs.Remote;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IUploadAPI {
    @Multipart
    @POST("upload.php")
    Call<String> uploadFile(@Part MultipartBody.Part file);

    @Multipart
    @POST("upload1.php")
    Call<String> uploadImages(@Part List<MultipartBody.Part> images);
}
