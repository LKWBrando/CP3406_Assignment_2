//HighScoreActivity created to display the Top five scores based off the SQLite database of the application
package example.assignmentone.cp3406.cp3406_assignment_2;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

public class HighScoreActivity extends AppCompatActivity {

    private HighScoresDB highScoreDB;
    private TextView displayHighScore;
    private HashMap<String, Integer> nameToScore;
    private ArrayList<String>topNames;
    private String scoreFromList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        highScoreDB = new HighScoresDB(this);
        displayHighScore = findViewById(R.id.displayHighScore);

        nameToScore = new HashMap<>();
        topNames = new ArrayList<>();

        //Cursor cursor = highScoreDB.getReadableDatabase().query("highscores", null, null, null, null, null, "score" + "DESC");
        Cursor cursor = highScoreDB.getReadableDatabase().rawQuery("SELECT * FROM highscores ORDER BY score DESC", null);
        //Selecting all the data from highscores table and arranging according to scores in descending order
        while(cursor.moveToNext()){
            System.out.println(cursor.getString(1));
            topNames.add(cursor.getString(1));
            nameToScore.put(cursor.getString(1), cursor.getInt(2)); //Mapping the names to the respective scores
        }
        cursor.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<5; i++){ //Taking top 5 scores
            String topName = topNames.get(i);
            scoreFromList = Integer.toString(nameToScore.get(topName)); //Getting the score values based off the names
            builder.append("Number ").append(i+1).append(" : ").append(topName).append("  ---   ").append(scoreFromList).append("\n");
        }
        displayHighScore.setText(builder.toString().trim());
    }

    public void buttonPressed(View view){
        Intent returnToMain = new Intent(this, MainActivity.class);
        startActivity(returnToMain);
    }
}