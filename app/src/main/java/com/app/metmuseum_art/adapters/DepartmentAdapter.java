package com.app.metmuseum_art.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metmuseum_art.R;
import com.app.metmuseum_art.databinding.RowDepartmentItemBinding;
import com.app.metmuseum_art.models.DepartmentItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.ViewHolder> {

    RequestManager glideManager;
    ArrayList<DepartmentItem> arrayList;
    Context context;

    OnDepartmentListener onDepartmentListener;

    public DepartmentAdapter(ArrayList<DepartmentItem> arrayList, Context context, OnDepartmentListener onDepartmentListener) {
        this.arrayList = arrayList;
        this.context = context;
        glideManager = Glide.with(context);
        this.onDepartmentListener = onDepartmentListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowDepartmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DepartmentItem model = arrayList.get(position);
        holder.binding.depNameTw.setText(model.getName());

        if (model.getImgUrl() != null) {
            glideManager
                    .asBitmap()
                    .load(model.getImgUrl())
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            setBackgroundColor(resource, holder.binding.depNameTw);
                            return false;
                        }
                    })
                    //.transition(DrawableTransitionOptions.withCrossFade())
                    //Here a fading animation
                    //.placeholder(R.drawable.ic_placeholder)
                    .fitCenter()
                    .error(R.drawable.logo)
                    .into(holder.binding.depImg);

        }

        holder.binding.getRoot().setOnClickListener(view -> {
            onDepartmentListener.onDepartmentClick(model.getId(),model.getName());
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RowDepartmentItemBinding binding;

        public ViewHolder(RowDepartmentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }


    public void setBackgroundColor(Bitmap bitmap, TextView textView) {
        // Generate the palette and get the vibrant swatch
        // See the createPaletteSync() method
        // from the code snippet above
        Palette p = createPaletteSync(bitmap);

        // Load default colors
        int backgroundColor = ContextCompat.getColor(context,
                R.color.black);
        int textColor = ContextCompat.getColor(context,
                R.color.white);

        backgroundColor = p.getDarkMutedColor(backgroundColor);
        textColor = p.getLightMutedColor(textColor);

        // Set the textview background and text color
        textView.setBackgroundColor(backgroundColor);
        textView.setTextColor(textColor);
    }

    // Generate palette synchronously and return it
    public Palette createPaletteSync(Bitmap bitmap) {
        return Palette.from(bitmap).generate();
    }

    public interface OnDepartmentListener {
        void onDepartmentClick(int depId, String depName);
    }

}
