package com.onyx.gitdev;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by onyekaanene on 15/03/2017.
 */

public class DeveloperCursorAdapter extends CursorAdapter {

    private int mLayoutResource;
    private int lastAnimatedPosition = -1;
    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    public DeveloperCursorAdapter(Context context, Cursor c, int layoutResource) {
        super(context, c, 0);
        mLayoutResource = layoutResource;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(mLayoutResource, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        runEnterAnimation(view, getCursor().getPosition());
        ImageView devImage = (ImageView) view.findViewById(R.id.dev_image);
        byte [] blob = cursor.getBlob(cursor.getColumnIndex(DataContract.Developers.IMAGE));
        devImage.setImageBitmap(BitmapFactory.decodeByteArray(blob, 0, blob.length));
        TextView devName = (TextView) view.findViewById(R.id.dev_name);
        devName.setText(cursor.getString(
                cursor.getColumnIndex(DataContract.Developers.USER_NAME)));

    }
    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    })
                    .start();
        }
    }
}
