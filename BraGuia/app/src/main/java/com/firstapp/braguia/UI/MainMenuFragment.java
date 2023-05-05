package com.firstapp.braguia.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.firstapp.braguia.Model.User;
import com.firstapp.braguia.R;
import com.firstapp.braguia.ViewModel.ViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


public class MainMenuFragment extends Fragment implements BottomNavigationView.OnItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment, container, false);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);
        ViewModel viewModel = new ViewModelProvider(this).get(ViewModel.class);

        // Set the default selected item
        bottomNavigationView.setSelectedItemId(R.id.home);


        /*
        viewModel.getUser().observe(getViewLifecycleOwner() , new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if(users.size()>1){
                    viewModel.delete();
                }
                else{
                    viewModel.insert(users.get(0));
                }
            }
        });
        */
        return view;
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.route:
                // Handle the click on the Home menu item
                navigateToRouteFragment();
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


    private void navigateToRouteFragment() {
        // Navigate to the HomeFragment
        Navigation.findNavController(this.getView()).navigate(R.id.action_FirstFragment_to_TrailList);
    }

    private void navigateToMenuFragment(){
        Navigation.findNavController(this.getView()).navigate(R.id.action_FirstFragment_to_MenuFragment);
    }







}
