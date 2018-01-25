/*This activity is created to allow the user to post his/her score on twitter.*/

package example.assignmentone.cp3406.cp3406_assignment_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SocialMediaActivity extends AppCompatActivity {

    private SharedPreferences gameSettings;
    private int finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);
        gameSettings = getSharedPreferences("GameSettings", MODE_PRIVATE);
        finalScore = gameSettings.getInt("finalScore", 0); //Get final score from
    }

    public void buttonPressed(View view) {
        switch (view.getId()) {
            case R.id.shareButton: //Share high score on twitter
                String shareMessage = "I've just obtained " + finalScore + " on the Quick Science App!";
                String tweetUrl = "https://twitter.com/intent/tweet?text=" + shareMessage;
                Uri uri = Uri.parse(tweetUrl);
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
                break;

            case R.id.passButton: //Pass high score, returns to main menu
                Intent goToMenu = new Intent(this, MainActivity.class);
                startActivity(goToMenu);
                finish();
                break;
        }
    }
}
