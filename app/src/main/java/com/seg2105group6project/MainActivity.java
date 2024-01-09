package com.seg2105group6project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://seg-2105-f23-group-6-project-default-rtdb.firebaseio.com").getReference(); // Initialize DatabaseReference

    }

    // Commenting this out since it doesnt seem to be doing anything as of right now
    //@Override
    //public void onStart() {
    //    super.onStart();
    //    FirebaseUser currentUser = mAuth.getCurrentUser();
    //    if(currentUser != null){
    //        reload();
    //    }
    //}

    // In MainActivity class
    private void signIn(String username, String password) {
        if("admin".equals(username)&& "admin".equals(password)){
            Intent adminLogin = new Intent(MainActivity.this,AdminDashboard.class);
            startActivity(adminLogin);
            finish();
            return;
        }

                mDatabase.child("Users").child(username).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                email = dataSnapshot.child("email").getValue(String.class);
                                if (email == null || email.equals("")) {
                                    Toast.makeText(MainActivity.this, "Username does not exist", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                                // Get the user's role from the database
                                                mDatabase.child("Users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        String role = dataSnapshot.child("role").getValue(String.class);
                                                        if ("Organizer".equals(role)) {
                                                            // Redirect to Organizer dashboard
                                                            OrganizerAccount organizer = dataSnapshot.getValue(OrganizerAccount.class);
                                                            Intent intent = new Intent(MainActivity.this, OrganizerDashboard.class);
                                                            intent.putExtra("organizer", organizer); // Pass the username as an extra
                                                            startActivity(intent);
                                                        } else if ("Participant".equals(role)) {
                                                            // Redirect to Participant dashboard
                                                            ParticipantAccount participant = dataSnapshot.getValue(ParticipantAccount.class);
                                                            Log.d("DEBUG", "Participant: " + participant);
                                                            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                                                            intent.putExtra("participant", participant); // Pass the username as an extra
                                                            startActivity(intent);
                                                        }
                                                        finish(); // Finish MainActivity
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        Toast.makeText(MainActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        }

                                    }
                                    });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

    }


    // onCreateClubAccount opens the create club account activity view
    public void onCreateClubAccount(View view){
        Intent intent = new Intent(this, CreateOrganizerAccount.class);
        startActivity(intent);
    }

    public void onCreateParticipantAccount(View view){
        Intent intent = new Intent(this, CreateParticipantAccount.class);
        startActivity(intent);
    }

    private void reload() {}
    public void onLogin(View view) throws InterruptedException {
        // Get the values from the TextInputEditText fields
        TextInputEditText usernameInput = findViewById(R.id.userNameTextInput);
        TextInputEditText passwordInput = findViewById(R.id.passTextInput);

        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        StringBuilder sb = new StringBuilder();
        String loginCheck;

        if(username.equals("")) {
            sb.append("Username field is empty. ");
        }
        if(password.equals("")){
            sb.append("Password Field is empty.");
        }
        if (sb.length() >=1){
            Toast.makeText(getApplicationContext(),sb.toString(), Toast.LENGTH_LONG).show();
        } else{
            // Call the signIn method with the username and password
            signIn(username, password);

        }


    }

}