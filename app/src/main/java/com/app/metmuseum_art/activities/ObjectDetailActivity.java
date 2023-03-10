package com.app.metmuseum_art.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.app.metmuseum_art.databinding.ActivityObjectDetailBinding;
import com.app.metmuseum_art.models.ObjectItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class ObjectDetailActivity extends AppCompatActivity {

    ActivityObjectDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityObjectDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        Bundle extras = getIntent().getExtras();
        ObjectItem objectItem = extras.getParcelable("objectItem");

        String imageUrl = objectItem.getPrimaryImage();
        //get transition name and set again
        binding.imageView.setTransitionName(objectItem.getTitle());

        Glide.with(this).load(imageUrl).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                startPostponedEnterTransition();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                startPostponedEnterTransition();
                return false;
            }
        }).into(binding.imageView);


    }

}