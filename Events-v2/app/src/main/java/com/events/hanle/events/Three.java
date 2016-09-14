package com.events.hanle.events;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.StringRequest;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Model.EventMessage;
import com.events.hanle.events.Model.FeedItem;
import com.events.hanle.events.adapter.ChatRoomThreadAdapter;
import com.events.hanle.events.adapter.EventMessageAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class Three extends Fragment {

    private static final String TAG = "Three";
    private List<EventMessage> feedsList = new ArrayList<EventMessage>();
    private RecyclerView mRecyclerView;
    private EventMessageAdapter eventMessageAdapter;
    private AlertDialog progressDialog;
    TextView t;
    EventMessage mess;
    TextView tv;
    String event_status, event_title, Username, invitername;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_three, container, false);
        tv = (TextView) v.findViewById(R.id.no_event);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_fragment_three);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mRecyclerView.setHasFixedSize(true);

        event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEvent_status();
        event_title = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEvent_title();
        Username = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getName();
        invitername = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getInvitername();
        int es = Integer.parseInt(event_status);
        feedsList = new ArrayList<>();
        EventMessage eventMessage = new EventMessage();
        if (es == 2) {
            tv.setVisibility(View.GONE);
            tv.setTextColor(Color.parseColor("#FF0000"));
            eventMessage.setTitle("Dear " + Username + ",");
            eventMessage.setDescription("Please be informed, the event " + event_title + " stands cancelled. The chat page is now inaccessible. Regards. " + invitername+".");
            feedsList.add(eventMessage);
            eventMessageAdapter = new EventMessageAdapter(getActivity(), (ArrayList<EventMessage>) feedsList);
            mRecyclerView.setAdapter(eventMessageAdapter);
        } else if (es == 3) {
            tv.setVisibility(View.GONE);
            tv.setTextColor(Color.parseColor("#FF0000"));
            eventMessage.setTitle("Dear " + Username + ",");
            eventMessage.setDescription("Thank you for participating. This event is now concluded. The chat page is now inaccessible. Regards. " + invitername+".");
            feedsList.add(eventMessage);
            eventMessageAdapter = new EventMessageAdapter(getActivity(), (ArrayList<EventMessage>) feedsList);
            mRecyclerView.setAdapter(eventMessageAdapter);

        } else if (es == 1) {
            tv.setVisibility(View.VISIBLE);
            tv.setText("No Message to View");
            mRecyclerView.setVisibility(View.GONE);
            mRecyclerView.setAdapter(eventMessageAdapter);

        }


        return v;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}