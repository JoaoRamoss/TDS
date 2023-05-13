package com.firstapp.braguia.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.firstapp.braguia.Model.User;
import com.firstapp.braguia.R;
import com.firstapp.braguia.ViewModel.ViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserDetailsFragment extends Fragment implements BottomNavigationView.OnItemSelectedListener{

    private Button backArrow;
    private TextView userType;
    private TextView username;
    private TextView firstname;
    private TextView lastname;
    private TextView email;
    private TextView createdDate;
    private ViewModel viewmodel;
    private BottomNavigationView bottomNavigationView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_info, container, false);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);

        backArrow = view.findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(view1 -> getActivity().onBackPressed());

        userType = view.findViewById(R.id.userType);
        username = view.findViewById(R.id.username);
        firstname = view.findViewById(R.id.firstname);
        lastname = view.findViewById(R.id.lastname);
        email = view.findViewById(R.id.email);
        createdDate = view.findViewById(R.id.created);

        viewmodel = new ViewModelProvider(this).get(ViewModel.class);
        viewmodel.getLocalUser().observe(getViewLifecycleOwner(), user -> {
            userType.setText(user.getUserType());
            username.setText(user.getUsername());
            firstname.setText(user.getFirstName());
            lastname.setText(user.getLastName());
            email.setText(user.getEmail());

            String inputDate = user.getDateJoined();

            LocalDateTime dateTime = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                dateTime = LocalDateTime.parse(inputDate, DateTimeFormatter.ISO_DATE_TIME);
            }
            DateTimeFormatter dateFormatter = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                String outputDate = dateTime.format(dateFormatter);
                createdDate.setText(outputDate);
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
        Navigation.findNavController(this.getView()).navigate(R.id.action_user_info_to_FirstFragment);
    }

    private void navigateToMenuFragment(){
        Navigation.findNavController(this.getView()).navigate(R.id.action_user_info_to_MenuFragment);
    }

    private void navigateToRouteFragment(){
        Navigation.findNavController(this.getView()).navigate(R.id.action_user_info_to_TrailList);
    }

}
