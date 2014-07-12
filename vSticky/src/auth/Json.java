package auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Json {

	static InputStream is = null;
	static JSONArray jArr = null;
	static JSONObject jObj = null;
	static String json = "";

//	public static JSONArray getJSONFromUrlPost(String url) {
//
//	    // Making HTTP request
//	    try {
//	        // defaultHttpClient
//	        DefaultHttpClient httpClient = new DefaultHttpClient();
//	        HttpPost httpPost = new HttpPost(url);
//	        HttpResponse httpResponse = httpClient.execute(httpPost);
//	        HttpEntity httpEntity = httpResponse.getEntity();
//	        is = httpEntity.getContent();
//
//	    } catch (UnsupportedEncodingException e) {
//	        e.printStackTrace();
//	    } catch (ClientProtocolException e) {
//	        e.printStackTrace();
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    }
//
//	    try {
//	        BufferedReader reader = new BufferedReader(new InputStreamReader(
//	                is, "utf-8"), 8);
//	        StringBuilder sb = new StringBuilder();
//	        String line = null;
//	        while ((line = reader.readLine()) != null) {
//	            sb.append(line + "\n");
//	        }
//	        is.close();
//	        json = sb.toString();
//	    } catch (Exception e) {
//	        Log.e("Buffer Error", "Error converting result " + e.toString());
//	    }
//
//	    // try parse the string to a JSON object
//	    try {
//	        jObj = new JSONArray(json);
//	    } catch (JSONException e) {
//	        Log.e("JSON Parser", "Error parsing data " + e.toString());
//	    }
//
//	    // return JSON String
//	    return jObj;
//
//	}
	public static JSONArray getJSONArrayFromUrlGet(String url) {

	    // Making HTTP request
	    try {
	        // defaultHttpClient
	        DefaultHttpClient httpClient = new DefaultHttpClient();
	        HttpGet httpGet = new HttpGet(url);
	        HttpResponse httpResponse = httpClient.execute(httpGet);
	        HttpEntity httpEntity = httpResponse.getEntity();
	        is = httpEntity.getContent();

	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    try {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(
	                is, "utf-8"), 8);
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	        is.close();
	        json = sb.toString();
	    } catch (Exception e) {
	        Log.e("Buffer Error", "Error converting result " + e.toString());
	    }

	    // try parse the string to a JSON object
	    try {
	        jArr = new JSONArray(json);
	    } catch (JSONException e) {
	        Log.e("JSON Parser", "Error parsing data " + e.toString());
	    }

	    // return JSON String
	    return jArr;

	}
	public static JSONObject getJSONObjectFromUrlGet(String url) {

	    // Making HTTP request
	    try {
	        // defaultHttpClient
	        DefaultHttpClient httpClient = new DefaultHttpClient();
	        HttpGet httpGet = new HttpGet(url);
	        HttpResponse httpResponse = httpClient.execute(httpGet);
	        HttpEntity httpEntity = httpResponse.getEntity();
	        is = httpEntity.getContent();

	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    try {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(
	                is, "utf-8"), 8);
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	        is.close();
	        json = sb.toString();
	    } catch (Exception e) {
	        Log.e("Buffer Error", "Error converting result " + e.toString());
	    }

	    // try parse the string to a JSON object
	    try {
	        jObj = new JSONObject(json);
	    } catch (JSONException e) {
	        Log.e("JSON Parser", "Error parsing data " + e.toString());
	    }

	    // return JSON String
	    return jObj;

	}
}
