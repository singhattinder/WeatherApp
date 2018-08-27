package com.attinder.weatherdemoapp.activities;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.attinder.weatherdemoapp.R;
import com.attinder.weatherdemoapp.adapters.ListAdapter;

import com.attinder.weatherdemoapp.listeners.RecyclerItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Set;


public class FavourtieLocationsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private HashSet<String> cities = new HashSet<>();
    private String[] array;
    private LinearLayout backGroundFrameFavourtie;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourtie_locations);




        mRecyclerView =  findViewById(R.id.my_recycler_view);
        backGroundFrameFavourtie = findViewById(R.id.back_ground_favourite);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);



        Toolbar myChildToolbar = findViewById(R.id.favourite_toolbar);
        setSupportActionBar(myChildToolbar);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        mRecyclerView.setItemAnimator(itemAnimator);



        Glide.with(FavourtieLocationsActivity.this).load(R.drawable.favourite).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                backGroundFrameFavourtie.setBackground(resource);
            }


        });




        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference favouriteCities = null;

        DatabaseReference myRef = database.getReference();

        try {
            favouriteCities = myRef.child("users").child(currentFirebaseUser.getUid());
            favouriteCities.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String c = ds.getValue(String.class);
                        cities.add(c);
                    }

                    array = cities.toArray(new String[cities.size()]);
                    mAdapter = new ListAdapter(array);
                    mRecyclerView.setAdapter(mAdapter);

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.d("google", "Failed to read value.", error.toException());
                }
            });
        } catch (NullPointerException ex) {
            Log.d("google", ex.toString());
        }

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(FavourtieLocationsActivity.this, mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent =  new Intent(FavourtieLocationsActivity.this, MainActivity.class);
                        intent.putExtra("search", array[position]);
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );




        }




    }



