package com.example.helperfactory.Remote;

import com.example.helperfactory.Activity.Subcategory;
import com.example.helperfactory.Model.Categorymodel;
import com.example.helperfactory.Model.Couponmodel;
import com.example.helperfactory.Model.Generatetokenmodel;
import com.example.helperfactory.Model.Historymodel;
import com.example.helperfactory.Model.Insertresultmodel;
import com.example.helperfactory.Model.Membershipmodel;
import com.example.helperfactory.Model.Offermodel;
import com.example.helperfactory.Model.Ongoingmodel;
import com.example.helperfactory.Model.Refermodel;
import com.example.helperfactory.Model.Resultmodel;
import com.example.helperfactory.Model.Reviewmodel;
import com.example.helperfactory.Model.Slidermodel;
import com.example.helperfactory.Model.Subcategorymodel;
import com.example.helperfactory.Model.Usermembershipmodel;

import java.util.List;

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
    Call<Slidermodel> Slider(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Categorymodel> Category(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Resultmodel> Register(@Field("tag") String tag, @Field("mobile") String mobile,@Field("deviceuid") String deviceuid,@Field("devicetoken") String devicetoken,@Field("referral_code") String referral_code,@Field("city") String city);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Subcategorymodel> Subcategory(@Field("tag") String tag, @Field("Cat_id") String cat_id);

    @Multipart
    @POST("Helper.php")
    Call<Insertresultmodel> Servicebooking(@Part("tag") RequestBody tag, @Part("Client_id") RequestBody client_id, @Part("Category_id") RequestBody cat_id, @Part("Sub_cat_id") RequestBody sub_cat_id, @Part("date") RequestBody date, @Part("time") RequestBody time,@Part MultipartBody.Part images,@Part("coupon_code") RequestBody coupon_code);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Insertresultmodel> UpdateProfile(@Field("tag") String tag, @Field("Client_id") String client_id, @Field("Client_name") String client_name, @Field("Photo") String photo, @Field("Email") String email, @Field("Address") String address,@Field("City") String city,@Field("Pincode") String pincode);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Ongoingmodel> Ongoing_detail(@Field("tag") String tag, @Field("Client_id") String client_id);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Historymodel> Booking_history(@Field("tag") String tag, @Field("Client_id") String client_id);

    @Multipart
    @POST("Helper.php")
    Call<Insertresultmodel> Update_Profile_detail(@Part("tag") RequestBody tag, @Part("Client_id") RequestBody client_id, @Part("Client_name") RequestBody client_name, @Part MultipartBody.Part images, @Part("Address") RequestBody address, @Part("City") RequestBody city, @Part("Pincode") RequestBody pincode);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Insertresultmodel> CancelBooking(@Field("tag") String tag, @Field("Appointment_id") String appointment_id);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Insertresultmodel> Add_membership(@Field("tag") String tag, @Field("u_id") String u_id,@Field("m_id") String m_id,@Field("from_date") String from_date,@Field("to_date") String to_date,@Field("txnid") String txnid,@Field("amount") String amount);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Membershipmodel> Membership_list(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Usermembershipmodel> usermembership_list(@Field("tag") String tag, @Field("client_id") String id);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Offermodel> Offer_list(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("sample.php")
    Call<Generatetokenmodel> generateTokenCall(@Field("mid") String mid, @Field("orderid") String order_id, @Field("amount") String amount, @Field("customer_id") String customer_id);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Insertresultmodel> Pay_Booking(@Field("tag") String tag, @Field("u_id") String u_id, @Field("b_id") String b_id, @Field("type") String type,@Field("txnid") String txnid);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Insertresultmodel> Add_Review(@Field("tag") String tag,@Field("s_id") String s_id,@Field("user_id") String user_id,@Field("msg") String msg,@Field("rating") String rating);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Reviewmodel> Fetch_Review(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Refermodel> Refer(@Field("tag") String tag, @Field("referral_code") String referral_code);

    @FormUrlEncoded
    @POST("Helper.php")
    Call<Couponmodel> Coupon(@Field("tag") String tag, @Field("client_id") String client_id);

}
