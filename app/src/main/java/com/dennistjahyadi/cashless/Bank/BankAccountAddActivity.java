package com.dennistjahyadi.cashless.Bank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dennistjahyadi.cashless.Models.Bank;
import com.dennistjahyadi.cashless.R;
import com.dennistjahyadi.cashless.RetrofitServices.APIBankService;
import com.dennistjahyadi.cashless.Utils.ConstantUtils;
import com.dennistjahyadi.cashless.Utils.Encryption;
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
 * Created by Denn on 10/22/2017.
 */

public class BankAccountAddActivity extends AppCompatActivity {

    private Spinner spinnerBank;
    private TextView tvBtnFinish;
    private EditText etAccountName, etAccountNumber, etPassword;
    private FrameLayout frameProgressBar;
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private List<Bank> bankList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account_add);
        init();

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        spinnerBank = (Spinner) findViewById(R.id.spinnerBank);
        tvBtnFinish = (TextView) findViewById(R.id.tvBtnFinish);
        tvBtnFinish.setTypeface(TypeFaceManager.getFontAwesomeTypeface(this));
        etAccountName = (EditText) findViewById(R.id.etAccountName);
        etAccountNumber = (EditText) findViewById(R.id.etAccountNumber);
        etPassword = (EditText) findViewById(R.id.etPassword);
        frameProgressBar = (FrameLayout) findViewById(R.id.frameProgressBar);
        tvBtnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFinishClicked();
            }
        });
        loadBank();
    }

    private void setButtonFunction(boolean allow) {
        if (allow) {
            frameProgressBar.setVisibility(View.GONE);
        } else {
            frameProgressBar.setVisibility(View.VISIBLE);
        }
        tvBtnFinish.setEnabled(allow);
        etAccountName.setEnabled(allow);
        etPassword.setEnabled(allow);
        etAccountNumber.setEnabled(allow);
        spinnerBank.setEnabled(allow);
    }

    private void btnFinishClicked() {
        int itemIndex = spinnerBank.getSelectedItemPosition();
        if (itemIndex == 0) {
            Snackbar.make(coordinatorLayout, "Please select your bank", Snackbar.LENGTH_LONG).show();
        } else {
            if (TextUtils.isEmpty(etAccountName.getText()) || TextUtils.isEmpty(etAccountNumber.getText()) || TextUtils.isEmpty(etPassword.getText())) {
                Snackbar.make(coordinatorLayout, "Please fill all field", Snackbar.LENGTH_LONG).show();
            } else {
                saveBankAccount(etPassword.getText().toString(), bankList.get(itemIndex - 1).getBank_id(), etAccountName.getText().toString(), etAccountNumber.getText().toString());
            }
        }
    }

    private void saveBankAccount(String userPass, String bankId, String bankAccountName, String bankAccountNumber) {
        setButtonFunction(false);
        String userId = SharedPreferenceUtils.getUserId(this);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APIBankService service = retrofit.create(APIBankService.class);
        Call<String> call = service.insertBankAccount(userId, Encryption.encrypt(userPass), bankId, bankAccountName, bankAccountNumber, "a");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.body().equals("1")) {
                    finish();
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else if (response.body().equals("11")) {
                    Snackbar.make(coordinatorLayout, "Wrong password.", Snackbar.LENGTH_LONG).show();
                    setButtonFunction(true);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                setButtonFunction(true);
                Snackbar.make(coordinatorLayout, ConstantUtils.parseErrorMessage(t), Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private void loadBank() {
        setButtonFunction(false);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APIBankService service = retrofit.create(APIBankService.class);
        Call<List<Bank>> call = service.fetchAllBank("fetchAllBank");
        call.enqueue(new Callback<List<Bank>>() {
            @Override
            public void onResponse(Call<List<Bank>> call, Response<List<Bank>> response) {
                bankList = response.body();
                List<String> bankName = new ArrayList<String>();
                bankName.add("Pilih Bank");
                for (Bank bank : bankList) {
                    bankName.add(bank.getBank_name());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.spinner_item_bank, bankName);
                spinnerBank.setAdapter(dataAdapter);
                setButtonFunction(true);

            }

            @Override
            public void onFailure(Call<List<Bank>> call, Throwable t) {
                setButtonFunction(false);
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
