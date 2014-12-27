package lu.uni.cityhunter.activities.challenges;

import java.util.Random;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.activities.ChallengeActivity;
import lu.uni.cityhunter.persistence.QuestionChallenge;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChooseDateActivity extends ChallengeActivity {

	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_date);
		ImageView coverPicture = (ImageView) findViewById(R.id.challengeCoverPicture);
		coverPicture.setImageResource(challenge.getCoverPicture());
		TextView description = (TextView) findViewById(R.id.challengeDescription);
		description.setText(challenge.getDescription());
		OnClickListener dateClickListener = new OnClickListener() {
			public void onClick(View view) {
				if (((Button) view).getText().toString().equals(((QuestionChallenge) challenge).getAnswer())) {
					displayCorrectAnswerDialog();
				} else {
					nrOfTries++;
					((Button) view).setEnabled(false);
					((Button) view).setBackgroundColor(Color.LTGRAY);
					displayWrongAnswerDialog();
				}
			}
		};
		button1 = (Button) findViewById(R.id.dateButton1);
		button1.setOnClickListener(dateClickListener);
		button2 = (Button) findViewById(R.id.dateButton2);
		button2.setOnClickListener(dateClickListener);
		button3 = (Button) findViewById(R.id.dateButton3);
		button3.setOnClickListener(dateClickListener);
		button4 = (Button) findViewById(R.id.dateButton4);
		button4.setOnClickListener(dateClickListener);
		if (sharedPreferences.contains("button_1_text")) {
			button1.setText(sharedPreferences.getString("button_1_text", ""));
			button1.setEnabled(sharedPreferences.getBoolean("button_1_enabled", true));
			if (!sharedPreferences.getBoolean("button_1_enabled", true)) {
				button1.setBackgroundColor(Color.LTGRAY);
			}
			button2.setText(sharedPreferences.getString("button_2_text", ""));
			button2.setEnabled(sharedPreferences.getBoolean("button_2_enabled", true));
			if (!sharedPreferences.getBoolean("button_2_enabled", true)) {
				button2.setBackgroundColor(Color.LTGRAY);
			}
			button3.setText(sharedPreferences.getString("button_3_text", ""));
			button3.setEnabled(sharedPreferences.getBoolean("button_3_enabled", true));
			if (!sharedPreferences.getBoolean("button_3_enabled", true)) {
				button3.setBackgroundColor(Color.LTGRAY);
			}
			button4.setText(sharedPreferences.getString("button_4_text", ""));
			button4.setEnabled(sharedPreferences.getBoolean("button_4_enabled", true));
			if (!sharedPreferences.getBoolean("button_4_enabled", true)) {
				button4.setBackgroundColor(Color.LTGRAY);
			}
		} else {
			Random rand = new Random();
			switch (rand.nextInt(4)) {
				case 0: button1.setText(((QuestionChallenge) challenge).getAnswer()); 
						button2.setText(((QuestionChallenge) challenge).getPossibleAnswers()[0]);
						button3.setText(((QuestionChallenge) challenge).getPossibleAnswers()[1]);
						button4.setText(((QuestionChallenge) challenge).getPossibleAnswers()[2]); 
						break;
				case 1: button1.setText(((QuestionChallenge) challenge).getPossibleAnswers()[0]);
						button2.setText(((QuestionChallenge) challenge).getAnswer()); 
						button3.setText(((QuestionChallenge) challenge).getPossibleAnswers()[1]);
						button4.setText(((QuestionChallenge) challenge).getPossibleAnswers()[2]); 
						break;
				case 2: button1.setText(((QuestionChallenge) challenge).getPossibleAnswers()[0]);
						button2.setText(((QuestionChallenge) challenge).getPossibleAnswers()[1]);
						button3.setText(((QuestionChallenge) challenge).getAnswer()); 
						button4.setText(((QuestionChallenge) challenge).getPossibleAnswers()[2]); 
						break;
				case 3: button1.setText(((QuestionChallenge) challenge).getPossibleAnswers()[0]);
						button2.setText(((QuestionChallenge) challenge).getPossibleAnswers()[1]);
						button3.setText(((QuestionChallenge) challenge).getPossibleAnswers()[2]);
						button4.setText(((QuestionChallenge) challenge).getAnswer()); 
						break;
				default: break;
			}
		}
	}
	
	@Override
	protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("button_1_text", button1.getText().toString());
        editor.putBoolean("button_1_enabled", button1.isEnabled());
        editor.putString("button_2_text", button2.getText().toString());
        editor.putBoolean("button_2_enabled", button2.isEnabled());
        editor.putString("button_3_text", button3.getText().toString());
        editor.putBoolean("button_3_enabled", button3.isEnabled());
        editor.putString("button_4_text", button4.getText().toString());
        editor.putBoolean("button_4_enabled", button4.isEnabled());
        editor.commit();
    }
	
}
