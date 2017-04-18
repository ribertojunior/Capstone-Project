package com.casasw.sportclub.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
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

    //test methods and variables
    private boolean isTest =  false;
    public static UriMatcher getsUriMatcher() {
        return sUriMatcher;
    }

    public void testOnCreate(Context c){
        mOpenHelper = new SportDbHelper(c);
        isTest = true;
    }
    //end of test methods

    static final int PLAYER = 100;
    static final int PLAYER_ID = 101;
    static final int TEAM = 200;
    static final int TEAM_ID = 201;
    static final int MATCH = 300;
    static final int MATCH_ID = 301;
    static final int VENUE = 400;
    static final int VENUE_ID = 401;
    static final int PLAYER_TEAM = 500;
    static final int PLAYER_TEAM_ID = 501;
    static final int COMMENTARIES = 600;
    static final int COMMENTARIES_ID = 606;
    static final int COMMENTARIES_VENUE_ID = 601;
    static final int COMMENTARIES_PARENT_ID = 602;
    static final int FRIENDS = 700;
    static final int FRIENDS_FT_ID = 701;
    static final int PHOTOS = 750;
    static final int PHOTOS_ID = 751;
    static final int ATTRIBUTES = 770;
    static final int ATTRIBUTES_ID = 771;
    static final int PLAYER_TEAM_ATTRIBUTES = 800;
    static final int VENUE_PLAYER_COMMENTARIES_PHOTOS = 900;
    static final int PLAYER_FRIENDS_ATTRIBUTES = 1100;
    static final int MATCH_VENUE_TEAM = 1200;

    static final String[] PLAYER_TEAM_ATTRIBUTES_COLUMNS = {
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry._ID,
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry.COLUMN_PLAYER_NAME,
            SportContract.TeamEntry.TABLE_NAME +
                    "." + SportContract.TeamEntry._ID,
            SportContract.TeamEntry.TABLE_NAME +
                    "." + SportContract.TeamEntry.COLUMN_TEAM_NAME,
            SportContract.TeamEntry.TABLE_NAME +
                    "." + SportContract.TeamEntry.COLUMN_RATING,
            SportContract.AttributesEntry.COLUMN_SPEED,
            SportContract.AttributesEntry.COLUMN_POWER,
            SportContract.AttributesEntry.COLUMN_TECHNIQUE,
            SportContract.AttributesEntry.COLUMN_FITNESS,
            SportContract.AttributesEntry.COLUMN_FAIR_PLAY
    };

    /*local, jogador, comentarios_local*/
    static final String[] VENUE_PLAYER_COMMENTARIES_PHOTOS_COLUMNS = {
            SportContract.VenueEntry.TABLE_NAME +
                    "." + SportContract.VenueEntry._ID,
            SportContract.VenueEntry.TABLE_NAME +
                    "." + SportContract.VenueEntry.COLUMN_VENUE_NAME,
            SportContract.VenueEntry.TABLE_NAME +
                    "." + SportContract.VenueEntry.COLUMN_RATING,
            SportContract.VenueEntry.TABLE_NAME +
                    "." + SportContract.VenueEntry.COLUMN_LAT_COORD,
            SportContract.VenueEntry.TABLE_NAME +
                    "." + SportContract.VenueEntry.COLUMN_LONG_COORD,
            SportContract.VenueEntry.TABLE_NAME +
                    "." + SportContract.VenueEntry.COLUMN_ADDRESS,
            SportContract.VenueEntry.TABLE_NAME +
                    "." + SportContract.VenueEntry.COLUMN_TELEPHONE,
            SportContract.VenueEntry.TABLE_NAME +
                    "." + SportContract.VenueEntry.COLUMN_EMAIL,
            SportContract.VenueEntry.TABLE_NAME +
                    "." + SportContract.VenueEntry.COLUMN_CITY,
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry._ID,
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry.COLUMN_PLAYER_NAME,
            SportContract.CommentariesEntry.TABLE_NAME +
                    "." + SportContract.CommentariesEntry._ID,
            SportContract.CommentariesEntry.TABLE_NAME +
                    "." + SportContract.CommentariesEntry.COLUMN_COMMENTARY,
            SportContract.CommentariesEntry.TABLE_NAME +
                    "." + SportContract.CommentariesEntry.COLUMN_RATING,
            SportContract.CommentariesEntry.TABLE_NAME +
                    "." + SportContract.CommentariesEntry.COLUMN_PARENT_ID,
            SportContract.PhotosEntry.TABLE_NAME +
                    "." + SportContract.PhotosEntry.COLUMN_URL
    };

    /* jogador e amigo*/
    static final String[] PLAYER_FRIENDS_ATTRIBUTES_COLUMNS = {
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry._ID,
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry.COLUMN_PLAYER_NAME,
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry.COLUMN_HANDEDNESS,
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry.COLUMN_AGE,
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry.COLUMN_HEIGHT,
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry.COLUMN_WEIGHT,
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry.COLUMN_CITY,
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry.COLUMN_RATING,
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry.COLUMN_EMAIL,
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry.COLUMN_PROFILE_PHOTO,
            SportContract.AttributesEntry.TABLE_NAME +
                    "." + SportContract.AttributesEntry.COLUMN_SPEED,
            SportContract.AttributesEntry.TABLE_NAME +
                    "." + SportContract.AttributesEntry.COLUMN_POWER,
            SportContract.AttributesEntry.TABLE_NAME +
                    "." + SportContract.AttributesEntry.COLUMN_TECHNIQUE,
            SportContract.AttributesEntry.TABLE_NAME +
                    "." + SportContract.AttributesEntry.COLUMN_FITNESS,
            SportContract.AttributesEntry.TABLE_NAME +
                    "." + SportContract.AttributesEntry.COLUMN_FAIR_PLAY,
            SportContract.FriendsEntry.TABLE_NAME +
                    "." + SportContract.FriendsEntry.COLUMN_TO,
            SportContract.FriendsEntry.TABLE_NAME +
                    "." + SportContract.FriendsEntry.COLUMN_FROM,
            SportContract.FriendsEntry.TABLE_NAME +
                    "." + SportContract.FriendsEntry.COLUMN_STATUS

    };

    /*Partida, local e time*/
    static final String[] MATCH_VENUE_TEAM_COLUMNS = {
            SportContract.MatchEntry.TABLE_NAME +
                    "." + SportContract.MatchEntry._ID,
            SportContract.MatchEntry.TABLE_NAME +
                    "." + SportContract.MatchEntry.COLUMN_MATCH_NAME,
            SportContract.MatchEntry.TABLE_NAME +
                    "." + SportContract.MatchEntry.COLUMN_DATE,
            SportContract.MatchEntry.TABLE_NAME +
                    "." + SportContract.MatchEntry.COLUMN_RATING,
            SportContract.VenueEntry.TABLE_NAME +
                    "." + SportContract.VenueEntry._ID,
            SportContract.VenueEntry.TABLE_NAME +
                    "." + SportContract.VenueEntry.COLUMN_VENUE_NAME,
            SportContract.TeamEntry.TABLE_NAME +
                    "." + SportContract.TeamEntry._ID,
            SportContract.TeamEntry.TABLE_NAME +
                    "." + SportContract.TeamEntry.COLUMN_TEAM_NAME

    };


    private static final SQLiteQueryBuilder sPlayerTeamAttributesQueryBuilder;
    static {
        sPlayerTeamAttributesQueryBuilder = new SQLiteQueryBuilder();

        //inner join
        //TEAM inner join player_team inner join player inner join attributes
        sPlayerTeamAttributesQueryBuilder.setTables(
                SportContract.TeamEntry.TABLE_NAME + " INNER JOIN " +
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
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "static initializer: Player Team Attributes Query\n "+ sPlayerTeamAttributesQueryBuilder.getTables());
        }
    }

    private static final SQLiteQueryBuilder sVenuePlayerCommentariesQueryBuilder;
    static {
        sVenuePlayerCommentariesQueryBuilder = new SQLiteQueryBuilder();

        //inner join
        //venue inner join commentaries inner join player
        sVenuePlayerCommentariesQueryBuilder.setTables(
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
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "static initializer: Venue Player Commentaries Query\n "+ sVenuePlayerCommentariesQueryBuilder.getTables());
        }
    }

    private static final SQLiteQueryBuilder sPlayerFriendsAttributes;
    static {
        sPlayerFriendsAttributes = new SQLiteQueryBuilder();
        sPlayerFriendsAttributes.setTables(
                SportContract.PlayerEntry.TABLE_NAME + " INNER JOIN " +
                        SportContract.AttributesEntry.TABLE_NAME +
                        " ON " + SportContract.PlayerEntry.TABLE_NAME +
                        "." + SportContract.PlayerEntry._ID +
                        " = " + SportContract.AttributesEntry.COLUMN_PLAYER_ID +
                        " INNER JOIN " +
                        SportContract.FriendsEntry.TABLE_NAME +
                        " ON (" + SportContract.PlayerEntry.TABLE_NAME +
                        "." + SportContract.PlayerEntry._ID +
                        " = " + SportContract.FriendsEntry.COLUMN_TO +
                        " OR " + SportContract.PlayerEntry.TABLE_NAME +
                        "." + SportContract.PlayerEntry._ID +
                        " = " + SportContract.FriendsEntry.COLUMN_FROM+") AND " +
                        SportContract.FriendsEntry.COLUMN_STATUS + " = 1"
        );
    }

    private static final SQLiteQueryBuilder sMatchVenueTeam;
    static {
        sMatchVenueTeam = new SQLiteQueryBuilder();
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
    }


    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        /*still on analyze for more matches*/
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_PLAYER, PLAYER);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_PLAYER+"/#", PLAYER_ID);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_TEAM, TEAM);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_TEAM+"/#", TEAM_ID);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_MATCH, MATCH);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_MATCH+"/#", MATCH_ID);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_VENUE, VENUE);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_VENUE+"/#", VENUE_ID);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_PLAYER_TEAM, PLAYER_TEAM);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_PLAYER_TEAM+"/#", PLAYER_TEAM_ID);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_COMMENTARIES, COMMENTARIES);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_COMMENTARIES+"/#", COMMENTARIES_ID);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_PHOTOS, PHOTOS);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_PHOTOS+"/#", PHOTOS_ID);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_ATTRIBUTES, ATTRIBUTES);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_ATTRIBUTES+"/#", ATTRIBUTES_ID);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY,
                SportContract.PATH_COMMENTARIES+"/"+SportContract.PATH_VENUE+"/#", COMMENTARIES_VENUE_ID);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY,
                SportContract.PATH_COMMENTARIES+"/"+SportContract.PATH_COMMENTARIES+"/#", COMMENTARIES_PARENT_ID);
        //to = ? or from = ?
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_FRIENDS, FRIENDS);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportContract.PATH_FRIENDS+"/#", FRIENDS_FT_ID);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY,
                SportContract.PATH_PLAYER+"/"+SportContract.PATH_TEAM+"/"+SportContract.PATH_ATTRIBUTES+"/#",
                PLAYER_TEAM_ATTRIBUTES);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY,
                SportContract.PATH_PLAYER+"/"+SportContract.PATH_FRIENDS+"/"+SportContract.PATH_ATTRIBUTES+"/#",
                PLAYER_FRIENDS_ATTRIBUTES);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY,
                SportContract.PATH_VENUE+
                        "/"+SportContract.PATH_PLAYER+"/"+SportContract.PATH_COMMENTARIES+
                        "/"+SportContract.PATH_PHOTOS+"/#", VENUE_PLAYER_COMMENTARIES_PHOTOS);
        uriMatcher.addURI(SportContract.CONTENT_AUTHORITY,
                SportContract.PATH_MATCH+"/"+SportContract.PATH_VENUE+"/"+SportContract.PATH_TEAM+"/#", MATCH_VENUE_TEAM);

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
    private static final String sCommentariesSelection = 
            SportContract.CommentariesEntry.TABLE_NAME +
            "." + SportContract.CommentariesEntry._ID + " = ?";
    private static final String sPhotosSelection = 
            SportContract.PhotosEntry.TABLE_NAME +
            "." + SportContract.PhotosEntry._ID + " = ?";
    private static final String sAttributesSelection = 
            SportContract.AttributesEntry.TABLE_NAME +
            "." + SportContract.AttributesEntry._ID + " = ?";
    private static final String sPlayerTeamSelection =
            SportContract.PlayerTeamEntry.TABLE_NAME +
                    "." + SportContract.PlayerTeamEntry.COLUMN_TEAM_ID+ " = ?";
    private static final String sCommentariesVenueSelection =
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
            case PLAYER_TEAM_ID:
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
            case COMMENTARIES_ID:
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
            case COMMENTARIES_VENUE_ID:
                selectionArgs = new String[]{SportContract.CommentariesEntry.getIdFromUri(uri)};
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.CommentariesEntry.TABLE_NAME,
                        projection,
                        sCommentariesVenueSelection,
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
            case FRIENDS:
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
            case FRIENDS_FT_ID:
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
            case PHOTOS:
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.PhotosEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PHOTOS_ID:
                selectionArgs = new String[]{SportContract.PhotosEntry.getIdFromUri(uri)};
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.PhotosEntry.TABLE_NAME,
                        projection,
                        sPhotosSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case ATTRIBUTES:
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.AttributesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case ATTRIBUTES_ID:
                selectionArgs = new String[]{SportContract.AttributesEntry.getIdFromUri(uri)};
                cursor = mOpenHelper.getReadableDatabase().query(
                        SportContract.AttributesEntry.TABLE_NAME,
                        projection,
                        sAttributesSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PLAYER_TEAM_ATTRIBUTES:
                selectionArgs = new String[]{SportContract.PlayerEntry.getPlayerIdFromUri(uri)};
                cursor = sPlayerTeamAttributesQueryBuilder.query(
                        mOpenHelper.getReadableDatabase(),
                        PLAYER_TEAM_ATTRIBUTES_COLUMNS,
                        sPlayerSelection,
                        selectionArgs,
                         null, null, null);
                break;
            case VENUE_PLAYER_COMMENTARIES_PHOTOS:
                selectionArgs = new String[]{SportContract.VenueEntry.getVenueIdFromUri(uri)};
                cursor = sVenuePlayerCommentariesQueryBuilder.query(
                        mOpenHelper.getReadableDatabase(),
                        VENUE_PLAYER_COMMENTARIES_PHOTOS_COLUMNS,
                        sVenueSelection,
                        selectionArgs,
                        null, null, null
                );
                break;
            case PLAYER_FRIENDS_ATTRIBUTES:
                selectionArgs = new String[]{SportContract.PlayerEntry.getPlayerIdFromUri(uri)};
                cursor = sPlayerFriendsAttributes.query(
                        mOpenHelper.getReadableDatabase(),
                        PLAYER_FRIENDS_ATTRIBUTES_COLUMNS,
                        sPlayerSelection,
                        selectionArgs,
                        null, null, null
                );
                break;
            case MATCH_VENUE_TEAM:
                selectionArgs = new String[]{SportContract.MatchEntry.getIdFromUri(uri)};
                cursor = sMatchVenueTeam.query(
                        mOpenHelper.getReadableDatabase(),
                        MATCH_VENUE_TEAM_COLUMNS,
                        sMatchSelection,
                        selectionArgs,
                        null, null, null
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
            case PLAYER_TEAM_ID:
                return SportContract.PlayerTeamEntry.CONTENT_ITEM_TYPE;
            case COMMENTARIES:
                return SportContract.CommentariesEntry.CONTENT_TYPE;
            case COMMENTARIES_ID:
                return SportContract.CommentariesEntry.CONTENT_ITEM_TYPE;
            case COMMENTARIES_VENUE_ID:
                return SportContract.CommentariesEntry.CONTENT_ITEM_TYPE;
            case COMMENTARIES_PARENT_ID:
                return SportContract.CommentariesEntry.CONTENT_ITEM_TYPE;
            case FRIENDS:
                return SportContract.FriendsEntry.CONTENT_TYPE;
            case FRIENDS_FT_ID:
                return SportContract.FriendsEntry.CONTENT_ITEM_TYPE;
            case PHOTOS:
                return SportContract.PhotosEntry.CONTENT_TYPE;
            case PHOTOS_ID:
                return SportContract.PhotosEntry.CONTENT_ITEM_TYPE;
            case ATTRIBUTES:
                return SportContract.AttributesEntry.CONTENT_TYPE;
            case ATTRIBUTES_ID:
                return SportContract.AttributesEntry.CONTENT_ITEM_TYPE;
            case PLAYER_TEAM_ATTRIBUTES:
                return SportContract.PlayerEntry.CONTENT_ITEM_TYPE;
            case VENUE_PLAYER_COMMENTARIES_PHOTOS:
                return SportContract.VenueEntry.CONTENT_ITEM_TYPE;
            case PLAYER_FRIENDS_ATTRIBUTES:
                return SportContract.PlayerEntry.CONTENT_ITEM_TYPE;
            case MATCH_VENUE_TEAM:
                return SportContract.MatchEntry.CONTENT_ITEM_TYPE;
            default: throw new UnsupportedOperationException(TAG +" - Unknown uri: "+ uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri uriReturn;
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

            case FRIENDS:
                _id = db.insert(SportContract.FriendsEntry.TABLE_NAME, null, contentValues);
                if (_id >0)
                    uriReturn = SportContract.FriendsEntry.buildFriendsUri(_id);
                else
                    throw new android.database.SQLException(TAG + " Failed to insert row into " + uri);
                break;

            case PHOTOS:
                _id = db.insert(SportContract.PhotosEntry.TABLE_NAME, null, contentValues);
                if (_id >0)
                    uriReturn = SportContract.PhotosEntry.buildPhotosUri(_id);
                else
                    throw new android.database.SQLException(TAG + " Failed to insert row into " + uri);
                break;

            case ATTRIBUTES:
                _id = db.insert(SportContract.AttributesEntry.TABLE_NAME, null, contentValues);
                if (_id >0)
                    uriReturn = SportContract.AttributesEntry.buildAttributesUri(_id);
                else
                    throw new android.database.SQLException(TAG + " Failed to insert row into " + uri);
                break;

            default: throw new UnsupportedOperationException(TAG +" - Unknown uri: "+ uri);
        }
        if (uriReturn != null && !isTest) {
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
            case FRIENDS:
                del =  db.delete(SportContract.FriendsEntry.TABLE_NAME, s, strings);
                break;
            case PHOTOS:
                del =  db.delete(SportContract.PhotosEntry.TABLE_NAME, s, strings);
                break;
            case ATTRIBUTES:
                del =  db.delete(SportContract.AttributesEntry.TABLE_NAME, s, strings);
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
            case FRIENDS:
                updates = db.update(SportContract.FriendsEntry.TABLE_NAME, contentValues, s, strings);
                break;
            case PHOTOS:
                updates = db.update(SportContract.PhotosEntry.TABLE_NAME, contentValues, s, strings);
                break;
            case ATTRIBUTES:
                updates = db.update(SportContract.AttributesEntry.TABLE_NAME, contentValues, s, strings);
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
        String tableName;
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
            case FRIENDS:
                tableName = SportContract.FriendsEntry.TABLE_NAME; break;
            case PHOTOS:
                tableName = SportContract.PhotosEntry.TABLE_NAME; break;
            case ATTRIBUTES:
                tableName = SportContract.AttributesEntry.TABLE_NAME; break;
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
