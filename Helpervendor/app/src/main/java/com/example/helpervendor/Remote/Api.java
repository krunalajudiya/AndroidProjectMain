package com.example.helpervendor.Remote;


import com.example.helpervendor.Model.Categorymodel;
import com.example.helpervendor.Model.Historymodel;
import com.example.helpervendor.Model.Insertresultmodel;
import com.example.helpervendor.Model.Newordermodel;
import com.example.helpervendor.Model.Ongoingmodel;
import com.example.helpervendor.Model.Resultmodel;
import com.example.helpervendor.Model.Servicechargemodel;
import com.example.helpervendor.Model.Servicemodel;
import com.example.helpervendor.Model.Usermembershipmodel;
import com.example.helpervendor.Model.Vendordetailmodel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {


    @FormUrlEncoded
    @POST("Helper.php")
    Call<Resultmodel> Login(@Field("tag") String tag, @Field("mobile") String mobile,@Field("password") String password,@Field("deviceuid") String deviceuid,@Field("devicetoken") String devicetoken);


    @Multipart
    @POST("Helper.php")
    Call<Insertresultmodel> Register_user(@Part("tag") RequestBody tag, @Part("vendor_name") RequestBody name,@Part("mobile") RequestBody mobile, @Part("password") RequestBody password, @Part MultipartBody.Part  photo, @Part("address") RequestBody address, @Part("city") RequestBody city, @Part("pincode") RequestBody pincode, @Part MultipartBody.Part id_proof,@Part("visiting_rate") RequestBody visiting_rate);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Categorymodel> Category(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Servicechargemodel> Service_charge(@Field("tag") String tag,@Field("city") String city);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Ongoingmodel> Ongoing_booking(@Field("tag") String tag, @Field("vendor_id") String vendor_id);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Historymodel> booking_history(@Field("tag") String tag, @Field("vendor_id") String vendor_id);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Newordermodel> New_Booking(@Field("tag") String tag, @Field("vendor_id") String vendor_id, @Field("city") String city);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Insertresultmodel> Confirm_Booking(@Field("tag") String tag,@Field("booking_id") String booking_id, @Field("vendor_id") String vendor_id);

    @Multipart
    @POST("Helper.php")
    Call<Insertresultmodel> Update_vendor_details(@Part("tag") RequestBody tag, @Part("v_id") RequestBody vendor_id,@Part("vendor_name") RequestBody name,@Part("mobile") RequestBody mobile, @Part MultipartBody.Part  photo, @Part("address") RequestBody address, @Part("city") RequestBody city, @Part("pincode") RequestBody pincode, @Part MultipartBody.Part id_proof,@Part("visiting_rate") RequestBody visiting_rate);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Insertresultmodel> Add_service(@Field("tag") String tag, @Field("vendor_id") String vendor_id,@Field("Sub_cat_id") String sub_cat_id);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Insertresultmodel> Reject_service(@Field("tag") String tag,@Field("b_id") String b_id, @Field("vendor_id") String vendor_id);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Insertresultmodel> Vendor_avalibality(@Field("tag") String tag,@Field("status") String b_id, @Field("vendor_id") String vendor_id);

    @Multipart
    @POST("Helper.php")
    Call<Insertresultmodel> Generate_invoice(@Part("tag") RequestBody tag, @Part("b_id") RequestBody b_id,@Part("service_charge") RequestBody service_charge,@Part("part_charge") RequestBody part_charge, @Part("sub_cat_charge") RequestBody sub_cat_charge, @Part("vendor_id") RequestBody vendor_id, @Part("customer_id") RequestBody customer_id,@Part("total_charges") RequestBody total_charges,@Part MultipartBody.Part bill_pdf,@Part("bill_date") RequestBody bill_date,@Part("discount") String discount);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Usermembershipmodel> usermembership_list(@Field("tag") String tag, @Field("client_id") String id);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Resultmodel> vendoravailable_status(@Field("tag") String tag, @Field("vendor_id") String vendor_id, @Field("status") String status);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Vendordetailmodel> Chaeckstatus(@Field("tag") String tag, @Field("vendor_id") String vendor_id);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Servicemodel> all_service(@Field("tag") String tag, @Field("vendor_id") String vendor_id);
}
