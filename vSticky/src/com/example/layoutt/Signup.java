package com.example.layoutt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import auth.ServerApi;
public class Signup extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		((Button) findViewById(R.id.btnSignup)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = ((EditText) findViewById(R.id.etxtUsername)).getText().toString();
				String email = ((EditText) findViewById(R.id.etxtEmail)).getText().toString();
				String password = ((EditText) findViewById(R.id.etxtPassword)).getText().toString();
				String password2 = ((EditText) findViewById(R.id.etxtPassword2)).getText().toString();
				if (!password.equals(password2)){
					Toast.makeText(Signup.this, "Passwords didn't match", Toast.LENGTH_SHORT).show();
				} else if (username.equals("") || email.equals("") || password.equals("")) {
					Toast.makeText(Signup.this, "Complete required fields", Toast.LENGTH_SHORT).show();
				} else {
					ServerApi.signup(username, email, password);
					Intent intent = new Intent(getApplicationContext(),
							Login.class);
					startActivity(intent);
				}
			}
		});
	}
}