package com.seg2105group6project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateEvent extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private TextView selectDateTextView;

    // EditText variables
    private TextInputEditText regionTextInput;
    private TextInputEditText paceTextInput;
    private TextInputEditText distanceTextInput;
    private TextInputEditText levelTextInput;
    private TextInputEditText minAgeTextInput;
    private TextInputEditText maxParticipantsTextInput;
    private TextInputEditText registrationFeeTextInput;
    private TextInputEditText eventNameTextInput;
    private EventType selectedEventType;
    private OrganizerAccount organizer;
    List<Event> Events;
    private String date;
    Spinner spinnerEventTypes;
    Button createEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        organizer = (OrganizerAccount) getIntent().getSerializableExtra("organizer");

        mDatabase = FirebaseDatabase.getInstance("https://seg-2105-f23-group-6-project-default-rtdb.firebaseio.com").getReference();
        Events = new ArrayList<>();
        regionTextInput = findViewById(R.id.regionTextInput);
        distanceTextInput= findViewById(R.id.distanceTextInput);
        minAgeTextInput = findViewById(R.id.minAgeTextInput);
        paceTextInput = findViewById(R.id.paceTextInput);
        levelTextInput = findViewById(R.id.levelTextInput);
        registrationFeeTextInput = findViewById(R.id.registrationFeeTextInput);
        maxParticipantsTextInput = findViewById(R.id.maxParticipantsTextInput);
        eventNameTextInput = findViewById(R.id.eventNameTextInput);
        spinnerEventTypes = findViewById(R.id.spinnerEventTypes);


        createEvent = findViewById(R.id.btnCreateEvent);


        selectDateTextView = findViewById(R.id.selectDateTextView2);

        selectDateTextView.setOnClickListener(v -> showDatePickerDialog());
        createEvent.setOnClickListener(v -> createEvent());


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

        spinnerEventTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEventType = (EventType) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedEventType = null;
            }
        });

    }

    private void showDatePickerDialog() {
        // Use the current date as the default date in the picker
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEvent.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // This is where you handle the date selected
                        String selectedDate = selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        // You can display or use the selected date as needed
                        // For example, updating the button's text
                        date = selectedDate;
                        selectDateTextView.setText(selectedDate);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    public boolean checkForNumber(String check){
        String regex = "^[0-9]+$";
        return check.matches(regex);
    }

    public boolean checkForLetters(String check){
        String regex = "^[a-zA-Z]+$";
        return check.matches(regex);
    }
    public boolean checkForEventName(String check){
        String regex = "^[a-zA-Z0-9[ ]+,]+$";
        return check.matches(regex);
    }
    public void createEvent(){
        String pace = String.valueOf(paceTextInput.getText());
        String registrationFee = String.valueOf(registrationFeeTextInput.getText());
        String minAge = String.valueOf(minAgeTextInput.getText());
        String region = String.valueOf(regionTextInput.getText());
        String level = String.valueOf(levelTextInput.getText());
        String distance = String.valueOf(distanceTextInput.getText());
        String maxParticipants = String.valueOf(maxParticipantsTextInput.getText());
        String eventName = String.valueOf(eventNameTextInput.getText());
        StringBuilder errorToast = new StringBuilder();
        int _pace;
        int _registrationFee;
        int _minAge;
        int _level;
        int _distance;
        int _maxParticipants;


        // simple field validation for all of the entry fields.
        if(!checkForEventName(eventName)){
            errorToast.append("Event Name should not have any special characters excluding commas and spaces. ");
        }
        if (!checkForEventName(region)){
            errorToast.append("The Region should only contain letters and should follow this EX: Country, Province, City, or just an address. ");
        }
        if(!checkForNumber(registrationFee)){
            errorToast.append("Registration Fee should only contain numbers. ");
        }
        if(!checkForNumber(minAge)){
            errorToast.append("Minimum Age should only contain numbers. ");
        }
        if(!checkForNumber(level)){
            errorToast.append("Level should only contain numbers. ");
        }
        else if((Integer.parseInt(level) > 3) || Integer.parseInt(level) < 1){
            errorToast.append("Level should only be from 1 to 3. ");
        }
        if(!checkForNumber(pace)){
            errorToast.append("The pace should only contain numbers. ");
        }
        if(!checkForNumber(distance)){
            errorToast.append("The distance should only contain numbers. ");
        }
        if(!checkForNumber(maxParticipants)){
            errorToast.append("The max number of participants should only contain numbers. ");
        }
        if(date == null){
            errorToast.append("Please select a date. ");
        }
        if(selectedEventType == null){
            errorToast.append("Please pick an event type.");
        }

        // not the best way of doing things considering there are so many fields but this works for now.
        if(errorToast.length() >=1){
            Toast.makeText(getApplicationContext(),errorToast.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        else{
            _minAge = Integer.parseInt(minAge);
            _level = Integer.parseInt(level);
            _registrationFee = Integer.parseInt(registrationFee);
            EventRegistration reg = new EventRegistration(_minAge, _level, _registrationFee, date);

            _distance = Integer.parseInt(distance);
            _maxParticipants = Integer.parseInt(maxParticipants);
            _pace = Integer.parseInt(pace);




            DatabaseReference dR = FirebaseDatabase.getInstance("https://seg-2105-f23-group-6-project-default-rtdb.firebaseio.com").getReference("Events");

            Event event = new Event(region, _distance, _pace, _maxParticipants,eventName, organizer, selectedEventType, reg);

            dR.child(eventName).setValue(event);
            Toast.makeText(getApplicationContext(),"Event Created Successfully!", Toast.LENGTH_LONG).show();
        }
    }

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

        spinnerEventTypes.setAdapter(adapter);
    }

}