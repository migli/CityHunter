package lu.uni.cityhunter.activities;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.challenges.ChooseDate;
import lu.uni.cityhunter.challenges.ChoosePicture;
import lu.uni.cityhunter.challenges.FindDirection;
import lu.uni.cityhunter.challenges.GuessName;
import lu.uni.cityhunter.datastructure.Challenge;
import lu.uni.cityhunter.datastructure.ChallengeState;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChallengeActivity extends Activity implements SensorEventListener {

    private Challenge challenge;
	
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private boolean lastAccelerometerSet = false;
    private boolean lastMagnetometerSet = false;
    private float[] rotation = new float[9];
    private float[] orientation = new float[3];
    private float currentDegree = 0.f;
    private int tolerance = 5;
    private Timer timer = new Timer();
    private int currentTime = 0;
    private TimerTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		challenge = (Challenge) getIntent().getParcelableExtra(Challenge.CHALLENGE_PAR_KEY);  
		if (challenge.getState() == null) {
			challenge.setState(ChallengeState.PLAYING);
		}
		setTitle(challenge.getTitle());
		// ChooseDate challenge
		if (challenge instanceof ChooseDate) {
			setContentView(R.layout.choose_date);
			ImageView coverPicture = (ImageView) findViewById(R.id.challengeCoverPicture);
			coverPicture.setImageResource(challenge.getCoverPicture());
			TextView description = (TextView) findViewById(R.id.challengeDescription);
			description.setText(challenge.getDescription());
			OnClickListener dateClickListener = new OnClickListener() {
				public void onClick(View view) {
					if (Integer.parseInt((String) ((Button) view).getText()) == ((ChooseDate) challenge).getAnswer()) {
						displayCorrectAnswerDialog(challenge);
					} else {
						challenge.setNrOfTries(challenge.getNrOfTries()-1);
						((Button) view).setEnabled(false);
						((Button) view).setBackgroundColor(Color.LTGRAY);
						displayWrongAnswerDialog(challenge);
					}
				}
			};
			Button button1 = (Button) findViewById(R.id.dateButton1);
			button1.setOnClickListener(dateClickListener);
			Button button2 = (Button) findViewById(R.id.dateButton2);
			button2.setOnClickListener(dateClickListener);
			Button button3 = (Button) findViewById(R.id.dateButton3);
			button3.setOnClickListener(dateClickListener);
			Button button4 = (Button) findViewById(R.id.dateButton4);
			button4.setOnClickListener(dateClickListener);
			Random rand = new Random();
			switch (rand.nextInt(4)) {
				case 0: button1.setText(String.valueOf(((ChooseDate) challenge).getAnswer())); 
						button2.setText(String.valueOf(((ChooseDate) challenge).getDates()[0]));
						button3.setText(String.valueOf(((ChooseDate) challenge).getDates()[1]));
						button4.setText(String.valueOf(((ChooseDate) challenge).getDates()[2])); 
						break;
				case 1: button1.setText(String.valueOf(((ChooseDate) challenge).getDates()[0]));
						button2.setText(String.valueOf(((ChooseDate) challenge).getAnswer())); 
						button3.setText(String.valueOf(((ChooseDate) challenge).getDates()[1]));
						button4.setText(String.valueOf(((ChooseDate) challenge).getDates()[2])); 
						break;
				case 2: button1.setText(String.valueOf(((ChooseDate) challenge).getDates()[0]));
						button2.setText(String.valueOf(((ChooseDate) challenge).getDates()[1]));
						button3.setText(String.valueOf(((ChooseDate) challenge).getAnswer())); 
						button4.setText(String.valueOf(((ChooseDate) challenge).getDates()[2])); 
						break;
				case 3: button1.setText(String.valueOf(((ChooseDate) challenge).getDates()[0]));
						button2.setText(String.valueOf(((ChooseDate) challenge).getDates()[1]));
						button3.setText(String.valueOf(((ChooseDate) challenge).getDates()[2]));
						button4.setText(String.valueOf(((ChooseDate) challenge).getAnswer())); 
						break;
				default: break;
			}
		} else 
		// ChoosePicture challenge
		if (challenge instanceof ChoosePicture) {
			setContentView(R.layout.choose_picture);
			TextView description = (TextView) findViewById(R.id.challengeDescription);
			description.setText(challenge.getDescription());
			OnClickListener pictureClickListener = new OnClickListener() {
				public void onClick(View view) {
					if ((Integer) ((ImageView) view).getTag() == ((ChoosePicture) challenge).getAnswer()) {
						displayCorrectAnswerDialog(challenge);
					} else {
						challenge.setNrOfTries(challenge.getNrOfTries()-1);
						((ImageView) view).setEnabled(false);
						((View) ((ImageView) view).getParent()).setVisibility(View.INVISIBLE);
						displayWrongAnswerDialog(challenge);
					}
				}
			};
			ImageView imageView1 = (ImageView) findViewById(R.id.pictureImageView1);
			imageView1.setOnClickListener(pictureClickListener);
			TextView textView1 = (TextView) findViewById(R.id.pictureTextView1);
			ImageView imageView2 = (ImageView) findViewById(R.id.pictureImageView2);
			imageView2.setOnClickListener(pictureClickListener);
			TextView textView2 = (TextView) findViewById(R.id.pictureTextView2);
			ImageView imageView3 = (ImageView) findViewById(R.id.pictureImageView3);
			imageView3.setOnClickListener(pictureClickListener);
			TextView textView3 = (TextView) findViewById(R.id.pictureTextView3);
			ImageView imageView4 = (ImageView) findViewById(R.id.pictureImageView4);
			imageView4.setOnClickListener(pictureClickListener);
			TextView textView4 = (TextView) findViewById(R.id.pictureTextView4);
			Random rand = new Random();
			switch (rand.nextInt(4)) {
				case 0: imageView1.setImageResource(((ChoosePicture) challenge).getAnswer()); imageView1.setTag(((ChoosePicture) challenge).getAnswer()); textView1.setText(((ChoosePicture) challenge).getAnswerDescription());
						imageView2.setImageResource(((ChoosePicture) challenge).getPictures()[0]); imageView2.setTag(((ChoosePicture) challenge).getPictures()[0]); textView2.setText(((ChoosePicture) challenge).getPictureDescriptions()[0]);  
						imageView3.setImageResource(((ChoosePicture) challenge).getPictures()[1]); imageView3.setTag(((ChoosePicture) challenge).getPictures()[1]); textView3.setText(((ChoosePicture) challenge).getPictureDescriptions()[1]);
						imageView4.setImageResource(((ChoosePicture) challenge).getPictures()[2]); imageView4.setTag(((ChoosePicture) challenge).getPictures()[2]); textView4.setText(((ChoosePicture) challenge).getPictureDescriptions()[2]);
						break;
				case 1: imageView1.setImageResource(((ChoosePicture) challenge).getPictures()[0]); imageView1.setTag(((ChoosePicture) challenge).getPictures()[0]); textView1.setText(((ChoosePicture) challenge).getPictureDescriptions()[0]);
						imageView2.setImageResource(((ChoosePicture) challenge).getAnswer()); imageView2.setTag(((ChoosePicture) challenge).getAnswer()); textView2.setText(((ChoosePicture) challenge).getAnswerDescription());
						imageView3.setImageResource(((ChoosePicture) challenge).getPictures()[1]); imageView3.setTag(((ChoosePicture) challenge).getPictures()[1]); textView3.setText(((ChoosePicture) challenge).getPictureDescriptions()[1]);
						imageView4.setImageResource(((ChoosePicture) challenge).getPictures()[2]); imageView4.setTag(((ChoosePicture) challenge).getPictures()[2]); textView4.setText(((ChoosePicture) challenge).getPictureDescriptions()[2]);
						break;
				case 2: imageView1.setImageResource(((ChoosePicture) challenge).getPictures()[0]); imageView1.setTag(((ChoosePicture) challenge).getPictures()[0]); textView1.setText(((ChoosePicture) challenge).getPictureDescriptions()[0]);
						imageView2.setImageResource(((ChoosePicture) challenge).getPictures()[1]); imageView2.setTag(((ChoosePicture) challenge).getPictures()[1]); textView2.setText(((ChoosePicture) challenge).getPictureDescriptions()[1]);
						imageView3.setImageResource(((ChoosePicture) challenge).getAnswer()); imageView3.setTag(((ChoosePicture) challenge).getAnswer()); textView3.setText(((ChoosePicture) challenge).getAnswerDescription());
						imageView4.setImageResource(((ChoosePicture) challenge).getPictures()[2]); imageView4.setTag(((ChoosePicture) challenge).getPictures()[2]); textView4.setText(((ChoosePicture) challenge).getPictureDescriptions()[2]);
						break;
				case 3: imageView1.setImageResource(((ChoosePicture) challenge).getPictures()[0]); imageView1.setTag(((ChoosePicture) challenge).getPictures()[0]); textView1.setText(((ChoosePicture) challenge).getPictureDescriptions()[0]);
						imageView2.setImageResource(((ChoosePicture) challenge).getPictures()[1]); imageView2.setTag(((ChoosePicture) challenge).getPictures()[1]); textView2.setText(((ChoosePicture) challenge).getPictureDescriptions()[1]);
						imageView3.setImageResource(((ChoosePicture) challenge).getPictures()[2]); imageView3.setTag(((ChoosePicture) challenge).getPictures()[2]); textView3.setText(((ChoosePicture) challenge).getPictureDescriptions()[2]);
						imageView4.setImageResource(((ChoosePicture) challenge).getAnswer()); imageView4.setTag(((ChoosePicture) challenge).getAnswer()); textView4.setText(((ChoosePicture) challenge).getAnswerDescription());
						break;
				default: break;
			}
		} else 
		// GuessName challenge
		if (challenge instanceof GuessName) {
			setContentView(R.layout.guess_name);
			TextView description = (TextView) findViewById(R.id.challengeDescription);
			description.setText(challenge.getDescription());
			ImageView coverPicture = (ImageView) findViewById(R.id.challengeCoverPicture);
			coverPicture.setImageResource(challenge.getCoverPicture());
			EditText answer = (EditText) findViewById(R.id.answer);
			answer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
					if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) { 
						if (((EditText) view).getText().toString().equals(((GuessName) challenge).getName())) {
							displayCorrectAnswerDialog(challenge);
						} else {
							challenge.setNrOfTries(challenge.getNrOfTries()-1);
							((EditText) view).setText("");
							displayWrongAnswerDialog(challenge);
						}
					}
					return true;
				}
			});
		} else
		// FindDirection challenge
		if (challenge instanceof FindDirection) {
			setContentView(R.layout.find_direction);
			TextView description = (TextView) findViewById(R.id.challengeDescription);
			description.setText(challenge.getDescription());
			ImageView coverPicture = (ImageView) findViewById(R.id.challengeCoverPicture);
			coverPicture.setImageResource(challenge.getCoverPicture());
			task = new TimerTask() {
				public void run() {
					if (currentTime == 30) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								displayCorrectAnswerDialog(challenge);
							}
						});
					}
					currentTime++;
				}
			};
			sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
			// Accelerometer
			accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			if (accelerometer != null) {
				Toast.makeText(this, "ACCELEROMETER OK", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "ACCELEROMETER Sensor not found", Toast.LENGTH_LONG).show();
			}
		    // Magnetometer
			magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		    if (magnetometer != null) {
				Toast.makeText(this, "MAGNETIC FIELD OK", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "MAGNETIC FIELD Sensor not found", Toast.LENGTH_LONG).show();
			}
		} else {
			setContentView(R.layout.activity_challenge);
		}
	}
	
	protected void onResume() {
	    super.onResume();
	    sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
	    sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
	}
	 
	protected void onPause() {
	    super.onPause();
	    sensorManager.unregisterListener(this, accelerometer);
        sensorManager.unregisterListener(this, magnetometer);
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		TextView azimuth = (TextView) findViewById(R.id.azimuth);
        if (event.sensor == accelerometer) {
            System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.length);
            lastAccelerometerSet = true;
        } else if (event.sensor == magnetometer) {
            System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.length);
            lastMagnetometerSet = true;
        }
        if (lastAccelerometerSet && lastMagnetometerSet) {
            SensorManager.getRotationMatrix(rotation, null, lastAccelerometer, lastMagnetometer);
            SensorManager.getOrientation(rotation, orientation);
            float azimuthInRadians = orientation[0];
            float azimuthInDegress = (float)(Math.round(Math.toDegrees(azimuthInRadians)+360))%360;
            RotateAnimation ra = new RotateAnimation(currentDegree, -azimuthInDegress, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            ra.setDuration(250);
            ra.setFillAfter(true);
            ImageView compass = (ImageView) findViewById(R.id.compass);
            compass.startAnimation(ra);
            currentDegree = -azimuthInDegress;
            if ((Math.round(azimuthInDegress) >= ((FindDirection)challenge).getDirection()-tolerance) &&
            	(Math.round(azimuthInDegress) <= ((FindDirection)challenge).getDirection()+tolerance)) {
            	if (task.scheduledExecutionTime() == 0) {
            		timer.schedule(task, 0, 1000);
            	}
            	azimuth.setTextColor(Color.GREEN);
            	azimuth.setText(String.valueOf(Math.round(azimuthInDegress))+"\u00b0"+" - 0:"+currentTime);
            } else {
            	currentTime = 0;
            	azimuth.setTextColor(Color.BLACK);
            	azimuth.setText(String.valueOf(Math.round(azimuthInDegress))+"\u00b0");
            }
        }
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.challenge, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.action_settings: 
				startActivity(new Intent(ChallengeActivity.this, SettingsActivity.class));
				return true;
			case R.id.action_about: 
				startActivity(new Intent(ChallengeActivity.this, AboutActivity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void displayCorrectAnswerDialog(final Challenge challenge) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ChallengeActivity.this);
		builder.setMessage("Congrats!\nYou picked the correct answer! :)").setTitle("You Won");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				challenge.setState(ChallengeState.SUCCESS);
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void displayWrongAnswerDialog(final Challenge challenge) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ChallengeActivity.this);
		if (challenge.getNrOfTries() == 0) {
			builder.setMessage("You have no tries anymore!\nNo hint for you! :(").setTitle("You Lost");
		} else
		if (challenge.getNrOfTries() == 1) {
			builder.setMessage("You picked the wrong answer!\nYou still have one last try!").setTitle("Wrong Answer");
		} else {
			builder.setMessage("You picked the wrong answer!\nYou still have " + challenge.getNrOfTries() + " more tries!").setTitle("Wrong Answer");
		}
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				if (challenge.getNrOfTries() == 0) {
					challenge.setState(ChallengeState.LOST);
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
}
