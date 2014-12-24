package lu.uni.cityhunter.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lu.uni.cityhunter.persistence.Challenge;
import lu.uni.cityhunter.persistence.ChallengeState;
import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;


public class ReceiveTransitionsIntentService extends IntentService{
	
	private boolean isLogging = false;
	
	private ArrayList<Challenge> challenges;
	private SharedPreferences sharedPreferences;
	
	public ReceiveTransitionsIntentService(ArrayList<Challenge> challenges) {
		super("ReceiveTransitionsIntentService");
		this.challenges = challenges;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		GeofencingEvent event = GeofencingEvent.fromIntent(intent);
		if(event != null){
			if(event.hasError()){
				// Do nothing...
				if(isLogging)
					Log.e("ReceiveTransitionIntentService", "Error code: "+event.getErrorCode());
			}else{
				int transitionType = event.getGeofenceTransition();
				if(transitionType == Geofence.GEOFENCE_TRANSITION_ENTER){
					List <Geofence> triggerList = event.getTriggeringGeofences();
					String[] triggerIds = new String[triggerList.size()];

					for (int i = 0; i < triggerIds.length; i++) {
						String id = triggerList.get(i).getRequestId();
						Iterator<Challenge> iter = challenges.iterator();
						while(iter.hasNext()){
							Challenge c = iter.next();
							if(c.getTitle().equals(id)){
								ChallengeState state = this.getChallengeState(c);
								if(state != ChallengeState.LOST && state != ChallengeState.PLAYING && state != ChallengeState.SUCCESS){
									this.setChallengeState(ChallengeState.ACTIVE);
								}
							}
						}
					}
					MysteryMapActivity.updateListView();
					Toast.makeText(this, "Entering some geofences!", Toast.LENGTH_SHORT).show();
				}else if(transitionType == Geofence.GEOFENCE_TRANSITION_EXIT){
					List <Geofence> triggerList = event.getTriggeringGeofences();
					String[] triggerIds = new String[triggerList.size()];

					for (int i = 0; i < triggerIds.length; i++) {
						String id = triggerList.get(i).getRequestId();
						Iterator<Challenge> iter = challenges.iterator();
						while(iter.hasNext()){
							Challenge c = iter.next();
							if(c.getTitle().equals(id)){
								ChallengeState state = this.getChallengeState(c);
								if(state != ChallengeState.LOST && state != ChallengeState.PLAYING &&
										state != ChallengeState.SUCCESS){
									this.setChallengeState(ChallengeState.INACTIVE);
								}
							}
						}
					}
					MysteryMapActivity.updateListView();
					Toast.makeText(this, "Leaving some geofences!", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	private ChallengeState getChallengeState(Challenge challenge){
		if (challenge.getTitle().equals("G\u00eblle Fra")) {
			sharedPreferences =  getSharedPreferences(ChallengeActivity.GELLE_FRA_PREFERENCES, Activity.MODE_PRIVATE);
		} else 
		if (challenge.getTitle().equals("Notre-Dame Cathedral")) {
			sharedPreferences = getSharedPreferences(ChallengeActivity.CATHEDRAL_PREFERENCES, Activity.MODE_PRIVATE);
		} else 
		if (challenge.getTitle().equals("Grand Ducal Palace")) {
			sharedPreferences = getSharedPreferences(ChallengeActivity.PALAIS_PREFERENCES, Activity.MODE_PRIVATE);
		} else
		if (challenge.getTitle().equals("Place Guillaume II")) {
			sharedPreferences = getSharedPreferences(ChallengeActivity.PLACE_GUILLAUME_PREFERENCES, Activity.MODE_PRIVATE);
		} else
		if (challenge.getTitle().equals("Place de Clairefontaine")) {
			sharedPreferences = getSharedPreferences(ChallengeActivity.PLACE_CLAIREFONTAINE_PREFERENCES, Activity.MODE_PRIVATE);
		}
		ChallengeState challengeState = ChallengeState.values()[sharedPreferences.getInt("challengeState", ChallengeState.INACTIVE.ordinal())];
		return challengeState;
	}
	
	private void setChallengeState(ChallengeState challengeState){
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("challengeState", challengeState.ordinal());
		editor.commit();
	}
	
}
