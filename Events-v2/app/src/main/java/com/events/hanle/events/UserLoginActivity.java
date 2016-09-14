package com.events.hanle.events;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crashlytics.android.Crashlytics;
import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Model.User;
import com.events.hanle.events.app.MyApplication;
import com.events.hanle.events.Constants.WebUrl;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class UserLoginActivity extends AppCompatActivity {

    private String TAG = LoginActivity.class.getSimpleName();
    private EditText inputEmail, inputpassword;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    private Button btnEnter;
    ProgressDialog progressDialog;

    Boolean isInternetPresent = false;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MyApplication.getInstance().getPrefManager().getUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_user_login);

        findViewbyId();

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (isInternetPresent) {
                    login();
                } else {
                    Toast.makeText(UserLoginActivity.this, "No internet connection!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void findViewbyId() {
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

        inputEmail = (EditText) findViewById(R.id.input_user_email);
        inputpassword = (EditText) findViewById(R.id.input_user_password);


        btnEnter = (Button) findViewById(R.id.login);

        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputpassword.addTextChangedListener(new MyTextWatcher(inputpassword));

    }

    private void login() {

        final String email = inputEmail.getText().toString();
        final String password = inputpassword.getText().toString();


        if (!validateEmail()) {
            return;
        }

        if (!validatepassword()) {
            return;
        }

        progressDialog = new ProgressDialog(UserLoginActivity.this);
        progressDialog.setMessage("Logging in please wait");
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebUrl.USER_LOGIN_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);
                progressDialog.hide();

                try {
                    JSONObject obj = new JSONObject(response);
                    if (!(obj.length() == 0)) {


                        int success;
                        success = obj.getInt("success");
                        String message;
                        message = obj.getString("message");

                        // check for error flag
                        if (success == 1) {
                            // user successfully logged in
                            //JSONObject userObj = obj.getJSONObject("user");

                            User user = new User(obj.getString("user_id"),
                                    obj.getString("name"), obj.getString("mobile_no"));


                            // storing user in shared preferences
                            MyApplication.getInstance().getPrefManager().storeUser(user);

                            // start main activity
                            Intent i = new Intent(UserLoginActivity.this, ListOfEvent.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                            Toast.makeText(UserLoginActivity.this, message, Toast.LENGTH_SHORT).show();

                        } else {
                            // login error - simply toast the message
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validatepassword() {
        String password = inputpassword.getText().toString().trim();

        if (password.isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputpassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        public MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            switch (view.getId()) {




            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}



