package com.dennistjahyadi.cashless;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dennistjahyadi.cashless.Models.InfoModel;
import com.dennistjahyadi.cashless.RetrofitServices.APIInfoService;
import com.dennistjahyadi.cashless.RetrofitServices.APIUserService;
import com.dennistjahyadi.cashless.Utils.ConstantUtils;
import com.dennistjahyadi.cashless.Utils.Encryption;
import com.dennistjahyadi.cashless.Utils.SharedPreferenceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Denn on 7/1/2017.
 */

public class LoginActivity extends AppCompatActivity {
    private TextView tvLogin, tvRegister;
    private EditText etEmail, etPassword;
    private CoordinatorLayout coordinatorLayout;
    private FrameLayout frameProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //checkAppInfo();
        SharedPreferences prefs = getSharedPreferences(SharedPreferenceUtils.SP_NAME, MODE_PRIVATE);
        String email = prefs.getString(SharedPreferenceUtils.EMAIL, null);
        String pass = prefs.getString(SharedPreferenceUtils.PASSWORD, null);
        setContentView(R.layout.login_activity);
        Typeface fontLatoLight = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");
        Typeface fontLatoRegular = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        coordinatorLayout.setVisibility(View.GONE);
        frameProgressBar = (FrameLayout) findViewById(R.id.frameProgressBar);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        if (email != null) {
            doLogin(email, pass);
        } else {
            coordinatorLayout.setVisibility(View.VISIBLE);
        }

        tvLogin.setTypeface(fontLatoRegular);
        tvRegister.setTypeface(fontLatoLight);
        etEmail.setTypeface(fontLatoLight);
        etPassword.setTypeface(fontLatoLight);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(it);
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etEmail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
                    Snackbar.make(coordinatorLayout, R.string.lit_please_fill_all_form, Snackbar.LENGTH_LONG).show();

                } else {
                    if (!ConstantUtils.isEmailValid(etEmail.getText().toString())) {
                        Snackbar.make(coordinatorLayout, R.string.lit_email_not_valid, Snackbar.LENGTH_LONG).show();
                    } else {
                        doLogin(etEmail.getText().toString(), etPassword.getText().toString());
                    }
                }
            }
        });

    }

    private void setButtonFunction(boolean bool) {
        if (bool) {
            frameProgressBar.setVisibility(View.GONE);
        } else {
            frameProgressBar.setVisibility(View.VISIBLE);
        }
        tvLogin.setEnabled(bool);
        tvRegister.setEnabled(bool);
    }

    private void checkAppInfo() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APIInfoService service = retrofit.create(APIInfoService.class);
        Call<InfoModel> call = service.getInfo("maintenance");
        call.enqueue(new Callback<InfoModel>() {
            @Override
            public void onResponse(Call<InfoModel> call, Response<InfoModel> response) {
                InfoModel infoModel = response.body();
                Log.e("AAA", "aaa");
            }

            @Override
            public void onFailure(Call<InfoModel> call, Throwable t) {
                Log.e("BBB", t.getMessage());

            }
        });
    }

    private void doLogin(final String email, final String pass) {
        setButtonFunction(false);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APIUserService service = retrofit.create(APIUserService.class);
        Call<String> call = service.doLogin(email, Encryption.encrypt(pass), "1");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("0")) {
                    coordinatorLayout.setVisibility(View.VISIBLE);

                    Snackbar.make(coordinatorLayout, R.string.lit_wrong_email_or_password, Snackbar.LENGTH_SHORT).show();
                } else if (response.body().equals("2")) {
                    coordinatorLayout.setVisibility(View.VISIBLE);

                    //do verification
                    showVerificationDialog(etEmail.getText().toString(), etPassword.getText().toString());

                } else {
                    //login success
                    moveToHome(email, pass);
                }
                setButtonFunction(true);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                setButtonFunction(true);

                Snackbar.make(coordinatorLayout, ConstantUtils.parseErrorMessage(t),
                        Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void showVerificationDialog(final String email, final String pass) {
        Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.dialog_verification);
        dialog.setTitle(R.string.lit_verification_email);
        final EditText etCode = (EditText) dialog.findViewById(R.id.etCode);
        TextView tvOk = (TextView) dialog.findViewById(R.id.tvOk);
        final TextView tvResend = (TextView) dialog.findViewById(R.id.tvResend);

        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvResend.setTextColor(Color.parseColor("#616161"));
                resendCodeVerification(email, pass);
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doVerification(email, pass, etCode.getText().toString());
            }
        });


        dialog.show();
        etEmail.setText("");
        etPassword.setText("");
    }

    private void resendCodeVerification(String email, String pass) {
        setButtonFunction(false);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APIUserService service = retrofit.create(APIUserService.class);
        Call<String> call = service.resendCodeVerification(email, Encryption.encrypt(pass), "1");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                setButtonFunction(true);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                setButtonFunction(true);

                Snackbar.make(coordinatorLayout, ConstantUtils.parseErrorMessage(t),
                        Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void doVerification(final String email, final String pass, String code) {
        setButtonFunction(false);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APIUserService service = retrofit.create(APIUserService.class);
        Call<String> call = service.doVerification(email, Encryption.encrypt(pass), code);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("0")) {
                    Snackbar.make(coordinatorLayout, R.string.lit_wrong_verification_code, Snackbar.LENGTH_LONG).show();
                } else {
                    //verification success, move to home.
                    moveToHome(email, pass);
                }
                setButtonFunction(true);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                setButtonFunction(true);

                Snackbar.make(coordinatorLayout, ConstantUtils.parseErrorMessage(t),
                        Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void moveToHome(final String email, final String pass) {
        SharedPreferences.Editor editor = getSharedPreferences(SharedPreferenceUtils.SP_NAME, MODE_PRIVATE).edit();
        editor.putString(SharedPreferenceUtils.EMAIL, email);
        editor.putString(SharedPreferenceUtils.PASSWORD, pass);
        editor.apply();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
