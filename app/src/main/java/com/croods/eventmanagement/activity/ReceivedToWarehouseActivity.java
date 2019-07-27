package com.croods.eventmanagement.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.adapter.MaterialProductAdapter;
import com.croods.eventmanagement.api.APIInterface;
import com.croods.eventmanagement.api.Common;
import com.croods.eventmanagement.api.DataStorage;
import com.croods.eventmanagement.api.UtilApi;
import com.croods.eventmanagement.local_data_manage.LocalProductModel;
import com.croods.eventmanagement.local_data_manage.ProductRepository;
import com.croods.eventmanagement.model.SpinnerResponse;
import com.croods.eventmanagement.model.StoreListResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceivedToWarehouseActivity extends AppCompatActivity {

    @BindView(R.id.lbl_jobcode)
    TextView lbl_jobcode;

    @BindView(R.id.lbl_date)
    TextView lbl_date;

    @BindView(R.id.tool_rtow)
    Toolbar tool_rtow;

    @BindView(R.id.spn_employee)
    AppCompatSpinner spn_employee;

    @BindView(R.id.spn_transport)
    AppCompatSpinner spn_transport;

    @BindView(R.id.spn_store)
    AppCompatSpinner spn_store;

    @BindView(R.id.txt_vehicleNo)
    EditText txt_vehicleNo;
    @BindView(R.id.txt_driverName)
    EditText txt_driverName;
    @BindView(R.id.txt_driverMobNo)
    EditText txt_driverMobNo;
    @BindView(R.id.txt_note)
    EditText txt_note;

    @BindView(R.id.snack_receivedw)
    ConstraintLayout snack_receivedw;

    @BindView(R.id.btn_next_add_product)
    FloatingActionButton btn_next_add_product;

    Context ctx = this;
    Bundle b;
    String barcode = "", isscan = "";

    String token, tokenType, jobcode;
    int eventId, storeId;
    DataStorage storage, tempStorage;
    KProgressHUD progressHUD;
    APIInterface apiInterface;

    ProductRepository repository;

    List<LocalProductModel> listProduct;
    ArrayList<SpinnerResponse> maintransportList, mainemployeeList;
    ArrayList<StoreListResponse> mainstoreList;
    List<String> transportList, employeeList, storeList;

    MaterialProductAdapter adapter;
    Calendar myCalendar;
    int size;
    List<Long> materialOutwardItemVos;
    Date mainDate;
    String employeeId = " ", employeeName = " ", date = " ", driverName = " ", transporter = " ", transportId = " ", vehicleNumber = " ", driverMobNo = " ", note = " ";
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_to_warehouse);

        ButterKnife.bind(this);
        allocateMemory();
        setSupportActionBar(tool_rtow);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setDate();

        b = getIntent().getExtras();

        if (b != null) {
            eventId = b.getInt("eventId");
            jobcode = b.getString("jobcode");
            lbl_jobcode.setText(b.getString("jobcode"));
            isscan = b.getString("isscan");
        }


        lbl_jobcode.setText(jobcode);
       /* txt_vehicleNo.setText(vehicleNumber);
        txt_driverName.setText(driverName);
        txt_driverMobNo.setText(driverMobNo);
        txt_note.setText(note);
        lbl_date.setText(date);
        spn_transport.setText(transporter);
        spn_employee.setText(employeeName);*/


        //repository.deleteAllTask();
        setDate();

        setTransportSpinnerData();
        setEmployeeSpinnerData();
        setStoreData();

        btn_next_add_product.setOnClickListener(view -> {

            vehicleNumber = txt_vehicleNo.getText().toString().trim();
            driverMobNo = txt_driverMobNo.getText().toString().trim();
            driverName = txt_driverName.getText().toString().trim();
            note = txt_note.getText().toString().trim();
            date = lbl_date.getText().toString().trim();

            if (!employeeId.equals(" ") || transportId.equals(" ") || date.equals(" ")) {
                Intent i = new Intent(ctx, ProductReceivedActivity.class);
                i.putExtra("eventId", eventId);
                i.putExtra("jobcode", jobcode);
                i.putExtra("barcode", "");
                i.putExtra("isscan", "");
                i.putExtra("employeeId", employeeId);
                i.putExtra("storeId", storeId);
                i.putExtra("employeeName", employeeName);
                i.putExtra("date", date);
                i.putExtra("driverName", driverName);
                i.putExtra("transportId", transportId);
                i.putExtra("transporter", transporter);
                i.putExtra("vehicleNumber", vehicleNumber);
                i.putExtra("driverMobNo", driverMobNo);
                i.putExtra("note", note);
                startActivity(i);
            } else {
                Common.showSnack(snack_receivedw, "Require all field");
            }

        });
    }

    private void setStoreData() {
        storeList = new ArrayList<>();
        apiInterface.getAllStoreList(tokenType + " " + token).enqueue(new Callback<ArrayList<StoreListResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<StoreListResponse>> call, Response<ArrayList<StoreListResponse>> response) {
                if (response.body() != null) {
                    mainstoreList = response.body();
                    for (StoreListResponse spnresponse : mainstoreList) {
                        storeList.add(spnresponse.getStoreName());
                    }

                    ArrayAdapter adapter = new ArrayAdapter(ctx, android.R.layout.simple_spinner_item, storeList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spn_store.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StoreListResponse>> call, Throwable t) {

            }
        });


        spn_store.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                storeId = mainstoreList.get(i).getStoreId();
                spn_store.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setTransportSpinnerData() {

        transportList = new ArrayList<>();
        apiInterface.getTransportList(tokenType + " " + token).enqueue(new Callback<ArrayList<SpinnerResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SpinnerResponse>> call, Response<ArrayList<SpinnerResponse>> response) {
                if (response.body() != null) {
                    maintransportList = response.body();
                    for (SpinnerResponse spnresponse : maintransportList) {
                        transportList.add(spnresponse.getCompanyName());
                    }

                    ArrayAdapter adapter = new ArrayAdapter(ctx, android.R.layout.simple_spinner_item, transportList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spn_transport.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SpinnerResponse>> call, Throwable t) {

            }
        });


        spn_transport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                transportId = maintransportList.get(i).getContactId();
                transporter = maintransportList.get(i).getCompanyName();
                spn_transport.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setEmployeeSpinnerData() {

        employeeList = new ArrayList<>();
        apiInterface.getEmployeeList(tokenType + " " + token).enqueue(new Callback<ArrayList<SpinnerResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SpinnerResponse>> call, Response<ArrayList<SpinnerResponse>> response) {
                if (response.body() != null) {
                    mainemployeeList = response.body();
                    for (SpinnerResponse spnresponse : mainemployeeList) {
                        employeeList.add(spnresponse.getCompanyName());
                    }

                    ArrayAdapter adapter = new ArrayAdapter(ctx, android.R.layout.simple_spinner_item, employeeList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spn_employee.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SpinnerResponse>> call, Throwable t) {

            }
        });


        spn_employee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                employeeId = mainemployeeList.get(i).getContactId();
                spn_employee.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setDate() {
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        lbl_date.setOnClickListener(view -> new DatePickerDialog(ctx, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        lbl_date.setText(sdf.format(myCalendar.getTime()));
        date = lbl_date.getText().toString();
    }


    private void allocateMemory() {

        listProduct = new ArrayList<>();
        materialOutwardItemVos = new ArrayList<>();

        employeeList = new ArrayList<>();
        transportList = new ArrayList<>();

        mainemployeeList = new ArrayList<>();
        maintransportList = new ArrayList<>();

        myCalendar = Calendar.getInstance();
        apiInterface = UtilApi.getAPIService();
        storage = new DataStorage("loginPref", ctx);
        tempStorage = new DataStorage("temp", ctx);
        progressHUD = Common.progressBar(ctx);

        token = (String) storage.read("token", DataStorage.STRING);
        tokenType = (String) storage.read("tokenType", DataStorage.STRING);

        repository = new ProductRepository(ctx);
    }

}
