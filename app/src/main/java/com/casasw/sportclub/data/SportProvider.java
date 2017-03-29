package com.casasw.sportclub.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.casasw.sportclub.ui.BuildConfig;

import static com.casasw.sportclub.data.SportContract.PlayerEntry.buildPlayerUri;

/**
 * Created by Junior on 28/03/2017.
 * Sport Club Db content provider.
 */

public class SportProvider extends ContentProvider {

    public static final String TAG = SportProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private SportDbHelper mOpenHelper;

    static final int PLAYER = 100;
    static final int PLAYER_ID = 101;
    static final int TEAM = 200;
    static final int TEAM_ID = 201;
    static final int MATCH = 300;
    static final int MATCH_ID = 301;
    static final int VENUE = 400;
    static final int VENUE_ID = 401;
    static final int PLAYER_TEAM = 500;
    static final int PLAYER_TEAM_TEAM_ID = 501;
    static final int COMMENTARIES = 600;
    static final int COMMENTARIES_VENUE_ID = 601;
    static final int COMMENTARIES_PARENT_ID = 602;
    static final int FRIEND = 700;
    static final int FRIEND_FT_ID = 701;

    //this is still on debate regarding its existence
    private static final SQLiteQueryBuilder sTeamPlayersQueryBuilder;
    static {
        sTeamPlayersQueryBuilder = new SQLiteQueryBuilder();

        //inner join
        //TEAM inner join player_team inner join player
        sTeamPlayersQueryBuilder.setTables(
                SportContract.TeamEntry.TABLE_NAME + " INNER JOIN " +
                        SportContract.PlayerTeamEntry.TABLE_NAME +
                        " ON " + SportContract.TeamEntry.TABLE_NAME +
                        "." + SportContract.TeamEntry._ID +
                        " = " + SportContract.PlayerTeamEntry.TABLE_NAME +
                        "." + SportContract.PlayerTeamEntry.COLUMN_TEAM_ID +
                        " INNER JOIN " +
                        SportContract.PlayerEntry.TABLE_NAME +
                        " ON " + SportContract.PlayerEntry.TABLE_NAME +
                        "." + SportContract.PlayerEntry._ID +
                        " = " + SportContract.PlayerTeamEntry.TABLE_NAME +
                        "." + SportContract.PlayerTeamEntry.COLUMN_PLAYER_ID
        );
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "static initializer: Team Player Query\n "+sTeamPlayersQueryBuilder.getTables());
        }
    }

    //this is still on debate regarding its existence
    private static final SQLiteQueryBuilder sCommentariesQueryBuilder;
    static {
        sCommentariesQueryBuilder = new SQLiteQueryBuilder();

        //inner join
        //venue inner join commentaries inner join player
        sCommentariesQueryBuilder.setTables(
                SportContract.VenueEntry.TABLE_NAME + " INNER JOIN " +
                        SportContract.CommentariesEntry.TABLE_NAME +
                        " ON " + SportContract.VenueEntry.TABLE_NAME +
                        "." + SportContract.VenueEntry._ID +
                        " = " + SportContract.CommentariesEntry.TABLE_NAME +
                        "." + SportContract.CommentariesEntry.COLUMN_VENUE_ID+
                        " INNER JOIN " +
                        SportContract.PlayerEntry.TABLE_NAME +
                        " ON " + SportContract.PlayerEntry.TABLE_NAME +
                        "." + SportContract.PlayerEntry._ID +
                        " = " + SportContract.CommentariesEntry.TABLE_NAME +
                        "." + SportContract.CommentariesEntry.COLUMN_PLAYER_ID
        );
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "static initializer: Commentaries Query\n "+sCommentariesQueryBuilder.getTables());
        }
    }


    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        /*still on analyze for more matches*/
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_PLAYER, PLAYER);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_PLAYER+"/*", PLAYER_ID);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_TEAM, TEAM);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_TEAM+"/*", TEAM_ID);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_MATCH, MATCH);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_MATCH+"/*", MATCH_ID);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_VENUE, VENUE);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_VENUE+"/*", VENUE_ID);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_PLAYER_TEAM, PLAYER_TEAM);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_PLAYER_TEAM+"/*", PLAYER_TEAM_TEAM_ID);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_COMMENTARIES, COMMENTARIES);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_COMMENTARIES+"/*", COMMENTARIES_VENUE_ID);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_COMMENTARIES+"/*/*", COMMENTARIES_PARENT_ID);
        //to = ? or from = ?
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_FRIENDS, FRIEND);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_FRIENDS+"/*", FRIEND_FT_ID);

        return uriMatcher;
    }

    private static final String sPlayerSelection =
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry._ID + " = ?";
    private static final String sTeamSelection =
            SportContract.TeamEntry.TABLE_NAME +
                    "." + SportContract.TeamEntry._ID + " = ?";
    private static final String sMatchSelection =
            SportContract.MatchEntry.TABLE_NAME +
                    "." + SportContract.MatchEntry._ID + " = ?";
    private static final String sVenueSelection =
            SportContract.VenueEntry.TABLE_NAME +
                    "." + SportContract.VenueEntry._ID + " = ?";
    private static final String sPlayerTeamSelection =
            SportContract.PlayerTeamEntry.TABLE_NAME +
                    "." + SportContract.PlayerTeamEntry.COLUMN_TEAM_ID+ " = ?";
    private static final String sCommentariesSelection =
            SportContract.CommentariesEntry.TABLE_NAME +
                    "." + SportContract.CommentariesEntry.COLUMN_VENUE_ID+ " = ?";
    private static final String sCommentariesByParentSelection =
            SportContract.CommentariesEntry.TABLE_NAME +
                    "." + SportContract.CommentariesEntry.COLUMN_PARENT_ID+ " = ?";
    private static final String sFriendsSelection =
            SportContract.FriendsEntry.TABLE_NAME +
                    "." + SportContract.FriendsEntry.COLUMN_FROM+ " = ? OR "+
                    SportContract.FriendsEntry.TABLE_NAME +
                    "." + SportContract.FriendsEntry.COLUMN_TO+ " = ?";


    @Override
    public boolean onCreate() {
        mOpenHelper = new SportDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)){
            case PLAYER:
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.PlayerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PLAYER_ID:
                selectionArgs = new String[]{SportContract.PlayerEntry.getPlayerIdFromUri(uri)};
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.PlayerEntry.TABLE_NAME,
                        projection,
                        sPlayerSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TEAM:
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.TeamEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TEAM_ID:
                selectionArgs = new String[]{SportContract.TeamEntry.getTeamIdFromUri(uri)};
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.TeamEntry.TABLE_NAME,
                        projection,
                        sTeamSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MATCH:
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.MatchEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MATCH_ID:
                selectionArgs = new String[]{SportContract.MatchEntry.getIdFromUri(uri)};
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.MatchEntry.TABLE_NAME,
                        projection,
                        sMatchSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case VENUE:
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.VenueEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case VENUE_ID:
                selectionArgs = new String[]{SportContract.VenueEntry.getVenueIdFromUri(uri)};
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.VenueEntry.TABLE_NAME,
                        projection,
                        sVenueSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PLAYER_TEAM:
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.PlayerTeamEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PLAYER_TEAM_TEAM_ID:
                selectionArgs = new String[]{SportContract.PlayerTeamEntry.getIdFromUri(uri)};
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.PlayerTeamEntry.TABLE_NAME,
                        projection,
                        sPlayerTeamSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case COMMENTARIES:
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.CommentariesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case COMMENTARIES_VENUE_ID:
                selectionArgs = new String[]{SportContract.CommentariesEntry.getIdFromUri(uri)};
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.CommentariesEntry.TABLE_NAME,
                        projection,
                        sCommentariesSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case COMMENTARIES_PARENT_ID:
                selectionArgs = new String[]{SportContract.CommentariesEntry.getIdFromUri(uri)};
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.CommentariesEntry.TABLE_NAME,
                        projection,
                        sCommentariesByParentSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FRIEND:
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.FriendsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FRIEND_FT_ID:
                selectionArgs = new String[]{SportContract.FriendsEntry.getIdFromUri(uri)};
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.FriendsEntry.TABLE_NAME,
                        projection,
                        sFriendsSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default: throw new UnsupportedOperationException(TAG +" - Unknown uri: "+ uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PLAYER:
                return SportContract.PlayerEntry.CONTENT_TYPE;
            case PLAYER_ID:
                return SportContract.PlayerEntry.CONTENT_ITEM_TYPE;
            case TEAM:
                return SportContract.TeamEntry.CONTENT_TYPE;
            case TEAM_ID:
                return SportContract.TeamEntry.CONTENT_ITEM_TYPE;
            case MATCH:
                return SportContract.MatchEntry.CONTENT_TYPE;
            case MATCH_ID:
                return SportContract.MatchEntry.CONTENT_ITEM_TYPE;
            case VENUE:
                return SportContract.VenueEntry.CONTENT_TYPE;
            case VENUE_ID:
                return SportContract.VenueEntry.CONTENT_ITEM_TYPE;
            case PLAYER_TEAM:
                return SportContract.PlayerTeamEntry.CONTENT_TYPE;
            case PLAYER_TEAM_TEAM_ID:
                return SportContract.PlayerTeamEntry.CONTENT_ITEM_TYPE;
            case COMMENTARIES:
                return SportContract.CommentariesEntry.CONTENT_TYPE;
            case COMMENTARIES_VENUE_ID:
                return SportContract.CommentariesEntry.CONTENT_ITEM_TYPE;
            case COMMENTARIES_PARENT_ID:
                return SportContract.CommentariesEntry.CONTENT_ITEM_TYPE;
            case FRIEND:
                return SportContract.FriendsEntry.CONTENT_TYPE;
            case FRIEND_FT_ID:
                return SportContract.FriendsEntry.CONTENT_ITEM_TYPE;
            default: throw new UnsupportedOperationException(TAG +" - Unknown uri: "+ uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri uriReturn = null;
        long _id;
        switch (match) {
            case PLAYER:
                _id = db.insert(SportContract.PlayerEntry.TABLE_NAME, null, contentValues);
                if (_id >0)
                    uriReturn = buildPlayerUri(_id);
                else
                    throw new android.database.SQLException(TAG + " Failed to insert row into " + uri);
                break;

            case TEAM:
                _id = db.insert(SportContract.TeamEntry.TABLE_NAME, null, contentValues);
                if (_id >0)
                    uriReturn = SportContract.TeamEntry.buildTeamUri(_id);
                else
                    throw new android.database.SQLException(TAG + " Failed to insert row into " + uri);
                break;
            case MATCH:
                _id = db.insert(SportContract.MatchEntry.TABLE_NAME, null, contentValues);
                if (_id >0)
                    uriReturn = SportContract.MatchEntry.buildMatchUri(_id);
                else
                    throw new android.database.SQLException(TAG + " Failed to insert row into " + uri);
                break;
            case VENUE:
                _id = db.insert(SportContract.VenueEntry.TABLE_NAME, null, contentValues);
                if (_id >0)
                    uriReturn = SportContract.VenueEntry.buildVenueUri(_id);
                else
                    throw new android.database.SQLException(TAG + " Failed to insert row into " + uri);
                break;
            case PLAYER_TEAM:
                _id = db.insert(SportContract.PlayerTeamEntry.TABLE_NAME, null, contentValues);
                if (_id >0)
                    uriReturn = SportContract.PlayerTeamEntry.buildPlayerTeamUri(_id);
                else
                    throw new android.database.SQLException(TAG + " Failed to insert row into " + uri);
                break;

            case COMMENTARIES:
                _id = db.insert(SportContract.CommentariesEntry.TABLE_NAME, null, contentValues);
                if (_id >0)
                    uriReturn = SportContract.CommentariesEntry.buildCommentariesUri(_id);
                else
                    throw new android.database.SQLException(TAG + " Failed to insert row into " + uri);
                break;

            case FRIEND:
                _id = db.insert(SportContract.FriendsEntry.TABLE_NAME, null, contentValues);
                if (_id >0)
                    uriReturn = SportContract.FriendsEntry.buildFriendUri(_id);
                else
                    throw new android.database.SQLException(TAG + " Failed to insert row into " + uri);
                break;

            default: throw new UnsupportedOperationException(TAG +" - Unknown uri: "+ uri);
        }
        if (uriReturn != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return uriReturn;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int del = 0;
        if (s == null) {s="1";} //deleting every registry
        switch (match) {
            case PLAYER:
                del = db.delete(SportContract.PlayerEntry.TABLE_NAME, s, strings);
                break;
            case TEAM:
                del = db.delete(SportContract.TeamEntry.TABLE_NAME, s, strings);
                break;
            case VENUE:
                del = db.delete(SportContract.VenueEntry.TABLE_NAME, s, strings);
                break;
            case MATCH:
                del = db.delete(SportContract.MatchEntry.TABLE_NAME, s, strings);
                break;
            case COMMENTARIES:
                del = db.delete(SportContract.CommentariesEntry.TABLE_NAME, s, strings);
                break;
            case PLAYER_TEAM:
                del = db.delete(SportContract.PlayerTeamEntry.TABLE_NAME, s, strings);
                break;
            case FRIEND:
                del =  db.delete(SportContract.FriendsEntry.TABLE_NAME, s, strings);
                break;
        }
        if (del != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return del;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int updates = 0;
        switch (match) {
            case PLAYER:
                updates = db.update(SportContract.PlayerEntry.TABLE_NAME, contentValues, s, strings);
                break;
            case TEAM:
                updates = db.update(SportContract.TeamEntry.TABLE_NAME, contentValues, s, strings);
                break;
            case VENUE:
                updates = db.update(SportContract.VenueEntry.TABLE_NAME, contentValues, s, strings);
                break;
            case MATCH:
                updates = db.update(SportContract.MatchEntry.TABLE_NAME, contentValues, s, strings);
                break;
            case COMMENTARIES:
                updates = db.update(SportContract.CommentariesEntry.TABLE_NAME, contentValues, s, strings);
                break;
            case PLAYER_TEAM:
                updates = db.update(SportContract.PlayerTeamEntry.TABLE_NAME, contentValues, s, strings);
                break;
            case FRIEND:
                updates = db.update(SportContract.FriendsEntry.TABLE_NAME, contentValues, s, strings);
                break;
        }
        if (updates != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updates;
    }
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String tableName = "";
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "bulkInsert: Database size: "+getContext().getDatabasePath(mOpenHelper.getDatabaseName()).length()+" bytes.");
        }
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PLAYER:
                tableName = SportContract.PlayerEntry.TABLE_NAME; break;
            case TEAM:
                tableName = SportContract.TeamEntry.TABLE_NAME; break;
            case VENUE:
                tableName = SportContract.VenueEntry.TABLE_NAME; break;
            case MATCH:
                tableName = SportContract.MatchEntry.TABLE_NAME; break;
            case COMMENTARIES:
                tableName = SportContract.CommentariesEntry.TABLE_NAME; break;
            case PLAYER_TEAM:
                tableName = SportContract.PlayerTeamEntry.TABLE_NAME; break;
            case FRIEND:
                tableName = SportContract.FriendsEntry.TABLE_NAME; break;
            default:
                return super.bulkInsert(uri, values);
        }
        db.beginTransaction();
        int count = 0;
        try {
            for (ContentValues value : values){
                long _id = db.insert(tableName, null, value);
                if (_id !=-1)
                    count++;
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }

    @Override
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
