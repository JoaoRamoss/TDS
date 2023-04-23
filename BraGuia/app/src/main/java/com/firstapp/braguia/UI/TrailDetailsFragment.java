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
        import androidx.navigation.fragment.NavHostFragment;

        import com.firstapp.braguia.Model.Trail;
        import com.firstapp.braguia.R;
        import com.google.android.gms.maps.MapView;
        import com.google.android.material.bottomnavigation.BottomNavigationView;

        public class TrailDetailsFragment extends Fragment implements BottomNavigationView.OnItemSelectedListener{

            private TextView trailTitle;
            private TextView trailDescription;
            private MapView mapView;
            private Button startTrailButton;

            private BottomNavigationView bottomNavigationView;

            @Nullable
            @Override
            public View onCreateView( LayoutInflater inflater,ViewGroup container,  Bundle savedInstanceState) {
                    View view = inflater.inflate(R.layout.content_trail, container, false);


                    trailTitle = view.findViewById(R.id.route_title);
                    trailDescription = view.findViewById(R.id.route_description);
                    //mapView = view.findViewById(R.id.map_view);
                    startTrailButton = view.findViewById(R.id.start_route_button);

                    Trail trail = (Trail) getArguments().getSerializable("selectedTrail");
                    trailTitle.setText(trail.getTrail_name());
                    trailDescription.setText(trail.getTrail_desc());

                    // inicializar o mapview e tratar do botão de iniciar a rota

                    bottomNavigationView = view.findViewById(R.id.bottom_navigation);
                    bottomNavigationView.setOnItemSelectedListener(this);

                    // Set the default selected item
                    bottomNavigationView.setSelectedItemId(R.id.trailDetailsFragment);


                    return view;
                }











            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        // Handle the click on the Home menu item
                        navigateToHomeFragment();
                        return true;
                    case R.id.emergency:
                        // Handle the click on the Emergency menu item
                        EmergencyCall.makeEmergencyCall(getContext());
                        return true;
                    case R.id.route:
                        //Handle the click on the Route menu item
                        navigateToRouteFragment();
                        return true;
                    default:
                        return false;
                }
            }

            private void navigateToHomeFragment() {
                // Navigate to the HomeFragment
                Navigation.findNavController(getView()).navigate(R.id.action_trailDetailsFragment_to_FistFragment);
            }

            private void navigateToRouteFragment() {
                // Navigate to the HomeFragment
                Navigation.findNavController(getView()).navigate(R.id.action_trailDetailsFragment_to_trailListFragment);
            }
        }
