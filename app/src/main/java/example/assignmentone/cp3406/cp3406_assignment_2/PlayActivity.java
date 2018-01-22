package example.assignmentone.cp3406.cp3406_assignment_2;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

public class PlayActivity extends AppCompatActivity implements SensorEventListener {
    private float eventX=0.00f;
    private float xPos=0.00f;
    private float maxHeight=0.00f,minHeight=0.00f,midHeight=0.00f;
    private float maxWidth=0.00f,minWidth=0.00f,midWidth=0.00f;
    private int displayHeight,displayWidth;

    private DisplayInvisibleBubble displayInvisibleBubble;
    private SensorManager sensorManager;
    private LinearLayout invisiblePlayBg;
    private TextView questionView;
    private DecimalFormat df = new DecimalFormat("#.00");

    private int questionCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        displayInvisibleBubble = new DisplayInvisibleBubble(this);

        invisiblePlayBg = findViewById(R.id.invisBubbleBg);
        invisiblePlayBg.addView(displayInvisibleBubble);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        questionView = findViewById(R.id.questionView);

    }

    @Override
    public void onWindowFocusChanged(boolean focus){
        displayHeight= displayInvisibleBubble.getHeight();
        displayWidth= displayInvisibleBubble.getWidth();

        //Setting height boundaries
        minHeight= (float)0.15*displayHeight;
        maxHeight=(float)0.85*displayHeight;
        midHeight=(float)0.5*displayHeight;

        //Setting width boundaries
        minWidth= (float)0.15*displayWidth;
        maxWidth=(float)0.85*displayWidth;
        midWidth=(float)0.5*displayWidth;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        eventX=event.values[0];

        df = new DecimalFormat("#.00");
        String xStringValue = df.format(eventX); //Parsing to String with 2 decimal places

        eventX = Float.parseFloat(xStringValue); //X coords limited to 2 decimal places

        xPos = getXPosition(eventX, maxWidth, minWidth, midWidth);

        displayInvisibleBubble.getBubble().setX((int)xPos); //Draw Bubble

        if(eventX<=-9.81 || eventX>9.81){
            questionView.setText("Changed");
            questionCount += 1;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    public static float getXPosition(float eventX, float maxWidth, float minWidth, float midWidth){ //get new X coords
        float xPos = 0.00f;
        if(eventX<=-9.81){
            xPos=maxWidth;
        }else if(eventX>9.81){
            xPos=minWidth;
        }else if(eventX<0){
            xPos=midWidth+((0-eventX)/(float)9.81*(midWidth-minWidth));
        }else if(eventX>0){
            xPos=midWidth+((0-eventX)/(float)9.81*(maxWidth-midWidth));
        }else if(eventX==0){
            xPos=midWidth;
        }
        return xPos;
    }
    /*if (gyroscopeSensor == null) {
            Toast.makeText(this, "This device has no gyroscope!", Toast.LENGTH_SHORT).show();
        }

    gyroscopeEventListener = new SensorEventListener() { //Acceleration force along the z axis (including gravity).
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.values[2] > 1) {
                skipQuestion();
                //getWindow().getDecorView().setBackgroundColor(Color.BLUE);
            } else if(sensorEvent.values[2] < -1){
                skipQuestion();
                //getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
            }
            else{
                questionView.setText("Center");
                //getWindow().getDecorView().setBackgroundColor(Color.WHITE);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };*/
}