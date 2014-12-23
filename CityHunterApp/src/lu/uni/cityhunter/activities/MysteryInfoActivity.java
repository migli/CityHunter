package lu.uni.cityhunter.activities;

import java.util.ArrayList;
import java.util.Iterator;

import lu.uni.cityhunter.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MysteryInfoActivity extends Activity{
	
	private Mystery mistery;
	
	private ArrayAdapter<String> hintsListViewAdapter;
	private ListView hintsListView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mistery_info);
		mistery = (Mystery) getIntent().getParcelableExtra(Mistery.MISTERY_PAR_KEY);
		
		TextView title = (TextView) findViewById(R.id.mysteryInfo_title);
		title.setText(mistery.getTitle());
		
		TextView description = (TextView) findViewById(R.id.mysteryInfo_description);
		description.setText(mistery.getQuestion());
		
		hintsListView = (ListView) findViewById(R.id.mysteryInfo_HintsListView);
		ArrayList<String> hintsArray = this.fetchHints();
		hintsListViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hintsArray);
		hintsListView.setAdapter(hintsListViewAdapter);
		
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
	
	private ArrayList<String> fetchHints(){
		ArrayList<String> hintsArray = new ArrayList<String>();
		Iterator<Challenge> iter = mistery.getChallenges().iterator();
		while(iter.hasNext()){
			Challenge c = iter.next();
			if(c.getState() == ChallengeState.SUCCESS){
				hintsArray.add(c.getHint());
			}
		}
		if(hintsArray.isEmpty()){
			hintsArray.add("No hints available!\nSolve some challenges to gather new hints!");
		}
		
		return hintsArray;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		hintsListViewAdapter.clear();
		hintsListViewAdapter.addAll(this.fetchHints());
		hintsListViewAdapter.notifyDataSetChanged();
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
	
}
