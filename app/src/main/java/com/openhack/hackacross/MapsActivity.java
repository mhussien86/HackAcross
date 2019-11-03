package com.openhack.hackacross;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.openhack.hackacross.adapter.CustomInfoViewAdapter;
import com.openhack.hackacross.data.CrissAPI;
import com.openhack.hackacross.data.NetworkClient;
import com.openhack.hackacross.models.CrissData;

import org.json.JSONException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    Spinner groupsSpinner;
    Spinner cropsSpinner;
    CheckBox showFires, showAccidents;
    private ClusterManager<MyItem> mClusterManager;

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(60.503186, 18.126446), 5.0f));
        mMap.getUiSettings().setZoomControlsEnabled(true);
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


    private void readFireItems(List<CrissData> response, int image) {
        MyItem offsetItem = null;
        String title = "This is FIRE";
        String snippet = "and this is the snippet.";

        for (CrissData criss : response) {
            double lat = criss.getLat();
            double lng = criss.getLng();
            offsetItem = new MyItem(lat, lng, title, snippet, image);
            mClusterManager.addItem(offsetItem);
        }
        mClusterManager.cluster();

    }

    private void setUpClusterer() {
        // Position the map.
        mClusterManager = new ClusterManager<MyItem>(this, mMap);

        OwnIconRendered ownIconRendered = new OwnIconRendered(getApplicationContext(), mMap, mClusterManager);
        mClusterManager.setRenderer(ownIconRendered);

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
                    setUpClusterer();
                    readFireItems(crissResponse, R.drawable.fire);

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
                    setUpClusterer();
                    readFireItems(crissResponse, R.drawable.accident);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

}
