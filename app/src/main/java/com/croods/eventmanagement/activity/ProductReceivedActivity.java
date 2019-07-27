package com.croods.eventmanagement.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.croods.eventmanagement.model.ReceivedMaterialRequest;
import com.croods.eventmanagement.model.ResponseBarcodeVo;
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

public class ProductReceivedActivity extends AppCompatActivity {

    @BindView(R.id.btn_add_product)
    Button btn_add_product;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.lst_received_product)
    RecyclerView lst_received_product;

    @BindView(R.id.btn_scan_product)
    ImageButton btn_scan_product;

    @BindView(R.id.snack_received_product)
    ConstraintLayout snack_add_product;

    @BindView(R.id.txt_barcode)
    EditText txt_barcode;

    Context ctx = this;
    Bundle b;
    String barcode = "", isscan = "";

    String token, tokenType, jobcode;
    int eventId,storeId;
    DataStorage storage, tempStorage;
    KProgressHUD progressHUD;
    APIInterface apiInterface;

    ProductRepository repository;

    List<LocalProductModel> listProduct;
    ArrayList<SpinnerResponse> maintransportList, mainemployeeList;
    List<MaterialProductModel> receivedProductList;
    List<String> transportList, employeeList, barcodeList;

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
        setContentView(R.layout.activity_product_received);

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
            storeId = b.getInt("storeId");
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
            i.putExtra("received", "true");
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
        });

        lst_received_product.addItemDecoration(new DividerItemDecoration(lst_received_product.getContext(), DividerItemDecoration.VERTICAL));
        getEventwiseProductList();

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
                        ReceivedMaterialRequest request = new ReceivedMaterialRequest(eventId, driverMobNo, driverName, employeeId, date,storeId, note, transportId, vehicleNumber, materialOutwardItemVos);
                        apiInterface.saveMaterialReceived(tokenType + " " + token, request).enqueue(new Callback<ReceivedMaterialRequest>() {
                            @Override
                            public void onResponse(Call<ReceivedMaterialRequest> call, Response<ReceivedMaterialRequest> response) {

                                if (response.body() != null) {
                                    progressHUD.dismiss();
                                    if (response.body().getMaterialInwardItemVos().size() != 0)
                                    {
                                        repository.deleteAllTask();
                                        Common.showToast(ctx, "Material in successfully");
                                        Intent i = new Intent(ctx, DashBoardActivity.class);
                                        i.putExtra("loadf", 3);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ReceivedMaterialRequest> call, Throwable t) {
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
        enableSwipeToDeleteAndUndo();

    }

    private void getEventwiseProductList() {
        apiInterface.getEventWiseReceivedProductList(eventId, tokenType + " " + token).enqueue(new Callback<List<MaterialProductModel>>() {
            @Override
            public void onResponse(Call<List<MaterialProductModel>> call, Response<List<MaterialProductModel>> response) {
                if (response.body() != null) {
                    receivedProductList = response.body();

                    final Handler handlser = new Handler();
                    progressHUD.show();
                    handlser.postDelayed(() -> {
                        if (isscan.equals("yes")) {
                            for (MaterialProductModel model : receivedProductList) {
                                ResponseBarcodeVo vo = model.getBarcodeVo();
                                BarcodeProductVo pvo = vo.getProductVo();
                                barcodeList.add(vo.getBarcode());
                                if (barcode.equals(vo.getBarcode())) {
                                    listProduct = new ArrayList<>();
                                    repository.verifyData(barcode).observe((LifecycleOwner) ctx, list -> {
                                        size = list.size();
                                    });
                                    final Handler handler = new Handler();
                                    handler.postDelayed(() -> {
                                        if (size == 0) {
                                            LocalProductModel lmodel = new LocalProductModel(pvo.getDisplayName(), vo.getBarcode(), vo.getBarcodeId());
                                            repository.insertTask(lmodel);

                                        } else {
                                            Common.showSnack(snack_add_product, "Product already added");
                                        }

                                    }, 1000);

                                }
                            }

                            final Handler handler2 = new Handler();
                            handler2.postDelayed(() -> {

                                repository.getAllData().observe((LifecycleOwner) ctx, list -> {
                                    listProduct = list;
                                    Log.d("size trace", "=====================" + listProduct.size());

                                    adapter = new MaterialProductAdapter(ctx, list);
                                    lst_received_product.setAdapter(adapter);
                                    progressHUD.dismiss();
                                });

                            }, 3000);
                        }
                        else
                        {
                            progressHUD.dismiss();
                        }

                    }, 1000);

                }

            }

            @Override
            public void onFailure(Call<List<MaterialProductModel>> call, Throwable t) {

            }
        });
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
        itemTouchhelper.attachToRecyclerView(lst_received_product);
    }


    private void allocateMemory() {

        receivedProductList = new ArrayList<>();
        listProduct = new ArrayList<>();
        materialOutwardItemVos = new ArrayList<>();

        employeeList = new ArrayList<>();
        transportList = new ArrayList<>();
        barcodeList = new ArrayList<>();

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
