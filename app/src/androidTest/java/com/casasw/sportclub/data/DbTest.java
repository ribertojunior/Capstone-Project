package com.casasw.sportclub.data;

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
    public void testPlayerTable(){}
    public void testTeamTable(){}
    public void testMatchTable(){}
    public void testVenueTable(){}
    public void testCommentariesTable(){}
    public void testPlayerTeamTable(){}
    public void testFriendsTable(){}


}
