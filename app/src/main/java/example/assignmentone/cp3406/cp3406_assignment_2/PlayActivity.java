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
import android.widget.Toast;

import java.text.DecimalFormat;

public class PlayActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private TextView questionView, questionNumber, countdownTimer, skipCount;
    private SharedPreferences gameSettings; //SharedPreferences of the whole application.
    private CountDownTimer timer; //CountDownTimer object to keep track of time given for each question.

    private float eventX=0.00f; //Variable eventX used to determine if the phone is being tilted along the x-axis.
    private DecimalFormat df; //Used to ensure that raw x-axis values are limited to two decimals.

    private int currentQuestionCount = 1; //Keeping track of the number of questions.
    private long currentTimeScore = 0; //Variable that take the summation of time left on the CountDownTimer if the user chooses the correct answer.
    private long remainingTime; //Variable that takes the long value of the time left on the CountDownTimer.

    private long gameTime;
    private String gameTopic;
    private int totalQuestionCount;
    private int totalSkippedQuestionCount;
    private int currentSkippedQuestionCount;

    private Toast displayError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); //Sensor Manager
        questionView = findViewById(R.id.questionView); //Textview that displays the topic question
        questionNumber = findViewById(R.id.questionNumber); //Textview that displays the current question number, according to how many have been answered/timed out
        countdownTimer = findViewById(R.id.countdownTimer); //Textview that displays the Countdown Timer
        skipCount = findViewById(R.id.skipCount); //Textview that displays the number of question skips left
        gameSettings = getSharedPreferences("GameSettings", MODE_PRIVATE); //Shared Preferences

        currentQuestionCount = gameSettings.getInt("QuestionCount", currentQuestionCount); //Getting the current question count as the game progresses, initialises at 1
        currentTimeScore = gameSettings.getLong("timeScore", 0); //Getting the combined time score, as per game mechanics
        totalSkippedQuestionCount = gameSettings.getInt("skippedQuestionCount", 2); //Getting the total number of skipped questions allowed, as per user setting before game starts
        currentSkippedQuestionCount = gameSettings.getInt("gameSkippedQuestionCount", 0); //Getting the current number of skipped questions that user had used

        questionView.setText(Long.toString(currentTimeScore)); //testing

        gameTime = gameSettings.getLong("timeLimit", 21000); //Getting the time allowed per question, as per user setting before game starts
        gameTopic = gameSettings.getString("gameSubject", null); //Getting the topic of the quiz, as per user setting before game starts
        totalQuestionCount = gameSettings.getInt("totalQuestionCount", 10); //Getting the total number of questions in the game, as per user setting before game starts.

        timer = new CountDownTimer(gameTime, 1000) {
            public void onTick(long millisUntilFinished) {
                countdownTimer.setText("Seconds remaining: " + millisUntilFinished / 1000);
                remainingTime = millisUntilFinished; //Variable to store time remaining when user selects an answer, to be used to calculate time score
            }
            public void onFinish() { //On timer expiration, restart with new question (or end game)
                currentQuestionCount += 1; //User has failed to answer the question in time, game proceeds with next question
                gameSettings.edit().putInt("QuestionCount", currentQuestionCount).apply(); //Storing current question count to be used to progress the game
                checkGameStatus(currentQuestionCount); //Checking if game is completed
                finish();
            }
        }.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        questionNumber.setText(Integer.toString(currentQuestionCount));
        skipCount.setText("Number of skips left : " + (Integer.toString(totalSkippedQuestionCount - currentSkippedQuestionCount)));
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
    public void onSensorChanged(SensorEvent event) {
        try{
            displayError.cancel(); //Resetting Toast message duration, so that it does not linger on the screen for an excessive amount of time
        }catch(Exception e){}

        eventX=event.values[0]; //Get current x-axis value
        df = new DecimalFormat("#.00");
        String xStringValue = df.format(eventX); //Parsing to String with 2 decimal places
        eventX = Float.parseFloat(xStringValue); //X coords limited to 2 decimal places

        if((eventX<-9.81 || eventX>9.81) && (remainingTime < (gameTime - 1000))){
            //If device is tilted to the left or right, along the x-axis, go to next question.
            //(gameTime - 1000) to allow for one second delay, so as to avoid multiple continued instances of eventX >9.81 or <-9.81 as user returns phone to upright position
            if(currentSkippedQuestionCount < totalSkippedQuestionCount){
                currentSkippedQuestionCount +=1;
                gameSettings.edit().putInt("gameSkippedQuestionCount", currentSkippedQuestionCount).apply(); //Adding one to the number of questions that have been skipped

                //currentQuestionCount += 1;
                //gameSettings.edit().putInt("QuestionCount", currentQuestionCount).apply(); //Adding one to question count

                currentTimeScore += remainingTime; //testing timer equations
                gameSettings.edit().putLong("timeScore", currentTimeScore).apply(); //Test

                sensorManager.unregisterListener(this); //Stop listener
                timer.cancel(); //Stop timer
                checkGameStatus(currentQuestionCount);
            }
            else{
                displayError = Toast.makeText(this, "You cannot skip anymore questions!", Toast.LENGTH_SHORT);
                displayError.show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void checkGameStatus(int currentQuestionCount){
        /*Method to check if game has been completed.
        If Yes, proceeds with EndGameActivity. If No, refreshes the PlayActivity*/

        if(currentQuestionCount > totalQuestionCount){ //If the last question has been answered/timed out
            Intent goToEndscreen = new Intent(this, EndGameActivity.class);
            startActivity(goToEndscreen);
        }
        else{ //Else the game is still ongoing
            Intent refreshQuestion = new Intent(this, PlayActivity.class);
            startActivity(refreshQuestion);
        }
    }
}