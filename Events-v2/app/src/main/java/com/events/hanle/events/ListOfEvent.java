package com.events.hanle.events;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Model.ListEvent;
import com.events.hanle.events.adapter.ListEventAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ListOfEvent extends AppCompatActivity {

    private static final String TAG = "RecyclerViewExample";
    private ArrayList<ListEvent> feedsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ListEventAdapter adapter;
    Context ctx;
    ProgressDialog progressDialog;
    TextView noEvent, listEventID;
    String user_id;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_event);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_for_listevent);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(t);
        assert t != null;
        t.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        noEvent = (TextView) findViewById(R.id.no_events_to_show);
        listEventID = (TextView) findViewById(R.id.list_event_id);
        user_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUserId().getId();

        if (savedInstanceState == null) {

            new AsyncHttpTask().execute(WebUrl.LIST_EVENT_URL + user_id);
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Refreshing data on server
                new AsyncHttpTask().execute(WebUrl.LIST_EVENT_URL + user_id);
            }
        });

    }



    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ListOfEvent.this);
            progressDialog.setMessage("Loading the List of events please wait...");
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    System.out.println("response:" + response);
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fe
                    // tch data!";
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI


            if (result == 1) {
                adapter = new ListEventAdapter(ListOfEvent.this, feedsList);
                mRecyclerView.setAdapter(adapter);
                progressDialog.hide();
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            } else {
                progressDialog.hide();
                Toast.makeText(ListOfEvent.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {
        try {
            String user_attending_status;

            JSONObject response = new JSONObject(result);
            System.out.println("response:" + response);
            JSONArray jsonArray = response.optJSONArray("server_response");
            feedsList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                    ListEvent item = new ListEvent();
                    item.setEvent_title(jsonObject.getString("event_title"));
                    item.setId(jsonObject.getString("id"));
                    item.setUser_status(jsonObject.getString("user_attending_status"));
                    item.setInvitername(jsonObject.getString("inviter_name"));
                    feedsList.add(item);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void RunSeparteThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.hide();
                noEvent.setVisibility(View.VISIBLE);
                noEvent.setText("You have no events to view!");
                listEventID.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);

    }

}
