package com.firstapp.braguia;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.Utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TrailListFragment extends Fragment implements BottomNavigationView.OnItemSelectedListener {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private static Map<Integer, Trail> trails = new HashMap<>();
    private static boolean isLoaded = false;

    private BottomNavigationView bottomNavigationView;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        initTrails(this.getContext());
    }

    public TrailListFragment(){}

    private void initTrails (Context ctx){
        if (isLoaded){
            return;
        }
        JSONArray jsonArray= Utils.loadSONFile(ctx);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jo = null;
            try {
                jo = jsonArray.getJSONObject(i);
                Trail t = new Trail (jo);
                trails.put(t.getId(), t);
            } catch (JSONException | NoSuchFieldException | IllegalAccessException e){
                e.printStackTrace();
            }
        }
        isLoaded = true;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trail_list, container, false);
        RecyclerView recvView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recvView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recvView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recvView.setAdapter(new TrailRecyclerViewAdapter(new ArrayList<>(trails.values())));

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
}
