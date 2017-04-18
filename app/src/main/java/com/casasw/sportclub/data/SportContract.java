package com.casasw.sportclub.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Junior on 28/03/2017.
 * Defines app's database
 */

class SportContract {
    static final String CONTENT_AUTHORITY = "com.casasw.sportclub";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String PATH_PLAYER = "player";
    static final String PATH_TEAM = "team";
    static final String PATH_VENUE = "venue";
    static final String PATH_MATCH = "match";
    static final String PATH_PLAYER_TEAM = "player_team";
    static final String PATH_COMMENTARIES = "commentaries";
    static final String PATH_FRIENDS = "friends";
    static final String PATH_ATTRIBUTES = "attributes";
    static final String PATH_PHOTOS= "photos";

    /**
     * Inner class that defines the table contents of player table
     **/
    static final class PlayerEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLAYER).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYER;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYER;

        static Uri buildPlayerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        static Uri buildPlayerWithTeamAndAttributes(long id) {
            return CONTENT_URI.buildUpon().appendPath(PATH_TEAM).appendPath(PATH_ATTRIBUTES)
                    .appendPath(""+id).build();
        }
        static Uri buildPlayerWithFriendsAndAttributes(long id) {
            return CONTENT_URI.buildUpon().appendPath(PATH_FRIENDS).appendPath(PATH_ATTRIBUTES)
                    .appendPath(""+id).build();
        }
        static String getPlayerIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        static final String TABLE_NAME = "player";

        static final String COLUMN_PLAYER_NAME = "player_name";

        static final String COLUMN_POSITION = "position";

        static final String COLUMN_HANDEDNESS = "handedness";

        static final String COLUMN_AGE = "age";

        static final String COLUMN_HEIGHT = "height";

        static final String COLUMN_WEIGHT = "weight";

        static final String COLUMN_CITY = "city";

        static final String COLUMN_RATING = "rating";

        static final String COLUMN_EMAIL = "email";

        static final String COLUMN_PROFILE_PHOTO = "player_photo";

        /*
        I need to study if there's any other uri need
         */
    }

    /**
     * Inner class that defines the table contents of team table
     **/
    static final class TeamEntry implements BaseColumns {
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

        static final String TABLE_NAME = "team";

        static final String COLUMN_TEAM_NAME = "team_name";

        /* Player foreign key */
        static final String COLUMN_ADM_ID = "adm_id";

        static final String COLUMN_CITY = "city";

        static final String COLUMN_RATING = "rating";
    }

    /**
     * Inner class that defines the table contents of venue table
     **/
    static final class VenueEntry implements BaseColumns {
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

        static final String TABLE_NAME = "venue";

        static final String COLUMN_VENUE_NAME = "venue_name";

        static final String COLUMN_RATING = "rating";

        static final String COLUMN_LAT_COORD = "lat_coord";

        static final String COLUMN_LONG_COORD = "long_coord";

        static final String COLUMN_ADDRESS = "address";

        static final String COLUMN_TELEPHONE = "telephone";

        static final String COLUMN_EMAIL = "email";

        static final String COLUMN_CITY = "city";
    }

    /**
     * Inner class that defines the table contents of commentaries table
     **/
    static final class CommentariesEntry implements BaseColumns {
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

        static final String TABLE_NAME = "commentaries";

        /*Player foreign key*/
        static final String COLUMN_PLAYER_ID = "player_id";

        /*Venue foreign key*/
        static final String COLUMN_VENUE_ID = "venue_id";

        static final String COLUMN_COMMENTARY = "commentary";

        static final String COLUMN_PARENT_ID = "parent_id";

        static final String COLUMN_RATING = "rating";
    }

    /**
     * Inner class that defines the table contents of player_team table
     * This table stores the relation between player and teams
     **/
    static final class PlayerTeamEntry implements BaseColumns {
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

        static final String TABLE_NAME = "player_team";

        /*Player foreign key*/
        static final String COLUMN_PLAYER_ID = "player_id";

        /*Venue foreign key*/
        static final String COLUMN_TEAM_ID = "team_id";

        static final String COLUMN_MATCHES = "matches";

        static final String COLUMN_RATING = "rating";

    }
    /**
     * Inner class that defines the table contents of match table
     **/
    static final class MatchEntry implements BaseColumns {
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

        static final String TABLE_NAME = "match";

        static final String COLUMN_MATCH_NAME = "name";

        static final String COLUMN_DATE = "date";

        /*Player foreign key*/
        static final String COLUMN_VENUE_ID = "venue_id";

        /*Team foreign key*/
        static final String COLUMN_TEAM_ID = "team_id";

        static final String COLUMN_RATING = "rating";
    }

    /**
     * Inner class that defines the table contents of friends table
     * This table stores the relation between players
     **/
    static final class FriendsEntry implements BaseColumns {
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

        static final String TABLE_NAME = "friends";

        /*Player foreign key*/
        static final String COLUMN_FROM = "req_from";

        /*Player foreign key*/
        static final String COLUMN_TO = "req_to";

        /* fase boolean status 0 for false, 1 for true*/
        static final String COLUMN_STATUS = "status";
    }

    /**
     * Inner class that defines the table contents of attributes table
     * These attributes are a average of other players about one.
     **/
    static final class AttributesEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ATTRIBUTES).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTRIBUTES;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTRIBUTES;

        static Uri buildAttributesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        static final String TABLE_NAME = "attributes";

        /*Player foreign key*/
        static final String COLUMN_PLAYER_ID = "player_id";

        static final String COLUMN_SPEED = "speed";

        static final String COLUMN_POWER = "power";

        static final String COLUMN_TECHNIQUE = "technique";

        static final String COLUMN_FITNESS = "fitness";

        static final String COLUMN_FAIR_PLAY = "fair_player";
    }

    /**
     * Inner class that defines the table contents of photos table
     * These attributes are a average of other players about one.
     **/
    static final class PhotosEntry implements BaseColumns {
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

        static final String TABLE_NAME = "photos";

        /*Venue foreign key*/
        static final String COLUMN_VENUE_ID = "venue_id";

        static final String COLUMN_URL = "photo_url";

    }
}