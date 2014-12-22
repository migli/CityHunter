package lu.uni.cityhunter.activities;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.persistence.Mystery;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MysteryTabActivity extends TabActivity{

	private TabSpec mapTab, misteryTab;
	
	private Mystery mystery;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mystery_tab);
		
		mystery = getIntent().getParcelableExtra(Mystery.MYSTERY_PAR_KEY);

		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
		
		mapTab = tabHost.newTabSpec("Map");
		misteryTab = tabHost.newTabSpec("Mystery");
		
		mapTab.setIndicator("Map");
		Intent intent1 = new Intent(this, MysteryMapActivity.class);
		Bundle bundle1 = new Bundle();  
        bundle1.putParcelable(Mystery.MYSTERY_PAR_KEY, mystery);  
        intent1.putExtras(bundle1);
		mapTab.setContent(intent1);
		
		misteryTab.setIndicator("Mystery");
		Intent intent2 = new Intent(this, MysteryInfoActivity.class);
		Bundle bundle2 = new Bundle();  
        bundle2.putParcelable(Mystery.MYSTERY_PAR_KEY, mystery);  
        intent2.putExtras(bundle2);
		misteryTab.setContent(intent2);

		tabHost.addTab(mapTab);
		tabHost.addTab(misteryTab);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mystery, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.action_settings: 
				startActivity(new Intent(MysteryTabActivity.this, SettingsActivity.class));
				return true;
			case R.id.action_help: 
				startActivity(new Intent(MysteryTabActivity.this, HelpActivity.class));
				return true;
			case R.id.action_about: 
				startActivity(new Intent(MysteryTabActivity.this, AboutActivity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
    
}
