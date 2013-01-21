package taxi.route;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		createButtons();
	}

	private void createButtons() {
		createBtnExit();
		createBtnStart();
		createBtnStop();
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
	
	private void createBtnStop() {
		Button btnStop = (Button) findViewById(R.id.buttonStop);
		btnStop.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//stop MainActivity.java
			}
		});
	}
	
	private void createBtnSettings() {
		Button btnSettings = (Button) findViewById(R.id.buttonSettings);
		btnSettings.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainMenu.this, Settings.class);
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
