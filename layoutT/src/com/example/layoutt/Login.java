package com.example.layoutt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import auth.ServerApi;
import auth.SessionManager;


public class Login extends Activity {

	public static final String TAG = "VSN::MainActivity";
	private static EditText etxtUsername, etxtPassword;
	public SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		// Set session manager
		this.sessionManager = new SessionManager(getApplicationContext());
		String storedUsername = sessionManager.getUserDetails().get(
				SessionManager.KEY_USERNAME);
		String storedToken = sessionManager.getUserDetails().get(
				SessionManager.KEY_TOKEN);
		boolean isLoggeIn = sessionManager.isLoggedIn();
		
		// If was logged in and authorizator created a token (token and secret
		// provided)
		// And open new intent
		if (isLoggeIn 
				&& storedUsername != null
				&& storedToken != null) {
			Intent intent = new Intent(getApplicationContext(),Notepad.class);
			intent.putExtra("Username", storedUsername);
			intent.putExtra("Token", storedToken);
			startActivity(intent);
		} else {
			// Change layout to activity_main (login) if token not present
			setContentView(R.layout.activity_login);

			// Handling text input
			etxtUsername = (EditText) findViewById(R.id.etxtUsername);
			etxtPassword = (EditText) findViewById(R.id.etxtPassword);
			etxtUsername.setText(storedUsername);

			// Adding OnClickListener to login button
			((Button) findViewById(R.id.btnLogin))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Getting username from text field etxtUsername
						String username = etxtUsername.getText().toString();
						// Getting username from text field etxtPassword
						String password = etxtPassword.getText().toString();

						// Check login validity
						String authToken = "";
						//try{
							authToken = ServerApi.authenticate(username, password);
							if (authToken != null && authToken != "") {
								// Store username, password and token
								// credintials to remember
								if (rememberMe()) {
									sessionManager.createLoginSession(username,authToken);
								}
								Intent intent = new Intent(Login.this,Notepad.class);
								startActivity(intent);
							} else {
								Toast.makeText(getApplicationContext(),
										"Invalid Credentials",
										Toast.LENGTH_LONG).show();
							}
						//} catch (Exception e){
							//Toast.makeText(getApplicationContext(), "Cannot Connect to Server", Toast.LENGTH_SHORT).show();
						//}
					}
				});
			((Button) findViewById(R.id.btnSignup))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Login.this,
								Signup.class);
						startActivity(intent);
					}
				});
		}
    }
	private boolean rememberMe() {
		return ((CheckBox) findViewById(R.id.chkBoxRememberMe)).isChecked();
	}
}