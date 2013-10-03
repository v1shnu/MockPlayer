package com.vishnu.mockplayer.models;

import android.net.Uri;

public class Screen {
    int screenId;
    Uri image;

    public Screen(int screenId, Uri image) {
        this.screenId = screenId;
        this.image = image;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
