package lu.uni.cityhunter.activities;

import lu.uni.cityhunter.R;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		PackageManager manager = getApplicationContext().getPackageManager();
		PackageInfo info;
		try {
			info = manager.getPackageInfo(getApplicationContext().getPackageName(), 0);
			TextView copyright = (TextView) findViewById(R.id.copyright);
			copyright.setText("Version " + info.versionName + " \u00a9 Copyright 2014.");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
}
