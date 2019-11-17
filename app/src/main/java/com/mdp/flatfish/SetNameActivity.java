package com.mdp.flatfish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class SetNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);
        getSupportActionBar().hide();
    }

    public void onFinish(View view) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Preferences", 0);
        String userid = preferences.getString("userid", "defaultUserid");

        TextView textFirstname = (TextView) findViewById(R.id.text_firstname);

        HashMap<String, String> data = new HashMap<>();
        data.put("userid", userid);
        data.put("name", textFirstname.getText().toString());

        Log.d("Testing", userid);

        PostCallback callback = new PostCallback() {
            @Override
            public void run(String response) {
                Log.d("Testing", response.toString());
            }

            @Override
            public void error(int errorCode) {
                Log.d("Testing", String.valueOf(errorCode));
            }
        };

        HttpManager.postData("https://tourmaline-fall.glitch.me/set-user-data", data, callback);
    }
}
