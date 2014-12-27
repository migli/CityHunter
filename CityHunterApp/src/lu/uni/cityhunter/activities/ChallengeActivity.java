package lu.uni.cityhunter.activities;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.persistence.Challenge;
import lu.uni.cityhunter.persistence.ChallengeState;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ChallengeActivity extends Activity {

    protected Challenge challenge;
    protected ChallengeState challengeState;
    protected int nrOfTries;
    protected int score;
	protected SharedPreferences sharedPreferences;
	protected SharedPreferences scoreSharedPreferences;
	
	public static final String GELLE_FRA_PREFERENCES = "uni.lu.cityhunter.gelle_fra";
	public static final String CATHEDRAL_PREFERENCES = "uni.lu.cityhunter.cathedral";
	public static final String PALAIS_PREFERENCES = "uni.lu.cityhunter.palais";
	public static final String PLACE_GUILLAUME_PREFERENCES = "uni.lu.cityhunter.place_guillaume";
	public static final String PLACE_CLAIREFONTAINE_PREFERENCES = "uni.lu.cityhunter.place_clairefontaine";
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challenge);
		challenge = (Challenge) getIntent().getParcelableExtra(Challenge.CHALLENGE_PAR_KEY);  
		setTitle(challenge.getTitle());
		if (challenge.getTitle().equals("G\u00eblle Fra")) {
			sharedPreferences = getSharedPreferences(GELLE_FRA_PREFERENCES, MODE_PRIVATE);
		} else 
		if (challenge.getTitle().equals("Notre-Dame Cathedral")) {
			sharedPreferences = getSharedPreferences(CATHEDRAL_PREFERENCES, MODE_PRIVATE);
		} else 
		if (challenge.getTitle().equals("Grand Ducal Palace")) {
			sharedPreferences = getSharedPreferences(PALAIS_PREFERENCES, MODE_PRIVATE);
		} else
		if (challenge.getTitle().equals("Place Guillaume II")) {
			sharedPreferences = getSharedPreferences(PLACE_GUILLAUME_PREFERENCES, MODE_PRIVATE);
		} else
		if (challenge.getTitle().equals("Place de Clairefontaine")) {
			sharedPreferences = getSharedPreferences(PLACE_CLAIREFONTAINE_PREFERENCES, MODE_PRIVATE);
		}
		challengeState = ChallengeState.values()[sharedPreferences.getInt("challengeState", ChallengeState.PLAYING.ordinal())];
		nrOfTries = sharedPreferences.getInt("nrOfTries", 0);
		scoreSharedPreferences = getSharedPreferences(Home.SCORE_PREFERENCES, MODE_PRIVATE);
		score = scoreSharedPreferences.getInt("score", 0);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
	public void displayCorrectAnswerDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ChallengeActivity.this);
		score += challenge.getMaxNrOfTries() * 100 - nrOfTries * 100;
		builder.setMessage("Congrats you picked the right answer! :)").setTitle("You Won");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				challengeState = ChallengeState.SUCCESS;
				finish();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public void displayWrongAnswerDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ChallengeActivity.this);
		if (nrOfTries == challenge.getMaxNrOfTries()) {
			builder.setMessage("You have no tries anymore! :(").setTitle("You Lost");
		} else
		if (nrOfTries == challenge.getMaxNrOfTries() - 1) {
			builder.setMessage("You picked the wrong answer!\nYou still have one last try!").setTitle("Wrong Answer");
		} else {
			builder.setMessage("You picked the wrong answer!\nYou still have " + (challenge.getMaxNrOfTries() - nrOfTries) + " more tries!").setTitle("Wrong Answer");
		}
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				if (nrOfTries == challenge.getMaxNrOfTries()) {
					challengeState = ChallengeState.LOST;
					finish();
				}
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("challengeState", challengeState.ordinal());
		editor.putInt("nrOfTries", nrOfTries);
		editor.commit();
		editor = scoreSharedPreferences.edit();
		editor.putInt("score", score);
		editor.commit();
		MysteryMapActivity.updateListView();
	}
	
}
