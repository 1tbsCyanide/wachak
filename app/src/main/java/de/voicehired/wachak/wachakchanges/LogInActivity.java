package de.voicehired.wachak.wachakchanges;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.voicehired.wachak.R;
import de.voicehired.wachak.activity.MainActivity;

/**
 * Created by Vetero on 05-02-2016.
 */
public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton imageButton;
    ImageView imageView;
    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:" +
            "(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";
    private static final String TAG = LogInActivity.class.getSimpleName();
    public static final String PACKAGE = "de.voicehired.wachak";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        imageButton = (ImageButton) findViewById(R.id.bt_linked_in_log_in_activity);
        imageView = (ImageView) findViewById(R.id.iv_logo_sign_in_activity);

        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        imageView.setAnimation(animation1);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences(ConstantValues
                    .MY_PREFS_NAME, MODE_PRIVATE);
            boolean b = sharedPreferences.getBoolean(ConstantValues.LINKEDIN_SIGNED_KEY, false);
            if (b) {
                Toast.makeText(LogInActivity.this, "Welcome, " + sharedPreferences.getString
                                (ConstantValues.LINKEDIN_NAME, " to Wachak"),
                        Toast
                                .LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else {
                imageButton.setVisibility(View.VISIBLE);
                imageButton.setOnClickListener(this);
            }
        }, 2000);
    }

    @Override
    public void onClick(View v) {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                            String name = (response.get("emailAddress").toString());
                            String email = (response.get("formattedName").toString
                                    ());
                            SharedPreferences sharedPreferences = getSharedPreferences
                                    (ConstantValues.MY_PREFS_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(ConstantValues.LINKEDIN_SIGNED_KEY, true);
                            editor.putString(ConstantValues.LINKED_IN_ACCESS_TOKEN, LISessionManager.getInstance
                                    (getApplicationContext()).getSession().getAccessToken()
                                    .toString());
                            editor.putString(ConstantValues.LINKEDIN_NAME, name);
                            editor.putString(ConstantValues.LINKEDIN_EMAIL, email);
                            editor.apply();
                            startActivity(new Intent(LogInActivity.this, MainActivity.class));
                            finish();
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

    private Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }
}