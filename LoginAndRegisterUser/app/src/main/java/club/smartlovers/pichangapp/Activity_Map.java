package club.smartlovers.pichangapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import volley.AppController;
import volley.Config_URL;

public class Activity_Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ProgressDialog pDialog;
    private Button btnMain;
    private JSONArray mapObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(club.smartlovers.pichangapp.R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(club.smartlovers.pichangapp.R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, club.smartlovers.pichangapp.R.raw.night_style));

        LatLng jesusMaria = new LatLng(-12.0712084, -77.0445137);
        // Tag used to cancel the request

        StringRequest strReq = new StringRequest(Method.GET, Config_URL.URL_SUBSIDIARIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Check for status node in json
                    mapObject = new JSONArray(response);
                    LatLng[] mapPoints = new LatLng[mapObject.length()];
                    for (int i = 0; i < mapObject.length(); i++) {
                        JSONObject jsonObject =  mapObject.optJSONObject(i);
                        mapPoints[i] = new LatLng(jsonObject.optDouble("latitude"), jsonObject.optDouble("longitude"));
                        mMap.addMarker(new MarkerOptions().position(mapPoints[i])
                                .title(jsonObject.optString("name"))
                                .snippet(jsonObject.optString("contact_name") + " - " + jsonObject.optString("phone"))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.smal_pin_pichangapp)));
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("URL_SUBSIDIARIES", "No se pudo cargar las canchas");
                Toast.makeText(getApplicationContext(), "No se pudo cargar las canchas", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "maps");
        // Initial Position
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jesusMaria, 14.0f));
    }

    @Override
    public void onBackPressed() {
        // Launch main activity
        Intent intent = new Intent(Activity_Map.this, Activity_Main.class);
        startActivity(intent);
        finish();
    }

    /**
     * function to verify maps
     * */
    private void getMapMarkers() {
        // Tag used to cancel the request
        pDialog.setMessage("Cargando canchas...");
        showDialog();
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
