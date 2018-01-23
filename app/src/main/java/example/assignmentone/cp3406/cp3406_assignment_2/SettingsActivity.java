package example.assignmentone.cp3406.cp3406_assignment_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {
    RadioGroup questionsRgroup;
    RadioGroup timeRgroup;
    RadioButton tenQuestions;
    RadioButton fifteenQuestions;
    RadioButton twentyQuestions;
    RadioButton learningLimit;
    RadioButton thirtyLimit;
    RadioButton twentyLimit;
    RadioButton tenLimit;
    Button returnToMenu;
    private SharedPreferences gameSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        questionsRgroup = findViewById(R.id.questionsRgroup);
        tenQuestions = findViewById(R.id.tenQuestions);
        fifteenQuestions = findViewById(R.id.fifteenQuestions);
        twentyQuestions = findViewById(R.id.twentyQuestions);

        timeRgroup = findViewById(R.id.timeRgroup);
        learningLimit = findViewById(R.id.learningLimit);
        thirtyLimit = findViewById(R.id.thirtyLimit);
        twentyLimit = findViewById(R.id.twentyLimit);
        tenLimit = findViewById(R.id.tenLimit);

        gameSettings = getSharedPreferences("GameSettings", MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(gameSettings.getInt("numberOfQuestions", 10) == 10){
            questionsRgroup.check(tenQuestions.getId());
        }
        else if(gameSettings.getInt("numberOfQuestions", 10) == 20){
            questionsRgroup.check(fifteenQuestions.getId());
        }
        else{
            questionsRgroup.check(twentyQuestions.getId());
        }


        if(gameSettings.getLong("timeLimit", 21000) == 0){
            timeRgroup.check(learningLimit.getId());
        }
        else if (gameSettings.getLong("timeLimit", 21000) == 11000){
            timeRgroup.check(tenLimit.getId());
        }
        else if (gameSettings.getLong("timeLimit", 21000) == 21000){
            timeRgroup.check(twentyLimit.getId());
        }
        else{
            timeRgroup.check(thirtyLimit.getId());
        }
    }

    public void radioButtonPressed(View view){
        switch (view.getId()){
            case R.id.tenQuestions:
                gameSettings.edit().putInt("numberOfQuestions", 10).apply();
                break;
            case R.id.fifteenQuestions:
                gameSettings.edit().putInt("numberOfQuestions", 15).apply();
                break;
            case R.id.twentyQuestions:
                gameSettings.edit().putInt("numberOfQuestions", 20).apply();
                break;
            case R.id.learningLimit:
                gameSettings.edit().putLong("timeLimit", 0).apply();
                break;
            case R.id.tenLimit:
                gameSettings.edit().putLong("timeLimit", 11000).apply();
                break;
            case R.id.twentyLimit:
                gameSettings.edit().putLong("timeLimit", 21000).apply();
                break;
            case R.id.thirtyLimit:
                gameSettings.edit().putLong("timeLimit", 31000).apply();
                break;
        }
    }

    public void buttonPressed(View view){
        Intent goToPreGame = new Intent (this, PreGameActivity.class);
        startActivity(goToPreGame);
    }
}
