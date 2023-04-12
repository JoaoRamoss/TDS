package com.firstapp.braguia;

import android.annotation.SuppressLint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.braguia.Utils.ImageLoader;
import com.firstapp.braguia.Model.Trail;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailRecyclerViewAdapter extends RecyclerView.Adapter<TrailRecyclerViewAdapter.TrailViewHolder> implements ImageLoader {

    private final List<Trail> mTrails;

    public TrailRecyclerViewAdapter (List<Trail> items) {
        mTrails = items;
    }

    @Override
    public TrailViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new TrailViewHolder(v);
    }

    @SuppressLint("AssertionSideEffect")
    @Override
    public void onBindViewHolder(final TrailViewHolder holder, int position) {


        Trail t = mTrails.get(position);
        String duration = String.valueOf(t.getDuration()) + 'm';
        holder.mTrailName.setText(t.getTrail_name());
        holder.mTrailDuration.setText(duration);
        holder.mTrailDiff.setText(t.getDifficulty());
        holder.mTrailDesc.setText(t.getTrail_description());
        //Get image
        Picasso.get().load(t.getImage_src()).placeholder(R.drawable.no_image).into(holder.mTrailImage);


    }

    static class TrailDiff extends DiffUtil.ItemCallback<Trail>{
        @Override
        public boolean areItemsTheSame(@NonNull Trail oldItem, @NonNull Trail newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Trail oldItem, @NonNull Trail newItem) {
            return oldItem.equals(newItem);
        }
    }

    @Override
    public int getItemCount() {
        return mTrails.size();
    }

    public static class TrailViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTrailName;
        private final TextView mTrailDesc;
        private final ImageView mTrailImage;
        private final TextView mTrailDiff;
        private final TextView mTrailDuration;


        private TrailViewHolder(View itemView) {
            super(itemView);
            mTrailName = itemView.findViewById(R.id.TrailName);
            mTrailDesc = itemView.findViewById(R.id.trailDesc);
            mTrailDiff = itemView.findViewById(R.id.trailDiff);
            mTrailDuration = itemView.findViewById(R.id.trailDuration);
            mTrailImage = itemView.findViewById(R.id.trailImage);
        }
    }
}

