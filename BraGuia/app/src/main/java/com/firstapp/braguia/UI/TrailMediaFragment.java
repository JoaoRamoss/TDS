package com.firstapp.braguia.UI;

import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.firstapp.braguia.Model.Edge;
import com.firstapp.braguia.Model.Media;
import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.R;
import com.firstapp.braguia.ViewModel.ViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TrailMediaFragment extends Fragment implements BottomNavigationView.OnItemSelectedListener {

    private Button backArrow;


    private List<Media> media;
    private Trail selectedTrail;
    private TextView description;

    private TextView title;
    private BottomNavigationView bottomNavigationView;

    private HorizontalScrollView scrollView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trail_media, container, false);

        backArrow = view.findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(view1 -> getActivity().onBackPressed());
        description = view.findViewById(R.id.description);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);
        scrollView = view.findViewById(R.id.image_scroll);
        title = view.findViewById(R.id.title);

        this.selectedTrail = (Trail) requireArguments().getSerializable("selectedTrail");

        this.title.setText(selectedTrail.getTrail_name());
        this.media = getMediaFromTrail();

        this.description.setMovementMethod(new ScrollingMovementMethod());
        this.description.setText(selectedTrail.getTrail_desc());

        ViewModel viewmodel = new ViewModelProvider(this).get(ViewModel.class);
        viewmodel.getLocalUser().observe(getViewLifecycleOwner(), user -> {
            if (user.getUserType().equals("Premium")) {
                addMediaToScrollView();
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
        Navigation.findNavController(getView()).navigate(R.id.action_trail_media_to_FirstFragment);
    }

    private void navigateToMenuFragment(){
        Navigation.findNavController(this.getView()).navigate(R.id.action_trail_media_to_MenuFragment);
    }

    private void navigateToRouteFragment(){
        Navigation.findNavController(this.getView()).navigate(R.id.action_trail_media_to_TrailList);
    }

    private List<Media> getMediaFromTrail() {
        HashSet<String> files = new HashSet<>();
        List<Media> res = new ArrayList<>();
        for (Edge e : this.selectedTrail.getEdges()){
            for (Media m : e.getEdge_start().getMedia()){
                if (!files.contains(m.getMedia_file())){
                    files.add(m.getMedia_file());
                    res.add(m);
                }
            }
            for (Media m : e.getEdge_end().getMedia()){
                if (!files.contains(m.getMedia_file())){
                    files.add(m.getMedia_file());
                    res.add(m);
                }
            }
        }
        return res;
    }


    private void addMediaToScrollView() {

        LinearLayout containerLayout = new LinearLayout(getContext());
        containerLayout.setOrientation(LinearLayout.HORIZONTAL);

        for (Media mediaItem : this.media) {
            if (mediaItem.getMedia_type().equals("I")) {
                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(new LinearLayout.LayoutParams(600, 600));
                Picasso.get().load(mediaItem.getMedia_file()).into(imageView);
                containerLayout.addView(imageView);
            } else if (mediaItem.getMedia_type().equals("V")) {
                FrameLayout frameLayout = new FrameLayout(getContext());
                frameLayout.setLayoutParams(new LinearLayout.LayoutParams(800, 600, 1));

                VideoView videoView = new VideoView(getContext());
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

                MediaController mediaController = new MediaController(getContext());
                mediaController.setAnchorView(videoView);
                frameLayout.addView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(Uri.parse(mediaItem.getMedia_file()));

                videoView.setLayoutParams(layoutParams);
                videoView.start();

                containerLayout.addView(frameLayout);
            }
        }

        scrollView.addView(containerLayout);
    }
}
