package com.firstapp.braguia.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.firstapp.braguia.R;
import com.firstapp.braguia.ViewModel.ViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Menu extends Fragment implements BottomNavigationView.OnItemSelectedListener{
    private Button logoutButton;
    private Button historyButton;

    private Button profileButton;
    private ViewModel viewmodel;
    private BottomNavigationView bottomNavigationView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu, container, false);
        logoutButton = view.findViewById(R.id.btn_logout);
        historyButton = view.findViewById(R.id.button_historico);
        profileButton = view.findViewById(R.id.button_perfil);

        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);
        // Set the default selected item
        bottomNavigationView.setSelectedItemId(R.id.menu);
        this.viewmodel = new ViewModelProvider(this).get(ViewModel.class);

        setButtonListeners();
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                navigatetoHomeFragment();
                return true;
            case R.id.route:
                // Handle the click on the Home menu item
                navigateToRouteFragment();
                return true;
            case R.id.emergency:
                // Handle the click on the Emergency menu item
                EmergencyCall.makeEmergencyCall(getContext());
                return true;
            default:
                return false;
        }
    }

    private void navigateToRouteFragment() {
        // Navigate to the HomeFragment
        Navigation.findNavController(this.getView()).navigate(R.id.action_MenuFragment_to_TrailList);
    }

    private void navigatetoHomeFragment() {
        Navigation.findNavController(this.getView()).navigate(R.id.action_MenuFragment_to_FirstFragment);
    }

    private void setButtonListeners() {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewmodel.logout();
                viewmodel.clearCookies();
                viewmodel.delete();
                startActivity(new Intent(getActivity(), Login.class));
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_MenuFragment_to_HistoryFragment);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_MenuFragment_to_user_info);
            }
        });

    }
}
