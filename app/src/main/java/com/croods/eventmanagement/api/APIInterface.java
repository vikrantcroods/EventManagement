package com.croods.eventmanagement.api;

import com.croods.eventmanagement.model.CityResponse;
import com.croods.eventmanagement.model.CustomerAddRequest;
import com.croods.eventmanagement.model.CustomerDetailResponse;
import com.croods.eventmanagement.model.CustomerListResponse;
import com.croods.eventmanagement.model.DashModel;
import com.croods.eventmanagement.model.EventListResponse;
import com.croods.eventmanagement.model.LoginRequest;
import com.croods.eventmanagement.model.MaterialOutWordEventList;
import com.croods.eventmanagement.model.MaterialProductModel;
import com.croods.eventmanagement.model.MaterialReceivedListResponse;
import com.croods.eventmanagement.model.MaterialSendListResponse;
import com.croods.eventmanagement.model.ProductListResponse;
import com.croods.eventmanagement.model.ReceivedMaterialRequest;
import com.croods.eventmanagement.model.ReceivedMaterialToEventRequest;
import com.croods.eventmanagement.model.SendMaterialRequest;
import com.croods.eventmanagement.model.SpinnerResponse;
import com.croods.eventmanagement.model.StateResponse;
import com.croods.eventmanagement.model.StoreListResponse;
import com.croods.eventmanagement.model.TokenResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {

    @POST("auth/signin")
    Call<TokenResponse> getsignIn(@Body LoginRequest request);

    @GET("customer")
    Call<List<CustomerListResponse>> getAllCustomer(@Header("Authorization") String auth);

    @GET("event")
    Call<List<EventListResponse>> getAllEvent(@Header("Authorization") String auth);


    @GET("customer/{id}")
    Call<CustomerDetailResponse> getCustomerDetail(@Path("id")int id, @Header("Authorization") String auth);

    @GET("products")
    Call<List<ProductListResponse>> getProductList(@Header("Authorization") String auth);

    @GET("send-material")
    Call<List<MaterialSendListResponse>> getMaterialSendList(@Header("Authorization") String auth);

    @GET("receive-to-store")
    Call<List<MaterialReceivedListResponse>> getMaterialReceivedList(@Header("Authorization") String auth);

    @GET("products/barcode/{barcode}")
    Call<MaterialProductModel> getProductFromBarcode(@Header("Authorization") String auth, @Path("barcode") String barcode);

    @POST("send-material")
    Call<SendMaterialRequest> saveMaterialSend(@Header("Authorization") String auth,@Body SendMaterialRequest request);


    @GET("transport")
    Call<ArrayList<SpinnerResponse>> getTransportList(@Header("Authorization") String auth);


    @GET("employee")
    Call<ArrayList<SpinnerResponse>> getEmployeeList(@Header("Authorization") String auth);

    @GET("dashboard")
    Call<DashModel> getDashTotal(@Header("Authorization") String auth);

    @GET("send-material/event/{id}")
    Call<List<MaterialOutWordEventList>> getMaterialOutwordList(@Path("id")int id, @Header("Authorization") String auth);


    @GET("send-material/mout/{id}")
    Call<List<ProductListResponse>> getMoutProductList(@Path("id")int id, @Header("Authorization") String auth);

    @POST("receive-to-event/{id}")
    Call<List<ReceivedMaterialToEventRequest>> saveRtoEMaterial(@Path("id")int id, @Body List<ReceivedMaterialToEventRequest> req, @Header("Authorization") String auth);


    @POST("location/state/IN")
    Call<List<StateResponse>> getAllStateList(@Header("Authorization") String auth);


    @GET("location/city/{stateId}")
    Call<List<CityResponse>> getAllCityList(@Path("stateId")String stateId, @Header("Authorization") String auth);

    @POST("event")
    Call<EventListResponse> saveEvent(@Body EventListResponse request, @Header("Authorization") String auth);

    @GET("store")
    Call<ArrayList<StoreListResponse>> getAllStoreList(@Header("Authorization") String auth);

    @POST("receive-to-store")
    Call<ReceivedMaterialRequest> saveMaterialReceived(@Header("Authorization") String auth, @Body ReceivedMaterialRequest request);

    @GET("receive-to-store/event-wise-receivable/{eventId}")
    Call<List<MaterialProductModel>> getEventWiseReceivedProductList(@Path("eventId") int eventId, @Header("Authorization") String auth);

    @POST("customer")
    Call<CustomerAddRequest> saveCustomer(@Header("Authorization") String auth, @Body CustomerAddRequest request);

}