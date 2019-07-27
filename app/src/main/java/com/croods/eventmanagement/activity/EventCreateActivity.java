package com.croods.eventmanagement.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.api.APIInterface;
import com.croods.eventmanagement.api.Common;
import com.croods.eventmanagement.api.DataStorage;
import com.croods.eventmanagement.api.UtilApi;
import com.croods.eventmanagement.model.CityResponse;
import com.croods.eventmanagement.model.CustomerListResponse;
import com.croods.eventmanagement.model.EventListResponse;
import com.croods.eventmanagement.model.SpinnerResponse;
import com.croods.eventmanagement.model.StateResponse;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventCreateActivity extends AppCompatActivity {

    @BindView(R.id.tool_ecreate)
    Toolbar tool_ecreate;

    @BindView(R.id.txt_ecreate)
    TextView txt_ecreate;

    @BindView(R.id.btn_submit_event)
    Button btn_submit_event;

    @BindView(R.id.spn_customer)
    AppCompatSpinner spn_customer;

    @BindView(R.id.spn_employee)
    AppCompatSpinner spn_employee;

    @BindView(R.id.spn_state)
    AppCompatSpinner spn_state;

    @BindView(R.id.spn_city)
    AppCompatSpinner spn_city;

    @BindView(R.id.lbl_event_date)
    TextView lbl_event_date;

    @BindView(R.id.txt_event_venue)
    EditText txt_event_venue;

    @BindView(R.id.txt_event_name)
    EditText txt_event_name;

    @BindView(R.id.snack_cEvent)
    ConstraintLayout snack_cEvent;

    Calendar myCalendar;
    DataStorage storage, tempStorage;
    KProgressHUD progressHUD;
    APIInterface apiInterface;
    String token, tokenType, date;
    Context ctx = this;

    List<StateResponse> lstState;
    List<CityResponse> lstCity;
    List<CustomerListResponse> customerList;
    ArrayList<SpinnerResponse> employeeList;

    List<String> spnCityList, spnStateList,spnCustomerList,spnemployeeList;

    String eventDate,venue,eventName,stateCode,cityCode;
    int customerId,employeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        ButterKnife.bind(this);

        allocateMemory();
        setDate();

        setCustomerList();

        setStateSpinnerList();

        setEmployeeList();

        spn_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String id = lstState.get(i).getStateCode();
                stateCode = id;
                setCitySpinnerList(id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spn_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityCode = lstCity.get(i).getCityCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spn_customer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                customerId = customerList.get(i).getContactId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spn_employee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                employeeId = Integer.parseInt(employeeList.get(i).getContactId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_submit_event.setOnClickListener(view -> {

            venue = txt_event_venue.getText().toString();
            eventDate = lbl_event_date.getText().toString();
            eventName = txt_event_name.getText().toString();

            if (!venue.equals("") || !eventDate.equals("") || !eventName.equals(""))
            {
                apiInterface.saveEvent(new EventListResponse(customerId,employeeId,cityCode,stateCode,eventDate,venue,eventName),tokenType +" "+token).enqueue(new Callback<EventListResponse>() {
                    @Override
                    public void onResponse(Call<EventListResponse> call, Response<EventListResponse> response) {
                        if (response.body()!=null)
                        {
                            Common.showToast(ctx,"Event Create Successfully");
                            Intent i = new Intent(ctx,DashBoardActivity.class);
                            i.putExtra("loadf",4);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            Common.showToast(ctx,"Please try again latter");
                        }
                    }

                    @Override
                    public void onFailure(Call<EventListResponse> call, Throwable t) {
                        Common.showToast(ctx,"Server error....Please try again latter");
                    }
                });
            }
            else
            {
                Common.showSnack(snack_cEvent,"All fields are require");
            }
        });

    }

    private void setCustomerList()
    {
        customerList = new ArrayList<>();
        progressHUD.show();
        apiInterface.getAllCustomer(tokenType+ " " +token).enqueue(new Callback<List<CustomerListResponse>>() {
            @Override
            public void onResponse(Call<List<CustomerListResponse>> call, Response<List<CustomerListResponse>> response)
            {

                if (response.body()!=null)
                {
                    customerList = response.body();
                    for (CustomerListResponse model : customerList) {
                        spnCustomerList.add(model.getCompanyName());
                    }
                    ArrayAdapter adapter = new ArrayAdapter(ctx, android.R.layout.simple_spinner_item, spnCustomerList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spn_customer.setAdapter(adapter);
                    progressHUD.dismiss();
                }
                else
                {
                    progressHUD.dismiss();
                    Common.showToast(ctx,"Please try again latter");
                }
            }

            @Override
            public void onFailure(Call<List<CustomerListResponse>> call, Throwable t)
            {
                progressHUD.dismiss();
                Common.showToast(ctx,"Server error Please try again latter");
            }
        });
    }

    private void setEmployeeList()
    {
        employeeList = new ArrayList<>();
        progressHUD.show();
        apiInterface.getEmployeeList(tokenType+ " " +token).enqueue(new Callback<ArrayList<SpinnerResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SpinnerResponse>> call, Response<ArrayList<SpinnerResponse>> response)
            {

                if (response.body()!=null)
                {
                    employeeList = response.body();
                    for (SpinnerResponse model : employeeList) {
                        spnemployeeList.add(model.getCompanyName());
                    }
                    ArrayAdapter adapter = new ArrayAdapter(ctx, android.R.layout.simple_spinner_item, spnemployeeList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spn_employee.setAdapter(adapter);
                    progressHUD.dismiss();
                }
                else
                {
                    progressHUD.dismiss();
                    Common.showToast(ctx,"Please try again latter");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SpinnerResponse>> call, Throwable t)
            {
                progressHUD.dismiss();
                Common.showToast(ctx,"Server error Please try again latter");
            }
        });
    }


    private void setStateSpinnerList() {
        progressHUD.show();
        apiInterface.getAllStateList(tokenType + " " + token).enqueue(new Callback<List<StateResponse>>() {
            @Override
            public void onResponse(Call<List<StateResponse>> call, Response<List<StateResponse>> response) {
                if (response.body() != null) {
                    lstState = response.body();
                    for (StateResponse model : lstState) {
                        spnStateList.add(model.getStateName());
                    }
                    ArrayAdapter adapter = new ArrayAdapter(ctx, android.R.layout.simple_spinner_item, spnStateList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spn_state.setAdapter(adapter);
                    progressHUD.dismiss();
                } else {
                    progressHUD.dismiss();
                }

            }

            @Override
            public void onFailure(Call<List<StateResponse>> call, Throwable t) {
                progressHUD.dismiss();
            }
        });
    }

    private void setCitySpinnerList(String id) {
        progressHUD.show();
        spnCityList = new ArrayList<>();
        lstCity = new ArrayList<>();
        apiInterface.getAllCityList(id, tokenType + " " + token).enqueue(new Callback<List<CityResponse>>() {
            @Override
            public void onResponse(Call<List<CityResponse>> call, Response<List<CityResponse>> response) {
                if (response.body() != null) {
                    lstCity = response.body();
                    for (CityResponse model : lstCity) {
                        spnCityList.add(model.getCityName());
                    }
                    ArrayAdapter adapter = new ArrayAdapter(ctx, android.R.layout.simple_spinner_item, spnCityList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spn_city.setAdapter(adapter);
                    progressHUD.dismiss();
                } else {
                    progressHUD.dismiss();
                }

            }

            @Override
            public void onFailure(Call<List<CityResponse>> call, Throwable t) {
                progressHUD.dismiss();
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

        lbl_event_date.setOnClickListener(view -> new DatePickerDialog(ctx, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        lbl_event_date.setText(sdf.format(myCalendar.getTime()));
        date = lbl_event_date.getText().toString();
    }

    private void allocateMemory() {

        myCalendar = Calendar.getInstance();
        apiInterface = UtilApi.getAPIService();
        storage = new DataStorage("loginPref", ctx);
        tempStorage = new DataStorage("temp", ctx);
        progressHUD = Common.progressBar(ctx);

        token = (String) storage.read("token", DataStorage.STRING);
        tokenType = (String) storage.read("tokenType", DataStorage.STRING);


        lstState = new ArrayList<>();
        spnStateList = new ArrayList<>();
        spnCustomerList = new ArrayList<>();
        spnemployeeList = new ArrayList<>();
    }

}
