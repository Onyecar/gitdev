package com.onyx.gitdev;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.util.Log;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by onyekaanene on 15/03/2017.
 */

public class SyncHelper {

    private Context context;

    public SyncHelper(Context context) {
        this.context = context;
    }
    public String fetch(){

        String endpoint = "https://api.github.com/search/users?q=location:Lagos";
        final ArrayList<ContentProviderOperation> batch = new ArrayList<>();
        StringRequest stringRequest
                = new StringRequest(Request.Method.GET, endpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String developerJson = String.valueOf(json.getJSONArray("items"));
                    try {
                        DeveloperHandler developerHandler = new DeveloperHandler(context);
                        ArrayList<ContentProviderOperation> developerOperations =
                                null;
                        try {
                            developerOperations = developerHandler.parse(developerJson);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(developerOperations!=null)
                            batch.addAll(developerOperations);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                    }}
            }}) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("user-agent", "gitdev-app");
                return header;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        return stringRequest.toString();
    }
}
