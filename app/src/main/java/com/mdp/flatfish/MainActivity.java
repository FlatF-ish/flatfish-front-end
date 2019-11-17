package com.mdp.flatfish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }

    public void onCreateFlat(View v) {
        Log.d("Testing", "Creating a new flat");
        Intent intent = new Intent(this, CreateFlatActivity.class);
        startActivity(intent);
    }

    public void onJoinFlat(View v) {
        Log.d("Testing", "Joining an existing flat");
        Intent intent = new Intent(this, JoinFlatActivity.class);
        startActivity(intent);
    }
}
