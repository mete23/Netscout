package com.example.netscout;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Settings extends AppCompatActivity {
private EditText editText;
SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        editText = findViewById(R.id.editTextLink);
    }
    public void safeLink(View view){

        sharedPref =getSharedPreferences("A", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String newLink = editText.getText().toString();
        editor.putString("userland", newLink);
        editor.apply();


    }
}