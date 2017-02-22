package com.example.constrainlayoutsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CustomProgreesBar bar= (CustomProgreesBar) findViewById(R.id.custom_bar);
        //bar.start();
        bar.setPercent(75);
    }
}
