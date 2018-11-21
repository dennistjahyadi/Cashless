package com.dennistjahyadi.cashless.Bank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.dennistjahyadi.cashless.Bank.adapter.BankAccountRecyclerViewAdapter;
import com.dennistjahyadi.cashless.Models.BankAccount;
import com.dennistjahyadi.cashless.R;
import com.dennistjahyadi.cashless.RetrofitServices.APIBankService;
import com.dennistjahyadi.cashless.Utils.ConstantUtils;
import com.dennistjahyadi.cashless.Utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Denn on 10/21/2017.
 */

public class BankAccountActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fabAddBank;
    private RecyclerView recyclerView;
    private List<BankAccount> bankAccountList = new ArrayList<>();
    private BankAccountRecyclerViewAdapter adapter;
    private FrameLayout frameProgressBar;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account);
        init();

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Bank Account");
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        frameProgressBar = (FrameLayout) findViewById(R.id.frameProgressBar);
        fabAddBank = (FloatingActionButton) findViewById(R.id.fabAddBank);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        fabAddBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), BankAccountAddActivity.class);
                startActivity(i);
            }
        });

        adapter = new BankAccountRecyclerViewAdapter(this, bankAccountList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void setButtonFunction(boolean allow) {
        if (allow) {
            frameProgressBar.setVisibility(View.GONE);
        } else {
            frameProgressBar.setVisibility(View.VISIBLE);
        }
        fabAddBank.setEnabled(allow);
    }

    private void fetchData() {
        setButtonFunction(false);
        String userId = SharedPreferenceUtils.getUserId(this);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APIBankService service = retrofit.create(APIBankService.class);
        Call<List<BankAccount>> call = service.fetchBankAccountByUserId(userId, "a");
        call.enqueue(new Callback<List<BankAccount>>() {
            @Override
            public void onResponse(Call<List<BankAccount>> call, Response<List<BankAccount>> response) {
                for (BankAccount ba : bankAccountList) {
                    bankAccountList.remove(ba);
                }
                for (BankAccount ba : response.body()) {
                    bankAccountList.add(ba);
                }
                adapter.notifyDataSetChanged();
                setButtonFunction(true);

            }

            @Override
            public void onFailure(Call<List<BankAccount>> call, Throwable t) {
                Snackbar.make(coordinatorLayout, ConstantUtils.parseErrorMessage(t), Snackbar.LENGTH_LONG).show();
                setButtonFunction(true);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }
}
