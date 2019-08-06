package com.croods.eventmanagement.fragment;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.activity.CustomerDetailActivity;
import com.croods.eventmanagement.activity.ProductDetailActivity;
import com.croods.eventmanagement.activity.ReceivedToEventActivity;
import com.croods.eventmanagement.activity.ReceivedToWarehouseActivity;
import com.croods.eventmanagement.activity.SendMaterialActivity;
import com.croods.eventmanagement.adapter.ProductListAdapter;
import com.croods.eventmanagement.adapter.ProductStockListAdapter;
import com.croods.eventmanagement.api.APIInterface;
import com.croods.eventmanagement.api.Common;
import com.croods.eventmanagement.api.DataStorage;
import com.croods.eventmanagement.api.UtilApi;
import com.croods.eventmanagement.model.ProductDetailResponse;
import com.croods.eventmanagement.model.ProductListResponse;
import com.croods.eventmanagement.model.ProductStockDetailList;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Context ctx;

    @BindView(R.id.lst_product)
    ListView lst_product;

    List<ProductListResponse> productList;
    private ProductListAdapter adapter;

    private APIInterface apiInterface;

    private KProgressHUD progressHUD;

    private DataStorage storage;
    private String token,tokenType;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    private Dialog dialog;
    private  TextView lbl_tqty,lbl_cqty;
    private ListView lst_product_detail;
    private List<ProductStockDetailList> psList;

    public ProductFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_product, container, false);

        lst_product = (ListView)root.findViewById(R.id.lst_product);
        apiInterface = UtilApi.getAPIService();
        progressHUD = Common.progressBar(ctx);
        storage = new DataStorage("loginPref",ctx);

        token = (String)storage.read("token",DataStorage.STRING);
        tokenType = (String)storage.read("tokenType",DataStorage.STRING);


        final Handler handler = new Handler();
        progressHUD.show();
        handler.postDelayed(() ->  getAllProductList(tokenType+" "+token), 2000);

       lst_product.setOnItemClickListener((adapterView, view, i, l) -> {

           if (productList.get(i).getOutquantity()!=0)
           {
               Intent pIntent = new Intent(ctx,ProductDetailActivity.class);
               pIntent.putExtra("productId",productList.get(i).getProductId());
               startActivity(pIntent);
           }
           else
           {
               Toast.makeText(ctx,"No  product detail because OUT quentity is 0",Toast.LENGTH_LONG).show();
           }

        });


        return  root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.img_search);
      //  searchItem.collapseActionView();

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);


        if (searchItem != null) {
            searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

            ImageView icon = searchView.findViewById(androidx.appcompat.R.id.search_button);

            icon.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_search_black_24dp));

            searchView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + getResources().getString(R.string.hintSearchMess) + "</font>"));

        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);

                    adapter.filter(newText);
                    adapter.notifyDataSetChanged();

                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.img_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    private void getAllProductList(String auth)
    {
        productList = new ArrayList<>();
        progressHUD.dismiss();
        apiInterface.getProductList(auth).enqueue(new Callback<List<ProductListResponse>>() {
            @Override
            public void onResponse(Call<List<ProductListResponse>> call, Response<List<ProductListResponse>> response)
            {

                if (response.body()!=null)
                {
                    productList = response.body();
                    adapter = new ProductListAdapter(productList,ctx);
                    lst_product.setAdapter(adapter);
                }
                else
                {
                    Common.showToast(ctx,"Please try again latter");
                }
            }

            @Override
            public void onFailure(Call<List<ProductListResponse>> call, Throwable t)
            {
                Common.showToast(ctx,"Server error Please try again latter");
            }
        });
    }



    private void showProductDetailDialog(int position) {
        dialog = new Dialog(ctx, R.style.AlertDialogCustom);
        dialog.setTitle("Product Detail : " + productList.get(position).getName());
        dialog.setContentView(R.layout.product_detail_dialog);

        lbl_cqty = (TextView) dialog.findViewById(R.id.lbl_cqty);
        lbl_tqty = (TextView) dialog.findViewById(R.id.lbl_tqty);
        lst_product_detail = (ListView) dialog.findViewById(R.id.lst_product_detail);
        psList = new ArrayList<>();

        apiInterface.getProductDetail(tokenType+" "+token,productList.get(position).getProductId()).enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {

                if (response.body()!=null)
                {
                    lbl_cqty.setText(response.body().getCurrentQty());
                    lbl_tqty.setText(response.body().getTotalQty());

                    ProductStockListAdapter adapter = new ProductStockListAdapter(response.body().getEvents(),ctx);
                    lst_product_detail.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<ProductDetailResponse> call, Throwable t) {

            }
        });

        dialog.show();

    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
