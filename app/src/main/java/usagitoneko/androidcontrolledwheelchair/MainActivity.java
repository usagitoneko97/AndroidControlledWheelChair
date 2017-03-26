package usagitoneko.androidcontrolledwheelchair;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;

public class MainActivity extends AppCompatActivity {
    View lineIndicator;
    int[] location = new int[2];
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
    }
}
