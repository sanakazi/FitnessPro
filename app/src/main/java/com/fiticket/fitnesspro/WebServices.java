package com.fiticket.fitnesspro;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fiticket.fitnesspro.constants.Constant;
import com.fiticket.fitnesspro.singleton.MySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by InFinItY on 1/21/2016.
 */
public class WebServices {

    public static final String SUCCESS_CODE = "0";
    private static final String TAG = WebServices.class.getSimpleName();
    private static final String CONTENT_TYPE = "application/json";
    public static final String SUCCESS_CODE_ERR = "1003";

    public static void triggerVolleyGetRequest(final Context context,String url,Response.Listener<String> responseListener,
                                               Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(Request.Method.GET, url, responseListener, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences sPref= context.getSharedPreferences(Constant.PREF, Context.MODE_PRIVATE);
                String sessionToken=sPref.getString(Constant.SESSION_TOKEN, "");

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-fitnesspro-auth", sessionToken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }



    public static void sendPostRequest(final Context context, String url, final Object json, Response.Listener<JSONObject> responseListener,
                                       Response.ErrorListener errorListener, Class jsonClass) {
        Gson gson= new Gson();
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(gson.toJson(json, jsonClass));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,jsonObject,responseListener,errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences sPref= context.getSharedPreferences(Constant.PREF, Context.MODE_PRIVATE);
                String sessionToken=sPref.getString(Constant.SESSION_TOKEN, "");
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-fitnesspro-auth", sessionToken);
                return headers;
            }
        };
        //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions.
        //Volley does retry for you if you have specified the policy.
        request.setRetryPolicy(new DefaultRetryPolicy(6000,
                2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Log.d(TAG, "volley request:" + request.toString());
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void triggerVolleyDeleteRequest(final Context context,String url,Response.Listener<String> responseListener,
                                               Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(Request.Method.DELETE, url, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences sPref= context.getSharedPreferences(Constant.PREF, Context.MODE_PRIVATE);
                String sessionToken=sPref.getString(Constant.SESSION_TOKEN, "");
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("X-fitnesspro-auth", sessionToken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }


}
