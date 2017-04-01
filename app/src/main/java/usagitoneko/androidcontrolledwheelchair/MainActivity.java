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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;
import usagitoneko.androidcontrolledwheelchair.Widget.Croller;


public class MainActivity extends AppCompatActivity {
    View lineIndicator;
    private TickerView kmperH;
    private Croller croller;
    private FancyButton uTurnButton;
    private FancyButton forceStopButton;
    int[] location = new int[2];
    final String USERNAME = "we success!!!!";
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

        uTurnButton = (FancyButton)findViewById(R.id.uTurnButton);
        forceStopButton = (FancyButton)findViewById(R.id.forceStopButton);

        final TickerView speedIndicator =(TickerView) findViewById(R.id.speedIndicator);
        speedIndicator.setCharacterList(TickerUtils.getDefaultNumberList());
        speedIndicator.bringToFront();

        lineIndicator.bringToFront();

        //sendCommand(1);
        Joystick joystick = (Joystick) findViewById(R.id.joystick);
        joystick.setJoystickListener(new JoystickListener() {
            @Override
            public void onDown() {
                // ..
                speedIndicator.setText("45");
                sendCommand(1);
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
                sendCommand(0);
                // ..
            }
        });



    }

    private Croller initCroller(Croller croller){
        croller.setIndicatorWidth(10);

        return croller;
    }

    private boolean sendCommand (int status){
        // TODO: 3/28/2017 send degrees and offset as json object

        StringBuilder uRLBuilder = new StringBuilder();
        uRLBuilder.append("http://192.168.4.1/led/");
        uRLBuilder.append(status);
        String URL = uRLBuilder.toString();
        String mURL = "http://192.168.4.1/gpio/0";
        //final String URL = "http://192.168.4.1/led/1";
// Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("speed", "AbCdEfGh123456");
        params.put("angle", "360");

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("speed", "success");
            jsonBody.put("angle", "360");
            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, mURL, null,new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            //mTxtDisplay.setText("Response: " + response.toString());
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub

                        }
                    }) {
           /* @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                return params;
            }*/
                /*@Override
                public String getBodyContentType() {
                    return "text/html";
                }
*/
                @Override
                public byte[] getBody()  {
                   /* try {
                        return mRequestBody == null? null : mRequestBody.getBytes("utf-8");
                    }
                    catch (UnsupportedEncodingException e){
                        e.printStackTrace();
                        return null;
                    }*/
                    JSONObject jsonObject = new JSONObject();
                    String body = null;
                    try
                    {
                        jsonObject.put("username", "user123");
                        jsonObject.put("password", "Pass123");

                        body = jsonObject.toString();
                    } catch (JSONException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    try
                    {
                        return body.toString().getBytes("utf-8");
                    } catch (UnsupportedEncodingException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return null;
                }
            };

            StringRequest stringRequest = new StringRequest(Request.Method.POST, mURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // your response

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // error
                }
            }){
                @Override
                public byte[] getBody() throws AuthFailureError {
                    String your_string_json = "you have success!!" ; // put your json
                    return your_string_json.getBytes();
                }
            };
            
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return true;
    }

}
