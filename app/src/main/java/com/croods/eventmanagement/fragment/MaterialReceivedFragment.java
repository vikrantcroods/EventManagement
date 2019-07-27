package com.croods.eventmanagement.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.adapter.MaterialReceivedListAdapter;
import com.croods.eventmanagement.adapter.MaterialSendListAdapter;
import com.croods.eventmanagement.api.APIInterface;
import com.croods.eventmanagement.api.Common;
import com.croods.eventmanagement.api.DataStorage;
import com.croods.eventmanagement.api.UtilApi;
import com.croods.eventmanagement.model.MaterialReceivedListResponse;
import com.croods.eventmanagement.model.MaterialSendListResponse;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MaterialReceivedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MaterialReceivedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MaterialReceivedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Context ctx;

    @BindView(R.id.lst_material_received)
    ListView lst_material_received;

    private List<MaterialReceivedListResponse> materialReceivedList;
    private MaterialReceivedListAdapter adapter;

    private APIInterface apiInterface;

    private KProgressHUD progressHUD;

    private DataStorage storage;
    private String token,tokenType;


    public MaterialReceivedFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MaterialReceivedFragment newInstance() {
        MaterialReceivedFragment fragment = new MaterialReceivedFragment();
        Bundle args = new Bundle();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_material_received, container, false);

        ButterKnife.bind(this,root);

        apiInterface = UtilApi.getAPIService();
        progressHUD = Common.progressBar(ctx);
        storage = new DataStorage("loginPref",ctx);

        token = (String)storage.read("token",DataStorage.STRING);
        tokenType = (String)storage.read("tokenType",DataStorage.STRING);

        final Handler handler = new Handler();
        progressHUD.show();
        handler.postDelayed(() ->  getAllMaterialSendList(tokenType+" "+token), 2000);

        return root;
    }

    private void getAllMaterialSendList(String auth)
    {
        materialReceivedList = new ArrayList<>();
        progressHUD.dismiss();
        apiInterface.getMaterialReceivedList(auth).enqueue(new Callback<List<MaterialReceivedListResponse>>() {
            @Override
            public void onResponse(Call<List<MaterialReceivedListResponse>> call, Response<List<MaterialReceivedListResponse>> response)
            {
                if (response.body()!=null)
                {
                    materialReceivedList = response.body();
                    adapter = new MaterialReceivedListAdapter(materialReceivedList,ctx);
                    lst_material_received.setAdapter(adapter);
                }
                else
                {
                    Common.showToast(ctx,"Please try again latter");
                }
            }

            @Override
            public void onFailure(Call<List<MaterialReceivedListResponse>> call, Throwable t)
            {
                Common.showToast(ctx,"Server error Please try again latter");
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
}
