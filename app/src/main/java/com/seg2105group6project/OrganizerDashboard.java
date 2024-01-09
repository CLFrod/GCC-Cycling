package com.seg2105group6project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrganizerDashboard extends AppCompatActivity {
    Button btnCreateEvent;
    private TextView selectDateTextView2;
    OrganizerAccount organizer;
    private String date;
    Spinner spinnerEventTypes2;
    private EventType selectedEventType;
    ListView eventListView;

    List<Event> events;
    private Event currentEvent;
    private DatabaseReference mDatabase;

    public OrganizerDashboard() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_dashboard);
        mDatabase = FirebaseDatabase.getInstance("https://seg-2105-f23-group-6-project-default-rtdb.firebaseio.com").getReference();
        eventListView = findViewById(R.id.eventListView);
        events = new ArrayList<>();
        organizer = (OrganizerAccount) getIntent().getSerializableExtra("organizer");
        TextView WelcomeTextView = findViewById(R.id.welcomeTextView);

        WelcomeTextView.setText("Welcome " + organizer.getUserName() + "!");
        btnCreateEvent = findViewById(R.id.btnCreateEvent);
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchEventCreation();
            }
        });
        eventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Event event = events.get(position);
                showUpdateEventDialog(event.getEventName(), event.getEventType());
                return true;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.child("Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                events.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // getting product
                    Event event = postSnapshot.getValue(Event.class);
                    // adding product to list
                    if (event.getMainOrganizer().getUserName().equals(organizer.getUserName())) {
                        events.add(event);
                    } else {
                        continue;
                    }
                }

                EventList eventAdapter = new EventList(OrganizerDashboard.this, events);
                // attaching adapter to listview
                eventListView.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do nothing for now
            }
        });

    }

    private void launchEventCreation() {
        Intent intent = new Intent(this, CreateEvent.class);
        intent.putExtra("organizer", organizer);
        startActivity(intent);
    }


    // deleteEvent method based on Carlos (Callum)'s deleteEventType method in AdminDashboard.java.
    public void deleteEvent(String eventName){
        DatabaseReference dR = FirebaseDatabase.getInstance("https://seg-2105-f23-group-6-project-default-rtdb.firebaseio.com").getReference("Events");

        dR.child(eventName).removeValue().addOnCompleteListener(task ->{
            if(task.isSuccessful()){ //if successful, send success Toast
                Toast.makeText(getApplicationContext(),"Event Deleted Successfully",Toast.LENGTH_SHORT).show();
            }
            else{//if unsuccessful, send toast message
                Toast.makeText(getApplicationContext(),"Event not Deleted ",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateEventDialog(String eventName, EventType type) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_event_dialog, null);
        dialogBuilder.setView(dialogView);


        EditText eventNameEditText = (EditText) dialogView.findViewById(R.id.eventNameUpdateEditText);
        EditText eventPaceUpdateEditText = (EditText) dialogView.findViewById(R.id.eventPaceUpdateEditText);
        EditText eventMinAgeEditText = (EditText) dialogView.findViewById(R.id.eventMinAgeUpdateEditText);
        EditText eventRegionEditText = (EditText) dialogView.findViewById(R.id.eventRegionUpdateEditText);
        EditText eventDistanceEditText = (EditText) dialogView.findViewById(R.id.eventDistanceUpdateEditText);
        EditText eventRegistrationFeeEditText = (EditText) dialogView.findViewById(R.id.eventRegistrationFeeUpdateEditText);
        EditText eventDifficultyUpdateEditText = (EditText) dialogView.findViewById(R.id.eventDifficultyUpdateEditText);
        EditText eventMaxParticipantsUpdateEditText = (EditText) dialogView.findViewById(R.id.eventMaxParticipantsUpdateEditText);
        EditText dateEditText = (EditText) dialogView.findViewById(R.id.dateEditText);
        EditText monthEditText = (EditText) dialogView.findViewById(R.id.monthDateEditText);
        EditText yearEditText = (EditText) dialogView.findViewById(R.id.yearDateEditText);

        Button btnDelete = (Button) dialogView.findViewById(R.id.btnDelete);
        Button btnUpdate = (Button) dialogView.findViewById(R.id.btnUpdate);

        spinnerEventTypes2 = (Spinner) dialogView.findViewById(R.id.eventTypeSpinner2);


        dialogBuilder.setTitle("Update / Delete Event ");
        mDatabase.child("EventTypes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<EventType> eventTypes = new ArrayList<>();
                for(DataSnapshot eventTypeSnapshot : snapshot.getChildren()){
                    EventType eventType = eventTypeSnapshot.getValue(EventType.class);
                    eventTypes.add(eventType);
                }
                setupDropdown(eventTypes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database error, try again", Toast.LENGTH_SHORT).show();
            }
        });

        spinnerEventTypes2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEventType = (EventType) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedEventType = null;
            }
        });

        final AlertDialog b = dialogBuilder.create();

        b.show();



        // DELETE on Click
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEvent(eventName);
                b.dismiss();
            }
        });

        // UPDATE ON CLICK
        btnUpdate.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view){
                String newName = eventNameEditText.getText().toString().trim();

                String newPace = eventPaceUpdateEditText.getText().toString().trim();
                String newMinAge = eventMinAgeEditText.getText().toString().trim();
                String newMaxParticipants = eventMaxParticipantsUpdateEditText.getText().toString().trim();
                String newLevel = eventDifficultyUpdateEditText.getText().toString().trim();
                String newRegistrationFee = eventRegistrationFeeEditText.getText().toString().trim();
                String newRegion = eventRegionEditText.getText().toString().trim();
                String newDistance = eventDistanceEditText.getText().toString().trim();
                String newYear = yearEditText.getText().toString().toString();
                String newMonth = monthEditText.getText().toString();
                String newDate = dateEditText.getText().toString();
                DatabaseReference dR = FirebaseDatabase.getInstance("https://seg-2105-f23-group-6-project-default-rtdb.firebaseio.com").getReference("Events").child(eventName);
                dR.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentEvent = dataSnapshot.getValue(Event.class);
                        // Now 'event' is the specific event object you wanted
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors
                    }
                });

                updateEvent(currentEvent, newName, newPace, newMinAge, newMaxParticipants, newLevel, newRegistrationFee, newDate,newMonth, newYear, newRegion, newDistance, selectedEventType, eventName);
                b.dismiss();
            }
        });
    }



    //String region, double distance, int averagePace, int maxParticipants, String eventName, OrganizerAccount organizer, EventType eventType, String id
    private void updateEvent(Event updatedEvent, String newName, String newPace, String newMinAge, String newMaxParticipants, String newLevel, String newRegistrationFee, String newDate, String newMonth, String newYear, String newRegion, String newDistance, EventType eventType, String currentEventName){//TOTALLY pasted from admin
                Event newEvent;
                StringBuilder errorToast = new StringBuilder();
                    if(!checkForEventName(newName)){
                        errorToast.append("Event Name should not have any special characters excluding commas and spaces. ");
                    }
                    if (!checkForEventName(newRegion)){
                        errorToast.append("The Region should only contain letters and should follow this EX: Country, Province, City, or just an address. ");
                    }
                    if(!checkForNumber(newRegistrationFee)){
                        errorToast.append("Registration Fee should only contain numbers. ");
                    }
                    if(!checkForNumber(newMinAge)){
                        errorToast.append("Minimum Age should only contain numbers. ");
                    }
                    if(!checkForNumber(newLevel)){
                        errorToast.append("Level should only contain numbers. ");
                    }
                    else if((Integer.parseInt(newLevel) > 3) || Integer.parseInt(newLevel) < 1){
                        errorToast.append("Level should only be from 1 to 3. ");
                    }
                    if(!checkForNumber(newPace)){
                        errorToast.append("The pace should only contain numbers. ");
                    }
                    if(!checkForNumber(newDistance)){
                        errorToast.append("The distance should only contain numbers. ");
                    }
                    if(!checkForNumber(newMaxParticipants)){
                        errorToast.append("The max number of participants should only contain numbers. ");
                    }
                    if(!checkDay(newDate)){
                        errorToast.append("Make sure date is format (DD). ");
                    }
                    if(!checkYear(newYear)){
                        errorToast.append("Make sure year is format (YYYY). ");
                    }
                    if(!checkForNumber(newMonth)){
                        errorToast.append("Make sure Month is format (MM). ");
                    }
                    if(eventType == null){
                        errorToast.append("Please pick an event type.");
                    }

                    // not the best way of doing things considering there are so many fields but this works for now.
                    if(errorToast.length() >=1){
                        Toast.makeText(getApplicationContext(),errorToast.toString(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        int minAge = Integer.parseInt(newMinAge);
                        int level = Integer.parseInt(newLevel);
                        int registrationFee = Integer.parseInt(newRegistrationFee);
                        EventRegistration reg = new EventRegistration(minAge, level, registrationFee, date);

                        int distance = Integer.parseInt(newDistance);
                        int maxParticipants = Integer.parseInt(newMaxParticipants);
                        int pace = Integer.parseInt(newPace);

                        DatabaseReference dR = FirebaseDatabase.getInstance("https://seg-2105-f23-group-6-project-default-rtdb.firebaseio.com").getReference("Events");

                        newEvent = new Event(newRegion, distance, pace, maxParticipants, newName, organizer, selectedEventType, reg);

                        // take new event and update the old one.
                        deleteEvent(currentEventName);
                        dR.child(newName).setValue(newEvent);
                    }
    }



    // copy pasted from createEvent
    public boolean checkForNumber(String check){
        String regex = "^[0-9]+$";
        return check.matches(regex);
    }
    // copy pasted from createEvent
    public boolean checkForLetters(String check){
        String regex = "^[a-zA-Z]*$";
        return check.matches(regex);
    }
    // copy pasted from createEvent
    public boolean checkForEventName(String check){
        String regex = "^[a-zA-Z0-9[ ]+,]+$";
        return check.matches(regex);
    }
    public boolean checkYear(String check){
        String regex = "^\\d{4}$";
        return check.matches(regex);
    }
    public boolean checkDay(String check){
        String regex = "^(0[1-9]|[12][0-9]|3[01])$";
        return check.matches(regex);
    }
    public boolean checkMonth(String check){
        String regex = "^(0[1-9]|1[0-2])$";
        return check.matches(regex);
    }

    // copy pasted from createEvent
    private void setupDropdown(List<EventType> eventTypes){
        ArrayAdapter<EventType> adapter = new ArrayAdapter<EventType>(this, android.R.layout.simple_spinner_item, eventTypes){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setText(eventTypes.get(position).getEventName());
                return view;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setText(eventTypes.get(position).getEventName());
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerEventTypes2.setAdapter(adapter);
    }

}
