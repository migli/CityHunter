
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
import android.widget.ImageView;

public class Home extends Activity {

	private ArrayList<City> cities;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		// Initialize cities array
		cities = new ArrayList<City>();
		City luxembourg = new City("Luxembourg", "Luxembourg City lies at the heart of Western Europe, situated 213 km (132 mi) by road from Brussels, 372 km (231 mi) from Paris, 209 km (130 mi) from Cologne.", R.drawable.luxembourg_preview, R.drawable.luxembourg_cover);
		cities.add(luxembourg);
		City paris = new City("Paris", "Paris is the capital and most populous city of France. Situated on the Seine River, in the north of the country, it is at the heart of the \u00cele-de-France region, also known as the r\u00e9gion parisienne.", 0, R.drawable.paris_cover);
		cities.add(paris);
		City lisbon = new City("Lisbon", "Lisbon is the capital and the largest city of Portugal. It is the westernmost large city located in continental Europe, as well as its westernmost capital city and the only one along the Atlantic coast.", 0, R.drawable.lisbon_cover);
		cities.add(lisbon);
		City dublin = new City("Dublin", "Dublin is the capital and largest city of Ireland. Dublin is in the province of Leinster on Ireland's east coast, at the mouth of the River Liffey.", 0, R.drawable.dublin_cover);
		cities.add(dublin);
		// Initialize current city button
		final City currentCity;
		ImageView currentCityPreview = (ImageView) findViewById(R.id.imageViewCurrentCityPreview);
		if (cities.size() > 0) {
			currentCity = cities.get(0);
			currentCityPreview.setImageResource(currentCity.getPreviewPicture());
		} else {
			currentCity = null;
			currentCityPreview.setVisibility(View.INVISIBLE);
		}
		Button currentCityBtn = (Button) findViewById(R.id.currentCity);
		if (currentCity != null) {
			currentCityBtn.setText(currentCity.getName());
			currentCityBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					Intent intent = new Intent(Home.this, CityActivity.class);
					Bundle bundle = new Bundle();  
			        bundle.putParcelable(City.CITY_PAR_KEY, currentCity);  
			        intent.putExtras(bundle);
					startActivity(intent);
				}
			});
		} else {
			currentCityBtn.setVisibility(View.INVISIBLE);
		}
		// Initialize all cities button
		Button allCities = (Button) findViewById(R.id.allCities);
		allCities.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Home.this, AllCitiesActivity.class);
				Bundle bundle = new Bundle();  
		        bundle.putParcelableArrayList(City.CITY_ARRAY_PAR_KEY, cities);  
		        intent.putExtras(bundle);
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
