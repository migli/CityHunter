package lu.uni.cityhunter.activities;

import java.util.ArrayList;
import java.util.Iterator;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.datastructure.City;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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
		if (cities.size() > 0) {
			LinearLayout horizontalLayout = null;
			for (Iterator<City> i = cities.iterator(); i.hasNext(); ) {
				final City city = i.next();
			    if (cities.indexOf(city) % 2 == 0) {
		        	horizontalLayout = new LinearLayout(AllCitiesActivity.this);
			    	horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
			    	scrollViewLayout.addView(horizontalLayout);
		        }
			    OnClickListener cityListener = new OnClickListener() {
					public void onClick(View view) {
						Intent intent = new Intent(AllCitiesActivity.this, CityActivity.class);
						Bundle bundle = new Bundle();  
				        bundle.putParcelable(City.CITY_PAR_KEY, city);  
				        intent.putExtras(bundle);
						startActivity(intent);
					}
				};
			    ImageView image = new ImageView(AllCitiesActivity.this);
		        image.setBackgroundResource(city.getCoverPicture());
		        image.setLayoutParams(new LayoutParams(310, 175));
		        image.setOnClickListener(cityListener);
		        TextView name = new TextView(AllCitiesActivity.this);
		        name.setText(city.getName());
		        name.setPadding(0, 10, 0, 10);
		        name.setTypeface(null, Typeface.BOLD);
		        name.setTextSize(16);
		        name.setTextColor(Color.parseColor("#45AEFF"));
		        name.setOnClickListener(cityListener);
		        TextView description = new TextView(AllCitiesActivity.this);
		        description.setText(city.getDescription());
		        description.setLayoutParams(new LayoutParams(310, 310));
		        description.setOnClickListener(cityListener);
		        LinearLayout verticalLayout = new LinearLayout(AllCitiesActivity.this);
		        verticalLayout.setOrientation(LinearLayout.VERTICAL);
		        if (cities.indexOf(city) % 2 == 0) {
		        	verticalLayout.setPadding(0, 0, 36, 36);
		        } else {
		        	verticalLayout.setPadding(0, 0, 0, 36);
		        }
		        verticalLayout.addView(image);
		        verticalLayout.addView(name);
		        verticalLayout.addView(description);
		        horizontalLayout.addView(verticalLayout);
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
