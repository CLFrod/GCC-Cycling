package com.seg2105group6project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ClubList extends ArrayAdapter<OrganizerAccount> {
    private Activity context;
    List<OrganizerAccount> clubs;

    public ClubList(Activity context, List<OrganizerAccount> clubs){
        super(context, R.layout.club_list, clubs);
        this.context = context;
        this.clubs = clubs;
    }



    @Override
    public View getView(int position, View counterView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.club_list, null, true);

        TextView clubNameTextView =(TextView)listViewItem.findViewById(R.id.clubNameTextView);
        TextView instaTextView = (TextView) listViewItem.findViewById(R.id.instaTextView);
        TextView emailTextView = (TextView) listViewItem.findViewById(R.id.emailTextView);
        TextView phoneNumTextView = (TextView) listViewItem.findViewById(R.id.phoneNumTextView);

        OrganizerAccount club = clubs.get(position);
        clubNameTextView.setText(club.getUserName());
        instaTextView.setText(club.getInstagramLink());
        emailTextView.setText(club.getEmail());
        phoneNumTextView.setText(club.getPhoneNumber());

        return listViewItem;
    }
}
