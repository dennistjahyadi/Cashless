package com.dennistjahyadi.cashless.Barcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dennistjahyadi.cashless.Barcode.adapter.SavedBarcodeRecyclerViewAdapter;
import com.dennistjahyadi.cashless.Components.EndlessRecyclerViewScrollListener;
import com.dennistjahyadi.cashless.Models.BarcodeBusiness;
import com.dennistjahyadi.cashless.R;
import com.dennistjahyadi.cashless.RetrofitServices.APIBusinessListService;
import com.dennistjahyadi.cashless.Utils.ConstantUtils;
import com.dennistjahyadi.cashless.Utils.SharedPreferenceUtils;
import com.dennistjahyadi.cashless.Utils.TypeFaceManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Denn on 10/20/2017.
 */

public class SavedBarcodeListActivity extends AppCompatActivity {
    private static final int ACTIVITY_RESULT_BUSINESS_CREATE = 1001;
    private List<BarcodeBusiness> sourceMapList = new ArrayList<>();
    private TextView tvBtnCreateBarcode;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private SavedBarcodeRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_business_list);
        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvBtnCreateBarcode = (TextView) findViewById(R.id.tvBtnCreateBarcode);
        tvBtnCreateBarcode.setTypeface(TypeFaceManager.getFontAwesomeTypeface(this));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        tvBtnCreateBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SavedBarcodeCreateActivity.class);
                startActivityForResult(i, ACTIVITY_RESULT_BUSINESS_CREATE);
            }
        });
        adapter = new SavedBarcodeRecyclerViewAdapter(sourceMapList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchData(totalItemsCount);
            }
        });

        fetchData(0);
    }

    private void fetchData(int offset) {
        String userId = SharedPreferenceUtils.getUserId(this);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APIBusinessListService service = retrofit.create(APIBusinessListService.class);
        Call<List<BarcodeBusiness>> call = service.fetchAllBusinessListByUserId(userId, offset);
        call.enqueue(new Callback<List<BarcodeBusiness>>() {
            @Override
            public void onResponse(Call<List<BarcodeBusiness>> call, Response<List<BarcodeBusiness>> response) {
                for (BarcodeBusiness bb : response.body()) {
                    sourceMapList.add(bb);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<BarcodeBusiness>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_RESULT_BUSINESS_CREATE) {
            if (resultCode == Activity.RESULT_OK) {
                fetchData(sourceMapList.size());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }
}
