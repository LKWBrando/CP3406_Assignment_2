package example.assignmentone.cp3406.cp3406_assignment_2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonPressed(View view){
        Intent goToSelectTopic = new Intent(this, SelectTopicActivity.class);
        Intent goToInstructions = new Intent(this, InstructionsActivity.class);
        Intent goToHighscores = new Intent(this, HighScoreActivity.class);
        switch(view.getId()){
            case R.id.playButton:
                startActivity(goToSelectTopic);
                break;
            case R.id.instructionsButton:
                startActivity(goToInstructions);
                break;
            case R.id.highScoreButton:
                startActivity(goToHighscores);
                break;
        }
    }
}
