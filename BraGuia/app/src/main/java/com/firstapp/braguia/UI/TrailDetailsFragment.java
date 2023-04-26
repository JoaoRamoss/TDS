package com.firstapp.braguia.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.firstapp.braguia.Model.Edge;
import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.graphics.Color;


import java.util.ArrayList;

public class TrailDetailsFragment extends Fragment implements BottomNavigationView.OnItemSelectedListener, OnMapReadyCallback {

    private TextView trailTitle;
    private TextView trailDescription;
    private MapView mapView;
    private GoogleMap googleMap;
    private Button startTrailButton;

    private BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_trail, container, false);

        trailTitle = view.findViewById(R.id.route_title);
        trailDescription = view.findViewById(R.id.route_description);
        mapView = view.findViewById(R.id.map_view);
        startTrailButton = view.findViewById(R.id.start_route_button);

        Trail trail = (Trail) getArguments().getSerializable("selectedTrail");
        trailTitle.setText(trail.getTrail_name());
        trailDescription.setText(trail.getTrail_desc());

        // Inicializar o MapView e tratar do botão de iniciar a rota
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);

        // Set the default selected item
        bottomNavigationView.setSelectedItemId(R.id.trailDetailsFragment);

        startTrailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Código para iniciar a rota
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        this.googleMap = googleMap;


        Trail trail = (Trail) getArguments().getSerializable("selectedTrail");

        ArrayList<LatLng> locations = new ArrayList<>();

        for(Edge e : trail.getEdges()) {
            LatLng trailLocation_start = new LatLng(e.getEdge_start().getPin_lat(),e.getEdge_start().getPin_lng());
            LatLng trailLocation_end = new LatLng(e.getEdge_end().getPin_lat(),e.getEdge_end().getPin_lng());
            locations.add(trailLocation_start);
            locations.add(trailLocation_end);
            googleMap.addMarker(new MarkerOptions().position(trailLocation_start));
            googleMap.addMarker(new MarkerOptions().position(trailLocation_end));
        }

        // Polyline to define the itenerary
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(locations)
                .width(5f) //
                .color(Color.parseColor("#d83349"));

        googleMap.addPolyline(polylineOptions);

        // set Camera
        float zoomLevel = 11.0f;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations.get(0), zoomLevel));
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
