package example.assignmentone.cp3406.cp3406_assignment_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.text.DecimalFormat;

public class PlayActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private TextView questionView, questionNumber, countdownTimer;
    private SharedPreferences gameSettings; //SharedPreferences of the whole application.
    private CountDownTimer timer; //CountDownTimer object to keep track of time given for each question.

    private float eventX=0.00f; //Variable eventX used to determine if the phone is being tilted along the x-axis.
    private DecimalFormat df; //Used to ensure that raw x-axis values are limited to two decimals.

    private int questionCount = 1; //Keeping track of the number of questions.
    private long currentTimeScore = 0; //Variable that take the summation of time left on the CountDownTimer if the user chooses the correct answer.
    private long remainingTime; //Variable that takes the long value of the time left on the CountDownTimer.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        questionView = findViewById(R.id.questionView);
        questionNumber = findViewById(R.id.questionNumber);
        countdownTimer = findViewById(R.id.countdownTimer);
        gameSettings = getSharedPreferences("GameSettings", MODE_PRIVATE);

        questionCount = gameSettings.getInt("QuestionCount", questionCount);
        currentTimeScore = gameSettings.getLong("timeScore", 0);

        questionView.setText(Long.toString(currentTimeScore)); //testing

        timer = new CountDownTimer(15000, 1000) {
            public void onTick(long millisUntilFinished) {
                countdownTimer.setText("Seconds remaining: " + millisUntilFinished / 1000);
                remainingTime = millisUntilFinished;
            }

            public void onFinish() {
                questionCount += 1;
                gameSettings.edit().putInt("QuestionCount", questionCount).apply();
                Intent nextQuestion = new Intent(PlayActivity.this, PlayActivity.class);
                finish();
                startActivity(nextQuestion);
            }
        }.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        questionNumber.setText(Integer.toString(questionCount));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this); //Stop listener on pause.
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this); //Stop listener on stop.
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Intent refreshQuestion = new Intent(this, PlayActivity.class);
        eventX=event.values[0]; //Get current x-axis value
        df = new DecimalFormat("#.00");
        String xStringValue = df.format(eventX); //Parsing to String with 2 decimal places
        eventX = Float.parseFloat(xStringValue); //X coords limited to 2 decimal places

        if(eventX<-9.81 || eventX>9.81){ //If device is tilted to the left or right, along the x-axis, go to next question
            questionCount += 1;
            gameSettings.edit().putInt("QuestionCount", questionCount).apply(); //Adding one to question count

            currentTimeScore += remainingTime;
            gameSettings.edit().putLong("timeScore", currentTimeScore).apply(); //Test

            sensorManager.unregisterListener(this); //Stop listener
            timer.cancel(); //Stop timer
            startActivity(refreshQuestion); //Restart Activity with next question
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}