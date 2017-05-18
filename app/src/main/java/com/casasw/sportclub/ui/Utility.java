package com.casasw.sportclub.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.casasw.sportclub.data.SportContract;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Junior on 02/05/2017.
 * Some often used methods
 */

class Utility {
    static long getUserID(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getLong(context.getString(R.string.pref_user_id_key),
                Long.parseLong(context.getString(R.string.pref_user_id_default)));
    }
    static boolean isOnline(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    static ContentValues createPlayerValues(String name, String photo, String email) {
        ContentValues values = new ContentValues();
        values.put(SportContract.PlayerEntry.COLUMN_PLAYER_NAME, name);
        values.put(SportContract.PlayerEntry.COLUMN_HANDEDNESS, "");
        values.put(SportContract.PlayerEntry.COLUMN_BDAY, "");
        values.put(SportContract.PlayerEntry.COLUMN_HEIGHT, "");
        values.put(SportContract.PlayerEntry.COLUMN_WEIGHT, "");
        values.put(SportContract.PlayerEntry.COLUMN_CITY, "");
        values.put(SportContract.PlayerEntry.COLUMN_STATE, "");
        values.put(SportContract.PlayerEntry.COLUMN_RATING, "7");
        values.put(SportContract.PlayerEntry.COLUMN_EMAIL, email);
        values.put(SportContract.PlayerEntry.COLUMN_PROFILE_PHOTO, photo);
        return values;
    }

    static ContentValues createAttributesValues(long _id) {
        ContentValues values = new ContentValues();
        int random = (int) Math.floor(Math.random()*10);
        values.put(SportContract.AttributesEntry.COLUMN_PLAYER_ID, _id);
        values.put(SportContract.AttributesEntry.COLUMN_SPEED, random);
        random = (int) Math.floor(Math.random()*10);
        values.put(SportContract.AttributesEntry.COLUMN_POWER, random);
        random = (int) Math.floor(Math.random()*10);
        values.put(SportContract.AttributesEntry.COLUMN_TECHNIQUE, random);
        random = (int) Math.floor(Math.random()*10);
        values.put(SportContract.AttributesEntry.COLUMN_FITNESS, random);
        random = (int) Math.floor(Math.random()*10);
        values.put(SportContract.AttributesEntry.COLUMN_FAIR_PLAY, random);
        return values;
    }

    static ContentValues createPlayerSportValues(long player_id, long sport_id) {
        ContentValues values = new ContentValues();
        values.put(SportContract.PlayerSportEntry.COLUMN_PLAYER_ID, ""+player_id);
        values.put(SportContract.PlayerSportEntry.COLUMN_SPORT_ID, ""+sport_id);
        values.put(SportContract.PlayerSportEntry.COLUMN_POSITIONS, "");

        return values;
    }

    static void logCursor(Cursor c, final String LOG_TAG) {
        String col = "";
        for (int i =0;i<c.getColumnCount();i++) {
            col =  col + "["+c.getColumnName(i)+"] - ";
        }
        String values = "";
        do {
            for (int j =0;j<c.getColumnCount();j++) {
                values =  values + c.getString(j) +" - ";
            }
            values = values + "\n";

        }while (c.moveToNext());
        Log.d(LOG_TAG, col);
        Log.d(LOG_TAG, values);
    }

    static String getSoccerPosition(String posAbbrev, Context context) {
        final List<String> listPosAbbrev = Arrays.asList(context.getResources().getStringArray(R.array.soccer_position_abbrev));
        List<String> listPos = Arrays.asList(context.getResources().getStringArray(R.array.soccer_position_array));
        int i = listPosAbbrev.indexOf(posAbbrev);

        return listPos.get(i);
    }

    static String getBasketPosition(String posAbbrev, Context context) {
        final List<String> listPosAbbrev = Arrays.asList(context.getResources().getStringArray(R.array.basket_position_abbrev));
        List<String> listPos = Arrays.asList(context.getResources().getStringArray(R.array.basket_position_array));
        int i = listPosAbbrev.indexOf(posAbbrev);

        return listPos.get(i);
    }
}
