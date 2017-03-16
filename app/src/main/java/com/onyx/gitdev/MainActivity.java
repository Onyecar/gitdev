package com.onyx.gitdev;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements MasterFragment.Listener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!PrefUtils.isSetupDone(getApplicationContext())){
            SyncHelper syncHelper = new SyncHelper(getApplicationContext());
            syncHelper.fetch();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    public void onDeveloperSelected(long developerId, Bundle data) {
        Intent detailIntent = new Intent(Intent.ACTION_VIEW);
        Uri uri = DataContract.Developers.buildDeveloperUri(developerId);
        detailIntent.setData(uri);
        detailIntent.putExtra(DetailActivity.ARG_DEVELOPER_LIST, data );
        startActivity(detailIntent);
    }

    @Override
    public void onFragmentAttached(ListFragment fragment) {

    }

    @Override
    public void onFragmentDetached(ListFragment fragment) {

    }
}
