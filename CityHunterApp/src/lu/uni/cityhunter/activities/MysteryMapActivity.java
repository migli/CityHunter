package lu.uni.cityhunter.activities;

import java.util.ArrayList;
import java.util.Iterator;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.activities.GoogleDirection.OnDirectionResponseListener;
import lu.uni.cityhunter.activities.adapters.ChallengeInfoWindowAdapter;
import lu.uni.cityhunter.activities.adapters.ChallengeListViewAdapter;
import lu.uni.cityhunter.activities.challenges.ChooseDateActivity;
import lu.uni.cityhunter.activities.challenges.ChoosePictureActivity;
import lu.uni.cityhunter.activities.challenges.FindDirectionActivity;
import lu.uni.cityhunter.activities.challenges.GuessNameActivity;
import lu.uni.cityhunter.persistence.Challenge;
import lu.uni.cityhunter.persistence.ChallengeState;
import lu.uni.cityhunter.persistence.Mystery;

import org.w3c.dom.Document;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MysteryMapActivity extends FragmentActivity implements OnClickListener, ConnectionCallbacks, 
											OnConnectionFailedListener, OnDirectionResponseListener, 
											LocationListener, OnItemClickListener {

	private final static int GOOGLE_PLAY_SERVICES_ERROR_RESOLUTION = 9999;
	private final static float GEOFENCE_RADIUS = 50; // in meters
	private final static long GEOFENCE_EXPIRATION_DURATION = 1000*60*60*12; 
	
	private boolean isLogging = false;

	// Static variables of run and this instance needed, in order to update 
	// ListView when Geofences are triggered!
	private static Runnable RUN;
	private static MysteryMapActivity INSTANCE = null;
	
	static final LatLng LUXEMBOURG = new LatLng(49.611498, 6.131750);
//	static final LatLng LUXEMBOURG = new LatLng(49.626597, 6.158986);

	private boolean isMapFullscreen = false;
	private boolean geofencesAdded = false;

	private GoogleApiClient googleApiClient;
    private PendingIntent geofenceRequestIntent;
	private ArrayList<Geofence> geofences;

	private GoogleMap map;
	private Mystery mystery;
	
	protected SharedPreferences sharedPreferences;
	private GoogleDirection directions;
	private Polyline route;
	private LatLng currentLocation;
	
	private boolean routeToNextChallenge; // Flag to know if user wants to route to a given Challenge or the next one
	private int indexOfChallenge; // Index of the challenge to route to
	
	public MysteryMapActivity(){
		INSTANCE = this;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mystery_map);
		
		routeToNextChallenge = true;
		indexOfChallenge = 0;
		
		mystery = (Mystery) getIntent().getParcelableExtra(Mystery.MYSTERY_PAR_KEY);
		ArrayList<Challenge> c = mystery.getChallenges();
		
		directions = new GoogleDirection(this);
		directions.setOnDirectionResponseListener(this);
		
		ChallengeInfoWindowAdapter infoAdapter = new ChallengeInfoWindowAdapter(getLayoutInflater());
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setInfoWindowAdapter(infoAdapter);
		
		
		// Get and set challenge markers + geofences
		geofences = new ArrayList<Geofence>();
		Iterator<Challenge> iter = c.iterator();
		while(iter.hasNext()){
			Challenge challenge = iter.next(); 
			// Add Marker to the map
			map.addMarker(new MarkerOptions()
			.position(challenge.getLocation())
			.title(challenge.getTitle())
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));
			
			//Create and add Geofence
			Geofence g = new Geofence.Builder()
			.setRequestId(challenge.getTitle())
			.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
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
		map.setMyLocationEnabled(true);
	
		//ListView
		final ListView list = (ListView) findViewById(R.id.challengeListView);
		final ChallengeListViewAdapter listAdapter = new ChallengeListViewAdapter(c, getLayoutInflater());
		list.setAdapter(listAdapter);
		list.setOnItemClickListener(this);
		
		// Create Runable to update Listview when geofences triggered:
		RUN = new Runnable(){
			public void run(){
				//reload content
				listAdapter.notifyDataSetChanged();
				list.invalidateViews();
				list.refreshDrawableState();
			}
		};

		ImageButton btn = (ImageButton) findViewById(R.id.mapFullScreenButton);
		btn.setOnClickListener(this);

		// Initialize the current location manager
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
	}
	
	public static void updateListView(){
		if(RUN != null && INSTANCE != null){
			INSTANCE.runOnUiThread(RUN);
		}
	}
	
	private void updateIndexOfChallenge(){
		ArrayList<Challenge> challenges = mystery.getChallenges();
		if(routeToNextChallenge){
			// Take the index of the first challenge with state different from success and lost
			for(int i = 0; i < challenges.size(); i++){
				ChallengeState state = this.getChallengeState(challenges.get(i));
				if(state != ChallengeState.LOST && state != ChallengeState.SUCCESS){
					indexOfChallenge = i;
					break;
				}
			}
		}else{ 
			// User specified to route to a specific Challenge
			// Check the state of the given challenge:
			ChallengeState state = this.getChallengeState(challenges.get(indexOfChallenge));
			// If challenge LOST or SUCCESS => change to automatic routing mode
			if(state == ChallengeState.LOST || state == ChallengeState.SUCCESS){
				routeToNextChallenge = true;
				this.updateIndexOfChallenge();
			}
		}
	}
	
	public void routeToNextChallenge(View v){
		this.routeToNextChallenge = true;
		this.updateIndexOfChallenge();
		directions.request(currentLocation, mystery.getChallenge(indexOfChallenge).getLocation(), GoogleDirection.MODE_WALKING);
	}
	
//	private void requestDirections(ArrayList<LatLng> path){
//		Iterator<LatLng> iter = path.iterator();
//		if(!path.isEmpty()){
//			LatLng start = iter.next();
//			while(iter.hasNext()){
//				LatLng end = iter.next();
//				// Request the directions:
//				directions.request(start, end, GoogleDirection.MODE_WALKING);
//				start = end;
//			}
//		}
//	}
	
	
	
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
        if (!servicesConnected()) {
            return;
        }
        
        googleApiClient = new GoogleApiClient.Builder(this)
        .addApi(LocationServices.API)
        .addConnectionCallbacks(this)
        .build();
        googleApiClient.connect();
    }
    
    private boolean servicesConnected() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		
		if (resultCode == ConnectionResult.SUCCESS) {
			return true;
		} else {
			int errorCode = ConnectionResult.Iu.getErrorCode();
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
	public void onConnectionFailed(ConnectionResult result) {
        if(result.hasResolution()) {
            try {
                result.startResolutionForResult(this, GOOGLE_PLAY_SERVICES_ERROR_RESOLUTION);
            } catch (SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            int errorCode = result.getErrorCode();
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, this, GOOGLE_PLAY_SERVICES_ERROR_RESOLUTION);
            if (errorDialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getFragmentManager(), "Geofence Detection");
            }
        }
	}

	@Override
	public void onConnected(Bundle bundle) {
		geofenceRequestIntent = getTransitionPendingIntent();
		GeofencingRequest request = new GeofencingRequest.Builder().addGeofences(geofences).build();
		PendingResult<Status> result = LocationServices.GeofencingApi.addGeofences(googleApiClient, request, geofenceRequestIntent);
		result.setResultCallback(new ResultCallback<Status>() {
	        @Override
	        public void onResult(Status status) {
	            if (status.isSuccess()) {
	                // Successfully registered
	                if(isLogging)
	                	Log.i("onConnected", "Successfully added Geofences!");
	                geofencesAdded = true;
	            } else if (status.hasResolution()) {
	                // Google provides a way to fix the issue
	                /*
	                status.startResolutionForResult(
	                        mContext,     // your current activity used to receive the result
	                        RESULT_CODE); // the result code you'll look for in your
	                // onActivityResult method to retry registering
	                */
	            } else {
	                // No recovery.
	                Log.e("onConnected", "Registering failed: " + status.getStatusMessage());
	            }
	        }
	    });
	}
	
	@Override
	public void onResponse(String status, Document doc, GoogleDirection gd) {
		ArrayList<LatLng> directionPoint = directions.getDirection(doc);
		if(isLogging)
			Log.i("MysteryMapActivity", "Get direction status: "+directions.getStatus(doc));
		if(route != null){
			route.remove();
		}
		PolylineOptions rectLine = new PolylineOptions().width(5)
									    .color(Color.parseColor("#45AEFF"));
		

		for(int i = 0 ; i < directionPoint.size() ; i++) {          
			rectLine.add(directionPoint.get(i));
		}
		route = map.addPolyline(rectLine);
	}

	@Override
	public void onLocationChanged(Location location) {
		if(isLogging)
			Log.i("MysteryMapActivity", "Location changed!");
		updateIndexOfChallenge();
		currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
		if(!geofencesAdded)
			checkDistancesToChallenges();
		LatLng end = mystery.getChallenges().get(indexOfChallenge).getLocation();
		// Request the directions:
		directions.request(currentLocation, end, GoogleDirection.MODE_WALKING);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Challenge challenge = (Challenge) parent.getItemAtPosition(position);
		ChallengeState state = this.getChallengeState(challenge);
		if(isLogging)
			Log.i("MysteryMapActivity", "Challenge '"+challenge.getTitle()+"' in state '"+state+"'!");
		state = ChallengeState.ACTIVE;
		if(state == ChallengeState.ACTIVE || state == ChallengeState.PLAYING){
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
			bundle.putParcelable(Challenge.CHALLENGE_PAR_KEY, (Parcelable) challenge);
			intent.putExtras(bundle);
			startActivity(intent);
		}else if(state == ChallengeState.INACTIVE){
			indexOfChallenge = mystery.getChallenges().indexOf(challenge);
			routeToNextChallenge = false;
			directions.request(currentLocation, mystery.getChallenge(indexOfChallenge).getLocation(), GoogleDirection.MODE_WALKING);
		}else if(state == ChallengeState.SUCCESS){
			Toast.makeText(view.getContext(), "You already solved this challenge!", Toast.LENGTH_SHORT).show();
		}else if(state == ChallengeState.LOST){
			Toast.makeText(view.getContext(), "Sorry, but you cannot try again!", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private ChallengeState getChallengeState(Challenge challenge){
		SharedPreferences sharedPreferences = null;
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
	
	private void checkDistancesToChallenges(){
		if(currentLocation != null){
			ArrayList<Challenge> challenges = mystery.getChallenges();
			Iterator<Challenge> iter = challenges.iterator();
			while(iter.hasNext()){
				Challenge c = iter.next();
				float[] distance = new float[1];
				Location.distanceBetween(currentLocation.latitude, currentLocation.longitude, 
						c.getLocation().latitude, c.getLocation().longitude, distance);
				if(distance[0] <= GEOFENCE_RADIUS){
					ChallengeState state = this.getChallengeState(c);
					if(state == ChallengeState.INACTIVE){
						// Change Challenge state to ACTIVE
						SharedPreferences.Editor editor = sharedPreferences.edit();
						editor.putInt("challengeState", ChallengeState.ACTIVE.ordinal());
						editor.commit();
					}
				}
			}
		}else{
			if(isLogging)
				Log.e("MysteryMapActivity", "Current location is null in 'checkDistancesToChallenges'!");
		}
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// nothing
	}

}
