package com.example.cs125finalproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
        clicky();
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                TextView loadingMessage = findViewById(R.id.loadingMessage);
                loadingMessage.setVisibility(View.GONE);
                makeChunks(resultString);
            }
        }

};
    public void clicky() {
        TextView loadingMessage = findViewById(R.id.loadingMessage);
        loadingMessage.setText("Loading...");
        backgroundThread();
    }



    public void backgroundThread() {
        resultString = "";
        new Thread(new Runnable() {
            @Override
            public void run() {
                String temp = "";
//
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
            String result = WebAPI.uploadImageToAPI("https://stark-beach-10531.herokuapp.com/upload/?numberToShow=4",
                    current.getStringExtra("currentPhotoPath")).get("Recipes").toString();
            Log.i("Finished", "Finished the thingy.");



            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }
    public void makeChunks(String response) {
        LinearLayout recipeLayout = findViewById(R.id.recipeLayout);
        JsonArray entries = (JsonArray) new JsonParser().parse(response);
        System.out.println("LENGTH IS:  " + entries.size());
        for (int i = 0; i < entries.size(); i++) {
            JsonObject recipe = entries.get(i).getAsJsonObject();
            View layout = getLayoutInflater().inflate(R.layout.chunk_recipe, null);
            TextView recipeName = layout.findViewById(R.id.recipeName);
            recipeName.setText(recipe.get("title").toString().replaceAll("\\\\n","").replaceAll("\"","").replaceAll("&amp",""));
            recipeLayout.addView(layout);
            Button recipeLink = layout.findViewById(R.id.recipeLink);
            recipeLink.setOnClickListener(unused -> goToUrl(recipe.get("href").toString()));

        }

    }

    public void goToUrl(String url) {
        Uri webpage = Uri.parse(Uri.decode(url.replaceAll("\"","")));
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
