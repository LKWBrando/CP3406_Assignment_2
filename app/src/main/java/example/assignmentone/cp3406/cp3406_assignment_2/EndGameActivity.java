//EndGameActivity created to display the end progress of the quiz, allowing the option for the user to store the result in the database
package example.assignmentone.cp3406.cp3406_assignment_2;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EndGameActivity extends AppCompatActivity {

    private TextView displayCorrectQuestions;
    private TextView displaySkipsUsed;
    private TextView displayFinalScore;
    private EditText inputName;
    private SharedPreferences gameSettings;

    private int numCorrectAns;
    private int skipsUsed;
    private long timeScore;
    private int finalScore;

    private HighScoresDB highScoreDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        displayCorrectQuestions = findViewById(R.id.displayCorrectQuestions);
        displaySkipsUsed = findViewById(R.id.displaySkipsUsed);
        displayFinalScore = findViewById(R.id.displayFinalScore);
        inputName = findViewById(R.id.inputName);
        gameSettings = getSharedPreferences("GameSettings", MODE_PRIVATE);

        highScoreDB = new HighScoresDB(this);

        timeScore = gameSettings.getLong("timeScore", 0);
        numCorrectAns = gameSettings.getInt("numCorrectAns", 0);
        skipsUsed = gameSettings.getInt("gameSkippedQuestionCount", 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        finalScore = calFinalScore(timeScore, numCorrectAns, skipsUsed);
        gameSettings.edit().putInt("finalScore", finalScore).apply();

        displayCorrectQuestions.setText(Integer.toString(numCorrectAns));
        displaySkipsUsed.setText(Integer.toString(skipsUsed));
        displayFinalScore.setText(Integer.toString(finalScore));
    }

    public void buttonPressed(View view){
        Intent goToEndScreen = new Intent(this, SocialMediaActivity.class);
        switch (view.getId()){
            case R.id.passButton:
                startActivity(goToEndScreen);
                break;
            case R.id.saveButton:
                if(inputName.getText().toString().equals("")){ //Error checking
                    Toast errorMessage = Toast.makeText(this, "Enter a name!", Toast.LENGTH_SHORT);
                    errorMessage.show();
                }
                else{
                    addScore(inputName.getText().toString(), finalScore);
                    Toast confirmation = Toast.makeText(this, "Name added!", Toast.LENGTH_SHORT);
                    confirmation.show();
                    startActivity(goToEndScreen);
                }
                break;
        }
    }

    public static int calFinalScore(long timeScore, int numCorrectAns, int skipUsed){
        int totalCount;
        totalCount = (numCorrectAns * 100) + (skipUsed * 100) + ((int)(timeScore/1000));
        return totalCount;
    }

    public void addScore(String name, int score){
        //Adding the score values into the database
        SQLiteDatabase db = highScoreDB.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("score", score);
        db.insert("highscores", null, contentValues);
    }
}