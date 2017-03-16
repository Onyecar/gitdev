package com.onyx.gitdev.profiles;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onyx.gitdev.R;
import com.onyx.gitdev.Util;
import com.onyx.gitdev.data.adapters.PersonRecyclerViewAdapter;
import com.onyx.gitdev.data.model.Person;
import com.onyx.gitdev.data.webservice.GithubService;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilesFragment extends Fragment
        implements ProfilesContract.View,
        PersonRecyclerViewAdapter.PersonInteractionListener {

    private ProfilesContract.UserActions mActionListener;

    private ArrayList<Person> mPersons;

    private PersonRecyclerViewAdapter mAdapter;

    public ProfilesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionListener = new ProfilesPresenter(new GithubService(), this);

        mPersons = new ArrayList<>();

        mAdapter = new PersonRecyclerViewAdapter(getContext(), mPersons, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profiles, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        Context context = view.getContext();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mAdapter);

        //statesRecyclerViewAdapter = new MyStatesAdapter(mAdapter);
        //recyclerView.setAdapter(statesRecyclerViewAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            requestContentRefresh();
        }
        if (mPersons.isEmpty()) {
            showEmptyContent(getString(R.string.welcome_to_gitdev));
        }
    }

    private void requestContentRefresh() {
        if (!Util.isOnline(getActivity())) {
            //mActionListener.notifyNoConnection(getString(R.string.no_internet_connection));
            return;
        }

        //onRefreshingStateChanged(true);
        Timber.d("start loading developers");
        mActionListener.loadDevelopers();
    }

    @Override
    public void showDevelopers(ArrayList<Person> persons) {
        if (!isAdded()) return;
        Timber.d("persons count = %d", mPersons.size());
        //statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL, null);
        mPersons = new ArrayList<>(persons);
        mAdapter.setData(mPersons);
    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void onOrderClick(View view, int position, Person order) {

    }

    @Override
    public void showEmptyContent(String message) {

    }

    @Override
    public void showError(String message) {

    }
}
