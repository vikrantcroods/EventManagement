package com.croods.eventmanagement.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.adapter.EmployeeAdapter;
import com.croods.eventmanagement.adapter.MaterialProductAdapter;
import com.croods.eventmanagement.adapter.TransportAdapter;
import com.croods.eventmanagement.api.APIInterface;
import com.croods.eventmanagement.api.Common;
import com.croods.eventmanagement.api.DataStorage;
import com.croods.eventmanagement.api.UtilApi;
import com.croods.eventmanagement.local_data_manage.ProductRepository;
import com.croods.eventmanagement.model.MaterialSendListResponse;
import com.croods.eventmanagement.model.SpinnerResponse;
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

public class SendMaterialActivity extends AppCompatActivity {

    @BindView(R.id.lbl_jobcode)
    TextView lbl_jobcode;
    @BindView(R.id.lbl_date)
    TextView lbl_date;

    @BindView(R.id.spn_employee)
    TextView spn_employee;
    @BindView(R.id.spn_transport)
    AppCompatSpinner spn_transport;

    @BindView(R.id.txt_vehicleNo)
    EditText txt_vehicleNo;
    @BindView(R.id.txt_driverName)
    EditText txt_driverName;
    @BindView(R.id.txt_driverMobNo)
    EditText txt_driverMobNo;
    @BindView(R.id.txt_note)
    EditText txt_note;



    @BindView(R.id.snack_sendm)
    ConstraintLayout snack_sendm;

    @BindView(R.id.btn_next_add_product)
    FloatingActionButton btn_next_add_product;

    Context ctx = this;
    Bundle b;
    String barcode = "", isscan = "";

    String token, tokenType, jobcode;
    int eventId;
    DataStorage storage, tempStorage;
    KProgressHUD progressHUD;
    APIInterface apiInterface;

    ProductRepository repository;

    ArrayList<SpinnerResponse> maintransportList, mainemployeeList;
    List<String> transportList, employeeList;

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
        setContentView(R.layout.activity_send_material);

        ButterKnife.bind(this);
        allocateMemory();

        employeeList = getEmployeeList();
        transportList = getTransportList();

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

        spn_employee.setOnClickListener(view -> setEmployeeSpinnerData());
        //spn_transport.setOnClickListener(view -> setTransportSpinnerData());

        final Handler handler = new Handler();
        handler.postDelayed(this::setTransportSpinnerData, 1000);

        spn_transport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    transportId = maintransportList.get(i).getContactId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_next_add_product.setOnClickListener(view -> {

            vehicleNumber = txt_vehicleNo.getText().toString().trim() ;
            driverMobNo = txt_driverMobNo.getText().toString().trim();
            driverName = txt_driverName.getText().toString().trim();
            note = txt_note.getText().toString().trim();
            date = lbl_date.getText().toString().trim();

            if (!employeeId.equals(" ") || transportId.equals(" ") || date.equals(" ")) {
                Intent i = new Intent(ctx, AddProdcutActivity.class);
                i.putExtra("received","false");
                i.putExtra("eventId", eventId);
                i.putExtra("jobcode", jobcode);
                i.putExtra("barcode", "");
                i.putExtra("isscan", "");
                i.putExtra("employeeId", employeeId);
                i.putExtra("employeeName", employeeName);
                i.putExtra("date", date);
                i.putExtra("driverName", driverName);
                i.putExtra("transportId", transportId);
                i.putExtra("transporter", transporter);
                i.putExtra("vehicleNumber", vehicleNumber);
                i.putExtra("driverMobNo", driverMobNo);
                i.putExtra("note", note);
                startActivity(i);
            }
            else
            {
                Common.showSnack(snack_sendm,"Require all field");
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

    private void setTransportSpinnerData() {

        ArrayAdapter adapter = new ArrayAdapter(ctx, android.R.layout.simple_spinner_item, transportList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_transport.setAdapter(adapter);
    }

    private void setEmployeeSpinnerData() {
        dialog = new Dialog(ctx, R.style.AlertDialogCustom);
        dialog.setTitle("Select Employee ");
        dialog.setContentView(R.layout.spinner_dialog);

        ListView lst = (ListView) dialog.findViewById(R.id.lst);
        TextView btn_add_employee = (TextView) dialog.findViewById(R.id.done);
        btn_add_employee.setVisibility(View.VISIBLE);

        EmployeeAdapter adapter = new EmployeeAdapter(ctx, R.layout.spinner_item, mainemployeeList);
        lst.setAdapter(adapter);

        btn_add_employee.setOnClickListener(view -> {

            String result = "";
            String result2 = "";
            for (SpinnerResponse p : adapter.getBox()) {
                if (p.isSelected()) {
                    result = result + p.getContactId() + ",";
                    result2 = result2 + p.getCompanyName() + ",";
                }
            }
            employeeId = result.substring(0, result.length() - 1);
            employeeName = result2.substring(0, result2.length() - 1);
            spn_employee.setText(result2.substring(0, result2.length() - 1));
            dialog.dismiss();
        });

        dialog.show();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        lbl_date.setText(sdf.format(myCalendar.getTime()));
        date = lbl_date.getText().toString();
    }


    private List<String> getTransportList() {
        transportList = new ArrayList<>();
        apiInterface.getTransportList(tokenType + " " + token).enqueue(new Callback<ArrayList<SpinnerResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SpinnerResponse>> call, Response<ArrayList<SpinnerResponse>> response) {
                if (response.body() != null) {
                    maintransportList = response.body();
                    for (SpinnerResponse spnresponse : maintransportList) {
                        transportList.add(spnresponse.getCompanyName());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SpinnerResponse>> call, Throwable t) {

            }
        });

        return transportList;
    }

    private List<String> getEmployeeList() {
        employeeList = new ArrayList<>();
        apiInterface.getEmployeeList(tokenType + " " + token).enqueue(new Callback<ArrayList<SpinnerResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SpinnerResponse>> call, Response<ArrayList<SpinnerResponse>> response) {
                if (response.body() != null) {
                    mainemployeeList = response.body();

                    employeeList.add(0, "Select Employee");

                    for (SpinnerResponse spnresponse : mainemployeeList) {
                        employeeList.add(spnresponse.getCompanyName());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SpinnerResponse>> call, Throwable t) {

            }
        });

        return employeeList;
    }

    private void allocateMemory() {

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
