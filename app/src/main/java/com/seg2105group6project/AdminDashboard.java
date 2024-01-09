package com.seg2105group6project;

//import statements
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class AdminDashboard extends AppCompatActivity {
    private EditText deleteParticipantEditText;
    private EditText deleteOrganizerEditText;
    private EditText eventNameEditText;
    private EditText eventDescriptionEditText;
    private Button btnDeleteParticipant;
    private Button btnDeleteClub;
    private Button btnAddEventType;
    ListView eventTypeListView;
    List<EventType> EventTypes;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        EventTypes = new ArrayList<>();
        //Initialize firebase
        mDatabase = FirebaseDatabase.getInstance("https://seg-2105-f23-group-6-project-default-rtdb.firebaseio.com").getReference();

        //Initialize text fields and buttons
        deleteOrganizerEditText = (EditText) findViewById(R.id.deleteOrganizerEditText);
        deleteParticipantEditText = (EditText) findViewById(R.id.deleteParticipantEditText);
        eventNameEditText = (EditText) findViewById(R.id.eventNameEditText);
        eventDescriptionEditText = (EditText) findViewById(R.id.eventDescriptionEditText);
        btnDeleteClub = (Button) findViewById(R.id.btnDeleteClub);
        btnDeleteParticipant = (Button) findViewById(R.id.btnDeleteParticipant);
        btnAddEventType = (Button) findViewById(R.id.btnAddEventType);
        eventTypeListView = (ListView) findViewById(R.id.eventTypeListView);



        btnDeleteParticipant.setOnClickListener(new View.OnClickListener() { //listener for participant deletion button
            @Override
            public void onClick(View view) {//call deleteAccount for participant account
                deleteAccount(deleteParticipantEditText.getText().toString().trim());
            }
        });
        btnDeleteClub.setOnClickListener(new View.OnClickListener() {//listener for club deletion button
            @Override
            public void onClick(View view) {//call deleteAccount for club account
                deleteAccount(deleteOrganizerEditText.getText().toString().trim());
            }
        });
        btnAddEventType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                 addEventType();
            }
        });

        eventTypeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                EventType eventType = EventTypes.get(position);
                showUpdateDeleteDialog(eventType.getEventName());
                return true;
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        mDatabase.child("EventTypes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                EventTypes.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    // getting product
                    EventType eventType = postSnapshot.getValue(EventType.class);
                    // adding product to list
                    EventTypes.add(eventType);
                }

                EventTypeList eventTypeAdapter = new EventTypeList(AdminDashboard.this, EventTypes);
                // attaching adapter to listview
                eventTypeListView.setAdapter(eventTypeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do nothing for now
            }
        });

    }

    private void showUpdateDeleteDialog(String eventTypeName){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_delete_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText eventTypeNameEditText = (EditText) dialogView.findViewById(R.id.eventNameUpdateEditText);
        final EditText eventTypeDescriptionEditText = (EditText) dialogView.findViewById(R.id.eventDescriptionUpdateEditText);
        final Button btnUpdate = (Button) dialogView.findViewById(R.id.btnUpdate);
        final Button btnDelete = (Button) dialogView.findViewById(R.id.btnDelete);


        dialogBuilder.setTitle(eventTypeName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        // update functionality
        btnUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String newName = eventTypeNameEditText.getText().toString().trim();
                String newDescription = eventTypeDescriptionEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(newName) && !TextUtils.isEmpty(newDescription)){
                    updateEventType(eventTypeName, newName, newDescription);
                    b.dismiss();
                }
                else{
                    Toast.makeText(getApplicationContext(),"A new name or description is missing",Toast.LENGTH_SHORT).show();
                }
            }

        });

        // delete functionality
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEventType(eventTypeName);
                b.dismiss();
            }
        });



    }



    private void deleteAccount(String username){ //method to delete accounts

        //if a username is not entered, send toast message
        if(username.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please enter a username",Toast.LENGTH_SHORT).show();
            return;
        }
        //search Users in firebase for the entered username, and attempt to remove it
        // Using a lambda expression with a Task (deleteAccount) as a parameter for isSuccessful,
        // allowing asynchronous execution without explicitly using a separate onCompleteListener.
        mDatabase.child("Users").child(username).removeValue().addOnCompleteListener(task ->{
            if(task.isSuccessful()){ //if successful, send success Toast
                Toast.makeText(getApplicationContext(),"Account deleted successfully",Toast.LENGTH_SHORT).show();
            }
            else{//if unsuccessful, send toast message
                Toast.makeText(getApplicationContext(),"Username not recognized",Toast.LENGTH_SHORT).show();
            }
        });

        //whether successful or unsuccessful, clear text box after button is pushed
        if(username.equals(deleteOrganizerEditText.getText().toString().trim())){
            deleteOrganizerEditText.setText("");
        }
        else if(username.equals(deleteParticipantEditText.getText().toString().trim())){
            deleteParticipantEditText.setText("");
        }
    }

    private void addEventType(){
        String eventName = eventNameEditText.getText().toString().trim();
        String eventDescription = eventDescriptionEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(eventName)){
            DatabaseReference dR = FirebaseDatabase.getInstance("https://seg-2105-f23-group-6-project-default-rtdb.firebaseio.com").getReference("EventTypes");




            EventType eventType = new EventType(eventName, eventDescription);

            dR.child(eventName).setValue(eventType);

            eventNameEditText.setText("");
            eventDescriptionEditText.setText("");

            Toast.makeText(this, "EventType added", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }

    private void updateEventType(String eventName, String newName, String newDescription){
        DatabaseReference dR = FirebaseDatabase.getInstance("https://seg-2105-f23-group-6-project-default-rtdb.firebaseio.com").getReference("EventTypes");
        EventType eventType = new EventType(newName, newDescription);



        dR.child(eventName).removeValue().addOnCompleteListener(task ->{
            if(task.isSuccessful()){ //if successful, send success Toast
                Toast.makeText(getApplicationContext(),"Event Type Updated Successfully",Toast.LENGTH_SHORT).show();
            }
            else{//if unsuccessful, send toast message
                Toast.makeText(getApplicationContext(),"Event Type not updated",Toast.LENGTH_SHORT).show();
            }
        });

        dR.child(newName).setValue(eventType);


    }

    public void deleteEventType(String eventName){
        DatabaseReference dR = FirebaseDatabase.getInstance("https://seg-2105-f23-group-6-project-default-rtdb.firebaseio.com").getReference("EventTypes");

        dR.child(eventName).removeValue().addOnCompleteListener(task ->{
            if(task.isSuccessful()){ //if successful, send success Toast
                Toast.makeText(getApplicationContext(),"Event Type Deleted Successfully",Toast.LENGTH_SHORT).show();
            }
            else{//if unsuccessful, send toast message
                Toast.makeText(getApplicationContext(),"Event Type not Deleted ",Toast.LENGTH_SHORT).show();
            }
        });
    }

}