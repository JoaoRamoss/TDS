package com.firstapp.braguia.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TrailMediaFragment extends Fragment implements BottomNavigationView.OnItemSelectedListener {

    private Button backArrow;
    Trail selectedTrail;
    TextView description;
    private BottomNavigationView bottomNavigationView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trail_media, container, false);

        backArrow = view.findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        description = view.findViewById(R.id.description);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);

        this.selectedTrail = (Trail) requireArguments().getSerializable("selectedTrail");

        this.description.setText(selectedTrail.getTrail_desc());

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
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
            case R.id.menu:
                navigateToMenuFragment();
                return true;
            case R.id.route:
                navigateToRouteFragment();
                return true;
            default:
                return false;
        }
    }

    private void navigateToHomeFragment() {
        // Navigate to the HomeFragment
        Navigation.findNavController(getView()).navigate(R.id.action_trail_media_to_FirstFragment);
    }

    private void navigateToMenuFragment(){
        Navigation.findNavController(this.getView()).navigate(R.id.action_trail_media_to_MenuFragment);
    }

    private void navigateToRouteFragment(){
        Navigation.findNavController(this.getView()).navigate(R.id.action_trail_media_to_TrailList);
    }

}
