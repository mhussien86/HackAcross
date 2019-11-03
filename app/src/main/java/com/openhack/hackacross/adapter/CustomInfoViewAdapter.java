package com.openhack.hackacross.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.openhack.hackacross.R;

public class CustomInfoViewAdapter implements GoogleMap.InfoWindowAdapter {

    private final LayoutInflater mInflater;

    public CustomInfoViewAdapter(LayoutInflater inflater) {
        this.mInflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        final View popup = mInflater.inflate(R.layout.info_window_layout, null);
        ((TextView) popup.findViewById(R.id.title)).setText(marker.getTitle());
        return popup;
    }

    @Override
    public View getInfoContents(Marker marker) {
//        final View popup = mInflater.inflate(R.layout.info_window_layout, null);
//
//        ((TextView) popup.findViewById(R.id.title)).setText(marker.getSnippet());

        return null;
    }
}
