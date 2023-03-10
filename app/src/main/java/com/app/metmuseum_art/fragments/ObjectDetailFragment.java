package com.app.metmuseum_art.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.metmuseum_art.R;
import com.app.metmuseum_art.databinding.FragmentObjectDetailBinding;
import com.app.metmuseum_art.models.ObjectItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


public class ObjectDetailFragment extends Fragment {


    FragmentObjectDetailBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentObjectDetailBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @NonNull
    public static ObjectDetailFragment newInstance(ObjectItem objectItem, String objectItemTransitionName) {
        ObjectDetailFragment myFragment = new ObjectDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("objectItem", objectItem);
        args.putString("objectItemTransitionName", objectItemTransitionName);
        myFragment.setArguments(args);
        return myFragment;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() == null) {
            Toast.makeText(getContext(), "arguments null", Toast.LENGTH_SHORT).show();
            return;
        }
        ObjectItem objectItem = getArguments().getParcelable("objectItem");

        String imageUrl = objectItem.getPrimaryImage();
        //get transition name and set again
        binding.photoView.setTransitionName(objectItem.getTitle());
        Toast.makeText(getContext(), objectItem.getTitle(), Toast.LENGTH_SHORT).show();

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
        }).into(binding.photoView);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}