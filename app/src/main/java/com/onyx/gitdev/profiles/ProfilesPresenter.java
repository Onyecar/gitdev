package com.onyx.gitdev.profiles;

import com.onyx.gitdev.data.model.GitApiResponse;
import com.onyx.gitdev.data.webservice.IGithubService;

import timber.log.Timber;

/**
 * Created by Morph-Deji on 15-Mar-17.
 */

public class ProfilesPresenter implements ProfilesContract.UserActions{
    private IGithubService mGithubService;
    private ProfilesContract.View mProfilesView;

    public ProfilesPresenter(IGithubService gitHubservice, ProfilesContract.View view) {
        this.mGithubService = gitHubservice;
        this.mProfilesView = view;
    }

    @Override
    public void loadDevelopers() {
        mProfilesView.showProgress(true);
        Timber.d("inside lagos");
        mGithubService.getDevelopers("Lagos", 1, new IGithubService.GithubServiceCallback<GitApiResponse>() {
            @Override
            public void onLoaded(GitApiResponse data) {
                mProfilesView.showProgress(false);
                mProfilesView.showDevelopers(data.getPersons());
            }

            @Override
            public void onError(String message) {
                mProfilesView.showProgress(false);
                mProfilesView.showError(message);
            }
        });
    }
}
