package com.mdp.flatfish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;

public class CreateFlatActivity extends AppCompatActivity {

    Handler handler = null;
    HttpManager httpManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_flat);
        getSupportActionBar().hide();
    }

    public void onCreateFlat(View v) {
        final ProgressBar spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        HashMap<String, String> data = new HashMap<>();
        TextView textPassword = (TextView) findViewById(R.id.text_password);
        Log.d("Testing", textPassword.getText().toString());
        data.put("password", textPassword.getText().toString());

        handler = new Handler();

        final Context self = this;
        PostCallback callback = new PostCallback() {
            @Override
            public void run(final String response) {
                Log.d("Testing", response.toString());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView textKey1 = (TextView) findViewById(R.id.text_key1);
                        TextView textKey2 = (TextView) findViewById(R.id.text_key2);
                        Button buttonContinue = (Button) findViewById(R.id.button_continue);

                        textKey1.setVisibility(View.VISIBLE);
                        textKey2.setVisibility(View.VISIBLE);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            textKey2.setText(jsonResponse.get("houseid").toString());
                        } catch (Exception e) {
                            Log.d("Testing", e.toString());
                        }
                        buttonContinue.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.INVISIBLE);

                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            SharedPreferences preferences = getApplicationContext().getSharedPreferences("Preferences", 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("userid", jsonResponse.get("userid").toString());
                            editor.putString("houseid", jsonResponse.get("houseid").toString());
                            editor.commit();

                        } catch (Exception e) {
                            Log.d("Testing", e.toString());
                        }
                    }
                });
            }

            public void error(int errorCode) {
                Log.d("Testing", String.valueOf(errorCode));
            }
        };

        HttpManager.postData("https://tourmaline-fall.glitch.me/create-house", data, callback);

        // Hides the keyboard.
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        Button buttonCreatePassword = (Button) findViewById(R.id.button_create_password);
        buttonCreatePassword.setEnabled(false);
        textPassword.setEnabled(false);
    }

    public void onContinue(View v) {
        Intent intent = new Intent(this, SetNameActivity.class);
        startActivity(intent);
    }
}
