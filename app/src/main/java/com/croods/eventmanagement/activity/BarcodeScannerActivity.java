package com.croods.eventmanagement.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.croods.eventmanagement.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.CAMERA;


public class BarcodeScannerActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1021;
    @BindView(R.id.view)
    ConstraintLayout view;
    @BindView(R.id.scanner_view)
    CodeScannerView scannerView;
    Bundle b;
    int eventId;
    Date mainDate;
    int storeId;
    String employeeId, employeeName, date, driverName, transporter, transportId, vehicleNumber, driverMobNo, note, jobcode, barcode, isscan,received;
    private CodeScanner mCodeScanner;
    private Context ctx = this;

    @SuppressLint("Assert")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        ButterKnife.bind(this);
        b = getIntent().getExtras();

        if (b != null) {
            received = b.getString("received");
            eventId = b.getInt("eventId");
            jobcode = b.getString("jobcode");
            barcode = b.getString("barcode");
            isscan = b.getString("isscan");
            employeeId = b.getString("employeeId");
            storeId = b.getInt("storeId");
            employeeName = b.getString("employeeName");
            date = b.getString("date");
            driverName = b.getString("driverName");
            transportId = b.getString("transportId");
            transporter = b.getString("transporter");
            vehicleNumber = b.getString("vehicleNumber");
            driverName = b.getString("driverName");
            driverMobNo = b.getString("driverMobNo");
            note = b.getString("note");
        }
        mCodeScanner = new CodeScanner(this, scannerView);

        if (checkPermission()) {

            mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
                Log.d("--------------------", result.getText());
                Intent i = null;
                if (received.equals("true"))
                {
                    i = new Intent(ctx, ProductReceivedActivity.class);
                    i.putExtra("received","true");
                }
                else
                {
                    i = new Intent(ctx, AddProdcutActivity.class);
                    i.putExtra("received","false");
                }
                i.putExtra("eventId",eventId);
                i.putExtra("jobcode",jobcode);
                i.putExtra("barcode",result.getText());
                i.putExtra("isscan","yes");
                i.putExtra("employeeId",employeeId);
                i.putExtra("storeId",storeId);
                i.putExtra("employeeName",employeeName);
                i.putExtra("date",date);
                i.putExtra("driverName",driverName);
                i.putExtra("transportId",transportId);
                i.putExtra("transporter",transporter);
                i.putExtra("vehicleNumber",vehicleNumber);
                i.putExtra("driverName",driverName);
                i.putExtra("driverMobNo",driverMobNo);
                i.putExtra("note",note);
                startActivity(i);
                finish();
            }));
            scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
        } else {
            requestPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermission())
            mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (checkPermission()) {
            mCodeScanner.releaseResources();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result == PackageManager.PERMISSION_GRANTED /*&& result1 == PackageManager.PERMISSION_GRANTED*/;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
                        Toast.makeText(ctx, result.getText(), Toast.LENGTH_SHORT).show();
                        finish();
                    }));
                    scannerView.setOnClickListener(view -> mCodeScanner.startPreview());

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(CAMERA)) {
                            showMessageOKCancel("You need to allow permissions for barcode scan",
                                    (dialog, which) -> {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{CAMERA},
                                                    PERMISSION_REQUEST_CODE);
                                        }
                                    }, (dialogInterface, i) -> {
                                        Snackbar.make(view, "Permission Denied, You cannot access camera.", Snackbar.LENGTH_LONG).show();
                                        finish();
                                    });
                            return;
                        }
                    }

                }

                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(ctx)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", cancelListener)
                .create()
                .show();
    }


}
