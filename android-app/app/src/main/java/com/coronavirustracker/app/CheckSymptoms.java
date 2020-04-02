package com.coronavirustracker.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.amplifyframework.api.graphql.MutationType;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.DetectionSource;
import com.amplifyframework.datastore.generated.model.Infection;

import java.util.UUID;

public class CheckSymptoms extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_symptoms);

        // Enable back to main button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Symptom checker button
     */
    public void submitSymptoms(View view) {
        // Get the checkbox values
        CheckBox highTemperatureCheckbox = findViewById(R.id.highTemperature);
        Boolean highTemperature = highTemperatureCheckbox.isChecked();
        CheckBox continuousCoughCheckbox = findViewById(R.id.continuousCough);
        Boolean continuousCough = continuousCoughCheckbox.isChecked();

        // Intent for going back to the home screen
        final Intent goToAppHomeScreen = new Intent(this, MainActivity.class);

        // Handle negative symptoms
        if (!highTemperature && !continuousCough) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.check_symptoms_negative_title)
                    .setMessage(R.string.check_symptoms_negative_body)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(goToAppHomeScreen);
                        }
                    })
                    .show();
        }

        // Handle positive symptoms
        else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.check_symptoms_positive_title)
                    .setMessage(R.string.check_symptoms_positive_body)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Post to API
                            Long contactTimestamp = System.currentTimeMillis();
                            String dummyUUID = UUID.randomUUID().toString();
                            Infection infection = Infection.builder().infectedTimestamp(contactTimestamp).detectionSource(DetectionSource.symptoms).id(dummyUUID).build();
                            Amplify.API.mutate(infection, MutationType.CREATE,
                                    taskCreationResponse -> {
                                        //Log.i("InfectionReported", "Logged: " + taskCreationResponse.getData());
                                        //Log.i("InfectionReported", "Errors: " + taskCreationResponse.getErrors());
                                    },
                                    apiFailure -> {} //Log.e("InfectionReported", "Failed to create an infection: ", apiFailure)
                            );

                            // Update symptoms settings
                           SharedPreferences sharedPreferences = getApplicationContext()
                                   .getSharedPreferences("preferences", MODE_PRIVATE);
                            Long currentTimestamp = System.currentTimeMillis();
                            sharedPreferences
                                    .edit()
                                    .putString("symptomsReportedDate", currentTimestamp.toString())
                                    .apply();

                            // Go to home screen
                            startActivity(goToAppHomeScreen);
                        }
                    })
                    .setNegativeButton(android.R.string.no,null )
                    .show();
        }

    }
}

