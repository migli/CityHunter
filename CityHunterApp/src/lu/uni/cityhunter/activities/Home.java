
package lu.uni.cityhunter.activities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.persistence.Challenge;
import lu.uni.cityhunter.persistence.ChallengeType;
import lu.uni.cityhunter.persistence.City;
import lu.uni.cityhunter.persistence.CompassChallenge;
import lu.uni.cityhunter.persistence.Mystery;
import lu.uni.cityhunter.persistence.QuestionChallenge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

/* 
 * The Home class represent the main activity of our application.
 * 
 * @author Christof Ferreira Torres
 */
public class Home extends Activity implements LocationListener {

	private ArrayList<City> cities;
	private SharedPreferences sharedPreferences;
	
	public static final String SCORE_PREFERENCES = "uni.lu.cityhunter.score";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		// Initialize cities array
		cities = new ArrayList<City>();
		// Load persistent data which is stored as JSON in assets
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(loadJSONFromAsset("persistent_data.json"));
			JSONArray jsonCities = jsonObject.getJSONArray("cities");
			for (int i = 0; i < jsonCities.length(); i++) {
				City city = new City(
						jsonCities.getJSONObject(i).getString("name"), 
						jsonCities.getJSONObject(i).getString("description"), 
						Integer.parseInt(jsonCities.getJSONObject(i).getString("previewPicture"), 16),
						Integer.parseInt(jsonCities.getJSONObject(i).getString("coverPicture"), 16),
						new LatLng(jsonCities.getJSONObject(i).getJSONObject("location").getDouble("latitude"), jsonCities.getJSONObject(i).getJSONObject("location").getDouble("longitude")), 
						new ArrayList<Mystery>());
				JSONArray jsonMysteries = jsonCities.getJSONObject(i).getJSONArray("mysteries");
				for (int j = 0; j < jsonMysteries.length(); j++) {
					Mystery mystery = new Mystery(
							jsonMysteries.getJSONObject(j).getString("title"), 
							jsonMysteries.getJSONObject(j).getString("question"), 
							jsonMysteries.getJSONObject(j).getString("answer"), 
							new ArrayList<Challenge>());
					JSONArray jsonChallenges = jsonMysteries.getJSONObject(j).getJSONArray("challenges");
					for (int k = 0; k < jsonChallenges.length(); k++) {
						ChallengeType challengeType = ChallengeType.valueOf(jsonChallenges.getJSONObject(k).getString("challengeType"));
						switch (challengeType) {
							case CHOOSE_DATE: 
							case CHOOSE_PICTURE:
							case GUESS_NAME:
								JSONArray jsonPossibleAnswers = jsonChallenges.getJSONObject(k).getJSONArray("possibleAnswers");
								String[] possibleAnswers = new String[jsonPossibleAnswers.length()];
								for (int l = 0; l < jsonPossibleAnswers.length(); l++) {
									possibleAnswers[l] = jsonPossibleAnswers.getString(l);
								}
								JSONArray jsonPossibleAnswerDescriptions = jsonChallenges.getJSONObject(k).getJSONArray("possibleAnswerDescriptions");
								String[] possibleAnswerDescriptions = new String[jsonPossibleAnswerDescriptions.length()];
								for (int l = 0; l < jsonPossibleAnswerDescriptions.length(); l++) {
									possibleAnswerDescriptions[l] = jsonPossibleAnswerDescriptions.getString(l);
								}								
								mystery.setChallenge(new QuestionChallenge(
										jsonChallenges.getJSONObject(k).getString("title"), 
										jsonChallenges.getJSONObject(k).getString("description"),
										Integer.parseInt(jsonChallenges.getJSONObject(k).getString("coverPicture"), 16), 
										new LatLng(jsonChallenges.getJSONObject(k).getJSONObject("location").getDouble("latitude"), jsonChallenges.getJSONObject(k).getJSONObject("location").getDouble("longitude")), 
										jsonChallenges.getJSONObject(k).getInt("maxNrOfTries"), 
										challengeType, 
										possibleAnswers, 
										possibleAnswerDescriptions, 
										jsonChallenges.getJSONObject(k).getString("answer"),
										jsonChallenges.getJSONObject(k).getString("answerDescription")));
								break;
							case FIND_DIRECTION:
								mystery.setChallenge(new CompassChallenge(
										jsonChallenges.getJSONObject(k).getString("title"), 
										jsonChallenges.getJSONObject(k).getString("description"),
										Integer.parseInt(jsonChallenges.getJSONObject(k).getString("coverPicture"), 16), 
										new LatLng(jsonChallenges.getJSONObject(k).getJSONObject("location").getDouble("latitude"), jsonChallenges.getJSONObject(k).getJSONObject("location").getDouble("longitude")), 
										jsonChallenges.getJSONObject(k).getInt("maxNrOfTries"),
										challengeType,
										jsonChallenges.getJSONObject(k).getInt("direction"), 
										jsonChallenges.getJSONObject(k).getInt("time")));
								break;
							default:
								break;
						}
					}
					city.setMystery(mystery);
				}
				cities.add(city);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// Load and display score
		sharedPreferences = getSharedPreferences(SCORE_PREFERENCES, MODE_PRIVATE);
		TextView score = (TextView) findViewById(R.id.textViewScore);
		score.setText("Score: " + String.valueOf(sharedPreferences.getInt("score", 0)));
		// Hide initially the current city button and preview
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
			case R.id.action_help: 
				startActivity(new Intent(Home.this, HelpActivity.class));
				return true;
			case R.id.action_about: 
				startActivity(new Intent(Home.this, AboutActivity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Load and display score
		sharedPreferences = getSharedPreferences(SCORE_PREFERENCES, MODE_PRIVATE);
		TextView score = (TextView) findViewById(R.id.textViewScore);
		score.setText("Score: " + String.valueOf(sharedPreferences.getInt("score", 0)));
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
	
	private String loadJSONFromAsset(String fileName) {
	    String JSON = null;
	    try {
	        InputStream inputStream = getAssets().open(fileName);
	        int size = inputStream.available();
	        byte[] buffer = new byte[size];
	        inputStream.read(buffer);
	        inputStream.close();
	        JSON = new String(buffer, "UTF-8");
	    } catch (IOException ex) {
	        ex.printStackTrace();
	        return null;
	    }
	    return JSON;
	}
	
}
