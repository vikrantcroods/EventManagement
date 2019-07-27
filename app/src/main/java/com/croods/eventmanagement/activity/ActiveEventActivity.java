package com.croods.eventmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.adapter.MaterialSendListAdapter;
import com.croods.eventmanagement.api.APIInterface;
import com.croods.eventmanagement.api.Common;
import com.croods.eventmanagement.api.DataStorage;
import com.croods.eventmanagement.api.UtilApi;
import com.croods.eventmanagement.model.MaterialSendListResponse;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActiveEventActivity extends AppCompatActivity {

    @BindView(R.id.tool_aevent)
    Toolbar tool_aevent;

    @BindView(R.id.spn_jobcode)
    AppCompatSpinner spn_jobcode;

    @BindView(R.id.lst_active_event)
    ListView lst_active_event;

    private Context ctx=this;

    private List<MaterialSendListResponse> materialSendList;
    private  List<String> jobCodeList;
    private MaterialSendListAdapter adapter;

    private APIInterface apiInterface;

    private KProgressHUD progressHUD;

    private DataStorage storage;
    private String token,tokenType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_event);

        ButterKnife.bind(this);
        allocateMemory();

        final Handler handler = new Handler();
        progressHUD.show();
        handler.postDelayed(() ->  getAllMaterialSendList(tokenType+" "+token), 2000);

        spn_jobcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.filter(jobCodeList.get(i));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void allocateMemory() {

        jobCodeList = new ArrayList<>();
        apiInterface = UtilApi.getAPIService();
        progressHUD = Common.progressBar(ctx);
        storage = new DataStorage("loginPref",ctx);

        token = (String)storage.read("token",DataStorage.STRING);
        tokenType = (String)storage.read("tokenType",DataStorage.STRING);
    }

    private void getAllMaterialSendList(String auth)
    {
        materialSendList = new ArrayList<>();
        apiInterface.getMaterialSendList(auth).enqueue(new Callback<List<MaterialSendListResponse>>() {
            @Override
            public void onResponse(Call<List<MaterialSendListResponse>> call, Response<List<MaterialSendListResponse>> response)
            {
                if (response.body()!=null)
                {
                    materialSendList = response.body();
                    adapter = new MaterialSendListAdapter(materialSendList,ctx);
                    lst_active_event.setAdapter(adapter);

                    for (MaterialSendListResponse response1 : materialSendList)
                    {
                        if (!jobCodeList.contains(response1.getJobCode()))
                            jobCodeList.add(response1.getJobCode());
                    }

                    ArrayAdapter adapter = new ArrayAdapter(ctx, android.R.layout.simple_spinner_item, jobCodeList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spn_jobcode.setAdapter(adapter);
                    progressHUD.dismiss();
                }
                else
                {
                    progressHUD.dismiss();
                    Common.showToast(ctx,"Please try again latter");
                }
            }

            @Override
            public void onFailure(Call<List<MaterialSendListResponse>> call, Throwable t)
            {
                progressHUD.dismiss();
                Common.showToast(ctx,"Server error Please try again latter");
            }
        });
    }

}
