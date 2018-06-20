package com.pcdgroup.hp.pcd_group.DatabaseComponents;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.pcdgroup.hp.pcd_group.Client.ClientAdepter;
import com.pcdgroup.hp.pcd_group.Client.ClientOfClientList;
import com.pcdgroup.hp.pcd_group.Client.DataAdapter;
import com.pcdgroup.hp.pcd_group.Client.MyApplication;
import com.pcdgroup.hp.pcd_group.Http.HttpParse;
import com.pcdgroup.hp.pcd_group.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.HttpUrl;

/**
 * @author Grasp
 * @version 1.0 on 18-06-2018.
 */

public class DataBaseQuery {

  private HashMap<String, String>  hashMap;
  private String url;

  private String finalResult;
  private HttpParse httpParse;
  private Context currentContext;

  private CallBackInterface resultReceiver;
  private CallType queryType;
  JsonArrayRequest jsonArrayRequest;
  RequestQueue requestQueue ;
  DataGetUrl dataGetUrl;

  public DataBaseQuery(HashMap<String, String> hashMapRef,
                            DataGetUrl urlQry,
                            CallType typeOfQuery,
                            Context cntx,
                            CallBackInterface callBackExecutor){

  //Initialise all the class parameter.
    hashMap = new HashMap<String, String>();
    hashMap.putAll(hashMapRef);
    httpParse = new HttpParse();

    //Get the required url.
    url = urlQry.getUrl(urlQry);
    Log.v("Url Enum----", urlQry.toString());
    Log.v("Url-----",url);
    //Assign the currnt Context.
    currentContext = cntx;

    //Assign the call back executor.
    resultReceiver = callBackExecutor;

    queryType = typeOfQuery;

    dataGetUrl = urlQry;
  }

  public void PrepareForQuery(){
    switch (queryType){

      case POST_CALL:
        ExecuteDatabaseQuery();
        break;
      case JSON_CALL:
        JSON_WEB_CALL();
        break;
      case GET_DATA_CALL:
        break;
      default:
        break;
    }
  }

  // Execute the DataBase Query
  public void ExecuteDatabaseQuery() {

    class ExecuteDatabaseQuery extends AsyncTask<String, Void, String> {

      @Override
      protected void onPreExecute() {
        super.onPreExecute();
      }

      @Override
      protected void onPostExecute(String httpResponseMsg) {

        super.onPostExecute(httpResponseMsg);
        resultReceiver.ExecuteQueryResult(httpResponseMsg,dataGetUrl);
      }

      @Override
      protected String doInBackground(String... params) {

        finalResult = httpParse.postRequest(hashMap, url);
        return finalResult;
      }
    }

    ExecuteDatabaseQuery executeDatabaseQuery = new ExecuteDatabaseQuery();
    executeDatabaseQuery.execute();
  }

  public void JSON_WEB_CALL(){

    jsonArrayRequest  = new JsonArrayRequest(url,

      new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {

          if (response == null) {
            Toast.makeText(currentContext, "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
            return;
          }

          resultReceiver.ExecuteQueryResult(response.toString(),dataGetUrl);
        }
      },
      new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
          // error in getting json
          Toast.makeText(currentContext, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
      });

    MyApplication.getInstance().addToRequestQueue(jsonArrayRequest);

    requestQueue = Volley.newRequestQueue(currentContext);

    requestQueue.add(jsonArrayRequest);
  }
}
