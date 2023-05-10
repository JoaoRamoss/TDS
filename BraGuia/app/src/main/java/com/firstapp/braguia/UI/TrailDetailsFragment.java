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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.firstapp.braguia.Model.Edge;
import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.Model.User;
import com.firstapp.braguia.R;
import com.firstapp.braguia.ViewModel.ViewModel;
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

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import android.location.Location;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TrailDetailsFragment extends Fragment implements BottomNavigationView.OnItemSelectedListener, OnMapReadyCallback {

    private ArrayList<LatLng> locations;

    private Button moreButton;
    private TextView trailTitle;
    private TextView trailDescription;
    private MapView mapView;
    private GoogleMap googleMap;

    private FusedLocationProviderClient fusedLocationClient;

    private ViewModel viewmodel;

    private Button startTrailButton;

    private BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        locations = new ArrayList<>();
        View view = inflater.inflate(R.layout.content_trail, container, false);

        this.viewmodel = new ViewModelProvider(this).get(ViewModel.class);

        trailTitle = view.findViewById(R.id.route_title);
        mapView = view.findViewById(R.id.map_view);
        startTrailButton = view.findViewById(R.id.start_route_button);
        moreButton = view.findViewById(R.id.info);

        Trail trail = (Trail) requireArguments().getSerializable("selectedTrail");
        trailTitle.setText(trail.getTrail_name());

        Button backArrow = view.findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        //trailDescription.setText(trail.getTrail_desc());

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Trail selectedTrail = trail;
                        Bundle bundle = new Bundle();
                bundle.putSerializable("selectedTrail", (Serializable) selectedTrail);
                Navigation.findNavController(view).navigate(R.id.action_trailDetailsFragment_to_trail_media, bundle);
            }
        });
        // Inicializar o MapView
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //inicializar o  FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);

        // Set the default selected item
        bottomNavigationView.setSelectedItemId(R.id.trailDetailsFragment);

        startTrailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Place trail in trails history
                assert getArguments() != null;
                Trail trail = (Trail) getArguments().getSerializable("selectedTrail");
                viewmodel.insert(trail);

                openGoogleMapsNavigation(locations);
            }
        });

        viewmodel.getLocalUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (!user.getUserType().equals("Premium")){
                    startTrailButton.setVisibility(View.GONE);
                }
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
        requestLocationPermission();
    }



    private void setupMap() {
        googleMap.addMarker(new MarkerOptions().position(locations.get(0)));

        Trail trail = (Trail) getArguments().getSerializable("selectedTrail");

        for (Edge e : trail.getEdges()) {
            LatLng trailLocation_start = new LatLng(e.getEdge_start().getPin_lat(), e.getEdge_start().getPin_lng());
            LatLng trailLocation_end = new LatLng(e.getEdge_end().getPin_lat(), e.getEdge_end().getPin_lng());
            locations.add(trailLocation_start);
            locations.add(trailLocation_end);
            googleMap.addMarker(new MarkerOptions().position(trailLocation_start));
            googleMap.addMarker(new MarkerOptions().position(trailLocation_end));
        }

        // Polyline to define the itinerary
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(locations)
                .width(5f)
                .color(Color.parseColor("#d83349"));

        googleMap.addPolyline(polylineOptions);

        // Set Camera
        float zoomLevel = 11.0f;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations.get(0), zoomLevel));
    }


    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            getCurrentLocation(new LocationCallback() {
                @Override
                public void onLocationReceived(LatLng currentLocation) {
                    locations.add(0,currentLocation);
                    setupMap();
                }
            });
        }
    }

    public interface LocationCallback {
        void onLocationReceived(LatLng currentLocation);
    }

    private void getCurrentLocation(LocationCallback callback) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                callback.onLocationReceived(currentLocation);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // O que fazer quando falha?
                        }
                    });
        }
    }





    private void openGoogleMapsNavigation(ArrayList<LatLng> locations) {
        if (locations != null && locations.size() > 1) {
            StringBuilder waypoints = new StringBuilder("https://www.google.com/maps/dir/?api=1");

            // Append origin and destination
            waypoints.append("&origin=").append(locations.get(0).latitude).append(",").append(locations.get(0).longitude);
            waypoints.append("&destination=").append(locations.get(locations.size() - 1).latitude).append(",").append(locations.get(locations.size() - 1).longitude);

            // Append waypoints
            if (locations.size() > 2) {
                waypoints.append("&waypoints=");
                Set<String> uniqueWaypoints = new HashSet<String>(); // Store unique waypoints in a Set (Fixes issue where there was waypoint repetition)
                for (int i = 1; i < locations.size() - 1; i++) {
                    LatLng location = locations.get(i);
                    String coordinateString = location.latitude + "," + location.longitude;
                    if (!uniqueWaypoints.contains(coordinateString)) {
                        waypoints.append(coordinateString);
                        uniqueWaypoints.add(coordinateString);
                        if (i < locations.size() - 2) {
                            waypoints.append("|");
                        }
                    }
                }
            }

            // Append travel mode
            waypoints.append("&travelmode=driving");
            // Convert waypoints to Uri
            Uri gmmIntentUri = Uri.parse(waypoints.toString());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(getContext(), "Google Maps não está instalado", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "O trajeto não está disponível", Toast.LENGTH_SHORT).show();
        }
    }





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.route:
                // Handle the click on the Home menu item
                navigateToRouteFragment();
                return true;
            case R.id.home:
                // Handle the click on the Home menu item
                navigateToHomeFragment();
                return true;
            case R.id.emergency:
                // Handle the click on the Emergency menu item
                EmergencyCall.makeEmergencyCall(getContext());
                return true;
            case R.id.menu:
                navigateToMenuFragment();
                return true;
            default:
                return false;
        }
    }

    private void navigateToHomeFragment() {
        // Navigate to the HomeFragment
        Navigation.findNavController(getView()).navigate(R.id.action_trailDetailsFragment_to_FirstFragment);
    }

    private void navigateToRouteFragment() {
        // Navigate to the HomeFragment
        Navigation.findNavController(getView()).navigate(R.id.action_trailDetailsFragment_to_trailList);
    }

    private void navigateToMenuFragment(){
        Navigation.findNavController(this.getView()).navigate(R.id.action_trailDetailsFragment_to_MenuFragment);
    }
}
