package com.dennistjahyadi.cashless.Barcode;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dennistjahyadi.cashless.R;
import com.dennistjahyadi.cashless.RetrofitServices.APIBusinessListService;
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
 * Created by Denn on 10/20/2017.
 */

public class BarcodeBusinessCreateActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etBusinessName, etDesc, etAmount;
    private TextView tvBtnAdd;
    private CoordinatorLayout coordinatorLayout;
    private FrameLayout frameProgressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_barcode_add);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        tvBtnAdd = (TextView) findViewById(R.id.tvBtnAdd);
        tvBtnAdd.setTypeface(TypeFaceManager.getFontAwesomeTypeface(this));
        etBusinessName = (EditText) findViewById(R.id.etBusinessName);
        etDesc = (EditText) findViewById(R.id.etDesc);
        etAmount = (EditText) findViewById(R.id.etAmount);
        frameProgressBar = (FrameLayout) findViewById(R.id.frameProgressBar);
        tvBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etBusinessName.getText()) || TextUtils.isEmpty(etDesc.getText()) || TextUtils.isEmpty(etAmount.getText())) {
                    Snackbar.make(coordinatorLayout, "Please fill all field", Snackbar.LENGTH_LONG).show();
                } else {
                    String businessName = etBusinessName.getText().toString();
                    String businessDesc = etDesc.getText().toString();
                    Integer amount = Integer.parseInt(etAmount.getText().toString());
                    doSave(businessName, businessDesc, amount);
                }
            }
        });
    }

    private void setButtonFunction(boolean allow) {

        if (allow) {
            frameProgressBar.setVisibility(View.GONE);
        } else {
            frameProgressBar.setVisibility(View.VISIBLE);
        }

        tvBtnAdd.setEnabled(allow);
        etBusinessName.setEnabled(allow);
        etDesc.setEnabled(allow);
        etAmount.setEnabled(allow);
    }

    private void doSave(String businessName, String businessDesc, Integer amount) {
        setButtonFunction(false);
        SharedPreferences prefs = SharedPreferenceUtils.getPrefs(this);
        String email = prefs.getString(SharedPreferenceUtils.EMAIL, null);
        String pass = prefs.getString(SharedPreferenceUtils.PASSWORD, null);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APIBusinessListService service = retrofit.create(APIBusinessListService.class);
        Call<String> call = service.addBusiness(email, Encryption.encrypt(pass), businessName, businessDesc, amount);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("1")) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    Toast.makeText(getApplicationContext(), "add success", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Snackbar.make(coordinatorLayout, "Something wrong.", Snackbar.LENGTH_LONG).show();
                    setButtonFunction(true);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Snackbar.make(coordinatorLayout, ConstantUtils.parseErrorMessage(t), Snackbar.LENGTH_LONG).show();
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
