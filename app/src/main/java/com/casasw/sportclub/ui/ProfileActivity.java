package com.casasw.sportclub.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.casasw.sportclub.data.SportContract;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;

import static com.casasw.sportclub.ui.LoginActivity.EXTRA_EMAIL;
import static com.casasw.sportclub.ui.LoginActivity.EXTRA_NAME;
import static com.casasw.sportclub.ui.LoginActivity.EXTRA_PHOTO;
import static com.casasw.sportclub.ui.LoginActivity.EXTRA_URI;
import static com.casasw.sportclub.ui.R.menu.profile;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private static final int LOADER_ID = 14;
    private Uri mPhotoURL;
    private String mEmail;
    private String mName;
    private Uri mUri;


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
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        View rootView = findViewById(android.R.id.content);

        ViewHolder viewHolder = new ViewHolder(rootView);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_NAME, mName);
                bundle.putString(EXTRA_EMAIL, mEmail);
                bundle.putParcelable(EXTRA_URI, mUri);
                bundle.putParcelable(EXTRA_PHOTO,mPhotoURL);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        Bundle bundle = getIntent().getExtras();
        mUri = bundle.getParcelable(EXTRA_URI);
        boolean update = false;
        if (mPhotoURL == null || !mPhotoURL.equals(bundle.getString(EXTRA_PHOTO))) {
            mPhotoURL = bundle.getParcelable(EXTRA_PHOTO);
            update = true;
        }
        if (mName == null || !mName.equals(bundle.getString(EXTRA_NAME))) {
            mName = bundle.getString(EXTRA_NAME);
            update = true;
        }
        if (mEmail == null || !mEmail.equals(bundle.getString(EXTRA_EMAIL))) {
            mEmail = bundle.getString(EXTRA_EMAIL);
            update = true;
        }
        //update name and photo
        //need to srudy if this is really necessary.
        if (update) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String selection = SportContract.PlayerEntry.TABLE_NAME +
                            "." + SportContract.PlayerEntry.COLUMN_EMAIL + " = ? ";
                    Cursor c = getContentResolver().query(SportContract.PlayerEntry.buildPlayerUri(),null, selection, new String[]{mEmail}, null);
                    if (c !=null){
                        if (c.moveToFirst()){
                            ContentValues cv = new ContentValues();
                            cv.put(SportContract.PlayerEntry.COLUMN_PLAYER_NAME, mName);
                            cv.put(SportContract.PlayerEntry.COLUMN_PROFILE_PHOTO, mPhotoURL.toString());
                            int up = getContentResolver().update(SportContract.PlayerEntry.buildPlayerUri(),cv,selection, new String[]{mEmail});
                            if (up==1 && BuildConfig.DEBUG){
                                Log.d(TAG, "run: User photo and name updated.");
                            }
                        } else {
                            ContentValues cv = Utility.createPlayerValues(mName, mPhotoURL.toString(), mEmail);
                            Uri uri = getContentResolver().insert(SportContract.PlayerEntry.buildPlayerUri(), cv);
                            cv = Utility.createAttributesValues(Long.parseLong(SportContract.PlayerEntry.getPlayerIdFromUri(uri)));
                            Uri uriAttr = getContentResolver().insert(SportContract.AttributesEntry.buildAttributesUri(), cv);
                            cv = Utility.createPlayerSportValues(Long.parseLong(SportContract.PlayerEntry.getPlayerIdFromUri(uri)), 1);
                            Uri uriSport = getContentResolver().insert(SportContract.PlayerSportEntry.buildPlayerSportUri(), cv);
                            if (BuildConfig.DEBUG){
                                Log.d(TAG,
                                        "run: Player Profile created with id (" +
                                                SportContract.PlayerEntry.getPlayerIdFromUri(uri) +
                                                ") and attributes (" +
                                                SportContract.AttributesEntry.getIdFromUri(uriAttr) +
                                                ") and sport (" +
                                                SportContract.PlayerSportEntry.getIdFromUri(uriSport));
                            }
                        }
                        c.close();
                        //getSupportLoaderManager().restartLoader(LOADER_ID, null, ProfileActivity.this);
                    }
                }
            }).start();
        }


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

        viewHolder.mNameTextView.setText(mName);

        viewHolder.mNoSport.setVisibility(View.VISIBLE);
        viewHolder.mTextViewSoccer.setVisibility(View.GONE);
        viewHolder.mTextViewBasket.setVisibility(View.GONE);
        viewHolder.mTextViewSoccerPos.setVisibility(View.GONE);
        viewHolder.mTextViewSoccerPos2.setVisibility(View.GONE);
        viewHolder.mTextViewBasketPos.setVisibility(View.GONE);
        viewHolder.mTextViewBasketPos2.setVisibility(View.GONE);
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
        getMenuInflater().inflate(profile, menu);
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
                intent = new Intent(this, EditProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_NAME, mName);
                bundle.putString(EXTRA_EMAIL, mEmail);
                bundle.putParcelable(EXTRA_URI, mUri);
                bundle.putParcelable(EXTRA_PHOTO,mPhotoURL);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
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

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            if (BuildConfig.DEBUG) {
                Utility.logCursor(data, TAG);
                data.moveToFirst();
                Log.d(TAG, "onLoadFinished: "+ data.getPosition());
            }
            ViewHolder viewHolder = (ViewHolder) findViewById(android.R.id.content).getTag();

            viewHolder.mTextRating.setText(getString(R.string.format_rate,data.getFloat(COL_PLAYER_RATING)));

            int[][] columns = new int[][]{
                    {COL_PLAYER_EMAIL},
                    {COL_PLAYER_CITY, COL_PLAYER_STATE},
                    {COL_PLAYER_BDAY},
                    {COL_PLAYER_HEIGHT},
                    {COL_PLAYER_WEIGHT}};
            int[] icons = new int[] {R.drawable.ic_email,
                    R.drawable.ic_place,R.drawable.ic_cake,
                    R.drawable.ic_accessibility, R.drawable.ic_action_ruler};
            View item;
            ImageView imageView;
            TextView textView;
            TextView textView2;
            String aux[][] = new String[][]{
                    {""},
                    {getString(R.string.user_city_eg), getString(R.string.user_state_eg)},
                    {getString(R.string.user_bd_eg)},
                    {getString(R.string.user_height_eg)},
                    {getString(R.string.user_weight_eg)}};
            String text;
            for (int i=0;i<5;i++) {
                item = LayoutInflater
                                .from(this)
                                .inflate(R.layout.item_image_text_text, null);
                imageView = (ImageView) item.findViewById(R.id.image_view_item);
                imageView.setBackground(getResources().getDrawable(icons[i]));
                textView = (TextView) item.findViewById(R.id.text_view_item);
                text = data.getString(columns[i][0]);
                if (text.equals(""))
                    text = aux[i][0];
                textView.setText(text);
                if (i==1) {
                    textView2 = (TextView) item.findViewById(R.id.text_view_item2);
                    text = data.getString(columns[i][1]);
                    if (text.equals(""))
                        text = aux[i][1];
                    textView2.setText(text);
                }
                viewHolder.mImageTextContainer.addView(item);
            }


            do{
                String sport = data.getString(COL_SPORTS_NAME);
                String pos; String[] posV;
                //Soccer Basketball
                switch (sport){
                    case "Soccer":
                        viewHolder.mNoSport.setVisibility(View.GONE);
                        viewHolder.mTextViewSoccer.setVisibility(View.VISIBLE);
                        pos = data.getString(COL_SPORTS_POSITION);
                        viewHolder.mTextViewSoccerPos.setVisibility(View.VISIBLE);
                        if (viewHolder.mTextViewSoccerPos.getText().equals("")) {
                            viewHolder.mTextViewSoccerPos.setText(pos);
                        } else {
                            viewHolder.mTextViewSoccerPos2.setVisibility(View.VISIBLE);
                            viewHolder.mTextViewSoccerPos2.setText(pos);
                        }
                        //text position visibility
                        break;
                    case "Basketball":
                        viewHolder.mNoSport.setVisibility(View.GONE);
                        viewHolder.mTextViewBasket.setVisibility(View.VISIBLE);
                        pos = data.getString(COL_SPORTS_POSITION);
                        viewHolder.mTextViewBasketPos.setVisibility(View.VISIBLE);
                        if (viewHolder.mTextViewBasketPos.getText().equals("")) {
                            viewHolder.mTextViewBasketPos.setText(pos);
                        } else {
                            viewHolder.mTextViewBasketPos2.setVisibility(View.VISIBLE);
                            viewHolder.mTextViewBasketPos2.setText(pos);
                        }
                        break;
                }
            }while (data.moveToNext());
            data.moveToFirst();
            /*viewHolder.mImageViewSpinner.setBackground(getResources().getDrawable(R.drawable.ic_fiber_manual_record));
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                            R.array.soccer_position_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            viewHolder.mTextViewSoccerPos.setAdapter(adapter);
            adapter = ArrayAdapter.createFromResource(this,
                            R.array.basket_position_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            viewHolder.mTextViewSoccerPos2.setAdapter(adapter);

            viewHolder.mImageViewHand.setBackground(getResources().getDrawable(R.drawable.ic_pan_tool));
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.hand_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            viewHolder.mTextViewHand.setAdapter(adapter);*/
            // I need to check which one is selected

            String hand = data.getString(COL_PLAYER_HAND);
            if (hand.trim().equals(""))
                hand = getString(R.string.hand_default);
            viewHolder.mTextViewHand.setText(hand);
            int [] columns_attr = new int[]{
                    COL_ATTRIBUTES_SPEED,
                    COL_ATTRIBUTES_POWER,
                    COL_ATTRIBUTES_TECH,
                    COL_ATTRIBUTES_FIT,
                    COL_ATTRIBUTES_FAIR};
            final String[] columns_names = new String[] {getString(R.string.attribute_speed), getString(R.string.attribute_power),
                    getString(R.string.attributes_tech), getString(R.string.attribute_fit),
                    getString(R.string.attribute_fair)};
            RatingBar ratingBar;
            TextView  rate;
            float[] ratings = new float[5];

            for (int i=0;i<5;i++) {
                item = LayoutInflater
                        .from(this)
                        .inflate(R.layout.item_attributes, null);
                textView = (TextView) item.findViewById(R.id.text_view_attributes);
                textView.setText(columns_names[i]);
                rate = (TextView) item.findViewById(R.id.text_view_rating);
                ratings[i] = data.getFloat(columns_attr[i]);
                rate.setText(getString(R.string.format_rate,data.getFloat(columns_attr[i])));
                viewHolder.mAttributes.addView(item);
            }

            viewHolder.mRadar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            viewHolder.mRadar.getDescription().setEnabled(false);
            viewHolder.mRadar.setWebLineWidth(1f);
            viewHolder.mRadar.setWebColor(getResources().getColor(R.color.colorAccent));
            viewHolder.mRadar.setWebLineWidthInner(1f);
            viewHolder.mRadar.setWebColorInner(getResources().getColor(R.color.colorPrimaryLight));
            viewHolder.mRadar.setWebAlpha(100);
            MarkerView mv = new RadarMarkerView(this, R.layout.radar_markerview);
            mv.setChartView(viewHolder.mRadar);
            viewHolder.mRadar.setMarker(mv);
            viewHolder.mRadar.setData(setData(ratings));
            viewHolder.mRadar.invalidate();

            viewHolder.mRadar.animateXY(
                    1400, 1400, Easing.EasingOption.EaseInOutQuad,Easing.EasingOption.EaseInOutQuad);
            XAxis xAxis = viewHolder.mRadar.getXAxis();
            xAxis.setTypeface(Typeface.DEFAULT);
            xAxis.setTextSize(9f);
            xAxis.setYOffset(0f);
            xAxis.setYOffset(0f);
            xAxis.setValueFormatter(new IAxisValueFormatter() {

                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return columns_names[(int) value % columns_names.length];
                }
            });
            xAxis.setTextColor(Color.BLACK);

            YAxis yAxis = viewHolder.mRadar.getYAxis();
            yAxis.setTypeface(Typeface.DEFAULT);
            yAxis.setLabelCount(5, false);
            yAxis.setTextSize(9f);
            yAxis.setAxisMinimum(0f);
            yAxis.setAxisMaximum(10f);
            yAxis.setDrawLabels(false);

            Legend l = viewHolder.mRadar.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setTypeface(Typeface.DEFAULT);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(5f);
            l.setTextColor(Color.BLACK);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    public void onCheckBoxClicked(View view){
        //update sport table
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.text_view_soccer:
                if (BuildConfig.DEBUG){
                    Log.d(TAG, "onCheckBoxClicked - Soccer checkbox: " + checked);
                }
                if (checked) {
                    //add soccer to player_sport

                }
                else {
                    //remove soccer in player_sport
                }
                break;
            case R.id.text_view_basket:
                if (BuildConfig.DEBUG){
                    Log.d(TAG, "onCheckBoxClicked - Basket checkbox" + checked);
                }
                if (checked) {
                    //add basket to player_sport
                }
                else {
                    //remove basket in player_sport
                }
                break;
        }
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

    public RadarData setData(float[] ratings) {
        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (float rating : ratings) {
            entries1.add(new RadarEntry(rating));
        }

        RadarDataSet set1 = new RadarDataSet(entries1, "Attributes");
        set1.setColor(getResources().getColor(R.color.colorAccent));
        set1.setFillColor(getResources().getColor(R.color.colorAccent));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);


        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);


        RadarData data = new RadarData(sets);
        data.setValueTypeface(Typeface.DEFAULT);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);
        return data;
    }


    private static class ViewHolder {
        final NavigationView mNavigationView;
        final ImageView mProfilePhoto;
        final TextView mNameTextView;
        final ImageView mImageStarRating;
        final TextView mTextRating;
        final LinearLayout mImageTextContainer;
        final TextView mNoSport;
        final TextView mTextViewSoccer;
        final TextView mTextViewBasket;
        final TextView mTextViewSoccerPos;
        final TextView mTextViewSoccerPos2;
        final TextView mTextViewBasketPos;
        final TextView mTextViewBasketPos2;
        final TextView mTextViewHand;
        final LinearLayout mAttributes;
        final RadarChart mRadar;

        ViewHolder (View view){
            mNavigationView = (NavigationView) view.findViewById(R.id.nav_view);
            mProfilePhoto = (ImageView) view.findViewById(R.id.image_view_profile);
            mNameTextView = (TextView) view.findViewById(R.id.text_view_name_toolbar);
            mImageStarRating = (ImageView) view.findViewById(R.id.image_view_star);
            mTextRating = (TextView) view.findViewById(R.id.text_view_rating);
            mImageTextContainer = (LinearLayout) view.findViewById(R.id.image_text_container);
            mNoSport = (TextView) view.findViewById(R.id.text_view_no_sport);
            mTextViewSoccer = (TextView) view.findViewById(R.id.text_view_soccer);
            mTextViewBasket = (TextView) view.findViewById(R.id.text_view_basket);
            mTextViewSoccerPos = (TextView) view.findViewById(R.id.text_view_soccer_pos1);
            mTextViewSoccerPos2 = (TextView) view.findViewById(R.id.text_view_soccer_pos2);
            mTextViewBasketPos = (TextView) view.findViewById(R.id.text_view_basket_pos1);
            mTextViewBasketPos2 = (TextView) view.findViewById(R.id.text_view_basket_pos2);
            mTextViewHand = (TextView) view.findViewById(R.id.text_view_hand);
            mAttributes = (LinearLayout) view.findViewById(R.id.list_attributes);
            mRadar = (RadarChart) view.findViewById(R.id.radar_item);
        }

    }
}
