package com.dennistjahyadi.cashless.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dennistjahyadi.cashless.Bank.BankAccountActivity;
import com.dennistjahyadi.cashless.PersonalInformation.PersonalInformationActivity;
import com.dennistjahyadi.cashless.R;
import com.dennistjahyadi.cashless.Utils.SharedPreferenceUtils;

/**
 * Created by Denn on 10/7/2017.
 */

public class ProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tvProfileIndicator, tvFullname, tvEmail;
    private RelativeLayout layBankAccount, layPersonalInformation, layNotificationSetting, layInviteFriend,
            layHelp, layLegalAgreements, layLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

    }

    private void init() {
        tvProfileIndicator = (TextView) findViewById(R.id.tvProfileIndicator);
        tvFullname = (TextView) findViewById(R.id.tvFullname);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        layBankAccount = (RelativeLayout) findViewById(R.id.layBankAccount);
        layPersonalInformation = (RelativeLayout) findViewById(R.id.layPersonalInformation);
        layNotificationSetting = (RelativeLayout) findViewById(R.id.layNotificationSetting);
        layInviteFriend = (RelativeLayout) findViewById(R.id.layInviteFriend);
        layHelp = (RelativeLayout) findViewById(R.id.layHelp);
        layLegalAgreements = (RelativeLayout) findViewById(R.id.layLegalAgreements);
        layLogout = (RelativeLayout) findViewById(R.id.layLogout);
        SharedPreferences prefs = getSharedPreferences(SharedPreferenceUtils.SP_NAME, MODE_PRIVATE);
        String email = prefs.getString(SharedPreferenceUtils.EMAIL, null);
        String fullname = prefs.getString(SharedPreferenceUtils.FULLNAME, null);
        tvProfileIndicator.setText(fullname.substring(0, 1).toUpperCase());
        tvFullname.setText(fullname);
        tvEmail.setText(email);

        layBankAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), BankAccountActivity.class);
                startActivity(i);
            }
        });

        layPersonalInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PersonalInformationActivity.class);
                startActivity(i);
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
