package lu.uni.cityhunter.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lu.uni.cityhunter.datastructure.Challenge;
import lu.uni.cityhunter.datastructure.ChallengeState;
import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;


public class ReceiveTransitionsIntentService extends IntentService{
	
	private ArrayList<Challenge> challenges;

	public ReceiveTransitionsIntentService(ArrayList<Challenge> challenges) {
		super("ReceiveTransitionsIntentService");
		this.challenges = challenges;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if(LocationClient.hasError(intent)){
			// Do nothing...
			Toast.makeText(this, "Error in transition intent!", Toast.LENGTH_SHORT);
		}else{
			int transitionType = LocationClient.getGeofenceTransition(intent);
			if(transitionType == Geofence.GEOFENCE_TRANSITION_ENTER){
				List <Geofence> triggerList = LocationClient.getTriggeringGeofences(intent);
				String[] triggerIds = new String[triggerList.size()];

	            for (int i = 0; i < triggerIds.length; i++) {
	                String id = triggerList.get(i).getRequestId();
	                Iterator<Challenge> iter = challenges.iterator();
	                while(iter.hasNext()){
		            	Challenge c = iter.next();
		            	if(c.getTitle().equals(id)){
		            		ChallengeState state = c.getState();
		            		if(state != ChallengeState.LOST && state != ChallengeState.PLAYING &&
		            				state != ChallengeState.SUCCESS){
		            			c.setState(ChallengeState.ACTIVE);
		            		}
		            	}
		            }
	            }
	            MisteryMapActivity.updateListView();
	            Toast.makeText(this, "Entering some geofences!", Toast.LENGTH_SHORT).show();
			}else if(transitionType == Geofence.GEOFENCE_TRANSITION_EXIT){
				List <Geofence> triggerList = LocationClient.getTriggeringGeofences(intent);
				String[] triggerIds = new String[triggerList.size()];

	            for (int i = 0; i < triggerIds.length; i++) {
	                String id = triggerList.get(i).getRequestId();
	                Iterator<Challenge> iter = challenges.iterator();
	                while(iter.hasNext()){
		            	Challenge c = iter.next();
		            	if(c.getTitle().equals(id)){
		            		ChallengeState state = c.getState();
		            		if(state != ChallengeState.LOST && state != ChallengeState.PLAYING &&
		            				state != ChallengeState.SUCCESS){
		            			c.setState(null);
		            		}
		            	}
		            }
	            }
	            MisteryMapActivity.updateListView();
	            Toast.makeText(this, "Leaving some geofences!", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
}
