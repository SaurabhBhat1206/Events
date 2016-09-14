package com.events.hanle.events.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.events.hanle.events.Model.EventMessage;
import com.events.hanle.events.R;
import com.events.hanle.events.Three;

import java.util.ArrayList;

/**
 * Created by Hanle on 5/23/2016.
 */
public class EventMessageAdapter extends RecyclerView.Adapter<EventMessageAdapter.EventMessageViewHolder> {

    private Context mContext;
    private ArrayList<EventMessage> eventmessageArrayList;

    public class EventMessageViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;

        public EventMessageViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }

    public EventMessageAdapter(Context context, ArrayList<EventMessage> eventmessagelist) {
        this.mContext = context;
        this.eventmessageArrayList = eventmessagelist;

    }


    @Override
    public EventMessageAdapter.EventMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_fragemnt_row_three, parent, false);
        return new EventMessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventMessageAdapter.EventMessageViewHolder holder, int position) {
        String event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEvent_status();
        int es = Integer.parseInt(event_status);
        if (es == 2 || es == 3) {
            EventMessage eventMessage = eventmessageArrayList.get(position);
            holder.title.setText(eventMessage.getTitle());
            holder.description.setText(eventMessage.getDescription());
            holder.description.setTextColor(Color.RED);
        } else if (es == 1) {
            EventMessage eventMessage = eventmessageArrayList.get(position);
            holder.title.setText(eventMessage.getTitle());
            holder.description.setText(eventMessage.getDescription());
            holder.description.setTextColor(Color.GREEN);

        }


    }

    @Override
    public int getItemCount() {
        return eventmessageArrayList.size();
    }


}
