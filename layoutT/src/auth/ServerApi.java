package auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class ServerApi extends AsyncTask<String, Void, String>{
	private final static String
			HOST = "http://192.168.173.1:3000",
			AUTH_URL = HOST + "/oauth",
			IDS_URL = HOST + "/notes/all",
			SGN_URL = HOST + "/signup",
			ADD_NOTE_URL = HOST + "/notes/add",
			GET_NOTE_URL = HOST + "/notes",
			DELETE_NOTE_URL = HOST + "/notes/del";
	public static String token;
	
	public static String authenticate(String username, String password){
	InputStream is = null;
	// Making HTTP request
    try {
        // defaultHttpClient
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(AUTH_URL);
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));	        
        HttpResponse httpResponse = httpClient.execute(httpPost);
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
        token = sb.toString();
        Log.d("API::Auth",token);
        token = new JSONObject(token).getString("token");
        return token;
    } catch (Exception e) {
        Log.e("Buffer Error", "Error converting result " + e.toString());
        return null;
    }

	}
	public static boolean signup(String username, String email, String password){
		InputStream is = null;
		// Making HTTP request
	    try {
	        // defaultHttpClient
	        DefaultHttpClient httpClient = new DefaultHttpClient();
	        HttpPost httpPost = new HttpPost(SGN_URL);
	        List<NameValuePair> params = new ArrayList<NameValuePair>(3);
	        params.add(new BasicNameValuePair("username", username));
	        params.add(new BasicNameValuePair("email", email));
	        params.add(new BasicNameValuePair("password", password));
	        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));	        
	        HttpResponse httpResponse = httpClient.execute(httpPost);
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
			Log.d("API::Signup",sb.toString());
	        return (sb.toString() == "ok");
	    } catch (Exception e) {
	        Log.e("Buffer Error", "Error converting result " + e.toString());
	        return false;
	    }
	}
	public static JSONArray getIds(){
		try {
			JSONObject response = Json.getJSONFromUrl(IDS_URL + "/" + token);
			Log.d("API::GetIDs", response.toString());
			return response.getJSONArray("notes");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}		
	}
	public static boolean sendNote(String id, String title,
									String body, String image,
									Integer modeid, Integer placeid){
		InputStream is = null;
		// Making HTTP request
	    try {
	        // defaultHttpClient
	        DefaultHttpClient httpClient = new DefaultHttpClient();
	        HttpPost httpPost = new HttpPost(ADD_NOTE_URL);
	        List<NameValuePair> params = new ArrayList<NameValuePair>(5);
	        params.add(new BasicNameValuePair("id", id));
	        params.add(new BasicNameValuePair("title", title));
	        params.add(new BasicNameValuePair("body", body));
	        params.add(new BasicNameValuePair("modeid", modeid.toString()));
	        params.add(new BasicNameValuePair("placeid", placeid.toString()));	        
	        params.add(new BasicNameValuePair("image", image));
	        params.add(new BasicNameValuePair("token", token));
	        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));	        
	        HttpResponse httpResponse = httpClient.execute(httpPost);
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
	        return (sb.toString() == "ok");
	    } catch (Exception e) {
	        Log.e("Buffer Error", "Error converting result " + e.toString());
	        return false;
	    }
	}
	public static JSONObject getNote(String id){
		return Json.getJSONFromUrl(GET_NOTE_URL + "/" + id + "/" + token);
	}
	@Override
	public String doInBackground(String... params) {
		if (params[0].equals("send")) {
			sendNote(params[1], params[2], params[3], params[4], Integer.parseInt(params[5]), Integer.parseInt(params[6]));
		} else if (params[0].equals("getnote")) {
			getNote(params[1]);
		} else if (params[0].equals("signup")){
			signup(params[1], params[2], params[3]);
		}
		return null;
	}
}