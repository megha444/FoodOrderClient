package com.meghaagarwal.foodorderclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.core.Context;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView mRVFoodList;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mRVFoodList = (RecyclerView) findViewById(R.id.rvFoodList);
        mRVFoodList.setHasFixedSize(true);
        mRVFoodList.setLayoutManager(new LinearLayoutManager(this));
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Item");

        mFirebaseAuth = FirebaseAuth.getInstance();
        fetch();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mFirebaseAuth.getCurrentUser() == null)
                {
                    Intent loginIntent = new Intent(MenuActivity.this, MainActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
                else
                { //fetch();
                }
            }
        };
    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance().getReference().child("Item");

        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(query, new SnapshotParser<Food>() {
            @NonNull
            @Override
            public Food parseSnapshot(@NonNull DataSnapshot snapshot) {
                return new Food(snapshot.child("name").getValue().toString(),
                        snapshot.child("desc").getValue().toString(),
                        snapshot.child("price").getValue().toString(),
                        snapshot.child("image").getValue().toString());
            }
        }).build();

        mAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i, @NonNull Food food) {
                foodViewHolder.setName(food.getName());
                foodViewHolder.setDesc(food.getDesc());
                foodViewHolder.setPrice(food.getPrice());
                foodViewHolder.setImage(getApplicationContext(), food.getImage());

                final String food_key = getRef(i).getKey().toString();
                foodViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent singleFoodActivity = new Intent(MenuActivity.this, SingleFoodActivity.class);
                        singleFoodActivity.putExtra("FoodID", food_key);
                        startActivity(singleFoodActivity);
                    }
                });
            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlemenuitem, parent, false);
                return new FoodViewHolder(view);
            }
        };
mRVFoodList.setAdapter(mAdapter);
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            mView= itemView;
        }

        public void setName(String name)
        {
            TextView foodName = (TextView) mView.findViewById(R.id.tvFoodName);
            foodName.setText(name);
        }

        public void setDesc (String desc)
        {
            TextView foodDesc = (TextView) mView.findViewById(R.id.tvFoodDesc);
            foodDesc.setText(desc);
        }

        public void setPrice(String price)
        {
            TextView foodPrice = (TextView) mView.findViewById(R.id.tvFoodPrice);
            foodPrice.setText(price+"/-");
        }

        public void setImage(Context ctx, String image)
        {
            ImageView foodImage = (ImageView) mView.findViewById(R.id.ivFoodImage);
            Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/foodorder-ba83e.appspot.com/o/primary%3ADownload%2F440478889.jpg?alt=media&token=4c5a954b-46e4-4097-a3e7-1eb9275fb5d8");
            Picasso.with(ctx).load(uri).into(foodImage);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("FOODCLIENT", "onStart callback");
        mRVFoodList.setAdapter(mAdapter);
        mAdapter.startListening();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

}