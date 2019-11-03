package com.openhack.hackacross;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.ClusterManager.OnClusterClickListener;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class OwnIconRendered extends DefaultClusterRenderer<MyItem> implements OnClusterClickListener<MyItem>, OnInfoWindowClickListener {

    private final IconGenerator mClusterIconGenerator;
    private final Context mContext;
    private final View clusterItemView;
    private LayoutInflater layoutInflater;

    public OwnIconRendered(Context context, GoogleMap map,
                           ClusterManager<MyItem> clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
        layoutInflater = LayoutInflater.from(mContext);

        clusterItemView = layoutInflater.inflate(R.layout.single_cluster_marker_view, null);

        mClusterIconGenerator = new IconGenerator(mContext);
        Drawable drawable = ContextCompat.getDrawable(mContext, android.R.color.transparent);
        mClusterIconGenerator.setBackground(drawable);
        mClusterIconGenerator.setContentView(clusterItemView);

        clusterManager.setOnClusterClickListener(this);

        map.setInfoWindowAdapter(clusterManager.getMarkerManager());

        map.setOnInfoWindowClickListener(this);

        clusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyCustomClusterItemInfoView());

        map.setOnCameraIdleListener(clusterManager);

        map.setOnMarkerClickListener(clusterManager);
    }

    @Override
    protected void onClusterItemRendered(MyItem clusterItem, Marker marker) {
        marker.setTag(clusterItem);
    }

    @Override
    protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
        markerOptions.icon(item.getIcon());
        markerOptions.snippet(item.getSnippet());
        markerOptions.title(item.getTitle());
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<MyItem> cluster,
                                           MarkerOptions markerOptions) {
        TextView singleClusterMarkerSizeTextView = clusterItemView.findViewById(R.id.singleClusterMarkerSizeTextView);
        singleClusterMarkerSizeTextView.setText(String.valueOf(cluster.getSize()));
        Bitmap icon = mClusterIconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        MyItem myItem = (MyItem) marker.getTag(); //  handle the clicked marker object
        if (mContext != null && myItem != null)
            Toast.makeText(mContext, myItem.getTitle(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onClusterClick(Cluster<MyItem> cluster) {
//        if (cluster == null) return false;
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        for (MyItem myItem : cluster.getItems())
//            builder.include(myItem.getPosition());
//        LatLngBounds bounds = builder.build();
        return true;
    }

    private class MyCustomClusterItemInfoView implements GoogleMap.InfoWindowAdapter {

        private final View clusterItemView;

        MyCustomClusterItemInfoView() {
            clusterItemView = layoutInflater.inflate(R.layout.marker_info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            MyItem user = (MyItem) marker.getTag();
            if (user == null) return clusterItemView;
            TextView itemNameTextView = clusterItemView.findViewById(R.id.itemNameTextView);
            TextView itemAddressTextView = clusterItemView.findViewById(R.id.itemAddressTextView);
            itemNameTextView.setText(marker.getTitle());
            itemAddressTextView.setText(user.getTitle());
            return clusterItemView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }
}