package com.firstapp.braguia.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.R;
import com.firstapp.braguia.ViewModel.ViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrailListFragment extends Fragment implements BottomNavigationView.OnItemSelectedListener, TrailRecyclerViewAdapter.ItemClickListener {

    private static List<Trail> trails = new ArrayList<>();

    private SearchView searchbar;

    private TrailRecyclerViewAdapter adapter;

    private BottomNavigationView bottomNavigationView;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public TrailListFragment(){}


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.trail_list, container, false);
        RecyclerView recvView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recvView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recvView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        searchbar = view.findViewById(R.id.search);
        searchbar.clearFocus();
        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        adapter = new TrailRecyclerViewAdapter(new ArrayList<>(), this);
        recvView.setAdapter(adapter);

        ViewModel viewmodel = new ViewModelProvider(this).get(ViewModel.class);
        viewmodel.getTrails().observe(getViewLifecycleOwner(), trails -> {
            adapter.setTrails(trails);
            TrailListFragment.trails = trails;
        });


        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);

        // Set the default selected item
        bottomNavigationView.setSelectedItemId(R.id.route);
        return view;
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
            default:
                return false;
        }
    }

    private void navigateToHomeFragment() {
        // Navigate to the HomeFragment
        Navigation.findNavController(getView()).navigate(R.id.action_TrailList_to_FirstFragment);
    }

    private void navigateToMenuFragment(){
        Navigation.findNavController(this.getView()).navigate(R.id.action_TrailList_to_MenuFragment);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onItemClick(int position) {
        Trail selectedTrail = adapter.getmTrails().get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedTrail", (Serializable) selectedTrail);
        Navigation.findNavController(getView()).navigate(R.id.action_trailListFragment_to_trailDetailsFragment, bundle);
    }


    private void filterList(String newText) {
        String query = Pattern.quote(newText);
        String regex = "\\b" + query + "\\w*";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        List<Trail> filteredList = new ArrayList<>();
        for (Trail t : this.trails){
            Matcher matcher = pattern.matcher(t.getTrail_name());
            Matcher matcher2 = pattern.matcher(t.getTrail_desc());
            if (matcher.find() || matcher2.find()){
                filteredList.add(t);
            }
        }

        adapter.setTrails(filteredList);
    }
}
