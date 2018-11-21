package com.dennistjahyadi.cashless.Withdraw;

import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dennistjahyadi.cashless.Models.BankAccount;
import com.dennistjahyadi.cashless.Models.HistoryTransaction;
import com.dennistjahyadi.cashless.Models.User;
import com.dennistjahyadi.cashless.R;
import com.dennistjahyadi.cashless.RetrofitServices.APIBankService;
import com.dennistjahyadi.cashless.RetrofitServices.APITransactionService;
import com.dennistjahyadi.cashless.RetrofitServices.APIUserService;
import com.dennistjahyadi.cashless.TransactionDetail.TransactionDetailActivity;
import com.dennistjahyadi.cashless.Utils.ConstantUtils;
import com.dennistjahyadi.cashless.Utils.Encryption;
import com.dennistjahyadi.cashless.Utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Denn on 10/24/2017.
 */

public class WithdrawActivity extends AppCompatActivity {

    private List<BankAccount> bankAccountList = new ArrayList<>();
    private TextView tvCurrentBalance, tvBankAccountName, tvBankAccountNumber, tvBankAccount, tvBtnWithdraw;
    private Spinner spinnerBankAccount;
    private EditText etAmountWithdraw, etPassword;
    private Toolbar toolbar;
    private FrameLayout frameProgressBar;
    private CoordinatorLayout coordinatorLayout;
    private Integer itemSelectedPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        init();

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        tvCurrentBalance = (TextView) findViewById(R.id.tvCurrentBalance);
        tvBankAccountName = (TextView) findViewById(R.id.tvBankAccountName);
        tvBankAccountNumber = (TextView) findViewById(R.id.tvBankAccountNumber);
        tvBankAccount = (TextView) findViewById(R.id.tvBankAccount);
        tvBtnWithdraw = (TextView) findViewById(R.id.tvBtnWithdraw);
        spinnerBankAccount = (Spinner) findViewById(R.id.spinnerBankAccount);
        etAmountWithdraw = (EditText) findViewById(R.id.etAmountWithdraw);
        etPassword = (EditText) findViewById(R.id.etPassword);
        frameProgressBar = (FrameLayout) findViewById(R.id.frameProgressBar);
        getUserBalance();
        fetchBankAccount();
        tvBtnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doWithdraw();

            }
        });
    }

    private void doWithdraw() {
        setButtonFunction(false);
        final String amount = etAmountWithdraw.getText().toString();
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
        builder = new AlertDialog.Builder(WithdrawActivity.this);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                setButtonFunction(true);
                frameProgressBar.setVisibility(View.GONE);
            }
        });
        builder.setTitle("Verification")
                .setMessage("Withdraw " + etAmountWithdraw.getText() + "?")
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
                                    doTransactionWithdraw(bankAccountList.get(spinnerBankAccount.getSelectedItemPosition()).getBank_account_id(), Integer.parseInt(amount));
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

    private void doTransactionWithdraw(String bankAccountId, Integer amount) {
        SharedPreferences prefs = getSharedPreferences(SharedPreferenceUtils.SP_NAME, MODE_PRIVATE);
        String userId = prefs.getString(SharedPreferenceUtils.ID, null);
        String email = prefs.getString(SharedPreferenceUtils.EMAIL, null);
        String pass = etPassword.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APITransactionService service = retrofit.create(APITransactionService.class);
        Call<HistoryTransaction> call = service.doWithdraw(Encryption.encrypt(userId), email, Encryption.encrypt(pass), bankAccountId, amount, "asf");
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
                        Snackbar.make(coordinatorLayout, "Not enough balance", Snackbar.LENGTH_LONG).show();
                        setButtonFunction(true);
                    } else if (response.body().getId().equals("-3")) {
                        Snackbar.make(coordinatorLayout, "Wrong password", Snackbar.LENGTH_LONG).show();
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

    private void fetchBankAccount() {
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
                bankAccountList = response.body();
                List<String> bankName = new ArrayList<String>();
                bankName.add("Pilih Bank");
                for (BankAccount bankAccount : bankAccountList) {
                    bankName.add(bankAccount.getBank_name());
                }
                MyCustomAdapter dataAdapter = new MyCustomAdapter(getApplicationContext(), R.layout.spinner_item_bank_custom, bankAccountList);
                spinnerBankAccount.setAdapter(dataAdapter);
                setButtonFunction(true);

            }

            @Override
            public void onFailure(Call<List<BankAccount>> call, Throwable t) {
                Snackbar.make(coordinatorLayout, ConstantUtils.parseErrorMessage(t), Snackbar.LENGTH_LONG).show();
                setButtonFunction(true);
            }
        });
    }

    private void setButtonFunction(boolean allow) {
        if (allow) {
            frameProgressBar.setVisibility(View.GONE);
        } else {
            frameProgressBar.setVisibility(View.VISIBLE);

        }
        etAmountWithdraw.setEnabled(allow);
        etPassword.setEnabled(allow);
        tvBtnWithdraw.setEnabled(allow);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    public class MyCustomAdapter extends ArrayAdapter<BankAccount> {

        List<BankAccount> bankAccountList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               List<BankAccount> objects) {
            super(context, textViewResourceId, objects);
            this.bankAccountList = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //return super.getView(position, convertView, parent);
            BankAccount bankAccount = bankAccountList.get(position);
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.spinner_item_bank_custom, parent, false);
            TextView tvBankAccount = (TextView) view.findViewById(R.id.tvBankAccount);
            TextView tvBankAccountName = (TextView) view.findViewById(R.id.tvBankAccountName);
            TextView tvBankAccountNumber = (TextView) view.findViewById(R.id.tvBankAccountNumber);
            tvBankAccount.setText(bankAccount.getBank_name());
            tvBankAccountName.setText(bankAccount.getAtas_nama());
            tvBankAccountNumber.setText(bankAccount.getNo_rekening());


            return view;
        }
    }
}
