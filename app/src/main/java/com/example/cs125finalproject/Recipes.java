package com.example.cs125finalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.FileNotFoundException;

public class Recipes extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes);
        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(unused -> startActivity(new Intent(this, MainActivity.class)));
        Button generateButton = findViewById(R.id.generateButton);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        AsyncHttpClient httpClient = new AsyncHttpClient();
                        try {
                            generateRecipes();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    public void generateRecipes() throws JSONException {
        Intent current = getIntent();
        try {
            String result = WebAPI.uploadImageToAPI("https://stark-beach-10531.herokuapp.com/upload/",
                    current.getStringExtra("currentPhotoPath")).get("Recipes").toString();
            TextView recipes = findViewById(R.id.recipes);
            recipes.setText(result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        /*
        ImageView tester = findViewById(R.id.tester);
        Bitmap myBitmap = BitmapFactory.decodeFile(current.getStringExtra("currentPhotoPath"));
        tester.setImageBitmap(myBitmap);
        */
    }
}
