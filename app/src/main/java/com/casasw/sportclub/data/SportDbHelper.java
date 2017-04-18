package com.casasw.sportclub.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.casasw.sportclub.ui.BuildConfig;

/**
 * Created by Junior on 28/03/2017.
 * Manages the local database.
 */

public class SportDbHelper extends SQLiteOpenHelper {
    private static final String TAG = SportDbHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "sportclub.db";

    public SportDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_PLAYER_TABLE = "CREATE TABLE " + SportContract.PlayerEntry.TABLE_NAME + " (" +
                SportContract.PlayerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SportContract.PlayerEntry.COLUMN_PLAYER_NAME + " TEXT NOT NULL, " +
                SportContract.PlayerEntry.COLUMN_POSITION + " TEXT NOT NULL, " +
                SportContract.PlayerEntry.COLUMN_HANDEDNESS + " TEXT NOT NULL, " +
                SportContract.PlayerEntry.COLUMN_AGE + " INTEGER NOT NULL, " +
                SportContract.PlayerEntry.COLUMN_HEIGHT + " REAL NOT NULL, " +
                SportContract.PlayerEntry.COLUMN_WEIGHT + " REAL NOT NULL, " +
                SportContract.PlayerEntry.COLUMN_CITY + " TEXT NOT NULL, " +
                SportContract.PlayerEntry.COLUMN_RATING + " REAL NOT NULL, " +
                SportContract.PlayerEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
                SportContract.PlayerEntry.COLUMN_PROFILE_PHOTO + " TEXT NOT NULL); ";
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate: player table: \n"+SQL_CREATE_PLAYER_TABLE);
        }

        sqLiteDatabase.execSQL(SQL_CREATE_PLAYER_TABLE);

        final String SQL_CREATE_TEAM_TABLE = "CREATE TABLE " + SportContract.TeamEntry.TABLE_NAME + " (" +
                SportContract.TeamEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SportContract.TeamEntry.COLUMN_TEAM_NAME + " TEXT NOT NULL, " +
                SportContract.TeamEntry.COLUMN_ADM_ID + " INTEGER NOT NULL, " +
                SportContract.TeamEntry.COLUMN_CITY + " TEXT NOT NULL, " +
                SportContract.TeamEntry.COLUMN_RATING + " REAL NOT NULL, " +
                "FOREIGN KEY ("+ SportContract.TeamEntry.COLUMN_ADM_ID +") REFERENCES " +
                SportContract.PlayerEntry.TABLE_NAME + " (" + SportContract.PlayerEntry._ID + ") );";
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate: team table: \n"+SQL_CREATE_TEAM_TABLE);
        }

        sqLiteDatabase.execSQL(SQL_CREATE_TEAM_TABLE);

        final String SQL_CREATE_VENUE_TABLE = "CREATE TABLE " + SportContract.VenueEntry.TABLE_NAME + " (" +
                SportContract.VenueEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SportContract.VenueEntry.COLUMN_VENUE_NAME + " TEXT NOT NULL, " +
                SportContract.VenueEntry.COLUMN_RATING + " REAL NOT NULL, " +
                SportContract.VenueEntry.COLUMN_LAT_COORD + " REAL NOT NULL, " +
                SportContract.VenueEntry.COLUMN_LONG_COORD + " REAL NOT NULL, " +
                SportContract.VenueEntry.COLUMN_ADDRESS + " TEXT NOT NULL, " +
                SportContract.VenueEntry.COLUMN_TELEPHONE + " TEXT NOT NULL, " +
                SportContract.VenueEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
                SportContract.VenueEntry.COLUMN_CITY + " TEXT NOT NULL);";
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate: venue table: \n"+SQL_CREATE_VENUE_TABLE);
        }
        sqLiteDatabase.execSQL(SQL_CREATE_VENUE_TABLE);

        final String SQL_CREATE_COMMENTARIES_TABLE = "CREATE TABLE " + SportContract.CommentariesEntry.TABLE_NAME + " (" +
                SportContract.CommentariesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SportContract.CommentariesEntry.COLUMN_PLAYER_ID + " INTEGER NOT NULL, " +
                SportContract.CommentariesEntry.COLUMN_VENUE_ID + " INTEGER NOT NULL, " +
                SportContract.CommentariesEntry.COLUMN_COMMENTARY + " TEXT NOT NULL, " +
                SportContract.CommentariesEntry.COLUMN_PARENT_ID + " INTEGER NOT NULL, " +
                SportContract.CommentariesEntry.COLUMN_RATING + " REAL NOT NULL, " +
                "FOREIGN KEY ("+ SportContract.CommentariesEntry.COLUMN_PLAYER_ID +") REFERENCES " +
                SportContract.PlayerEntry.TABLE_NAME + " (" + SportContract.PlayerEntry._ID +"), " +
                "FOREIGN KEY ("+ SportContract.CommentariesEntry.COLUMN_VENUE_ID +") REFERENCES " +
                SportContract.VenueEntry.TABLE_NAME + " (" + SportContract.VenueEntry._ID +")); ";
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate: commentaries table: \n"+SQL_CREATE_COMMENTARIES_TABLE);
        }
        sqLiteDatabase.execSQL(SQL_CREATE_COMMENTARIES_TABLE);

        final String SQL_CREATE_PLAYER_TEAM_TABLE = "CREATE TABLE " + SportContract.PlayerTeamEntry.TABLE_NAME + " (" +
                SportContract.PlayerTeamEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SportContract.PlayerTeamEntry.COLUMN_PLAYER_ID + " INTEGER NOT NULL, " +
                SportContract.PlayerTeamEntry.COLUMN_TEAM_ID + " INTEGER NOT NULL, " +
                SportContract.PlayerTeamEntry.COLUMN_MATCHES + " TEXT NOT NULL, " +
                SportContract.PlayerTeamEntry.COLUMN_RATING + " REAL NOT NULL, " +
                "FOREIGN KEY ("+ SportContract.PlayerTeamEntry.COLUMN_PLAYER_ID +") REFERENCES " +
                SportContract.PlayerEntry.TABLE_NAME + " (" + SportContract.PlayerEntry._ID +"), " +
                "FOREIGN KEY ("+ SportContract.PlayerTeamEntry.COLUMN_TEAM_ID +") REFERENCES " +
                SportContract.TeamEntry.TABLE_NAME + " (" + SportContract.TeamEntry._ID +")); ";
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate: player team table: \n"+SQL_CREATE_PLAYER_TEAM_TABLE);
        }
        sqLiteDatabase.execSQL(SQL_CREATE_PLAYER_TEAM_TABLE);

        final String SQL_CREATE_MATCH_TABLE = "CREATE TABLE " + SportContract.MatchEntry.TABLE_NAME + " (" +
                SportContract.MatchEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SportContract.MatchEntry.COLUMN_MATCH_NAME + " TEXT NOT NULL, " +
                SportContract.MatchEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                SportContract.MatchEntry.COLUMN_TEAM_ID + " INTEGER NOT NULL, " +
                SportContract.MatchEntry.COLUMN_VENUE_ID + " INTEGER NOT NULL, " +
                SportContract.MatchEntry.COLUMN_RATING + " REAL NOT NULL, " +
                "FOREIGN KEY ("+ SportContract.MatchEntry.COLUMN_TEAM_ID +") REFERENCES " +
                SportContract.TeamEntry.TABLE_NAME + " (" + SportContract.TeamEntry._ID +"), " +
                "FOREIGN KEY ("+ SportContract.MatchEntry.COLUMN_TEAM_ID +") REFERENCES " +
                SportContract.VenueEntry.TABLE_NAME + " (" + SportContract.VenueEntry._ID +")); ";
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate: match table: \n"+SQL_CREATE_MATCH_TABLE);
        }
        sqLiteDatabase.execSQL(SQL_CREATE_MATCH_TABLE);

        final String SQL_CREATE_FRIENDS_TABLE = "CREATE TABLE " + SportContract.FriendsEntry.TABLE_NAME + " (" +
                SportContract.FriendsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SportContract.FriendsEntry.COLUMN_FROM + " INTEGER NOT NULL, " +
                SportContract.FriendsEntry.COLUMN_TO+ " INTEGER NOT NULL, " +
                SportContract.FriendsEntry.COLUMN_STATUS + " INTEGER NOT NULL, " +
                "FOREIGN KEY ("+ SportContract.FriendsEntry.COLUMN_FROM +") REFERENCES " +
                SportContract.PlayerEntry.TABLE_NAME + " (" + SportContract.PlayerEntry._ID +"), " +
                "FOREIGN KEY ("+ SportContract.FriendsEntry.COLUMN_TO +") REFERENCES " +
                SportContract.PlayerEntry.TABLE_NAME + " (" + SportContract.PlayerEntry._ID +")); ";
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate: friends table: \n"+SQL_CREATE_FRIENDS_TABLE);
        }
        sqLiteDatabase.execSQL(SQL_CREATE_FRIENDS_TABLE);

        final String SQL_CREATE_ATTRIBUTES_TABLE = "CREATE TABLE " + SportContract.AttributesEntry.TABLE_NAME + " (" +
                SportContract.AttributesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SportContract.AttributesEntry.COLUMN_PLAYER_ID + " INTEGER NOT NULL, " +
                SportContract.AttributesEntry.COLUMN_SPEED + " REAL NOT NULL, " +
                SportContract.AttributesEntry.COLUMN_POWER+ " REAL NOT NULL, " +
                SportContract.AttributesEntry.COLUMN_TECHNIQUE + " REAL NOT NULL, " +
                SportContract.AttributesEntry.COLUMN_FITNESS + " REAL NOT NULL, " +
                SportContract.AttributesEntry.COLUMN_FAIR_PLAY + " REAL NOT NULL, " +
                "FOREIGN KEY ("+ SportContract.AttributesEntry.COLUMN_PLAYER_ID +") REFERENCES " +
                SportContract.PlayerEntry.TABLE_NAME + " (" + SportContract.PlayerEntry._ID +")); ";
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate: attributes table: \n"+SQL_CREATE_ATTRIBUTES_TABLE);
        }
        sqLiteDatabase.execSQL(SQL_CREATE_ATTRIBUTES_TABLE);

        final String SQL_CREATE_PHOTOS_TABLE = "CREATE TABLE " + SportContract.PhotosEntry.TABLE_NAME + " (" +
                SportContract.PhotosEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SportContract.PhotosEntry.COLUMN_VENUE_ID + " INTEGER NOT NULL, " +
                SportContract.PhotosEntry.COLUMN_URL + " TEXT NOT NULL, " +
                "FOREIGN KEY ("+ SportContract.PhotosEntry.COLUMN_VENUE_ID +") REFERENCES " +
                SportContract.VenueEntry.TABLE_NAME + " (" + SportContract.VenueEntry._ID +")); ";
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate: photos table: \n"+SQL_CREATE_PHOTOS_TABLE);
        }
        sqLiteDatabase.execSQL(SQL_CREATE_PHOTOS_TABLE);
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //this db is personalized cache for online data, so on upgrade we'll need user id
        //to restore only necessary data.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SportContract.PlayerEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SportContract.TeamEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SportContract.VenueEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SportContract.CommentariesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SportContract.PlayerTeamEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SportContract.MatchEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SportContract.FriendsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SportContract.AttributesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SportContract.PhotosEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
