package taxi.route;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Settings extends Activity implements OnItemSelectedListener {
	private String user_spinner[];
	private String currency_spinner[];
	private String currency;
	private String user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		usersList();
		currencyList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}
	
	public void usersList() {
		user_spinner=new String[3];
		user_spinner[0]="Kingston";
		user_spinner[1]="User one";
		user_spinner[2]="User two";
		Spinner spinnerU = (Spinner) findViewById(R.id.spinnerUser);
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, user_spinner);
		spinnerU.setOnItemSelectedListener(this);
		spinnerU.setAdapter(adapter);
	}
	
	public void currencyList() {
		currency_spinner=new String[2];
		currency_spinner[0]="Leva";
		currency_spinner[1]="Euro";
		Spinner spinnerC = (Spinner) findViewById(R.id.spinnerUser);
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, user_spinner);
		spinnerC.setOnItemSelectedListener(this);
		spinnerC.setAdapter(adapter);
		onItemSelected(spinnerC, spinnerC, 0, 0);
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemSelected(AdapterView<?> spinner, View view, int pos, long id) {
		// if spinner = spinnerUser get user
		// if spinner = spinnerCurrency get currency
	}

}