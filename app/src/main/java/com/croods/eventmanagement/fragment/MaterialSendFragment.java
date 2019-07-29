package com.croods.eventmanagement.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.adapter.EventListAdapter;
import com.croods.eventmanagement.adapter.MaterialSendListAdapter;
import com.croods.eventmanagement.api.APIInterface;
import com.croods.eventmanagement.api.Common;
import com.croods.eventmanagement.api.DataStorage;
import com.croods.eventmanagement.api.UtilApi;
import com.croods.eventmanagement.model.EventListResponse;
import com.croods.eventmanagement.model.MaterialSendListResponse;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MaterialSendFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Context ctx;

    @BindView(R.id.lst_material_send)
    ListView lst_material_send;

    @BindView(R.id.spn_send_event)
    AppCompatSpinner spn_send_event;



    private List<MaterialSendListResponse> materialSendList;
    private MaterialSendListAdapter adapter;
    private List<String> eventList;

    private APIInterface apiInterface;

    private KProgressHUD progressHUD;

    private DataStorage storage;
    private String token,tokenType;


    public MaterialSendFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MaterialSendFragment newInstance() {
        MaterialSendFragment fragment = new MaterialSendFragment();
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
        View root = inflater.inflate(R.layout.fragment_material_send, container, false);


        ButterKnife.bind(this,root);

        apiInterface = UtilApi.getAPIService();
        progressHUD = Common.progressBar(ctx);
        storage = new DataStorage("loginPref",ctx);

        token = (String)storage.read("token",DataStorage.STRING);
        tokenType = (String)storage.read("tokenType",DataStorage.STRING);

        final Handler handler = new Handler();
        progressHUD.show();
        handler.postDelayed(() ->  getAllMaterialSendList(tokenType+" "+token), 2000);

        spn_send_event.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.filter(eventList.get(i));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        return root;
    }

    private void getAllMaterialSendList(String auth)
    {
        materialSendList = new ArrayList<>();
        eventList = new ArrayList<>();
        progressHUD.dismiss();
        apiInterface.getMaterialSendList(auth).enqueue(new Callback<List<MaterialSendListResponse>>() {
            @Override
            public void onResponse(Call<List<MaterialSendListResponse>> call, Response<List<MaterialSendListResponse>> response)
            {

                if (response.body()!=null)
                {
                    materialSendList = response.body();
                    adapter = new MaterialSendListAdapter(materialSendList,ctx);
                    lst_material_send.setAdapter(adapter);

                    for (MaterialSendListResponse listResponse :  materialSendList)
                    {
                        if (!eventList.contains(listResponse.getJobCode()))
                            eventList.add(listResponse.getJobCode());
                    }

                    ArrayAdapter adapter = new ArrayAdapter(ctx, android.R.layout.simple_spinner_item, eventList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spn_send_event.setAdapter(adapter);
                }
                else
                {
                    Common.showToast(ctx,"Please try again latter");
                }
            }

            @Override
            public void onFailure(Call<List<MaterialSendListResponse>> call, Throwable t)
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
