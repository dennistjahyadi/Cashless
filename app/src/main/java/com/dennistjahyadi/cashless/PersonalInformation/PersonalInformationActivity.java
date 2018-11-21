package com.dennistjahyadi.cashless.PersonalInformation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dennistjahyadi.cashless.R;
import com.dennistjahyadi.cashless.Utils.SharedPreferenceUtils;
import com.dennistjahyadi.cashless.Utils.TypeFaceManager;

/**
 * Created by Denn on 10/16/2017.
 */

public class PersonalInformationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvAngleRightFullname, tvAngleRightEmail, tvAngleRightPhoneNo, tvAngleRightAddress,
            tvFullname, tvEmail, tvPhoneNo, tvAddress;
    private LinearLayout layFullname, layEmail, layPhoneNo, layAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        init();

    }


    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvAngleRightFullname = (TextView) findViewById(R.id.tvAngleRightFullname);
        tvAngleRightEmail = (TextView) findViewById(R.id.tvAngleRightEmail);
        tvAngleRightPhoneNo = (TextView) findViewById(R.id.tvAngleRightPhoneNo);
        tvAngleRightAddress = (TextView) findViewById(R.id.tvAngleRightAddress);
        layFullname = (LinearLayout) findViewById(R.id.layFullname);
        layEmail = (LinearLayout) findViewById(R.id.layEmail);
        layPhoneNo = (LinearLayout) findViewById(R.id.layPhoneNo);
        layAddress = (LinearLayout) findViewById(R.id.layAddress);
        tvFullname = (TextView) findViewById(R.id.tvFullname);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPhoneNo = (TextView) findViewById(R.id.tvPhoneNo);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        Typeface tfFontAwesome = TypeFaceManager.getFontAwesomeTypeface(this);
        tvAngleRightFullname.setTypeface(tfFontAwesome);
        tvAngleRightEmail.setTypeface(tfFontAwesome);
        tvAngleRightPhoneNo.setTypeface(tfFontAwesome);
        tvAngleRightAddress.setTypeface(tfFontAwesome);

        layFullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToEditor("fullname");
            }
        });
        layEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToEditor("email");
            }
        });
        layPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToEditor("phone_no");
            }
        });
        layAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToEditor("address");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = SharedPreferenceUtils.getPrefs(this);
        tvFullname.setText(prefs.getString(SharedPreferenceUtils.FULLNAME, ""));
        tvEmail.setText(prefs.getString(SharedPreferenceUtils.EMAIL, ""));
        tvPhoneNo.setText(prefs.getString(SharedPreferenceUtils.PHONE_NO, ""));
        tvAddress.setText(prefs.getString(SharedPreferenceUtils.ADDRESS, ""));
    }

    private void moveToEditor(String columnEdit) {
        Intent i = new Intent(getApplicationContext(), PersonalInformationEditorActivity.class);
        i.putExtra("edit", columnEdit);
        startActivity(i);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }
}
