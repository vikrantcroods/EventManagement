package com.croods.eventmanagement.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.SwipeToDeleteCallback;
import com.croods.eventmanagement.adapter.MaterialProductAdapter;
import com.croods.eventmanagement.api.APIInterface;
import com.croods.eventmanagement.api.Common;
import com.croods.eventmanagement.api.DataStorage;
import com.croods.eventmanagement.api.UtilApi;
import com.croods.eventmanagement.local_data_manage.LocalProductModel;
import com.croods.eventmanagement.local_data_manage.ProductRepository;
import com.croods.eventmanagement.model.BarcodeProductVo;
import com.croods.eventmanagement.model.MaterialProductModel;
import com.croods.eventmanagement.model.SendMaterialRequest;
import com.croods.eventmanagement.model.SpinnerResponse;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProdcutActivity extends AppCompatActivity {

    @BindView(R.id.btn_add_product)
    Button btn_add_product;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.lst_send_product)
    RecyclerView lst_send_product;

    @BindView(R.id.btn_scan_product)
    ImageButton btn_scan_product;

    @BindView(R.id.snack_add_product)
    ConstraintLayout snack_add_product;

    @BindView(R.id.txt_barcode)
    EditText txt_barcode;

    Context ctx = this;
    Bundle b;
    String barcode = "", isscan = "";

    String token, tokenType, jobcode;
    int eventId;
    DataStorage storage, tempStorage;
    KProgressHUD progressHUD;
    APIInterface apiInterface;

    ProductRepository repository;

    List<LocalProductModel> listProduct;
    ArrayList<SpinnerResponse> maintransportList, mainemployeeList;

    ArrayList<MaterialProductModel> eventWiseProductList;
    List<String> transportList, employeeList;

    MaterialProductAdapter adapter;
    Calendar myCalendar;
    int size;
    List<Long> materialOutwardItemVos;
    Date mainDate;
    String received,employeeId = "", employeeName = "", date = "", driverName = "", transporter = "", transportId = "", vehicleNumber = "", driverMobNo = "", note = "";
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prodcut);

        ButterKnife.bind(this);
        allocateMemory();

        b = getIntent().getExtras();
        if (b != null) {
            received = b.getString("received");
            eventId = b.getInt("eventId");
            jobcode = b.getString("jobcode");
            barcode = b.getString("barcode");
            isscan = b.getString("isscan");

            employeeId = b.getString("employeeId");
            employeeName = b.getString("employeeName");
            date = b.getString("date");
            driverName = b.getString("driverName");
            transportId = b.getString("transportId");
            transporter = b.getString("transporter");
            vehicleNumber = b.getString("vehicleNumber");
            driverMobNo = b.getString("driverMobNo");
            note = b.getString("note");
        }

        txt_barcode.setText(barcode);

        btn_scan_product.setOnClickListener(view -> {

            Intent i = new Intent(ctx, BarcodeScannerActivity.class);
            i.putExtra("received","false");
            i.putExtra("eventId", eventId);
            i.putExtra("jobcode", jobcode);
            i.putExtra("barcode", "");
            i.putExtra("isscan", "");
            i.putExtra("employeeId", employeeId);
            i.putExtra("storeId", "0");
            i.putExtra("employeeName", employeeName);
            i.putExtra("date", date);
            i.putExtra("driverName", driverName);
            i.putExtra("transportId", transportId);
            i.putExtra("transporter", transporter);
            i.putExtra("vehicleNumber", vehicleNumber);
            i.putExtra("driverMobNo", driverMobNo);
            i.putExtra("note", note);
            startActivity(i);
        });

        setProductdata();
        enableSwipeToDeleteAndUndo();
        lst_send_product.addItemDecoration(new DividerItemDecoration(lst_send_product.getContext(), DividerItemDecoration.VERTICAL));

        btn_add_product.setOnClickListener(view -> {
            barcode = txt_barcode.getText().toString();
            setProductdata();
        });
        btn_submit.setOnClickListener(view -> {
            progressHUD.show();
            repository.getAllId().observe((LifecycleOwner) ctx, list -> {
                materialOutwardItemVos = list;
            });

            final Handler handler = new Handler();
            handler.postDelayed(() -> {

                if (materialOutwardItemVos != null) {
                    if (!employeeId.equals("") || !transportId.equals("") || !date.equals("") || materialOutwardItemVos.size() != 0) {
                        Log.d("trace date", "-------------------------------" + date);
                        SendMaterialRequest request = new SendMaterialRequest(eventId, driverMobNo, driverName, employeeId, date, note, transportId, vehicleNumber, materialOutwardItemVos);
                        apiInterface.saveMaterialSend(tokenType + " " + token, request).enqueue(new Callback<SendMaterialRequest>() {
                            @Override
                            public void onResponse(Call<SendMaterialRequest> call, Response<SendMaterialRequest> response) {

                                if (response.body() != null) {
                                    progressHUD.dismiss();
                                    if (response.body().getMaterialOutwardItemVos().size() != 0)
                                    {
                                        repository.deleteAllTask();
                                        Common.showToast(ctx, "Material out created successfully");
                                        Intent i = new Intent(ctx, DashBoardActivity.class);
                                        i.putExtra("loadf", 3);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<SendMaterialRequest> call, Throwable t) {
                                progressHUD.dismiss();
                            }
                        });
                    }
                } else {
                    Common.showSnack(snack_add_product, "Please add at list one product");
                    progressHUD.dismiss();
                }

            }, 2000);

        });
    }

    private void setProductdata() {
        if (isscan.equals("yes")) {
            listProduct = new ArrayList<>();
            progressHUD.show();

            repository.verifyData(barcode).observe((LifecycleOwner) ctx, list -> {
                size = list.size();
            });

            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (size == 0) {
                    apiInterface.getProductFromBarcode(tokenType + " " + token, barcode).enqueue(new Callback<MaterialProductModel>() {
                        @Override
                        public void onResponse(Call<MaterialProductModel> call, Response<MaterialProductModel> response) {
                            //repository.deleteAllTask();

                            if (response.body() != null) {
                                MaterialProductModel m = response.body();
                                BarcodeProductVo productVo = m.getProductVo();

                                LocalProductModel model = new LocalProductModel(productVo.getDisplayName(), m.getBarcode(), m.getBarcodeId());
                                repository.insertTask(model);

                                final Handler handler = new Handler();
                                handler.postDelayed(() -> {

                                    repository.getAllData().observe((LifecycleOwner) ctx, list -> {
                                        listProduct = list;
                                        adapter = new MaterialProductAdapter(ctx, list);
                                        lst_send_product.setAdapter(adapter);
                                        progressHUD.dismiss();
                                    });

                                }, 2000);

                            } else {
                                final Handler handler = new Handler();
                                handler.postDelayed(() -> {
                                    repository.getAllData().observe((LifecycleOwner) ctx, list -> {
                                        listProduct = list;
                                        adapter = new MaterialProductAdapter(ctx, list);
                                        lst_send_product.setAdapter(adapter);
                                        progressHUD.dismiss();
                                    });

                                }, 2000);
                                Common.showSnack(snack_add_product, "Not Found");
                            }
                        }

                        @Override
                        public void onFailure(Call<MaterialProductModel> call, Throwable t) {

                            final Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                repository.getAllData().observe((LifecycleOwner) ctx, list -> {
                                    listProduct = list;
                                    adapter = new MaterialProductAdapter(ctx, list);
                                    lst_send_product.setAdapter(adapter);
                                    progressHUD.dismiss();
                                });

                            }, 2000);
                            Common.showSnack(snack_add_product, "Product not found");
                        }
                    });
                } else {
                    final Handler handler3 = new Handler();
                    handler3.postDelayed(() -> {
                        repository.getAllData().observe((LifecycleOwner) ctx, list -> {
                            listProduct = list;
                            adapter = new MaterialProductAdapter(ctx, list);
                            lst_send_product.setAdapter(adapter);
                        });

                    }, 2000);
                    progressHUD.dismiss();
                    Common.showSnack(snack_add_product, "Product already added");
                }


            }, 1000);
        }
        else
        {
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                repository.getAllData().observe((LifecycleOwner) ctx, list -> {
                    listProduct = list;
                    adapter = new MaterialProductAdapter(ctx, list);
                    lst_send_product.setAdapter(adapter);
                });

            }, 2000);
        }
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final LocalProductModel item = adapter.getData().get(position);
                repository.deleteTask(listProduct.get(position).getId());
                adapter.removeItem(position);

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(lst_send_product);
    }

    private void allocateMemory() {
        eventWiseProductList = new ArrayList<>();

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
