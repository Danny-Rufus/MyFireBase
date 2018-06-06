package com.example.paul.myfirebase;

import com.google.firebase.database.Exclude;

/**
 * Created by PAUL on 17-05-2018.
 */

public class Upload {
    private String mName;
    private String mImageUrl;
    private String mKey;

    public Upload() {
        //Empty constructor needed

    }

    public Upload(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No name";
        }

        mName = name;
        mImageUrl = imageUrl;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
@Exclude
    public String getmKey() {
        return mKey;
    }
@Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
