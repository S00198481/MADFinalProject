package s00198481.itsligo.gameproject;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class PlayScreen extends AppCompatActivity implements SensorEventListener {

    // Adding buttons
    Button btnNorth, btnWest, btnEast, btnSouth, FB;

    private final double NORTH_MOVE_FORWARD = 8;     // upper mag limit
    private final double NORTH_MOVE_BACKWARD = 5;      // lower mag limit

    private final double SOUTH_MOVE_FORWARD =  1;     // upper mag limit
    private final double SOUTH_MOVE_BACKWARD = 4;      // lower mag limit

    private final double EAST_MOVE_FORWARD = 1;     // upper mag limit
    private final double EAST_MOVE_BACKWARD = 0;      // lower mag limit

    private final double WEST_MOVE_FORWARD = -1;     // upper mag limit
    private final double WEST_MOVE_BACKWARD = 0;      // lower mag limit

    boolean highLimitNorth = false;      // detect high limit
    boolean highLimitSouth = false;      // detect high limit
    boolean highLimitEast = false;      // detect high limit
    boolean highLimitWest = false;      // detect high limit

    int counterNorth = 0;
    int counterSouth = 0;
    int counterEast = 0;
    int counterWest = 0;

    int sequenceCount = 4, n = 0;
    int[] gameSequence = new int[120];
    int[] sequence = new int[4];
    int[] thisSequence = new int[4];
    int arrayIndex = 0;
    int sequenceIndex = 0;
    int score = 0, round = 1, increase;

    Animation anim;
    StringBuilder sb;
    Button bRed, bBlue, bYellow, bGreen;
    TextView tvSeq, tvx, tvy, tvz, tvNorth, tvSouth, tvEast, tvWest;
    private SensorManager mSensorManager;
    private Sensor mSensor;

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
        public void onFinish() { }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);


        btnEast = findViewById(R.id.btnEast);
        btnWest = findViewById(R.id.btnWest);
        btnNorth = findViewById(R.id.btnNorth);
        btnSouth = findViewById(R.id.btnSouth);


        tvx = findViewById(R.id.tvX);
        tvy = findViewById(R.id.tvY);
        tvz = findViewById(R.id.tvZ);
        tvSeq = findViewById(R.id.tvSeq);
        tvNorth = findViewById(R.id.tvNorth);
        tvSouth = findViewById(R.id.tvSouth);
        tvWest  = findViewById(R.id.tvWest);
        tvEast  = findViewById(R.id.tvEast);

        // we are going to use the sensor service
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sequence = getIntent().getIntArrayExtra("sequence");
        if(sequence!=null) {
            tvSeq.setText(String.valueOf(sequence[0]) + String.valueOf(sequence[1])
                    + String.valueOf(sequence[2]) + String.valueOf(sequence[3]));
        } else {
            tvSeq.setText("sequence error");
        }

    }

    protected void onResume() {
        super.onResume();
        // turn on the sensor
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);    // turn off listener to save power
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        tvx.setText(String.valueOf(x));
        tvy.setText(String.valueOf(y));
        tvz.setText(String.valueOf(z));

        // North Movement
        if ((x > NORTH_MOVE_FORWARD && z > 0) && (highLimitNorth == false)) {
            highLimitNorth = true;
        }
        if ((x < NORTH_MOVE_BACKWARD && z > 0) && (highLimitNorth == true)) {
            // we have a tilt to the NORTH
            counterNorth++;
            thisSequence[sequenceIndex++] = 1;
            tvNorth.setText(String.valueOf(counterNorth));
            highLimitNorth = false;
            Handler handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {

                    btnNorth.setPressed(true);
                    btnNorth.invalidate();
                    btnNorth.performClick();
                    Handler handler1 = new Handler();
                    Runnable r1 = new Runnable() {
                        public void run() {
                            btnNorth.setPressed(false);
                            btnNorth.invalidate();
                        }
                    };
                    handler1.postDelayed(r1, 600);

                } // end runnable
            };
            handler.postDelayed(r, 600);
        }

        // South Movement
        if ((x < SOUTH_MOVE_FORWARD && z < 0) && (highLimitSouth == false)) {
            highLimitSouth = true;
        }
        if ((x > SOUTH_MOVE_BACKWARD && z < 0) && (highLimitSouth == true)) {
            // we have a tilt to the SOUTH
            counterSouth++;
            thisSequence[sequenceIndex++] = 3;
            tvSouth.setText(String.valueOf(counterSouth));
            highLimitSouth = false;
            Handler handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {

                    btnSouth.setPressed(true);
                    btnSouth.invalidate();
                    btnSouth.performClick();
                    Handler handler1 = new Handler();
                    Runnable r1 = new Runnable() {
                        public void run() {
                            btnSouth.setPressed(false);
                            btnSouth.invalidate();
                        }
                    };
                    handler1.postDelayed(r1, 600);

                } // end runnable
            };
            handler.postDelayed(r, 600);
        }

        // East Movement
        if (y > EAST_MOVE_FORWARD && highLimitEast == false) {
            highLimitEast = true;
        }
        if (y < EAST_MOVE_BACKWARD && highLimitEast == true) {
            // we have a tilt to the EAST
            counterEast++;
            thisSequence[sequenceIndex++] = 2;
            tvEast.setText(String.valueOf(counterEast));
            highLimitEast = false;
            Handler handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {

                    btnEast.setPressed(true);
                    btnEast.invalidate();
                    btnEast.performClick();
                    Handler handler1 = new Handler();
                    Runnable r1 = new Runnable() {
                        public void run() {
                            btnEast.setPressed(false);
                            btnEast.invalidate();
                        }
                    };
                    handler1.postDelayed(r1, 600);

                } // end runnable
            };
            handler.postDelayed(r, 600);
        }

        // West Movement
        if (y < WEST_MOVE_FORWARD && highLimitWest == false) {
            highLimitWest = true;
        }
        if (y > WEST_MOVE_BACKWARD && highLimitWest == true) {
            // we have a tilt to the WEST
            counterWest++;
            thisSequence[sequenceIndex++] = 4;
            tvWest.setText(String.valueOf(counterWest));
            highLimitWest = false;
            Handler handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {

                    btnWest.setPressed(true);
                    btnWest.invalidate();
                    btnWest.performClick();
                    Handler handler1 = new Handler();
                    Runnable r1 = new Runnable() {
                        public void run() {
                            btnWest.setPressed(false);
                            btnWest.invalidate();
                        }
                    };
                    handler1.postDelayed(r1, 600);

                } // end runnable
            };
            handler.postDelayed(r, 600);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not used
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    CountDownTimer cdtRound1 = new CountDownTimer(6000,  1500) {

        public void onTick(long millisUntilFinished) {
            //mTextField.setText("seconds remaining: " + millisUntilFinished / 1500);
            oneButton();
            //here you can have your logic to set text to edittext
        }
        public void onFinish() { }
    };
    CountDownTimer cdtRound2 = new CountDownTimer(9000,  1500) {

        public void onTick(long millisUntilFinished) {
            //mTextField.setText("seconds remaining: " + millisUntilFinished / 1500);
            oneButton();
            //here you can have your logic to set text to edittext
        }
        public void onFinish() { }
    };
    CountDownTimer cdtRound3 = new CountDownTimer(12000,  1500) {

        public void onTick(long millisUntilFinished) {
            //mTextField.setText("seconds remaining: " + millisUntilFinished / 1500);
            oneButton();
            //here you can have your logic to set text to edittext
        }
        public void onFinish() { }
    };
    CountDownTimer cdtRound4 = new CountDownTimer(15000,  1500) {

        public void onTick(long millisUntilFinished) {
            //mTextField.setText("seconds remaining: " + millisUntilFinished / 1500);
            oneButton();
            //here you can have your logic to set text to edittext
        }
        public void onFinish() { }
    };
    CountDownTimer cdtRound5 = new CountDownTimer(18000,  1500) {

        public void onTick(long millisUntilFinished) {
            //mTextField.setText("seconds remaining: " + millisUntilFinished / 1500);
            oneButton();
            //here you can have your logic to set text to edittext
        }
        public void onFinish() { }
    };

    public void doPlay(View view) {

        switch  (round)
        {
            case(1):
                cdtRound1.start();
                break;
            case(2):
                cdtRound2.start();
                break;
            case(3):
                cdtRound3.start();
                break;
            case(4):
                cdtRound4.start();
                break;
            case(5):
                cdtRound5.start();
                break;
        }
    }

    private void oneButton() {
        n = getRandom(sequenceCount);

        switch (n) {
            case 1:
                flashButton(btnNorth);
                gameSequence[arrayIndex++] = NORTH;
                break;
            case 2:
                flashButton(btnWest);
                gameSequence[arrayIndex++] = WEST;
                break;
            case 3:
                flashButton(btnSouth);
                gameSequence[arrayIndex++] = SOUTH;
                break;
            case 4:
                flashButton(btnEast);
                gameSequence[arrayIndex++] = EAST;
                break;
            default:
                break;
        }   // end switch

        for (int i = 0; i<4; i++) {
            thisSequence[i] = 0;
        }
        tvSeq.setText(String.valueOf(sequence[0]) + String.valueOf(sequence[1])
                    + String.valueOf(sequence[2]) + String.valueOf(sequence[3]));

    }

    // return a number between 1 and maxValue
    private int getRandom(int maxValue) {
        return ((int) ((Math.random() * maxValue) + 1));
    }

    private void flashButton(Button button) {
        FB = button;
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {

                FB.setPressed(true);
                FB.invalidate();
                FB.performClick();
                Handler handler1 = new Handler();
                Runnable r1 = new Runnable() {
                    public void run() {
                        FB.setPressed(false);
                        FB.invalidate();
                    }
                };
                handler1.postDelayed(r1, 600);

            } // end runnable
        };
        handler.postDelayed(r, 600);
    }


    public void doFail(View view) {
        Intent gameOver = new Intent(this, GameOver.class);
        gameOver.putExtra("score", score);
        gameOver.putExtra("round", round);
        startActivity(gameOver);
    }

    public void checkForWin(View view) {
        for (int i = 0; i< 4; i++) {
            if (sequence[i]!=thisSequence[i]) {
                Toast.makeText(this, "Fail - Try again!", Toast.LENGTH_SHORT).show();
                doFail();
                return;
            }
            Toast.makeText(this, "Round " + String.valueOf(round) + " Complete!", Toast.LENGTH_SHORT).show();
            score = score+4;
        }
    }

    public void doExit(View view) {
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }

    public void doFail() {
        Intent gameOver = new Intent(this, GameOver.class);
        gameOver.putExtra("score", score);
        gameOver.putExtra("round", round);
        startActivity(gameOver);
    }


}