package at.jku.stockticker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import at.jku.stockticker.R;
import at.jku.stockticker.SessionManager;
import at.jku.stockticker.services.UserManagementService;

public class LoginActivity extends Activity {
	
	SessionManager session;
	
	private EditText usernameTxt;
	private EditText passwordTxt;
	private Button loginBtn;
	private Button signupBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		usernameTxt = (EditText) findViewById(R.id.usernameLogin);
		passwordTxt = (EditText) findViewById(R.id.passwordLogin);
		loginBtn = (Button) findViewById(R.id.loginButton);
		signupBtn = (Button) findViewById(R.id.signUpButtonLogin);
		
		session = new SessionManager(getApplicationContext());
		
		signupBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(i);
                finish();				
			}
		});
		
        loginBtn.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View arg0) {
                String username = usernameTxt.getText().toString();
                String password = passwordTxt.getText().toString();
                 
                if(username.trim().length() > 0 && password.trim().length() > 0){
                	try {
						String ret = new UserManagementService().signIn(username, password);
						if(ret != null && ret.equals("200")) {
	                        session.createLoginSession(username, password);
	                         
	                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
	                        startActivity(i);
	                        finish();
	                    }else{
	                    	Toast.makeText(LoginActivity.this, R.string.err_wrong_credentials, Toast.LENGTH_LONG).show();
	                    }        
					} catch (Exception e) {
						Log.e(LoginActivity.this.getClass().getName(), e.getMessage());
						Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
					}
                }else{
                    Toast.makeText(LoginActivity.this, R.string.err_no_credentials, Toast.LENGTH_LONG).show();
                }
            }
        });
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

}
