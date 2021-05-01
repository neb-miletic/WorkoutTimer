package com.example.workouttimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
     EditText workoutInput;
     TextView workoutText;
     TextView workoutTag;
     SharedPreferences sharedPreferences;

     private Chronometer chronometer;
     private long pauseOffset;
     private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("com.example.workouttimer",MODE_PRIVATE);
        workoutInput = findViewById(R.id.workoutInput);
        workoutText = findViewById(R.id.workoutText);
        workoutTag = findViewById(R.id.workoutTag);
        chronometer = findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String t = (h < 10 ? "0"+h: h)+":"+(m < 10 ? "0"+m: m)+":"+ (s < 10 ? "0"+s: s);
                chronometer.setText(t);
            }
        });
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setText("00:00:00");
        checkSharedPreferences();
    }

   @Override
   protected void onSaveInstanceState(@NonNull Bundle outState) {
       super.onSaveInstanceState(outState);
       getDelegate().onSaveInstanceState(outState);


   }

    public void startChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }
    public void pauseChronometer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }
    public void stopChronometer(View v) {
        pauseChronometer(v);
     if (workoutInput != null) {
         SharedPreferences.Editor editor = sharedPreferences.edit();
         editor.putString("time", chronometer.getText().toString());
         editor.putString("WORKOUT_TYPE", workoutInput.getText().toString());
         editor.apply();
     }
     else {
         sharedPreferences.edit().putString("WORKOUT_TYPE", "").apply();
         sharedPreferences.edit().putString("time", chronometer.getText().toString()).apply();
     }

    }

    public void checkSharedPreferences () {
        String workout = sharedPreferences.getString("WORKOUT_TYPE", "");
        String workoutTime = sharedPreferences.getString("time","");
        workoutText.setText(workoutTime );
        workoutTag.setText(workout);


    }



 }


