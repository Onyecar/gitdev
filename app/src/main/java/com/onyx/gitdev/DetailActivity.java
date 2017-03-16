package com.onyx.gitdev;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements MasterFragment.Listener {

    private final static int mPages = 50;
    private static final String TAG = DetailActivity.class.getSimpleName();
    private PaymentPagerAdapter mPagerAdapter;
    private ViewPager mPager;
    Uri mSelectedDeveloper;
    private Toolbar mActionBarToolbar;

    public ArrayList<Long> mDevelopers;
    public static String ARG_DEVELOPER_LIST =
            "com.onyx.gitdev.extra.ARG_DEVELOPER_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle(null);
        if(findViewById(R.id.developerPane) != null){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Intent intent = getIntent();
        if(intent != null){
            mSelectedDeveloper = intent.getData();
            if( mSelectedDeveloper == null ) return;

            Bundle paymentsBundle = intent.getBundleExtra(ARG_DEVELOPER_LIST);
            if(paymentsBundle != null){

                mDevelopers = convertToArray(paymentsBundle, ARG_DEVELOPER_LIST);
            }
        }
        mPagerAdapter = new PaymentPagerAdapter(getSupportFragmentManager(), mDevelopers.size());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(mDevelopers.indexOf(
                Long.parseLong(mSelectedDeveloper.getLastPathSegment())));

        final Toolbar toolbar = getActionBarToolbar();
        if(toolbar == null) return;
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                toolbar.setTitle("Payment Details");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        return true;
    }


    /**
     * Implements PaymentDetailsFragment.Callbacks
     * @param paymentId
     * @param data
     */
    @Override
    public void onDeveloperSelected(long paymentId, Bundle data) {
        mPager.setCurrentItem(mDevelopers.indexOf(paymentId), true);
    }
    public ArrayList<Long> convertToArray(Bundle bundle, String key) {
        ArrayList<Long> items = new ArrayList<Long>();
        int n = 0;
        while (true) {
            long item = bundle.getLong(key + Integer.toString(n++), -1);
            // terminate once we cant retrieve items
            if (item == -1) break;
            items.add(item);
        }
        return items;
    }

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

    private class PaymentPagerAdapter extends FragmentStatePagerAdapter {

        private final int mSize;

        public PaymentPagerAdapter(FragmentManager fm, int size) {
            super(fm);
            mSize = size;
        }

        @Override
        public Fragment getItem(int position) {
            return DetailsFragment.newInstance(position, DetailActivity.this);
        }

        @Override
        public int getCount() {
            return mSize;
        }
    }

    @Override
    public void onFragmentAttached(ListFragment fragment) {

    }

    @Override
    public void onFragmentDetached(ListFragment fragment) {

    }

}
