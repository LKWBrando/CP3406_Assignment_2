/*SelectTopicActivity is created to allow the user to choose from the list of topics available, and loads the questions accordingly*/
package example.assignmentone.cp3406.cp3406_assignment_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SelectTopicActivity extends AppCompatActivity {
    private SharedPreferences gameSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_topic);
        gameSettings = getSharedPreferences("GameSettings", MODE_PRIVATE);
    }

    public void buttonPressed(View view){
        //On user's button press, change the topic of the quiz accordingly
        Intent goToPlay = new Intent(this, PreGameActivity.class);
        Intent goToMenu = new Intent(this, MainActivity.class);
        switch(view.getId()){
            case R.id.subjectBiology:
                gameSettings.edit().putString("gameSubject", "Biology").apply();
                startActivity(goToPlay);
                break;
            case R.id.subjectChemistry:
                gameSettings.edit().putString("gameSubject", "Chemistry").apply();
                startActivity(goToPlay);
                break;
            case R.id.subjectPhysics:
                gameSettings.edit().putString("gameSubject", "Physics").apply();
                startActivity(goToPlay);
                break;
            case R.id.returnToMenu:
                startActivity(goToMenu);
                break;
        }
    }
}
