package com.example.cs125finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Recipes extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes);
        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(unused -> startActivity(new Intent(this, MainActivity.class)));

    }
}
