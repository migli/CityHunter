package lu.uni.cityhunter.activities;

import java.util.ArrayList;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.activities.challenges.ChooseDateActivity;
import lu.uni.cityhunter.activities.challenges.ChoosePictureActivity;
import lu.uni.cityhunter.activities.challenges.FindDirectionActivity;
import lu.uni.cityhunter.activities.challenges.GuessNameActivity;
import lu.uni.cityhunter.persitence.Challenge;
import lu.uni.cityhunter.persitence.ChallengeState;
import lu.uni.cityhunter.persitence.Mystery;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class MysteryMapActivity extends FragmentActivity implements OnClickListener, ConnectionCallbacks, 
											OnConnectionFailedListener, OnAddGeofencesResultListener{

	private final static int GOOGLE_PLAY_SERVICES_ERROR_RESOLUTION = 9999;
	private final static float GEOFENCE_RADIUS = 50; // in meters
	private final static long GEOFENCE_EXPIRATION_DURATION = 1000*60*60*12; 
	
	static final LatLng LUXEMBOURG = new LatLng(49.611498, 6.131750);
//	static final LatLng LUXEMBOURG = new LatLng(49.626597, 6.158986);

	private boolean isMapFullscreen = false;

	private LocationClient locationClient;
    private PendingIntent geofenceRequestIntent;
    public enum RequestType {ADD}
    private RequestType requestType;
	private ArrayList<Geofence> geofences;
    private boolean inProgress;

	private GoogleMap map;
	private ListView list;
	private Mystery mystery;
	
	protected SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mystery_map);
		
		mystery = (Mystery) getIntent().getParcelableExtra(Mystery.MYSTERY_PAR_KEY);
		ArrayList<Challenge> c = mystery.getChallenges();
		
		ChallengeInfoWindowAdapter infoAdapter = new ChallengeInfoWindowAdapter(getLayoutInflater());
		/*map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setInfoWindowAdapter(infoAdapter);
		
		// Get and set challenge markers + geofences
		geofences = new ArrayList<Geofence>();
		Iterator<Challenge> iter = c.iterator();
		while(iter.hasNext()){
			Challenge challenge = iter.next(); 
			// Add Marker to the map
			map.addMarker(new MarkerOptions()
			.position(challenge.getLocation())
			.title(challenge.getTitle()));
			
			//Create and add Geofence
			Geofence g = new Geofence.Builder()
			.setRequestId(challenge.getTitle())
			.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
			.setCircularRegion(challenge.getLocation().latitude, challenge.getLocation().longitude, GEOFENCE_RADIUS)
			.setExpirationDuration(GEOFENCE_EXPIRATION_DURATION)
			.build();
			this.geofences.add(g);
		}
		this.addGeofences();
		
		// Move the camera instantly to luxembourg with a zoom of 15.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(LUXEMBOURG, 15));
		map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
		
		// Enable current location of device
		map.setMyLocationEnabled(true);*/
		
		// ListView
		list = (ListView) findViewById(R.id.challengeListView);
		list.setAdapter(new ListViewAdapter(c, getLayoutInflater()));
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Challenge challenge = (Challenge) parent.getItemAtPosition(position);
				if (challenge.getTitle().equals("G\u00eblle Fra")) {
					sharedPreferences = getSharedPreferences(ChallengeActivity.GELLE_FRA_PREFERENCES, MODE_PRIVATE);
				} else 
				if (challenge.getTitle().equals("Notre-Dame Cathedral")) {
					sharedPreferences = getSharedPreferences(ChallengeActivity.CATHEDRAL_PREFERENCES, MODE_PRIVATE);
				} else 
				if (challenge.getTitle().equals("Grand Ducal Palace")) {
					sharedPreferences = getSharedPreferences(ChallengeActivity.PALAIS_PREFERENCES, MODE_PRIVATE);
				} else
				if (challenge.getTitle().equals("Place Guillaume II")) {
					sharedPreferences = getSharedPreferences(ChallengeActivity.PLACE_GUILLAUME_PREFERENCES, MODE_PRIVATE);
				} else
				if (challenge.getTitle().equals("Place de Clairefontaine")) {
					sharedPreferences = getSharedPreferences(ChallengeActivity.PLACE_CLAIREFONTAINE_PREFERENCES, MODE_PRIVATE);
				}
				ChallengeState challengeState = ChallengeState.values()[sharedPreferences.getInt("challengeState", ChallengeState.PLAYING.ordinal())];
				if (challengeState.equals(ChallengeState.PLAYING)) {
					Intent intent;
					switch (challenge.getType()) {
						case CHOOSE_DATE:
							intent = new Intent(view.getContext(), ChooseDateActivity.class);
							break;
						case CHOOSE_PICTURE:
							intent = new Intent(view.getContext(), ChoosePictureActivity.class);
							break;
						case GUESS_NAME:
							intent = new Intent(view.getContext(), GuessNameActivity.class);
							break;
						case FIND_DIRECTION:
							intent = new Intent(view.getContext(), FindDirectionActivity.class);
							break;
						default:
							intent = new Intent(view.getContext(), ChallengeActivity.class);
							break;
					}
					Bundle bundle = new Bundle();
					bundle.putParcelable(Challenge.CHALLENGE_PAR_KEY, challenge);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});

		ImageButton btn = (ImageButton) findViewById(R.id.mapFullScreenButton);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// Set size of elements:
		RelativeLayout l = (RelativeLayout) findViewById(R.id.misteryActivityRelativeLayout);
		int lHeight = l.getHeight();

		ImageButton btn = (ImageButton) findViewById(R.id.mapFullScreenButton);

		// Set map automatically to fullscreen:
		int mapHeight = lHeight;
		int listHeight = 0;

		if(this.isMapFullscreen){ // Minimise Map
			mapHeight = (int) Math.ceil(lHeight*2/3);
			listHeight = (int) Math.floor(lHeight/3);
			this.isMapFullscreen = false;
			btn.setBackgroundResource(R.drawable.fullscreen_icon);
		}else{
			this.isMapFullscreen = true;
			btn.setBackgroundResource(R.drawable.minimise_icon);
		}

		LinearLayout lmap = (LinearLayout) findViewById(R.id.mapLinearLayout);
		RelativeLayout.LayoutParams lmapParams = (RelativeLayout.LayoutParams) lmap.getLayoutParams();
		lmapParams.height = mapHeight;
		lmap.setLayoutParams(lmapParams);

		LinearLayout llist = (LinearLayout) findViewById(R.id.challengeListLinearLayout);
		RelativeLayout.LayoutParams llistParams = (RelativeLayout.LayoutParams) llist.getLayoutParams();
		llistParams.height = listHeight;
		llist.setLayoutParams(llistParams);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
		// Set size of elements:
		RelativeLayout l = (RelativeLayout) findViewById(R.id.misteryActivityRelativeLayout);
		int lHeight = l.getHeight();
		
		int mapHeight = (int) Math.ceil(lHeight*2/3);
		int listHeight = (int) Math.floor(lHeight/3);
		
		LinearLayout lmap = (LinearLayout) findViewById(R.id.mapLinearLayout);
		lmap.getLayoutParams().height = mapHeight;
		
		ListView llist = (ListView) findViewById(R.id.challengeListView);
		llist.getLayoutParams().height = listHeight;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case GOOGLE_PLAY_SERVICES_ERROR_RESOLUTION:
				if (resultCode == RESULT_OK) {
					// TODO: Resend Request
				}
				break;
			default: break;
		}
	}
	
	private PendingIntent getTransitionPendingIntent() {
        // Create an explicit Intent
        Intent intent = new Intent(this, ReceiveTransitionsIntentService.class);
        
        return PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void addGeofences() {
        requestType = RequestType.ADD;
        
        if (!servicesConnected()) {
            return;
        }
        
        locationClient = new LocationClient(this, this, this);
        
        if (!inProgress) {
            inProgress = true;
            locationClient.connect();
        } else {
            // A request is currently being progressed => do nothing
        }
    }
    
    private boolean servicesConnected() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		
		if (resultCode == ConnectionResult.SUCCESS) {
			return true;
		} else {
			int errorCode = ConnectionResult.HE.getErrorCode();
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, this, GOOGLE_PLAY_SERVICES_ERROR_RESOLUTION);
			
			if (errorDialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);
				errorFragment.show(getFragmentManager(), "Geofence Detection");
			}
			return false;
		}
	}

	@Override
	public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {
		if (LocationStatusCodes.SUCCESS == statusCode) {
            // it worked => Yeah!
			Toast.makeText(this, "Successfully added geofences!", Toast.LENGTH_SHORT).show();
        } else {
        	Toast.makeText(this, "Adding Geofences failed!", Toast.LENGTH_SHORT).show();
        }

        inProgress = false;
        locationClient.disconnect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
        inProgress = false;
        if(ConnectionResult.HE.hasResolution()) {
            try {
                ConnectionResult.HE.startResolutionForResult( this, GOOGLE_PLAY_SERVICES_ERROR_RESOLUTION);
            } catch (SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            int errorCode = ConnectionResult.HE.getErrorCode();
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, this, GOOGLE_PLAY_SERVICES_ERROR_RESOLUTION);
            if (errorDialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getFragmentManager(), "Geofence Detection");
            }
        }
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		switch (requestType) {
		case ADD :
			geofenceRequestIntent = getTransitionPendingIntent();
			locationClient.addGeofences(geofences, geofenceRequestIntent, this);
		}

	}

	@Override
	public void onDisconnected() {
		inProgress = false;
		locationClient = null;
		Toast.makeText(this, "Geofences disconnection!", Toast.LENGTH_SHORT).show();
	}

}
