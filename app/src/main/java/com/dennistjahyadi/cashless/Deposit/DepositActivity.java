package com.dennistjahyadi.cashless.Deposit;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dennistjahyadi.cashless.Models.HistoryTransaction;
import com.dennistjahyadi.cashless.Models.User;
import com.dennistjahyadi.cashless.R;
import com.dennistjahyadi.cashless.RetrofitServices.APITransactionService;
import com.dennistjahyadi.cashless.RetrofitServices.APIUserService;
import com.dennistjahyadi.cashless.TransactionDetail.TransactionDetailActivity;
import com.dennistjahyadi.cashless.Utils.ConstantUtils;
import com.dennistjahyadi.cashless.Utils.Encryption;
import com.dennistjahyadi.cashless.Utils.SharedPreferenceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Denn on 10/24/2017.
 */

public class DepositActivity extends AppCompatActivity {

    private TextView tvCurrentBalance, tvBtnDeposit;
    private EditText etAmountDeposit;
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private FrameLayout frameProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        frameProgressBar = (FrameLayout) findViewById(R.id.frameProgressBar);
        tvCurrentBalance = (TextView) findViewById(R.id.tvCurrentBalance);
        tvBtnDeposit = (TextView) findViewById(R.id.tvBtnDeposit);
        etAmountDeposit = (EditText) findViewById(R.id.etAmountDeposit);

        tvBtnDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer amount = Integer.parseInt(etAmountDeposit.getText().toString());
                if (amount >= 10000) {
                    doVerification(amount);
                } else {
                    Snackbar.make(coordinatorLayout, "Minimum deposit Rp 10.000",
                            Snackbar.LENGTH_LONG)
                            .show();
                }

            }
        });
        getUserBalance();
    }

    private void doVerification(final Integer amount) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(DepositActivity.this);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                setButtonFunction(true);
            }
        });
        builder.setTitle("Verification")
                .setMessage("Deposit Rp " + amount + "?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        setButtonFunction(false);
                        doDeposit(amount);

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        setButtonFunction(true);

                    }
                })
                .show();

    }

    private void doDeposit(Integer amount) {
        setButtonFunction(false);
        SharedPreferences prefs = getSharedPreferences(SharedPreferenceUtils.SP_NAME, MODE_PRIVATE);
        String email = prefs.getString(SharedPreferenceUtils.EMAIL, null);
        String pass = prefs.getString(SharedPreferenceUtils.PASSWORD, null);
        String userId = prefs.getString(SharedPreferenceUtils.ID, null);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APITransactionService service = retrofit.create(APITransactionService.class);
        Call<HistoryTransaction> call = service.doDeposit(Encryption.encrypt(userId), email, Encryption.encrypt(pass), amount, "asd");
        call.enqueue(new Callback<HistoryTransaction>() {
            @Override
            public void onResponse(Call<HistoryTransaction> call, Response<HistoryTransaction> response) {
                if (response.body() != null) {
                    if (!response.body().getId().equals("-5") && !response.body().getId().equals("-3")) {
                        Intent i = new Intent(getApplicationContext(), TransactionDetailActivity.class);
                        i.putExtra("data", response.body());
                        startActivity(i);
                        finish();
                    } else if (response.body().getId().equals("-5")) {
                        Snackbar.make(coordinatorLayout, "error", Snackbar.LENGTH_LONG).show();
                        setButtonFunction(true);
                    } else if (response.body().getId().equals("-3")) {
                        Snackbar.make(coordinatorLayout, "Minimum deposit Rp 10.000", Snackbar.LENGTH_LONG).show();
                        setButtonFunction(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<HistoryTransaction> call, Throwable t) {
                Snackbar.make(coordinatorLayout, ConstantUtils.parseErrorMessage(t),
                        Snackbar.LENGTH_LONG)
                        .show();
                setButtonFunction(true);
            }
        });

    }

    private void getUserBalance() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setButtonFunction(false);

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
                        tvCurrentBalance.setText(SharedPreferenceUtils.getUserBalance(getApplicationContext()) + ""); //get from prefs
                        setButtonFunction(true);

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Snackbar.make(coordinatorLayout, "Please check your internet connection",
                                Snackbar.LENGTH_LONG)
                                .show();
                        setButtonFunction(true);
                    }
                });
            }
        }).start();

    }

    private void setButtonFunction(boolean allow) {
        if (allow) {
            frameProgressBar.setVisibility(View.GONE);
        } else {
            frameProgressBar.setVisibility(View.VISIBLE);
        }
        etAmountDeposit.setEnabled(allow);
        tvBtnDeposit.setEnabled(allow);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
