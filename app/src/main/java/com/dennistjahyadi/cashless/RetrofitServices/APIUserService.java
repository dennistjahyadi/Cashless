package com.dennistjahyadi.cashless.RetrofitServices;

import com.dennistjahyadi.cashless.Models.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Denn on 7/13/2017.
 */

public interface APIUserService {
    @FormUrlEncoded
    @POST("users.php")
    Call<String> registerClient(
            @Field("email") String email
            , @Field("fullname") String fullname
            , @Field("pass") String pass);

    @FormUrlEncoded
    @POST("users.php")
    Call<String> doLogin(@Field("email") String email, @Field("pass") String pass, @Field("login") String login);

    @FormUrlEncoded
    @POST("users.php")
    Call<String> doVerification(@Field("email") String email, @Field("pass") String pass, @Field("code") String code);

    @FormUrlEncoded
    @POST("users.php")
    Call<String> resendCodeVerification(@Field("email") String email, @Field("pass") String pass, @Field("resendCodeVerification") String resendCodeVerification);

    @FormUrlEncoded
    @POST("users.php")
    Call<User> getUserInfo(@Field("email") String email, @Field("pass") String pass, @Field("getUserInfo") String getUserInfo);

    @FormUrlEncoded
    @POST("users.php")
    Call<String> updateUserProfile(@Field("email") String email, @Field("pass") String pass, @Field("columnUpdated") String columnUpdated, @Field("updatedData") String updatedData);

    @GET("users.php")
    Call<User> getReceiverInfoById(@Query("userId") String userId, @Query("getReceiverInfoById") String getReceiverInfoById);

}
