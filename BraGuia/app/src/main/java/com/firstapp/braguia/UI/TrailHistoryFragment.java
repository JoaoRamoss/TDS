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
import androidx.appcompat.widget.SearchView;
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

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrailHistoryFragment extends Fragment implements BottomNavigationView.OnItemSelectedListener {


    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private static List<Trail> trails = new ArrayList<>();
    private static boolean isLoaded = false;

    private SearchView searchbar;
    private TextView emptyMessage;
    private ViewModel viewmodel;

    private Button clearHistory;

    private TrailHistoryAdapter adapter;

    private BottomNavigationView bottomNavigationView;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    public TrailHistoryFragment(){}


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.history_list, container, false);
        RecyclerView recvView = (RecyclerView) view.findViewById(R.id.recyclerview_history);
        clearHistory = view.findViewById(R.id.clearHistory);
        emptyMessage = view.findViewById(R.id.emptyMessage);
        recvView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recvView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        viewmodel = new ViewModelProvider(this).get(ViewModel.class);
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

        adapter = new TrailHistoryAdapter(new ArrayList<>());
        recvView.setAdapter(adapter);

        ViewModel viewmodel = new ViewModelProvider(this).get(ViewModel.class);
        viewmodel.getmAllTrails().observe(getViewLifecycleOwner(), new Observer<List<Trail>>() {
            @Override
            public void onChanged(List<Trail> trails) {
                adapter.setTrails(trails);
                TrailHistoryFragment.trails = trails;
                if (trails.isEmpty()){
                    clearHistory.setVisibility(View.GONE);
                    emptyMessage.setVisibility(View.VISIBLE);
                    searchbar.setVisibility(View.GONE);
                }
                else {
                    clearHistory.setVisibility(View.VISIBLE);
                    emptyMessage.setVisibility(View.GONE);
                    searchbar.setVisibility(View.VISIBLE);
                }
            }
        });


        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);


        setButtonListeners();
        // Set the default selected item
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
            case R.id.route:
                navigateToRouteFragment();
                return true;
            default:
                return false;
        }
    }

    private void navigateToHomeFragment() {
        // Navigate to the HomeFragment
        Navigation.findNavController(getView()).navigate(R.id.action_HistoryFragment_to_FirstFragment);
    }

    private void navigateToMenuFragment(){
        Navigation.findNavController(this.getView()).navigate(R.id.action_HistoryFragment_to_MenuFragment);
    }

    private void navigateToRouteFragment(){
        Navigation.findNavController(this.getView()).navigate(R.id.action_HistoryFragment_to_TrailList);
    }


    @Override
    public void onResume() {
        super.onResume();
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

    private void setButtonListeners() {
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewmodel.clearHistory();
            }
        });
    }
}
