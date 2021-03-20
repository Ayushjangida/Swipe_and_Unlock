package com.example.swipeunlock;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.EventLog;
import android.util.Log;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import weka.core.Attribute;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private final String TAG = "Point Info";
    private long pressTime = -1l;
    private long releaseTime = 1l;
    private long duration = -1l;
    float oldX, oldY;
    float newX, newY;
    private VelocityTracker mVelocityTracker = null;
    private SeekBar seekBar;
    private long startTime, endTime;

    private Swipe swipe;

    private Data db;
    private List<Swipe> swipeList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new Data(MainActivity.this);


        seekBar = (SeekBar) findViewById(R.id.seekBar);

        swipe = new Swipe();
        swipeList = new ArrayList<>();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        if (action == MotionEvent.ACTION_DOWN)    {
                            float x = event.getX();
                            float y = event.getY();
                            float pressure = event.getPressure();
                            swipe.setStartPointX(x);
                            swipe.setStartPointY(y);
                            swipe.setPressure(pressure);
                            startTime = System.currentTimeMillis();
                        }

                        if (action == MotionEvent.ACTION_UP)    {
                            float x = event.getX();
                            float y = event.getY();
                            endTime = System.currentTimeMillis();
                            long duration = endTime - startTime;
                            swipe.setEndPointX(x);
                            swipe.setEndPointY(y);
                            swipe.setDuration(duration);
                            Toast.makeText(MainActivity.this, "Password has been recorded", Toast.LENGTH_SHORT).show();
//                            Log.d(TAG, "start  point: " + swipe.getStartPointX() + " " + swipe.getStartPointY());
//                            Log.d(TAG, "Duration and Pressure: " + swipe.getDuration() + " " + swipe.getPressure());
//                            Log.d(TAG, "End Points: " + swipe.getEndPointX() + " " + swipe.getEndPointY());
                            if (swipe.getPressure() != 0)   db.addSwipe(swipe, getApplicationContext());
                        }

                        return false;
                    }
                });
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });


    swipeList = db.getSwipeInfo();
    if (swipeList.size() > 0)   {
        for (int i = 0; i < swipeList.size(); i++)  {
            Swipe swipe = new Swipe();
            Log.d(TAG, "KEY -> " + i);
            Log.d(TAG, "------------ ");
            Log.d(TAG, swipeList.get(i).getStartPointX() + " ___ " + swipeList.get(i).getStartPointY());
            Log.d(TAG, swipeList.get(i).getDuration() + "___ " + swipeList.get(i).getPressure());
            Log.d(TAG, swipeList.get(i).getEndPointX() + "___" + swipeList.get(i).getEndPointY());
            Log.d(TAG, "------------");
        }
    }

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);

        switch(action) {
            case MotionEvent.ACTION_DOWN:
                oldX = event.getX();
                oldY = event.getY();
                if(mVelocityTracker == null) {
                    // Retrieve a new VelocityTracker object to watch the
                    // velocity of a motion.
                    mVelocityTracker = VelocityTracker.obtain();
                }
                else {
                    // Reset the velocity tracker back to its initial state.
                    mVelocityTracker.clear();
                }
                // Add a user's movement to the tracker.
                mVelocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                // When you want to determine the velocity, call
                // computeCurrentVelocity(). Then call getXVelocity()
                // and getYVelocity() to retrieve the velocity for each pointer ID.
                mVelocityTracker.computeCurrentVelocity(1000);
                // Log velocity of pixels per second
                // Best practice to use VelocityTrackerCompat where possible.
                Log.d(TAG, "X velocity: " + mVelocityTracker.getXVelocity(pointerId));
                Log.d(TAG, "Y velocity: " + mVelocityTracker.getYVelocity(pointerId));
                break;
            case MotionEvent.ACTION_UP:
                newX = event.getX();
                newY = event.getY();
                break;
            case MotionEvent.ACTION_CANCEL:
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker.recycle();
                break;
        }

        Log.d(TAG, " start points: " + oldX + "--" + oldY);
        Log.d(TAG, "end points: " + newX + "--" + newY);
        return true;
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
//
        return true;
    }
}