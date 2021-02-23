package com.example.swipeunlock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Data extends SQLiteOpenHelper {
//    private static final String END_POINT_X = "end_point_X";
//    private static final String END_POINT_Y = "end_point_Y";

    private final Context context;

    private final String TAG = "DB";
    private String fileName = "Data";


    public Data(@Nullable Context context) {
        super(context, SwipeConstants.DATABASE_NAME, null, SwipeConstants.DATABASE_VERSION);
        this.context = context;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create Table

        String CREATE_SWIPE_TABLE = "CREATE TABLE " + SwipeConstants.TABLE_SWIPES + "("
                + SwipeConstants.START_POINT_X + " FLOAT,"
                + SwipeConstants.START_POINT_Y + " FLOAT,"
                + SwipeConstants.DURATION + " FLOAT,"
                + SwipeConstants.PRESSURE + " FLOAT,"
                + SwipeConstants.END_POINT_X + " FLOAT,"
                + SwipeConstants.END_POINT_Y + " FLOAT);";
        db.execSQL(CREATE_SWIPE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SwipeConstants.TABLE_SWIPES);

        onCreate(db);
    }

    void addSwipe(Swipe swipe, Context context)  {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SwipeConstants.START_POINT_X, swipe.getStartPointX());
        values.put(SwipeConstants.START_POINT_Y, swipe.getStartPointY());
        values.put(SwipeConstants.DURATION, swipe.getDuration());
        values.put(SwipeConstants.PRESSURE, swipe.getPressure());
        values.put(SwipeConstants.END_POINT_X, swipe.getEndPointX());
        values.put(SwipeConstants.END_POINT_Y, swipe.getEndPointY());

        db.insert(SwipeConstants.TABLE_SWIPES, null, values);
        String string = swipe.getStartPointX() + "," + swipe.getStartPointY() + "," + swipe.getDuration() + "," + swipe.getPressure()
        + "," + swipe.getEndPointX() + "," + swipe.getEndPointY();
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            outputStreamWriter.write(string);
            outputStreamWriter.close();
            Log.d(TAG, "File saved successfully");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Swipe info added to the database successfully");
        db.close();
    }


    public List<Swipe> getSwipeInfo()   {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Swipe> swipeList =  new ArrayList<>();

        Cursor cursor = db.query(SwipeConstants.TABLE_SWIPES,
                new String[]{
                        SwipeConstants.START_POINT_X,
                        SwipeConstants.START_POINT_Y,
                        SwipeConstants.DURATION,
                        SwipeConstants.PRESSURE,
                        SwipeConstants.END_POINT_X,
                        SwipeConstants.END_POINT_Y},
                null,
                null,
                null,
                null,
                null);

        if(cursor != null)  cursor.moveToFirst();

        if (cursor.moveToFirst())   {
            do {
                Swipe swipe = new Swipe();
                swipe.setStartPointX(Float.parseFloat(cursor.getString(cursor.getColumnIndex(SwipeConstants.START_POINT_X))));
                swipe.setStartPointY(Float.parseFloat(cursor.getString(cursor.getColumnIndex(SwipeConstants.START_POINT_Y))));
                swipe.setDuration(Float.parseFloat(cursor.getString(cursor.getColumnIndex(SwipeConstants.DURATION))));
                swipe.setPressure(Float.parseFloat(cursor.getString(cursor.getColumnIndex(SwipeConstants.PRESSURE))));
                swipe.setEndPointX(Float.parseFloat(cursor.getString(cursor.getColumnIndex(SwipeConstants.END_POINT_X))));
                swipe.setEndPointY(Float.parseFloat(cursor.getString(cursor.getColumnIndex(SwipeConstants.END_POINT_Y))));

                swipeList.add(swipe);
            }while (cursor.moveToNext());
        }
        return swipeList;
    }
}
