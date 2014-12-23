package lu.uni.cityhunter.activities;

import java.util.ArrayList;

import lu.uni.cityhunter.R;
import lu.uni.cityhunter.activities.challenges.ChooseDateActivity;
import lu.uni.cityhunter.activities.challenges.ChoosePictureActivity;
import lu.uni.cityhunter.activities.challenges.FindDirectionActivity;
import lu.uni.cityhunter.activities.challenges.GuessNameActivity;
import lu.uni.cityhunter.persistence.Challenge;
import lu.uni.cityhunter.persistence.ChallengeState;
import lu.uni.cityhunter.persistence.Mystery;

import lu.uni.cityhunter.activities.GoogleDirection.OnDirectionResponseListener;
import lu.uni.cityhunter.activities.adapters.ChallengeInfoWindowAdapter;
import lu.uni.cityhunter.activities.adapters.ChallengeListViewAdapter;

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
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MysteryMapActivity extends FragmentActivity implements OnClickListener, ConnectionCallbacks, 
											OnConnectionFailedListener, OnAddGeofencesResultListener,
											OnDirectionResponseListener, LocationListener, OnItemClickListener {

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
	private Mystery mistery;
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
		
		mistery = (Mystery) getIntent().getParcelableExtra(Mystery.MYSTERY_PAR_KEY);
		ArrayList<Challenge> c = mistery.getChallenges();
		
		// TODO: Temporarily: Set path statically for Mistery 1
		// => Should be given by the mistery object?
		ArrayList<LatLng> path = new ArrayList<LatLng>();
		path.add(c.get(0).getLocation()); // Golden Lady
		path.add(c.get(1).getLocation()); // Cathedral
		path.add(c.get(2).getLocation()); // Place Guillaume
		path.add(c.get(3).getLocation()); // Palace
		path.add(c.get(4).getLocation()); // Place Cairefontaine
		
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
		
		// ListView
//		list.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				Challenge challenge = (Challenge) parent.getItemAtPosition(position);
//				if (challenge.getTitle().equals("G\u00eblle Fra")) {
//					sharedPreferences = getSharedPreferences(ChallengeActivity.GELLE_FRA_PREFERENCES, MODE_PRIVATE);
//				} else 
//				if (challenge.getTitle().equals("Notre-Dame Cathedral")) {
//					sharedPreferences = getSharedPreferences(ChallengeActivity.CATHEDRAL_PREFERENCES, MODE_PRIVATE);
//				} else 
//				if (challenge.getTitle().equals("Grand Ducal Palace")) {
//					sharedPreferences = getSharedPreferences(ChallengeActivity.PALAIS_PREFERENCES, MODE_PRIVATE);
//				} else
//				if (challenge.getTitle().equals("Place Guillaume II")) {
//					sharedPreferences = getSharedPreferences(ChallengeActivity.PLACE_GUILLAUME_PREFERENCES, MODE_PRIVATE);
//				} else
//				if (challenge.getTitle().equals("Place de Clairefontaine")) {
//					sharedPreferences = getSharedPreferences(ChallengeActivity.PLACE_CLAIREFONTAINE_PREFERENCES, MODE_PRIVATE);
//				}
//				ChallengeState challengeState = ChallengeState.values()[sharedPreferences.getInt("challengeState", ChallengeState.PLAYING.ordinal())];
//				if (challengeState.equals(ChallengeState.PLAYING)) {
//					Intent intent;
//					switch (challenge.getType()) {
//						case CHOOSE_DATE:
//							intent = new Intent(view.getContext(), ChooseDateActivity.class);
//							break;
//						case CHOOSE_PICTURE:
//							intent = new Intent(view.getContext(), ChoosePictureActivity.class);
//							break;
//						case GUESS_NAME:
//							intent = new Intent(view.getContext(), GuessNameActivity.class);
//							break;
//						case FIND_DIRECTION:
//							intent = new Intent(view.getContext(), FindDirectionActivity.class);
//							break;
//						default:
//							intent = new Intent(view.getContext(), ChallengeActivity.class);
//							break;
//					}
//					Bundle bundle = new Bundle();
//					bundle.putParcelable(Challenge.CHALLENGE_PAR_KEY, challenge);
//					intent.putExtras(bundle);
//					startActivity(intent);
//				}
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
		ArrayList<Challenge> challenges = mistery.getChallenges();
		if(routeToNextChallenge){
			// Take the index of the first challenge with state different from success and lost
			for(int i = 0; i < challenges.size(); i++){
				ChallengeState state = challenges.get(i).getState();
				if(state != ChallengeState.LOST && state != ChallengeState.SUCCESS){
					indexOfChallenge = i;
					break;
				}
			}
		}else{ 
			// User specified to route to a specific Challenge
			// Check the state of the given challenge:
			ChallengeState state = challenges.get(indexOfChallenge).getState();
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
		directions.request(currentLocation, mistery.getChallenge(indexOfChallenge).getLocation(), GoogleDirection.MODE_WALKING);
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
		LatLng end = mistery.getChallenges().get(indexOfChallenge).getLocation();
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
		if(challenge.getState() == ChallengeState.ACTIVE){
			Intent intent = new Intent(view.getContext(), ChallengeActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable(Challenge.CHALLENGE_PAR_KEY, (Parcelable) challenge);
			intent.putExtras(bundle);
			startActivity(intent);
		}else if(challenge.getState() == null){
//			Toast.makeText(view.getContext(), "You need to be in proximity of '"+challenge.getTitle()+"' in order to start the challenge!", Toast.LENGTH_SHORT).show();
			indexOfChallenge = mistery.getChallenges().indexOf(challenge);
			routeToNextChallenge = false;
			directions.request(currentLocation, mistery.getChallenge(indexOfChallenge).getLocation(), GoogleDirection.MODE_WALKING);
		}else if(challenge.getState() == ChallengeState.SUCCESS){
			Toast.makeText(view.getContext(), "You already solved this challenge!", Toast.LENGTH_SHORT).show();
		}else if(challenge.getState() == ChallengeState.LOST){
			Toast.makeText(view.getContext(), "Sorry, but you cannot try again!", Toast.LENGTH_SHORT).show();
		}
		
	}

}
