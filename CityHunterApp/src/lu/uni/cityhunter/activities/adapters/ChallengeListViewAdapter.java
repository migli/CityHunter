package lu.uni.cityhunter.activities.adapters;

import java.util.ArrayList;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.persistence.Challenge;
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
		if(v == null){
			if(false){ // location in radius or playing
				v = inflater.inflate(R.layout.list_row, null);
			}else{
				v = inflater.inflate(R.layout.list_row_inactive, null);
			}
		}
		TextView title = (TextView) v.findViewById(R.id.listChallengeTitle);
		TextView description = (TextView) v.findViewById(R.id.listChallengeDescription);
		ImageView picture = (ImageView) v.findViewById(R.id.listChallengePicture);
		
		String descriptionStr = c.getDescription();
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
		return v;
	}
	
}
