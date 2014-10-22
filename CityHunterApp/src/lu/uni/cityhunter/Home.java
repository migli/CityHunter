
package lu.uni.cityhunter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Home extends Activity {

	private ArrayList<City> cities;
	 
    protected final static String CITY_PAR_KEY = "lu.uni.city.par"; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		// Initialize cities array
		cities = new ArrayList<City>();
		City luxembourg = new City("Luxembourg", "Luxembourg City lies at the heart of Western Europe, situated 213 km (132 mi) by road from Brussels, 372 km (231 mi) from Paris, 209 km (130 mi) from Cologne.");
		cities.add(luxembourg);
		// Initialize current city button
		final City currentCity = cities.get(0);
		Button currentCityBtn = (Button) findViewById(R.id.currentCity);
		currentCityBtn.setText(currentCity.getName());
		currentCityBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(Home.this, CityActivity.class);
				Bundle bundle = new Bundle();  
		        bundle.putParcelable(CITY_PAR_KEY, currentCity);  
		        intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		// Initialize all cities button
		Button allCities = (Button) findViewById(R.id.allCities);
		allCities.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Home.this, AllCitiesActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
