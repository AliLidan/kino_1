package com.example.kino;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class films extends AppCompatActivity {

    TextView tvInfo;
    //TextView ValumeI;
    EditText tvName;
    films.MyTask mt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_films);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        tvName = (EditText) findViewById(R.id.editTextTextPersonName);
    }

    public void onClick(View v) {
        mt = new films.MyTask();
        mt.execute(tvName.getText().toString());
    }

    class MyTask extends AsyncTask<String, Void, ArrayList<String[]>> { //создание классадля для не слишком
        // продолжительных операций - загрузка небольших изображений, файловые операции, операции с базой данных
        @Override
        protected void onPreExecute() { //первый – строка поиска
            super.onPreExecute();       //use для вызова конструкторов супер класса и обращения к элементам супер класса (дочерний)
            tvInfo.setText("Begin");    // просто обозначае что процесс начался
        }
        @Override
        protected ArrayList<String[]> doInBackground(String... params) { // основные операции
            ArrayList<String[]> res=new ArrayList <>(); // в этот массив будем записывать полученные из запроса данные


            HttpURLConnection myConnection = null;
            /*Реализует работу по отправке и получении данных из сети по протоколу HTTP.
            Данные могут быть любого типа и длины.
            Класс используют для отправки и получения потоковых данных, размеры которых нельзя заранее определить.*/
            String line=""; // для записи строки(запроса?)
            String total=""; // цельная/главная ссылка?
            try {
                URL mySite = new URL("http://192.168.43.171:8080/kino/films/index.php?name="+params[0]);
                // 192.168.43.171
                myConnection = (HttpURLConnection) mySite.openConnection(); //использует метод GET
            } catch (MalformedURLException e) {
                e.printStackTrace(); //Исключение генерируется, когда программа пытается создать URL-адрес на основе неправильной спецификации
            } catch (IOException e) { // т.е ссылка содержит запрещенные символы
                e.printStackTrace(); //нет доступа к хосту /соединение было принудительно закрыто удаленным хостом
            }

            int i=0; // показатель соединения
            // Не_Рабочий_Код: Toast.makeText(theatres.this, "i=0!",Toast.LENGTH_LONG).show();
            try {
                i = myConnection.getResponseCode(); //Получает самый последний полученный код ответа для этой транзакции
                String si =  String.valueOf(i);
                // Не_Рабочий_Код: Toast.makeText(theatres.this, "2) i = "+ si,Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (i==200) {
                //Toast.makeText(theatres.this, "3) This is my Toast message!",Toast.LENGTH_LONG).show();
                InputStream responseBody=null;

                try {
                    responseBody = myConnection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                InputStreamReader responseBodyReader =null;
                try {
                    responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                BufferedReader r = new BufferedReader(new InputStreamReader(responseBody));
                while (true) {
                    try {
                        if (!((line = r.readLine()) != null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    total=total+line;
                }
                JSONArray JA=null;
                try {
                    JA=new JSONArray(total);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j=0;j<JA.length();j++) {
                    JSONObject JO=null;
                    try {
                        JO=JA.getJSONObject(j);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String[] st= new String[2];
                    try {
                        st[0] = JO.getString("name").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    res.add(st);
                }
            }
            myConnection.disconnect();

            return res;
        }
        @Override
        protected void onPostExecute(ArrayList<String[]> result) { //третий – возвращаемое значение
            super.onPostExecute(result);
            films.ClAdapter clAdapter=new films.ClAdapter(tvInfo.getContext(),result);
            ListView lvMain = (ListView) findViewById(R.id.lvMain);
            lvMain.setAdapter(clAdapter);
            tvInfo.setText("End");

        }

    }


    class ClAdapter extends BaseAdapter { //место где будут видны результаты поиска
        Context ctx;
        LayoutInflater lInflater;
        List<String[]> lines;
        ClAdapter(Context context, List<String[]> elines){
            ctx = context;
            lines = elines;
            lInflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return lines.size();
        }
        @Override
        public Object getItem(int position) {
            return lines.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = lInflater.inflate(R.layout.item, parent, false);
            };
            String[] p =(String[]) getItem(position);
            ((TextView) view.findViewById(R.id.tvText)).setText(p[0]);
            return view;
        }

    }
}