package com.events.hanle.events;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Model.FeedItem;
import com.events.hanle.events.app.EndPoints;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class OneFragment extends Fragment {

    private static final String TAG = "OneFragment";
    private List<FeedItem> feedsList = new ArrayList<FeedItem>();
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;
    Context ctx;
    private AlertDialog progressDialog;
    View mainview;
    TextView t;
    String event_id, mobileno, countrycode;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainview = inflater.inflate(R.layout.fragment_one, container, false);
        mRecyclerView = (RecyclerView) mainview.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getId();
        mobileno = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getMobile();
        countrycode = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getCountrycode();
        //Toast.makeText(getActivity(), "country code"+countrycode, Toast.LENGTH_SHORT).show();
        t = (TextView) mainview.findViewById(R.id.adc);

        if (savedInstanceState == null) {

            if (ConnectionDetector.isInternetAvailable(getActivity())) {
                String endpoint = WebUrl.USER_EVENT_URL.replace("MOBILE_NO", mobileno);
                String endpoint1 = endpoint.replace("EVENT_ID", event_id);
                System.out.println("One fragment:" + endpoint1);
                new AsyncHttpTask().execute(endpoint1 + countrycode);
            } else {
                Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
            }


        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) mainview.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Refreshing data on server
                if (ConnectionDetector.isInternetAvailable(getActivity())) {
                    String endpoint = WebUrl.USER_EVENT_URL.replace("MOBILE_NO", mobileno);
                    String endpoint1 = endpoint.replace("EVENT_ID", event_id);
                    System.out.println("One fragment:" + endpoint1);
                    new AsyncHttpTask().execute(endpoint1 + countrycode);

                } else {
                    Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return mainview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Toast.makeText(getActivity(), String.valueOf(event_id), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            progressDialog = new SpotsDialog(getActivity(), R.style.Custom);
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
                    System.out.println("TAG:" + response);
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
                adapter = new MyRecyclerAdapter(OneFragment.this, feedsList);
                mRecyclerView.setAdapter(adapter);
                progressDialog.hide();
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }


            } else {
                progressDialog.hide();
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            System.out.println("TAG:" + response);
            String user_status;
            user_status = response.getString("user_status");

            System.out.println("TAG:" + user_status);
            int status = Integer.parseInt(user_status);
            if (status == 3 || status == 1) {
                RunSeparteThread(status);

            } else {
                feedsList = new ArrayList<>();
                FeedItem item = new FeedItem();
                item.setEvent_creator_name(response.optString("event_creater_username"));
                item.setOrgnaserphone(response.optString("event_creator_phone"));
                item.setEventdesc(response.optString("description"));
                item.setAddress(response.optString("event_address"));
                item.setDate(response.optString("event_date"));
                item.setTime(response.optString("event_time"));
                item.setEventname(response.optString("event_type"));
                item.setPayment(response.optString("payment"));
                item.setDresscode(response.optString("dresscode"));
                item.setTimezone(response.optString("timezone"));
                feedsList.add(item);
                FeedItem feedItem = new FeedItem(response.getString("event_status"));
                com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().storeEventInfoID(feedItem);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void RunSeparteThread(final int status) {


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.hide();
                t.setVisibility(View.VISIBLE);
                if (status == 3) {
                    t.setText("You have cancelled this event!!!");
                    mRecyclerView.setVisibility(View.GONE);
                } else if (status == 1) {
                    t.setText("You are invited please confirm!");
                    mRecyclerView.setVisibility(View.GONE);
                }

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
