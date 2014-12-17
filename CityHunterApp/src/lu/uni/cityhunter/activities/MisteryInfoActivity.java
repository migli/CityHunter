package lu.uni.cityhunter.activities;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.datastructure.Mistery;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MisteryInfoActivity extends Activity{

	private Mistery mistery;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mistery_info);
		mistery = (Mistery) getIntent().getParcelableExtra(Mistery.MISTERY_PAR_KEY);
		
		TextView title = (TextView) findViewById(R.id.misteryInfo_title);
		title.setText(mistery.getTitle());
		
		TextView description = (TextView) findViewById(R.id.misteryInfo_description);
		description.setText(mistery.getQuestion());
	}
	
}
