package com.example2017.android.clientsmap;

import android.content.Context;
import android.graphics.Point;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AdminMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Location mlocation;
    private LatLng postion;
    String name;
    private Map<String,Marker> markers;

    DatabaseReference users=FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // setup markers
        this.markers = new HashMap<String, Marker>();
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


        final DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("CustomerRequest");
        final DatabaseReference Users= FirebaseDatabase.getInstance().getReference().child("username");

        GeoFire geoFire=new GeoFire(db);

        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(29.975051, 31.287913), 100);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, final GeoLocation location) {
                // Add a marker in Sydney and move the camera

                // Add a new marker to the map

                Users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                        };
                        Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);


                         name=map.get(key);

                        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)));
                        marker.setTitle(name);
                        markers.put(key, marker);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }

            @Override
            public void onKeyExited(String key) {
                System.out.println(String.format("Key %s is no longer in the search area", key));

            }

            @Override
            public void onKeyMoved(final String key, final GeoLocation location) {
                System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));


                // Move the marker
                Marker marker = markers.get(key);
                if (marker != null) {

                    postion=new LatLng(location.latitude,location.longitude);

                    animateMarker(marker, postion,false);
                }

            }


            @Override
            public void onGeoQueryReady() {
                System.out.println("All initial data has been loaded and events have been fired!");


            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                System.err.println("There was an error with this query: " + error);
            }
        });



    }




    // Animation handler for old APIs without animation support
    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {

                        marker.setVisible(false);
                    } else {

                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    public  boolean checkInternetConnection(Context context)
    {
        try
        {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected())
                return true;
            else
                return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }



}
