package com.meghaagarwal.foodorderclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText mETEmail, mETPass;
    private Button mBSignUp, mBSignIn;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mETEmail = (EditText) findViewById(R.id.etEmail);
        mETPass = (EditText) findViewById(R.id.etPass);

        mBSignIn = (Button) findViewById(R.id.bSignIn);
        mBSignUp = (Button) findViewById(R.id.bSignUp);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("users");

        mBSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        mBSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String email_text = mETEmail.getText().toString().trim();
                String pass_text = mETPass.getText().toString().trim();

                if(!TextUtils.isEmpty(email_text) && !TextUtils.isEmpty(pass_text))
                {
                    mFirebaseAuth.createUserWithEmailAndPassword(email_text,pass_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(MainActivity.this, "User Added", Toast.LENGTH_LONG).show();
                            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            String user_id = mFirebaseAuth.getCurrentUser().getUid();
                            DatabaseReference current_user = mDatabaseReference.child(user_id);
                            current_user.child("name").setValue(email_text);

                            Intent login = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(login);
                        }
                    });
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Enter valid input", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}