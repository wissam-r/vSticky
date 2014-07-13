package auth;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import note.Mode;
import note.Note;
import note.Photo;
import note.Place;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

public class ServerApi{
	private final static String
				HOST = "http://192.168.173.1:3000",
				SGN_URL = HOST + "/signup",
				AUTH_URL = HOST + "/oauth",
				NOTES_URL = HOST + "/notes",
				MODES_URL = HOST + "/modes",
				PLACES_URL = HOST + "/places",
				PHOTOS_URL = HOST + "/photos";
	public static String token, userId;
	
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
        JSONObject jObject = new JSONObject(token);
        token = jObject.getString("token");
        userId = jObject.getString("_id");
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
	public static ArrayList<Integer> getNotesIds(){
		JSONArray jArray = null;
		ArrayList<Integer> result = new ArrayList<Integer>();
		jArray = Json.getJSONArrayFromUrlGet(NOTES_URL + "/all/" + token);
		Log.d("API::GetNotesIDs", jArray.toString());
		if (jArray != null) {
			for (int i=0;i<jArray.length();i++) { 
				try {
					result.add(new JSONObject(jArray.get(i).toString()).getInt("_id"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	public static ArrayList<Integer> getModesIds(){
		JSONArray jArray = null;
		ArrayList<Integer> result = new ArrayList<Integer>();
		jArray = Json.getJSONArrayFromUrlGet(MODES_URL + "/all/" + token);
		Log.d("API::GetModesIDs", jArray.toString());
		if (jArray != null) {
			for (int i=0;i<jArray.length();i++) { 
				try {
					result.add(new JSONObject(jArray.get(i).toString()).getInt("_id"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	public static ArrayList<Integer> getPlacesIds(){
		JSONArray jArray = null;
		ArrayList<Integer> result = new ArrayList<Integer>();
		jArray = Json.getJSONArrayFromUrlGet(PLACES_URL + "/all/" + token);
		Log.d("API::GetPlacesIDs", jArray.toString());
		if (jArray != null) {
			for (int i=0;i<jArray.length();i++) { 
				try {
					result.add(new JSONObject(jArray.get(i).toString()).getInt("_id"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	public static ArrayList<Integer> getPhotosIds(){
		JSONArray jArray = null;
		ArrayList<Integer> result = new ArrayList<Integer>();
		jArray = Json.getJSONArrayFromUrlGet(PHOTOS_URL + "/all/" + token);
		Log.d("API::GetPhotosIDs", jArray.toString());
		if (jArray != null) {
			for (int i=0;i<jArray.length();i++) { 
				try {
					result.add(new JSONObject(jArray.get(i).toString()).getInt("_id"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static boolean sendNote(Integer id, String title, String body,
			String date, Integer modeid, Integer placeid, Integer photoid) {
		InputStream is = null;
		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(NOTES_URL);
			List<NameValuePair> params = new ArrayList<NameValuePair>(5);
			params.add(new BasicNameValuePair("id", id.toString()));
			params.add(new BasicNameValuePair("title", title));
			params.add(new BasicNameValuePair("body", body));
			params.add(new BasicNameValuePair("date", date));
			params.add(new BasicNameValuePair("modeId", modeid.toString()));
			params.add(new BasicNameValuePair("placeId", placeid.toString()));
			params.add(new BasicNameValuePair("photoId", photoid.toString()));
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
	public static boolean sendMode(Integer id, String name) {
		InputStream is = null;
		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(MODES_URL);
			List<NameValuePair> params = new ArrayList<NameValuePair>(5);
			params.add(new BasicNameValuePair("id", id.toString()));
			params.add(new BasicNameValuePair("name", name));
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
	public static boolean sendPlace(Integer id, String name,
			Double x, Double y, Double radius) {
		InputStream is = null;
		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(PLACES_URL);
			List<NameValuePair> params = new ArrayList<NameValuePair>(5);
			params.add(new BasicNameValuePair("id", id.toString()));
			params.add(new BasicNameValuePair("name", name.toString()));
			params.add(new BasicNameValuePair("x", x.toString()));
			params.add(new BasicNameValuePair("y", y.toString()));
			params.add(new BasicNameValuePair("radius", radius.toString()));
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
	public static boolean sendPhoto(Integer id, String path) {
		String photo = null;
		Bitmap bm = BitmapFactory.decodeFile(path);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
		byte[] byteArray = byteArrayOutputStream .toByteArray();
		photo = Base64.encodeToString(byteArray, Base64.DEFAULT);
		InputStream is = null;
		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(PHOTOS_URL);
			List<NameValuePair> params = new ArrayList<NameValuePair>(5);
			params.add(new BasicNameValuePair("id", id.toString()));
			params.add(new BasicNameValuePair("photo", photo));
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

	
	public static Note getNote(Integer id){
		JSONObject jObject = Json.getJSONObjectFromUrlGet(NOTES_URL + "/" + id.toString() + "/" + token);
		try {
			return new Note(jObject.getInt("_id"),jObject.getString("title"),jObject.getString("body"),jObject.getInt("photoId"),jObject.getInt("placeId"),jObject.getInt("modeId"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Mode getMode(Integer id){
		JSONObject jObject = Json.getJSONObjectFromUrlGet(MODES_URL + "/" + id.toString() + "/" + token);
		try {
			return new Mode(jObject.getInt("_id"), jObject.getString("name"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Place getPlace(Integer id){
		JSONObject jObject = Json.getJSONObjectFromUrlGet(PLACES_URL + "/" + id.toString() + "/" + token);
		try {
			return new Place(jObject.getInt("_id"),jObject.getString("name"),jObject.getDouble("x"),jObject.getDouble("y"),jObject.getInt("radius"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Photo getPhoto(Integer id){
		JSONObject jObject = Json.getJSONObjectFromUrlGet(PHOTOS_URL + "/" + id.toString() + "/" + token);
		try {
			byte[] decodedString = Base64.decode(jObject.getString("photo"), Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + "/synced_images");    
			myDir.mkdirs();
			String fname = "Image-"+ jObject.getInt("_id") +".jpg";
			File file = new File (myDir, fname);
			if (file.exists ()) file.delete (); 
			try {
			       FileOutputStream out = new FileOutputStream(file);
			       decodedByte.compress(Bitmap.CompressFormat.JPEG, 90, out);
			       out.flush();
			       out.close();

			} catch (Exception e) {
			       e.printStackTrace();
			}
			return new Photo(jObject.getInt("_id"),myDir + fname);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void delNote(Integer id) {
		/*try {
			HttpURLConnection con = (HttpURLConnection) new URL(NOTES_URL).openConnection();
			con.setRequestMethod("DELETE");
			//con.setDoOutput(true);
			con.setUseCaches(false);
			con.connect();
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			String json = "{ _id : " + id.toString() + " }";
			wr.write(json);
			wr.flush();
			//wr.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	public static void delMode(Integer id) {
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(MODES_URL).openConnection();
			con.setRequestMethod("DELETE");
			//con.setDoOutput(true);
			con.setUseCaches(false);
			con.connect();
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			String json = "{ _id : " + id.toString() + " }";
			wr.write(json);
			wr.flush();
			//wr.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void delPlace(Integer id) {
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(PLACES_URL).openConnection();
			con.setRequestMethod("DELETE");
			//con.setDoOutput(true);
			con.setUseCaches(false);
			con.connect();
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			String json = "{ _id : " + id.toString() + " }";
			wr.write(json);
			wr.flush();
			//wr.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void delPhoto(Integer id) {
		/*try {
			HttpURLConnection con = (HttpURLConnection) new URL(PHOTOS_URL).openConnection();
			con.setRequestMethod("DELETE");
			//con.setDoOutput(true);
			con.setUseCaches(false);
			con.connect();
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			String json = "{ _id : " + id.toString() + " }";
			wr.write(json);
			wr.flush();
			//wr.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	public static void setToken(String storedToken) {
		token = storedToken;
	}
}