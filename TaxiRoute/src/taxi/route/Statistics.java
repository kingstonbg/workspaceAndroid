package taxi.route;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.widget.TextView;

public class Statistics extends Activity {

	private FacebookConnector facebookConnector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		TextView userList = (TextView)findViewById(R.id.userList);
		DatabaseHandler db = new DatabaseHandler(null);
		
		if(facebookConnector.getFacebook().isSessionValid()) {
			userList.setText(db.showDatabase().toString());
		}
		else {
			facebookConnector.login();
			db.addNewUser();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}

}

