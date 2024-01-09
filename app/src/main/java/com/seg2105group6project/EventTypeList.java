package com.seg2105group6project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
public class EventTypeList extends ArrayAdapter<EventType> {
    private Activity context;
    List<EventType> eventTypes;

    public EventTypeList(Activity context, List<EventType> eventTypes){
        super(context, R.layout.event_type_list, eventTypes);
        this.context = context;
        this.eventTypes = eventTypes;
    }



    @Override
    public View getView(int position, View counterView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.event_type_list, null, true);

        TextView eventNameTextView =(TextView)listViewItem.findViewById(R.id.eventNameTextView);
        TextView eventDescriptionTextView = (TextView) listViewItem.findViewById(R.id.eventDescriptionTextView);

        EventType eventType = eventTypes.get(position);
        eventNameTextView.setText(eventType.getEventName());
        eventDescriptionTextView.setText(eventType.getEventDescription());


        return listViewItem;
    }
}
