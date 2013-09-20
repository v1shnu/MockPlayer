package com.vishnu.mockplayer.models;

import android.net.Uri;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 20/9/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Screen {
    int screen_id;
    Uri image;

    public Screen(int screen_id, Uri image) {
        this.screen_id = screen_id;
        this.image = image;
    }

    public int getScreen_id() {
        return screen_id;
    }

    public void setScreen_id(int screen_id) {
        this.screen_id = screen_id;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
