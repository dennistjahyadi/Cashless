package com.dennistjahyadi.cashless;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dennistjahyadi.cashless.RetrofitServices.APIUserService;
import com.dennistjahyadi.cashless.Utils.ConstantUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Denn on 7/3/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    private TextView tvRegister, tvBack;
    private EditText etEmail, etName, etPassword, etRetypePassword;
    private CoordinatorLayout coordinatorLayout;
    private FrameLayout frameLayoutProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        Typeface fontLatoLight = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");
        Typeface fontLatoRegular = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        frameLayoutProgressBar = (FrameLayout) findViewById(R.id.frameWorkProgressBar);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvBack = (TextView) findViewById(R.id.tvBack);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etName = (EditText) findViewById(R.id.etName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRetypePassword = (EditText) findViewById(R.id.etRetypePassword);
        tvBack.setTypeface(fontLatoRegular);
        tvRegister.setTypeface(fontLatoLight);
        etEmail.setTypeface(fontLatoLight);
        etName.setTypeface(fontLatoLight);
        etPassword.setTypeface(fontLatoLight);
        etRetypePassword.setTypeface(fontLatoLight);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etEmail.getText().toString().isEmpty() || etName.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty() || etRetypePassword.getText().toString().isEmpty()) {
                    Snackbar.make(coordinatorLayout, R.string.lit_please_fill_all_form, Snackbar.LENGTH_LONG).show();
                } else {
                    if (!ConstantUtils.isEmailValid(etEmail.getText().toString())) {
                        Snackbar.make(coordinatorLayout, R.string.lit_email_not_valid, Snackbar.LENGTH_LONG).show();
                    } else {
                        if (!etPassword.getText().toString().equals(etRetypePassword.getText().toString())) {
                            Snackbar.make(coordinatorLayout, R.string.lit_type_the_password_correctly, Snackbar.LENGTH_LONG).show();
                        } else {
                            disableComponent();
                            frameLayoutProgressBar.setVisibility(View.VISIBLE);
                            doRegisterUser(etEmail.getText().toString(), etName.getText().toString(), etPassword.getText().toString());
                        }
                    }
                }

            }
        });

    }

    private void doRegisterUser(String email, String name, String pass) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APIUserService service = retrofit.create(APIUserService.class);
        Call<String> call = service.registerClient(email, name, pass);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("0")) {
                    Snackbar.make(coordinatorLayout, R.string.lit_email_has_been_used, Snackbar.LENGTH_LONG).show();
                    frameLayoutProgressBar.setVisibility(View.GONE);
                    enableComponent();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.lit_register_success, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Snackbar.make(coordinatorLayout, ConstantUtils.parseErrorMessage(t),
                        Snackbar.LENGTH_LONG)
                        .show();
            }
        });

    }

    private void disableComponent() {
        etEmail.setEnabled(false);
        etName.setEnabled(false);
        etPassword.setEnabled(false);
        etRetypePassword.setEnabled(false);
        tvRegister.setEnabled(false);
        tvBack.setEnabled(false);
    }

    private void enableComponent() {
        etEmail.setEnabled(true);
        etName.setEnabled(true);
        etPassword.setEnabled(true);
        etRetypePassword.setEnabled(true);
        tvRegister.setEnabled(true);
        tvBack.setEnabled(true);
    }


}
