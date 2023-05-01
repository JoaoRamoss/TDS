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



import java.util.ArrayList;

public class TrailDetailsFragment extends Fragment implements BottomNavigationView.OnItemSelectedListener, OnMapReadyCallback {

    private ArrayList<LatLng> locations;
    private TextView trailTitle;
    private TextView trailDescription;
    private MapView mapView;
    private GoogleMap googleMap;

    private FusedLocationProviderClient fusedLocationClient;

    private Button startTrailButton;

    private BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        locations = new ArrayList<>();
        View view = inflater.inflate(R.layout.content_trail, container, false);

        trailTitle = view.findViewById(R.id.route_title);
        mapView = view.findViewById(R.id.map_view);
        startTrailButton = view.findViewById(R.id.start_route_button);

        Trail trail = (Trail) getArguments().getSerializable("selectedTrail");
        trailTitle.setText(trail.getTrail_name());
        //trailDescription.setText(trail.getTrail_desc());

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
                openGoogleMapsNavigation(locations);
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

        Trail trail = (Trail) getArguments().getSerializable("selectedTrail");

        //adicionar localizacao antes disto tudo


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


    private void requestLocationPermission() {
        ArrayList<LatLng> locations = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            getCurrentLocation(new LocationCallback() {
                @Override
                public void onLocationReceived(LatLng currentLocation) {
                    //locations.add(0, currentLocation); // Adicione a localização atual como o primeiro ponto da rota
                }
            });
        }

       // retun locations.get(0);
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
                            // Trate a falha ao obter a localização atual
                        }
                    });
        }
    }



    private void openGoogleMapsNavigation(ArrayList<LatLng> locations) {
        if (locations != null && locations.size() > 1) {
            StringBuilder waypoints = new StringBuilder("https://www.google.com/maps/dir/?api=1");

            for (int i = 0; i < locations.size(); i++) {
                waypoints.append("&waypoints=").append(locations.get(i).latitude).append(",").append(locations.get(i).longitude);
            }

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
