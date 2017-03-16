package com.onyx.gitdev;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by onyekaanene on 15/03/2017.
 */

public class DeveloperHandler {

    Context context;
    public DeveloperHandler(Context context){
        this.context = context;
    }

    private int mDeveloperCount;

    public ArrayList<ContentProviderOperation> parse (String json) throws Exception{
        final ArrayList<ContentProviderOperation> batch = Lists.newArrayList();
        JSONArray developerArray;
        try {
            developerArray = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<List<Developer>>() {
        }.getType();
        ArrayList<Developer> developersJson = gson.fromJson(developerArray.toString(), listType);

        mDeveloperCount = developersJson.size();
        for(Developer developer : developersJson){

            fetch(developer, batch);
        }
        return batch;
    }
    private static void parseDeveloper(Developer driver,
                                    ArrayList<ContentProviderOperation> batch){
        Uri uri = DataContract.Developers.CONTENT_URI;
        Bitmap bmp = driver.getImage();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        ContentProviderOperation.Builder builder = ContentProviderOperation
                .newInsert(uri)
                .withValue(DataContract.Developers.USER_NAME, driver.getUsername())
                .withValue(DataContract.Developers.URL, driver.getUrl())
                .withValue(DataContract.Developers.IMAGE, byteArray);
        batch.add(builder.build());
    }
    private void fetch(final Developer developer, final ArrayList<ContentProviderOperation> batch){

        final String endpoint = developer.getAvatarUrl();
        Bitmap image;
        ImageRequest imgRequest
                = new ImageRequest(endpoint, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                developer.setImage(response);
                parseDeveloper(developer, batch);
                PrefUtils.markSetupDone(true, context);
            }
        }, 300, 200, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("user-agent", "gitdev-app");
                return header;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(imgRequest);
    }

    public int getmDeveloperCount() {
        return mDeveloperCount;
    }
}
