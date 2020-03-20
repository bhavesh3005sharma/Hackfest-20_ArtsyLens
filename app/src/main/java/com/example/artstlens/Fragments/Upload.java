package com.example.artstlens.Fragments;

import com.google.firebase.database.Exclude;

public class Upload {
    public Upload() {
    }

    public Upload(String category, String uri) {
        this.category = category;
        this.uri = uri;
    }

    public String getCategory() {
        return category;
    }

    public String getUri() {
        return uri;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    private String category;
    private String uri;

    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    @Exclude
    public String getmKey() {
        return mKey;
    }

    private String mKey;
}
