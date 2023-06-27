package com.example.kino;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class new_theatres extends AppCompatActivity {
    TextView tvInfo;
    EditText tvName;
    EditText tvAddress;
    new_theatres.MyTask mt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_theatres);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        tvName = (EditText) findViewById(R.id.editTextTextPersonName);
        tvAddress = (EditText) findViewById(R.id.editTextTextPersonAddress);

    }
    public void onClick(View v) {
        mt = new new_theatres.MyTask();
        mt.execute(tvName.getText().toString(),tvAddress.getText().toString());
    }



    class MyTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo.setText("Begin");
        }

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection myConnection = null;
            BufferedOutputStream os = null;
//          InputStream is = null;
            String line = null;
            String total = "";
            JSONObject obj = null;
            try {
                URL githubEndpoint = new URL("http://192.168.43.171:8080/kino/POST/newtheatre/index.php ");
                myConnection =
                        (HttpURLConnection) githubEndpoint.openConnection();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                myConnection.setRequestMethod("POST");
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            }
            myConnection.setDoInput(true);
            myConnection.setDoOutput(true);
            myConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            myConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", params[0]);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
                jsonObject.put("address", params[1]);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            String message = jsonObject.toString();
            try {
                os = new BufferedOutputStream(myConnection.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                os.write(message.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//clean up
            try {
                os.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            int i = 0;
            try {
                i = myConnection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
//                tvInfo.setText(str);
            if (i == 200) {
                InputStream responseBody = null;
                try {
                    responseBody = myConnection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader r = new BufferedReader(new InputStreamReader(responseBody));
                while (true) {
                    try {
                        if (!((line = r.readLine()) != null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    total = total + line;
                }
                try {
                    obj = new JSONObject(total);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void params) {
            super.onPostExecute(null);

            tvInfo.setText("End");
            Toast toast = Toast.makeText(getApplicationContext(), "Запись добавлена!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

}