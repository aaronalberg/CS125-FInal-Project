package com.example.cs125finalproject;

import android.os.AsyncTask;
import android.util.Log;
import android.view.textclassifier.TextLinks;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Arrays;
import com.loopj.android.http.*;


import cz.msebera.android.httpclient.Header;


public class WebAPI {

    private static JSONObject response;


    private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
        sb.append((char) cp);
    }
    return sb.toString();
}

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static void MyGETRequest(String url) throws IOException {

        URL urlForGetRequest = new URL(url);
        String readLine = null;
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();

        conection.setRequestMethod("GET");
        conection.setRequestProperty("userId", "a1bcdef"); // set userId its a sample here

        int responseCode = conection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((readLine = in .readLine()) != null) {
                response.append(readLine);
            } in .close();
            // print result
            System.out.println("JSON String Result " + response.toString());
            //GetAndPost.POSTRequest(response.toString());
        } else {
            System.out.println("GET NOT WORKED");
        }
    }

    public static JSONObject uploadImageToAPI(String url, String filepath) throws FileNotFoundException {
        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("file", new File(filepath));

        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("msg", responseString);
                try {
                    response = new JSONObject(responseString);
                    Log.i("bruh", "broooooo what");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("hereere");

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    response = new JSONObject(responseString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("blahahahah");
            }

            });
        return response;
    }

}
