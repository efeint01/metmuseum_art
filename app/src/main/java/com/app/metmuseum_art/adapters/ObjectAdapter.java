package com.app.metmuseum_art.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metmuseum_art.databinding.RowObjectItemBinding;
import com.app.metmuseum_art.models.ObjectItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;

public class ObjectAdapter extends RecyclerView.Adapter<ObjectAdapter.ViewHolder> {

    RequestManager glideManager;
    ArrayList<ObjectItem> arrayList;
    Context context;


    OnObjectListener objectListener;

    public ObjectAdapter(ArrayList<ObjectItem> arrayList, Context context, OnObjectListener objectListener) {
        this.arrayList = arrayList;
        this.context = context;
        glideManager = Glide.with(context);
        this.objectListener = objectListener;
    }

    @NonNull
    @Override
    public ObjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ObjectAdapter.ViewHolder(RowObjectItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ObjectItem model = arrayList.get(position);

        holder.binding.objectTitleTw.setText(model.getTitle());
        holder.binding.objectDescTw.setText(model.getArtistName());

        glideManager.load(model.getPrimaryImage())
                .transition(DrawableTransitionOptions.withCrossFade())
                .fitCenter().into(holder.binding.objectImg);

        //set transition name
        ViewCompat.setTransitionName(holder.binding.objectImg, model.getTitle());

        holder.binding.getRoot().setOnClickListener(view -> objectListener.onClickListener(model, holder.binding.objectImg));
        holder.binding.getRoot().setOnLongClickListener(view -> {
            objectListener.onLongClickListener(model);
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RowObjectItemBinding binding;

        public ViewHolder(RowObjectItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnObjectListener {
        void onClickListener(ObjectItem item, ImageView sharedImg);

        void onLongClickListener(ObjectItem item);
    }

}
