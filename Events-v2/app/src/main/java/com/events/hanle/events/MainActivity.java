package com.events.hanle.events;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import org.json.JSONObject;


public class MainActivity extends AppCompatActivity  {

    Button next;
    RadioGroup rg;
    RadioButton attending, notattending;
    private AppCompatButton button_confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUserId() != null) {
            startActivity(new Intent(this, ListOfEvent.class));
            finish();
        }

        setContentView(R.layout.activity_landing);


        findViewsById();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                radiobuttonAction();
            }
        });

    }

    private void radiobuttonAction() {
        if (rg.getCheckedRadioButtonId() != -1) {
            int id = rg.getCheckedRadioButtonId();
            View radioButton = rg.findViewById(id);
            int radioId = rg.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) rg.getChildAt(radioId);
            String selection_strng = (String) btn.getText();
            if (selection_strng.equalsIgnoreCase("Not Attending")) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                Toast.makeText(MainActivity.this, "Thank you!, You can attend the next event", Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                //callalertDialog();
            }
        }
    }

    public void callalertDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_confirm, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);


        // create an alert dialog
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        // setup a dialog window

        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent i = new Intent(getApplicationContext(), UserTabView.class);
                startActivity(i);
            }
        });


    }

    public void findViewsById() {

        next = (Button) findViewById(R.id.next);
        rg = (RadioGroup) findViewById(R.id.rgroup);
        attending = (RadioButton) findViewById(R.id.attending);
        notattending = (RadioButton) findViewById(R.id.not_attending);


    }


}
