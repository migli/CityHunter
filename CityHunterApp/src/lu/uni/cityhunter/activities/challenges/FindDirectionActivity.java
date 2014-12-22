package lu.uni.cityhunter.activities.challenges;

import java.util.Timer;
import java.util.TimerTask;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.activities.ChallengeActivity;
import lu.uni.cityhunter.persitence.CompassChallenge;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FindDirectionActivity extends ChallengeActivity implements SensorEventListener {
	
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
    private int tolerance = 10;
    private Timer timer = new Timer();
    private int currentTime = 0;
    private TimerTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
							displayCorrectAnswerDialog();
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
            if ((Math.round(azimuthInDegress) >= ((CompassChallenge)challenge).getDirection()-tolerance) &&
            	(Math.round(azimuthInDegress) <= ((CompassChallenge)challenge).getDirection()+tolerance)) {
            	if (task.scheduledExecutionTime() == 0) {
            		timer.schedule(task, 0, 1000);
            	}
            	azimuth.setTextColor(Color.GREEN);
            	if (currentTime < 10) {
            		azimuth.setText(String.valueOf(Math.round(azimuthInDegress))+"\u00b0"+" - 00:0"+currentTime);
            	} else {
            		azimuth.setText(String.valueOf(Math.round(azimuthInDegress))+"\u00b0"+" - 00:"+currentTime);
            	}
            } else {
            	if (currentTime > 0) {
            		nrOfTries++;
            		displayWrongAnswerDialog();
            	}
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
	
}
