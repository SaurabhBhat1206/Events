package com.events.hanle.events;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.WindowManager;

import com.events.hanle.events.app.MyApplication;
import com.events.hanle.events.interf.BackPressListener;


public class LoginActivity extends AppCompatActivity implements BackPressListener {

    private String TAG = LoginActivity.class.getSimpleName();

    private Toolbar toolbar;
    private static boolean activityStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (MyApplication.getInstance().getPrefManager().getUserId() != null && MyApplication.getInstance().getPrefManager().getUserId().getCountrycode() != null) {
            startActivity(new Intent(this, ListOfEvent1.class));
            finish();
        }
        setContentView(R.layout.activity_login1);

        Login l = new Login();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame_login, l)
                .commit();

        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void callToPreviousPage() {
        OtpFragment otpfragment = new OtpFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame_login, otpfragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void Gotonextactivity() {
        Intent i = new Intent(LoginActivity.this, UserTabView.class);
        startActivity(i);
        finish();
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



