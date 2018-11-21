package com.dennistjahyadi.cashless.PersonalInformation;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dennistjahyadi.cashless.R;
import com.dennistjahyadi.cashless.RetrofitServices.APIUserService;
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
 * Created by Denn on 10/18/2017.
 */

public class PersonalInformationEditorActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etFullname, etEmail, etPhoneNo, etAddress;
    private RelativeLayout layFullname, layEmail, layPhoneNo, layAddress;
    private TextView tvBtnFinish;
    private String edit;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_information);

        init();
        SharedPreferences prefs = SharedPreferenceUtils.getPrefs(this);

        edit = getIntent().getStringExtra("edit");
        if (edit.equals("fullname")) {
            layFullname.setVisibility(View.VISIBLE);
            toolbar.setTitle("Change Fullname");
            etFullname.setText(prefs.getString(SharedPreferenceUtils.FULLNAME, ""));
        } else if (edit.equals("email")) {
            layEmail.setVisibility(View.VISIBLE);
            toolbar.setTitle("Change Email");
            etEmail.setText(prefs.getString(SharedPreferenceUtils.EMAIL, ""));

        } else if (edit.equals("phone_no")) {
            layPhoneNo.setVisibility(View.VISIBLE);
            toolbar.setTitle("Change Phone Number");
            etPhoneNo.setText(prefs.getString(SharedPreferenceUtils.PHONE_NO, ""));

        } else if (edit.equals("address")) {
            layAddress.setVisibility(View.VISIBLE);
            toolbar.setTitle("Change Address");
            etAddress.setText(prefs.getString(SharedPreferenceUtils.ADDRESS, ""));

        }
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        etFullname = (EditText) findViewById(R.id.etFullname);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhoneNo = (EditText) findViewById(R.id.etPhoneNo);
        etAddress = (EditText) findViewById(R.id.etAddress);
        tvBtnFinish = (TextView) findViewById(R.id.tvBtnFinish);
        layFullname = (RelativeLayout) findViewById(R.id.layFullname);
        layEmail = (RelativeLayout) findViewById(R.id.layEmail);
        layPhoneNo = (RelativeLayout) findViewById(R.id.layPhoneNo);
        layAddress = (RelativeLayout) findViewById(R.id.layAddress);
        layFullname.setVisibility(View.GONE);
        layEmail.setVisibility(View.GONE);
        layPhoneNo.setVisibility(View.GONE);
        layAddress.setVisibility(View.GONE);
        tvBtnFinish.setTypeface(TypeFaceManager.getFontAwesomeTypeface(this));

        tvBtnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit.equals("fullname")) {
                    if (!TextUtils.isEmpty(etFullname.getText())) {
                        updateUserProfile("fullname", etFullname.getText().toString());
                    } else {
                        showSnackBar();
                    }

                } else if (edit.equals("email")) {
                    if (!TextUtils.isEmpty(etEmail.getText())) {
                        updateUserProfile("email", etEmail.getText().toString());
                    } else {
                        showSnackBar();

                    }
                } else if (edit.equals("phone_no")) {
                    if (!TextUtils.isEmpty(etPhoneNo.getText())) {
                        updateUserProfile("phone_no", etPhoneNo.getText().toString());
                    } else {
                        showSnackBar();

                    }
                } else if (edit.equals("address")) {
                    if (!TextUtils.isEmpty(etAddress.getText())) {
                        updateUserProfile("address", etAddress.getText().toString());
                    } else {
                        showSnackBar();
                    }
                }
            }
        });
    }

    private void showSnackBar() {
        Snackbar.make(coordinatorLayout, "field cannot be empty", Snackbar.LENGTH_LONG).show();

    }

    private void updateUserProfile(String columnUpdated, final String updatedData) {
        final SharedPreferences prefs = SharedPreferenceUtils.getPrefs(getApplicationContext());
        String email = prefs.getString(SharedPreferenceUtils.EMAIL, null);
        String pass = prefs.getString(SharedPreferenceUtils.PASSWORD, null);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APIUserService service = retrofit.create(APIUserService.class);
        Call<String> call = service.updateUserProfile(email, Encryption.encrypt(pass), columnUpdated, updatedData);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.body().equals("1")) {
                    SharedPreferences.Editor prefsEditor = SharedPreferenceUtils.getPrefsEditor(getApplicationContext());
                    if (edit.equals("fullname")) {
                        prefsEditor.putString(SharedPreferenceUtils.FULLNAME, updatedData);
                    } else if (edit.equals("email")) {
                        prefsEditor.putString(SharedPreferenceUtils.EMAIL, updatedData);
                    } else if (edit.equals("phone_no")) {
                        prefsEditor.putString(SharedPreferenceUtils.PHONE_NO, updatedData);
                    } else if (edit.equals("address")) {
                        prefsEditor.putString(SharedPreferenceUtils.ADDRESS, updatedData);
                    }
                    prefsEditor.apply();
                    Toast.makeText(getApplicationContext(), "update complete!", Toast.LENGTH_LONG).show();
                    finish();
                } else if (response.body().equals("11")) {
                    Snackbar.make(coordinatorLayout, "Email already used!", Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

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
