package com.example.cs125finalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Recipes extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes);
        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(unused -> startActivity(new Intent(this, MainActivity.class)));
        Button generateButton = findViewById(R.id.generateButton);
        generateButton.setOnClickListener(unused -> generateRecipes());
    }

    public void generateRecipes() {
        Intent current = getIntent();
        //WebAPI.uploadImageToAPI("https://stark-beach-10531.herokuapp.com/upload/",
           //     current.getStringExtra("currentPhotoPath"));
        ImageView tester = findViewById(R.id.tester);
        Bitmap myBitmap = BitmapFactory.decodeFile(current.getStringExtra("currentPhotoPath"));
        tester.setImageBitmap(myBitmap);
    }
}
