package s00198481.itsligo.gameproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GameOver extends AppCompatActivity {

    int score, round;
    TextView tvScore, tvRound;

    public DatabaseHandler db;
    public EditText etScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        etScore = findViewById(R.id.etScore);


        tvScore = findViewById(R.id.tvScore);
        tvRound = findViewById(R.id.tvRound);

        score = getIntent().getIntExtra("score", 0);
        round = getIntent().getIntExtra("round",0);

        tvScore.setText(String.valueOf(score));
        tvRound.setText(String.valueOf(round));


        db = new DatabaseHandler(this);
        db.emptyHiScores();
        Data();
        Log.i("Reading: ", "Reading all scores..");
        List<HighScore> hiScores = db.getAllHiScores();


        for (HighScore hs : hiScores) {
            String log =
                    "Id: " + hs.getScore_id() +
                            ", Date: " + hs.getGame_date() +
                            " , Player: " + hs.getPlayer_name() +
                            " , Score: " + hs.getScore();

            // Writing HiScore to log
            Log.i("Score: ", log);
        }

        Log.i("divider", "========================================");

        HighScore singleScore = db.getHiScore(5);
        Log.i("High Score 5 is by ", singleScore.getPlayer_name() + " with a score of " + singleScore.getScore());

        Log.i("divider", "========================================");

        // Calling SQL statement
        List<HighScore> top5HiScores = db.getTopFiveScores();
        for (HighScore hs : top5HiScores) {
            String log =
                    "Id: " + hs.getScore_id() +
                            ", Date: " + hs.getGame_date() +
                            " , Player: " + hs.getPlayer_name() +
                            " , Score: " + hs.getScore();
            // Writing HiScore to log
            Log.i("Score: ", log);
        }

        HighScore lastScore = top5HiScores.get(top5HiScores.size() - 1);
        if (score > lastScore.score) {
            Toast.makeText(this,"You Won!!! Enter your Name!!", Toast.LENGTH_LONG).show();
        }

    }

    public void Data(){
        // Inserting hi scores
        Log.i("Insert: ", "Inserting Scores...");
        db.addHiScore(new HighScore("20/11/2021", "J Biden", 4));
        db.addHiScore(new HighScore("26/11/2021", "M Martin", 8));
        db.addHiScore(new HighScore("28/03/2021", "L Varadkar", 4));
        db.addHiScore(new HighScore("09/12/2021", "T Holohan", 12));
        db.addHiScore(new HighScore("31/07/2021", "G Adams", 800));
        db.addHiScore(new HighScore("23/09/2021", "B Johnson", 32));
    }

    public void doSubmit(View view) {
        List<HighScore> top5HiScores = db.getTopFiveScores();
        HighScore lastScore = top5HiScores.get(top5HiScores.size() - 1);

        if(score > lastScore.score && etScore.getText().toString() != ""){
            String userName = etScore.getText().toString();
            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            db.addHiScore(new HighScore(date, userName, score));
            top5HiScores = db.getTopFiveScores();
            for (HighScore hs : top5HiScores) {
                String log =
                        "Id: " + hs.getScore_id() +
                                " , Player: " + hs.getPlayer_name() +
                                " , Score: " + hs.getScore();

                // Writing HiScore to log
                Log.i("Score: ", log);
            }
        }
        else{
            Toast.makeText(this,"Your Score isn't High Enough",Toast.LENGTH_SHORT).show();
        }

        onHighScore(view);
    }

    public void onHighScore(View view) {
        Intent intent = new Intent(view.getContext(), HiScores.class);

        startActivity(intent);
        finish();
    }

    public void onRestart(View view) {
        Intent in = new Intent(view.getContext(), MainActivity.class);

        startActivity(in);
    }
}