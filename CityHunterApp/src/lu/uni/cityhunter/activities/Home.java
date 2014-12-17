
package lu.uni.cityhunter.activities;

import java.util.ArrayList;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.challenges.ChooseDate;
import lu.uni.cityhunter.challenges.ChoosePicture;
import lu.uni.cityhunter.challenges.FindDirection;
import lu.uni.cityhunter.challenges.GuessName;
import lu.uni.cityhunter.datastructure.Challenge;
import lu.uni.cityhunter.datastructure.City;
import lu.uni.cityhunter.datastructure.Mistery;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

/* 
 * The Home class represent the main activity of our application.
 * 
 * @author Christof Ferreira Torres
 */
public class Home extends Activity implements LocationListener {

	private ArrayList<City> cities;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		// Initialize cities array
		cities = new ArrayList<City>();
		// Luxembourg
		City luxembourg = new City("Luxembourg", "Luxembourg City lies at the heart of Western Europe, situated 213 km (132 mi) by road from Brussels, 372 km (231 mi) from Paris, 209 km (130 mi) from Cologne.", R.drawable.luxembourg_preview, R.drawable.luxembourg_cover, new LatLng(49.611498, 6.131750), new ArrayList<Mistery>());
		Mistery mistery1 = new Mistery("Mistery 1", "As according to the comic series 'The Hitchhiker's Guide to the Galaxy ', what is the sense of life?", "42", new ArrayList<Challenge>());
		
		mistery1.setChallenge(new ChooseDate("G\u00eblle Fra", "The G\u00eblle Fra (Golden Lady) is a war memorial dedicated to the volunteers of WWI. The Nazis dismantled the memorial in 1940 and it remained unaccounted until 1980.\n\nIn which year did Grand Duke Jean unveil the memorial back to the public?", R.drawable.gelle_fra_cover, new LatLng(49.609350, 6.129358), null, 2, new int[]{1980, 1986, 1991}, 1985));
		mistery1.setChallenge(new ChoosePicture("Notre-Dame Cathedral", "Which one of these members of the Grand-Ducal family is not burried in the crypt of the Cathedral?", R.drawable.cathedral, new LatLng(49.609405, 6.131161), null, 2, new int[]{R.drawable.john_the_blind, R.drawable.marie_anne_of_portugal, R.drawable.charlotte_grand_duchess_of_luxembourg}, new String[]{"John the Blind", "Marie Anne of Portugal", "Charlotte, Grand Duchess of Luxembourg"}, R.drawable.adolphe_grand_duke_of_luxembourg, "Adolphe, Grand Duke of Luxembourg"));
		mistery1.setChallenge(new GuessName("Place Guillaume II", "How do locals call this place?", R.drawable.place_guillaume, new LatLng(49.610833, 6.130278) , null, 3, "Knuedler"));
		mistery1.setChallenge(new ChoosePicture("Grand Ducal Palace", "Which one of these guards is a guard of the Grand Ducal Palace?", R.drawable.palais_cover, new LatLng(49.610817, 6.132635), null, 2, new int[]{R.drawable.british_guard, R.drawable.russian_guard, R.drawable.swiss_guard}, new String[]{"", "", ""}, R.drawable.luxemburgish_guard, ""));
		mistery1.setChallenge(new FindDirection("Place de Clairefontaine", "Turn your phone in the direction that Grand Duchess Charlotte is pointing at and maintain this direction for 30 seconds.", R.drawable.place_clairefontaine, new LatLng(49.609849, 6.132466), null, 2, 110, 30));

		luxembourg.setMistery(mistery1);
		cities.add(luxembourg);
		// Paris
		City paris = new City("Paris", "Paris is the capital and most populous city of France. Situated on the Seine River, in the north of the country, it is at the heart of the \u00cele-de-France region, also known as the r\u00e9gion parisienne.", 0, R.drawable.paris_cover, new LatLng(48.8567, 2.3508), new ArrayList<Mistery>());
		cities.add(paris);
		// Lisbon
		City lisbon = new City("Lisbon", "Lisbon is the capital and the largest city of Portugal. It is the westernmost large city located in continental Europe, as well as its westernmost capital city and the only one along the Atlantic coast.", 0, R.drawable.lisbon_cover, new LatLng(38.713811, -9.139386), new ArrayList<Mistery>());
		cities.add(lisbon);
		// Dublin
		City dublin = new City("Dublin", "Dublin is the capital and largest city of Ireland. Dublin is in the province of Leinster on Ireland's east coast, at the mouth of the River Liffey.", 0, R.drawable.dublin_cover, new LatLng(53.347778, -6.259722), new ArrayList<Mistery>());
		cities.add(dublin);
		// Hide initially he current city button and preview
		ImageView currentCityPreview = (ImageView) findViewById(R.id.imageViewCurrentCityPreview);
		currentCityPreview.setVisibility(View.INVISIBLE);
		Button currentCityBtn = (Button) findViewById(R.id.currentCity);
		currentCityBtn.setVisibility(View.INVISIBLE);
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
		// Initialize the current location manager
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);		
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
		switch (item.getItemId()) {
			case R.id.action_settings: 
				startActivity(new Intent(Home.this, SettingsActivity.class));
				return true;
			case R.id.action_about: 
				startActivity(new Intent(Home.this, AboutActivity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		final City currentCity;
		ImageView currentCityPreview = (ImageView) findViewById(R.id.imageViewCurrentCityPreview);
		if (cities.size() > 0) {
			// -- Find the city which is the closest to our current location --
			City closestCity = cities.get(0);
			for (int i = 1; i < cities.size(); i++) {
				float[] closestCityResults = new float[1];
				Location.distanceBetween(location.getLatitude(), location.getLongitude(), closestCity.getLocation().latitude, closestCity.getLocation().longitude, closestCityResults);
				float[] results = new float[1];
				Location.distanceBetween(location.getLatitude(), location.getLongitude(), cities.get(i).getLocation().latitude, cities.get(i).getLocation().longitude, results);
				if (results[0] < closestCityResults[0]) {
					closestCity = cities.get(i);
				}
			}
			// ----------------------------------------------------------------
			currentCity = closestCity;
			currentCityPreview.setImageResource(currentCity.getPreviewPicture());
			currentCityPreview.setVisibility(View.VISIBLE);
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
			currentCityBtn.setVisibility(View.VISIBLE);
		} else {
			currentCityBtn.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}
