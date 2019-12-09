package com.example.cs125finalproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONException;
import java.io.FileNotFoundException;


public class Recipes extends Activity {

    public class Wrapper {
        String title;
        String href;

    }

    public static String resultString;
    public static JsonArray resultArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes);
        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(unused -> startActivity(new Intent(this, MainActivity.class)));
        Button generateButton = findViewById(R.id.generateButton);


        generateButton.setOnClickListener(unused -> clicky());


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
            // Gson gson = new Gson();

            // Wrapper[] data = gson.fromJson(result, Wrapper[].class);


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
    public void makeChunks(String response) {
        LinearLayout recipeGroup = findViewById(R.id.recipeGroup);
        LinearLayout recipeLayout = findViewById(R.id.recipeLayout);
        JsonArray entries = (JsonArray) new JsonParser().parse(response);
        System.out.println("LENGTH IS:  " + entries.size());
        for (int i = 0; i < entries.size(); i++) {
            System.out.println("LOOOK AT MEMEMEMEM");
            JsonObject recipe = entries.get(i).getAsJsonObject();

            View layout = getLayoutInflater().inflate(R.layout.chunk_recipe, null);
            System.out.println("Before text view");
            TextView recipeName = layout.findViewById(R.id.recipeName);
            recipeName.setText(recipe.get("title").toString().replaceAll("\\\\n",""));
            recipeLayout.addView(layout);
            Button recipeLink = findViewById(R.id.recipeLink);

            recipeLink.setOnClickListener(unused -> goToUrl(recipe.get("href").toString()));

            //openWebPage(recipe.get("href").toString())
        }

    }

    public void goToUrl(String Url) {
        System.out.println("URL IS:   " + Url);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Url)));
    }


    public void openWebPage(String url) {
        WebView webView = new WebView(this);
        setContentView(webView);
        webView.loadUrl(url);
    }
}
