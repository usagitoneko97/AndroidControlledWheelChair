package usagitoneko.androidcontrolledwheelchair;

import android.provider.SyncStateContract;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.provider.Telephony.Carriers.PASSWORD;

public class MainActivity extends AppCompatActivity {
    View lineIndicator;
    int[] location = new int[2];
    final String USERNAME = "hgx1997";
    final String PASSWORD = "hxg138854";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lineIndicator = findViewById(R.id.lineIndicator);
        lineIndicator.bringToFront();

        Joystick joystick = (Joystick) findViewById(R.id.joystick);
        joystick.setJoystickListener(new JoystickListener() {
            @Override
            public void onDown() {
                // ..
            }

            @Override
            public void onDrag(float degrees, float offset) {
                // ..
                lineIndicator.getLocationOnScreen(location);
                lineIndicator.setRotation(360-degrees);
                lineIndicator.setLayoutParams(new ConstraintLayout.LayoutParams( Math.round(offset*556),11));
                lineIndicator.setX(701);
                lineIndicator.setY(726);

            }

            @Override
            public void onUp() {
                lineIndicator.setLayoutParams(new ConstraintLayout.LayoutParams(1,1 ));

                // ..
            }
        });


        final String URL = "https://raw.githubusercontent.com/usagitoneko97/AndroidControlledWheelChair/master/UltimateJsonDummy.json";
// Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", "AbCdEfGh123456");

        StringRequest req = new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      Log.d("Response", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", "ERROR");
                Toast.makeText(MainActivity.this, "ERROR!!!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                //headers here
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("username", USERNAME);
                params.put("Authorization", PASSWORD);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }


}
