package com.croods.eventmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.api.APIInterface;
import com.croods.eventmanagement.api.Common;
import com.croods.eventmanagement.api.DataStorage;
import com.croods.eventmanagement.api.UtilApi;
import com.croods.eventmanagement.model.LoginRequest;
import com.croods.eventmanagement.model.TokenResponse;
import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.txt_mobileno)
    EditText txt_mobileno;

    @BindView(R.id.txt_password)
    EditText txt_password;


    @BindView(R.id.btn_login)
    Button btn_login;


    Context ctx=this;

    APIInterface apiInterface;

    String userName,password;
    String token,isFirst;
    DataStorage storage;
    KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        apiInterface = UtilApi.getAPIService();
        storage = new DataStorage("loginPref",ctx);
        progressHUD = Common.progressBar(ctx);


        isFirst = (String) storage.read("isfirst", DataStorage.STRING);

        if (isFirst.equals("false"))
        {
            Intent i = new Intent(ctx,DashBoardActivity.class);
            startActivity(i);
            finish();
        }

        btn_login.setOnClickListener(v -> {

            if (isValidate())
            {
                getsignIn(txt_mobileno.getText().toString(),txt_password.getText().toString());
            }
        });

    }

    public void getsignIn(String username , String password)
    {
        progressHUD.show();
        apiInterface.getsignIn(new LoginRequest(username,password)).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response)
            {
                progressHUD.dismiss();
                if (response.body()!=null)
                {
                    token = response.body().getAccessToken();
                    storage.write("token",response.body().getAccessToken());
                    storage.write("tokenType",response.body().getTokenType());
                    storage.write("isfirst","false");
                    Intent i = new Intent(ctx,DashBoardActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Common.showToast(ctx,"Somehing went wrong please try again latter");
                }

            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t)
            {
                progressHUD.dismiss();
                Common.showToast(ctx,"Somehing went wrong please try again latter");
            }
        });
    }

    public boolean isValidate()
    {
        userName = txt_mobileno.getText().toString();
        password = txt_password.getText().toString();
        if (userName.equals("") || password.equals(""))
        {
            Common.showToast(ctx,"should not blank");
            return false;
        }
        else
        {
            return true;
        }
    }

}
