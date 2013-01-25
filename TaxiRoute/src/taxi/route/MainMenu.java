package taxi.route;

import java.io.IOException;
import java.net.MalformedURLException;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainMenu extends Activity {
	
	ImageView btnLogin;
	Facebook fb;
	private SharedPreferences shared_pref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		btnLogin = (ImageView) findViewById(R.id.buttonFb);
		
		String APP_ID = getString(R.string.APP_ID);
		fb = new Facebook(APP_ID);
		
		//tryToGetDataFromPreviousLogin();
		
		createButtons();
	}

	private void tryToGetDataFromPreviousLogin() {
		shared_pref = getPreferences(MODE_PRIVATE);
		String access_token = shared_pref.getString("access_token", null);
		long expires = shared_pref.getLong("access_expires", 0);
		
		if(access_token != null) {
			fb.setAccessToken(access_token);
		}
		
		if(expires != 0) {
			fb.setAccessExpires(expires);
		}
	}

	private void createButtons() {
		createBtnExit();
		createBtnStart();
		createBtnFbLogin();
		createBtnSettings();		
	}

	private void createBtnExit() {
		Button btnExit = (Button) findViewById(R.id.buttonExit);
		btnExit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				popQuitDialog();
			}
		});
	}
	
	private void createBtnStart() {
		Button btnStart = (Button) findViewById(R.id.buttonStart);
		btnStart.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainMenu.this, MainActivity.class);
				MainMenu.this.startActivity(intent);
			}
		});
	}
	
	private void createBtnFbLogin() {
		btnLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(fb.isSessionValid()) {
					try {
						fb.logout(getApplicationContext());
						updateFbButtonImage();
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else {
					fb.authorize(MainMenu.this, new DialogListener() {
						
						@Override
						public void onFacebookError(FacebookError e) {
							Toast.makeText(MainMenu.this, "onFacebookError", Toast.LENGTH_SHORT).show();
							
						}
						
						@Override
						public void onError(DialogError e) {
							Toast.makeText(MainMenu.this, "onError", Toast.LENGTH_SHORT).show();							
						}
						
						@Override
						public void onComplete(Bundle values) {
							/*
							Editor editor = shared_pref.edit();
							editor.putString("access_token", fb.getAccessToken());
							editor.putLong("access_expires", fb.getAccessExpires());
							editor.commit(); */
							updateFbButtonImage();							
						}
						
						@Override
						public void onCancel() {
							Toast.makeText(MainMenu.this, "onCancel", Toast.LENGTH_SHORT).show();							
						}
					});
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		fb.authorizeCallback(requestCode, resultCode, data);
	}
	
	private void updateFbButtonImage() {
		if(fb.isSessionValid()) {
			btnLogin.setImageResource(R.drawable.logout_button);
		}
		else {
			btnLogin.setImageResource(R.drawable.login_button);
		}
	}
	
	private void createBtnSettings() {
		Button btnSettings = (Button) findViewById(R.id.buttonStatistics);
		btnSettings.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainMenu.this, Statistics.class);
				MainMenu.this.startActivity(intent);
			}
		});
	}

	public void popQuitDialog() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainMenu.this);
		
		alertDialog.setTitle("Do you really want to quit?");
		alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		
		alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		
		alertDialog.show();
	}
	
}
