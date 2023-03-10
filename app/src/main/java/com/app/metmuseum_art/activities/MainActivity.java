package com.app.metmuseum_art.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.toolbox.JsonObjectRequest;
import com.app.metmuseum_art.utilities.RandomGenerator;
import com.app.metmuseum_art.utilities.VolleySingleton;
import com.app.metmuseum_art.adapters.DepartmentAdapter;
import com.app.metmuseum_art.databinding.ActivityMainBinding;
import com.app.metmuseum_art.models.DepartmentItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DepartmentAdapter.OnDepartmentListener {

    /* 10.03.2023 
    Created by Efe İnanç */
    
    ActivityMainBinding binding;
    SplashScreen splashScreen;

    boolean showContent;

    ArrayList<DepartmentItem> departmentItems = new ArrayList<>();
    DepartmentAdapter adapter;

    String TAG = "MainActivity";

    VolleySingleton volleySingleton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Initialize
        initialize();

        //Get data and wait for loading
        getDepartments();

        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (showContent) {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return false;
            }
        });
    }

    private void initialize() {
        volleySingleton = VolleySingleton.getInstance(this);
        adapter = new DepartmentAdapter(departmentItems, this, this);

        setSupportActionBar(binding.toolbar);
        binding.recylerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.recylerView.setHasFixedSize(true);
        binding.recylerView.setAdapter(adapter);
    }

    private void getDepartments() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://collectionapi.metmuseum.org/public/collection/v1/departments", response -> {
            try {
                JSONArray array = response.getJSONArray("departments");
                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject obj = array.getJSONObject(i);
                        int departmentId = obj.getInt("departmentId");
                        String name = obj.getString("displayName");
                        getDepartmentObject(i, name,departmentId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e(TAG, "onErrorResponse: " + error.getMessage()));

        volleySingleton.addToRequestQueue(jsonObjectRequest);
    }

    private void getDepartmentObject(int index, String departmentName, int departmentId) {
        String query = "https://collectionapi.metmuseum.org/public/collection/v1/search?q=" + departmentName + "&isHighlight=true&hasImages=true";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(query, response -> {
            try {
                int total = response.getInt("total");
                Log.e(TAG, "Total objects for " + departmentName + ": " + total);
                int objId = response.getJSONArray("objectIDs").getInt(RandomGenerator.getRandomNumber(0,total));
                getObjectData(departmentId, objId, departmentName, index);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e(TAG, "onErrorResponse: " + error.getMessage()));

        volleySingleton.addToRequestQueue(jsonObjectRequest);
    }

    private void getObjectData(int depId, int objectId, String departmentName, int index) {
        String query = "https://collectionapi.metmuseum.org/public/collection/v1/objects/" + objectId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(query, response -> {
            try {
                String primaryImage = response.getString("primaryImageSmall");
                if (primaryImage.isEmpty()) {
                    //get new object data
                    getDepartmentObject(index,departmentName,depId);
                    return;
                }
                addArraylist(depId, departmentName, primaryImage, index);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e(TAG, "onErrorResponse: " + error.getMessage()));

        volleySingleton.addToRequestQueue(jsonObjectRequest);
    }

    private void addArraylist(int depId, String name, String imgUrl, int index) {
        departmentItems.add(new DepartmentItem(depId, name, imgUrl));
        if (index >= departmentItems.size() || index < 0) {
            //index does not exists
            adapter.notifyItemInserted(index);
        }
        showContent = true;
        Log.e(TAG, "addArraylist: " + name);
    }


    @Override
    public void onDepartmentClick(int depId, String depName) {
        startActivity(new Intent(MainActivity.this, ObjectsActivity.class)
                .putExtra("departmentId", depId)
                .putExtra("departmentName", depName));
    }
}
