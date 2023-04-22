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

public class TrailListFragment extends Fragment implements BottomNavigationView.OnItemSelectedListener, TrailRecyclerViewAdapter.ItemClickListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private static List<Trail> trails = new ArrayList<>();
    private static boolean isLoaded = false;

    private TrailRecyclerViewAdapter adapter;

    private BottomNavigationView bottomNavigationView;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    public TrailListFragment(){}


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.trail_list, container, false);
        RecyclerView recvView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recvView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recvView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        adapter = new TrailRecyclerViewAdapter(new ArrayList<>(), this);
        recvView.setAdapter(adapter);

        ViewModel viewmodel = new ViewModelProvider(this).get(ViewModel.class);
        viewmodel.getTrails().observe(getViewLifecycleOwner(), new Observer<List<Trail>>() {
            @Override
            public void onChanged(List<Trail> trails) {
                adapter.setTrails(trails);
                TrailListFragment.trails = trails; // Adicione esta linha para atualizar a variável 'trails'
            }
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
            default:
                return false;
        }
    }

    private void navigateToHomeFragment() {
        // Navigate to the HomeFragment
        Navigation.findNavController(getView()).navigate(R.id.action_TrailList_to_FirstFragment);


    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onItemClick(int position) {
        Trail selectedTrail = trails.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedTrail", (Serializable) selectedTrail);
        Navigation.findNavController(getView()).navigate(R.id.action_trailListFragment_to_trailDetailsFragment, bundle);
    }


}
