package com.openhack.hackacross;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.openhack.hackacross.data.CrissAPI;
import com.openhack.hackacross.data.NetworkClient;
import com.openhack.hackacross.models.CrissData;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Spinner groupsSpinner;
    Spinner cropsSpinner;
    CheckBox showFires, showAccidents;
    TileOverlay mOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        groupsSpinner = findViewById(R.id.groupsSpinner);
        cropsSpinner = findViewById(R.id.cropsSpinner);

        showFires = findViewById(R.id.showfires);
        showAccidents = findViewById(R.id.showaccidents);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(60.503186, 18.126446), 5.0f));
        mClusterManager = new ClusterManager<MyItem>(this, mMap);
        showFires.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fetchCrissDetails();
                } else {
                    clearView();
                }
            }
        });
        showAccidents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fetchAccidentsDetails();
                } else {
                    clearView();
                }
            }
        });

    }

    private void clearView() {
        mClusterManager.getAlgorithm().clearItems();
        mClusterManager.cluster();
    }


    private ArrayList<LatLng> readFireItems(List<CrissData> response, int image) throws JSONException {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        MyItem offsetItem = null;
        String title = "This is FIRE";
        String snippet = "and this is the snippet.";
        for (CrissData criss : response) {
            double lat = criss.getLat();
            double lng = criss.getLng();
            list.add(new LatLng(lat, lng));
            offsetItem = new MyItem(lat, lng, title, snippet, image);
            mClusterManager.addItem(offsetItem);
        }
        mClusterManager.setRenderer(new OwnIconRendered(getApplicationContext(), mMap, mClusterManager));
        return list;
    }

    private ClusterManager<MyItem> mClusterManager;
    HeatmapTileProvider mProvider;

    private void setUpClusterer(int resource) {
        // Position the map.
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)


        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {
                //put your code here

                return false;
            }
        });

    }

    private void fetchCrissDetails() {
        //Obtain an instance of Retrofit by calling the static method.
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        /*
        The main purpose of Retrofit is to create HTTP calls from the Java interface based on the annotation associated with each method. This is achieved by just passing the interface class as parameter to the create method
        */
        CrissAPI crissAPI = retrofit.create(CrissAPI.class);
        /*
        Invoke the method corresponding to the HTTP request which will return a Call object. This Call object will used to send the actual network request with the specified parameters
        */
        Call call = crissAPI.getFireData();
        /*
        This is the line which actually sends a network request. Calling enqueue() executes a call asynchronously. It has two callback listeners which will invoked on the main thread
        */
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    List<CrissData> crissResponse = (List<CrissData>) response.body();
                    setUpClusterer(R.drawable.fire);
                    try {
                        readFireItems(crissResponse, R.drawable.fire);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void fetchAccidentsDetails() {
        //Obtain an instance of Retrofit by calling the static method.
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        /*
        The main purpose of Retrofit is to create HTTP calls from the Java interface based on the annotation associated with each method. This is achieved by just passing the interface class as parameter to the create method
        */
        CrissAPI crissAPI = retrofit.create(CrissAPI.class);
        /*
        Invoke the method corresponding to the HTTP request which will return a Call object. This Call object will used to send the actual network request with the specified parameters
        */
        Call call = crissAPI.getAccidentsData();
        /*
        This is the line which actually sends a network request. Calling enqueue() executes a call asynchronously. It has two callback listeners which will invoked on the main thread
        */
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    List<CrissData> crissResponse = (List<CrissData>) response.body();
                    setUpClusterer(R.drawable.fire);
                    try {
                        readFireItems(crissResponse, R.drawable.accident);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }
}
