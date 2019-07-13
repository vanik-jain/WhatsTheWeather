package com.vapps.json;


import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.json.JSONArray;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
LinearLayout linearLayout;
TextView loadingMsg;

    public void checkWeather(View view)
    {

        String cityName="";
        InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);


        try
        {
             cityName = URLEncoder.encode(editText.getText().toString(),"UTF-8");

            DataDownload dataDownload = new DataDownload();


            dataDownload.execute("https://openweathermap.org/data/2.5/weather?q="+cityName+"&appid=b6907d289e10d714a6e88b30761fae22" );



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        Log.i("city",cityName);


    }


    public class DataDownload extends AsyncTask<String,Void,String>
    {

        String result;
        String result1;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            //loadingMsg.setText("Fetching Models");
            linearLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls)
        {

            try {

                URL url = new URL(urls[0]);

                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();




                InputStream inputStream = httpURLConnection.getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data != -1 )
                {
                    char c = (char)data;

                    result += c;

                    data = inputStreamReader.read();

                }
                Log.i("result",result);

                result1 = result.replaceFirst("null","");

                Log.i("result1",result1);

                return result;



            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            linearLayout.setVisibility(View.INVISIBLE);
           // String message = "";
            String main = "";
           String description= "";
            try
            {



                Log.i("result1",result1);


                JSONObject jsonObject = new JSONObject(result1);


                String weather  = jsonObject.getString("weather");


                Log.i("weather",weather);

                JSONArray jsonArray = new JSONArray(weather);

                Log.i("result2" ,jsonArray.toString());

                for(int i = 0 ; i<jsonArray.length();++i)
                {

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    main =    jsonObject1.getString("main")+" , "+jsonObject1.getString("description");


                }

                JSONObject jsonObject2 = jsonObject.getJSONObject("main");

               description= jsonObject2.getString("temp");


                textView.setText("Weather:\n"+main+"\nTemperature:\n"+description+"°C");









            }

            catch (Exception e)
            {
                e.printStackTrace();
            }


        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText =  findViewById(R.id.editText1);

        textView = findViewById(R.id.textView);


        linearLayout=findViewById(R.id.progressbar_layout);
        loadingMsg = findViewById(R.id.loading_msg);
    }
}
