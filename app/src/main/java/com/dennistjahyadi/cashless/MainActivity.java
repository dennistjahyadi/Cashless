package com.dennistjahyadi.cashless;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dennistjahyadi.cashless.Balance.BalanceActivity;
import com.dennistjahyadi.cashless.Barcode.BarcodeActivity;
import com.dennistjahyadi.cashless.BarcodeScanner.BarcodeCaptureActivity;
import com.dennistjahyadi.cashless.Deposit.DepositActivity;
import com.dennistjahyadi.cashless.History.HistoryActivity;
import com.dennistjahyadi.cashless.Models.User;
import com.dennistjahyadi.cashless.Payment.PaymentActivity;
import com.dennistjahyadi.cashless.Profile.ProfileActivity;
import com.dennistjahyadi.cashless.RetrofitServices.APIUserService;
import com.dennistjahyadi.cashless.Utils.ConstantUtils;
import com.dennistjahyadi.cashless.Utils.Encryption;
import com.dennistjahyadi.cashless.Utils.SharedPreferenceUtils;
import com.dennistjahyadi.cashless.Withdraw.WithdrawActivity;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private RelativeLayout layManageBalance, layViewActivity, layProfile;
    private LinearLayout layAcceptPayment, layPayOrSend, layWithdraw;
    private TextView tvFullname, tvEmail, tvProfileIndicator;
    private FrameLayout frameProgressBar;
    private CoordinatorLayout coordinatorLayout;
    private static final int RC_BARCODE_CAPTURE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        tvProfileIndicator = (TextView) findViewById(R.id.tvProfileIndicator);
        layProfile = (RelativeLayout) findViewById(R.id.layProfile);
        layManageBalance = (RelativeLayout) findViewById(R.id.layManageBalance);
        layViewActivity = (RelativeLayout) findViewById(R.id.layViewActivity);
        layAcceptPayment = (LinearLayout) findViewById(R.id.layAcceptPayment);
        layWithdraw = (LinearLayout) findViewById(R.id.layWithdraw);
        layPayOrSend = (LinearLayout) findViewById(R.id.layPayOrSend);
        tvFullname = (TextView) findViewById(R.id.tvFullname);
        tvEmail = (TextView) findViewById(R.id.tvEmail);

        frameProgressBar = (FrameLayout) findViewById(R.id.frameProgressBar);

        new Thread(new Runnable() {
            public void run() {
                SharedPreferences prefs = SharedPreferenceUtils.getPrefs(getApplicationContext());
                String email = prefs.getString(SharedPreferenceUtils.EMAIL, null);
                String pass = prefs.getString(SharedPreferenceUtils.PASSWORD, null);
                String fullname = prefs.getString(SharedPreferenceUtils.FULLNAME, null);
                // String userId = prefs.getString(SharedPreferenceUtils.ID, null);
                // Integer userBalance = prefs.getInt(SharedPreferenceUtils.BALANCE, -1);
                tvEmail.setText(email);

                if (fullname != null) {
                    tvFullname.setText(fullname);
                    tvProfileIndicator.setText(fullname.substring(0, 1).toUpperCase());
                }
                turnOnOffButton(false);
                getUserInfo(email, pass);
            }
        }).start();


        layProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(i);
            }
        });

        layManageBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), BalanceActivity.class);
                startActivity(i);
            }
        });
        layViewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(i);
            }
        });
        layAcceptPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), BarcodeActivity.class);
                i.putExtra("type", "transfer");
                startActivity(i);
            }
        });

        layPayOrSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }
        });

        layWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), WithdrawActivity.class);
                startActivity(i);
            }
        });
    }

    private void turnOnOffButton(boolean allow) {
        if (allow) {
            frameProgressBar.setVisibility(View.GONE);
        } else {
            frameProgressBar.setVisibility(View.VISIBLE);
        }
        layProfile.setEnabled(allow);
        layManageBalance.setEnabled(allow);
        layViewActivity.setEnabled(allow);
        layAcceptPayment.setEnabled(allow);
        layPayOrSend.setEnabled(allow);
    }

    private void getUserInfo(String email, String pass) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APIUserService service = retrofit.create(APIUserService.class);
        Call<User> call = service.getUserInfo(email, Encryption.encrypt(pass), "a");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null) {
                    SharedPreferences.Editor prefsEditor = SharedPreferenceUtils.getPrefsEditor(getApplicationContext());
                    prefsEditor.putInt(SharedPreferenceUtils.BALANCE, response.body().getBalance());
                    prefsEditor.putString(SharedPreferenceUtils.ID, response.body().getId());
                    prefsEditor.putString(SharedPreferenceUtils.FULLNAME, response.body().getFullname());
                    prefsEditor.putString(SharedPreferenceUtils.PHONE_NO, response.body().getPhone_no());
                    prefsEditor.putString(SharedPreferenceUtils.ADDRESS, response.body().getAddress());
                    prefsEditor.apply();

                    tvFullname.setText(response.body().getFullname());
                    tvProfileIndicator.setText(response.body().getFullname().substring(0, 1).toUpperCase());
                    turnOnOffButton(true);

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                turnOnOffButton(false);
                Snackbar.make(coordinatorLayout, ConstantUtils.parseErrorMessage(t),
                        Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

                    String value = Encryption.decrypt(barcode.displayValue);

                    try {
                        JSONObject jsonObject = new JSONObject(value);
                        Intent i = new Intent(getApplicationContext(), PaymentActivity.class);
                        i.putExtra("type", data.getStringExtra("type"));
                        if (data.getStringExtra("type").equals("transfer")) {
                            i.putExtra("userIdReceiver", jsonObject.getString("userId"));
                        } else if (data.getStringExtra("type").equals("business")) {
                            i.putExtra("businessId", jsonObject.getString("businessId"));
                        }
                        startActivity(i);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                }
            } else {
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

      /*  if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
