package lu.uni.cityhunter.activities;

import java.util.ArrayList;
import java.util.Iterator;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.datastructure.Challenge;
import lu.uni.cityhunter.datastructure.Mistery;
import lu.uni.cityhunter.datastructure.MyInfoWindowAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MisteryActivity extends Activity {

	static final LatLng LUXEMBOURG = new LatLng(49.611498, 6.131750);
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mystery);

		Mistery mistery = (Mistery) getIntent().getParcelableExtra(Mistery.MISTERY_PAR_KEY);
		setTitle(mistery.getTitle());

		MyInfoWindowAdapter infoAdapter = new MyInfoWindowAdapter(getLayoutInflater());
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setInfoWindowAdapter(infoAdapter);
		
		ArrayList<Challenge> c = mistery.getChallenges();
		Iterator<Challenge> iter = c.iterator();
		while(iter.hasNext()){
			Challenge challenge = iter.next(); 
			map.addMarker(new MarkerOptions()
			.position(challenge.getLocation())
			.title(challenge.getTitle())
			.snippet(challenge.getDescription()));
		}
		
		// Move the camera instantly to luxembourg with a zoom of 15.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(LUXEMBOURG, 15));
		map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
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
				startActivity(new Intent(MisteryActivity.this, SettingsActivity.class));
				return true;
			case R.id.action_about: 
				startActivity(new Intent(MisteryActivity.this, AboutActivity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
