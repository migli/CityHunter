package lu.uni.cityhunter.activities;

import java.util.Iterator;
import java.util.Random;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.persitence.Challenge;
import lu.uni.cityhunter.persitence.City;
import lu.uni.cityhunter.persitence.Mystery;
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

public class CityActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);
		TextView cityName = (TextView) findViewById(R.id.cityName);  
		City city = (City) getIntent().getParcelableExtra(City.CITY_PAR_KEY);  
		cityName.setText(city.getName());
		ImageView cityCoverPicture = (ImageView) findViewById(R.id.cityCoverPicture);
		cityCoverPicture.setImageResource(city.getCoverPicture());
		DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        LayoutParams layout = cityCoverPicture.getLayoutParams();
        layout.height = displaymetrics.heightPixels * 1/3 - 36;
		cityCoverPicture.requestLayout();
		LinearLayout scrollViewLayout = (LinearLayout) findViewById(R.id.scrollViewLayout);
        if (city.getMysteries().size() > 0) {
			LinearLayout horizontalLayout = null;
			for (Iterator<Mystery> i = city.getMysteries().iterator(); i.hasNext(); ) {
				final Mystery mystery = i.next();
			    if (city.getMysteries().indexOf(mystery) % 2 == 0) {
		        	horizontalLayout = new LinearLayout(CityActivity.this);
			    	horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
			        horizontalLayout.setPadding(0, 0, 0, 36);
			    	scrollViewLayout.addView(horizontalLayout);
		        }
			    LinearLayout verticalLayout = new LinearLayout(CityActivity.this);
		        verticalLayout.setOrientation(LinearLayout.VERTICAL);
		        verticalLayout.setBackgroundColor(Color.WHITE);
		        verticalLayout.setBackground(getResources().getDrawable(R.layout.border));
		        verticalLayout.setPadding(1, 1, 1, 1);
		        verticalLayout.setClickable(true);
		        verticalLayout.setOnClickListener(new OnClickListener() {
					public void onClick(View view) {
						Intent intent = new Intent(CityActivity.this, MysteryTabActivity.class);
						Bundle bundle = new Bundle();  
				        bundle.putParcelable(Mystery.MYSTERY_PAR_KEY, mystery);  
				        intent.putExtras(bundle);
				        startActivity(intent);
					}
				});
		        verticalLayout.setLayoutParams(new LayoutParams((displaymetrics.widthPixels - 36 * 3) / 2, displaymetrics.heightPixels / 2 - 2 * 36));
		        ImageView image = new ImageView(CityActivity.this);
		        if (mystery.getChallenges().size() > 0) {
				    Random rand = new Random();
				    Challenge randomChallenge = mystery.getChallenge(rand.nextInt(mystery.getChallenges().size()));
				    image.setBackgroundResource(randomChallenge.getCoverPicture());
		        }
		        image.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.5f));
		        verticalLayout.addView(image);
		        TextView title = new TextView(CityActivity.this);
		        title.setText(mystery.getTitle());
		        title.setPadding(10, 10, 10, 10);
		        title.setTypeface(null, Typeface.BOLD);
		        title.setTextSize(16);
		        title.setTextColor(Color.parseColor("#45AEFF"));
		        verticalLayout.addView(title);
		        TextView question = new TextView(CityActivity.this);
		        question.setText(mystery.getQuestion());
		        question.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
		        question.setPadding(10, 0, 10, 10);
		        question.setTextSize(13);
		        verticalLayout.addView(question);
		        horizontalLayout.addView(verticalLayout);
		        if (city.getMysteries().indexOf(mystery) % 2 == 0) {
		        	LinearLayout spacer = new LinearLayout(CityActivity.this);
		        	spacer.setOrientation(LinearLayout.VERTICAL);
		        	spacer.setLayoutParams(new LayoutParams(36, 0));
			        horizontalLayout.addView(spacer);
		        }
		    }
			TextView sources = new TextView(CityActivity.this);
			sources.setText("Sources from Wikipedia.");
			sources.setGravity(Gravity.RIGHT);
			sources.setTextColor(Color.GRAY);
			scrollViewLayout.addView(sources);
		} else {
			TextView noCities = new TextView(CityActivity.this);
			noCities.setText("No Misteries");
			noCities.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			noCities.setTextSize(16);
			noCities.setPadding(0, 200, 0, 0);
			noCities.setGravity(Gravity.CENTER);
			scrollViewLayout.addView(noCities);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.city, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.action_settings: 
				startActivity(new Intent(CityActivity.this, SettingsActivity.class));
				return true;
			case R.id.action_help: 
				startActivity(new Intent(CityActivity.this, HelpActivity.class));
				return true;
			case R.id.action_about: 
				startActivity(new Intent(CityActivity.this, AboutActivity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
