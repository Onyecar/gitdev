package com.onyx.gitdev;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, ObservableScrollView.Callbacks {

    //private Uri mPaymentUri;
    public static final String ARGS_URI =
            "com.onyx.gitdev.extras.DEVELOPERS_URI";
    public static final String ARGS_DEVELOPER_ID =
            "com.onyx.gitdev.extras.DEVELOPERS_ID";
    private static final String TAG = DetailsFragment.class.getSimpleName();
    private static final float GAP_FILL_DISTANCE_MULTIPLIER = 1.5f;
    private static final float PHOTO_ASPECT_RATIO = 1.7777777f;
    private Uri dataUri = DataContract.Developers.CONTENT_URI;
    private TextView devName, devUrl;
    private ImageView devImage;
    private View mMakePaymentButton;
    private View mHeaderBox;
    private View mHeaderContentBox;
    private View mHeaderBackgroundBox;
    private View mHeaderShadow;
    private View mDetailsContainer;
    private View mScrollViewChild;
    private ObservableScrollView mScrollView;
    private int mHeaderTopClearance;
    private int mPhotoHeightPixels;
    private int mHeaderHeightPixels;
    private int mAddScheduleButtonHeightPixels;
    private boolean mHasPhoto = false;
    private View mPhotoViewContainer;
    private ImageView mPhotoView;
    private int mPlanColor;
    private Handler mHandler;
    private float mMaxHeaderElevation;
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener
            = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            //mAddScheduleButtonHeightPixels = makePaymentButton.getHeight();
            recomputePhotoAndScrollingMetrics();
        }
    };

    public static DetailsFragment newInstance(int position,
                                                    DetailActivity hostActivity) {
        DetailsFragment fragment =
                new DetailsFragment();
        Bundle args = new Bundle();
        long _id = hostActivity.mDevelopers.get(position);
        args.putLong(ARGS_DEVELOPER_ID, _id);
        fragment.setArguments(args);
        return fragment;
    }

    public static DetailsFragment newInstance(String paymentId,
                                                    DetailActivity hostActivity) {
        DetailsFragment fragment =
                new DetailsFragment();
        Bundle args = new Bundle();
        //long _id = hostActivity.mPayments.get(position);
        args.putLong(ARGS_DEVELOPER_ID, Long.parseLong(paymentId));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        Bundle args = getArguments();
        if (args != null) {
            String paymentId = Long.toString(args.getLong(ARGS_DEVELOPER_ID));
            dataUri = dataUri.buildUpon().appendPath(paymentId).build();
        }
        mMaxHeaderElevation = getResources().getDimensionPixelSize(
                R.dimen.default_elevation);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(PaymentQuery._TOKEN, Bundle.EMPTY, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup rootView =
                (ViewGroup) inflater.inflate(R.layout.fragment_details, container, false);

        devName = (TextView) rootView.findViewById(R.id.dev_name);
        devUrl = (TextView) rootView.findViewById(R.id.dev_url);
        devImage = (ImageView) rootView.findViewById(R.id.dev_image);



        /*HeaderView*/
        mScrollViewChild = rootView.findViewById(R.id.scroll_view_child);
        mScrollViewChild.setVisibility(View.INVISIBLE);

        // set toolbar
        final Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_up);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    toolbar.setTitle("Payment Details");
                }
            });
        }
        setupCustomScrolling(rootView);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                dataUri,
                DataContract.Developers.PROJECTION_ALL,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            buildUIfromCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void buildUIfromCursor(Cursor cursor) {
        if (getActivity() == null) {
            return;
        }

        if (!cursor.moveToFirst()) {
            return;
        }


        String strDevName = cursor.getString(
                cursor.getColumnIndex(DataContract.Developers.USER_NAME));
        devName.setText(strDevName);


        String strDevUrl = cursor.getString(
                cursor.getColumnIndex(DataContract.Developers.URL));
        devUrl.setText(strDevUrl);

        byte [] blob = cursor.getBlob(cursor.getColumnIndex(DataContract.Developers.IMAGE));
        devImage.setImageBitmap(BitmapFactory.decodeByteArray(blob, 0, blob.length));

        mHasPhoto = false;
        if (mHasPhoto) {
            recomputePhotoAndScrollingMetrics();
        } else {
            recomputePhotoAndScrollingMetrics();
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onScrollChanged(0, 0); // trigger scroll handling
                mScrollViewChild.setVisibility(View.VISIBLE);
                //mPlanDescription.setTextIsSelectable(true);
            }
        });

    }

    private void recomputePhotoAndScrollingMetrics() {
        mHeaderHeightPixels = mHeaderBox.getHeight();

        mPhotoHeightPixels = 0;
        if (mHasPhoto) {
            mPhotoHeightPixels = (int) (mPhotoView.getWidth() / PHOTO_ASPECT_RATIO);
            mPhotoHeightPixels = Math.min(mPhotoHeightPixels, mScrollView.getHeight() * 2 / 3);
        }

        ViewGroup.LayoutParams lp;
        lp = mPhotoViewContainer.getLayoutParams();
        if (lp.height != mPhotoHeightPixels) {
            lp.height = mPhotoHeightPixels;
            mPhotoViewContainer.setLayoutParams(lp);
        }

        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)
                mDetailsContainer.getLayoutParams();
        if (mlp.topMargin != mHeaderHeightPixels + mPhotoHeightPixels) {
            mlp.topMargin = mHeaderHeightPixels + mPhotoHeightPixels;
            mDetailsContainer.setLayoutParams(mlp);
        }

        onScrollChanged(0, 0); // trigger scroll handling
    }

    @Override
    public void onScrollChanged(int deltaX, int deltaY) {
        // Reposition the header bar -- it's normally anchored to the top of the content,
        // but locks to the top of the screen on scroll
        int scrollY = mScrollView.getScrollY();

        float newTop = Math.max(mPhotoHeightPixels, scrollY);
        mHeaderBox.setTranslationY(newTop);
        /*makePaymentButton.setTranslationY(newTop + mHeaderHeightPixels
                - mAddScheduleButtonHeightPixels / 2);*/

        float gapFillProgress = 1;
        if (mPhotoHeightPixels != 0) {
            gapFillProgress = Math.min(Math.max(getProgress(scrollY,
                    0,
                    mPhotoHeightPixels), 0), 1);
        }

        ViewCompat.setElevation(mHeaderBox, gapFillProgress * mMaxHeaderElevation);
       /* ViewCompat.setElevation(makePaymentButton, gapFillProgress * mMaxHeaderElevation
                + mFABElevation);*/

        // Move background photo (parallax effect)
        mPhotoViewContainer.setTranslationY(scrollY * 0.5f);
    }

    private void setupCustomScrolling(View rootView) {
        mScrollView = (ObservableScrollView) rootView.findViewById(R.id.scroll_view);
        mScrollView.addCallbacks(this);
        ViewTreeObserver vto = mScrollView.getViewTreeObserver();
        if (vto.isAlive()) {
            vto.addOnGlobalLayoutListener(mGlobalLayoutListener);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mScrollView == null) {
            return;
        }

        ViewTreeObserver vto = mScrollView.getViewTreeObserver();
        if (vto.isAlive()) {
            vto.removeGlobalOnLayoutListener(mGlobalLayoutListener);
        }
    }

    interface PaymentQuery {
        int _TOKEN = 0x51;
    }
    public static float getProgress(int value, int min, int max) {
        if (min == max) {
            throw new IllegalArgumentException("Max (" + max + ") cannot equal min (" + min + ")");
        }

        return (value - min) / (float) (max - min);
    }
}
