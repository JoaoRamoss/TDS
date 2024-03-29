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

    private Button settingsButton;

    private Button contactButton;
    private ViewModel viewmodel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu, container, false);


        logoutButton = view.findViewById(R.id.btn_logout);
        historyButton = view.findViewById(R.id.button_historico);
        profileButton = view.findViewById(R.id.button_perfil);
        contactButton = view.findViewById(R.id.button_contactenos);
        settingsButton = view.findViewById(R.id.button_definicoes);

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);

        // Set the default selected item
        bottomNavigationView.setSelectedItemId(R.id.menu);
        this.viewmodel = new ViewModelProvider(this).get(ViewModel.class);

        viewmodel.getLocalUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                // Un-renders "History" button in case the user isn't a Premium user
                if (!user.getUserType().equals("Premium")) {
                    historyButton.setVisibility(View.GONE);
                }
            }
        });

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
        Navigation.findNavController(this.getView()).navigate(R.id.action_MenuFragment_to_TrailList);
    }

    private void navigatetoHomeFragment() {
        Navigation.findNavController(this.getView()).navigate(R.id.action_MenuFragment_to_FirstFragment);
    }

    private void setButtonListeners() {
        logoutButton.setOnClickListener(view -> {
            viewmodel.logout();
            viewmodel.clearCookies();
            viewmodel.delete();
            startActivity(new Intent(getActivity(), Login.class));
        });

        historyButton.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_MenuFragment_to_HistoryFragment));

        profileButton.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_MenuFragment_to_user_info));

        contactButton.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_MenuFragment_to_contact_page));

        settingsButton.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_MenuFragment_to_settings_page));

    }
}
