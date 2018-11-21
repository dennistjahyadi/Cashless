package com.dennistjahyadi.cashless.Balance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dennistjahyadi.cashless.Deposit.DepositActivity;
import com.dennistjahyadi.cashless.Models.User;
import com.dennistjahyadi.cashless.R;
import com.dennistjahyadi.cashless.RetrofitServices.APIUserService;
import com.dennistjahyadi.cashless.Utils.ConstantUtils;
import com.dennistjahyadi.cashless.Utils.Encryption;
import com.dennistjahyadi.cashless.Utils.SharedPreferenceUtils;
import com.dennistjahyadi.cashless.Utils.TypeFaceManager;
import com.dennistjahyadi.cashless.Withdraw.WithdrawActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Denn on 10/2/2017.
 */

public class BalanceActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvBalance, tvBtnRefresh;
    private FrameLayout frameProgressBar;
    private CoordinatorLayout coordinatorLayout;
    private LinearLayout layDeposit, layWithdraw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        layDeposit = (LinearLayout) findViewById(R.id.layDeposit);
        layWithdraw = (LinearLayout) findViewById(R.id.layWithdraw);
        tvBtnRefresh = (TextView) findViewById(R.id.tvBtnRefresh);
        tvBalance = (TextView) findViewById(R.id.tvBalance);
        frameProgressBar = (FrameLayout) findViewById(R.id.frameProgressBar);
        tvBtnRefresh.setTypeface(TypeFaceManager.getFontAwesomeTypeface(this));
        //tvBalance.setText(SharedPreferenceUtils.getUserBalance(getApplicationContext()).toString());
        tvBtnRefresh.setVisibility(View.GONE);

        setButtonFunction(false);
        getUserBalance();
        layDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DepositActivity.class);
                startActivity(i);
            }
        });
        layWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), WithdrawActivity.class);
                startActivity(i);
            }
        });
    }

    private void setButtonFunction(boolean allow) {
        if (allow) {
            frameProgressBar.setVisibility(View.GONE);
        } else {
            frameProgressBar.setVisibility(View.VISIBLE);
        }
        layDeposit.setEnabled(allow);
        layWithdraw.setEnabled(allow);

    }


    private void getUserBalance() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences prefs = getSharedPreferences(SharedPreferenceUtils.SP_NAME, MODE_PRIVATE);
                String email = prefs.getString(SharedPreferenceUtils.EMAIL, null);
                String pass = prefs.getString(SharedPreferenceUtils.PASSWORD, null);
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(ConstantUtils.webserviceurl)
                        .build();
                APIUserService service = retrofit.create(APIUserService.class);
                Call<User> call = service.getUserInfo(email, Encryption.encrypt(pass), "a");
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        SharedPreferenceUtils.setUserBalance(getApplicationContext(), response.body().getBalance());
                        tvBalance.setText(SharedPreferenceUtils.getUserBalance(getApplicationContext()) + ""); //get from prefs
                        setButtonFunction(true);

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        setButtonFunction(false);
                        Snackbar.make(coordinatorLayout, "Please check your internet connection",
                                Snackbar.LENGTH_LONG)
                                .show();
                        setButtonFunction(false);
                    }
                });
            }
        }).start();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }
}

/*tvBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // setButtonFunction(false);
                frameProgressBar.setVisibility(View.VISIBLE);
                getUserBalance(); //get from database and save it to prefs

            }
        });*/