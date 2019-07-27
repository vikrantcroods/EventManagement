package com.croods.eventmanagement.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.adapter.MaterialOutProductListAdapter;
import com.croods.eventmanagement.api.APIInterface;
import com.croods.eventmanagement.api.Common;
import com.croods.eventmanagement.api.DataStorage;
import com.croods.eventmanagement.api.UtilApi;
import com.croods.eventmanagement.model.MaterialOutWordEventList;
import com.croods.eventmanagement.model.ProductListResponse;
import com.croods.eventmanagement.model.ReceivedMaterialToEventRequest;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceivedToEventActivity extends AppCompatActivity {

    @BindView(R.id.spn_mout_no)
    AppCompatSpinner spn_mout_no;

    @BindView(R.id.lst_mout_product)
    ListView lst_mout_product;

    @BindView(R.id.lbl_plist)
    TextView lbl_plist;

    @BindView(R.id.tool_rtoe)
    Toolbar tool_rtoe;

    TextView txt_remark,txt_received_qty;
    Button btn_update;

    Context ctx = this;
    Bundle b;

    String token, tokenType, jobcode;
    int eventId,id;
    DataStorage storage;
    KProgressHUD progressHUD;
    APIInterface apiInterface;
    List<MaterialOutWordEventList> list;
    List<String> materialOutwordEvent;
    List<ProductListResponse> listProduct;
    List<ReceivedMaterialToEventRequest> listReq;

    Dialog dialog;
    String remark,receivedQty;
    MaterialOutProductListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_to_event);

        ButterKnife.bind(this);
        setSupportActionBar(tool_rtoe);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        b = getIntent().getExtras();
        if (b!=null)
        {
            eventId = b.getInt("eventId");
            jobcode = b.getString("jobcode");
        }

        allocateMemory();

        ListMaterialSendEvent();

        spn_mout_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                progressHUD.show();
                id = list.get(i).getMaterialOutwardId();
                apiInterface.getMoutProductList(list.get(i).getMaterialOutwardId(),tokenType + " " + token).enqueue(new Callback<List<ProductListResponse>>() {
                    @Override
                    public void onResponse(Call<List<ProductListResponse>> call, Response<List<ProductListResponse>> response) {

                        if (response.body()!=null)
                        {
                            listProduct = response.body();
                            adapter = new MaterialOutProductListAdapter(response.body(),ctx);
                            lst_mout_product.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressHUD.dismiss();
                            lbl_plist.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            progressHUD.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ProductListResponse>> call, Throwable t) {
                        progressHUD.dismiss();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lst_mout_product.setOnItemClickListener((adapterView, view, i, l) -> {

            dialog = new Dialog(ctx, R.style.AlertDialogCustom);
            dialog.setTitle("Enter Remark and Qty ");
            dialog.setContentView(R.layout.mout_product_dialog);

            txt_remark = (TextView)dialog.findViewById(R.id.txt_remark);
            txt_received_qty = (TextView)dialog.findViewById(R.id.txt_received_qty);
            btn_update = (Button) dialog.findViewById(R.id.btn_update);

            btn_update.setOnClickListener(view1 -> {

                remark = txt_remark.getText().toString();
                receivedQty = txt_received_qty.getText().toString();
                if (!remark.equals("") ||!receivedQty.equals(""))
                {
                    if (Integer.parseInt(receivedQty) > listProduct.get(i).getQuantity())
                    {
                        Common.showToast(ctx,"Received quantity is not greater than total quantity");
                    }
                    else
                    {
                        listProduct.get(i).setRquantity(Integer.parseInt(receivedQty));
                        listProduct.get(i).setRemark(remark);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                }
            });

            dialog.show();

        });

    }

    private void ListMaterialSendEvent()
    {
        progressHUD.show();
        apiInterface.getMaterialOutwordList(eventId,tokenType+" "+token).enqueue(new Callback<List<MaterialOutWordEventList>>() {
            @Override
            public void onResponse(Call<List<MaterialOutWordEventList>> call, Response<List<MaterialOutWordEventList>> response) {
                if (response.body()!=null)
                {
                    list = response.body();
                    for (MaterialOutWordEventList model : list)
                    {
                        materialOutwordEvent.add(model.getMaterialOutwardNo());
                    }
                    ArrayAdapter adapter = new ArrayAdapter(ctx, android.R.layout.simple_spinner_item, materialOutwordEvent);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spn_mout_no.setAdapter(adapter);
                    progressHUD.dismiss();
                }
                else
                {
                    progressHUD.dismiss();
                }

            }

            @Override
            public void onFailure(Call<List<MaterialOutWordEventList>> call, Throwable t) {
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

        list = new ArrayList<>();
        listProduct = new ArrayList<>();
        materialOutwordEvent = new ArrayList<>();
        listReq = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rtoe_product_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.img_send:
                progressHUD.show();
                for (ProductListResponse response:listProduct)
                {
                    ReceivedMaterialToEventRequest model = new ReceivedMaterialToEventRequest(response.getProductId(),response.getRquantity(),response.getRemark());
                    listReq.add(model);
                }
                apiInterface.saveRtoEMaterial(id,listReq,tokenType+" "+token).enqueue(new Callback<List<ReceivedMaterialToEventRequest>>() {
                    @Override
                    public void onResponse(Call<List<ReceivedMaterialToEventRequest>> call, Response<List<ReceivedMaterialToEventRequest>> response) {

                        if (response.body()!=null)
                        {
                            progressHUD.dismiss();
                            Intent i = new Intent(ctx, DashBoardActivity.class);
                            i.putExtra("loadf",4);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            progressHUD.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ReceivedMaterialToEventRequest>> call, Throwable t) {
                        progressHUD.dismiss();
                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
