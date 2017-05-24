package com.casasw.sportclub.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.casasw.sportclub.data.SportContract;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.Scope;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.casasw.sportclub.ui.LoginActivity.EXTRA_EMAIL;
import static com.casasw.sportclub.ui.LoginActivity.EXTRA_NAME;
import static com.casasw.sportclub.ui.LoginActivity.EXTRA_PHOTO;
import static com.casasw.sportclub.ui.LoginActivity.EXTRA_URI;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoadingActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LoadingActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    @BindView(R.id.fullscreen_content) ImageView mLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);
        ButterKnife.bind(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoom);
        mLogo.startAnimation(animation);
        mLogo.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLogo.setVisibility(View.GONE);
                if (isFacebookLoggedIn()) {
                    Intent intent = new Intent(LoadingActivity.this, ProfileActivity.class);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoadingActivity.this);
                    String[] selectionArgs = new String[]{prefs.getString(getString(R.string.pref_user_email_key), getString(R.string.error_email))};
                    Cursor c = getContentResolver().query(SportContract.PlayerEntry.buildPlayerUri(),
                            null,
                            SportContract.PlayerEntry.TABLE_NAME+"."+ SportContract.PlayerEntry.COLUMN_EMAIL+" = ?",
                            selectionArgs, null);

                    if (c != null && c.moveToFirst()) {
                        if (BuildConfig.DEBUG)
                            Utility.logCursor(c, TAG);
                        intent = new Intent(LoadingActivity.this, MainActivity.class);
                        c.close();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(EXTRA_NAME, prefs.getString(getString(R.string.pref_user_name_key), getString(R.string.error_name)));
                    bundle.putString(EXTRA_EMAIL, prefs.getString(getString(R.string.pref_user_email_key), getString(R.string.error_email)));
                    bundle.putString(EXTRA_URI,
                            SportContract.PlayerEntry
                                    .buildPlayerWithSportAndAttributes(
                                            prefs.getString(getString(R.string.pref_user_email_key), getString(R.string.error_email))).toString());
                    Profile profile = Profile.getCurrentProfile();
                    bundle.putString(EXTRA_PHOTO,profile.getProfilePictureUri(100, 100).toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else {
                    OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
                    if (opr.isDone()) {
                        // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                        // and the GoogleSignInResult will be available instantly.
                        Log.d(TAG, "Got cached sign-in");
                        GoogleSignInResult result = opr.get();
                        handleSignInResult(result);
                    } else {
                        /*// If the user has not previously signed in on this device or the sign-in has expired,
                        // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                        // single sign-on will occur in this branch.
                        //showProgressDialog();
                        opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                            @Override
                            public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                                handleSignInResult(googleSignInResult);
                            }
                        });*/
                        Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }


                }
            }
        }, 1000);


    }
    private boolean isFacebookLoggedIn() {
        AccessToken accesstoken = AccessToken.getCurrentAccessToken();
        return !(accesstoken == null || accesstoken.getPermissions().isEmpty());
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct==null) {Log.e(TAG, "handleSignInResult: Error account object is null.");return;}
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().putString(getString(R.string.pref_account_key),
                    getString(R.string.pref_account_google)).apply();
            Intent intent = new Intent(this, ProfileActivity.class);
            String[] selectionArgs = new String[]{acct.getEmail()};
            Cursor c = getContentResolver().query(SportContract.PlayerEntry.buildPlayerUri(),
                    null,
                    SportContract.PlayerEntry.TABLE_NAME+"."+ SportContract.PlayerEntry.COLUMN_EMAIL+" = ?",
                    selectionArgs, null);

            if (c != null && c.moveToFirst()) {
                if (BuildConfig.DEBUG)
                    Utility.logCursor(c, TAG);
                intent = new Intent(LoadingActivity.this, MainActivity.class);
                c.close();
            }
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_NAME, acct.getDisplayName());
            bundle.putString(EXTRA_EMAIL, acct.getEmail());
            bundle.putParcelable(EXTRA_PHOTO,acct.getPhotoUrl());
            bundle.putParcelable(EXTRA_URI,
                    SportContract.PlayerEntry
                            .buildPlayerWithSportAndAttributes(
                                    acct.getEmail().replace("@", "(at)")));
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
}
