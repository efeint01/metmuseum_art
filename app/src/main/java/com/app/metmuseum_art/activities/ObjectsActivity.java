package com.app.metmuseum_art.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.toolbox.JsonObjectRequest;
import com.app.metmuseum_art.R;
import com.app.metmuseum_art.fragments.ObjectDetailFragment;
import com.app.metmuseum_art.utilities.EndlessRecyclerViewScrollListener;
import com.app.metmuseum_art.utilities.VolleySingleton;
import com.app.metmuseum_art.adapters.ObjectAdapter;
import com.app.metmuseum_art.databinding.ActivityObjectsBinding;
import com.app.metmuseum_art.models.ObjectItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class ObjectsActivity extends AppCompatActivity implements ObjectAdapter.OnObjectListener {

    ActivityObjectsBinding binding;

    ArrayList<ObjectItem> objectItems = new ArrayList<>();
    ObjectAdapter adapter;

    VolleySingleton volleySingleton;
    String TAG = "ObjectsActivity";

    String departmentName;
    int departmentId;
    int itemsCount;
    int fetchDataCount = 20; //We will fetch 20 items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityObjectsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initialize();

        Intent intent = getIntent();
        departmentId = intent.getIntExtra("departmentId", 0);
        departmentName = intent.getStringExtra("departmentName");

        binding.toolbar.setTitle(departmentName);

        getObjects(departmentId, departmentName);

    }

    private void initialize() {
        volleySingleton = VolleySingleton.getInstance(this);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        adapter = new ObjectAdapter(objectItems, this, this);


        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        binding.recylerView.setLayoutManager(layoutManager);
        binding.recylerView.setHasFixedSize(true);
        binding.recylerView.setAdapter(adapter);

        binding.recylerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                itemsCount += 20;
                getObjects(departmentId, departmentName);
            }
        });

    }

    private void getObjects(int departmentId, String depName) {
        //Get object from department
        String query = "https://collectionapi.metmuseum.org/public/collection/v1/objects?departmentIds= " + departmentId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(query, response -> {
            try {
                int total = response.getInt("total"); //total found objects
                Log.e(TAG, "Total founded objects: " + total);
                JSONArray objectIDs = response.getJSONArray("objectIDs"); //object ids in department
                //Get first ($fetchDataCount) object ids
                for (int i = itemsCount; i < itemsCount+fetchDataCount; i++) {
                    int objId = objectIDs.getInt(i);
                    getObjectInfo(objId, i);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e(TAG, "onErrorResponse: " + error.getMessage()));

        volleySingleton.addToRequestQueue(jsonObjectRequest);
    }

    private void getObjectInfo(int objId, int index) {
        //Get object info from obj id
        String query = "https://collectionapi.metmuseum.org/public/collection/v1/objects/" + objId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(query, response -> {
            //get object info's
            try {
                String imgUrl = response.getString("primaryImageSmall");
                if (!imgUrl.isEmpty()) {
                    String title = response.getString("title");
                    String artistDisplayName = response.getString("artistDisplayName");
                    //add item to arraylist
                    addArraylist(objId, title, imgUrl, artistDisplayName, index);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Log.e(TAG, "onErrorResponse: " + error.getMessage()));

        volleySingleton.addToRequestQueue(jsonObjectRequest);
    }

    private void addArraylist(int objId, String title, String imgUrl, String desc, int index) {

        objectItems.add(new ObjectItem(objId, title, imgUrl, desc));
        if (index >= objectItems.size() || index < 0) {
            //index does not exists
            adapter.notifyItemInserted(index);
        }

    }

    @Override
    public void onClickListener(ObjectItem item, ImageView sharedImg) {
//        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Artist Name: " + item.getArtistName());
        Log.e(TAG, "Title: " + item.getTitle());

        /* for activity transition
        Intent intent = new Intent(this, ObjectDetailActivity.class);
        intent.putExtra("objectItem", item);
        intent.putExtra("objectItemTransitionName", ViewCompat.getTransitionName(sharedImg));

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                sharedImg,
                Objects.requireNonNull(ViewCompat.getTransitionName(sharedImg)));

        startActivity(intent, options.toBundle());
         */

        //fragment transition
        Fragment fragment = ObjectDetailFragment.newInstance(item, ViewCompat.getTransitionName(sharedImg));
        getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(sharedImg, Objects.requireNonNull(ViewCompat.getTransitionName(sharedImg)))
                .addToBackStack(TAG)
                .replace(R.id.content, fragment)
                .commit();

    }

    @Override
    public void onLongClickListener(@NonNull ObjectItem item) {
        Log.e(TAG, "Object ID: " + item.getObjectID());
    }
}