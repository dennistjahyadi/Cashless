package com.dennistjahyadi.cashless.RetrofitServices;

import com.dennistjahyadi.cashless.Models.BarcodeBusiness;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Denn on 10/20/2017.
 */

public interface APIBusinessListService {
    @FormUrlEncoded
    @POST("businesslist.php")
    Call<String> addBusiness(
            @Field("email") String email
            , @Field("pass") String pass
            , @Field("businessName") String businessName
            , @Field("businessDesc") String businessDesc
            , @Field("amount") Integer amount);

    @GET("businesslist.php")
    Call<List<BarcodeBusiness>> fetchAllBusinessListByUserId(@Query("userId") String userId, @Query("fetchAllBusinessListByUserId") Integer offset);

    @GET("businesslist.php")
    Call<BarcodeBusiness> getBusinessInfoById(@Query("businessId") String businessId, @Query("getBusinessInfoById") String getBusinessInfoById);

    @FormUrlEncoded
    @POST("businesslist.php")
    Call<String> deleteBusinessById(
            @Field("businessId") String businessId
            , @Field("deleteBusinessById") String deleteBusinessById
    );

}
