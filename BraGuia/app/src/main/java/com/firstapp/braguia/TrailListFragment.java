package com.firstapp.braguia;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TrailListFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private static Map<Integer, Trail> trails = new HashMap<>();
    private static boolean isLoaded = false;

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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
