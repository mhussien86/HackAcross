package com.openhack.hackacross;

import androidx.annotation.DrawableRes;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem {

    private final LatLng mPosition;
    private String mTitle;
    private String mSnippet;
    private int mDrawable;

    public MyItem(double lat, double lng, String title, String snippet, int drawable) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        mDrawable = drawable;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public BitmapDescriptor getIcon() {
        return BitmapDescriptorFactory.fromResource(mDrawable);
    }
}
