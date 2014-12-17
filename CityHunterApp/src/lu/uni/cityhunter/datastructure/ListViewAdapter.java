package lu.uni.cityhunter.datastructure;

import java.util.ArrayList;

import lu.uni.cityhunter.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter{
	
	private ArrayList<Challenge> challenges;
	private LayoutInflater inflater = null;

	public ListViewAdapter (ArrayList<Challenge> challenges, LayoutInflater inflater){
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
		
		title.setText(c.getTitle());
		description.setText(c.getDescription());
		picture.setImageResource(c.getCoverPicture());
		return v;
	}
	
}
