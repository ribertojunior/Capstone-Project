package com.casasw.sportclub.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Junior on 30/03/2017.
 * Utilities for tests.
 */
@RunWith(AndroidJUnit4.class)
public class TestUtilities {

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            String value = valueCursor.getString(idx);
            if (expectedValue.length() > value.length()) {
                expectedValue = expectedValue.substring(0, value.length()-1);
                value = value.substring(0, value.length()-1);
            }

            assertTrue("Column '"+columnName+"' Value '" + value +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue.equals(value));


        }
    }

    static ContentValues createPlayerValues() {
        ContentValues values = new ContentValues();
        int random = (int) Math.floor(Math.random());
        values.put(SportContract.PlayerEntry.COLUMN_PLAYER_NAME, "Josivaldo "+random);
        values.put(SportContract.PlayerEntry.COLUMN_POSITION, "AMF");
        values.put(SportContract.PlayerEntry.COLUMN_HANDEDNESS, "R");
        values.put(SportContract.PlayerEntry.COLUMN_AGE, "27");
        values.put(SportContract.PlayerEntry.COLUMN_HEIGHT, "179");
        values.put(SportContract.PlayerEntry.COLUMN_WEIGHT, "80");
        values.put(SportContract.PlayerEntry.COLUMN_CITY, "Josivaldo City");
        values.put(SportContract.PlayerEntry.COLUMN_RATING, "7");
        values.put(SportContract.PlayerEntry.COLUMN_EMAIL, "josivaldo@josivaldo.com");
        return values;
    }

    static ContentValues createTeamValues(long adm_id) {
        ContentValues values = new ContentValues();
        int random = (int) Math.floor(Math.random());
        values.put(SportContract.TeamEntry.COLUMN_TEAM_NAME, "Josivaldo Team "+random);
        values.put(SportContract.TeamEntry.COLUMN_ADM_ID, ""+adm_id);
        values.put(SportContract.TeamEntry.COLUMN_CITY, "Josivaldo City");
        values.put(SportContract.TeamEntry.COLUMN_RATING, "7");
        return values;
    }

    static ContentValues createVenueValues() {
        ContentValues values = new ContentValues();
        int random = (int) Math.floor(Math.random());
        values.put(SportContract.VenueEntry.COLUMN_VENUE_NAME, "Josivaldo Team Sports Venue "+random);
        values.put(SportContract.VenueEntry.COLUMN_RATING, "7");
        values.put(SportContract.VenueEntry.COLUMN_LAT_COORD, "-23.189813"); //-23.189813, -45.870086
        values.put(SportContract.VenueEntry.COLUMN_LONG_COORD, "-45.870086");
        values.put(SportContract.VenueEntry.COLUMN_ADDRESS, "28 Staines Rd, Hounslow TW3 3LZ, UK");
        values.put(SportContract.VenueEntry.COLUMN_TELEPHONE, "+44 161 605 8200"); // +55 12 3101 3293 BR +1 423 267 3585 +54 US 9 351 676 3617 ARG
        values.put(SportContract.VenueEntry.COLUMN_EMAIL, "josivaldo_place@josivaldo.com");
        values.put(SportContract.VenueEntry.COLUMN_CITY, "Josivaldo City");

        return values;
    }

    static ContentValues createCommentariesValues(long player_id,long venue_id,  long parent_id) {
        ContentValues values = new ContentValues();
        values.put(SportContract.CommentariesEntry.COLUMN_PLAYER_ID, ""+player_id);
        values.put(SportContract.CommentariesEntry.COLUMN_VENUE_ID, "7");
        values.put(SportContract.CommentariesEntry.COLUMN_COMMENTARY, "Such a wonderplace, best pitch in the city!");
        values.put(SportContract.CommentariesEntry.COLUMN_PARENT_ID, ""+parent_id);
        values.put(SportContract.CommentariesEntry.COLUMN_RATING, "10");

        return values;
    }

    static ContentValues createPlayerTeamValues(long player_id, long team_id) {
        ContentValues values = new ContentValues();
        values.put(SportContract.PlayerTeamEntry.COLUMN_PLAYER_ID, ""+player_id);
        values.put(SportContract.PlayerTeamEntry.COLUMN_TEAM_ID, ""+team_id);
        values.put(SportContract.PlayerTeamEntry.COLUMN_MATCHES, "5");
        values.put(SportContract.PlayerTeamEntry.COLUMN_RATING, "6,5");

        return values;
    }

    static ContentValues createMatchValues(long venue_id, long team_id) {
        ContentValues values = new ContentValues();
        int random = (int) Math.floor(Math.random());
        values.put(SportContract.MatchEntry.COLUMN_MATCH_NAME, "Josivaldo's Match "+random);
        values.put(SportContract.MatchEntry.COLUMN_DATE, "01/01/2017 16:00");
        values.put(SportContract.MatchEntry.COLUMN_VENUE_ID, ""+venue_id);
        values.put(SportContract.MatchEntry.COLUMN_TEAM_ID, ""+team_id);
        values.put(SportContract.MatchEntry.COLUMN_RATING, "6,5");

        return values;
    }

    static ContentValues createFriendsValues(long from_id, long to_id) {
        ContentValues values = new ContentValues();
        int random = (int) Math.floor(Math.random());
        values.put(SportContract.FriendsEntry.COLUMN_FROM, ""+from_id);
        values.put(SportContract.FriendsEntry.COLUMN_TO, ""+to_id);
        values.put(SportContract.FriendsEntry.COLUMN_STATUS, "0");

        return values;
    }
}
