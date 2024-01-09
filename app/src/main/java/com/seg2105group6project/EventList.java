package com.seg2105group6project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
public class EventList extends ArrayAdapter<Event> {
    private Activity context;
    List<Event> events;

    public EventList(Activity context, List<Event> events){
        super(context, R.layout.event_list, events);
        this.context = context;
        this.events = events;
    }



    @Override
    public View getView(int position, View counterView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.event_list, null, true);

        TextView eventNameTextView =(TextView)listViewItem.findViewById(R.id.eventNameTextView);
        TextView eventDescriptionTextView = (TextView) listViewItem.findViewById(R.id.eventDescriptionTextView);
        TextView regionTextView = (TextView) listViewItem.findViewById(R.id.regionTextView);
        TextView organizerTextView = (TextView) listViewItem.findViewById(R.id.organizerTextView);

        Event event = events.get(position);
        eventNameTextView.setText(event.getEventName());
        eventDescriptionTextView.setText(event.getEventType().getEventDescription());
        regionTextView.setText(event.getRegion());
        organizerTextView.setText(event.getMainOrganizer().getUserName());

        return listViewItem;
    }
}
