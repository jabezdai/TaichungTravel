package com.example.taichungtravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private ListView lvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvShow = (ListView) findViewById(R.id.lvShow);

        String[] namelist = new String[]{};
        String[] placelist = new String[]{};
        Double[] xlist = new Double[]{};
        Double[] ylist = new Double[]{};

        AssetManager assetManager = this.getAssets();

        //讀取ＪＳＯＮ資料
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(assetManager.open("TaiTra.json"),"utf-8"));

            String line = br.readLine();
            StringBuffer json = new StringBuffer();
            while (line!=null){
                json.append(line);
                line = br.readLine();
            }

            JSONArray jsonArray = new JSONArray(String.valueOf(json));

            namelist = new String[jsonArray.length()];
            placelist = new String[jsonArray.length()];
            xlist = new Double[jsonArray.length()];
            ylist = new Double[jsonArray.length()];
            for(int i = 0; i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("名稱");
                Double x = jsonObject.getDouble("北緯");
                Double y = jsonObject.getDouble("東經");
                String place = jsonObject.getString("地址");

                namelist[i] = name;
                placelist[i] = place;
                xlist[i] = x;
                ylist[i] = y;
            }
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        //顯示到listview
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                namelist
        );
        lvShow.setAdapter(arrayAdapter);


        Double[] finalXlist = xlist;
        Double[] finalYlist = ylist;
        String[] finalNamelist = namelist;
        String[] finalPlacelist = placelist;
        //點擊listview任意欄位則會將資料傳進map並更換intent
        lvShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,MapsActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("name", finalNamelist[i]);
                bundle.putString("place", finalPlacelist[i]);
                bundle.putDouble("x", finalXlist[i]);
                bundle.putDouble("y", finalYlist[i]);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }


}