package com.example.word27_jsontest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=(Button)findViewById(R.id.button);
        textView=(TextView)findViewById(R.id.textview);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okhttpclient();
            }
        });
    }
    private void okhttpclient(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path="http://192.168.3.102:9090/get_data.json";
                try {
                    OkHttpClient client=new OkHttpClient();
                    Request request=new Request.Builder()
                            .url(path)
                            .build();
                    Response response=client.newCall(request).execute();
                    String reader=response.body().string();
                    httpJson(reader);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void httpJson(String jsondata){
        try {
            JSONArray jsonArray=new JSONArray(jsondata);
            String[] text=new String[jsonArray.length()];
            String textv=null;
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String id=jsonObject.getString("id");
                String name=jsonObject.getString("name");
                String version=jsonObject.getString("version");
                if(textv!=null) {
                    textv = textv + "id:" + id + ",name:" + name + ",version.\n";
                }
                else{
                    textv = "id:" + id + ",name:" + name + ",version.\n";
                }
            }
            final String finalTextv = textv;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText(finalTextv.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
