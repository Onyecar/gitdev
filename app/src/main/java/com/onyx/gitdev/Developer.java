package com.onyx.gitdev;

import android.graphics.Bitmap;
import com.google.gson.annotations.SerializedName;

/**
 * Created by onyekaanene on 15/03/2017.
 */

public class Developer {
    long _id;

    @SerializedName("login")
    String username;
    @SerializedName("html_url")
    String url;
    @SerializedName("avatar_url")
    String avatarUrl;

    Bitmap image;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
