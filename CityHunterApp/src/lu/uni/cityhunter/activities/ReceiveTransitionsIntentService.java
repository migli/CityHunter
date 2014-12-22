package lu.uni.cityhunter.activities;

import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;


public class ReceiveTransitionsIntentService extends IntentService{

	public ReceiveTransitionsIntentService() {
		super("ReceiveTransitionsIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if(LocationClient.hasError(intent)){
			// Do nothing... Shit happens^^
			Toast.makeText(this, "Error in transition intent!", Toast.LENGTH_SHORT);
		}else{
			int transitionType = LocationClient.getGeofenceTransition(intent);
			if(transitionType == Geofence.GEOFENCE_TRANSITION_ENTER){
				List <Geofence> triggerList = LocationClient.getTriggeringGeofences(intent);
				String[] triggerIds = new String[triggerList.size()];

	            for (int i = 0; i < triggerIds.length; i++) {
	                triggerIds[i] = triggerList.get(i).getRequestId();
	            }
	            Toast.makeText(this, "Received transition Intent for '"+triggerIds[0]+"'", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
}
