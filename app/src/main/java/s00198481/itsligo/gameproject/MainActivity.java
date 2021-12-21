package s00198481.itsligo.gameproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int BLUE = 1;
    private final int RED = 2;
    private final int YELLOW = 3;
    private final int GREEN = 4;

    Animation anim;

    StringBuilder sb;
    Button bRed, bBlue, bYellow, bGreen;
    TextView tv;

    int sequenceCount = 4, n = 0;
    int[] gameSequence = new int[120];
    int[] sequence = new int[4];
    int arrayIndex = 0;
    int sequenceIndex = 0;

    private final int NORTH = 1;
    private final int WEST = 2;
    private final int SOUTH = 3;
    private final int EAST = 4;

    CountDownTimer ct = new CountDownTimer(6000,  1500) {

        public void onTick(long millisUntilFinished) {
            //mTextField.setText("seconds remaining: " + millisUntilFinished / 1500);
            oneButton();
            //here you can have your logic to set text to edittext
        }

        public void onFinish() {
            //mTextField.setText("done!");
            // we now have the game sequence

            tv.setText(sb);
            for (int i = 0; i< arrayIndex; i++)
                Log.d("game sequence", String.valueOf(gameSequence[i]));
            // start next activity

            // put the sequence into the next activity
            // stack overglow https://stackoverflow.com/questions/3848148/sending-arrays-with-intent-putextra
            //Intent i = new Intent(A.this, B.class);
            //i.putExtra("numbers", array);
            //startActivity(i);

            // start the next activity
            // int[] arrayB = extras.getIntArray("numbers");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bRed = findViewById(R.id.btnWest);
        bBlue = findViewById(R.id.btnNorth);
        bYellow = findViewById(R.id.btnEast);
        bGreen = findViewById(R.id.btnSouth);
        tv = findViewById(R.id.tvResult);

    }

    private void oneButton() {
        n = getRandom(sequenceCount);
        sb.append(String.valueOf(n) + ", ");
        Toast.makeText(this, "Number = " + n, Toast.LENGTH_SHORT).show();

        switch (n) {
            case 1:
                flashButton(bBlue);
                gameSequence[arrayIndex++] = BLUE;
                sequence[sequenceIndex++] = n;
                break;
            case 2:
                flashButton(bYellow);
                gameSequence[arrayIndex++] = RED;
                sequence[sequenceIndex++] = n;
                break;
            case 3:
                flashButton(bGreen);
                gameSequence[arrayIndex++] = YELLOW;
                sequence[sequenceIndex++] = n;
                break;
            case 4:
                flashButton(bRed);
                gameSequence[arrayIndex++] = GREEN;
                sequence[sequenceIndex++] = n;
                break;
            default:
                break;
        }   // end switch
    }

    private void flashButton(Button button) {

        anim = new AlphaAnimation(1,0);
        anim.setDuration(1000); //You can manage the blinking time with this parameter

        anim.setRepeatCount(0);
        button.startAnimation(anim);

    }

    //
    // return a number between 1 and maxValue
    private int getRandom(int maxValue) {

        return ((int) ((Math.random() * maxValue) + 1));
    }

    public void doPlay(View view) {
        sb = new StringBuilder("Result : ");
        ct.start();
    }

    public void doNext(View view) {
        Intent playPage = new Intent(this, PlayScreen.class);
        playPage.putExtra("sequence", sequence);
        startActivity(playPage);
    }
}