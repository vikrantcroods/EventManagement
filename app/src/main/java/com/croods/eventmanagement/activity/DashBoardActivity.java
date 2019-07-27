package com.croods.eventmanagement.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.api.DataStorage;
import com.croods.eventmanagement.fragment.CustomerFragment;
import com.croods.eventmanagement.fragment.DashBoardFragment;
import com.croods.eventmanagement.fragment.EventFragment;
import com.croods.eventmanagement.fragment.InOutFragment;
import com.croods.eventmanagement.fragment.MaterialReceivedFragment;
import com.croods.eventmanagement.fragment.MaterialSendFragment;
import com.croods.eventmanagement.fragment.ProductFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashBoardActivity extends AppCompatActivity implements CustomerFragment.OnFragmentInteractionListener, EventFragment.OnFragmentInteractionListener, ProductFragment.OnFragmentInteractionListener,InOutFragment.OnFragmentInteractionListener, MaterialSendFragment.OnFragmentInteractionListener, MaterialReceivedFragment.OnFragmentInteractionListener ,DashBoardFragment.OnFragmentInteractionListener{


    @BindView(R.id.content)
    FrameLayout content;

    @BindView(R.id.tool_home)
    Toolbar tool_home;

    DataStorage storage;

    Context ctx = this;
    Bundle bundle;
    int loadf=0;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    loadFragment(new DashBoardFragment());
                    return true;
                case R.id.navigation_customer:
                    loadFragment(new CustomerFragment());
                    mTextMessage.setText(R.string.title_customer);
                    return true;
                case R.id.navigation_inout:
                    loadFragment(new InOutFragment());
                    mTextMessage.setText(R.string.title_inout);
                    return true;

                case R.id.navigation_events:
                    loadFragment(new EventFragment());
                    mTextMessage.setText(R.string.title_event);
                    return true;

                case R.id.navigation_product:
                    loadFragment(new ProductFragment());
                    mTextMessage.setText(R.string.title_product);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(this);
        setSupportActionBar(tool_home);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tool_home.collapseActionView();


        BottomNavigationView navView = findViewById(R.id.nav_view);

        mTextMessage = findViewById(R.id.message);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            loadf = bundle.getInt("loadf");
        }
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Log.d("trace","--------------------------"+loadf);
        switch (loadf)
        {
            case 0:
                navView.getMenu().findItem(R.id.navigation_home).setChecked(true);
                mTextMessage.setText(R.string.title_home);
                loadFragment(new DashBoardFragment());
                break;
            case 1:
                navView.getMenu().findItem(R.id.navigation_home).setChecked(true);
                mTextMessage.setText(R.string.title_home);
                loadFragment(new DashBoardFragment());
                break;
            case 2:
                navView.getMenu().findItem(R.id.navigation_customer).setChecked(true);
                mTextMessage.setText(R.string.title_customer);
                loadFragment(new CustomerFragment());
                break;
            case 3:
                navView.getMenu().findItem(R.id.navigation_inout).setChecked(true);
                mTextMessage.setText(R.string.title_inout);
                loadFragment(new InOutFragment());
                break;
            case 4:
                navView.getMenu().findItem(R.id.navigation_events).setChecked(true);
                mTextMessage.setText(R.string.title_event);
                loadFragment(new EventFragment());
                break;
            case 5:
                navView.getMenu().findItem(R.id.navigation_product).setChecked(true);
                mTextMessage.setText(R.string.title_product);
                loadFragment(new ProductFragment());
                break;
        }
    }



    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

    }
}
