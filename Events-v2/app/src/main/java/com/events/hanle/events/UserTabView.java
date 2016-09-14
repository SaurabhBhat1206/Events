package com.events.hanle.events;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.events.hanle.events.Chat.ChatRoomActivity;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UserTabView extends AppCompatActivity {

    private static TabLayout tabLayout;
    private static ViewPager viewPager;
    public static FragmentManager fragmentManager;
    int event_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_tab_view);
        String event_title = getIntent().getStringExtra("event_title");
        // Toast.makeText(UserTabView.this, event_title, Toast.LENGTH_SHORT).show();
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        assert t != null;
        t.setTitleTextColor(Color.WHITE);
        setSupportActionBar(t);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(event_title);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        assert viewPager != null;
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.not_attending:
                startActivity(new Intent(getApplicationContext(), UserChangeOfDescision.class));
                return true;

            case R.id.create_event:
                calldialogue();
                return true;

            case R.id.feedback:
                sendFeedbackalert();
                return true;

            case R.id.about_us:
                aboutus();
                return true;

            case R.id.mute:
                mute();
                return true;

            case R.id.list_invitee:
                String sharedetails = getIntent().getStringExtra("share_detail");
                //Toast.makeText(UserTabView.this, sharedetails, Toast.LENGTH_SHORT).show();
                int s = Integer.parseInt(sharedetails);
                if (s == 1) {
                    showDialog();

                } else {
                    Toast.makeText(UserTabView.this, "Organiser has not enabled this feature", Toast.LENGTH_SHORT).show();


                }
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void mute() {

        android.app.FragmentManager man = getFragmentManager();
        MuteDialog muteDialog = new MuteDialog();
        muteDialog.show(man,"dialog");

    }

    private void aboutus() {
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Hanle Solutions")
                .setContentText("Version 1.0")
                .setCustomImage(R.drawable.images)
                .show();

    }


    private void showDialog() {

        android.app.FragmentManager manager = getFragmentManager();

        AttendingDialogFragment dialog = new AttendingDialogFragment();
        dialog.show(manager, "dialog");

    }

    private void calldialogue() {

        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Hanle Solutions")
                .setContentText("To create Event visit www.nooitheinviteapp.com")
                .setCustomImage(R.drawable.images)
                .show();

    }

    private void sendFeedbackalert() {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.activity_otp, null);
//        dialogBuilder.setView(dialogView);
//
//        final EditText edt = (EditText) dialogView.findViewById(R.id.feed_back);
//
//        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //do something with edt.getText().toString();
//            }
//        });
//        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //pass
//            }
//        });
//        AlertDialog b = dialogBuilder.create();
//        b.show();
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Hanle Solutions")
                .setContentText("Please share your Feedback to info@hanlesolutions.com")
                .setCustomImage(R.drawable.images)
                .show();

    }

    private void sendFeedback() {

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "Event");
        adapter.addFragment(new TwoFragment(), "Venue");
        adapter.addFragment(new Three(), "Mailbag");
        adapter.addFragment(new ChatRoomActivity(), "Chat");
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
