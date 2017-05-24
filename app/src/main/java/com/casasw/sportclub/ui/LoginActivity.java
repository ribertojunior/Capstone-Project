package com.casasw.sportclub.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.casasw.sportclub.data.SportContract;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.casasw.sportclub.ui.Utility.isOnline;

/**
 * A login screen that offers login via google/facebook.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = LoginActivity.class.getSimpleName();
    static final String EXTRA_NAME = "NAME";
    static final String EXTRA_PHOTO = "PHOTO";
    static final String EXTRA_EMAIL = "EMAIL";
    static final String EXTRA_URI = "URI";

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private static final int RC_SIGN_IN = 9001;
    private boolean OUT;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View rootView = findViewById(android.R.id.content);
        /*ImageView logoLoading = (ImageView) findViewById(R.id.logo_loading);
        Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.zoom);
        Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.pop_in);
        AnimationSet outIn = new AnimationSet(true);
        outIn.addAnimation(animationOut);
        outIn.addAnimation(animationIn);*/
        //logoLoading.startAnimation(outIn);

        final ViewHolder viewHolder = new ViewHolder(rootView);
        OUT = false;

        /*Facebook login*/
        callbackManager = CallbackManager.Factory.create();
        //LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        viewHolder.mFacebookBtn.setReadPermissions(Arrays.asList("email", "user_friends"));


        viewHolder.mFacebookBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookLoginSuccess(loginResult);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "onError: "+exception.toString());
            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    OUT = false;
                    viewHolder.mSignOut.setVisibility(View.GONE);
                    viewHolder.mGoogleBtn.setVisibility(View.VISIBLE);
                    viewHolder.mFacebookBtn.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        };


        /*End of Facebook*/

        /*google*/
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Set the dimensions of the sign-in button.
        //SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        viewHolder.mGoogleBtn.setSize(SignInButton.SIZE_STANDARD);
        viewHolder.mGoogleBtn.setScopes(gso.getScopeArray());

        viewHolder.mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        String action = getIntent().getAction();
        if (action != null && action.equals("com.casasw.ui.LoginActivity")) {
            OUT = true;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String account = prefs.getString(getString(R.string.pref_account_key), getString(R.string.pref_account_default));
            if (account.equals(getString(R.string.pref_account_google))) {
                viewHolder.mSignOut.setVisibility(View.VISIBLE);
                viewHolder.mFacebookBtn.setVisibility(View.GONE);
            }
            viewHolder.mGoogleBtn.setVisibility(View.GONE);
        }

        if (isFacebookLoggedIn() && !OUT) {
            viewHolder.mGoogleBtn.setVisibility(View.GONE);
            viewHolder.mSignOut.setVisibility(View.GONE);
            Intent intent = new Intent(this, ProfileActivity.class);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_NAME, prefs.getString(getString(R.string.pref_user_name_key), getString(R.string.error_name)));
            bundle.putString(EXTRA_EMAIL, prefs.getString(getString(R.string.pref_user_email_key), getString(R.string.error_email)));
            bundle.putString(EXTRA_URI,
                    SportContract.PlayerEntry
                            .buildPlayerWithSportAndAttributes(
                                    prefs.getString(getString(R.string.pref_user_email_key), getString(R.string.error_email))).toString());
            Profile profile = Profile.getCurrentProfile();
            bundle.putString(EXTRA_PHOTO,profile.getProfilePictureUri(100, 100).toString());
            intent.putExtras(bundle);
            startActivity(intent);
        }

       if (!isOnline(this) && !isLoggedIn()) {
            viewHolder.mGoogleBtn.setEnabled(false);
            viewHolder.mFacebookBtn.setEnabled(false);
            Toast.makeText(this, R.string.offline_error, Toast.LENGTH_LONG).show();
        }


        rootView.setTag(viewHolder);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewHolder viewHolder = (ViewHolder) findViewById(android.R.id.content).getTag();
        if (isOnline(this)) {
            viewHolder.mGoogleBtn.setEnabled(true);
            viewHolder.mFacebookBtn.setEnabled(true);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (!OUT) {
            if (opr.isDone()) {
                // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                // and the GoogleSignInResult will be available instantly.
                Log.d(TAG, "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                // If the user has not previously signed in on this device or the sign-in has expired,
                // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                // single sign-on will occur in this branch.
                //showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                        hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (!OUT) {
            if (requestCode == RC_SIGN_IN) { //google
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            } else { //facebook
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        ViewHolder viewHolder = (ViewHolder) findViewById(android.R.id.content).getTag();
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct==null) {Log.e(TAG, "handleSignInResult: Error account object is null.");return;}
            viewHolder.mGoogleBtn.setVisibility(View.GONE);
            viewHolder.mFacebookBtn.setVisibility(View.GONE);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().putString(getString(R.string.pref_account_key),
                    getString(R.string.pref_account_google)).apply();
            Intent intent = new Intent(this, ProfileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_NAME, acct.getDisplayName());
            bundle.putString(EXTRA_EMAIL, acct.getEmail());
            bundle.putParcelable(EXTRA_PHOTO,acct.getPhotoUrl());
            bundle.putParcelable(EXTRA_URI,
                    SportContract.PlayerEntry
                            .buildPlayerWithSportAndAttributes(
                                    acct.getEmail().replace("@", "(at)")));
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //updateUI(true);
        } else {
            viewHolder.mGoogleBtn.setVisibility(View.VISIBLE);
            viewHolder.mFacebookBtn.setVisibility(View.VISIBLE);
            viewHolder.mSignOut.setVisibility(View.GONE);
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    public void helpOnClick(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public void signOut(View view) {
        showProgressDialog();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        OUT = false;
                        ViewHolder viewHolder = (ViewHolder) findViewById(android.R.id.content).getTag();
                        viewHolder.mSignOut.setVisibility(View.GONE);
                        viewHolder.mGoogleBtn.setVisibility(View.VISIBLE);
                        viewHolder.mFacebookBtn.setVisibility(View.VISIBLE);
                        // [END_EXCLUDE]
                    }
                });

        //LoginManager.getInstance().logOut();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private boolean isLoggedIn() {
        AccessToken accesstoken = AccessToken.getCurrentAccessToken();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        boolean google = opr.isDone();
        boolean facebook = false;
        if (!(accesstoken == null || accesstoken.getPermissions().isEmpty())){
            facebook = true;
        }
        return (facebook || google);
    }

    private boolean isFacebookLoggedIn() {
        AccessToken accesstoken = AccessToken.getCurrentAccessToken();
        return !(accesstoken == null || accesstoken.getPermissions().isEmpty());
    }


    private void facebookLoginSuccess(LoginResult loginResult) {
        showProgressDialog();
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    Profile profile = Profile.getCurrentProfile();
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    ViewHolder viewHolder = (ViewHolder) findViewById(android.R.id.content).getTag();
                    Bundle bundle = new Bundle();
                    viewHolder.mGoogleBtn.setVisibility(View.GONE);
                    viewHolder.mSignOut.setVisibility(View.GONE);
                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                    String aux = object.getString("name");
                    prefs.edit().putString(getString(R.string.pref_user_name_key), aux).apply();
                    bundle.putString(EXTRA_NAME, aux);

                    prefs.edit().putString(getString(R.string.pref_user_email_key), object.getString("email")).apply();
                    bundle.putString(EXTRA_EMAIL,aux);
                    bundle.putString(EXTRA_PHOTO,profile.getProfilePictureUri(100, 100).toString());
                    bundle.putParcelable(EXTRA_URI,
                            SportContract.PlayerEntry
                                    .buildPlayerWithSportAndAttributes(
                                            object.getString("email").replace("@","(at)")));
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    hideProgressDialog();
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("fields", "id,name, email");
        request.setParameters(bundle);
        request.executeAsync();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        prefs.edit().putString(getString(R.string.pref_account_key),
                getString(R.string.pref_account_facebook)).apply();
        hideProgressDialog();
    }

    private static class ViewHolder {
        final SignInButton mGoogleBtn;
        final LoginButton mFacebookBtn;
        final Button mSignOut;
        final LinearLayout mLoginLayout;

        ViewHolder(View view){
            mGoogleBtn = (SignInButton) view.findViewById(R.id.sign_in_button);
            mFacebookBtn = (LoginButton) view.findViewById(R.id.login_button);
            mSignOut = (Button) view.findViewById(R.id.sign_out_button);
            mLoginLayout = (LinearLayout) view.findViewById(R.id.login_container);
        }
    }
}

