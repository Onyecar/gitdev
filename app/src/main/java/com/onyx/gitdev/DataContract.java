package com.onyx.gitdev;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by onyekaanene on 15/03/2017.
 */

public class DataContract {

    /**
     * Authority of data provider
     */
    public static final String CONTENT_AUTHORITY = "com.onyx.gitdev";

    /**
     * Authority base URI
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);
    public static final String PATH_DEVELOPERS = "developers";

    public static final class Developers implements DeveloperColumns, BaseColumns {
        public static final Uri CONTENT_URI  =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DEVELOPERS).build();

        /** The mime type of a single item */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE
                        + "/vnd.com.onyx.gitdev.developer";

        /** The mime type of a single item */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE
                        + "/vnd.com.onyx.gitdev.developer";

        /** A projection of all tables in payment table */
        public static final String[] PROJECTION_ALL = {
                _ID, URL, IMAGE, USER_NAME
        };

        /** The default sort order for queries containing PaymentDate */
        public static final String SORT_ORDER_DEFAULT =
                BaseColumns._ID +" DESC";

        public static Uri buildDeveloperUri(long paymentId){
            return CONTENT_URI.buildUpon().appendPath(Long.toString(paymentId)).build();
        }


    }
    public interface DeveloperColumns {
        String USER_NAME = "UserName";
        String IMAGE = "Image";
        String URL = "url";
    }
}
