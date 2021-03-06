package com.events.hanle.events.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.events.hanle.events.Model.ListEvent;
import com.events.hanle.events.R;
import com.events.hanle.events.UserAttendingStatus;
import com.events.hanle.events.UserTabView;
import com.events.hanle.events.app.MyApplication;


import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Hanle on 6/15/2016.
 */
public class ListEventAdapter extends RecyclerView.Adapter<ListEventAdapter.ListEventViewHolder> {
    private static final String TAG = "ListEventAdapter";
    private ArrayList<ListEvent> feedItemList;
    private Context mContext;
    private static String today;

    public class ListEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title,message;
        ImageView counter;
        ArrayList<ListEvent> listevent = new ArrayList<>();
        Context ctx;
        CardView mCardView;

        public ListEventViewHolder(View itemView, Context ctx, ArrayList<ListEvent> listevent) {
            super(itemView);
            this.listevent = listevent;
            this.ctx = ctx;
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.title);
            mCardView = (CardView) itemView.findViewById(R.id.cardlist_item);
            message = (TextView) itemView.findViewById(R.id.message);
            counter = (ImageView) itemView.findViewById(R.id.counter);


        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ListEvent listEvent = this.listevent.get(position);
            listEvent = new ListEvent(listEvent.getId(), listEvent.getEvent_title(), listEvent.getUser_status(), listEvent.getInvitername(), listEvent.getEvent_status(), null,listEvent.getShare_detail());

            int user_Status = Integer.parseInt(listEvent.getUser_status());
            MyApplication.getInstance().getPrefManager().storeEventId(listEvent);
            if (user_Status == 1) {
                //callalertDialog();
                Intent i = new Intent(mContext, UserAttendingStatus.class);
                i.putExtra("event_title", listEvent.getEvent_title());
                i.putExtra("share_detail", listEvent.getShare_detail());

                this.ctx.startActivity(i);
            } else if (user_Status == 3) {
                Toast.makeText(this.ctx, "You said you are not attending this Event!!", Toast.LENGTH_LONG).show();
            } else {
                MyApplication.getInstance().getPrefManager().storeEventId(listEvent);
                Log.e(TAG, "Event details is stored in shared preferences. " + listEvent.getId() + ", " + listEvent.getEvent_title() + "," + "," + listEvent.getInvitername() + "," + listEvent.getEvent_status() + "," + listEvent.getUser_status());

                Intent i = new Intent(this.ctx, UserTabView.class);
                i.putExtra("event_title", listEvent.getEvent_title());
                i.putExtra("share_detail", listEvent.getShare_detail());
                this.ctx.startActivity(i);
            }
        }
    }


    public ListEventAdapter(Context context, ArrayList<ListEvent> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ListEventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_event, null);
        ListEventViewHolder viewHolder = new ListEventViewHolder(view, mContext, (ArrayList<ListEvent>) feedItemList);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ListEventViewHolder customViewHolder, int i) {
        ListEvent feedItem = feedItemList.get(i);
        int status = Integer.parseInt(feedItem.getUser_status());
        customViewHolder.message.setText(feedItem.getLastMessage());
        String s = feedItem.getLastMessage();
        int co = feedItem.getUnreadCount();

        if (feedItem.getUnreadCount() > 0) {
            customViewHolder.counter.setVisibility(View.VISIBLE);
            System.out.println("Totoal count is:"+co);

        } else {
            customViewHolder.counter.setVisibility(View.GONE);
        }


        if (status == 1) {
            customViewHolder.mCardView.setCardBackgroundColor(Color.parseColor("#98cbe5")); // will change the background color of the card view to blue
            customViewHolder.title.setTextColor(Color.parseColor("#000000"));
        } else if (status == 2) {
            customViewHolder.mCardView.setCardBackgroundColor(Color.parseColor("#96d796")); // will change the background color of the card view to green
            customViewHolder.title.setTextColor(Color.parseColor("#000000"));

        } else if (status == 3) {
            customViewHolder.mCardView.setCardBackgroundColor(Color.parseColor("#f42d4e")); // will change the background color of the card view to red

        }

        customViewHolder.title.setText(Html.fromHtml(feedItem.getEvent_title() + " : " + feedItem.getInvitername()));

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }
}
