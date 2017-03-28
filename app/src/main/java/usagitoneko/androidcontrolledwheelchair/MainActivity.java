package usagitoneko.androidcontrolledwheelchair;

import android.graphics.Color;
import android.provider.SyncStateContract;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
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
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import usagitoneko.androidcontrolledwheelchair.Widget.Croller;


public class MainActivity extends AppCompatActivity {
    View lineIndicator;
    private TickerView kmperH;
    private Croller croller;
    int[] location = new int[2];
    final String USERNAME = "acsa";
    final String PASSWORD = "1234";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lineIndicator = findViewById(R.id.lineIndicator);

        kmperH = (TickerView)findViewById(R.id.kmPerH);
        kmperH.setCharacterList(TickerUtils.getDefaultNumberList());
        kmperH.setText("Km/H");
        kmperH.bringToFront();

        croller = (Croller)findViewById(R.id.croller);
        croller = initCroller(croller);
        croller.setMax(100);

        final TickerView speedIndicator =(TickerView) findViewById(R.id.speedIndicator);
        speedIndicator.setCharacterList(TickerUtils.getDefaultNumberList());
        speedIndicator.bringToFront();

        lineIndicator.bringToFront();

        Joystick joystick = (Joystick) findViewById(R.id.joystick);
        joystick.setJoystickListener(new JoystickListener() {
            @Override
            public void onDown() {
                // ..
                speedIndicator.setText("45");
            }

            @Override
            public void onDrag(float degrees, float offset) {
                // ..
                lineIndicator.getLocationOnScreen(location);
                lineIndicator.setRotation(360-degrees);
                lineIndicator.setLayoutParams(new ConstraintLayout.LayoutParams( Math.round(offset*556),11));
                lineIndicator.setX(701);
                lineIndicator.setY(726);

                croller.setProgress(Math.round(offset*100));
            }

            @Override
            public void onUp() {
                lineIndicator.setLayoutParams(new ConstraintLayout.LayoutParams(1,1 ));
                speedIndicator.setText("23");
                // ..
            }
        });



    }

    private Croller initCroller(Croller croller){
        croller.setIndicatorWidth(10);

        return croller;
    }

    private boolean sendCommand (float degrees, float offset){
        // TODO: 3/28/2017 send degrees and offset as json object
        final String URL = "https://raw.githubusercontent.com/usagitoneko97/AndroidControlledWheelChair/master/UltimateJsonDummy.json";
// Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", "AbCdEfGh123456");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTxtDisplay.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("username", USERNAME);
                params.put("Authorization", PASSWORD);
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsObjRequest);
        return true;
    }

}
