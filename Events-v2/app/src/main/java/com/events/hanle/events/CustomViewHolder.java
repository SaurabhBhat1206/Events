package com.events.hanle.events;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class CustomViewHolder extends RecyclerView.ViewHolder {


    protected TextView event_heading,tim,date,venue,description,event_cretor,payment,dresscode,dresscodelabel,paymentlabel,phone;


    public CustomViewHolder(View view) {
        super(view);
        this.event_heading = (TextView) itemView.findViewById(R.id.event_heading1);
        this.tim = (TextView) itemView.findViewById(R.id.time1);
        this.date = (TextView) itemView.findViewById(R.id.date1);
        this.venue = (TextView) itemView.findViewById(R.id.venue1);
        this.description = (TextView) itemView.findViewById(R.id.description1);
        this.event_cretor = (TextView) itemView.findViewById(R.id.event_creator_name1);
        this.payment = (TextView) itemView.findViewById(R.id.paymentoptions1);
        this.dresscode = (TextView) itemView.findViewById(R.id.dress_code1);
        this.dresscodelabel = (TextView) itemView.findViewById(R.id.dress_code);
        this.paymentlabel = (TextView) itemView.findViewById(R.id.paymentoptions);
        this.phone = (TextView) itemView.findViewById(R.id.phone1);
    }
}
