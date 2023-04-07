package com.firstapp.braguia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.braguia.Model.Trail;

import java.util.List;

public class TrailRecyclerViewAdapter extends RecyclerView.Adapter<TrailRecyclerViewAdapter.TrailViewHolder> {

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

    @Override
    public void onBindViewHolder(final TrailViewHolder holder, int position) {
        Trail t = mTrails.get(position);
        holder.mTrailName.setText(t.getTrail_name());
        holder.mTrailDesc.setText(t.getTrail_description());
        //TODO
        //Alterar forma como guardamos a imagem (em vez de link da source, guardar mm a imagem)
        //holder.mTrailImage.findViewById()
    }

    @Override
    public int getItemCount() {
        return mTrails.size();
    }

    public static class TrailViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTrailName;
        private final TextView mTrailDesc;
        private final ImageView mTrailImage;

        private TrailViewHolder(View itemView) {
            super(itemView);
            mTrailName = itemView.findViewById(R.id.TrailName);
            mTrailDesc = itemView.findViewById(R.id.trailDesc);
            mTrailImage = itemView.findViewById(R.id.trailImage);
        }
    }
}

