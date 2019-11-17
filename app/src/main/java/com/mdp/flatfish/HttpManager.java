package com.mdp.flatfish;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class HttpManager {
    public static void postData(final String rawUrl, final HashMap<String, String> data, final PostCallback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(rawUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setChunkedStreamingMode(0);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setReadTimeout(10000);
                    urlConnection.setConnectTimeout(15000);
                    urlConnection.connect();

                    OutputStream output = urlConnection.getOutputStream();
                    OutputStreamWriter outputWriter = new OutputStreamWriter(output, "UTF-8");

                    JSONObject jsonData = new JSONObject(data);
                    outputWriter.write(jsonData.toString());
                    outputWriter.flush();
                    outputWriter.close();

                    int responseCode = urlConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream input = urlConnection.getInputStream();
                        BufferedReader inputReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                        Log.d("Testing", "Success");

                        StringBuilder sb = new StringBuilder();
                        String line = "";
                        while ((line = inputReader.readLine()) != null) {
                            sb.append(line);
                        }
                        String response = sb.toString();
                        callback.run(response);
                    } else {
                        Log.d("Testing", "Failure");
                        callback.error(responseCode);
                    }
                } catch (Exception e) {
                    Log.d("Testing", e.toString());
                }
            }
        }).start();
    }
}
