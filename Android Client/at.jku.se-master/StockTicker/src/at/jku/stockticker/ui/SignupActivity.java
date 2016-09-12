package at.jku.stockticker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import at.jku.stockticker.R;
import at.jku.stockticker.services.UserManagementService;

public class SignupActivity extends Activity {
	
	private EditText firstnameTxt;
	private EditText surnameTxt;
	private EditText usernameTxt;
	private EditText passwordTxt;
	private EditText passwordReplyTxt;
	
	private Button signupBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		
		this.firstnameTxt = (EditText) findViewById(R.id.firstname);
		this.surnameTxt = (EditText) findViewById(R.id.surname);
		this.usernameTxt = (EditText) findViewById(R.id.username);
		this.passwordTxt = (EditText) findViewById(R.id.password);
		this.passwordReplyTxt = (EditText) findViewById(R.id.passwordReply);
		
		this.signupBtn = (Button) findViewById(R.id.signupButton);
		
		this.signupBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String firstname = firstnameTxt.getText().toString();
				String surname = surnameTxt.getText().toString();
				String username = usernameTxt.getText().toString();
				String password = passwordTxt.getText().toString();
				String passwordReply = passwordReplyTxt.getText().toString();
				
				if(firstname.isEmpty() || surname.isEmpty() || username.isEmpty() ||
						password.isEmpty() || passwordReply.isEmpty()) {
					Toast.makeText(SignupActivity.this, R.string.err_fillin, Toast.LENGTH_LONG).show();
				} else if(!password.equals(passwordReply)) {
					Toast.makeText(SignupActivity.this, R.string.err_password, Toast.LENGTH_LONG).show();
				} else {
					try {
						String ret = new UserManagementService().signUp(username, password);
						Toast.makeText(SignupActivity.this, ret, Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						Log.e(this.getClass().getName(), e.getLocalizedMessage());
						Toast.makeText(SignupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
					}
					
					Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
				}				
			}
		});		
	}

}
