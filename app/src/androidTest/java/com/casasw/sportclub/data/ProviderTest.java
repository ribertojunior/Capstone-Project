package com.casasw.sportclub.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Junior on 13/04/2017.
 * SportProvider Tests
 */
@RunWith(AndroidJUnit4.class)
public class ProviderTest {
    public static final String TAG = ProviderTest.class.getSimpleName();
    private SportProvider mProvider;
    
    @Before
    public void setUp() throws Exception {
        mProvider = new SportProvider();
        mProvider.testOnCreate(InstrumentationRegistry.getTargetContext());
    }
    
    @Test
    public void testUriMatcher() {
        Uri.Builder builder = new Uri.Builder();
        
        Uri uriPlayer =
                builder.authority(SportContract.CONTENT_AUTHORITY).
                        appendPath(SportContract.PATH_PLAYER).build();
        assertTrue("Error: UriMatcher on Player failed: " +
                uriPlayer.toString(), SportProvider.getsUriMatcher().match(uriPlayer) == SportProvider.PLAYER);
        Uri uriPlayer_ID = SportContract.PlayerEntry.buildPlayerUri(123);
        assertTrue("Error: UriMatcher on Player failed: " +
                uriPlayer_ID.toString(), SportProvider.getsUriMatcher().match(uriPlayer_ID) == SportProvider.PLAYER_ID);

        builder = new Uri.Builder();
        Uri uriMatch =
                builder.authority(SportContract.CONTENT_AUTHORITY).
                        appendPath(SportContract.PATH_MATCH).build();
        assertTrue("Error: UriMatcher on Match failed: " +
                uriMatch.toString(), SportProvider.getsUriMatcher().match(uriMatch) == SportProvider.MATCH);
        Uri uriMatch_ID = SportContract.MatchEntry.buildMatchUri(123);
        assertTrue("Error: UriMatcher on Match failed: " +
                uriMatch_ID.toString(), SportProvider.getsUriMatcher().match(uriMatch_ID) == SportProvider.MATCH_ID);

        builder = new Uri.Builder();
        Uri uriVenue =
                builder.authority(SportContract.CONTENT_AUTHORITY).
                        appendPath(SportContract.PATH_VENUE).build();
        assertTrue("Error: UriMatcher on Venue failed: " +
                uriVenue.toString(), SportProvider.getsUriMatcher().match(uriVenue) == SportProvider.VENUE);
        Uri uriVenue_ID = SportContract.VenueEntry.buildVenueUri(123);
        assertTrue("Error: UriMatcher on Venue failed: " +
                uriVenue_ID.toString(), SportProvider.getsUriMatcher().match(uriVenue_ID) == SportProvider.VENUE_ID);

        builder = new Uri.Builder();
        Uri uriPlayerTeam =
                builder.authority(SportContract.CONTENT_AUTHORITY).
                        appendPath(SportContract.PATH_PLAYER_TEAM).build();
        assertTrue("Error: UriMatcher on PlayerTeam failed: " +
                uriPlayerTeam.toString(), SportProvider.getsUriMatcher().match(uriPlayerTeam) == SportProvider.PLAYER_TEAM);
        Uri uriPlayerTeam_ID = SportContract.PlayerTeamEntry.buildPlayerTeamUri(123);
        assertTrue("Error: UriMatcher on PlayerTeam failed: " +
                uriPlayerTeam_ID.toString(), SportProvider.getsUriMatcher().match(uriPlayerTeam_ID) == SportProvider.PLAYER_TEAM_ID);

        builder = new Uri.Builder();
        Uri uriCommentaries =
                builder.authority(SportContract.CONTENT_AUTHORITY).
                        appendPath(SportContract.PATH_COMMENTARIES).build();
        assertTrue("Error: UriMatcher on Commentaries failed: " +
                uriCommentaries.toString(), SportProvider.getsUriMatcher().match(uriCommentaries) == SportProvider.COMMENTARIES);
        Uri uriCommentaries_ID = SportContract.CommentariesEntry.buildCommentariesUri(123);
        assertTrue("Error: UriMatcher on Commentaries failed: " +
                uriCommentaries_ID.toString(), SportProvider.getsUriMatcher().match(uriCommentaries_ID) == SportProvider.COMMENTARIES_ID);

        builder = new Uri.Builder();
        Uri uriFriends =
                builder.authority(SportContract.CONTENT_AUTHORITY).
                        appendPath(SportContract.PATH_FRIENDS).build();
        assertTrue("Error: UriMatcher on Friends failed: " +
                uriFriends.toString(), SportProvider.getsUriMatcher().match(uriFriends) == SportProvider.FRIENDS);
        Uri uriFriends_ID = SportContract.FriendsEntry.buildFriendsUri(123);
        assertTrue("Error: UriMatcher on Friends failed: " +
                uriFriends_ID.toString(), SportProvider.getsUriMatcher().match(uriFriends_ID) == SportProvider.FRIENDS_FT_ID);

        builder = new Uri.Builder();
        Uri uriPhotos =
                builder.authority(SportContract.CONTENT_AUTHORITY).
                        appendPath(SportContract.PATH_PHOTOS).build();
        assertTrue("Error: UriMatcher on Photos failed: " +
                uriPhotos.toString(), SportProvider.getsUriMatcher().match(uriPhotos) == SportProvider.PHOTOS);
        Uri uriPhotos_ID = SportContract.PhotosEntry.buildPhotosUri(123);
        assertTrue("Error: UriMatcher on Photos failed: " +
                uriPhotos_ID.toString(), SportProvider.getsUriMatcher().match(uriPhotos_ID) == SportProvider.PHOTOS_ID);

        builder = new Uri.Builder();
        Uri uriAttributes =
                builder.authority(SportContract.CONTENT_AUTHORITY).
                        appendPath(SportContract.PATH_ATTRIBUTES).build();
        assertTrue("Error: UriMatcher on Attributes failed: " +
                uriAttributes.toString(), SportProvider.getsUriMatcher().match(uriAttributes) == SportProvider.ATTRIBUTES);
        Uri uriAttributes_ID = SportContract.AttributesEntry.buildAttributesUri(123);
        assertTrue("Error: UriMatcher on Attributes failed: " +
                uriAttributes_ID.toString(), SportProvider.getsUriMatcher().match(uriAttributes_ID) == SportProvider.ATTRIBUTES_ID);
        Log.d(TAG, "testUriMatcher: "+uriAttributes_ID);

        Uri uriCommentariesVenue = SportContract.CommentariesEntry.buildCommentariesWithVenueUri(123);
        assertTrue("Error: UriMatcher on Commentaries Venue failed: " +
                uriCommentariesVenue.toString()+" returning "+SportProvider.getsUriMatcher().match(uriCommentariesVenue),
                SportProvider.getsUriMatcher().match(uriCommentariesVenue) == SportProvider.COMMENTARIES_VENUE_ID);
        
        Uri uriCommentariesParent = SportContract.CommentariesEntry.buildCommentariesWithParentUri(123);
        assertTrue("Error: UriMatcher on Commentaries Parent failed: " +
                uriCommentariesParent.toString(),
                SportProvider.getsUriMatcher().match(uriCommentariesParent) == SportProvider.COMMENTARIES_PARENT_ID);
        
        Uri uriPlayerTeamAttributes = SportContract.PlayerEntry.buildPlayerWithTeamAndAttributes(123);
        assertTrue("Error: UriMatcher on Player Team Attributes failed: " +
                        uriPlayerTeamAttributes.toString(),
                SportProvider.getsUriMatcher().match(uriPlayerTeamAttributes) == SportProvider.PLAYER_TEAM_ATTRIBUTES);
        
        Uri uriVenuePlayerCommentariesPhotos = SportContract.VenueEntry.buildVenueWithPlayerCommentariesAndPhotosUri(123);
        assertTrue("Error: UriMatcher on Venue Player Commentaries failed: " +
                        uriVenuePlayerCommentariesPhotos.toString(),
                SportProvider.getsUriMatcher().match(uriVenuePlayerCommentariesPhotos) == SportProvider.VENUE_PLAYER_COMMENTARIES_PHOTOS);
        
        Uri uriPlayerFriendAttributes = SportContract.PlayerEntry.buildPlayerWithFriendsAndAttributes(123);
        assertTrue("Error: UriMatcher on Player Friends Attributes failed: " +
                        uriPlayerFriendAttributes.toString(),
                SportProvider.getsUriMatcher().match(uriPlayerFriendAttributes) == SportProvider.PLAYER_FRIENDS_ATTRIBUTES);

        Uri uriMatchVenueTeam = SportContract.MatchEntry.buildMathWithVenueAndPhotosUri(123);
        assertTrue("Error: UriMatcher on Player Friends Attributes failed: " +
                        uriMatchVenueTeam.toString(),
                SportProvider.getsUriMatcher().match(uriMatchVenueTeam) == SportProvider.MATCH_VENUE_TEAM);

    }
    
    @Test
    public void testInsert() {
        ContentValues values = TestUtilities.createPlayerValues();
        Uri uri = null;
        uri = mProvider.insert(SportContract.PlayerEntry.CONTENT_URI, values);
        assertTrue(uri!=null);
        values = TestUtilities.createAttributesValues(Long.parseLong(SportContract.PlayerEntry.getPlayerIdFromUri(uri)));
        uri = null;
        uri = mProvider.insert(SportContract.AttributesEntry.CONTENT_URI, values);
        assertTrue(uri!=null);
        values = TestUtilities.createVenueValues();
        mProvider.insert(SportContract.VenueEntry.CONTENT_URI, values);
    }
    
    @Test
    public void testQuery() {
        ContentValues values = TestUtilities.createPlayerValues();
        Uri uri = null;
        uri = mProvider.insert(SportContract.PlayerEntry.CONTENT_URI, values);
        String selection = SportContract.PlayerEntry.TABLE_NAME +
                "." + SportContract.PlayerEntry._ID + " = ? ";
        Cursor c = mProvider.query(uri, null, selection, new String[]{SportContract.PlayerEntry.getPlayerIdFromUri(uri)}, null, null);
        assertTrue(c.moveToFirst());
    }
}
