package lu.uni.cityhunter.activities;

import java.util.Iterator;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.persistence.Challenge;
import lu.uni.cityhunter.persistence.ChallengeState;
import lu.uni.cityhunter.persistence.Mystery;
import lu.uni.cityhunter.persistence.MysteryState;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MysteryInfoActivity extends Activity {
	
	private Mystery mystery;
	private MysteryState mysteryState;
	private int nrOfTries;
	private int score;
	private SharedPreferences sharedPreferences;
	private SharedPreferences scoreSharedPreferences;
	
	public static final String MYSTERY_1_PREFERENCES = "uni.lu.cityhunter.mystery_1";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mystery_info);
		mystery = (Mystery) getIntent().getParcelableExtra(Mystery.MYSTERY_PAR_KEY);
		
		TextView title = (TextView) findViewById(R.id.mysteryInfo_title);
		title.setText(mystery.getTitle());
		
		TextView description = (TextView) findViewById(R.id.mysteryInfo_description);
		description.setText(mystery.getQuestion());
		
		displayHint();
		
		sharedPreferences = getSharedPreferences(MYSTERY_1_PREFERENCES, MODE_PRIVATE);
		mysteryState = MysteryState.values()[sharedPreferences.getInt("mysteryState", MysteryState.PLAYING.ordinal())];
		nrOfTries = sharedPreferences.getInt("nrOfTries", 0);
		scoreSharedPreferences = getSharedPreferences(Home.SCORE_PREFERENCES, MODE_PRIVATE);
		score = scoreSharedPreferences.getInt("score", 0);
		
		final EditText answer = (EditText) findViewById(R.id.mysteryInfo_solveMysteryInput);
		answer.setOnEditorActionListener(new OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		    	if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
			        // Check if answer is right or wrong
		    		if (answer.getText().toString().equals(mystery.getAnswer())) {
		    			mysteryState = MysteryState.SUCCESS;
		    			score += 500 - nrOfTries * 100;
		    			Toast.makeText(getParent(), "Your answer is right!", Toast.LENGTH_SHORT).show();	
		    			finish();
		    		} else {
		    			if (nrOfTries < 4) {
		    				nrOfTries++;
		    			}
		    			Toast.makeText(getParent(), "Your answer is wrong!", Toast.LENGTH_SHORT).show();	
		    		}
		    		// Hide Software Keyboard
		        	EditText myEditText = (EditText) findViewById(R.id.mysteryInfo_solveMysteryInput);  
		        	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		        	imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
		        }
		        return true;
		    }
		});
		
	}
	
	private void displayHint(){
		boolean displayHint = true;
		Iterator<Challenge> iter = mystery.getChallenges().iterator();
		while(iter.hasNext()){
			Challenge c = iter.next();
			if(this.getChallengeState(c) != ChallengeState.SUCCESS){
				displayHint = false;
				break;
			}
		}
		
		TextView hint = (TextView) findViewById(R.id.mysteryInfo_Hint);
		if(displayHint){
			hint.setText(mystery.getHint());
		}else{
			hint.setText("You did not solve all challenges! No hint for you!");
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		displayHint();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("mysteryState", mysteryState.ordinal());
		editor.putInt("nrOfTries", nrOfTries);
		editor.commit();
		editor = scoreSharedPreferences.edit();
		editor.putInt("score", score);
		editor.commit();
		// Hide Software keyboard
		EditText myEditText = (EditText) findViewById(R.id.mysteryInfo_solveMysteryInput);  
    	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    	imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);		
	}
	
	private ChallengeState getChallengeState(Challenge challenge){
		SharedPreferences sharedPreferences = null;
		if (challenge.getTitle().equals("G\u00eblle Fra")) {
			sharedPreferences =  getSharedPreferences(ChallengeActivity.GELLE_FRA_PREFERENCES, Activity.MODE_PRIVATE);
		} else 
		if (challenge.getTitle().equals("Notre-Dame Cathedral")) {
			sharedPreferences = getSharedPreferences(ChallengeActivity.CATHEDRAL_PREFERENCES, Activity.MODE_PRIVATE);
		} else 
		if (challenge.getTitle().equals("Grand Ducal Palace")) {
			sharedPreferences = getSharedPreferences(ChallengeActivity.PALAIS_PREFERENCES, Activity.MODE_PRIVATE);
		} else
		if (challenge.getTitle().equals("Place Guillaume II")) {
			sharedPreferences = getSharedPreferences(ChallengeActivity.PLACE_GUILLAUME_PREFERENCES, Activity.MODE_PRIVATE);
		} else
		if (challenge.getTitle().equals("Place de Clairefontaine")) {
			sharedPreferences = getSharedPreferences(ChallengeActivity.PLACE_CLAIREFONTAINE_PREFERENCES, Activity.MODE_PRIVATE);
		}
		ChallengeState challengeState = ChallengeState.values()[sharedPreferences.getInt("challengeState", ChallengeState.PLAYING.ordinal())];
		return challengeState;
	}
	
	public void displayCorrectAnswerDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Congrats you solved the mystery!").setTitle("You Won");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				finish();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public void displayWrongAnswerDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("You entered the wrong answer to the mystery!\nTry again!").setTitle("Wrong Answer");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
					// do nothing
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	
}
