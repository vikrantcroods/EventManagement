package com.croods.eventmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.adapter.ProductStockListAdapter;
import com.croods.eventmanagement.api.APIInterface;
import com.croods.eventmanagement.api.Common;
import com.croods.eventmanagement.api.DataStorage;
import com.croods.eventmanagement.api.UtilApi;
import com.croods.eventmanagement.model.ProductDetailResponse;
import com.croods.eventmanagement.model.ProductStockDetailList;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    @BindView(R.id.lst_dproduct)
    ListView lst_dproduct;

    @BindView(R.id.tool_dproduct)
    Toolbar tool_dproduct;

    DataStorage storage;
    private String token,tokenType;

    Context ctx=this;
    APIInterface apiInterface;
    private KProgressHUD progressHUD;

    private List<ProductStockDetailList> psList;
    private  int position;
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ButterKnife.bind(this);

        allocateMemory();

        setSupportActionBar(tool_dproduct);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        b = getIntent().getExtras();
        if (b!=null)
        {
            position = b.getInt("productId");
        }

        final Handler handler = new Handler();
        progressHUD.show();
        handler.postDelayed(() ->  getProductDetailList(position), 1000);

    }

    private  void getProductDetailList(int position)
    {
        apiInterface.getProductDetail(tokenType+" "+token, position).enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {

                if (response.body()!=null)
                {
                    psList = response.body().getEvents();

                    psList.add(new ProductStockDetailList(response.body().getCurrentQty(),"Current Qty"));
                    psList.add(new ProductStockDetailList(response.body().getTotalQty(),"Total Qty"));


                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        ProductStockListAdapter adapter = new ProductStockListAdapter(response.body().getEvents(),ctx);
                        lst_dproduct.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        progressHUD.dismiss();
                    }, 2000);



                }
                else
                {
                    progressHUD.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ProductDetailResponse> call, Throwable t) {

            }
        });
    }


    private void allocateMemory()
    {
        psList = new ArrayList<>();

        apiInterface = UtilApi.getAPIService();
        progressHUD = Common.progressBar(ctx);
        storage = new DataStorage("loginPref",ctx);

        token = (String)storage.read("token",DataStorage.STRING);
        tokenType = (String)storage.read("tokenType",DataStorage.STRING);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(ctx,DashBoardActivity.class);
        i.putExtra("loadf",5);
        startActivity(i);
        finish();
    }
}
