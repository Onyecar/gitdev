package com.onyx.gitdev.data.webservice;

/**
 * Created by Morph-Deji on 15-Mar-17.
 */

import android.content.Context;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Configures retrofit
 */
public class GithubClient {
    private GithubEndpoint service;
    private Context context;

    public GithubClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(new OkHttpClient())
                //.addConverterFactory(new StringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient().create()))
                .build();

        service = retrofit.create(GithubEndpoint.class);
    }

    public GithubEndpoint getService() {
        return service;
    }
}
