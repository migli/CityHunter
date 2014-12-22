package lu.uni.cityhunter.activities;

import lu.uni.cityhunter.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		Button resetButton = (Button) findViewById(R.id.resetButton);
		resetButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				// Remove Gelle Fra challenge settings
				sharedPreferences = getSharedPreferences(ChallengeActivity.GELLE_FRA_PREFERENCES, MODE_PRIVATE);
				editor = sharedPreferences.edit();
				editor.remove("challengeState");
				editor.remove("nrOfTries");
				editor.remove("button_1_text");
				editor.remove("button_1_enabled");
				editor.remove("button_2_text");
				editor.remove("button_2_enabled");
				editor.remove("button_3_text");
				editor.remove("button_3_enabled");
				editor.remove("button_4_text");
				editor.remove("button_4_enabled");
				editor.commit();
				// Remove Cathedral challenge settings
				sharedPreferences = getSharedPreferences(ChallengeActivity.CATHEDRAL_PREFERENCES, MODE_PRIVATE);
				editor = sharedPreferences.edit();
				editor.remove("challengeState");
				editor.remove("nrOfTries");
				editor.remove("imageView_1_tag");
		        editor.remove("textView_1_text");
		        editor.remove("imageView_1_enabled");
		        editor.remove("imageView_2_tag");
		        editor.remove("textView_2_text");
		        editor.remove("imageView_2_enabled");
		        editor.remove("imageView_3_tag");
		        editor.remove("textView_3_text");
		        editor.remove("imageView_3_enabled");
		        editor.remove("imageView_4_tag");
		        editor.remove("textView_4_text");
		        editor.remove("imageView_4_enabled");
				editor.commit();
				// Remove Palais challenge settings
				sharedPreferences = getSharedPreferences(ChallengeActivity.PALAIS_PREFERENCES, MODE_PRIVATE);
				editor = sharedPreferences.edit();
				editor.remove("challengeState");
				editor.remove("nrOfTries");
				editor.remove("imageView_1_tag");
		        editor.remove("textView_1_text");
		        editor.remove("imageView_1_enabled");
		        editor.remove("imageView_2_tag");
		        editor.remove("textView_2_text");
		        editor.remove("imageView_2_enabled");
		        editor.remove("imageView_3_tag");
		        editor.remove("textView_3_text");
		        editor.remove("imageView_3_enabled");
		        editor.remove("imageView_4_tag");
		        editor.remove("textView_4_text");
		        editor.remove("imageView_4_enabled");
				editor.commit();
				// Remove Place Guillaume challenge settings
				sharedPreferences = getSharedPreferences(ChallengeActivity.PLACE_GUILLAUME_PREFERENCES, MODE_PRIVATE);
				editor = sharedPreferences.edit();
				editor.remove("challengeState");
				editor.remove("nrOfTries");
				editor.remove("editText_1_text");
		        editor.commit();
		        // Remove Place Clairefontaine challenge settings
		        sharedPreferences = getSharedPreferences(ChallengeActivity.PLACE_CLAIREFONTAINE_PREFERENCES, MODE_PRIVATE);
				editor = sharedPreferences.edit();
				editor.remove("challengeState");
				editor.remove("nrOfTries");
		        editor.commit();
		        // Delete score
		        sharedPreferences = getSharedPreferences(Home.SCORE_PREFERENCES, MODE_PRIVATE);
				editor = sharedPreferences.edit();
				editor.remove("score");
		        editor.commit();
		        // Notify user that the resetting is done
		        Toast.makeText(SettingsActivity.this, "RESETTING DONE", Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
}
