package com.firstapp.braguia.UI;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.braguia.Model.Trail;
import com.firstapp.braguia.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TrailRecyclerViewAdapter extends RecyclerView.Adapter<TrailRecyclerViewAdapter.TrailViewHolder> {

    private List<Trail> mTrails = new ArrayList<>();

    private ItemClickListener itemClickListener;

    public TrailRecyclerViewAdapter (List<Trail> items, ItemClickListener ic) {
        mTrails = items;
        itemClickListener = ic;
    }

    public List<Trail> getmTrails() {
        return mTrails;
    }

    @Override
    public TrailViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new TrailViewHolder(v);
    }

    @SuppressLint("AssertionSideEffect")
    @Override
    public void onBindViewHolder(final TrailViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Trail t = mTrails.get(position);
        String duration = String.valueOf(t.getTrail_duration()) + 'm';
        holder.mTrailName.setText(t.getTrail_name());
        holder.mTrailDuration.setText(duration);
        holder.mTrailDiff.setText(t.getTrail_difficulty());
        holder.mTrailDesc.setText(t.getTrail_desc());
        // Get image
        Picasso.get().load(t.getTrail_img())
                .placeholder(R.drawable.no_image)
                .into(holder.mTrailImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(position);
                }
            }
        });

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

    public void setTrails(List<Trail> trails) {
        this.mTrails = trails;
        notifyDataSetChanged();
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


    public Trail getTrail(int position) {
        return mTrails.get(position);
    }



    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
