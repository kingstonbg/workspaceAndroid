package taxi.route;

import java.io.IOException;
import java.net.MalformedURLException;

import com.facebook.Session;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends Activity {
	
	ImageView btnLogin;
	Facebook fb;
	String fbName;
	private SharedPreferences shared_pref;
	private static final String FACEBOOK_PERMISSION = "publish_stream";
	private static final String TAG = "FacebookSample";
	private static final String MSG = "Message from FacebookSample";
	
	private final Handler mFacebookHandler = new Handler();
	private FacebookConnector facebookConnector;
	private TextView loginStatus;
	
	final Runnable mUpdateFacebookNotification = new Runnable() {
        public void run() {
        	Toast.makeText(getBaseContext(), "Facebook updated !", Toast.LENGTH_LONG).show();
        }
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		loginStatus = (TextView)findViewById(R.id.login_status);
		Button clearCredentials = (Button) findViewById(R.id.btn_clear_credentials);
		Button share = (Button)findViewById(R.id.share);
		String APP_ID = getString(R.string.APP_ID);
		fb = new Facebook(APP_ID);
		
		this.facebookConnector = new FacebookConnector(APP_ID, this,
				getApplicationContext(), new String[] {FACEBOOK_PERMISSION});
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		share.setOnClickListener(new View.OnClickListener() {
        
            public void onClick(View v) {
        		postMessage();
            }
        });
		
		clearCredentials.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	clearCredentials();
            	updateLoginStatus();
            }
        });
		
		createButtons();
	}

	private void createButtons() {
		createBtnExit();
		createBtnStart();
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
	/*------------------------------F A C E B O O K----------------------------------*/
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		this.facebookConnector.getFacebook().authorizeCallback(requestCode, resultCode, data);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		updateLoginStatus();
	}

	public void updateLoginStatus() {
		loginStatus.setText("Logged into Facebook as " + fbName + " " + facebookConnector.getFacebook().isSessionValid());
	}
	
	public void postMessage() {

		if (facebookConnector.getFacebook().isSessionValid()) {
			postMessageInThread();
		} else {
			SessionEvents.AuthListener listener = new SessionEvents.AuthListener() {

				@Override
				public void onAuthSucceed() {
					postMessageInThread();
				}

				@Override
				public void onAuthFail(String error) {

				}
			};
			SessionEvents.addAuthListener(listener);
			facebookConnector.login();
		}
	}
	
	private void postMessageInThread() {
		Thread t = new Thread() {
			public void run() {

		    	try {
		    		facebookConnector.postMessageOnWall("test");
					mFacebookHandler.post(mUpdateFacebookNotification);
				} catch (Exception ex) {
					Log.e(TAG, "Error sending msg",ex);
				}
		    }
		};
		t.start();
	}
	
	private void clearCredentials() {
		try {
			facebookConnector.getFacebook().logout(getApplicationContext());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*------------------------------F A C E B O O K----------------------------------*/
	
}
