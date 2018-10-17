package services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Set;

public class FireBaseDatabaseService extends IntentService {

    public FireBaseDatabaseService() {
        super("FireBaseDatabaseService");
    }
    private final HashSet<String> cities = new HashSet<>();


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference favouriteCities = null;

        DatabaseReference myRef = database.getReference();

        try {
            String city = intent.getStringExtra("cityname");
            favouriteCities = myRef.child("users").child(currentFirebaseUser.getUid());
            favouriteCities.push().setValue(city);
            favouriteCities.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        String c = ds.getValue(String.class);
                        cities.add(c);
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.d("google", "Failed to read value.", error.toException());
                }
            });
        }
        catch (NullPointerException ex) {
            Log.d("google", ex.toString());
        }

    }


}
