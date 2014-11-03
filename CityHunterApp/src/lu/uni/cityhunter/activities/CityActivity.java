package lu.uni.cityhunter.activities;

import java.util.Iterator;
import java.util.Random;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.datastructure.Challenge;
import lu.uni.cityhunter.datastructure.City;
import lu.uni.cityhunter.datastructure.Mistery;

import android.app.Activity;
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
		LinearLayout scrollViewLayout = (LinearLayout) findViewById(R.id.scrollViewLayout);
		if (city.getMisteries().size() > 0) {
			LinearLayout horizontalLayout = null;
			for (Iterator<Mistery> i = city.getMisteries().iterator(); i.hasNext(); ) {
				final Mistery mistery = i.next();
			    if (city.getMisteries().indexOf(mistery) % 2 == 0) {
		        	horizontalLayout = new LinearLayout(CityActivity.this);
			    	horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
			        horizontalLayout.setPadding(0, 0, 0, 36);
			    	scrollViewLayout.addView(horizontalLayout);
		        }
			    ImageView image = new ImageView(CityActivity.this);
			    Random rand = new Random();
			    Challenge randomChallenge = mistery.getChallenge(rand.nextInt(mistery.getChallenges().size()));
			    image.setBackgroundResource(randomChallenge.getCoverPicture());
		        image.setLayoutParams(new LayoutParams(310, 175));
		        TextView title = new TextView(CityActivity.this);
		        title.setText(mistery.getTitle());
		        title.setPadding(10, 10, 10, 10);
		        title.setTypeface(null, Typeface.BOLD);
		        title.setTextSize(16);
		        title.setTextColor(Color.parseColor("#45AEFF"));
		        TextView question = new TextView(CityActivity.this);
		        question.setText(mistery.getQuestion());
		        question.setLayoutParams(new LayoutParams(310, 210));
		        question.setPadding(10, 0, 10, 10);
		        question.setTextSize(13);
		        LinearLayout verticalLayout = new LinearLayout(CityActivity.this);
		        verticalLayout.setOrientation(LinearLayout.VERTICAL);
		        verticalLayout.setBackgroundColor(Color.WHITE);
		        verticalLayout.setBackground(getResources().getDrawable(R.drawable.border));
		        verticalLayout.setPadding(1, 1, 1, 1);
		        verticalLayout.setClickable(true);
		        verticalLayout.setOnClickListener(new OnClickListener() {
					public void onClick(View view) {
						/*Intent intent = new Intent(CityActivity.this, MisteryActivity.class);
						Bundle bundle = new Bundle();  
				        bundle.putParcelable(Mistery.MISTERY_PAR_KEY, mistery);  
				        intent.putExtras(bundle);
						startActivity(intent);*/
					}
				});
		        verticalLayout.addView(image);
		        verticalLayout.addView(title);
		        verticalLayout.addView(question);
		        horizontalLayout.addView(verticalLayout);
		        if (city.getMisteries().indexOf(mistery) % 2 == 0) {
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
