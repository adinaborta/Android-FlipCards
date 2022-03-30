package com.example.flipcards;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DAOCard {
    private DatabaseReference databaseReference;

    public DAOCard(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference();
    }

    public Task<DataSnapshot> get(){
        return databaseReference.get();
    }

}
