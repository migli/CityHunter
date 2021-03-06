package lu.uni.cityhunter.activities;

import java.util.ArrayList;
import java.util.Iterator;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.persistence.City;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AllCitiesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_cities);
		ArrayList<City> cities = getIntent().getParcelableArrayListExtra(City.CITY_ARRAY_PAR_KEY);  
		LinearLayout scrollViewLayout = (LinearLayout) findViewById(R.id.scrollViewLayout);
		DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        if (cities.size() > 0) {
			LinearLayout horizontalLayout = null;
			for (Iterator<City> i = cities.iterator(); i.hasNext(); ) {
				final City city = i.next();
			    if (cities.indexOf(city) % 2 == 0) {
		        	horizontalLayout = new LinearLayout(AllCitiesActivity.this);
		        	horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
			        horizontalLayout.setPadding(0, 0, 0, 36);
			    	scrollViewLayout.addView(horizontalLayout);
		        }
			    LinearLayout verticalLayout = new LinearLayout(AllCitiesActivity.this);
		        verticalLayout.setOrientation(LinearLayout.VERTICAL);
		        verticalLayout.setBackgroundColor(Color.WHITE);
		        verticalLayout.setBackground(getResources().getDrawable(R.layout.border));
		        verticalLayout.setPadding(1, 1, 1, 1);
		        verticalLayout.setClickable(true);
		        verticalLayout.setOnClickListener(new OnClickListener() {
					public void onClick(View view) {
						Intent intent = new Intent(AllCitiesActivity.this, CityActivity.class);
						Bundle bundle = new Bundle();  
				        bundle.putParcelable(City.CITY_PAR_KEY, city);  
				        intent.putExtras(bundle);
						startActivity(intent);
					}
				});
		        verticalLayout.setLayoutParams(new LayoutParams((displaymetrics.widthPixels - 36 * 3) / 2, displaymetrics.heightPixels / 2 - 2 * 36));
		        ImageView image = new ImageView(AllCitiesActivity.this);
		        image.setBackgroundResource(city.getCoverPicture());
		        image.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.5f));
		        verticalLayout.addView(image);
		        TextView name = new TextView(AllCitiesActivity.this);
		        name.setText(city.getName());
		        name.setPadding(10, 10, 10, 10);
		        name.setTypeface(null, Typeface.BOLD);
		        name.setTextSize(16);
		        name.setTextColor(Color.parseColor("#45AEFF"));
		        verticalLayout.addView(name);
		        TextView description = new TextView(AllCitiesActivity.this);
		        description.setText(city.getDescription());
		        description.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
		        description.setPadding(10, 0, 10, 10);
		        description.setTextSize(13);
		        verticalLayout.addView(description);
		        horizontalLayout.addView(verticalLayout);
		        if (cities.indexOf(city) % 2 == 0) {
		        	LinearLayout spacer = new LinearLayout(AllCitiesActivity.this);
		        	spacer.setOrientation(LinearLayout.VERTICAL);
		        	spacer.setLayoutParams(new LayoutParams(36, 0));
			        horizontalLayout.addView(spacer);
		        }
		    }
			TextView sources = new TextView(AllCitiesActivity.this);
			sources.setText("Sources from Wikipedia.");
			sources.setGravity(Gravity.RIGHT);
			sources.setTextColor(Color.GRAY);
			scrollViewLayout.addView(sources);
		} else {
			TextView noCities = new TextView(AllCitiesActivity.this);
			noCities.setText("No Cities");
			noCities.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			noCities.setTextSize(16);
			noCities.setPadding(0, 400, 0, 0);
			noCities.setGravity(Gravity.CENTER);
			scrollViewLayout.addView(noCities);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.all_cities, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.action_settings: 
				startActivity(new Intent(AllCitiesActivity.this, SettingsActivity.class));
				return true;
			case R.id.action_help: 
				startActivity(new Intent(AllCitiesActivity.this, HelpActivity.class));
				return true;
			case R.id.action_about: 
				startActivity(new Intent(AllCitiesActivity.this, AboutActivity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
