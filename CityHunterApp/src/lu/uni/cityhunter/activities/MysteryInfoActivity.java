package lu.uni.cityhunter.activities;

import java.util.Iterator;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.persistence.Challenge;
import lu.uni.cityhunter.persistence.ChallengeState;
import lu.uni.cityhunter.persistence.Mystery;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MysteryInfoActivity extends Activity{
	
	private Mystery mystery;
	
	
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
		
		final EditText answer = (EditText) findViewById(R.id.mysteryInfo_solveMysteryInput);
		answer.setOnEditorActionListener(new OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if (actionId == EditorInfo.IME_ACTION_DONE) {
		            // Check if answer right
		        	String answerStr = answer.getText().toString();
		        	Toast.makeText(getParent(), "Your answer is: '"+answerStr+"'", Toast.LENGTH_SHORT).show();
		        	
		        	// Hide Software Keyboard
		        	EditText myEditText = (EditText) findViewById(R.id.mysteryInfo_solveMysteryInput);  
		        	InputMethodManager imm = (InputMethodManager)getSystemService(
		        	      Context.INPUT_METHOD_SERVICE);
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
		// Hide Software keyboard
		EditText myEditText = (EditText) findViewById(R.id.mysteryInfo_solveMysteryInput);  
    	InputMethodManager imm = (InputMethodManager)getSystemService(
    	      Context.INPUT_METHOD_SERVICE);
    	imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
		super.onPause();
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
	
}
