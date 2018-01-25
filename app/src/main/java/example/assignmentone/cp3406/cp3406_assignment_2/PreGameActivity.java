/*PreGameActivity was created to display the specifications of the game before it is run. User has the choice to alter the current setttings*/
package example.assignmentone.cp3406.cp3406_assignment_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PreGameActivity extends AppCompatActivity {
    private SharedPreferences gameSettings;
    private TextView timeInput;
    private TextView qNumInput;
    private TextView topicInput;
    private TextView allowedSkip;
    private int timeInSeconds;
    private String gameTopic;
    private ArrayList<String> listOfQuestions;
    private Set<String> setListOfQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_game);
        timeInput = findViewById(R.id.timeInput);
        qNumInput = findViewById(R.id.qNumInput);
        topicInput = findViewById(R.id.topicInput);
        allowedSkip = findViewById(R.id.allowedSkip);
        listOfQuestions = new ArrayList<>();
        gameSettings = getSharedPreferences("GameSettings", MODE_PRIVATE);

        //Check current selected topic
        switch(gameSettings.getString("gameSubject", "Biology")){
            case "Biology":
                gameTopic = "bio";
                break;
            case "Chemistry":
                gameTopic = "chem";
                break;
            case "Physics":
                gameTopic = "phys";
                break;
        }

        //Load all available questions headers from the arrays.xml, according to the selected topic
        for(int i=1; i<17; i++){
            listOfQuestions.add(gameTopic + "question" + i);
        }
        Collections.shuffle(listOfQuestions);
        //Add question headers into a set, and adding to preferences
        setListOfQuestions = new HashSet<String>();
        setListOfQuestions.addAll(listOfQuestions);
        gameSettings.edit().putStringSet("listOfGameQuestions", setListOfQuestions).apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        timeInSeconds = (int)(((gameSettings.getLong("timeLimit", 21000))-1000)/1000);
        timeInput.setText(timeInSeconds + " Seconds");
        qNumInput.setText(Integer.toString(gameSettings.getInt("numberOfQuestions", 10)));
        topicInput.setText(gameSettings.getString("gameSubject", null));
        allowedSkip.setText(Integer.toString(gameSettings.getInt("skippedQuestionCount", 2)));

        gameSettings.edit().putInt("QuestionCount", 1).apply(); //Resetting Stored preferences
        gameSettings.edit().putLong("timeScore", 0).apply();
        gameSettings.edit().putInt("gameSkippedQuestionCount", 0).apply();
        gameSettings.edit().putInt("numCorrectAns", 0).apply();
    }

    public void buttonPressed(View view){
        Intent goToPlay = new Intent(this, PlayActivity.class);
        Intent goToMenu = new Intent(this, MainActivity.class);
        Intent goToSettings = new Intent(this, SettingsActivity.class);

        switch (view.getId()){
            case R.id.playButton: //Start the game
                startActivity(goToPlay);
                break;
            case R.id.menuButton: //Return to the main menu
                startActivity(goToMenu);
                break;
            case R.id.settingsButton: //Go to the game settings
                startActivity(goToSettings);
                break;
        }
    }
}
