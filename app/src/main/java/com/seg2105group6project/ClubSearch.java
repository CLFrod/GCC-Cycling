package com.seg2105group6project;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
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

public class ClubSearch extends AppCompatActivity {
    private List<OrganizerAccount> clubs;
    private DatabaseReference mDatabase;
    private ListView clubListView;
    private EditText searchClubEditText;
    private Button searchButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_club);
        ParticipantAccount participant = (ParticipantAccount) getIntent().getSerializableExtra("participant");
        TextView WelcomeTextView = findViewById(R.id.welcomeTextView);

        WelcomeTextView.setText("Welcome " + participant.getFirstName() + "!");

        clubListView = findViewById(R.id.clubListView);
        clubs = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        searchClubEditText = findViewById(R.id.searchClubsEditText);
        searchButton = findViewById(R.id.searchClubsButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchClubEditText.getText().toString().trim();
                performSearch(query);
            }
        });


        loadEvents();
        clubListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrganizerAccount club = clubs.get(position);
                leaveRating(club);
            }
        });



    }

    private void performSearch(String query) { //method to search the events using a keyword
        List<OrganizerAccount> searchResults = new ArrayList<>(); //create list for search results

        for (OrganizerAccount club : clubs) { //look through clubs for entered keyword (type, name, club name)
            if (club.getUserName() == null) continue;
            if (club.getUserName().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(club); //if found add it to the list

            }
        }
        displaySearchResults(searchResults); //display the list
    }
    private void displaySearchResults(List<OrganizerAccount> searchResults){ //method to show the search results
        ClubList adapter = new ClubList(ClubSearch.this,searchResults); //update list with search results
        clubListView.setAdapter(adapter);

        if(searchResults.isEmpty()){ //toast message if no events are found using keyword
            Toast.makeText(ClubSearch.this,"No clubs found.",Toast.LENGTH_SHORT).show();
        }
    }
    private void loadEvents() {
        mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clubs.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    OrganizerAccount club = postSnapshot.getValue(OrganizerAccount.class);
                    if(postSnapshot.child("role").getValue().equals("Organizer")) clubs.add(club);
                }
                ClubList adapter = new ClubList(ClubSearch.this, clubs);
                clubListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ClubSearch.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void leaveRating(final OrganizerAccount club) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Rating from 0 - 5 and comment below");
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.rate_club_dialog, null);
        builder.setView(dialogView);

        final EditText ratingInput = (EditText) dialogView.findViewById(R.id.ratingEditText);
        final EditText commentInput = (EditText) dialogView.findViewById(R.id.commentEditText);
        ratingInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        commentInput.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);


        builder.setPositiveButton("Submit Rating", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    int rating = Integer.parseInt(ratingInput.getText().toString());
                    String comment = commentInput.getText().toString();
                    int eligibleRating = acceptableRating(rating);
                    int eligibleComment = acceptableComment(comment);
                    String buildToast = "";

                    if (eligibleRating == 1 && eligibleComment == 1) {
                        confirmRegistrationDialog(club, rating, comment); //call method to show confirmation dialog box if age entered is valid
                    } else {
                        if(eligibleRating == 2){ //if rating is too large toast message telling user.
                            buildToast += "Rating cannot exceed 5. ";
                        } else if (eligibleRating == 3){ //if entered rating is too small
                            buildToast += "Rating cannot be negative. ";
                        }
                        if(eligibleComment == 2){ //if comment is empty or null, toast message telling user.
                            buildToast += "Comment cannot be empty.";
                        } else if(eligibleComment == 3){ //if entered rating is too small
                            buildToast += "Comment is too short. Please justify your rating.";
                        } else if(eligibleComment == 4) {
                            buildToast += "Comment is too long.";
                        }
                        Toast.makeText(ClubSearch.this, buildToast, Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(ClubSearch.this, "Please enter a valid rating.", Toast.LENGTH_SHORT).show();
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

    private void confirmRegistrationDialog(final OrganizerAccount club, double rating, String comment){ //confirmation dialog to register for event
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setTitle("Rate this Club?");
        builder.setMessage("Would you like to leave your rating for this club?");

        builder.setPositiveButton("Rate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                rateClub(club, rating, comment); //register for event if the button is clicked
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

    private void rateClub(OrganizerAccount club, double rating, String comment) {
        if (club.getRatings() == null) {
            club.setRatings(new ArrayList<>());
        }
        club.addRating(new Rating(rating, comment));
        updateClubInDatabase(club);
    }


    private void updateClubInDatabase(OrganizerAccount club) {
        mDatabase.child("Users").child(club.getUserName()).setValue(club)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(ClubSearch.this, "Rating successful!", Toast.LENGTH_LONG).show())
                .addOnFailureListener(e ->
                        Toast.makeText(ClubSearch.this, "Rating error: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    public static int acceptableRating(int rating) {
        if (rating < 0) return 3;
        if (rating > 5) return 2;
        return 1;
    }

    public static int acceptableComment(String comment) {
        if (comment == null) return 2;
        if (comment.equals("")) return 2;
        if (comment.split(" ").length >= 150) return 4;
        if (comment.split(" ").length <= 1) return 3;
        return 1;
    }
}