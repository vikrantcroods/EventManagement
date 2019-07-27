package com.croods.eventmanagement.fragment;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.activity.EventCreateActivity;
import com.croods.eventmanagement.activity.ReceivedToEventActivity;
import com.croods.eventmanagement.activity.ReceivedToWarehouseActivity;
import com.croods.eventmanagement.activity.SendMaterialActivity;
import com.croods.eventmanagement.adapter.EventListAdapter;
import com.croods.eventmanagement.api.APIInterface;
import com.croods.eventmanagement.api.Common;
import com.croods.eventmanagement.api.DataStorage;
import com.croods.eventmanagement.api.UtilApi;
import com.croods.eventmanagement.model.EventListResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.lst_event)
    ListView lst_event;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private Context ctx;
    private List<EventListResponse> eventList, mainecentList;
    private EventListAdapter adapter;

    private APIInterface apiInterface;

    private KProgressHUD progressHUD;

    private DataStorage storage;
    private String token, tokenType;
    private Dialog dialog;

    private TextView lbl_rtow, lbl_rtoe, lbl_stoe;
    private FloatingActionButton btn_create_event;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    public EventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_event, container, false);

        lst_event = (ListView) root.findViewById(R.id.lst_event);
        btn_create_event = (FloatingActionButton) root.findViewById(R.id.btn_create_event);

        apiInterface = UtilApi.getAPIService();
        progressHUD = Common.progressBar(ctx);
        storage = new DataStorage("loginPref", ctx);

        token = (String) storage.read("token", DataStorage.STRING);
        tokenType = (String) storage.read("tokenType", DataStorage.STRING);


        final Handler handler = new Handler();
        progressHUD.show();
        handler.postDelayed(() -> getAllCategory(tokenType + " " + token), 2000);

        lst_event.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showMaterialDialog(i);
            }
        });

        btn_create_event.setOnClickListener(view -> {
            Intent cEvent = new Intent(ctx, EventCreateActivity.class);
            startActivity(cEvent);
        });


        return root;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.event_status_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        //  searchItem.collapseActionView();

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);


        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();

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
        if (item.getItemId() == R.id.menu_status) {// Not implemented here
            PopupMenu popup = new PopupMenu(ctx, getActivity().findViewById(R.id.menu_status));
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.event_popup_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    switch (item.getItemId()) {
                        case R.id.menu_active:
                            eventList = new ArrayList<>();
                            for (EventListResponse res : mainecentList) {
                                String status = res.getStatus();
                                if (status.equals("In Progress") || status.equals("Up Coming")) {
                                    eventList.add(res);
                                }
                            }
                            adapter = new EventListAdapter(eventList, ctx);
                            lst_event.setAdapter(adapter);
                            return true;

                        case R.id.menu_inactive:
                            eventList = new ArrayList<>();
                            for (EventListResponse res : mainecentList) {
                                String status = res.getStatus();
                                if (status.equals("Cancelled")) {
                                    eventList.add(res);
                                }
                            }
                            adapter = new EventListAdapter(eventList, ctx);
                            lst_event.setAdapter(adapter);
                            return true;

                        case R.id.menu_all:
                            // Not implemented here
                            adapter = new EventListAdapter(mainecentList, ctx);
                            lst_event.setAdapter(adapter);
                            return true;


                    }
                    return true;
                }
            });

            popup.show();//showing popup menu
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    private void showMaterialDialog(int position) {
        dialog = new Dialog(ctx, R.style.AlertDialogCustom);
        dialog.setTitle("Send/Received Material of " + eventList.get(position).getJobCode());
        dialog.setContentView(R.layout.marerial_action_dialog);

        lbl_stoe = (TextView) dialog.findViewById(R.id.lbl_stoe);
        lbl_rtow = (TextView) dialog.findViewById(R.id.lbl_rtow);
        lbl_rtoe = (TextView) dialog.findViewById(R.id.lbl_rtoe);
        dialog.show();

        lbl_rtoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(ctx, ReceivedToEventActivity.class);
                i.putExtra("eventId", eventList.get(position).getEventId());
                i.putExtra("jobcode", eventList.get(position).getJobCode());
                i.putExtra("isscan", "no");
                startActivity(i);
            }
        });

        lbl_stoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(ctx, SendMaterialActivity.class);
                i.putExtra("eventId", eventList.get(position).getEventId());
                i.putExtra("jobcode", eventList.get(position).getJobCode());
                i.putExtra("isscan", "no");
                startActivity(i);
            }
        });

        lbl_rtow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(ctx, ReceivedToWarehouseActivity.class);
                i.putExtra("eventId", eventList.get(position).getEventId());
                i.putExtra("jobcode", eventList.get(position).getJobCode());
                i.putExtra("isscan", "no");
                startActivity(i);
            }
        });


    }


    private void getAllCategory(String auth) {
        eventList = new ArrayList<>();
        apiInterface.getAllEvent(auth).enqueue(new Callback<List<EventListResponse>>() {
            @Override
            public void onResponse(Call<List<EventListResponse>> call, Response<List<EventListResponse>> response) {
                progressHUD.dismiss();

                if (response.body() != null) {
                    mainecentList = response.body();
                    for (EventListResponse res : mainecentList) {
                        String status = res.getStatus();
                        if (status.equals("In Progress") || status.equals("Up Coming")) {
                            eventList.add(res);
                        }
                    }


                    adapter = new EventListAdapter(eventList, ctx);
                    lst_event.setAdapter(adapter);
                } else {
                    Common.showToast(ctx, "Please try again latter");
                }
            }

            @Override
            public void onFailure(Call<List<EventListResponse>> call, Throwable t) {
                progressHUD.dismiss();
                Common.showToast(ctx, "Server error Please try again latter");
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
        //dialog.dismiss();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
