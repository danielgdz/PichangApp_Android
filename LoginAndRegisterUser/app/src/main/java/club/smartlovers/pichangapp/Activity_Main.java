package club.smartlovers.pichangapp;

import helper.SessionManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Activity_Main extends Activity {

    private static final String TAG = Activity_Register.class.getSimpleName();

    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private Button btnMap;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(club.smartlovers.pichangapp.R.layout.activity_main);

        txtName = (TextView) findViewById(club.smartlovers.pichangapp.R.id.name);
        txtEmail = (TextView) findViewById(club.smartlovers.pichangapp.R.id.email);
        btnLogout = (Button) findViewById(club.smartlovers.pichangapp.R.id.btnLogout);
        btnMap = (Button) findViewById(club.smartlovers.pichangapp.R.id.btnMap);

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        //HashMap<String, String> user = getApplicationContext();
        Log.d(TAG, getApplicationContext().toString());

        String name = getIntent().getStringExtra("user_name");
        String email = "¡Reserva tu cancha ahora!";

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);

        // Map button click event
        btnMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToMap();
            }
        });
        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    /**
     * Go to map view
     * */
    private void goToMap() {
        // Launching the map activity
        Intent intent = new Intent(Activity_Main.this, Activity_Map.class);
        startActivity(intent);
        finish();
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