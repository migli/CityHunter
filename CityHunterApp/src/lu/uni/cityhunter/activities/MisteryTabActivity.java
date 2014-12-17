package lu.uni.cityhunter.activities;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.datastructure.Mistery;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MisteryTabActivity extends TabActivity{

	private TabSpec mapTab, misteryTab;
	
	private Mistery mistery;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mistery_tab);
		
		mistery = getIntent().getParcelableExtra(Mistery.MISTERY_PAR_KEY);

		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
		
		mapTab = tabHost.newTabSpec("Map");
		misteryTab = tabHost.newTabSpec("Mistery");
		
		mapTab.setIndicator("Map");
		Intent intent1 = new Intent(this, MisteryMapActivity.class);
		Bundle bundle1 = new Bundle();  
        bundle1.putParcelable(Mistery.MISTERY_PAR_KEY, mistery);  
        intent1.putExtras(bundle1);
		mapTab.setContent(intent1);
		
		misteryTab.setIndicator("Mistery");
		Intent intent2 = new Intent(this, MisteryInfoActivity.class);
		Bundle bundle2 = new Bundle();  
        bundle2.putParcelable(Mistery.MISTERY_PAR_KEY, mistery);  
        intent2.putExtras(bundle2);
		misteryTab.setContent(intent2);

		tabHost.addTab(mapTab);
		tabHost.addTab(misteryTab);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mistery_tab, menu);
		return true;
	}
	
    
}
