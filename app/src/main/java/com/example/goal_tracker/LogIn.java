package com.example.goal_tracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.goal_tracker.RestAPI.ID;
import com.example.goal_tracker.RestAPI.RestApi;
import com.example.goal_tracker.RestAPI.User;
import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataService;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;
import com.samsung.android.sdk.healthdata.HealthResultHolder;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogIn extends AppCompatActivity {

    public static final String APP_TAG = "SimpleHealth";

    private static LogIn mInstance = null;
    private HealthDataStore mStore;
    private HealthConnectionErrorResult mConnError;
    private Set<HealthPermissionManager.PermissionKey> mKeySet;


    private static final String URL = "http://13.64.93.250:8080/";
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginButton = findViewById(R.id.loginButton);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
                switchToActivity(com.example.goal_tracker.ListView.class);
            }
        });


        mInstance = this;
        mKeySet = new HashSet<HealthPermissionManager.PermissionKey>();
        mKeySet.add(new HealthPermissionManager.PermissionKey(
                HealthConstants.StepCount.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
        mKeySet.add(new HealthPermissionManager.PermissionKey(
                HealthConstants.HeartRate.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
        HealthDataService healthDataService = new HealthDataService();
        try {
            healthDataService.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Create a HealthDataStore instance and set its listener
        mStore = new HealthDataStore(this, mConnectionListener);
        // Request the connection to the health data store
        mStore.connectService();

    }

    public void onDestroy() {
        mStore.disconnectService();
        super.onDestroy();
    }


    public void switchToActivity(Class nextClass) {
        Intent next = new Intent(this, nextClass);
        next.putExtra("userID", userID);
        startActivity(next);
    }

    protected void makeAccount() {
        String userName;
        EditText userNameInput = findViewById(R.id.userNameInput);
        userName = userNameInput.getText().toString();
        RestApi api = retroSetup();
        Call<ID> call = api.postUser(userName);
        try {
            Response<ID> retval = call.execute();
            userID = retval.body().getID();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected RestApi retroSetup() {
        Retrofit retrofit = getRetro();
        return retrofit.create(RestApi.class);
    }

    protected void login() {
        String userName;
        EditText userNameInput = findViewById(R.id.userNameInput);
        userName = userNameInput.getText().toString();
        RestApi api = retroSetup();

        Call<ID> call = api.getUserID(userName);
        try {
            Response<ID> retval = call.execute();
            if (retval.code() == 404) {
                makeAccount();
            } else {
                userID = retval.body().getID();
            }
        } catch (IOException e) {

        }

    }

    protected static Retrofit getRetro() {
        return new Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }


    private final HealthDataStore.ConnectionListener mConnectionListener = new HealthDataStore.ConnectionListener() {

        @Override
        public void onConnected() {
            Log.d(APP_TAG, "Health data service is connected.");
            HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);

            try {
                // Check whether the permissions that this application needs are acquired
                // Request the permission for reading step counts if it is not acquired

                // Get the current step count and display it if data permission is required
                // ...

                Map<HealthPermissionManager.PermissionKey, Boolean> resultMap = pmsManager.isPermissionAcquired(mKeySet);

                if (resultMap.containsValue(Boolean.FALSE)) {
                    // Request the permission for reading step counts if it is not acquired
                    pmsManager.requestPermissions(mKeySet, LogIn.this).setResultListener(mPermissionListener);
                } else {
                    // Get the current step count and display it
                    // ...
                }
            } catch (Exception e) {
                Log.e(APP_TAG, e.getClass().getName() + " - " + e.getMessage());
                Log.e(APP_TAG, "Permission setting fails.");
            }
        }

        @Override
        public void onConnectionFailed(HealthConnectionErrorResult error) {
            Log.d(APP_TAG, "Health data service is not available.");
            showConnectionFailureDialog(error);
        }

        @Override
        public void onDisconnected() {
            Log.d(APP_TAG, "Health data service is disconnected.");
        }
    };


    private void showConnectionFailureDialog(HealthConnectionErrorResult error) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        mConnError = error;
        String message = "Connection with Samsung Health is not available";

        if (mConnError.hasResolution()) {
            switch (error.getErrorCode()) {
                case HealthConnectionErrorResult.PLATFORM_NOT_INSTALLED:
                    message = "Please install Samsung Health";
                    break;
                case HealthConnectionErrorResult.OLD_VERSION_PLATFORM:
                    message = "Please upgrade Samsung Health";
                    break;
                case HealthConnectionErrorResult.PLATFORM_DISABLED:
                    message = "Please enable Samsung Health";
                    break;
                case HealthConnectionErrorResult.USER_AGREEMENT_NEEDED:
                    message = "Please agree with Samsung Health policy";
                    break;
                default:
                    message = "Please make Samsung Health available";
                    break;
            }
        }

        alert.setMessage(message);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (mConnError.hasResolution()) {
                    mConnError.resolve(mInstance);
                }
            }
        });

        if (error.hasResolution()) {
            alert.setNegativeButton("Cancel", null);
        }

        alert.show();
    }

    private final HealthResultHolder.ResultListener<HealthPermissionManager.PermissionResult> mPermissionListener =
            new HealthResultHolder.ResultListener<HealthPermissionManager.PermissionResult>() {

                @Override
                public void onResult(HealthPermissionManager.PermissionResult result) {
                    Log.d(APP_TAG, "Permission callback is received.");
                    Map<HealthPermissionManager.PermissionKey, Boolean> resultMap = result.getResultMap();
                    HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);

                    if (resultMap.containsValue(Boolean.FALSE)) {
                        pmsManager.requestPermissions(mKeySet, LogIn.this).setResultListener(mPermissionListener);
                        // Requesting permission fails
                    } else {
                        // Get the current step count and display it
                    }
                }
            };
}








