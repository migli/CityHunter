package lu.uni.cityhunter.activities.challenges;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.activities.ChallengeActivity;
import lu.uni.cityhunter.persistence.QuestionChallenge;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class GuessNameActivity extends ChallengeActivity {

	private EditText editText1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guess_name);
		TextView description = (TextView) findViewById(R.id.challengeDescription);
		description.setText(challenge.getDescription());
		ImageView coverPicture = (ImageView) findViewById(R.id.challengeCoverPicture);
		coverPicture.setImageResource(challenge.getCoverPicture());
		editText1 = (EditText) findViewById(R.id.answer);
		editText1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
		            if (((EditText) view).getText().toString().equals(((QuestionChallenge) challenge).getAnswer())) {
						displayCorrectAnswerDialog();
					} else {
						nrOfTries++;
						((EditText) view).setText("");
						displayWrongAnswerDialog();
					}
				}
				return true;
			}
		});
		if (sharedPreferences.contains("editText_1_text")) {
			editText1.setText(sharedPreferences.getString("editText_1_text", ""));
		} else {
			editText1.setText("");
		}
	}
	
	@Override
	protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("editText_1_text", editText1.getText().toString());
        editor.commit();
    }
	
}
