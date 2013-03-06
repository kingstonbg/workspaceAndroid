package taxi.route;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AfterTracking extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_after_tracking);
		
		Button share = (Button)findViewById(R.id.share);
		EditText distanceTraveledText = (EditText)findViewById(R.id.distanceTraveledText);
		EditText moneyPaidText        = (EditText)findViewById(R.id.moneyPaidText);
		//distanceTraveledText.setText(MainActivity.getDistanceTraveled()));
		//moneyPaidText       .setText(MainActivity.getDistanceTraveled());
		share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
				MainMenu.postMessageToFb();
				MainMenu.updateLoginStatus();
            }
        });
	}
}
