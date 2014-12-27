package lu.uni.cityhunter.activities.adapters;

import java.util.ArrayList;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.activities.ChallengeActivity;
import lu.uni.cityhunter.persistence.Challenge;
import lu.uni.cityhunter.persistence.ChallengeState;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams") 
public class ChallengeListViewAdapter extends BaseAdapter{
	
	private boolean isLogging = false;
	private int descriptionStringLimit = 200;
	
	private ArrayList<Challenge> challenges;
	private LayoutInflater inflater = null;

	public ChallengeListViewAdapter (ArrayList<Challenge> challenges, LayoutInflater inflater){
		this.challenges = challenges;
		this.inflater = inflater;
	}
	
	@Override
	public int getCount() {
		return this.challenges.size();
	}

	@Override
	public Object getItem(int position) {
		return this.challenges.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		Challenge c = this.challenges.get(position);
		ChallengeState state = this.getChallengeState(c, parent);
		if(isLogging)
			Log.i("ChallengeListViewAdapter", "State for '"+c.getTitle()+"': "+state);
		if(state == ChallengeState.ACTIVE || state == ChallengeState.PLAYING
				|| state == ChallengeState.SUCCESS){
			v = inflater.inflate(R.layout.list_row, null);
		}else{ // if state == ChallengeState.LOST || state == ChallengeState.INACTIVE
			v = inflater.inflate(R.layout.list_row_inactive, null);
			if(state == ChallengeState.LOST){
				ImageView routeIcon = (ImageView) v.findViewById(R.id.routeButton);
				routeIcon.setVisibility(View.INVISIBLE);
			}
		}
		TextView title = (TextView) v.findViewById(R.id.listChallengeTitle);
		TextView description = (TextView) v.findViewById(R.id.listChallengeDescription);
		ImageView picture = (ImageView) v.findViewById(R.id.listChallengePicture);
		ImageView statusPicture = (ImageView) v.findViewById(R.id.listRightArrow);
		
		String descriptionStr = "";
		descriptionStr = c.getDescription();
		
		// Flag to check whether the description has been shortened or not
		// This is needed to know whether to add "..." or not.
		boolean hasBeenStripped = false;
		
		// If The description contains linebreaks, only display the text until the linebreak
		if(descriptionStr.contains("\n")){
			for(int i = 0; i < descriptionStr.length(); i++){
				if(descriptionStr.charAt(i) == '\n'){
					descriptionStr = descriptionStr.substring(0, i-1);
					hasBeenStripped = true;
					break;
				}
			}
		}
		
		// If the description (still) contains more than the given number of chars, strip it
		if(descriptionStr.length() > this.descriptionStringLimit){
			descriptionStr = descriptionStr.substring(0, this.descriptionStringLimit-1);
			hasBeenStripped = true;
		}
		
		// If the description has been shortened, add "..." at the end
		if(hasBeenStripped){
			descriptionStr = descriptionStr + " ...";
		}
		
		
		title.setText(c.getTitle());
		description.setText(descriptionStr);
		picture.setImageResource(c.getCoverPicture());
		
		// If challenge was successfully replace right arrow by check mark
		// If challenge was lost replace it by cross mark
		if(state == ChallengeState.SUCCESS){
			statusPicture.setImageResource(R.drawable.challenge_success_icon);
		}else if(state == ChallengeState.LOST){
			statusPicture.setImageResource(R.drawable.challenge_lost_icon);
		}
		
		return v;
	}
	
	private ChallengeState getChallengeState(Challenge challenge, ViewGroup parent){
		SharedPreferences sharedPreferences = null;
		if (challenge.getTitle().equals("G\u00eblle Fra")) {
			sharedPreferences =  parent.getContext().getSharedPreferences(ChallengeActivity.GELLE_FRA_PREFERENCES, Activity.MODE_PRIVATE);
		} else 
		if (challenge.getTitle().equals("Notre-Dame Cathedral")) {
			sharedPreferences = parent.getContext().getSharedPreferences(ChallengeActivity.CATHEDRAL_PREFERENCES, Activity.MODE_PRIVATE);
		} else 
		if (challenge.getTitle().equals("Grand Ducal Palace")) {
			sharedPreferences = parent.getContext().getSharedPreferences(ChallengeActivity.PALAIS_PREFERENCES, Activity.MODE_PRIVATE);
		} else
		if (challenge.getTitle().equals("Place Guillaume II")) {
			sharedPreferences = parent.getContext().getSharedPreferences(ChallengeActivity.PLACE_GUILLAUME_PREFERENCES, Activity.MODE_PRIVATE);
		} else
		if (challenge.getTitle().equals("Place de Clairefontaine")) {
			sharedPreferences = parent.getContext().getSharedPreferences(ChallengeActivity.PLACE_CLAIREFONTAINE_PREFERENCES, Activity.MODE_PRIVATE);
		}
		ChallengeState challengeState = ChallengeState.values()[sharedPreferences.getInt("challengeState", ChallengeState.INACTIVE.ordinal())];
		return challengeState;
	}
	
}
