package com.mdp.flatfish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;

public class JoinFlatActivity extends AppCompatActivity {

    HttpManager httpManager;
    Handler handler;

    TextView textKey;
    TextView textPassword;
    TextView textInvalidKey;
    TextView textInvalidPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_flat);
        getSupportActionBar().hide();

        textKey = (TextView) findViewById(R.id.text_key);
        textPassword = (TextView) findViewById(R.id.text_password);
        textInvalidKey = (TextView) findViewById(R.id.text_invalid_key);
        textInvalidPassword = (TextView) findViewById(R.id.text_invalid_password);

        addListener(textKey, textInvalidKey);
        addListener(textPassword, textInvalidPassword);
    }

    private void addListener(final TextView textInput, final TextView textInvalid) {
        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInput.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorHighEmphasis));
                textInvalid.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void onJoinFlat(View v) {
        Log.d("Testing", "Joining a new flat");
        HashMap<String, String> data = new HashMap<>();

        final ProgressBar progressSpinner = (ProgressBar) findViewById(R.id.progress_spinner);
        progressSpinner.setVisibility(View.VISIBLE);


        data.put("houseid", textKey.getText().toString());
        data.put("password", textPassword.getText().toString());

        handler = new Handler();

        final Context self = this;
        PostCallback callback = new PostCallback() {
            @Override
            public void run(String response) {
                Log.d("Testing", response);

                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("Preferences", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("userid", jsonResponse.get("userid").toString());
                    editor.putString("houseid", jsonResponse.get("houseid").toString());
                    editor.commit();

                    Intent intent = new Intent(self, SetNameActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.d("Testing", e.toString());
                }
            }

            public void error(final int errorCode) {
                Log.d("Testing", String.valueOf(errorCode));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (errorCode == 401) {
                            // Key error.
                            TextView textInvalidKey = (TextView) findViewById(R.id.text_invalid_key);
                            textInvalidKey.setVisibility(View.VISIBLE);
                        } else if (errorCode == 402) {
                            // Password error.
                            TextView textInvalidPassword = (TextView) findViewById(R.id.text_invalid_password);
                            textInvalidPassword.setVisibility(View.VISIBLE);
                        }
                        progressSpinner.setVisibility(View.INVISIBLE);
                    }
                });
            }
        };

        HttpManager.postData("https://tourmaline-fall.glitch.me/join-house", data, callback);
    }
}
