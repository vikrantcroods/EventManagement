package com.croods.eventmanagement.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.api.APIInterface;
import com.croods.eventmanagement.api.Common;
import com.croods.eventmanagement.api.DataStorage;
import com.croods.eventmanagement.api.UtilApi;
import com.croods.eventmanagement.model.CustomerDetailResponse;
import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerDetailActivity extends AppCompatActivity {


    @BindView(R.id.tool_customer_detail)
    Toolbar tool_customer_detail;

    @BindView(R.id.txt_customer_detail)
    TextView txt_customer_detail;

    @BindView(R.id.lbl_customer_name)
    TextView lbl_customer_name;

    @BindView(R.id.lbl_customer_mob)
    TextView lbl_customer_mob;

    @BindView(R.id.lbl_customer_email)
    TextView lbl_customer_email;

    @BindView(R.id.lbl_customer_addr)
    TextView lbl_customer_addr;

    @BindView(R.id.lbl_contact_name)
    TextView lbl_contact_name;

    @BindView(R.id.lbl_contact_mob)
    TextView lbl_contact_mob;


    Context ctx = this;

    APIInterface apiInterface;

    String token, tokenType;
    DataStorage storage;
    KProgressHUD progressHUD;
    int contactId;

    Bundle b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        ButterKnife .bind(this);

        b = getIntent().getExtras();

        if (b != null)
            contactId = b.getInt("contactId");
        allocateMemory();

        final Handler handler = new Handler();
        progressHUD.show();
        handler.postDelayed(() -> getCustomerDetail(tokenType + " " + token), 2000);
    }


    private void getCustomerDetail(String auth) {

        apiInterface.getCustomerDetail(contactId, auth).enqueue(new Callback<CustomerDetailResponse>() {
            @Override
            public void onResponse(Call<CustomerDetailResponse> call, Response<CustomerDetailResponse> response) {

                if (response.body() != null) {
                    CustomerDetailResponse activity = response.body();
                    String address,contactName,contactMob;

                    address =activity.getAddressLine1() + " " + activity.getAddressLine2() + " " + activity.getCityName() + " " + activity.getStateName() ;
                    contactName = activity.getConcernPersonName();
                    contactMob = activity.getConcernPersonMobileno();
                    lbl_customer_name.setText(activity.getCompanyName());
                    lbl_customer_mob.setText(activity.getCompanyMobileno());
                    lbl_customer_email.setText(activity.getCompanyEmail());
                    lbl_customer_addr.setText(address);

                    if (contactMob.equals("") || contactMob == null)
                        lbl_contact_mob.setText("-");
                    else
                        lbl_contact_mob.setText("  "+activity.getConcernPersonMobileno());
                    if (contactName.equals("") || contactName == null)
                        lbl_contact_name.setText("-");
                    else
                        lbl_contact_name.setText("  "+activity.getConcernPersonName());

                    progressHUD.dismiss();
                }
                else
                {
                    progressHUD.dismiss();
                    Common.showToast(ctx,"Please try again latter");
                }
            }

            @Override
            public void onFailure(Call<CustomerDetailResponse> call, Throwable t) {
                progressHUD.dismiss();
            }
        });
    }

    private void allocateMemory() {
        apiInterface = UtilApi.getAPIService();
        storage = new DataStorage("loginPref", ctx);
        progressHUD = Common.progressBar(ctx);

        token = (String) storage.read("token", DataStorage.STRING);
        tokenType = (String) storage.read("tokenType", DataStorage.STRING);

    }
}
