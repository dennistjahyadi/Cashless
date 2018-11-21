package com.dennistjahyadi.cashless.RetrofitServices;

import com.dennistjahyadi.cashless.Models.HistoryTransaction;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Denn on 10/9/2017.
 */

public interface APITransactionService {
    @FormUrlEncoded
    @POST("transaction.php")
    Call<HistoryTransaction> doTransaction(
            @Field("userIdSender") String userIdSender
            , @Field("emailSender") String emailSender
            , @Field("passSender") String passSender
            , @Field("emailReceiver") String emailReceiver
            , @Field("userIdReceiver") String userIdReceiver
            , @Field("amount") String amount
            , @Field("description") String description);

    @FormUrlEncoded
    @POST("transaction.php")
    Call<HistoryTransaction> doWithdraw(
            @Field("userId") String userId
            , @Field("email") String email
            , @Field("pass") String pass
            , @Field("bankAccountId") String bankAccountId
            , @Field("amount") Integer amount
            , @Field("doWithdraw") String doWithdraw);

    @FormUrlEncoded
    @POST("transaction.php")
    Call<HistoryTransaction> doDeposit(
            @Field("userId") String userId
            , @Field("email") String email
            , @Field("pass") String pass
            , @Field("amount") Integer amount
            , @Field("doDeposit") String doDeposit);
}
