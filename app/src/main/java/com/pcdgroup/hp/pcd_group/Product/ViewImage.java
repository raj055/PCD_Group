package com.pcdgroup.hp.pcd_group.Product;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.Client.ClientDetailsActivity;
import com.pcdgroup.hp.pcd_group.Client.ClientRegisterActivity;
import com.pcdgroup.hp.pcd_group.MainActivity;
import com.pcdgroup.hp.pcd_group.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class ViewImage extends AppCompatActivity {


    ListView listView;
    CustomListAdapter adapter;
    String HttpURL = "http://pcddata-001-site1.1tempurl.com/fimage.php";
    InputStream is = null;
    String line = null;
    String result = null;
    String[] data;
    ArrayList<String> picNames;
    String recordName,EmailHolders,user;
    List<Entity> localEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image_layout);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewImage.this,UploadImage.class);
                startActivity(intent);
            }
        });


        Intent intent = getIntent();
        EmailHolders = intent.getStringExtra("email");


        if (EmailHolders == user){
            fab.setVisibility(View.INVISIBLE);
        }

        localEntity = new ArrayList<Entity>();
        recordName = new String("");
        picNames = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.lstv);

        adapter = new CustomListAdapter(this, localEntity);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //Allow network in main thread
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        //Retrieve
        getData();

        //Adepter
        adapter.notifyDataSetChanged();

    }

    private void getData(){

        try {
            URL url = new URL(HttpURL);
            HttpURLConnection con= (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            is = new BufferedInputStream(con.getInputStream());

        }catch (Exception e){
            e.printStackTrace();
        }

        //Read in content into String
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null)
            {
                sb.append(line+"\n");
            }

            is.close();
            result = sb.toString();

        }catch (Exception e){
            e.printStackTrace();
        }

        //Parse json data
        try {

            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;

            data = new String[ja.length()];

            for (int i=0; i<ja.length();i++){

                jo=ja.getJSONObject(i);
                String picname = jo.getString("name");
                String urlname = jo.getString("photo");
                Integer price = jo.getInt("price");
                Integer minimum = jo.getInt("minimum");
                Integer hsncode=jo.getInt("hsncode");
                Integer gst = jo.getInt("gst");
                String brand=jo.getString("brand");
                String description=jo.getString("description");
                Integer stock=jo.getInt("stock");
                Integer reorderlevel=jo.getInt("reorderlevel");
                Integer id = jo.getInt("id");
                picNames.add(picname);
                Entity e = new Entity(picname,urlname,price,gst, minimum,hsncode,brand,description,stock,reorderlevel,id);
                localEntity.add(e);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
