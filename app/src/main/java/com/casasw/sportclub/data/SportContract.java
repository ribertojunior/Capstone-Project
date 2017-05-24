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
    static final String CONTENT_AUTHORITY = "com.casasw.sportclub";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String PATH_PLAYER = "player";
    static final String PATH_TEAM = "team";
    static final String PATH_VENUE = "venue";
    static final String PATH_MATCH = "match";
    static final String PATH_PLAYER_TEAM = "player_team";
    static final String PATH_COMMENTARIES = "commentaries";
    static final String PATH_FRIENDS = "friends";
    static final String PATH_ATTRIBUTES = "attributes";
    static final String PATH_PHOTOS = "photos";
    static final String PATH_SPORT = "sports";
    static final String PATH_PLAYER_SPORT = "player_sport";
    static final String PATH_VENUE_SPORT = "venue_sport";

    /**
     * Inner class that defines the table contents of player table
     **/
    public static final class PlayerEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLAYER).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYER;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYER;

        public static Uri buildPlayerUri() {
            return CONTENT_URI;
        }

        public static Uri buildPlayerUriWithID(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildPlayerWithTeamAndAttributes(long id) {
            return CONTENT_URI.buildUpon().appendPath(PATH_TEAM).appendPath(PATH_ATTRIBUTES)
                    .appendPath(""+id).build();
        }
        public  static Uri buildPlayerWithSportAndAttributes(String email) {
            return CONTENT_URI.buildUpon().appendPath(PATH_SPORT).appendPath(PATH_ATTRIBUTES)
                    .appendPath(email).build();
        }
        public static Uri buildPlayerWithFriendsAndAttributes(long id) {
            return CONTENT_URI.buildUpon().appendPath(PATH_FRIENDS).appendPath(PATH_ATTRIBUTES)
                    .appendPath(""+id).build();
        }
        public static String getPlayerIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }
        public static String getPlayerEmailFromUri(Uri uri) {
            String ret = uri.getPathSegments().get(uri.getPathSegments().size()-1).replace("(at)", "@");
            return ret;
        }

        public static final String TABLE_NAME = "player";

        public static final String COLUMN_PLAYER_NAME = "player_name";

        public static final String COLUMN_HANDEDNESS = "handedness";

        public static final String COLUMN_BDAY = "bday";

        public static final String COLUMN_HEIGHT = "height";

        public static final String COLUMN_WEIGHT = "weight";

        public static final String COLUMN_CITY = "city";

        public static final String COLUMN_STATE = "state";

        public static final String COLUMN_RATING = "rating";

        public static final String COLUMN_EMAIL = "email";

        public static final String COLUMN_PROFILE_PHOTO = "player_photo";

        /*
        I need to study if there's any other uri need
         */
    }

    /**
     * Inner class that defines the table contents of team table
     **/
    public static final class TeamEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TEAM).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TEAM;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TEAM;

        static Uri buildTeamUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        static String getTeamIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        public static final String TABLE_NAME = "team";

        public static final String COLUMN_TEAM_NAME = "team_name";

        /* Player foreign key */
        public static final String COLUMN_ADM_ID = "adm_id";

        /* Sport foreign key */
        public static final String COLUMN_SPORT_ID = "sport_id";

        public static final String COLUMN_CITY = "city";

        public static final String COLUMN_STATE = "state";

        public static final String COLUMN_RATING = "rating";
    }

    /**
     * Inner class that defines the table contents of venue table
     **/
    public static final class VenueEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VENUE).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VENUE;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VENUE;

        static Uri buildVenueUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        static Uri buildVenueWithPlayerCommentariesAndPhotosUri(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(PATH_PLAYER).appendPath(PATH_COMMENTARIES)
                    .appendPath(PATH_PHOTOS).appendPath(""+id).build();
        }
        static String getVenueIdFromUri(Uri uri) {
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

        public static final String COLUMN_STATE = "state";
    }

    /**
     * Inner class that defines the table contents of commentaries table
     **/
    public static final class CommentariesEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMMENTARIES).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COMMENTARIES;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COMMENTARIES;

        static Uri buildCommentariesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        static Uri buildCommentariesWithVenueUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(PATH_VENUE).appendPath(""+id).build();
        }
        static Uri buildCommentariesWithParentUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(PATH_COMMENTARIES).appendPath(""+id).build();
        }

        static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        public static final String TABLE_NAME = "commentaries";

        /*Player foreign key*/
        public static final String COLUMN_PLAYER_ID = "player_id";

        /*Venue foreign key*/
        public static final String COLUMN_VENUE_ID = "venue_id";

        public static final String COLUMN_COMMENTARY = "commentary";

        public static final String COLUMN_PARENT_ID = "parent_id";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_RATING = "rating";
    }

    /**
     * Inner class that defines the table contents of sport table
     **/
    public static final class SportsEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SPORT).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SPORT;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SPORT;

        public static Uri buildSportUri() {
            return CONTENT_URI;
        }

        public static Uri buildSportWithIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        public static final String TABLE_NAME = "sports";
        //"none" for no edited profiles.
        public static final String COLUMN_NAME = "sport_name";
        //off for sport no longer supported and for none
        public static final String COLUMN_STATUS = "status";
    }

    /**
     * Inner class that defines the table contents of player_team table
     * This table stores the relation between player and teams
     **/
    public static final class PlayerTeamEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLAYER_TEAM).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYER_TEAM;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYER_TEAM;

        static Uri buildPlayerTeamUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        public static final String TABLE_NAME = "player_team";

        /*Player foreign key*/
        public static final String COLUMN_PLAYER_ID = "player_id";

        /*Venue foreign key*/
        public static final String COLUMN_TEAM_ID = "team_id";

        public static final String COLUMN_MATCHES = "matches";

        public static final String COLUMN_RATING = "rating";

    }

    /**
     * Inner class that defines the table contents of player_sport table
     * This table stores the relation between player and sports
     **/
    public static final class PlayerSportEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLAYER_SPORT).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYER_SPORT;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYER_SPORT;

        public static Uri buildPlayerSportUri() {
            return CONTENT_URI;
        }
        
        public static Uri buildPlayerSportUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        public static long getLongIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(uri.getPathSegments().size()-1));
        }

        public static final String TABLE_NAME = "player_sport";

        /*Player foreign key*/
        public static final String COLUMN_PLAYER_ID = "player_id";

        /*Sport foreign key*/
        public static final String COLUMN_SPORT_ID = "sport_id";

        public static final String COLUMN_POSITION = "positions";

    }

    /**
     * Inner class that defines the table contents of venue_sport table
     * This table stores the relation between venue and teams
     **/
    public static final class VenueSportEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VENUE_SPORT).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VENUE_SPORT;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VENUE_SPORT;

        static Uri buildVenueSportUri() {
            return CONTENT_URI;
        }

        static Uri buildVenueSportUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        public static final String TABLE_NAME = "venue_sport";

        /*Venue foreign key*/
        public static final String COLUMN_VENUE_ID = "venue_id";

        /*Sport foreign key*/
        public static final String COLUMN_SPORT_ID = "sport_id";

    }
    
    /**
     * Inner class that defines the table contents of match table
     **/
    public static final class MatchEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MATCH).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MATCH;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MATCH;

        static Uri buildMatchUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        static Uri buildMathWithVenueAndPhotosUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(PATH_VENUE).appendPath(PATH_TEAM).appendPath(""+id).build();
        }
        static String getIdFromUri(Uri uri) {
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
    }

    /**
     * Inner class that defines the table contents of friends table
     * This table stores the relation between players
     **/
    public static final class FriendsEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FRIENDS).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FRIENDS;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FRIENDS;

        static Uri buildFriendsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        public static final String TABLE_NAME = "friends";

        /*Player foreign key*/
        public static final String COLUMN_FROM = "req_from";

        /*Player foreign key*/
        public static final String COLUMN_TO = "req_to";

        /* fase boolean status 0 for false, 1 for true*/
        public static final String COLUMN_STATUS = "status";
    }

    /**
     * Inner class that defines the table contents of attributes table
     * These attributes are a average of other players about one.
     **/
    public static final class AttributesEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ATTRIBUTES).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTRIBUTES;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTRIBUTES;

        public static Uri buildAttributesUri() {
            return CONTENT_URI;
        }
        public static Uri buildAttributesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        public static final String TABLE_NAME = "attributes";

        /*Player foreign key*/
        public static final String COLUMN_PLAYER_ID = "player_id";

        public static final String COLUMN_SPEED = "speed";

        public static final String COLUMN_POWER = "power";

        public static final String COLUMN_TECHNIQUE = "technique";

        public static final String COLUMN_FITNESS = "fitness";

        public static final String COLUMN_FAIR_PLAY = "fair_player";
    }

    /**
     * Inner class that defines the table contents of photos table
     * These attributes are a average of other players about one.
     **/
    public static final class PhotosEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PHOTOS).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PHOTOS;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PHOTOS;

        static Uri buildPhotosUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        public static final String TABLE_NAME = "photos";

        /*Venue foreign key*/
        public static final String COLUMN_VENUE_ID = "venue_id";

        public static final String COLUMN_URL = "photo_url";

    }
}