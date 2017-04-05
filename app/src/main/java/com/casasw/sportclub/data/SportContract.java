package com.casasw.sportclub.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Junior on 28/03/2017.
 * Defines app's database
 */

public class SportContract {
    public static final String CONTENT_AUTHORITY = "com.casasw.sportclub";
    
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    
    public static final String PATH_PLAYER = "player";
    public static final String PATH_TEAM = "team";
    public static final String PATH_VENUE = "venue";
    public static final String PATH_MATCH = "match";
    public static final String PATH_PLAYER_TEAM = "player_team";
    public static final String PATH_COMMENTARIES = "commentaries";
    public static final String PATH_FRIENDS = "friends";

    /**
     * Inner class that defines the table contents of player table
     **/
    public static final class PlayerEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLAYER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYER;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYER;

        public static Uri buildPlayerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getPlayerIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        public static final String TABLE_NAME = "player";

        public static final String COLUMN_PLAYER_NAME = "player_name";
        
        public static final String COLUMN_POSITION = "position";
        
        public static final String COLUMN_HANDEDNESS = "handedness";

        public static final String COLUMN_AGE = "age";

        public static final String COLUMN_HEIGHT = "height";

        public static final String COLUMN_WEIGHT = "weight";

        public static final String COLUMN_CITY = "city";

        public static final String COLUMN_RATING = "rating";

        public static final String COLUMN_EMAIL = "email";

        /*
        I need to study if there's any other uri need
         */
    }

    /**
     * Inner class that defines the table contents of team table
     **/
    public static final class TeamEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TEAM).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TEAM;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TEAM;

        public static Uri buildTeamUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getTeamIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        public static final String TABLE_NAME = "team";

        public static final String COLUMN_TEAM_NAME = "team_name";

        /* Player foreign key */
        public static final String COLUMN_ADM_ID = "adm_id";

        public static final String COLUMN_CITY = "city";

        public static final String COLUMN_RATING = "rating";

        /*
        I need to study if there's any other uri need
         */
    }

    /**
     * Inner class that defines the table contents of venue table
     **/
    public static final class VenueEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VENUE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VENUE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VENUE;

        public static Uri buildVenueUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getVenueIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        public static final String TABLE_NAME = "venue";

        public static final String COLUMN_VENUE_NAME = "venue_name";

        public static final String COLUMN_RATING = "rating";
        
        public static final String COLUMN_LAT_COORD = "lat_coord";

        public static final String COLUMN_LONG_COORD = "long_coord";

        public static final String COLUMN_ADDRESS = "address";
        
        public static final String COLUMN_TELEPHONE = "telephone";

        public static final String COLUMN_EMAIL = "email";

        public static final String COLUMN_CITY = "city";

        /*
        I need to study if there's any other uri need
         */
    }

    /**
     * Inner class that defines the table contents of commentaries table
     **/
    public static final class CommentariesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMMENTARIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COMMENTARIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COMMENTARIES;

        public static Uri buildCommentariesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        public static final String TABLE_NAME = "commentaries";

        /*Player foreign key*/
        public static final String COLUMN_PLAYER_ID = "player_id";

        /*Venue foreign key*/
        public static final String COLUMN_VENUE_ID = "venue_id";

        public static final String COLUMN_COMMENTARY = "commentary";

        public static final String COLUMN_PARENT_ID = "parent_id";

        public static final String COLUMN_RATING = "rating";

        /*
        I need to study if there's any other uri need
         */
    }

    /**
     * Inner class that defines the table contents of player_team table
     * This table stores the relation between player and teams
     **/
    public static final class PlayerTeamEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLAYER_TEAM).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYER_TEAM;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYER_TEAM;

        public static Uri buildPlayerTeamUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        public static final String TABLE_NAME = "player_team";

        /*Player foreign key*/
        public static final String COLUMN_PLAYER_ID = "player_id";

        /*Venue foreign key*/
        public static final String COLUMN_TEAM_ID = "team_id";

        public static final String COLUMN_MATCHES = "matches";

        public static final String COLUMN_RATING = "rating";


        /*
        I need to study if there's any other uri need
         */
    }
    /**
     * Inner class that defines the table contents of match table
     **/
    public static final class MatchEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MATCH).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MATCH;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MATCH;

        public static Uri buildMatchUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        public static final String TABLE_NAME = "match";

        public static final String COLUMN_MATCH_NAME = "name";

        public static final String COLUMN_DATE = "date";

        /*Player foreign key*/
        public static final String COLUMN_VENUE_ID = "venue_id";

        /*Team foreign key*/
        public static final String COLUMN_TEAM_ID = "team_id";

        public static final String COLUMN_RATING = "rating";


        /*
        I need to study if there's any other uri need
         */
    }

    /**
     * Inner class that defines the table contents of friends table
     * This table stores the relation between players
     **/
    public static final class FriendsEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FRIENDS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FRIENDS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FRIENDS;

        public static Uri buildFriendUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        public static final String TABLE_NAME = "friends";

        /*Player foreign key*/
        public static final String COLUMN_FROM = "req_from";

        /*Player foreign key*/
        public static final String COLUMN_TO = "req_to";

        /* fase boolean status 0 for false, 1 for true*/
        public static final String COLUMN_STATUS = "status";




        /*
        I need to study if there's any other uri need
         */
    }
}
