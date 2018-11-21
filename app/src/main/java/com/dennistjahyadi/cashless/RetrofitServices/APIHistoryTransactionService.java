package com.dennistjahyadi.cashless.RetrofitServices;

import com.dennistjahyadi.cashless.Models.HistoryTransaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Denn on 10/11/2017.
 */

public interface APIHistoryTransactionService {

    @FormUrlEncoded
    @POST("historytransaction.php")
    Call<List<HistoryTransaction>> fetchAllHistoryTransaction(
            @Field("userId") String userId
            , @Field("offset") Integer offset
            , @Field("fetchAllHistoryTransaction") String fetchAllHistoryTransaction);

    @FormUrlEncoded
    @POST("historytransaction.php")
    Call<List<HistoryTransaction>> fetchInHistoryTransaction(
            @Field("userId") String userId
            , @Field("offset") Integer offset
            , @Field("fetchInHistoryTransaction") String fetchInHistoryTransaction);

    @FormUrlEncoded
    @POST("historytransaction.php")
    Call<List<HistoryTransaction>> fetchOutHistoryTransaction(
            @Field("userId") String userId
            , @Field("offset") Integer offset
            , @Field("fetchOutHistoryTransaction") String fetchOutHistoryTransaction);
}
