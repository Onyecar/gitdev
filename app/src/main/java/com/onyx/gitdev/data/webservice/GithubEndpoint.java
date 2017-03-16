package com.onyx.gitdev.data.webservice;

import com.onyx.gitdev.data.model.GitApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Morph-Deji on 15-Mar-17.
 */

public interface GithubEndpoint {
    @GET("search/users")
    Call<GitApiResponse> getDevelopers(@Query("q") String query);
}
