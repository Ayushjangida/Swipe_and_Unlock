package com.example.swipeunlock;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.EventLog;
import android.util.Log;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private final String TAG = "Point Info";
    private long pressTime = -1l;
    private long releaseTime = 1l;
    private long duration = -1l;
    float oldX, oldY;
    float newX, newY;
    private VelocityTracker mVelocityTracker = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.d(TAG, "duration: " + duration);
        Log.d(TAG, "start point: " + oldX + "--" + oldY);
        Log.d(TAG, "end point: " + newX + "--" + newY);

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
//        if(event.getAction() == MotionEvent.ACTION_DOWN){
//            oldX = event.getX();
//            oldY = event.getY();
//            pressTime = System.currentTimeMillis();
//            if(releaseTime != -1l) duration = pressTime - releaseTime;
//        }
//        else if(event.getAction() == MotionEvent.ACTION_UP){
//            newX = event.getX();
//            newY = event.getY();
//            releaseTime = System.currentTimeMillis();
//            duration = System.currentTimeMillis() - pressTime;
//        }
//
//        Log.d(TAG, "duration: " + duration);
        return true;
    }
}