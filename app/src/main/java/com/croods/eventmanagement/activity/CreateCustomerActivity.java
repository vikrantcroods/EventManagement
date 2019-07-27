package com.croods.eventmanagement.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

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
import com.croods.eventmanagement.model.CustomerAddRequest;
import com.croods.eventmanagement.model.CustomerListResponse;
import com.croods.eventmanagement.model.SpinnerResponse;
import com.croods.eventmanagement.model.StateResponse;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCustomerActivity extends AppCompatActivity {

    @BindView(R.id.snack_cus)
    ConstraintLayout snack_cus;

    @BindView(R.id.tool_create_customer)
    Toolbar tool_create_customer;

    @BindView(R.id.txt_companyname)
    EditText txt_companyname;

    @BindView(R.id.txt_shortname)
    EditText txt_shortname;

    @BindView(R.id.txt_mono)
    EditText txt_mono;

    @BindView(R.id.txt_email)
    EditText txt_email;

    @BindView(R.id.txt_ownername)
    EditText txt_ownername;

    @BindView(R.id.txt_ownermono)
    EditText txt_ownermono;

    @BindView(R.id.txt_panno)
    EditText txt_panno;

    @BindView(R.id.txt_concernpname)
    EditText txt_concernpname;

    @BindView(R.id.txt_concernpmono)
    EditText txt_concernpmono;

    @BindView(R.id.txt_concernpemail)
    EditText txt_concernpemail;

    @BindView(R.id.txt_companyname_address)
    EditText txt_companyname_address;

    @BindView(R.id.txt_postalcode)
    EditText txt_postalcode;

    @BindView(R.id.txt_addr1)
    EditText txt_addr1;

    @BindView(R.id.txt_addr2)
    EditText txt_addr2;

    @BindView(R.id.spn_state)
    AppCompatSpinner spn_state;

    @BindView(R.id.spn_city)
    AppCompatSpinner spn_city;

    @BindView(R.id.btn_customer_submit)
    Button btn_customer_submit;

    DataStorage storage;
    KProgressHUD progressHUD;
    APIInterface apiInterface;
    String token, tokenType;
    Context ctx = this;

    List<StateResponse> lstState;
    List<CityResponse> lstCity;
    List<String> spnCityList, spnStateList;

    String companyMobileno,shortName,companyName,stateCode,cityCode,companyEmail,ownerName,ownerMobileno,concernPersonName,concernPersonMobileno,concernPersonEmail,panNo,addressLine1,addressLine2,pinCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_customer);

        ButterKnife.bind(this);
        allocateMemory();

        setStateSpinnerList();

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


        btn_customer_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                companyMobileno = txt_mono.getText().toString();
                shortName = txt_shortname.getText().toString();
                companyName = txt_companyname.getText().toString();
                companyEmail = txt_email.getText().toString();
                ownerName = txt_ownername.getText().toString();
                ownerMobileno = txt_ownermono.getText().toString();
                panNo = txt_panno.getText().toString();
                concernPersonName = txt_concernpname.getText().toString();
                concernPersonMobileno = txt_concernpmono.getText().toString();
                concernPersonEmail = txt_concernpemail.getText().toString();
                addressLine1 = txt_addr1.getText().toString();
                addressLine2 = txt_addr2.getText().toString();
                pinCode = txt_postalcode.getText().toString();

                progressHUD.show();
                final Handler handler = new Handler();
                handler.postDelayed(() -> {

                if (!companyName.equals("") || !shortName.equals("") || !companyMobileno.equals("") || !stateCode.equals("") || !cityCode.equals(""))
                {
                    CustomerAddRequest request = new CustomerAddRequest(0,companyName,shortName,companyMobileno,companyEmail,ownerName,ownerMobileno,concernPersonName,concernPersonMobileno,concernPersonEmail,panNo,addressLine1,addressLine2,stateCode,cityCode,pinCode);
                    apiInterface.saveCustomer(tokenType + " " +token,request).enqueue(new Callback<CustomerAddRequest>() {
                        @Override
                        public void onResponse(Call<CustomerAddRequest> call, Response<CustomerAddRequest> response) {

                            progressHUD.dismiss();
                            if (response.body()!=null)
                            {
                                Common.showToast(ctx,"Customer Created successfully");

                                Intent i = new Intent(ctx,DashBoardActivity.class);
                                i.putExtra("loadf",2);
                                startActivity(i);
                            }
                            else
                            {
                                Common.showSnack(snack_cus,"PLease try again latter");
                                Intent i = new Intent(ctx,DashBoardActivity.class);
                                i.putExtra("loadf",2);
                                startActivity(i);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<CustomerAddRequest> call, Throwable t) {
                            progressHUD.dismiss();
                            Common.showSnack(snack_cus,"Server error...Please try again latter");
                        }
                    });
                }

                }, 2000);



            }
        });


    }

    private void allocateMemory() {

        apiInterface = UtilApi.getAPIService();
        storage = new DataStorage("loginPref", ctx);
        progressHUD = Common.progressBar(ctx);

        token = (String) storage.read("token", DataStorage.STRING);
        tokenType = (String) storage.read("tokenType", DataStorage.STRING);


        lstState = new ArrayList<>();
        spnStateList = new ArrayList<>();
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

}
