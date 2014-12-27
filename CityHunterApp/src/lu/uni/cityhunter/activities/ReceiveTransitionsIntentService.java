package lu.uni.cityhunter.activities;

import java.util.ArrayList;
import java.util.Iterator;

import lu.uni.cityhunter.persistence.Challenge;
import lu.uni.cityhunter.persistence.ChallengeState;
import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;


public class ReceiveTransitionsIntentService extends IntentService{
	
	private final boolean isLogging = false;
	
	private SharedPreferences sharedPreferences;
	
	public ReceiveTransitionsIntentService() {
		super("ReceiveTransitionsIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ArrayList<Challenge> challenges = intent.getParcelableArrayListExtra(Challenge.CHALLENGE_PAR_KEY);
		Challenge challenge = null;
		
		// Get the challenge whose Geofence was triggered:
		Iterator<Challenge> iter = challenges.iterator();
		while(iter.hasNext()){
			Challenge c = iter.next();
			if(intent.hasCategory(c.getTitle())){
				challenge = c;
				break;
			}
		}
		
		if(challenge != null){
			if(isLogging)
				Log.i("ReceiveTransitionsIntentService", "Geofence of '"+challenge.getTitle()+"' was triggered!");
			// Check if we need to change the state of the challenge
			// If we need to => toggle between Active and Inactive!
			ChallengeState state = this.getChallengeState(challenge);
			if(state == ChallengeState.INACTIVE){
				// If state is inactive => Switch it to active
				this.setChallengeState(ChallengeState.ACTIVE);
			}else if(state == ChallengeState.ACTIVE){
				// If state is active => Switch to inactive
				this.setChallengeState(ChallengeState.INACTIVE);
			}
			MysteryMapActivity.updateListView();
		}else{
			if(isLogging)
				Log.e("ReceiveTransitionsIntentService", "Geofence of unknown Challenge was triggered!");
		}
		
		// For some reason, this GeofencingEvent.fromIntent(intent) does only return a generic GeofencingEvent
		// i.e.: hasError() => false , getGeofenceTransition() => -1 , getTriggeringGeofences() => empty!
//		GeofencingEvent event = GeofencingEvent.fromIntent(intent);
//		if(event != null){
//			if(event.hasError()){
//				// Do nothing...
//				if(isLogging)
//					Log.e("ReceiveTransitionIntentService", "Error code: "+event.getErrorCode());
//			}else{
//				int transitionType = event.getGeofenceTransition();
//				
//				if(transitionType == Geofence.GEOFENCE_TRANSITION_ENTER){
//					List <Geofence> triggerList = event.getTriggeringGeofences();
//
//					for (int i = 0; i < triggerList.size(); i++) {
//						String id = triggerList.get(i).getRequestId();
//						Iterator<Challenge> iter = challenges.iterator();
//						while(iter.hasNext()){
//							Challenge c = iter.next();
//							if(c.getTitle().equals(id)){
//								ChallengeState state = this.getChallengeState(c);
//								if(state != ChallengeState.LOST && state != ChallengeState.PLAYING && state != ChallengeState.SUCCESS){
//									this.setChallengeState(ChallengeState.ACTIVE);
//								}
//							}
//						}
//					}
//					MysteryMapActivity.updateListView();
//					if(isLogging)
//						Log.i("ReceiveTransitionsIntentService", "Geofence Entering!");
//				}else if(transitionType == Geofence.GEOFENCE_TRANSITION_EXIT){
//					List <Geofence> triggerList = event.getTriggeringGeofences();
//
//					for (int i = 0; i < triggerList.size(); i++) {
//						String id = triggerList.get(i).getRequestId();
//						Iterator<Challenge> iter = challenges.iterator();
//						while(iter.hasNext()){
//							Challenge c = iter.next();
//							if(c.getTitle().equals(id)){
//								ChallengeState state = this.getChallengeState(c);
//								if(state != ChallengeState.LOST && state != ChallengeState.PLAYING &&
//										state != ChallengeState.SUCCESS){
//									this.setChallengeState(ChallengeState.INACTIVE);
//								}
//							}
//						}
//					}
//					MysteryMapActivity.updateListView();
//					if(isLogging)
//						Log.i("ReceiveTransitionsIntentService", "Geofence Leaving!");
//				}else{
//					if(isLogging)
//						Log.i("ReceiveTransitionsIntentService", "No transition found! Transitiontype = "+transitionType);
//				}
//			}
//		}
		
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
