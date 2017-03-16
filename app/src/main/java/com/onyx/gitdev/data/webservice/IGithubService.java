package com.onyx.gitdev.data.webservice;

import com.onyx.gitdev.data.model.GitApiResponse;

/**
 * Created by Morph-Deji on 15-Mar-17.
 */

public interface IGithubService {
    interface GithubServiceCallback<T> {

        void onLoaded(T data);
        void onError(String message);
    }

    void getDevelopers(String location, int page, GithubServiceCallback<GitApiResponse> callback);
}
