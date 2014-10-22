package lu.uni.cityhunter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AllCitiesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_cities);
		LinearLayout scrollViewLayout = (LinearLayout) findViewById(R.id.scrollViewLayout);
		LinearLayout horizontalLayout = null;
		for (int i = 0; i < 6; i++) {
	    	if (i % 2 == 0) {
	        	horizontalLayout = new LinearLayout(AllCitiesActivity.this);
		    	horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
		    	scrollViewLayout.addView(horizontalLayout);
	        }
	    	ImageView image = new ImageView(AllCitiesActivity.this);
	        image.setBackgroundResource(R.drawable.luxembourg_landscape);
	        image.setLayoutParams(new LayoutParams(310, 175));
	        TextView name = new TextView(AllCitiesActivity.this);
	        name.setText("Luxembourg");
	        name.setPadding(10, 10, 0, 0);
	        name.setTypeface(null, Typeface.BOLD);
	        name.setTextColor(Color.parseColor("#45AEFF"));
	        TextView description = new TextView(AllCitiesActivity.this);
	        description.setText("sdda asd a a d asd a as d as das sad d a das das.");
	        LinearLayout verticalLayout = new LinearLayout(AllCitiesActivity.this);
	        verticalLayout.setOrientation(LinearLayout.VERTICAL);
	        if (i % 2 == 0) {
	        	verticalLayout.setPadding(0, 0, 36, 36);
	        } else {
	        	verticalLayout.setPadding(0, 0, 0, 36);
	        }
	        verticalLayout.addView(image);
	        verticalLayout.addView(name);
	        verticalLayout.addView(description);
	        horizontalLayout.addView(verticalLayout);
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
