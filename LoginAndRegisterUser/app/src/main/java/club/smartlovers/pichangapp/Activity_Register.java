package club.smartlovers.pichangapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import helper.SessionManager;
import volley.AppController;
import volley.Config_URL;

public class Activity_Register extends Activity {
    private static final String TAG = Activity_Register.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputLastname;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(club.smartlovers.pichangapp.R.layout.activity_register);

        inputFullName = (EditText) findViewById(club.smartlovers.pichangapp.R.id.name);
        inputEmail = (EditText) findViewById(club.smartlovers.pichangapp.R.id.email);
        inputEmail = (EditText) findViewById(club.smartlovers.pichangapp.R.id.lastname);
        inputPassword = (EditText) findViewById(club.smartlovers.pichangapp.R.id.password);
        btnRegister = (Button) findViewById(club.smartlovers.pichangapp.R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(club.smartlovers.pichangapp.R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Activity_Register.this, Activity_Main.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            String name = inputFullName.getText().toString();
            String email = inputEmail.getText().toString();
            String lastname = "lastname"; //inputLastname.getText().toString();
            String password = inputPassword.getText().toString();

            if (!name.isEmpty() && !lastname.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                registerUser(name, email, password, lastname);
            } else {
                Toast.makeText(getApplicationContext(), "¡Por favor!, complete todos los campos.", Toast.LENGTH_LONG).show();
            }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_Login.class);
                startActivity(intent);
                finish();
                overridePendingTransition(club.smartlovers.pichangapp.R.anim.push_left_in, club.smartlovers.pichangapp.R.anim.push_left_out);
            }
        });

    }

    private void registerUser(final String name, final String email, final String password, final String lastname) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        pDialog.setMessage("Registrando ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST, Config_URL.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully register
                        String uid = jObj.getString("id");
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String lastname = user.getString("lastname");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");
                        // Launch login activity
                        Intent intent = new Intent(Activity_Register.this, Activity_Login.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error occurred in registration.
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "register");
                params.put("name", name);
                params.put("lastname", lastname);
                params.put("email", email);
                params.put("password", password);
                params.put("app_id", "1");
                params.put("role_id", "1");
                params.put("flag_admin", "0");
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
}