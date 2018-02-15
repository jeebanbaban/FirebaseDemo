package com.example.ingreens.firebasedemo;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    EditText etName,etEmail,etPassword;
    Button signin,signup;
    TextView tvoutput;
    UserModel userModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        etName=findViewById(R.id.etName);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        signin=findViewById(R.id.btnSignin);
        signup=findViewById(R.id.btnSignup);
        tvoutput=findViewById(R.id.tvoutput);
        signin.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignup:{
                String name=etName.getText().toString();
                String email=etEmail.getText().toString();
                String password=etPassword.getText().toString();
                signUp(name,email,password);

            }break;
            case R.id.btnSignin:{
                signIn(etEmail.getText().toString(),etPassword.getText().toString());

            }break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signUp(final String name, final String email, final String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                           userModel=new UserModel(name,email,password);
                            firebaseDatabase.getReference(mAuth.getCurrentUser().getUid()).child("user").setValue(userModel);
                            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                            System.out.println("model er name========"+userModel.getName());
                            System.out.println("model er email========"+userModel.getEmail());
                            System.out.println("model er password========"+userModel.getPassword());
                            System.out.println("email======"+user.getEmail());
                            System.out.println("uid======"+user.getUid());
                            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                            Toast.makeText(MainActivity.this, "registration successfull.", Toast.LENGTH_SHORT).show();
                            etName.getText().clear();
                            etEmail.getText().clear();
                            etPassword.getText().clear();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void signIn(final String email, final String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userModel=new UserModel(email,password);
                            System.out.println("###################################");
                            System.out.println("task.isComplete()"+task.isComplete());
                            System.out.println("task.getResult()"+task.getResult());
                            System.out.println("task.isSuccessful()"+task.isSuccessful());
                            System.out.println("task.toString()"+task.toString());
                            System.out.println("user.getEmail()"+user.getEmail());
                            System.out.println("###################################");
                            etName.getText().clear();
                            etEmail.getText().clear();
                            etPassword.getText().clear();
                            Toast.makeText(MainActivity.this, "login successfull with=="+user.getEmail(), Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private void getCurrentUser(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            //Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }

    }
    private void updateUI(FirebaseUser user){
        if (user!=null){
            //tvoutput.setText(user.getEmail());
            firebaseDatabase.getReference(user.getUid()).child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserModel userModel=dataSnapshot.getValue(UserModel.class);
                    tvoutput.setText(userModel.getName());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            


        }


    }


}
