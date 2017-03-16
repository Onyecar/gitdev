package com.onyx.gitdev.data.webservice;

import com.onyx.gitdev.data.model.GitApiResponse;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Morph-Deji on 15-Mar-17.
 */

public class GithubService implements IGithubService{
    GithubClient mRetrofitClient;

    GithubEndpoint mApiEndpoint;

    public GithubService() {
        this.mRetrofitClient = new GithubClient();
        mApiEndpoint = mRetrofitClient.getService();
    }


    @Override
    public void getDevelopers(String location, int page, final GithubServiceCallback<GitApiResponse> callback) {
        Timber.d("onyx:calling retrofit");
        Call<GitApiResponse> developerCall = new GithubClient().getService().getDevelopers("location:Lagos");
        developerCall.enqueue(new Callback<GitApiResponse>() {
            @Override
            public void onResponse(Call<GitApiResponse> call, Response<GitApiResponse> response) {
                Timber.d("onyx:respones, %s", response.body());
                if(response.isSuccessful() && response.code() == HttpURLConnection.HTTP_OK){
                    callback.onLoaded(response.body());
                }else {
                    callback.onError("Something went wrong");
                }

            }

            @Override
            public void onFailure(Call<GitApiResponse> call, Throwable t) {
                Timber.d("onyx:retrofit error, %s", t.getMessage());
                callback.onError("Not really your fault, probably poor internet connection");
            }
        });
    }
}
