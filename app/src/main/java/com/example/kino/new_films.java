package com.example.kino;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class new_films extends AppCompatActivity {
    TextView tvInfo;
    EditText tvName;
    new_films.MyTask mt;


    String line;
    String total="";
    new_films.MyTaskN mtn;
    ListView lvMain;

    new_films.MyTaskN1 mtn1;
    new_films.MyTaskTF mttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_films);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        tvName = (EditText) findViewById(R.id.editTextTextPersonName);
        lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

       /* mt = new new_films.MyTask();
        mt.execute();
        mtn = new new_films.MyTaskN();
        mtn.execute();*/

    }
    public void onClick(View v) {
        if(v.getId() == R.id.btn_Scecond) {
            mt = new new_films.MyTask();
            mt.execute(tvName.getText().toString());
            Toast toast = Toast.makeText(getApplicationContext(), "btn_Scecond 2-ая!", Toast.LENGTH_SHORT);
            toast.show();
        }
        if(v.getId() == R.id.btn_First)
        {
            mtn = new new_films.MyTaskN();
            mtn.execute();

            Toast toast = Toast.makeText(getApplicationContext(), "btn_First 1-ая!", Toast.LENGTH_SHORT);
            toast.show();
        }
        if(v.getId() == R.id.btn_Third)
        {
            mtn1 = new new_films.MyTaskN1();
            mtn1.execute(tvName.getText().toString());;


            Toast toast = Toast.makeText(getApplicationContext(), "btn_Third 3-я!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    //************************************************************************
                    //ЗАПРОС №1  ЗАПИСЬ НОВОГО ФИЛЬМА
    //***********************************************************************
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
                URL githubEndpoint = new URL("http://192.168.43.171:8080/kino/POST/newfilm/index.php");
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
                //    tvInfo.setText(str);
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

    //************************************************************************
    //ЗАПРОС №2  ВЫВОД ВСЕХ КИНОТЕАТРОВ
    //***********************************************************************
    class MyTaskN extends AsyncTask<Void, Void, ArrayList<String[]>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo.setText("Begin");
        }
        @Override
        protected ArrayList<String[]> doInBackground(Void... params) {
            ArrayList<String[]> res = new ArrayList<>();
            HttpURLConnection myConnection = null;
            try {
                URL githubEndpoint = new URL("http://192.168.43.171:8080/kino/alltheatres/index.php?name=");
                myConnection =
                        (HttpURLConnection) githubEndpoint.openConnection();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                tvInfo.setText("1");

            } catch (IOException e) {
                e.printStackTrace();
                tvInfo.setText("2");

            }

            int i=0;
            try {
                i = myConnection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (i==200) {
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
                        st[1] = JO.getString("id").toString();
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
        protected void onPostExecute(ArrayList<String[]> result) {
            super.onPostExecute(result);
            new_films.ClAdapter clAdapter=new new_films.ClAdapter(tvInfo.getContext(),result);
//            lvMain = (ListView) findViewById(R.id.lvMain);
            lvMain.setAdapter(clAdapter);
            tvInfo.setText("End");
        }
    }
    //************************************************************************
    //АДАПТЕР  ПОДДЕРЖКА ВЫВОДА ЧЕРЕЗ LAYOUT
    //***********************************************************************
    class ClAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater lInflater;
        List<String[]>lines;
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
                view = lInflater.inflate(R.layout.item2, parent, false);
            };
            String[] p =(String[]) getItem(position);
            ((TextView) view.findViewById(R.id.tvText)).setText(p[0]);
            return view;
        };
        public boolean getCheck (int position){

            return true;
        }
    }
    //************************************************************************
    //ЗАПРОС №3  СОЗДАНИЕ СВЯЗЕЙ КИНО-АДРЕСС
    //***********************************************************************

    class MyTaskN1 extends AsyncTask<String, Void, String > {
        @Override
        protected String doInBackground(String... params) {
            String line = null;
            String total = null;
            BufferedOutputStream os = null;
            HttpURLConnection myConnection = null;
            JSONObject obj=null;
            try {
                URL githubEndpoint = new URL("http://192.168.43.171:8080/kino/POST/newfilm/index.php");
                myConnection =
                        (HttpURLConnection) githubEndpoint.openConnection();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            myConnection.setRequestProperty("Accept",
                    "application/vnd.github.v3+kino");
            myConnection.setRequestProperty("Contact-Me",
                    "alina.com");
            try {
                myConnection.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            myConnection.setDoOutput(true);
            myConnection.setDoOutput(true);
            myConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            myConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", params[0]);
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
                    total = line;
                }
                try {
                    obj = new JSONObject(total);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                try {
                    total=obj.getString("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return total;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            ID=result;
            int n = lvMain.getChildCount();
            int n1 = 0;
            int m = 0;
            JSONArray JA = new JSONArray();
            for (int i = 0; i < n; i++) {
                String[] st = (String[]) lvMain.getAdapter().getItem(i);
                LinearLayout ll = (LinearLayout) lvMain.getChildAt(i);
                CheckBox ch = (CheckBox) ll.getChildAt(0);
               n1 = i;
                if (ch.isChecked()) {
                    /*mttn.add(new new_films.MyTaskTF());
                    mttn.get(m).execute(st[1],result);*/
                    try {
                        JA.put(m,new JSONObject());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        JA.getJSONObject(m).put("ID_films",n+1);
                        JA.getJSONObject(m).put("ID_theatres",n1+1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    m++;
                }
            }
            mttn = new new_films.MyTaskTF();
            mttn.execute(JA);
            Toast toast = Toast.makeText(getApplicationContext(), "дошли до mttn.execute(JA)!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    class MyTaskTF extends AsyncTask<JSONArray, Void, String > {

        @Override
        protected String doInBackground(JSONArray... jsonArrays) {
            String line = null;
            String total = null;
            ArrayList<String[]> res=new ArrayList <>();
            BufferedOutputStream os = null;
            HttpURLConnection myConnection = null;
            JSONObject obj=null;
            try {
                URL githubEndpoint = new URL("http://192.168.43.171:8080/kino/POST/newtheatre_film/index.php");
                myConnection =
                        (HttpURLConnection) githubEndpoint.openConnection();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            myConnection.setRequestProperty("Accept",
                    "application/vnd.github.v3+kino");
            myConnection.setRequestProperty("Contact-Me",
                    "alina12485615@gmail.com");
            try {
                myConnection.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            myConnection.setDoOutput(true);
            myConnection.setDoOutput(true);
            myConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            myConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            String message = jsonArrays[0].toString();

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
                    total = line;
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
                        st[0] = JO.getString("result").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    res.add(st);
                }            }
            return total;        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tvInfo.setText("End111");
            return;
        }
    }

}
