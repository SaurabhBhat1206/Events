package com.events.hanle.events;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Model.User;
import com.events.hanle.events.interf.BackPressListener;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.mukesh.countrypicker.models.Country;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class Login extends Fragment {

    private static final String TAG = "Login";
    private EditText inputmobile, countrycode;
    private TextInputLayout inputLayoutMobile, inputcountrycode;
    private Button btnEnter;
    private BackPressListener mListener;
    private AlertDialog progressDialog;
    private Context _context;
    private TextView termscondition;
    private CountryPicker countryPicker;
    private Country country=null;
    private String country_code=null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);
        inputLayoutMobile = (TextInputLayout) v.findViewById(R.id.input_mobile);
        inputcountrycode = (TextInputLayout) v.findViewById(R.id.input_countrycode);

        inputmobile = (EditText) v.findViewById(R.id.input_mobile_ed);
        countrycode = (EditText) v.findViewById(R.id.country_code);
        countrycode.setKeyListener(null);

        inputmobile.addTextChangedListener(new MyTextWatcher(inputmobile));
        countrycode.addTextChangedListener(new MyTextWatcher(countrycode));

        termscondition = (TextView) v.findViewById(R.id.textView8);
        termscondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame_login, new TermsandCondition(), "NewFragmentTag");
                ft.addToBackStack(null);
                ft.commit();
            }
        });


         countryPicker = CountryPicker.newInstance("Select Country");

        countrycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countryPicker.show(getActivity().getSupportFragmentManager(), "COUNTRY_PICKER");
                countryPicker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                        countrycode.setText(dialCode + " " + name);
                        country_code = dialCode;
                        countryPicker.dismiss();
                    }
                });
            }
        });

        btnEnter = (Button) v.findViewById(R.id.login);
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConnectionDetector.isInternetAvailable(getActivity())) {
                    callOtp();
                } else {
                    Toast.makeText(getActivity(), "No Internet!!!", Toast.LENGTH_SHORT).show();
                }


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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    private void navigateToRegistration() {
        mListener.callToPreviousPage();

    }

    private void callOtp() {
        final String mobileNo = inputmobile.getText().toString();
       // Toast.makeText(getActivity(), "cc"+country_code, Toast.LENGTH_SHORT).show();

        //Toast.makeText(getActivity(), "country code:"+country_code, Toast.LENGTH_SHORT).show();

        if (!validateMobile()) {
            return;
        }
        if (!validatecountrycode()) {
            return;
        }
        progressDialog = new SpotsDialog(getActivity(), R.style.Custom);
        progressDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebUrl.USER_LOGIN_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                progressDialog.hide();

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.length() != 0) {
                        String message;
                        int success;
                        // user successfully logged in
                        success = obj.getInt("success");
                        message = obj.getString("message");

                        if (success == 1) {
                            User user = new User(obj.getString("user_id"), obj.getString("name"), obj.getString("phone"),obj.getString("countrycode"));
                            com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().storetempuserID(user);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            navigateToRegistration();
                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // login error - simply toast the message
                        Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getActivity(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", mobileNo);
                params.put("countrycode", country_code.substring(1));
                Log.e(TAG, "params: " + params.toString());

                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

//        strReq.setRetryPolicy(new DefaultRetryPolicy(
//                WebUrl.MY_SOCKET_TIMEOUT_MS,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to request queue
        com.events.hanle.events.app.MyApplication.getInstance().addToRequestQueue(strReq);

    }


    private boolean validateMobile() {
        String mobile = inputmobile.getText().toString().trim();

        if (mobile.isEmpty()) {
            inputLayoutMobile.setError(getString(R.string.err_msg_mobile));
            return false;
        } else {
            inputLayoutMobile.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatecountrycode() {
        String coucode = countrycode.getText().toString().trim();


        if (coucode.isEmpty()) {
            inputcountrycode.setError(getString(R.string.err_msg_country_code));
            return false;
        } else {
            inputcountrycode.setErrorEnabled(false);
        }
        return true;

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
                case R.id.input_mobile_ed:
                    validateMobile();
                    break;
                case R.id.country_code:
                    validatecountrycode();
                    break;

            }

        }
    }

}
