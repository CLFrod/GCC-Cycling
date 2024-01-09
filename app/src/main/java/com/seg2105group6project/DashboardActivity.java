package com.seg2105group6project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private ListView eventListView;
    private List<Event> events;
    private DatabaseReference mDatabase;

    private Button searchEventBtn;
    private Button rateClubBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ParticipantAccount participant = (ParticipantAccount) getIntent().getSerializableExtra("participant");
        TextView WelcomeTextView = findViewById(R.id.welcomeTextView);

        WelcomeTextView.setText("Welcome " + participant.getFirstName() + "!");

        searchEventBtn = findViewById(R.id.searchEventBtn);
        rateClubBtn = findViewById(R.id.rateClubBtn);

        searchEventBtn.setOnClickListener(new View.OnClickListener() { //when search button is pressed, take to search event page
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, EventSearch.class);
                intent.putExtra("participant", participant); //keep credentials
                startActivity(intent); //open new page
            }
        });

        rateClubBtn.setOnClickListener(new View.OnClickListener() { //when search button is pressed, take to search event page
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, ClubSearch.class);
                intent.putExtra("participant", participant); //keep credentials
                startActivity(intent); //open new page
            }
        });
    }


}