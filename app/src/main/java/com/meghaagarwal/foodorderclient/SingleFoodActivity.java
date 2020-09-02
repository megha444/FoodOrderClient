package com.meghaagarwal.foodorderclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class SingleFoodActivity extends AppCompatActivity {

    private TextView mTitle, mDesc, mPrice;
    private String food_key = null;
    private ImageView mImage;
    private DatabaseReference mRef, user_data, orderRef;
    private FirebaseUser current_user;
    private FirebaseAuth mAuth;
    private Button mOrderButton;
    String title, desc, price, image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_food);

        food_key= getIntent().getExtras().getString("FoodId");

        mRef= FirebaseDatabase.getInstance().getReference().child("Item");
        mAuth= FirebaseAuth.getInstance();

        mTitle = (TextView) findViewById(R.id.singleTitle);
        mDesc = (TextView) findViewById(R.id.singleDesc);
        mPrice = (TextView) findViewById(R.id.singlePrice);
        mImage = (ImageView) findViewById(R.id.singleImage);
        mOrderButton= (Button) findViewById(R.id.orderButton);

        current_user= mAuth.getCurrentUser();
        user_data= FirebaseDatabase.getInstance().getReference().child("users").child(current_user.getUid());
        orderRef= FirebaseDatabase.getInstance().getReference().child("orders");

        Log.d("FOODCLIENT", user_data.toString());

        mRef.child(food_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 title = snapshot.child("name").getValue().toString();
                 desc = snapshot.child("desc").getValue().toString();
                 price = snapshot.child("price").getValue().toString();
                 image = snapshot.child("image").getValue().toString();

                mTitle.setText(title);
                mDesc.setText(desc);
                mPrice.setText(price);
                Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/foodorder-ba83e.appspot.com/o/primary%3ADownload%2F440478889.jpg?alt=media&token=4c5a954b-46e4-4097-a3e7-1eb9275fb5d8");
                Picasso.with(getApplicationContext()).load(uri).into(mImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference newOrder = orderRef.push();
                user_data.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        newOrder.child("itemname").setValue(title);
                        newOrder.child("username").setValue(snapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(SingleFoodActivity.this, MenuActivity.class));
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}