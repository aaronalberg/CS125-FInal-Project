package com.example.cs125finalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.FileNotFoundException;



public class Recipes extends Activity {

    public static String resultString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes);
        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(unused -> startActivity(new Intent(this, MainActivity.class)));
        Button generateButton = findViewById(R.id.generateButton);


        generateButton.setOnClickListener(unused -> clicky());


    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                TextView recipes = findViewById(R.id.recipes);
                recipes.setText(resultString);
            }
        }

};
    public void clicky() {
        backgroundThread();
    }



    public void backgroundThread() {
        resultString = "";
        new Thread(new Runnable() {
            @Override
            public void run() {
                String temp = "";
//                        AsyncHttpClient httpClient = new AsyncHttpClient();
                try {
                    temp = generateRecipes();
                    System.out.println("resoob is:  " + temp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                resultString = temp;
                Message msg = Message.obtain();
                msg.what = 1;
                handler.sendMessage(msg);

            }
        }).start();
    }

    public String generateRecipes() throws JSONException {
        Intent current = getIntent();
        try {
            String result = WebAPI.uploadImageToAPI("https://stark-beach-10531.herokuapp.com/upload/",
                    current.getStringExtra("currentPhotoPath")).get("Recipes").toString();
            Log.i("Finished", "Finished the thingy.");

            return result;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        /* LinearLayout recipeLayout = findViewById(R.id.recipeLayout);
        View recipe = getLayoutInflater().inflate(R.layout.chunk_recipe, recipeLayout, false);

         */

        /*
        ImageView tester = findViewById(R.id.tester);
        Bitmap myBitmap = BitmapFactory.decodeFile(current.getStringExtra("currentPhotoPath"));
        tester.setImageBitmap(myBitmap);
        */
    }
}
