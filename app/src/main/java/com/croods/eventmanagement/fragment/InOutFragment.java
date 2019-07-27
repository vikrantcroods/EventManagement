package com.croods.eventmanagement.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.adapter.PagerAdapter;
import com.croods.eventmanagement.api.APIInterface;
import com.croods.eventmanagement.api.Common;
import com.croods.eventmanagement.api.DataStorage;
import com.croods.eventmanagement.api.UtilApi;
import com.google.android.material.tabs.TabLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InOutFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.inout_viewpager)
    ViewPager inout_viewpager;

    @BindView(R.id.inout_tab)
    TabLayout inout_tab;


    private APIInterface apiInterface;
    private DataStorage storage;
    private Context ctx;
    private int userId;
    private KProgressHUD progressHUD;
    //private List<AllCompletedInquiryResponse> completedInquiryList;
    private String authtoken;

    public InOutFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static InOutFragment newInstance(String param1, String param2) {
        InOutFragment fragment = new InOutFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_inout, container, false);

        ButterKnife.bind(this, root);

        apiInterface = UtilApi.getAPIService();
        storage = new DataStorage("loginPref", ctx);
        progressHUD = Common.progressBar(ctx);

        authtoken = (String) storage.read("authtoken", DataStorage.STRING);
        userId = (int) storage.read("userId", DataStorage.INTEGER);

        PagerAdapter mViewPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
        mViewPagerAdapter.addFragment(MaterialSendFragment.newInstance(), "Send Material");
        mViewPagerAdapter.addFragment(MaterialReceivedFragment.newInstance(), "Received Material");

        inout_viewpager.setAdapter(mViewPagerAdapter);

        inout_tab.setupWithViewPager(inout_viewpager);

        inout_viewpager.setCurrentItem(0);

        return root;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
