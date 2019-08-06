package com.croods.eventmanagement.fragment;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.activity.ActiveEventActivity;
import com.croods.eventmanagement.activity.DashBoardActivity;
import com.croods.eventmanagement.activity.MainActivity;
import com.croods.eventmanagement.api.APIInterface;
import com.croods.eventmanagement.api.Common;
import com.croods.eventmanagement.api.DataStorage;
import com.croods.eventmanagement.api.UtilApi;
import com.croods.eventmanagement.model.DashModel;
import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashBoardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.lbl_tevent)
    TextView lbl_tevent;

    @BindView(R.id.card_event)
    CardView card_event;

    @BindView(R.id.card_product)
    CardView card_product;

    @BindView(R.id.lbl_tproduct)
    TextView lbl_tproduct;

    @BindView(R.id.lbl_tstock)
    TextView lbl_tstock;

    private APIInterface apiInterface;

    private KProgressHUD progressHUD;

    private DataStorage storage;
    private String token, tokenType;
    private Context ctx;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
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
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dash_board, container, false);

        ButterKnife.bind(this, root);

        apiInterface = UtilApi.getAPIService();
        progressHUD = Common.progressBar(ctx);
        storage = new DataStorage("loginPref", ctx);

        token = (String) storage.read("token", DataStorage.STRING);
        tokenType = (String) storage.read("tokenType", DataStorage.STRING);

        final Handler handler = new Handler();
        progressHUD.show();
        handler.postDelayed(() -> getAllDashTotal(tokenType + " " + token), 2000);


        card_event.setOnClickListener(view -> {
            Intent i = new Intent(ctx, ActiveEventActivity.class);
            startActivity(i);
        });

        card_product.setOnClickListener(view -> {
            Intent i = new Intent(ctx, DashBoardActivity.class);
            i.putExtra("loadf", 5);
            startActivity(i);
        });

        return root;
    }


    private void getAllDashTotal(String auth) {
        apiInterface.getDashTotal(auth).enqueue(new Callback<DashModel>() {
            @Override
            public void onResponse(Call<DashModel> call, Response<DashModel> response) {
                if (response.body() != null) {
                    lbl_tevent.setText(response.body().getTotalActiveEvents());
                    lbl_tproduct.setText(response.body().getTotalProduct());
                    lbl_tstock.setText(response.body().getTotalQty());

                    progressHUD.dismiss();
                } else {
                    progressHUD.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DashModel> call, Throwable t) {
                progressHUD.dismiss();

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.logout_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.img_logout) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
            alertDialogBuilder.setMessage("Are you sure You want to logout");
            alertDialogBuilder.setPositiveButton("yes",
                    (arg0, arg1) -> {
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        storage.write("token", "");
                        storage.write("tokenType", "");
                        storage.write("isfirst", "true");
                        Intent i = new Intent(ctx, MainActivity.class);
                        startActivity(i);
                        ((Activity) ctx).finish();
                    });

            alertDialogBuilder.setNegativeButton("No", null);
            alertDialogBuilder.show();


            // Not implemented here
        }
        return super.onOptionsItemSelected(item);
    }
}
