//HighScoresDB Class created to extend from the SQLiteOpenHelper class, allowing the application to create and maintain a local SQL database
package example.assignmentone.cp3406.cp3406_assignment_2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HighScoresDB extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private Context context;

    HighScoresDB(Context context){
        super(context, "highscores.db", null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE highscores (_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,score INT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS highscores");
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

}
