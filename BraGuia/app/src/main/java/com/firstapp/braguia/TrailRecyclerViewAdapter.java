package com.firstapp.braguia;

import static android.content.ContentValues.TAG;
import static android.os.FileUtils.copy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.braguia.Model.ImageLoader;
import com.firstapp.braguia.Model.Trail;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

    @Override
    public void onBindViewHolder(final TrailViewHolder holder, int position) {
        Trail t = mTrails.get(position);
        holder.mTrailName.setText(t.getTrail_name());
        holder.mTrailDesc.setText(t.getTrail_description());

        //Download the image
        Bitmap image = ImageLoader.downloadImage(t.getImage_src());
        holder.mTrailImage.setImageBitmap(image);
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

        private TrailViewHolder(View itemView) {
            super(itemView);
            mTrailName = itemView.findViewById(R.id.TrailName);
            mTrailDesc = itemView.findViewById(R.id.trailDesc);
            mTrailImage = itemView.findViewById(R.id.trailImage);
        }
    }
}

