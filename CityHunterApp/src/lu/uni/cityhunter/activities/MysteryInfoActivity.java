package lu.uni.cityhunter.activities;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.persitence.Mystery;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

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
	}
	
}
