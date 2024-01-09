package com.seg2105group6project;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventSearch extends AppCompatActivity {
    private List<Event> events;
    private DatabaseReference mDatabase;
    private ListView eventListView;
    private EditText searchEditText;
    private Button searchButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_event);
        ParticipantAccount participant = (ParticipantAccount) getIntent().getSerializableExtra("participant");
        TextView WelcomeTextView = findViewById(R.id.welcomeTextView);

        WelcomeTextView.setText("Welcome " + participant.getFirstName() + "!");

        eventListView = findViewById(R.id.eventListView); // make sure you have a ListView with this id in your layout
        events = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchEditText.getText().toString().trim();
                performSearch(query);
            }
        });


        loadEvents();
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event selectedEvent = events.get(position);
                promptForAgeAndRegister(selectedEvent);
            }
        });



    }

    private void performSearch(String query) { //method to search the events using a keyword
        List<Event> searchResults = new ArrayList<>(); //create list for search results

        for (Event event : events) { //look through events for entered keyword (type, name, club name)
            if (event.getEventName().toLowerCase().contains(query.toLowerCase()) || event.getEventType().getEventName().toLowerCase().contains(query)
                    || event.getMainOrganizer().getUserName().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(event); //if found add it to the list

            }
        }
        displaySearchResults(searchResults); //display the list
    }
    private void displaySearchResults(List<Event> searchResults){ //method to show the search results
        EventList adapter = new EventList(EventSearch.this,searchResults); //update list with search results
        eventListView.setAdapter(adapter);

        if(searchResults.isEmpty()){ //toast message if no events are found using keyword
            Toast.makeText(EventSearch.this,"No events found.",Toast.LENGTH_SHORT).show();
        }
    }
    private void loadEvents() {
        mDatabase.child("Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Event event = postSnapshot.getValue(Event.class);
                    events.add(event);
                }
                EventList adapter = new EventList(EventSearch.this, events);
                eventListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EventSearch.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void promptForAgeAndRegister(final Event event) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Enter Your Age");

        final EditText ageInput = new EditText(this);
        ageInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(ageInput);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    int enteredAge = Integer.parseInt(ageInput.getText().toString());
                    int eligible = ageEligible(event.getEventRegistration().getMinAge(), enteredAge);
                    if (eligible == 1) {
                        confirmRegistrationDialog(event); //call method to show confirmation dialog box if age entered is valid
                    } else if(eligible == 2){ //if age is smaller toast message telling user.

                        Toast.makeText(EventSearch.this, "You do not meet the age requirement for this event.", Toast.LENGTH_SHORT).show();
                    }
                    else{ //if entered age is too large
                        Toast.makeText(EventSearch.this, "Age must be under 100 years", Toast.LENGTH_SHORT).show();

                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(EventSearch.this, "Please enter a valid age.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void confirmRegistrationDialog(final Event event){ //confirmation dialog to register for event
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Register for this event?");
        builder.setMessage("Would you like to register for this event?");

        builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                registerForEvent(event); //register for event if the button is clicked
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void registerForEvent(Event event) {
        if (event.getRegisteredParticipants() == null) {
            event.setRegisteredParticipants(new ArrayList<>());
        }
        ParticipantAccount participant = (ParticipantAccount) getIntent().getSerializableExtra("participant");

        boolean alreadyRegistered = false; //set registered to false

        //check array of registered participants for user. assumes each participants username is unique
        for(ParticipantAccount registeredParticipant: event.getRegisteredParticipants()){
            if(registeredParticipant.getUserName().equals(participant.getUserName())){ // if participants username is found in list of registered participants
                alreadyRegistered = true; //set registered to true
                break; //break loop
            }
        }
        if(!alreadyRegistered){
            event.getRegisteredParticipants().add(participant);
            updateEventInDatabase(event);
            Toast.makeText(EventSearch.this, "Registered successfully!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(EventSearch.this, "You are already registered for this event.",Toast.LENGTH_SHORT).show();
        }
    }


    private void updateEventInDatabase(Event event) {
        mDatabase.child("Events").child(event.getEventName()).setValue(event)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(EventSearch.this, "Registered successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(EventSearch.this, "Failed to register: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public static int ageEligible(int minAge, int age) {
        if (age >= minAge && age <= 100) return 1;
        if (age < minAge) return 2;
        return 3;
    }
}