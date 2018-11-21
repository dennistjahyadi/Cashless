package com.dennistjahyadi.cashless.Barcode;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dennistjahyadi.cashless.R;
import com.dennistjahyadi.cashless.Utils.ConstantUtils;
import com.dennistjahyadi.cashless.Utils.Encryption;
import com.dennistjahyadi.cashless.Utils.SharedPreferenceUtils;
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Denn on 10/1/2017.
 */

public class BarcodeActivity extends AppCompatActivity {

    private ImageView ivBarcode;
    private Toolbar toolbar;
    private TextView tvBtnShareBarcode, tvBtnSaveBarcode, tvBtnBarcodeList, tvDescription, tvAmount;
    private CoordinatorLayout coordinatorLayout;
    private RelativeLayout layDescription, layAmount;
    private static Integer MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1001;
    private static Integer MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1002;
    private String type; //transfer, business

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        type = getIntent().getStringExtra("type");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layDescription = (RelativeLayout) findViewById(R.id.layDescription);
        layAmount = (RelativeLayout) findViewById(R.id.layAmount);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        ivBarcode = (ImageView) findViewById(R.id.ivBarcode);
        tvBtnShareBarcode = (TextView) findViewById(R.id.tvBtnShareBarcode);
        tvBtnSaveBarcode = (TextView) findViewById(R.id.tvBtnSaveBarcode);
        tvBtnBarcodeList = (TextView) findViewById(R.id.tvBtnSavedBarcodeList);
        tvBtnBarcodeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SavedBarcodeListActivity.class);
                startActivity(i);
            }
        });
        generateBarcode(width * 80 / 100);
    }

    private void generateBarcode(int width) {
        SharedPreferences prefs = getSharedPreferences(SharedPreferenceUtils.SP_NAME, MODE_PRIVATE);
        String userId = prefs.getString(SharedPreferenceUtils.ID, null);
        String email = prefs.getString(SharedPreferenceUtils.EMAIL, null);
        String fullname = prefs.getString(SharedPreferenceUtils.FULLNAME, null);
        JSONObject jsonContent = new JSONObject();
        try {
            if (type.equals("transfer")) {
                layDescription.setVisibility(View.GONE);
                layAmount.setVisibility(View.GONE);
                tvBtnBarcodeList.setVisibility(View.VISIBLE);
                jsonContent.put("userId", userId);
                //jsonContent.put("email", email);
                //jsonContent.put("fullname", fullname);
                jsonContent.put("&^)H\"aG@Vd({ds}:_=", "transfer");
            } else {
                layDescription.setVisibility(View.VISIBLE);
                layAmount.setVisibility(View.VISIBLE);
                tvDescription.setText(getIntent().getStringExtra("businessDesc"));
                tvAmount.setText(getIntent().getIntExtra("businessAmount", 0) + "");
                tvBtnBarcodeList.setVisibility(View.GONE);
                jsonContent.put("businessId", getIntent().getStringExtra("businessId"));
                jsonContent.put("&^)H\"aG@Vd({ds}:_=", "business");
                getSupportActionBar().setTitle(getIntent().getStringExtra("businessName"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String barcodeContent = jsonContent.toString();

        try {
            Bitmap bitmap = ConstantUtils.encodeAsBitmap(getApplicationContext(), Encryption.encrypt(barcodeContent), width);
            ivBarcode.setImageBitmap(bitmap);
            checkPermission();
            storeImage(bitmap);
            setButtonShareAndSave(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void setButtonShareAndSave(final Bitmap bitmap) {
        checkPermission();
        tvBtnShareBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    File f;
                    if (type.equals("transfer")) {
                        f = new File(Environment.getExternalStorageDirectory()
                                + "/Android/data/"
                                + getApplicationContext().getPackageName()
                                + "/Files/cashless_barcode.jpg");
                    } else {
                        f = new File(Environment.getExternalStorageDirectory()
                                + "/Android/data/"
                                + getApplicationContext().getPackageName()
                                + "/Files/" + getIntent().getStringExtra("businessName").replaceAll(" ", "_") + ".jpg");
                    }

                    Uri uri = Uri.parse("file://" + f.getAbsolutePath());
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    share.setType("image/*");
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(share, "Share image File"));
                }
            }
        });

        tvBtnSaveBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    saveImageToExternal(bitmap);
                }
            }
        });

    }

    private boolean checkPermission() {
        if (writeExternalStorage() && readExternalStorage()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean writeExternalStorage() {
        if (ContextCompat.checkSelfPermission(BarcodeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

         /*   if (ActivityCompat.shouldShowRequestPermissionRationale(BarcodeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {


            } else {*/


            ActivityCompat.requestPermissions(BarcodeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);


            // }
        } else {
            return true;
        }
        return false;

    }

    private boolean readExternalStorage() {
        if (ContextCompat.checkSelfPermission(BarcodeActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

          /*  if (ActivityCompat.shouldShowRequestPermissionRationale(BarcodeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {


            } else {*/

            ActivityCompat.requestPermissions(BarcodeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            //  }
        } else {
            return true;
        }
        return false;
    }

    private void storeImage(Bitmap image) {
        String root = Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files";
        File myDir = new File(root);
        myDir.mkdirs();
        String fname;
        if (type.equals("transfer")) {
            fname = "cashless_barcode.jpg";
        } else {
            fname = getIntent().getStringExtra("businessName").replaceAll(" ", "_") + ".jpg";
        }
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, new String[]{"image/*"}, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveImageToExternal(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/DCIM/cashless");
        myDir.mkdirs();
        String fname;
        if (type.equals("transfer")) {
            fname = "cashless_barcode.jpg";
        } else {
            fname = getIntent().getStringExtra("businessName").replaceAll(" ", "_") + ".jpg";
        }
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            MediaScannerConnection.scanFile(BarcodeActivity.this, new String[]{file.getAbsolutePath()}, null, null);

            Snackbar.make(coordinatorLayout, "Image saved!", Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

}
