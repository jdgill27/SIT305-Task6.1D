package com.example.food_rescue_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {

    RecyclerView rv;
    DatabaseReference database;
    Adapter adapter;
    List<Food_Item> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rv = findViewById(R.id.rv);
        rv.clearOnScrollListeners();
        database = FirebaseDatabase.getInstance().getReference().child("Food_Item").child("Food_Item");
        rv.setLayoutManager(new LinearLayoutManager(home.this));

        list.clear();

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Food_Item food_item = dataSnapshot.getValue(Food_Item.class);
                    list.add(food_item);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new Adapter(home.this, list);
        rv.setAdapter(adapter);
    }

    public void Add_Food_Item(View view) {
        Intent intent = new Intent(this, add_food_item.class);
        startActivity(intent);
    }
}