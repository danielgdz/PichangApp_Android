package com.appnucleus.loginandregisteruser;

import helper.SQLiteHandler;
import helper.SessionManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Activity_Main extends Activity {

    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        String name = "Default name";
        String email = "Default email";

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    /**
     * Logging out the user.
     * */
    private void logoutUser() {
        session.setLogin(false);
        // Launching the login activity
        Intent intent = new Intent(Activity_Main.this, Activity_Login.class);
        startActivity(intent);
        finish();
    }
}