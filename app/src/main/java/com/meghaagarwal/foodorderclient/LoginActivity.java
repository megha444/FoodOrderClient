package com.meghaagarwal.foodorderclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.*;

public class LoginActivity extends AppCompatActivity {

    private EditText mETEmail, mETPass;
    private Button mBLogin;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mETEmail = (EditText) findViewById(R.id.etEmailLogin);
        mETPass = (EditText) findViewById(R.id.etPassLogin);
        mBLogin = (Button) findViewById(R.id.bLogIn);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        mBLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = mETEmail.getText().toString().trim();
                String pass = mETPass.getText().toString().trim();

                if(!TextUtils.isEmpty(user) && !TextUtils.isEmpty(pass))
                {
                    mFirebaseAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                checkUserExists();
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Incorrect Email or Password", LENGTH_LONG).show();
                            }
                        }
                    });
                }

                else
                {
                    Toast.makeText(LoginActivity.this, "Enter valid input", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void checkUserExists()
    {
        final String user_id = mFirebaseAuth.getCurrentUser().getUid();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(user_id))
                {
                    Intent intent = new Intent (LoginActivity.this, MenuActivity.class);
                    Log.d("FOODCLIENT", "moving towards menu");
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}