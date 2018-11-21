package com.dennistjahyadi.cashless.RetrofitServices;

import com.dennistjahyadi.cashless.Models.Bank;
import com.dennistjahyadi.cashless.Models.BankAccount;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Denn on 10/22/2017.
 */

public interface APIBankService {

    @GET("bank.php")
    Call<List<Bank>> fetchAllBank(@Query("fetchAllBank") String fetchAllBank);

    @FormUrlEncoded
    @POST("bank.php")
    Call<String> insertBankAccount(
            @Field("userId") String userId
            , @Field("userPass") String userPass
            , @Field("bankId") String bankId
            , @Field("bankAccountName") String bankAccountName
            , @Field("bankAccountNumber") String bankAccountNumber
            , @Field("insertBankAccount") String insertBankAccount
    );

    @FormUrlEncoded
    @POST("bank.php")
    Call<List<BankAccount>> fetchBankAccountByUserId(
            @Field("userId") String userId
            , @Field("fetchBankAccountByUserId") String fetchBankAccountByUserId
    );

}
