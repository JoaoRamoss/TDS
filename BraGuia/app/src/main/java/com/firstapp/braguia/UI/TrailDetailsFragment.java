package com.firstapp.braguia.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.R;
import com.google.android.gms.maps.MapView;

public class TrailDetailsFragment extends Fragment {

    private TextView trailTitle;
    private MapView mapView;
    private Button startTrailButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_trail, container, false);


        trailTitle = view.findViewById(R.id.route_title);
        //mapView = view.findViewById(R.id.map_view);
        startTrailButton = view.findViewById(R.id.start_route_button);

        Trail trail = (Trail) getArguments().getSerializable("selectedTrail");
        trailTitle.setText(trail.getTrail_name());

        // inicializar o mapview e tratar do botão de iniciar a rota


        return view;    }

}
