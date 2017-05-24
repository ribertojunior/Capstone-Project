package com.casasw.sportclub.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.casasw.sportclub.data.SportContract;

public class EditProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener {

    private static final String TAG = EditProfileActivity.class.getSimpleName();
    private static final int LOADER_ID = 446;
    private Uri mPhotoURL;
    private String mEmail;
    private String mName;
    private Uri mUri;
    private ContentValues mCVPlayer, mCVPlayerSoccer, mCVPlayerBasket, mCVPlayerSoccer2, mCVPlayerBasket2;

    static final String[] PLAYER_SPORTS_ATTRIBUTES = {
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry._ID,
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry.COLUMN_PLAYER_NAME,
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry.COLUMN_HANDEDNESS,
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry.COLUMN_BDAY,
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry.COLUMN_HEIGHT,
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry.COLUMN_WEIGHT,
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry.COLUMN_CITY,
            SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry.COLUMN_STATE,
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
            SportContract.SportsEntry.TABLE_NAME +
                    "." + SportContract.SportsEntry.COLUMN_NAME,
            SportContract.SportsEntry.TABLE_NAME +
                    "." + SportContract.SportsEntry.COLUMN_STATUS,
            SportContract.PlayerSportEntry.TABLE_NAME +
                    "." + SportContract.PlayerSportEntry.COLUMN_POSITION

    };

    //indexes
    static final int COL_PLAYER_ID = 0;
    static final int COL_PLAYER_NAME = 1;
    static final int COL_PLAYER_HAND = 2;
    static final int COL_PLAYER_BDAY = 3;
    static final int COL_PLAYER_HEIGHT = 4;
    static final int COL_PLAYER_WEIGHT = 5;
    static final int COL_PLAYER_CITY = 6;
    static final int COL_PLAYER_STATE = 7;
    static final int COL_PLAYER_RATING = 8;
    static final int COL_PLAYER_EMAIL = 9;
    static final int COL_PLAYER_PHOTO = 10;
    static final int COL_ATTRIBUTES_SPEED = 11;
    static final int COL_ATTRIBUTES_POWER = 12;
    static final int COL_ATTRIBUTES_TECH = 13;
    static final int COL_ATTRIBUTES_FIT = 14;
    static final int COL_ATTRIBUTES_FAIR = 15;
    static final int COL_SPORTS_NAME = 16;
    static final int COL_SPORTS_STATUS = 17;
    static final int COL_SPORTS_POSITION = 18;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mCVPlayer = new ContentValues();
        mCVPlayerSoccer = new ContentValues();
        mCVPlayerBasket = new ContentValues();
        mCVPlayerSoccer2 = new ContentValues();
        mCVPlayerBasket2 = new ContentValues();

        View rootView = findViewById(android.R.id.content);
        ViewHolder viewHolder = new ViewHolder(rootView);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        Bundle bundle = getIntent().getExtras();
        mUri = bundle.getParcelable(LoginActivity.EXTRA_URI);
        mPhotoURL = bundle.getParcelable(LoginActivity.EXTRA_PHOTO);
        mName = bundle.getString(LoginActivity.EXTRA_NAME);
        mEmail = bundle.getString(LoginActivity.EXTRA_EMAIL);

        ImageView profile = (ImageView) header.findViewById(R.id.imageView_profile_nav);
        Glide.with(this)
                .load(mPhotoURL)
                .error(R.drawable.logo)
                .into(profile);

        TextView textView = (TextView) header.findViewById(R.id.textView_name_profile_nav);
        textView.setText(mName);

        textView = (TextView)  header.findViewById(R.id.textView_email_profile_nave);
        textView.setText(mEmail);

        Glide.with(this)
                .load(mPhotoURL)
                .error(R.drawable.logo)
                .into(viewHolder.mProfilePhoto);

        viewHolder.mProfileName.setText(mName);

        rootView.setTag(viewHolder);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case  R.id.nav_edit:
                intent = new Intent(this, SettingsActivity.class); break;
            case R.id.nav_preferrences:
                intent = new Intent(this, SettingsActivity.class); break;
            case R.id.nav_help:
                intent = new Intent(this, HelpActivity.class); break;
            default: throw new UnsupportedOperationException(TAG +" - Unknown nav draw option: "+ item);
        }
        startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mUri != null) {
            return new CursorLoader(this, mUri, PLAYER_SPORTS_ATTRIBUTES, null, null, null);
        }
        return null;
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            ViewHolder viewHolder = (ViewHolder) findViewById(android.R.id.content).getTag();
            viewHolder.mProfileRating.setText(getString(R.string.format_rate, data.getFloat(COL_PLAYER_RATING)));

            final int[][] columns = new int[][]{
                    {COL_PLAYER_CITY, COL_PLAYER_STATE},
                    {COL_PLAYER_BDAY},
                    {COL_PLAYER_HEIGHT},
                    {COL_PLAYER_WEIGHT}};
            int[] icons = new int[] {
                    R.drawable.ic_place,R.drawable.ic_cake,
                    R.drawable.ic_accessibility, R.drawable.ic_action_ruler};
            String aux[][] = new String[][]{
                    {getString(R.string.user_city_eg), getString(R.string.user_state_eg)},
                    {getString(R.string.user_bd_eg)},
                    {getString(R.string.user_height_eg)},
                    {getString(R.string.user_weight_eg)}};
            View item;
            ImageView imageView;
            EditText editText;
            EditText editText2;
            String text;
            for (int i=0;i<4;i++) {
                item = LayoutInflater
                        .from(this)
                        .inflate(R.layout.item_image_text_text_editable, null);
                imageView = (ImageView) item.findViewById(R.id.image_view_item);
                imageView.setBackground(getResources().getDrawable(icons[i]));
                editText = (EditText) item.findViewById(R.id.edit_text_item);
                final String columnName = data.getColumnName(columns[i][0]);
                final String egText = aux[i][0].substring(0,3);
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (!b){
                            String text = ((EditText) view).getText().toString();
                            if (!text.substring(0,3).equals(egText)) {
                                mCVPlayer.put(columnName, text);
                            }
                        }
                    }
                });
                editText.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        if (i == KeyEvent.KEYCODE_BACK &&
                                keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                            if (BuildConfig.DEBUG){
                                Log.d(TAG, "onKey: Back");
                            }
                            String text = ((EditText) view).getText().toString();
                            if (!text.substring(0,3).equals(egText)) {
                                mCVPlayer.put(columnName, text);
                            }
                        }
                        return false;
                    }
                });
                text = data.getString(columns[i][0]);
                if (text.equals(""))
                    text = aux[i][0];
                editText.setText(text);
                if (i==0) {
                    editText2 = (EditText) item.findViewById(R.id.edit_text_item2);
                    text = data.getString(columns[i][1]);
                    if (text.equals(""))
                        text = aux[i][1];
                    editText2.setText(text);
                    editText2.setVisibility(View.VISIBLE);
                    final String columnName2 = data.getColumnName(columns[i][1]);
                    final String egText2 = aux[i][1].substring(0,3);
                    editText2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean b) {
                            if (!b){
                                String text = ((EditText) view).getText().toString();
                                if (!text.substring(0,3).equals(egText2)) {
                                    mCVPlayer.put(columnName2, text);
                                }
                            }
                        }
                    });
                    editText2.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View view, int i, KeyEvent keyEvent) {
                            if (i == KeyEvent.KEYCODE_BACK &&
                                    keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                                if (BuildConfig.DEBUG){
                                    Log.d(TAG, "onKey: Back");
                                }
                                String text = ((EditText) view).getText().toString();
                                if (!text.substring(0,3).equals("Edit") &&
                                        !text.substring(0,3).equals("e.g.")) {
                                    mCVPlayer.put(columnName2, text);
                                }
                            }
                            return false;
                        }
                    });
                }
                viewHolder.mImageTextContainer.addView(item);
            }
            data.moveToFirst();
            final String playerID = data.getString(COL_PLAYER_ID);
            final String soccerID  = Utility.getSportID(this, "Soccer");
            final String basketID = Utility.getSportID(this, "Basketball");
            viewHolder.mCheckSoccer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((CheckBox) view).isChecked()) {
                        mCVPlayerSoccer.put(SportContract.PlayerSportEntry.COLUMN_PLAYER_ID, playerID);
                        mCVPlayerSoccer.put(SportContract.PlayerSportEntry.COLUMN_SPORT_ID, soccerID);
                        mCVPlayerSoccer2.put(SportContract.PlayerSportEntry.COLUMN_PLAYER_ID, playerID);
                        mCVPlayerSoccer2.put(SportContract.PlayerSportEntry.COLUMN_SPORT_ID, soccerID);
                    }
                    else {
                        mCVPlayerSoccer.remove(SportContract.PlayerSportEntry.COLUMN_PLAYER_ID);
                        mCVPlayerSoccer.remove(SportContract.PlayerSportEntry.COLUMN_SPORT_ID);
                        mCVPlayerSoccer.remove(SportContract.PlayerSportEntry.COLUMN_POSITION);
                        mCVPlayerSoccer2.remove(SportContract.PlayerSportEntry.COLUMN_PLAYER_ID);
                        mCVPlayerSoccer2.remove(SportContract.PlayerSportEntry.COLUMN_SPORT_ID);
                        mCVPlayerSoccer2.remove(SportContract.PlayerSportEntry.COLUMN_POSITION);
                    }
                }
            });
            viewHolder.mCheckBasket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((CheckBox) view).isChecked()) {
                        mCVPlayerBasket.put(SportContract.PlayerSportEntry.COLUMN_PLAYER_ID, playerID);
                        mCVPlayerBasket.put(SportContract.PlayerSportEntry.COLUMN_SPORT_ID, basketID);
                        mCVPlayerBasket2.put(SportContract.PlayerSportEntry.COLUMN_PLAYER_ID, playerID);
                        mCVPlayerBasket2.put(SportContract.PlayerSportEntry.COLUMN_SPORT_ID, basketID);
                    }
                    else {
                        mCVPlayerBasket.remove(SportContract.PlayerSportEntry.COLUMN_PLAYER_ID);
                        mCVPlayerBasket.remove(SportContract.PlayerSportEntry.COLUMN_SPORT_ID);
                        mCVPlayerBasket.remove(SportContract.PlayerSportEntry.COLUMN_POSITION);
                        mCVPlayerBasket2.remove(SportContract.PlayerSportEntry.COLUMN_PLAYER_ID);
                        mCVPlayerBasket2.remove(SportContract.PlayerSportEntry.COLUMN_SPORT_ID);
                        mCVPlayerBasket2.remove(SportContract.PlayerSportEntry.COLUMN_POSITION);
                    }
                }
            });
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.soccer_position_array, R.layout.spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            viewHolder.mSpinnerSoccer1.setAdapter(adapter);
            viewHolder.mSpinnerSoccer1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "onItemSelected: " + ((TextView)view).getText());
                    mCVPlayerSoccer.put(SportContract.PlayerSportEntry.COLUMN_POSITION, ((TextView)view).getText().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
            viewHolder.mSpinnerSoccer2.setAdapter(adapter);
            viewHolder.mSpinnerSoccer2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "onItemSelected: " + ((TextView)view).getText());

                    mCVPlayerSoccer2.put(SportContract.PlayerSportEntry.COLUMN_POSITION, ((TextView)view).getText().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.basket_position_array, R.layout.spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            viewHolder.mSpinnerBasket1.setAdapter(adapter);
            viewHolder.mSpinnerBasket1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "onItemSelected: " + ((TextView)view).getText());
                    mCVPlayerBasket.put(SportContract.PlayerSportEntry.COLUMN_POSITION, ((TextView)view).getText().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
            viewHolder.mSpinnerBasket2.setAdapter(adapter);
            viewHolder.mSpinnerBasket2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "onItemSelected: " + ((TextView)view).getText());
                    mCVPlayerBasket2.put(SportContract.PlayerSportEntry.COLUMN_POSITION, ((TextView)view).getText().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });

            adapter = ArrayAdapter.createFromResource(this,
                    R.array.hand_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            viewHolder.mSpinnerHand.setAdapter(adapter);
            viewHolder.mSpinnerHand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "onItemSelected: " + ((TextView)view).getText());
                    mCVPlayer.put(SportContract.PlayerEntry.COLUMN_HANDEDNESS, ((TextView)view).getText().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //update position column
        String pos = (String) adapterView.getItemAtPosition(i);
        if (BuildConfig.DEBUG){
            Log.d(TAG, "onItemSelected - position: "+pos);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        ViewHolder viewHolder = (ViewHolder) findViewById(android.R.id.content).getTag();
        if (mUri != null) {

            String selection = SportContract.PlayerEntry.TABLE_NAME +
                    "." + SportContract.PlayerEntry.COLUMN_EMAIL + " = ? ";
            String[] selectionArgs = new String[]{SportContract.PlayerEntry.getPlayerEmailFromUri(mUri)};
            if (BuildConfig.DEBUG) {
                Utility.logContentValues(mCVPlayer, TAG);
                Utility.logContentValues(mCVPlayerSoccer, TAG);
                Utility.logContentValues(mCVPlayerSoccer2, TAG);
                Utility.logContentValues(mCVPlayerBasket, TAG);
                Utility.logContentValues(mCVPlayerBasket2, TAG);
            }
            int up = getContentResolver().update(SportContract.PlayerEntry.buildPlayerUri(), mCVPlayer, selection, selectionArgs);
            int playerID = -1;
            long in_soccer, in_soccer2, in_basket, in_basket2;
            in_soccer = in_soccer2 = in_basket = in_basket2 = -1;

            Cursor c = getContentResolver().query(SportContract.PlayerEntry.buildPlayerUri(),null, selection, selectionArgs, null);
            if (c != null && c.moveToFirst()) {
                playerID = c.getInt(c.getColumnIndex(SportContract.PlayerEntry._ID));
                if (viewHolder.mCheckSoccer.isChecked()) {
                    if (mCVPlayerSoccer.containsKey(SportContract.PlayerSportEntry.COLUMN_POSITION)) {
                        in_soccer = SportContract.PlayerSportEntry.getLongIdFromUri(getContentResolver().insert(SportContract.PlayerSportEntry.buildPlayerSportUri(), mCVPlayerSoccer));
                    }
                    if (mCVPlayerSoccer2.containsKey(SportContract.PlayerSportEntry.COLUMN_POSITION)) {
                        in_soccer2 = SportContract.PlayerSportEntry.getLongIdFromUri(getContentResolver().insert(SportContract.PlayerSportEntry.buildPlayerSportUri(), mCVPlayerSoccer2));
                    }
                }
                if (viewHolder.mCheckBasket.isChecked()) {
                    if (mCVPlayerBasket.containsKey(SportContract.PlayerSportEntry.COLUMN_POSITION)) {
                        in_soccer = SportContract.PlayerSportEntry.getLongIdFromUri(getContentResolver().insert(SportContract.PlayerSportEntry.buildPlayerSportUri(), mCVPlayerBasket));
                    }
                    if (mCVPlayerBasket2.containsKey(SportContract.PlayerSportEntry.COLUMN_POSITION)) {
                        in_soccer2 = SportContract.PlayerSportEntry.getLongIdFromUri(getContentResolver().insert(SportContract.PlayerSportEntry.buildPlayerSportUri(), mCVPlayerBasket2));
                    }
                }
                c.close();
            }
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onStop: update - " + up);
                Log.d(TAG, "onStop: player id - "+ playerID);
                Log.d(TAG, "onStop: player sport soccer - " + in_soccer);
                Log.d(TAG, "onStop: player sport soccer2 - " + in_soccer2);
            }


        }
    }

    private static class ViewHolder {
        final ImageView mProfilePhoto;
        final TextView mProfileName;
        final TextView mProfileRating;
        final LinearLayout mImageTextContainer;
        final CheckBox mCheckSoccer;
        final CheckBox mCheckBasket;
        final Spinner mSpinnerSoccer1;
        final Spinner mSpinnerSoccer2;
        final Spinner mSpinnerBasket1;
        final Spinner mSpinnerBasket2;
        final Spinner mSpinnerHand;

        ViewHolder (View view) {
            mProfilePhoto = (ImageView) view.findViewById(R.id.image_view_profile);
            mProfileName = (TextView) view.findViewById(R.id.text_view_name_toolbar);
            mProfileRating = (TextView) view.findViewById(R.id.text_view_rating);
            mImageTextContainer = (LinearLayout) view.findViewById(R.id.image_text_container);
            mCheckSoccer = (CheckBox) view.findViewById(R.id.check_soccer);
            mCheckBasket = (CheckBox) view.findViewById(R.id.check_basket);
            mSpinnerSoccer1 = (Spinner) view.findViewById(R.id.spinner_soccer_pos1);
            mSpinnerSoccer2 = (Spinner) view.findViewById(R.id.spinner_soccer_pos2);
            mSpinnerBasket1 = (Spinner) view.findViewById(R.id.spinner_basket_pos1);
            mSpinnerBasket2 = (Spinner) view.findViewById(R.id.spinner_basket_pos2);
            mSpinnerHand = (Spinner) view.findViewById(R.id.spinner_hand);

        }
    }


}
