package com.events.hanle.events.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.events.hanle.events.Model.User;
import com.events.hanle.events.R;
import com.events.hanle.events.app.EndPoints;
import com.events.hanle.events.app.MyApplication;
import com.events.hanle.events.interf.BackPressListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ChatLogin extends Fragment {

    private String TAG = ChatLogin.class.getSimpleName();
    private EditText inputName, inputEmail;
    private TextInputLayout inputLayoutName, inputLayoutEmail;
    private Button btnEnter;
    private BackPressListener mListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_login_chat, container, false);


        inputLayoutName = (TextInputLayout) v.findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) v.findViewById(R.id.input_layout_email);
        inputName = (EditText) v.findViewById(R.id.editTextName);
        inputEmail = (EditText) v.findViewById(R.id.editTextEmail);
        btnEnter = (Button) v.findViewById(R.id.buttonEnter);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (BackPressListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement RegistrationListener");
        }
    }
    /**
     * logging in user. Will make http post request with name, email
     * as parameters
     */
    private void login() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        final String name = inputName.getText().toString();
        final String email = inputEmail.getText().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        // user successfully logged in

                        JSONObject userObj = obj.getJSONObject("user");
                        User user = new User(userObj.getString("user_id"),
                                userObj.getString("name"),
                                userObj.getString("phone"));

                        // storing user in shared preferences
                        MyApplication.getInstance().getPrefManager().storeUser(user);

                        // start main activity
                        startActivity(new Intent(getActivity(), ChatLogin.class));
                        getActivity().finish();

                    } else {
                        // login error - simply toast the message
                        Toast.makeText(getActivity(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getActivity(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("phone", email);

                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    // Validating name
    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    // Validating email
    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty()) {
            inputLayoutEmail.setError("Enter Phone No");
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_layout_name:
                    validateName();
                    break;
                case R.id.input_layout_email:
                    validateEmail();
                    break;
            }
        }
    }

}
