package com.firstapp.braguia.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.firstapp.braguia.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class ContactFragment extends Fragment implements BottomNavigationView.OnItemSelectedListener{

    private Button backArrow;
    private BottomNavigationView bottomNavigationView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact, container, false);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);

        backArrow = view.findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


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
        Navigation.findNavController(this.getView()).navigate(R.id.action_contact_page_to_FirstFragment);
    }

    private void navigateToMenuFragment(){
        Navigation.findNavController(this.getView()).navigate(R.id.action_contact_page_to_MenuFragment);
    }

    private void navigateToRouteFragment(){
        Navigation.findNavController(this.getView()).navigate(R.id.action_contact_page_to_TrailList);
    }

}
