package com.casasw.sportclub.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

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

    private static final String TAG = DbTest.class.getSimpleName();

    private void deleteDatabase() {
        InstrumentationRegistry.getTargetContext().deleteDatabase(SportDbHelper.DATABASE_NAME);
    }

    @Before
    public void setUp() throws Exception {
        deleteDatabase();
    }

    @Test
    public void testCreateDb() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(SportContract.PlayerEntry.TABLE_NAME);
        tableNameHashSet.add(SportContract.TeamEntry.TABLE_NAME);
        tableNameHashSet.add(SportContract.VenueEntry.TABLE_NAME);
        tableNameHashSet.add(SportContract.MatchEntry.TABLE_NAME);
        tableNameHashSet.add(SportContract.CommentariesEntry.TABLE_NAME);
        tableNameHashSet.add(SportContract.PlayerTeamEntry.TABLE_NAME);
        tableNameHashSet.add(SportContract.FriendsEntry.TABLE_NAME);
        tableNameHashSet.add(SportContract.SportsEntry.TABLE_NAME);
        tableNameHashSet.add(SportContract.PlayerSportEntry.TABLE_NAME);
        tableNameHashSet.add(SportContract.VenueSportEntry.TABLE_NAME);

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

        assertTrue("Error: Your database was created without all tables",
                tableNameHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + SportContract.PlayerEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        final HashSet<String> columnHashSet = new HashSet<>();
        columnHashSet.add(SportContract.PlayerEntry._ID);
        columnHashSet.add(SportContract.PlayerEntry.COLUMN_PLAYER_NAME);
        columnHashSet.add(SportContract.PlayerEntry.COLUMN_POSITION);
        columnHashSet.add(SportContract.PlayerEntry.COLUMN_HANDEDNESS);
        columnHashSet.add(SportContract.PlayerEntry.COLUMN_BDAY);
        columnHashSet.add(SportContract.PlayerEntry.COLUMN_HEIGHT);
        columnHashSet.add(SportContract.PlayerEntry.COLUMN_WEIGHT);
        columnHashSet.add(SportContract.PlayerEntry.COLUMN_CITY);
        columnHashSet.add(SportContract.PlayerEntry.COLUMN_STATE);
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
        assertTrue("Error: Player table update has fail.", updates==1);

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
        long inPlayer = db.insert(SportContract.PlayerEntry.TABLE_NAME, null, values);

        values = TestUtilities.createTeamValues(inPlayer);
        long in = db.insert(SportContract.TeamEntry.TABLE_NAME, null, values);
        assertTrue("Error: Insertion in team table has fail.", in != -1 );
        Cursor cursor = db.query(SportContract.TeamEntry.TABLE_NAME, null, null, null, null, null, null);
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
        assertTrue("Error: Team table update has fail.", updates==1);

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
        String sSelection =
                SportContract.VenueEntry.TABLE_NAME +
                        "." + SportContract.VenueEntry._ID + " = ?";
        Cursor cursor = db.query(SportContract.VenueEntry.TABLE_NAME, null, sSelection, new String[]{""+inVenue}, null, null, null);
        assertTrue("Error: Venue table select has fail.",
                cursor.moveToFirst());

        TestUtilities.validateCurrentRecord(
                "Error: The returning cursor is not equals to ContentValues inserted.", cursor, values);

        //update
        values.put(SportContract.VenueEntry.COLUMN_RATING, "8");

        int updates = db.update(SportContract.VenueEntry.TABLE_NAME, values, sSelection, new String[]{""+inVenue});
        assertTrue("Error: Venue table update has fail.", updates==1);

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
        assertTrue("Error: Match table update has fail.", updates==1);

        //delete
        int del = db.delete(SportContract.MatchEntry.TABLE_NAME, sSelection, new String[]{""+inMatch});
        assertTrue("Error: Match table delete row has fail.", del==1);

        cursor.close();
        db.close();
    }

    @Test
    public void testSportTable(){
        SQLiteDatabase db = new SportDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();
        //insertion
        ContentValues values = TestUtilities.createSportValues();
        long in = db.insert(SportContract.SportsEntry.TABLE_NAME, null, values);
        assertTrue("Error: Insertion in sport table has fail.", in != -1 );
        String sSportSelection =
                SportContract.SportsEntry.TABLE_NAME +
                        "." + SportContract.SportsEntry._ID + " = ?";
        Cursor cursor = db.query(SportContract.SportsEntry.TABLE_NAME, null, sSportSelection, new String[]{""+in}, null, null, null);
        assertTrue("Error: Sport table select has fail.",
                cursor.moveToFirst());
        TestUtilities.validateCurrentRecord(
                "Error: The returning cursor is not equals to ContentValues inserted.", cursor, values);

        //update
        values.put(SportContract.SportsEntry.COLUMN_NAME, "Soccer");

        int updates = db.update(SportContract.SportsEntry.TABLE_NAME, values, sSportSelection, new String[]{""+in});
        assertTrue("Error: Sport table update has fail.", updates==1);

        //delete
        int del = db.delete(SportContract.SportsEntry.TABLE_NAME, sSportSelection, new String[]{""+in});
        assertTrue("Error: Sport table delete row has fail.", del==1);

        cursor.close();
        db.close();
    }

    @Test
    public void testPlayerSportTable(){
        SQLiteDatabase db = new SportDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();
        //insertion
        ContentValues values = TestUtilities.createPlayerValues();
        long in_player = db.insert(SportContract.PlayerEntry.TABLE_NAME, null, values);


        Cursor c  = db.query(SportContract.SportsEntry.TABLE_NAME, null, SportContract.SportsEntry.COLUMN_NAME + " = ?", new String[]{"Soccer"}, null, null, null);
        assertTrue("Sport soccer not found.", c.moveToFirst());
        long in_sport = c.getLong(c.getColumnIndex(SportContract.SportsEntry._ID));
        c.close();

        values = TestUtilities.createPlayerSportValues(in_player, in_sport);
        long in = db.insert(SportContract.PlayerSportEntry.TABLE_NAME, null, values);
        assertTrue("Error: Insertion in Player-Sport table has fail.", in != -1 );
        Cursor cursor = db.query(SportContract.PlayerSportEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue("Error: PlayerSport table select has fail.",
                cursor.moveToFirst());
        TestUtilities.validateCurrentRecord(
                "Error: The returning cursor is not equals to ContentValues inserted.", cursor, values);

        //update
        values.put(SportContract.PlayerSportEntry.COLUMN_PLAYER_ID, in_player);
        String sPlayerSportSelection =
                SportContract.PlayerSportEntry.TABLE_NAME +
                        "." + SportContract.PlayerSportEntry._ID + " = ?";
        int updates = db.update(SportContract.PlayerSportEntry.TABLE_NAME, values, sPlayerSportSelection, new String[]{""+in});
        assertTrue("Error: PlayerSport table update has fail.", updates==1);

        //delete
        int del = db.delete(SportContract.PlayerSportEntry.TABLE_NAME, sPlayerSportSelection, new String[]{""+in});
        assertTrue("Error: PlayerSport table delete row has fail.", del==1);

        cursor.close();
        db.close();
    }

    @Test
    public void testVenueSportTable(){
        SQLiteDatabase db = new SportDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();
        //insertion
        ContentValues values = TestUtilities.createVenueValues();
        long in_venue = db.insert(SportContract.VenueEntry.TABLE_NAME, null, values);
        values = TestUtilities.createSportValues();
        long in_sport = db.insert(SportContract.SportsEntry.TABLE_NAME, null, values);

        values = TestUtilities.createVenueSportValues(in_venue, in_sport);
        long in = db.insert(SportContract.VenueSportEntry.TABLE_NAME, null, values);
        assertTrue("Error: Insertion in Venue-Sport table has fail.", in != -1 );
        Cursor cursor = db.query(SportContract.VenueSportEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue("Error: VenueSport table select has fail.",
                cursor.moveToFirst());
        TestUtilities.validateCurrentRecord(
                "Error: The returning cursor is not equals to ContentValues inserted.", cursor, values);

        //update
        values.put(SportContract.VenueSportEntry.COLUMN_VENUE_ID, in_venue);
        String sVenueSportSelection =
                SportContract.VenueSportEntry.TABLE_NAME +
                        "." + SportContract.VenueSportEntry._ID + " = ?";
        int updates = db.update(SportContract.VenueSportEntry.TABLE_NAME, values, sVenueSportSelection, new String[]{""+in});
        assertTrue("Error: VenueSport table update has fail.", updates==1);

        //delete
        int del = db.delete(SportContract.VenueSportEntry.TABLE_NAME, sVenueSportSelection, new String[]{""+in});
        assertTrue("Error: VenueSport table delete row has fail.", del==1);

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
        assertTrue("Error: Insertion in commentaries table has fail.", inComm != -1 );

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
        assertTrue("Error: Commentaries table update has fail.", updates==1);

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
        assertTrue("Error: Player-Team table update has fail.", updates==1);

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
        assertTrue("Error: Friends table update has fail.", updates==1);

        //delete
        int del = db.delete(SportContract.FriendsEntry.TABLE_NAME, sSelection, new String[]{""+in});
        assertTrue("Error: Friends table delete row has fail.", del==1);

        cursor.close();
        db.close();
    }

    @Test
    public void testAttributesTable(){
        SQLiteDatabase db = new SportDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();

        ContentValues values = TestUtilities.createPlayerValues();
        long inPlayer = db.insert(SportContract.PlayerEntry.TABLE_NAME, null, values);

        values = TestUtilities.createAttributesValues(inPlayer);
        long in = db.insert(SportContract.AttributesEntry.TABLE_NAME, null, values);
        assertTrue("Error: Insertion in Attributes table has fail.", in != -1 );

        Cursor cursor = db.query(SportContract.AttributesEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue("Error: Attributes table select has fail.",
                cursor.moveToFirst());

        TestUtilities.validateCurrentRecord(
                "Error: The returning cursor is not equals to ContentValues inserted.", cursor, values);

        values.put(SportContract.AttributesEntry.COLUMN_POWER, "1");
        String sSelection =
                SportContract.AttributesEntry.TABLE_NAME +
                        "." + SportContract.AttributesEntry._ID + " = ?";
        int updates = db.update(SportContract.AttributesEntry.TABLE_NAME, values, sSelection, new String[]{""+in});
        assertTrue("Error: Attributes table update has fail.", updates==1);

        //delete
        int del = db.delete(SportContract.AttributesEntry.TABLE_NAME, sSelection, new String[]{""+in});
        assertTrue("Error: Attributes table delete row has fail.", del==1);

        cursor.close();
        db.close();
    }

    @Test
    public void testInnerJoin(){
        SQLiteDatabase db = new SportDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();

        ContentValues values = TestUtilities.createPlayerValues();
        long inPlayer = db.insert(SportContract.PlayerEntry.TABLE_NAME, null, values);

        values = TestUtilities.createPlayerValues();
        long inPlayer2 = db.insert(SportContract.PlayerEntry.TABLE_NAME, null, values);

        values = TestUtilities.createFriendsValues(inPlayer, inPlayer2);
        long inFriends = db.insert(SportContract.FriendsEntry.TABLE_NAME, null, values);

        values = TestUtilities.createTeamValues(inPlayer);
        long inTeam = db.insert(SportContract.TeamEntry.TABLE_NAME, null, values);

        values = TestUtilities.createVenueValues();
        long inVenue = db.insert(SportContract.VenueEntry.TABLE_NAME, null, values);

        values = TestUtilities.createSportValues();
        long inSport = db.insert(SportContract.SportsEntry.TABLE_NAME, null, values);
        values.put(SportContract.SportsEntry.COLUMN_NAME, "Baseball");
        long inSport2 = db.insert(SportContract.SportsEntry.TABLE_NAME, null, values);

        values = TestUtilities.createMatchValues(inVenue, inTeam);
        long inMatch = db.insert(SportContract.MatchEntry.TABLE_NAME, null, values);

        values = TestUtilities.createCommentariesValues(inPlayer, inVenue, -1);
        long inComm = db.insert(SportContract.CommentariesEntry.TABLE_NAME, null, values);

        values = TestUtilities.createPlayerTeamValues(inPlayer, inTeam);
        long inPlayerTeam = db.insert(SportContract.PlayerTeamEntry.TABLE_NAME, null, values);

        values = TestUtilities.createPlayerSportValues(inPlayer, inSport);
        long inPlayerSport = db.insert(SportContract.PlayerSportEntry.TABLE_NAME, null, values);
        values = TestUtilities.createPlayerSportValues(inPlayer, inSport2);
        long inPlayerSport2 = db.insert(SportContract.PlayerSportEntry.TABLE_NAME, null, values);
        values = TestUtilities.createVenueSportValues(inVenue, inSport);
        long inVenueSport = db.insert(SportContract.VenueSportEntry.TABLE_NAME, null, values);

        values = TestUtilities.createAttributesValues(inPlayer);
        long inAttr = db.insert(SportContract.AttributesEntry.TABLE_NAME, null, values);

        values = TestUtilities.createPhotosValues(inVenue);
        long inPhoto = db.insert(SportContract.PhotosEntry.TABLE_NAME, null, values);

        /*jogador, time e jogador_time*/
        SQLiteQueryBuilder sPlayerTeamQueryBuilder = new SQLiteQueryBuilder();
        sPlayerTeamQueryBuilder.setTables(
                SportContract.PlayerEntry.TABLE_NAME + " INNER JOIN " +
                        SportContract.TeamEntry.TABLE_NAME +
                        " ON " + SportContract.PlayerEntry.TABLE_NAME +
                        "." + SportContract.PlayerEntry._ID +
                        " = " + SportContract.TeamEntry.COLUMN_ADM_ID +
                        " INNER JOIN " +
                        SportContract.PlayerTeamEntry.TABLE_NAME +
                        " ON " + SportContract.PlayerTeamEntry.COLUMN_TEAM_ID +
                        " = " + SportContract.TeamEntry.TABLE_NAME +
                        "." + SportContract.TeamEntry._ID +
                        " INNER JOIN " +
                        SportContract.AttributesEntry.TABLE_NAME +
                        " ON " + SportContract.AttributesEntry.TABLE_NAME +
                        "." + SportContract.AttributesEntry.COLUMN_PLAYER_ID +
                        " = " + SportContract.PlayerTeamEntry.TABLE_NAME +
                        "." + SportContract.PlayerTeamEntry.COLUMN_PLAYER_ID

        );
        String selection = SportContract.PlayerEntry.TABLE_NAME +
                "." + SportContract.PlayerEntry._ID + " = ? ";
        String[] selectionArgs = new String[]{""+inPlayer};
        Cursor c = sPlayerTeamQueryBuilder.query(
                db, TestUtilities.PLAYER_TEAM_COLUMNS, selection, selectionArgs,
                null, null, null
        );
        assertTrue("Error: Player Team inner join is returning no data.", c.moveToFirst());
        TestUtilities.logCursor(c, TAG);

        /* local, jogador, comentarios_local*/
        SQLiteQueryBuilder sVenuePlayerCommentaries = new SQLiteQueryBuilder();
        sVenuePlayerCommentaries.setTables(
                SportContract.VenueEntry.TABLE_NAME + " INNER JOIN " +
                        SportContract.CommentariesEntry.TABLE_NAME +
                        " ON " + SportContract.VenueEntry.TABLE_NAME +
                        "." + SportContract.VenueEntry._ID +
                        " = " + SportContract.CommentariesEntry.TABLE_NAME +
                        "." + SportContract.CommentariesEntry.COLUMN_VENUE_ID +
                        " INNER JOIN " +
                        SportContract.PlayerEntry.TABLE_NAME +
                        " ON " + SportContract.CommentariesEntry.COLUMN_PLAYER_ID +
                        " = " + SportContract.PlayerEntry.TABLE_NAME +
                        "." + SportContract.PlayerEntry._ID +
                        " INNER JOIN " +
                        SportContract.PhotosEntry.TABLE_NAME +
                        " ON " + SportContract.VenueEntry.TABLE_NAME +
                        "." + SportContract.VenueEntry._ID +
                        " = " + SportContract.PhotosEntry.TABLE_NAME +
                        "." + SportContract.PhotosEntry.COLUMN_VENUE_ID
        );
        selection = SportContract.VenueEntry.TABLE_NAME +
                "." + SportContract.VenueEntry._ID + " = ?";
        selectionArgs = new String[]{""+inVenue};
        c = sVenuePlayerCommentaries.query(
                db, TestUtilities.VENUE_PLAYER_COMMENTARIES_PHOTOS, selection, selectionArgs,
                null, null, null
        );
        assertTrue("Error: Venue Commentaries inner join is returning no data.", c.moveToFirst());
        TestUtilities.logCursor(c, TAG);

        /* jogador, atributo, jogador_esporte, esporte   */
        SQLiteQueryBuilder sPlayerSportAttributes = new SQLiteQueryBuilder();
        sPlayerSportAttributes.setTables(
                SportContract.PlayerEntry.TABLE_NAME + " INNER JOIN " +
                        SportContract.AttributesEntry.TABLE_NAME +
                        " ON " + SportContract.PlayerEntry.TABLE_NAME +
                        "." + SportContract.PlayerEntry._ID +
                        " = " + SportContract.AttributesEntry.TABLE_NAME +
                        "." + SportContract.AttributesEntry.COLUMN_PLAYER_ID +
                        " INNER JOIN " +
                        SportContract.PlayerSportEntry.TABLE_NAME +
                        " ON " + SportContract.PlayerEntry.TABLE_NAME +
                        "." + SportContract.PlayerEntry._ID +
                        " = " + SportContract.PlayerSportEntry.TABLE_NAME +
                        "." + SportContract.PlayerSportEntry.COLUMN_PLAYER_ID +
                        " INNER JOIN " +
                        SportContract.SportsEntry.TABLE_NAME +
                        " ON " + SportContract.PlayerSportEntry.TABLE_NAME +
                        "." + SportContract.PlayerSportEntry.COLUMN_SPORT_ID +
                        " = " + SportContract.SportsEntry.TABLE_NAME +
                        "." + SportContract.SportsEntry._ID


        );
        Log.d(TAG, "testInnerJoin: Player Sport Attributes inner join: "+sPlayerSportAttributes.getTables());
        selection = SportContract.PlayerEntry.TABLE_NAME +
                "." + SportContract.PlayerEntry._ID + " = ?";
        Log.d(TAG, "testInnerJoin: Player Sport Attributes inner join selection: "+selection);
        selectionArgs = new String[]{""+inPlayer};
        Log.d(TAG, "testInnerJoin: Player Sport Attributes inner join selection args: " + selectionArgs[0]);
        c = sPlayerSportAttributes.query(
                db, TestUtilities.PLAYER_SPORTS_ATTRIBUTES, selection, selectionArgs,
                null, null, null
        );
        assertTrue("Error: Player attributes friends inner join is returning no data.", c.moveToFirst());
        Log.d(TAG, "testInnerJoin: Jogador, atributo e amigo");
        TestUtilities.logCursor(c, TAG);

        /*Partida, local e time*/
        SQLiteQueryBuilder sMatchVenueTeam = new SQLiteQueryBuilder();
        sMatchVenueTeam.setTables(
                SportContract.MatchEntry.TABLE_NAME + " INNER JOIN "+
                        SportContract.VenueEntry.TABLE_NAME +
                        " ON " + SportContract.MatchEntry.TABLE_NAME +
                        "." + SportContract.MatchEntry.COLUMN_VENUE_ID +
                        " = " + SportContract.VenueEntry.TABLE_NAME +
                        "." + SportContract.VenueEntry._ID +
                        " INNER JOIN " +
                        SportContract.TeamEntry.TABLE_NAME +
                        " ON " + SportContract.MatchEntry.TABLE_NAME +
                        "." + SportContract.MatchEntry.COLUMN_TEAM_ID +
                        " = " + SportContract.TeamEntry.TABLE_NAME +
                        "." + SportContract.TeamEntry._ID
        );
        selection = SportContract.MatchEntry.TABLE_NAME +
                "." + SportContract.MatchEntry._ID + " = ?";
        selectionArgs = new String[]{""+inMatch};
        c = sMatchVenueTeam.query(
                db, TestUtilities.MATCH_VENUE_TEAM, selection, selectionArgs,
                null, null, null
        );
        assertTrue("Error: Match Venue Team inner join is returning no data.", c.moveToFirst());

        TestUtilities.logCursor(c, TAG);

        c.close();
        db.close();
    }

    @Test
    public void testPlayerSportInnerJoin(){

    }
}
