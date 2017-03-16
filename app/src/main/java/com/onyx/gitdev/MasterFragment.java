package com.onyx.gitdev;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Splashers on 06/05/2014.
 */
public class MasterFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private DeveloperCursorAdapter mListAdapter;
    private Bundle mDevelopersBundle = Bundle.EMPTY;

    private Listener mCallback;
    private ViewGroup mPhoneDeveloperSyncView;
    private TextView mDeveloperMessageView;
    private ProgressBar mPaymentprogressBar;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Initialize loader
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new DeveloperCursorAdapter(getActivity(), null, R.layout.developer_item);

        setListAdapter(mListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_list_with_empty_container, container, false);

        //sync waiting timer - mR
        mPhoneDeveloperSyncView =  (ViewGroup) rootView.findViewById(android.R.id.empty);
        inflater.inflate(R.layout.empty_waiting_for_sync,
                mPhoneDeveloperSyncView, true);
        mDeveloperMessageView = (TextView) rootView.findViewById(R.id.developer_status_message);
        mPaymentprogressBar = (ProgressBar) rootView.findViewById(R.id.developer_progress_bar);
        mDeveloperMessageView.setText("Waiting for sync...");

        ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        listView.setItemsCanFocus(true);
        listView.setCacheColorHint(Color.WHITE);
        listView.setSelector(R.drawable.list_selector);
        listView.setVerticalScrollBarEnabled(false);
        listView.setDividerHeight(0);
        //listView.setSelector(android.R.color.transparent);
        return  rootView;
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String whereClause = null;
        return new CursorLoader(
                getActivity(),
                DataContract.Developers.CONTENT_URI,
                DataContract.Developers.PROJECTION_ALL,
                whereClause,    // selection
                null,           // arguments
                null);          // use default sort order in provider
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Bundle bundle = new Bundle();

        mListAdapter.swapCursor(data);
        mListAdapter.notifyDataSetChanged();

        mDevelopersBundle = bundle;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mDevelopersBundle.isEmpty() ) {
                    mDeveloperMessageView.setText("No Developer available");
                    mPaymentprogressBar.setVisibility(View.GONE);

                }
            }
        }, 20 * 1000);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        final Cursor cursor = (Cursor) mListAdapter.getItem(position);
        if(cursor != null) {
            long paymentId = cursor.getLong(
                    cursor.getColumnIndex(DataContract.Developers._ID));
            mCallback.onDeveloperSelected(paymentId, mDevelopersBundle);


        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //empty out cursur
        mListAdapter.swapCursor(null);
    }

    private final ContentObserver mObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            if(!isAdded()){
                return;
            }
            getLoaderManager().restartLoader(0, null, MasterFragment.this);
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof Listener){
            mCallback = (Listener) activity;
            mCallback.onFragmentAttached(this);
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement " + Listener.class.getSimpleName());
        }
        activity.getContentResolver().registerContentObserver(
                DataContract.Developers.CONTENT_URI, true, mObserver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getActivity() instanceof Listener) {
            ((Listener) getActivity()).onFragmentDetached(this);
        }

    }

    interface Listener {
        public void onDeveloperSelected(long paymentId, Bundle data);
        public void onFragmentAttached(ListFragment fragment);
        public void onFragmentDetached(ListFragment fragment);
    }
}
