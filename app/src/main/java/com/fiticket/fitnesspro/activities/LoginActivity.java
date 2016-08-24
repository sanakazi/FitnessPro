package com.fiticket.fitnesspro.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiticket.fitnesspro.R;
import com.fiticket.fitnesspro.WebServices;
import com.fiticket.fitnesspro.constants.Constant;
import com.fiticket.fitnesspro.fragments.LoginFragment;
import com.fiticket.fitnesspro.pojos.LoginRequest;
import com.fiticket.fitnesspro.pojos.LoginResponseJson;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by InFinItY on 12/1/2015.
 */
public class LoginActivity extends AppCompatActivity implements LoginFragment.FragmentInteractionListner {
    private static final String TAG = LoginActivity.class.getSimpleName();
    FragmentManager fragmentManager;
    SharedPreferences sPref;
    private ProgressBar progressBar;
    Calendar currentDate;
    private long expiryDate;
    Calendar mcurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_activity);
        progressBar= (ProgressBar)findViewById(R.id.progressBar);
        mcurrentDate = Calendar.getInstance();
        sPref=getSharedPreferences(Constant.PREF, MODE_PRIVATE);
        expiryDate=sPref.getLong(Constant.EXPIRY_DATE,0);
        if((0!=(sPref.getInt(Constant.USER_TENANT_ID,0)))&&(expiryDate>=mcurrentDate.getTimeInMillis())){
            gotoMainActivity();
        }
        LoginFragment loginFragment= new LoginFragment();
        fragmentManager= getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container,loginFragment).commit();
    }

    private void gotoMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }


    @Override
    public void onLoginClicked(final LoginRequest request) {
        progressBar.setVisibility(View.VISIBLE);
        WebServices.sendPostRequest(this,Constant.LOGIN_URL, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "Login response: " + response.toString());
                LoginResponseJson responseJson = new Gson().fromJson(response.toString(), LoginResponseJson.class);
                handleLoginResponse(responseJson);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, error.toString());
                //Toast.makeText(LoginActivity.this, "Login Failed: " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(LoginActivity.this, "Login Failed Check Username or Password", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }, LoginRequest.class);
    }

    private void handleLoginResponse(LoginResponseJson responseJson) {

        if(responseJson.getStatusCode().equals(WebServices.SUCCESS_CODE)){
            saveUserDetails(responseJson);
            mcurrentDate = Calendar.getInstance();
            sPref=getSharedPreferences(Constant.PREF,MODE_PRIVATE);
            expiryDate=sPref.getLong(Constant.EXPIRY_DATE,0);
            if (expiryDate>=mcurrentDate.getTimeInMillis()) {
                gotoMainActivity();
            }
            else {
                Toast.makeText(LoginActivity.this, "Account expired", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(LoginActivity.this, "Login Failed: " + responseJson.getStatusMsg(), Toast.LENGTH_LONG).show();
        }
    }

    private void saveUserDetails(LoginResponseJson responseJson) {
        SharedPreferences.Editor editor= sPref.edit();
        editor.putInt(Constant.USER_TENANT_ID, responseJson.getData().getTenantId());
        editor.putString(Constant.USER_CONTACT_NUMMBER, responseJson.getData().getContactNumber());
        editor.putString(Constant.USER_ADDRESS,responseJson.getData().getAddress());
        editor.putString(Constant.USER_EMAIL,responseJson.getData().getEmail());
        editor.putInt(Constant.USER_USER_ID, responseJson.getData().getUserId());
        editor.putString(Constant.USER_ROLE_NAME, responseJson.getData().getRoleName());
        editor.putString(Constant.USER_VENUE_NAME,responseJson.getData().getVenueName());
        editor.putString(Constant.USER_TENANT_NAME,responseJson.getData().getTenantName());
        editor.putInt(Constant.USER_ROLE_ID, responseJson.getData().getRoleId());
        editor.putLong(Constant.EXPIRY_DATE, responseJson.getData().getExpiryDate());
        editor.putString(Constant.SESSION_TOKEN, responseJson.getData().getSessionToken());
        editor.putString(Constant.USER_NAME,responseJson.getData().getUserName());
        editor.putString(Constant.DISPLAY_NAME,responseJson.getData().getDisplayName());
        editor.commit();
    }
}
