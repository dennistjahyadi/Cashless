package com.dennistjahyadi.cashless.Payment;

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

import com.dennistjahyadi.cashless.Models.BarcodeBusiness;
import com.dennistjahyadi.cashless.Models.HistoryTransaction;
import com.dennistjahyadi.cashless.Models.User;
import com.dennistjahyadi.cashless.R;
import com.dennistjahyadi.cashless.RetrofitServices.APIBusinessListService;
import com.dennistjahyadi.cashless.RetrofitServices.APITransactionService;
import com.dennistjahyadi.cashless.RetrofitServices.APIUserService;
import com.dennistjahyadi.cashless.TransactionDetail.TransactionDetailActivity;
import com.dennistjahyadi.cashless.Utils.ConstantUtils;
import com.dennistjahyadi.cashless.Utils.Encryption;
import com.dennistjahyadi.cashless.Utils.SharedPreferenceUtils;
import com.dennistjahyadi.cashless.Utils.TypeFaceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Denn on 10/8/2017.
 */

public class PaymentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvBalance, tvPayTo, tvEmailContact, tvBtnRefresh;
    private EditText etAmount, etDesc;
    private TextView tvBtnSendMoney;
    private FrameLayout frameProgressBar;
    private String userIdReceiver, emailReceiver, fullnameReceiver;
    private CoordinatorLayout coordinatorLayout;
    private String type; //transfer,business;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        type = getIntent().getStringExtra("type");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        tvBalance = (TextView) findViewById(R.id.tvBalance);
        tvPayTo = (TextView) findViewById(R.id.tvFullname);
        tvEmailContact = (TextView) findViewById(R.id.tvEmailContact);
        tvBtnRefresh = (TextView) findViewById(R.id.tvBtnRefresh);
        tvBtnRefresh.setTypeface(TypeFaceManager.getFontAwesomeTypeface(this));
        etAmount = (EditText) findViewById(R.id.etAmount);
        etDesc = (EditText) findViewById(R.id.etDesc);
        tvBtnSendMoney = (TextView) findViewById(R.id.tvBtnSendMoney);
        frameProgressBar = (FrameLayout) findViewById(R.id.frameProgressBar);
        tvBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonFunction(false);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences prefs = getSharedPreferences(SharedPreferenceUtils.SP_NAME, MODE_PRIVATE);
                        String email = prefs.getString(SharedPreferenceUtils.EMAIL, null);
                        String pass = prefs.getString(SharedPreferenceUtils.PASSWORD, null);
                        getUserBalance(email, pass); //get from database and save it to prefs

                    }
                }).start();

            }
        });
        tvBtnSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSendMoney();
            }
        });
        init();

    }

    private void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences(SharedPreferenceUtils.SP_NAME, MODE_PRIVATE);
                String email = prefs.getString(SharedPreferenceUtils.EMAIL, null);
                String pass = prefs.getString(SharedPreferenceUtils.PASSWORD, null);
                getUserBalance(email, pass); //get from database and save it to prefs

            }
        }).start();
        if (type.equals("transfer")) {
            userIdReceiver = getIntent().getStringExtra("userIdReceiver");
            getReceiverInfoById(userIdReceiver);
        } else if (type.equals("business")) {
            etAmount.setEnabled(false);
            etDesc.setEnabled(false);
            getBusinessInfoById(getIntent().getStringExtra("businessId"));
        }
    }

    private void setButtonFunction(boolean bool) {
        if (bool) {
            frameProgressBar.setVisibility(View.GONE);
        } else {
            frameProgressBar.setVisibility(View.VISIBLE);
        }
        tvBtnRefresh.setEnabled(bool);
        tvBtnSendMoney.setEnabled(bool);
        if (type.equals("transfer")) {
            etAmount.setEnabled(bool);
            etDesc.setEnabled(bool);
        }
    }

    private void getReceiverInfoById(String userId) {
        setButtonFunction(false);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APIUserService service = retrofit.create(APIUserService.class);
        Call<User> call = service.getReceiverInfoById(userId, "a");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                emailReceiver = user.getEmail();
                fullnameReceiver = user.getFullname();
                tvPayTo.setText(fullnameReceiver);
                tvEmailContact.setText(emailReceiver);
                setButtonFunction(true);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Snackbar.make(coordinatorLayout, ConstantUtils.parseErrorMessage(t),
                        Snackbar.LENGTH_LONG)
                        .show();
                setButtonFunction(true);
            }
        });
    }

    private void getBusinessInfoById(String businessId) {
        setButtonFunction(false);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APIBusinessListService service = retrofit.create(APIBusinessListService.class);
        Call<BarcodeBusiness> call = service.getBusinessInfoById(businessId, "s");
        call.enqueue(new Callback<BarcodeBusiness>() {
            @Override
            public void onResponse(Call<BarcodeBusiness> call, Response<BarcodeBusiness> response) {
                BarcodeBusiness barcodeBusiness = response.body();
                fullnameReceiver = barcodeBusiness.getReceiverFullname();
                emailReceiver = barcodeBusiness.getReceiverEmail();
                userIdReceiver = barcodeBusiness.getFk_users();
                tvPayTo.setText(fullnameReceiver);
                tvEmailContact.setText(emailReceiver);
                etDesc.setText(barcodeBusiness.getBusiness_desc());
                etAmount.setText(barcodeBusiness.getAmount() + "");
                setButtonFunction(true);

            }

            @Override
            public void onFailure(Call<BarcodeBusiness> call, Throwable t) {
                Snackbar.make(coordinatorLayout, ConstantUtils.parseErrorMessage(t),
                        Snackbar.LENGTH_LONG)
                        .show();
                setButtonFunction(true);
            }
        });
    }

    private void getUserBalance(String email, String pass) {
        setButtonFunction(false);
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

                Snackbar.make(coordinatorLayout, ConstantUtils.parseErrorMessage(t),
                        Snackbar.LENGTH_LONG)
                        .show();
                setButtonFunction(true);
            }
        });
    }

    private void doSendMoney() {
        setButtonFunction(false);
        final String amount = etAmount.getText().toString();
        if (!amount.isEmpty()) {
            if (SharedPreferenceUtils.getUserBalance(this) > Integer.parseInt(amount)) {
                doVerification(amount);

            } else {
                setButtonFunction(true);
                frameProgressBar.setVisibility(View.GONE);
                Snackbar.make(coordinatorLayout, "Not enough balance", Snackbar.LENGTH_LONG).show();
            }
        } else {
            setButtonFunction(true);
            frameProgressBar.setVisibility(View.GONE);
            Snackbar.make(coordinatorLayout, "Please input an amount", Snackbar.LENGTH_LONG).show();
        }

    }

    private void doVerification(final String amount) {
        AlertDialog.Builder builder;
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(PaymentActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(PaymentActivity.this);
        }*/
        builder = new AlertDialog.Builder(PaymentActivity.this);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                setButtonFunction(true);
                frameProgressBar.setVisibility(View.GONE);
            }
        });
        builder.setTitle("Verification")
                .setMessage("Transfer " + etAmount.getText() + " to " + fullnameReceiver + "?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        setButtonFunction(false);
                        frameProgressBar.setVisibility(View.VISIBLE);

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

                                if (response.body().getBalance() > Integer.parseInt(amount)) {
                                    doTransaction(amount);
                                } else {
                                    setButtonFunction(true);
                                    frameProgressBar.setVisibility(View.GONE);
                                    Snackbar.make(coordinatorLayout, "Not enough balance", Snackbar.LENGTH_LONG).show();
                                }

                                setButtonFunction(true);

                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Snackbar.make(coordinatorLayout, ConstantUtils.parseErrorMessage(t),
                                        Snackbar.LENGTH_LONG)
                                        .show();
                                setButtonFunction(true);
                            }
                        });

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        setButtonFunction(true);
                        frameProgressBar.setVisibility(View.GONE);

                    }
                })
                .show();

    }

    private void doTransaction(String amount) {
        SharedPreferences prefs = getSharedPreferences(SharedPreferenceUtils.SP_NAME, MODE_PRIVATE);
        String userIdSender = prefs.getString(SharedPreferenceUtils.ID, null);
        String emailSender = prefs.getString(SharedPreferenceUtils.EMAIL, null);
        String passSender = prefs.getString(SharedPreferenceUtils.PASSWORD, null);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APITransactionService service = retrofit.create(APITransactionService.class);
        Call<HistoryTransaction> call = service.doTransaction(Encryption.encrypt(userIdSender), emailSender, Encryption.encrypt(passSender), emailReceiver, Encryption.encrypt(userIdReceiver), amount, etDesc.getText().toString());
        call.enqueue(new Callback<HistoryTransaction>() {
            @Override
            public void onResponse(Call<HistoryTransaction> call, Response<HistoryTransaction> response) {
                if (response.body() != null) {

                    Intent i = new Intent(getApplicationContext(), TransactionDetailActivity.class);
                    i.putExtra("data", response.body());
                    startActivity(i);
                    finish();


                } else {
                    Snackbar.make(coordinatorLayout, "Not enough balance", Snackbar.LENGTH_LONG).show();
                    setButtonFunction(true);
                    //  finish();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }
}
