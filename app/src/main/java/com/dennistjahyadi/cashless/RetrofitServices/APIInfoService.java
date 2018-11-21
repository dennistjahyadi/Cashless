package com.dennistjahyadi.cashless.RetrofitServices;


import com.dennistjahyadi.cashless.Models.InfoModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Denn on 7/2/2017.
 */

public interface APIInfoService {
    @GET("info.php")
    Call<InfoModel> getInfo(@Query("category") String category);


}
