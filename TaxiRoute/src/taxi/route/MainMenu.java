package taxi.route;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends Activity {
	
	String fbName;
	String fbId;
	private final String MY_TAG = "TaxiMainMenu";
	private static final String FACEBOOK_PERMISSION = "publish_stream";
	private static final String TAG = "FacebookSample";
	private static final String MSG = "Message from FacebookSample";
	
	private final static Handler mFacebookHandler = new Handler();
	private static FacebookConnector facebookConnector;
	private static TextView loginStatus;
	private static Button clearCredentials;
	/*final static Runnable mUpdateFacebookNotification = new Runnable() {
        public void run() {
        	Toast.makeText(getBaseContext(), "Facebook updated !", Toast.LENGTH_LONG).show();
        }
    };*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		loginStatus = (TextView)findViewById(R.id.login_status);
		clearCredentials = (Button) findViewById(R.id.btn_clear_credentials);
		//Button share = (Button)findViewById(R.id.share);
		String APP_ID = getString(R.string.APP_ID);
		
		this.facebookConnector = new FacebookConnector(APP_ID, this,
				getApplicationContext(), new String[] {FACEBOOK_PERMISSION});
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		updateLoginStatus();
		/*
		share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
				postMessageToFb();
				updateLoginStatus();
            }
        });
		*/
		
		clearCredentials.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(facebookConnector.getFacebook().isSessionValid()) {
            		clearCredentials();
            	}
            	else {
            		facebookConnector.login();
            	}
            	updateLoginStatus();
            }
        });
		
		createButtons();
	}

	private void createButtons() {
		createBtnExit();
		createBtnStart();
		createBtnStatistics();		
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
	
	private static void updateFbButtonImage() {
		if(facebookConnector.getFacebook().isSessionValid()) {
			clearCredentials.setBackgroundResource(R.drawable.logout_button);
		}
		else {
			clearCredentials.setBackgroundResource(R.drawable.login_button);
		}
	}
	
	private void createBtnStatistics() {
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

	public static void updateLoginStatus() {
		loginStatus.setText("Logged into Facebook as "+ facebookConnector.getUserName() + " " +
				facebookConnector.getFacebook().isSessionValid());
		updateFbButtonImage();
	}

	public static void postMessageToFb() {
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
	
	private static void postMessageInThread() {
		Thread t = new Thread() {
			public void run() {
		    	try {
		    		facebookConnector.postMessageOnWall("test");
					//mFacebookHandler.post(mUpdateFacebookNotification);
				} catch (Exception ex) {
					Log.e(TAG, "Error sending msg",ex);
				}
		    }
		};
		t.start();
	}
	
	private void clearCredentials() {
		Thread t = new Thread() {
			public void run() {
		    	try {
		    		facebookConnector.getFacebook().logout(getApplicationContext());
				} catch (Exception ex) {
					
				}
		    }
		};
		t.start();
	}
	
	/*------------------------------F A C E B O O K----------------------------------*/

}
