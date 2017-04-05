package com.casasw.sportclub.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Junior on 28/03/2017.
 * Database tests
 */
@RunWith(AndroidJUnit4.class)
public class DbTest {

    public static final String TAG = DbTest.class.getSimpleName();

    void deleteDatabase() {
        InstrumentationRegistry.getContext().deleteDatabase(SportDbHelper.DATABASE_NAME);
    }

    @Before
    public void setUp() throws Exception {
        deleteDatabase();
    }

    @Test
    public void testCreateDb() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(SportContract.PlayerEntry.TABLE_NAME);
        tableNameHashSet.add(SportContract.TeamEntry.TABLE_NAME);
        tableNameHashSet.add(SportContract.VenueEntry.TABLE_NAME);
        tableNameHashSet.add(SportContract.MatchEntry.TABLE_NAME);
        tableNameHashSet.add(SportContract.CommentariesEntry.TABLE_NAME);
        tableNameHashSet.add(SportContract.PlayerTeamEntry.TABLE_NAME);
        tableNameHashSet.add(SportContract.FriendsEntry.TABLE_NAME);

        deleteDatabase();

        SQLiteDatabase db =
                new SportDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        do {
            tableNameHashSet.remove(c.getString(0));
        }while (c.moveToNext());

        assertTrue("Error: Your database was created without tables",
                tableNameHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + SportContract.PlayerEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        final HashSet<String> columnHashSet = new HashSet<String>();
        columnHashSet.add(SportContract.PlayerEntry._ID);
        columnHashSet.add(SportContract.PlayerEntry.COLUMN_PLAYER_NAME);
        columnHashSet.add(SportContract.PlayerEntry.COLUMN_POSITION);
        columnHashSet.add(SportContract.PlayerEntry.COLUMN_HANDEDNESS);
        columnHashSet.add(SportContract.PlayerEntry.COLUMN_AGE);
        columnHashSet.add(SportContract.PlayerEntry.COLUMN_HEIGHT);
        columnHashSet.add(SportContract.PlayerEntry.COLUMN_WEIGHT);
        columnHashSet.add(SportContract.PlayerEntry.COLUMN_CITY);
        columnHashSet.add(SportContract.PlayerEntry.COLUMN_RATING);
        columnHashSet.add(SportContract.PlayerEntry.COLUMN_EMAIL);

        int columnNameIndex = c.getColumnIndex("name");
        String columnName;
        do {
            columnName = c.getString(columnNameIndex);
            columnHashSet.remove(columnName);
        }while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required movie entry columns.",
                columnHashSet.isEmpty());

        c.close();
        db.close();

    }

    @Test
    public void testPlayerTable(){
        SQLiteDatabase db = new SportDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();
        //insertion
        ContentValues values = TestUtilities.createPlayerValues();
        long in = db.insert(SportContract.PlayerEntry.TABLE_NAME, null, values);
        assertTrue("Error: Insertion in player table has fail.", in != -1 );
        Cursor cursor = db.query(SportContract.PlayerEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue("Error: Player table select has fail.",
                cursor.moveToFirst());
        TestUtilities.validateCurrentRecord(
                "Error: The returning cursor is not equals to ContentValues inserted.", cursor, values);

        //update
        values.put(SportContract.PlayerEntry.COLUMN_RATING, "8");
        String sPlayerSelection =
                SportContract.PlayerEntry.TABLE_NAME +
                        "." + SportContract.PlayerEntry._ID + " = ?";
        int updates = db.update(SportContract.PlayerEntry.TABLE_NAME, values, sPlayerSelection, new String[]{""+in});
        assertTrue("Error: Player table update has fail.", updates>0);

        //delete
        int del = db.delete(SportContract.PlayerEntry.TABLE_NAME, sPlayerSelection, new String[]{""+in});
        assertTrue("Error: Player table delete row has fail.", del==1);

        cursor.close();
        db.close();
    }

    @Test
    public void testTeamTable(){
        SQLiteDatabase db = new SportDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();

        ContentValues values = TestUtilities.createPlayerValues();
        db.insert(SportContract.PlayerEntry.TABLE_NAME, null, values);
        Cursor cursor = db.query(SportContract.TeamEntry.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();

        values = TestUtilities.createTeamValues(cursor.getLong(cursor.getColumnIndex(SportContract.PlayerEntry._ID)));
        long in = db.insert(SportContract.TeamEntry.TABLE_NAME, null, values);
        assertTrue("Error: Insertion in team table has fail.", in != -1 );
        cursor = db.query(SportContract.TeamEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue("Error: Team table select has fail.",
                cursor.moveToFirst());

        TestUtilities.validateCurrentRecord(
                "Error: The returning cursor is not equals to ContentValues inserted.", cursor, values);

        //update
        values.put(SportContract.TeamEntry.COLUMN_RATING, "8");
        String sSelection =
                SportContract.TeamEntry.TABLE_NAME +
                        "." + SportContract.TeamEntry._ID + " = ?";
        int updates = db.update(SportContract.TeamEntry.TABLE_NAME, values, sSelection, new String[]{""+in});
        assertTrue("Error: Team table update has fail.", updates>0);

        //delete
        int del = db.delete(SportContract.TeamEntry.TABLE_NAME, sSelection, new String[]{""+in});
        assertTrue("Error: Team table delete row has fail.", del==1);


        cursor.close();
        db.close();
    }

    @Test
    public void testVenueTable(){
        SQLiteDatabase db = new SportDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();
        ContentValues values = TestUtilities.createVenueValues();
        long inVenue = db.insert(SportContract.VenueEntry.TABLE_NAME, null, values);
        assertTrue("Error: Insertion in venue table has fail.", inVenue != -1 );

        Cursor cursor = db.query(SportContract.VenueEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue("Error: Venue table select has fail.",
                cursor.moveToFirst());

        TestUtilities.validateCurrentRecord(
                "Error: The returning cursor is not equals to ContentValues inserted.", cursor, values);

        //update
        values.put(SportContract.VenueEntry.COLUMN_RATING, "8");
        String sSelection =
                SportContract.VenueEntry.TABLE_NAME +
                        "." + SportContract.VenueEntry._ID + " = ?";
        int updates = db.update(SportContract.VenueEntry.TABLE_NAME, values, sSelection, new String[]{""+inVenue});
        assertTrue("Error: Venue table update has fail.", updates>0);

        //delete
        int del = db.delete(SportContract.VenueEntry.TABLE_NAME, sSelection, new String[]{""+inVenue});
        assertTrue("Error: Venue table delete row has fail.", del==1);

        cursor.close();
        db.close();
    }

    @Test
    public void testMatchTable(){
        SQLiteDatabase db = new SportDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();

        ContentValues values = TestUtilities.createPlayerValues();
        long inPlayer = db.insert(SportContract.PlayerEntry.TABLE_NAME, null, values);

        values = TestUtilities.createTeamValues(inPlayer);
        long inTeam = db.insert(SportContract.TeamEntry.TABLE_NAME, null, values);

        values = TestUtilities.createVenueValues();
        long inVenue = db.insert(SportContract.VenueEntry.TABLE_NAME, null, values);

        values = TestUtilities.createMatchValues(inVenue, inTeam);
        long inMatch = db.insert(SportContract.MatchEntry.TABLE_NAME, null, values);
        assertTrue("Error: Insertion in match table has fail.", inMatch != -1 );

        Cursor cursor = db.query(SportContract.MatchEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue("Error: Match table select has fail.",
                cursor.moveToFirst());

        TestUtilities.validateCurrentRecord(
                "Error: The returning cursor is not equals to ContentValues inserted.", cursor, values);

        //update
        values.put(SportContract.MatchEntry.COLUMN_RATING, "8");
        String sSelection =
                SportContract.MatchEntry.TABLE_NAME +
                        "." + SportContract.MatchEntry._ID + " = ?";
        int updates = db.update(SportContract.MatchEntry.TABLE_NAME, values, sSelection, new String[]{""+inMatch});
        assertTrue("Error: Match table update has fail.", updates>0);

        //delete
        int del = db.delete(SportContract.MatchEntry.TABLE_NAME, sSelection, new String[]{""+inMatch});
        assertTrue("Error: Match table delete row has fail.", del==1);

        cursor.close();
        db.close();
    }

    @Test
    public void testCommentariesTable(){
        SQLiteDatabase db = new SportDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();

        ContentValues values = TestUtilities.createPlayerValues();
        long inPlayer = db.insert(SportContract.PlayerEntry.TABLE_NAME, null, values);

        values = TestUtilities.createVenueValues();
        long inVenue = db.insert(SportContract.VenueEntry.TABLE_NAME, null, values);

        values = TestUtilities.createCommentariesValues(inPlayer, inVenue, -1);
        long inComm = db.insert(SportContract.CommentariesEntry.TABLE_NAME, null, values);
        assertTrue("Error: Insertion in commentaries table has fail.", inVenue != -1 );

        Cursor cursor = db.query(SportContract.CommentariesEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue("Error: Commentaries table select has fail.",
                cursor.moveToFirst());

        TestUtilities.validateCurrentRecord(
                "Error: The returning cursor is not equals to ContentValues inserted.", cursor, values);

        //update
        values.put(SportContract.CommentariesEntry.COLUMN_RATING, "8");
        String sSelection =
                SportContract.CommentariesEntry.TABLE_NAME +
                        "." + SportContract.CommentariesEntry._ID + " = ?";
        int updates = db.update(SportContract.CommentariesEntry.TABLE_NAME, values, sSelection, new String[]{""+inComm});
        assertTrue("Error: Commentaries table update has fail.", updates>0);

        //delete
        int del = db.delete(SportContract.CommentariesEntry.TABLE_NAME, sSelection, new String[]{""+inComm});
        assertTrue("Error: Commentaries table delete row has fail.", del==1);

        cursor.close();
        db.close();
    }

    @Test
    public void testPlayerTeamTable(){
        SQLiteDatabase db = new SportDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();

        ContentValues values = TestUtilities.createPlayerValues();
        long inPlayer = db.insert(SportContract.PlayerEntry.TABLE_NAME, null, values);

        values = TestUtilities.createTeamValues(inPlayer);
        long inTeam = db.insert(SportContract.TeamEntry.TABLE_NAME, null, values);

        values = TestUtilities.createPlayerTeamValues(inPlayer, inTeam);
        long in = db.insert(SportContract.PlayerTeamEntry.TABLE_NAME, null, values);

        assertTrue("Error: Insertion in player-team table has fail.", in != -1 );

        Cursor cursor = db.query(SportContract.PlayerTeamEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue("Error: Player Team table select has fail.",
                cursor.moveToFirst());

        TestUtilities.validateCurrentRecord(
                "Error: The returning cursor is not equals to ContentValues inserted.", cursor, values);

        //update
        values.put(SportContract.PlayerTeamEntry.COLUMN_RATING, "8");
        String sSelection =
                SportContract.PlayerTeamEntry.TABLE_NAME +
                        "." + SportContract.PlayerTeamEntry._ID + " = ?";
        int updates = db.update(SportContract.PlayerTeamEntry.TABLE_NAME, values, sSelection, new String[]{""+in});
        assertTrue("Error: Player-Team table update has fail.", updates>0);

        //delete
        int del = db.delete(SportContract.PlayerTeamEntry.TABLE_NAME, sSelection, new String[]{""+in});
        assertTrue("Error: Player-Team table delete row has fail.", del==1);

        cursor.close();
        db.close();
    }

    @Test
    public void testFriendsTable(){
        SQLiteDatabase db = new SportDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();

        ContentValues values = TestUtilities.createPlayerValues();
        long inPlayer1 = db.insert(SportContract.PlayerEntry.TABLE_NAME, null, values);

        values = TestUtilities.createPlayerValues();
        long inPlayer2 = db.insert(SportContract.PlayerEntry.TABLE_NAME, null, values);

        values = TestUtilities.createFriendsValues(inPlayer1, inPlayer2);
        long in = db.insert(SportContract.FriendsEntry.TABLE_NAME, null, values);

        assertTrue("Error: Insertion in friends table has fail.", in != -1 );

        Cursor cursor = db.query(SportContract.FriendsEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue("Error: Friends table select has fail.",
                cursor.moveToFirst());

        TestUtilities.validateCurrentRecord(
                "Error: The returning cursor is not equals to ContentValues inserted.", cursor, values);

        //update
        values.put(SportContract.FriendsEntry.COLUMN_STATUS, "1");
        String sSelection =
                SportContract.FriendsEntry.TABLE_NAME +
                        "." + SportContract.FriendsEntry._ID + " = ?";
        int updates = db.update(SportContract.FriendsEntry.TABLE_NAME, values, sSelection, new String[]{""+in});
        assertTrue("Error: Friends table update has fail.", updates>0);

        //delete
        int del = db.delete(SportContract.FriendsEntry.TABLE_NAME, sSelection, new String[]{""+in});
        assertTrue("Error: Friends table delete row has fail.", del==1);

        cursor.close();
        db.close();
    }


}
