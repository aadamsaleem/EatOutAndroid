package com.aadamsaleem.eatout.LoggedIn.Home;

/**
 * Created by aadamsaleem on 11/16/16.
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aadamsaleem.eatout.EventDetailActivity;
import com.aadamsaleem.eatout.R;
import com.aadamsaleem.eatout.client.CompletionInterface;
import com.aadamsaleem.eatout.client.EventManager;
import com.aadamsaleem.eatout.util.PrefUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NearbyFragment extends Fragment implements OnMapReadyCallback, LocationSource.OnLocationChangedListener, GoogleMap.OnMarkerClickListener{

    private static View rootView;
    private Context mContext;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;

    private int REQUEST_LOCATION_PERMISSION = 1;


    public NearbyFragment() {
    }

    public static NearbyFragment newInstance(Context context) {
        NearbyFragment fragment = new NearbyFragment();
        fragment.mContext = context;

        return fragment;
    }

    //region Override Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }

        try {
            rootView = inflater.inflate(R.layout.fragment_nearby, container, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

         mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.nearby_map);

        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(40.7128, -74.0059), 16));

        googleMap.setOnMarkerClickListener(this);

        getEvents();

        if (checkPermission()) {
            map.setMyLocationEnabled(true);

        } else
            requestPermission();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mapFragment.onDestroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    if (checkPermission()) {

                    } else {
                        Toast.makeText(mContext, "Permission denied to access location", Toast.LENGTH_SHORT).show();
                    }
                return;
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        CameraPosition camPos = new CameraPosition.Builder()

                .target(new LatLng(location.getLatitude(), location.getLongitude()))

                .zoom(15f)

                .build();

        CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

        googleMap.moveCamera(camUpdate);

    }
    //endregion

    //region Private Methods
    private boolean checkPermission() {

        return (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
    }

    private void getEvents(){


        EventManager.getPublicEvents(getContext(), new CompletionInterface() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray personalEvents = result.getJSONArray("PERSONAL_EVENTS");
                    for(int i = 0; i< personalEvents.length(); i++){
                        JSONObject json = personalEvents.getJSONObject(i);
                        JSONArray location = json.getJSONArray("EVENT_LOCATION");
                        addMarker(json.getString("EVENT_ID"), new LatLng(location.getDouble(0), location.getDouble(1)),0);
                    }

                    JSONArray publicEvents = result.getJSONArray("FRIENDS_EVENTS");
                    for(int i = 0; i< publicEvents.length(); i++){
                        JSONObject json = publicEvents.getJSONObject(i);
                        JSONArray location = json.getJSONArray("EVENT_LOCATION");
                        addMarker(json.getString("EVENT_ID"), new LatLng(location.getDouble(0), location.getDouble(1)),1);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void addMarker(String title, LatLng location, int type){

        if(type == 0) {
            googleMap.addMarker(new MarkerOptions().anchor(0.0f, 1.0f)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                    .position(location)).setTag(title);
        }
        else{
            googleMap.addMarker(new MarkerOptions().anchor(0.0f, 1.0f)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .position(location)).setTag(title);
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {


        Intent i = new Intent(getContext(), EventDetailActivity.class);
        i.putExtra("EVENT_ID", marker.getTag().toString());
        startActivity(i);

        JSONObject json = new JSONObject();
        try {
            json.put("EVENT_ID", marker.getTag().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        EventManager.getEventDetails(getContext(), json, new CompletionInterface() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.e("aaaa",""+result.toString());
            }

            @Override
            public void onFailure() {

            }
        });

        return false;
    }
    //endregion
}
