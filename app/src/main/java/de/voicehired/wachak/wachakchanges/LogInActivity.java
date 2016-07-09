package de.voicehired.wachak.wachakchanges;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.voicehired.wachak.R;
import de.voicehired.wachak.activity.MainActivity;

/**
 * Created by Vetero on 05-02-2016.
 */
public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    Button imageButton;
    ImageView imageView;
    LinearLayout linearLayout;
    Button fbimageButton;
    LoginButton fbLoginButton;
    private CallbackManager callbackManager;
    TextView logInTextView;

    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:" +
            "(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_log_in);

        imageButton = (Button) findViewById(R.id.bt_linked_in_log_in_activity);
        imageView = (ImageView) findViewById(R.id.iv_logo_sign_in_activity);
        linearLayout = (LinearLayout) findViewById(R.id.ll_log_in);
        fbimageButton = (Button) findViewById(R.id.ib_facebook_splash);
        fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
        logInTextView = (TextView) findViewById(R.id.tv_log_in_log_in_activity);

        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        imageView.setAnimation(animation1);

        callbackManager = CallbackManager.Factory.create();
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null) {
                    Log.d("accesstoken :", accessToken.getToken());
                    GraphRequest request = GraphRequest.newMeRequest(
                            accessToken,
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    try {
                                        Log.d("JSONResp accesstoken:", object.toString());
                                        Log.d("Graphresp accesstoken:", response.toString());
                                        Toast.makeText(LogInActivity.this, "Welcome " + object.getString("name"), Toast.LENGTH_SHORT).show();
                                        SharedPreferences sharedPreferences = getSharedPreferences
                                                (ConstantValues.MY_PREFS_NAME, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean(ConstantValues.SIGNED_KEY, true);
                                        editor.putString(ConstantValues.SIGNED_CLIENT, "F");
                                        editor.putString(ConstantValues.FACEBOOK_ACCESS_TOKEN, accessToken.getToken());
                                        editor.putString(ConstantValues.FB_NAME, object.getString("name"));
                                        editor.apply();
                                        JSONObject graphResponseJsonObject = response.getJSONObject();
                                        String hashValueString = md5("grq149" + graphResponseJsonObject.getString("email") + accessToken.getToken());
                                        Log.d("Hashvalue accesstoken", hashValueString);
                                        String urlToSaveUserString = ConstantValues.EMAIL_REGISTER_URL + graphResponseJsonObject.getString("email") + "/" + accessToken.getToken() + "/" + hashValueString;
                                        new SaveUserAsyncTask().execute(urlToSaveUserString);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,link,email");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(LogInActivity.this, "Call Log cancelled.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LogInActivity.this, "Call Log failed!.", Toast.LENGTH_SHORT).show();
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences(ConstantValues
                    .MY_PREFS_NAME, MODE_PRIVATE);
            boolean b = sharedPreferences.getBoolean(ConstantValues.SIGNED_KEY, false);
            if (b) {
                String s = sharedPreferences.getString(ConstantValues.SIGNED_CLIENT, "none");
                switch (s) {
                    case "L":
                        Toast.makeText(LogInActivity.this, "Welcome, " + sharedPreferences.getString
                                (ConstantValues.LINKEDIN_NAME, " to Wachak"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        break;

                    case "F":
                        Toast.makeText(LogInActivity.this, "Welcome, " + sharedPreferences.getString
                                (ConstantValues.FB_NAME, " to Wachak"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        break;

                    default:
                        break;
                }
            } else {
                fbimageButton.setOnClickListener(this);
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout.setAnimation(animation1);
                imageButton.setOnClickListener(this);
                logInTextView.setVisibility(View.VISIBLE);
            }
        }, 2000);
    }

    private String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_linked_in_log_in_activity:
                LISessionManager.getInstance(getApplicationContext()).init(LogInActivity.this,
                        buildScope(), new
                                AuthListener() {
                                    @Override
                                    public void onAuthSuccess() {
                                        Log.i("HomePage", "LinkedIn success");
                                    }

                                    @Override
                                    public void onAuthError(LIAuthError error) {
                                        Log.i("HomePage", "LinkedIn failed");
                                        Toast.makeText(getBaseContext(), "failed " + error.toString(), Toast.LENGTH_LONG)
                                                .show();
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                            finishAffinity();
                                        } else {
                                            finish();
                                        }
                                    }
                                }, true);
//        try {
//            PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(),
//                    PackageManager.GET_SIGNATURES);
//            Log.w(TAG, getPackageName());
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//
//                Log.w(TAG, Base64.encodeToString(md.digest(),
//                        Base64.NO_WRAP));
//            }
//        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
//            Log.d(TAG, e.getMessage(), e);
//        }
                break;

            case R.id.ib_facebook_splash:
                fbLoginButton.performClick();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else {
            LISessionManager.getInstance(getApplicationContext()).onActivityResult(this,
                    requestCode, resultCode, data);
            APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
            apiHelper.getRequest(LogInActivity.this, topCardUrl, new
                    ApiListener() {
                        @Override
                        public void onApiSuccess(ApiResponse result) {
                            try {
                                Toast.makeText(getBaseContext(), "success" + LISessionManager.getInstance
                                        (getApplicationContext()).getSession().getAccessToken().toString(), Toast.LENGTH_LONG).show();
                                JSONObject response = result.getResponseDataAsJson();
                                String accessTokenLinkedInString = LISessionManager.getInstance
                                        (getApplicationContext()).getSession().getAccessToken()
                                        .toString();
                                String name = (response.get("emailAddress").toString());
                                String email = (response.get("formattedName").toString
                                        ());
                                SharedPreferences sharedPreferences = getSharedPreferences
                                        (ConstantValues.MY_PREFS_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(ConstantValues.SIGNED_KEY, true);
                                editor.putString(ConstantValues.SIGNED_CLIENT, "L");
                                editor.putString(ConstantValues.LINKED_IN_ACCESS_TOKEN, accessTokenLinkedInString);
                                editor.putString(ConstantValues.LINKEDIN_NAME, name);
                                editor.putString(ConstantValues.LINKEDIN_EMAIL, email);
                                editor.apply();
                                String hashValueLinkedInString = md5("grq149" + email + accessTokenLinkedInString);
                                String urlToSaveUser = ConstantValues.EMAIL_REGISTER_URL + "/" + email + "/" + accessTokenLinkedInString + "/" + hashValueLinkedInString;
                                new SaveUserAsyncTask().execute(urlToSaveUser);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onApiError(LIApiError error) {
                            // ((TextView) findViewById(R.id.error)).setText(error.toString());

                        }
                    });
        }
    }

    private Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    private class SaveUserAsyncTask extends AsyncTask {

        String responseData;

        @Override
        protected Object doInBackground(Object[] params) {
            HttpURLConnection urlConnection = null;
            try {
                String urlString = (String) params[0];
                URL urlURL = new URL(urlString);
                Log.d("URL :", urlURL.toString());
                urlConnection = (HttpURLConnection) urlURL.openConnection();
                InputStream is = urlConnection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                responseData = response.toString();
                return responseData;
            } catch (IOException e) {
                return e.toString();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (responseData != null) {
                startActivity(new Intent(LogInActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Sorry, could not connect", Toast.LENGTH_SHORT).show();
            }
        }
    }
}