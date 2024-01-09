package com.seg2105group6project;

import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CreateParticipantAccount extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private TextInputEditText firstNameEditText;
    private TextInputEditText lastNameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText userNameEditText;
    private TextInputEditText passwordEditText;
    private Button btnCreateAccount;

    List<ParticipantAccount> participants;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_participant_account);
        participants = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance("https://seg-2105-f23-group-6-project-default-rtdb.firebaseio.com").getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        firstNameEditText = (TextInputEditText) findViewById(R.id.firstNameTextInput);
        lastNameEditText = (TextInputEditText) findViewById(R.id.lastNameTextInput);
        emailEditText = (TextInputEditText) findViewById(R.id.emailTextInput);
        userNameEditText = (TextInputEditText) findViewById(R.id.userNameTextInput);
        passwordEditText = (TextInputEditText) findViewById(R.id.passTextInput);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);


        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createParticipantAccount();
            }
        });

    }

    public void createParticipantAccount(){
        firstNameEditText = findViewById(R.id.firstNameTextInput);
        lastNameEditText = findViewById(R.id.lastNameTextInput);
        emailEditText = findViewById(R.id.emailTextInput);
        userNameEditText = findViewById(R.id.userNameTextInput);
        passwordEditText = findViewById(R.id.passTextInput);

        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String userName = userNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();


        //uses the booleans created for validity of each input, and if the boolean is false,
        //return a toast message explaining the error, and for which input.
        //Using StringBuilder so if there are multiple errors, they display in one single toast
        StringBuilder errorToast = new StringBuilder();

        if(!firstNameValidity(firstName)){
            errorToast.append("Invalid first name. Must contain only letters and cannot be blank.\n");
        }
        if(!lastNameValidity(lastName)){
            errorToast.append("Invalid last name. Must contain only letters and cannot be blank.\n");
        }
        if(!emailValidity(email)){
            errorToast.append("Please use a valid email address.\n");
        }
        if(!userNameValidity(userName)){
            errorToast.append("Invalid username. Must have between 3-20 characters.\n");
        }
        if(!passwordValidity(password)){
            errorToast.append("Invalid password. Must have between 8-20 characters, contain at least one number, and at least one uppercase letter.\n ");
        }
        //puts all error messages into one string and returns as toast message.
        if(errorToast.length() >=1){
            Toast.makeText(getApplicationContext(),errorToast.toString(), Toast.LENGTH_LONG).show();
        }
        // Otherwise all of the fields have been validated and then begin creating an account.
        else{
            mAuth.createUserWithEmailAndPassword(email, password);

            ParticipantAccount participant = new ParticipantAccount(userName, password, email, firstName, lastName);

            mDatabase.child(userName).setValue(participant);
            firstNameEditText.setText("");
            lastNameEditText.setText("");
            emailEditText.setText("");
            userNameEditText.setText("");
            passwordEditText.setText("");
            Toast.makeText(getApplicationContext(), "Account Created Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean firstNameValidity(String nameFirst) {//check that firstname only contains chars and isnt empty
        if (nameFirst.isEmpty() || !nameFirst.matches("[a-zA-Z]+")) {
            return false;
        }
        return true;
    }

    private boolean lastNameValidity(String nameLast) {//check that lastname only contains chars and isnt empty
        if (nameLast.isEmpty() || !nameLast.matches("[a-zA-Z]+")) { //regex was very helpful for this part
            return false;
        }
        return true;
    }

    private boolean emailValidity(String email) {
        //using a regex, a pattern of characters that describes a string(making sure only valid chars are used)
        String emailChars = "^[a-zA-Z0-9+_.-]+@(.+)$";// a-z, 0-9, . _ - are all valid characters.

        if (email.matches(emailChars)) {
            return true;
        }
        return false;//if input contains a character that is not valid as dictated in regex, return false
    }

    private boolean userNameValidity(String userName) {//check that username is between 3-20 chars, and isnt empty
        if (userName.isEmpty() || userName.length() < 3 || userName.length() > 20) { // no restriction on special chars
            return false;
        }
        return true;
    }

    private boolean passwordValidity(String password) {
        if (password.length() < 8 || password.length() > 20) { // password has to be between 8-20 chars
            return false;
        } else if (!password.matches(".*[A-Z].*")) {// .* checks for at least one uppercase
            return false;
        } else if (!password.matches(".*[0-9].*")) {// .* checks for at least one number
            return false;
        }
        return true;
    }
}


