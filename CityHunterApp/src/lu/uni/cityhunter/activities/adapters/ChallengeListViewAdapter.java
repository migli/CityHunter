package lu.uni.cityhunter.activities.adapters;

import java.util.ArrayList;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.persistence.Challenge;
import lu.uni.cityhunter.persistence.ChallengeState;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChallengeListViewAdapter extends BaseAdapter{
	
	private int descriptionStringLimit;
	
	private ArrayList<Challenge> challenges;
	private LayoutInflater inflater = null;

	public ChallengeListViewAdapter (ArrayList<Challenge> challenges, LayoutInflater inflater){
		this.challenges = challenges;
		this.inflater = inflater;
		this.descriptionStringLimit = 200;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		Challenge c = this.challenges.get(position);
		ChallengeState state = c.getState();
//		Log.e("UNI.LU", "State for '"+c.getTitle()+"': "+c.getState());
		if(state == ChallengeState.ACTIVE || state == ChallengeState.PLAYING
				|| state == ChallengeState.SUCCESS){
			v = inflater.inflate(R.layout.list_row, null);
		}else{ // if state == ChallengeState.LOST || state == null
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
		// If the Challenge was already done successfully => display the hint
		// instead of the description!
		if(state == ChallengeState.SUCCESS){
			descriptionStr = c.getHint();
		}else{
			descriptionStr = c.getDescription();
		}
		
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
	
}
