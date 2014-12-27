package lu.uni.cityhunter.activities.challenges;

import java.util.Random;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.activities.ChallengeActivity;
import lu.uni.cityhunter.persistence.QuestionChallenge;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ChoosePictureActivity extends ChallengeActivity {

	private ImageView imageView1;
	private TextView textView1;
	private ImageView imageView2;
	private TextView textView2;
	private ImageView imageView3;
	private TextView textView3;
	private ImageView imageView4;
	private TextView textView4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_picture);
		TextView description = (TextView) findViewById(R.id.challengeDescription);
		description.setText(challenge.getDescription());
		OnClickListener pictureClickListener = new OnClickListener() {
			public void onClick(View view) {
				if (((ImageView) view).getTag() == ((QuestionChallenge) challenge).getAnswer()) {
					displayCorrectAnswerDialog();
				} else {
					nrOfTries++;
					((ImageView) view).setEnabled(false);
					((View) ((ImageView) view).getParent()).setVisibility(View.INVISIBLE);
					displayWrongAnswerDialog();
				}
			}
		};
		imageView1 = (ImageView) findViewById(R.id.pictureImageView1);
		imageView1.setOnClickListener(pictureClickListener);
		textView1 = (TextView) findViewById(R.id.pictureTextView1);
		imageView2 = (ImageView) findViewById(R.id.pictureImageView2);
		imageView2.setOnClickListener(pictureClickListener);
		textView2 = (TextView) findViewById(R.id.pictureTextView2);
		imageView3 = (ImageView) findViewById(R.id.pictureImageView3);
		imageView3.setOnClickListener(pictureClickListener);
		textView3 = (TextView) findViewById(R.id.pictureTextView3);
		imageView4 = (ImageView) findViewById(R.id.pictureImageView4);
		imageView4.setOnClickListener(pictureClickListener);
		textView4 = (TextView) findViewById(R.id.pictureTextView4);
		if (sharedPreferences.contains("imageView_1_tag")) {
			imageView1.setImageResource(Integer.parseInt(sharedPreferences.getString("imageView_1_tag", "0"), 16)); imageView1.setTag(sharedPreferences.getString("imageView_1_tag", "0")); textView1.setText(sharedPreferences.getString("textView_1_text", ""));
			imageView1.setEnabled(sharedPreferences.getBoolean("imageView_1_enabled", true));
			if (!sharedPreferences.getBoolean("imageView_1_enabled", true)) {
				((View) imageView1.getParent()).setVisibility(View.INVISIBLE);
			}
			imageView2.setImageResource(Integer.parseInt(sharedPreferences.getString("imageView_2_tag", "0"), 16)); imageView2.setTag(sharedPreferences.getString("imageView_2_tag", "0")); textView2.setText(sharedPreferences.getString("textView_2_text", ""));  
			imageView2.setEnabled(sharedPreferences.getBoolean("imageView_2_enabled", true));
			if (!sharedPreferences.getBoolean("imageView_2_enabled", true)) {
				((View) imageView2.getParent()).setVisibility(View.INVISIBLE);
			}
			imageView3.setImageResource(Integer.parseInt(sharedPreferences.getString("imageView_3_tag", "0"), 16)); imageView3.setTag(sharedPreferences.getString("imageView_3_tag", "0")); textView3.setText(sharedPreferences.getString("textView_3_text", ""));
			imageView3.setEnabled(sharedPreferences.getBoolean("imageView_3_enabled", true));
			if (!sharedPreferences.getBoolean("imageView_3_enabled", true)) {
				((View) imageView3.getParent()).setVisibility(View.INVISIBLE);
			}
			imageView4.setImageResource(Integer.parseInt(sharedPreferences.getString("imageView_4_tag", "0"), 16)); imageView4.setTag(sharedPreferences.getString("imageView_4_tag", "0")); textView4.setText(sharedPreferences.getString("textView_4_text", ""));
			imageView4.setEnabled(sharedPreferences.getBoolean("imageView_4_enabled", true));
			if (!sharedPreferences.getBoolean("imageView_4_enabled", true)) {
				((View) imageView4.getParent()).setVisibility(View.INVISIBLE);
			}
		} else {
			Random rand = new Random();
			switch (rand.nextInt(4)) {
				case 0: imageView1.setImageResource(Integer.parseInt(((QuestionChallenge) challenge).getAnswer(), 16)); imageView1.setTag(((QuestionChallenge) challenge).getAnswer()); textView1.setText(((QuestionChallenge) challenge).getAnswerDescription());
						imageView2.setImageResource(Integer.parseInt(((QuestionChallenge) challenge).getPossibleAnswers()[0], 16)); imageView2.setTag(((QuestionChallenge) challenge).getPossibleAnswers()[0]); textView2.setText(((QuestionChallenge) challenge).getPossibleAnswerDescriptions()[0]);  
						imageView3.setImageResource(Integer.parseInt(((QuestionChallenge) challenge).getPossibleAnswers()[1], 16)); imageView3.setTag(((QuestionChallenge) challenge).getPossibleAnswers()[1]); textView3.setText(((QuestionChallenge) challenge).getPossibleAnswerDescriptions()[1]);
						imageView4.setImageResource(Integer.parseInt(((QuestionChallenge) challenge).getPossibleAnswers()[2], 16)); imageView4.setTag(((QuestionChallenge) challenge).getPossibleAnswers()[2]); textView4.setText(((QuestionChallenge) challenge).getPossibleAnswerDescriptions()[2]);
						break;
				case 1: imageView1.setImageResource(Integer.parseInt(((QuestionChallenge) challenge).getPossibleAnswers()[0], 16)); imageView1.setTag(((QuestionChallenge) challenge).getPossibleAnswers()[0]); textView1.setText(((QuestionChallenge) challenge).getPossibleAnswerDescriptions()[0]);
						imageView2.setImageResource(Integer.parseInt(((QuestionChallenge) challenge).getAnswer(), 16)); imageView2.setTag(((QuestionChallenge) challenge).getAnswer()); textView2.setText(((QuestionChallenge) challenge).getAnswerDescription());
						imageView3.setImageResource(Integer.parseInt(((QuestionChallenge) challenge).getPossibleAnswers()[1], 16)); imageView3.setTag(((QuestionChallenge) challenge).getPossibleAnswers()[1]); textView3.setText(((QuestionChallenge) challenge).getPossibleAnswerDescriptions()[1]);
						imageView4.setImageResource(Integer.parseInt(((QuestionChallenge) challenge).getPossibleAnswers()[2], 16)); imageView4.setTag(((QuestionChallenge) challenge).getPossibleAnswers()[2]); textView4.setText(((QuestionChallenge) challenge).getPossibleAnswerDescriptions()[2]);
						break;
				case 2: imageView1.setImageResource(Integer.parseInt(((QuestionChallenge) challenge).getPossibleAnswers()[0], 16)); imageView1.setTag(((QuestionChallenge) challenge).getPossibleAnswers()[0]); textView1.setText(((QuestionChallenge) challenge).getPossibleAnswerDescriptions()[0]);
						imageView2.setImageResource(Integer.parseInt(((QuestionChallenge) challenge).getPossibleAnswers()[1], 16)); imageView2.setTag(((QuestionChallenge) challenge).getPossibleAnswers()[1]); textView2.setText(((QuestionChallenge) challenge).getPossibleAnswerDescriptions()[1]);
						imageView3.setImageResource(Integer.parseInt(((QuestionChallenge) challenge).getAnswer(), 16)); imageView3.setTag(((QuestionChallenge) challenge).getAnswer()); textView3.setText(((QuestionChallenge) challenge).getAnswerDescription());
						imageView4.setImageResource(Integer.parseInt(((QuestionChallenge) challenge).getPossibleAnswers()[2], 16)); imageView4.setTag(((QuestionChallenge) challenge).getPossibleAnswers()[2]); textView4.setText(((QuestionChallenge) challenge).getPossibleAnswerDescriptions()[2]);
						break;
				case 3: imageView1.setImageResource(Integer.parseInt(((QuestionChallenge) challenge).getPossibleAnswers()[0], 16)); imageView1.setTag(((QuestionChallenge) challenge).getPossibleAnswers()[0]); textView1.setText(((QuestionChallenge) challenge).getPossibleAnswerDescriptions()[0]);
						imageView2.setImageResource(Integer.parseInt(((QuestionChallenge) challenge).getPossibleAnswers()[1], 16)); imageView2.setTag(((QuestionChallenge) challenge).getPossibleAnswers()[1]); textView2.setText(((QuestionChallenge) challenge).getPossibleAnswerDescriptions()[1]);
						imageView3.setImageResource(Integer.parseInt(((QuestionChallenge) challenge).getPossibleAnswers()[2], 16)); imageView3.setTag(((QuestionChallenge) challenge).getPossibleAnswers()[2]); textView3.setText(((QuestionChallenge) challenge).getPossibleAnswerDescriptions()[2]);
						imageView4.setImageResource(Integer.parseInt(((QuestionChallenge) challenge).getAnswer(), 16)); imageView4.setTag(((QuestionChallenge) challenge).getAnswer()); textView4.setText(((QuestionChallenge) challenge).getAnswerDescription());
						break;
				default: break;
			}
		}
	}
	
	@Override
	protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("imageView_1_tag", imageView1.getTag().toString());
        editor.putString("textView_1_text", textView1.getText().toString());
        editor.putBoolean("imageView_1_enabled", imageView1.isEnabled());
        editor.putString("imageView_2_tag", imageView2.getTag().toString());
        editor.putString("textView_2_text", textView2.getText().toString());
        editor.putBoolean("imageView_2_enabled", imageView2.isEnabled());
        editor.putString("imageView_3_tag", imageView3.getTag().toString());
        editor.putString("textView_3_text", textView3.getText().toString());
        editor.putBoolean("imageView_3_enabled", imageView3.isEnabled());
        editor.putString("imageView_4_tag", imageView4.getTag().toString());
        editor.putString("textView_4_text", textView4.getText().toString());
        editor.putBoolean("imageView_4_enabled", imageView4.isEnabled());
        editor.commit();
    }
	
}
