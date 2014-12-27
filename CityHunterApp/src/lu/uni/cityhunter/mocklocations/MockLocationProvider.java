package lu.uni.cityhunter.mocklocations;

import java.util.ArrayList;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;

public class MockLocationProvider implements LocationSource{
	
	public final static int INTERLOCATiON_TIMEINTERVAL = 1; // The time (in seconds) between two different mock locations
	
	private final boolean isLogging = false;
	
	private String providerName;
	private Context context;
	private boolean isThreadRunning = false;
	private OnLocationChangedListener listener;

	public MockLocationProvider(String providerName, Context context) {
		this.providerName = providerName;
		this.context = context;

		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		lm.addTestProvider(providerName, false, false, false, false, false, true, true, Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
		lm.setTestProviderEnabled(providerName, true);
	}
	
	public void simulatePath(ArrayList<LatLng> latLngList){
		if(!isThreadRunning){
			final ArrayList<LatLng> path = latLngList;
			isThreadRunning = true;
			new Thread(new Runnable(){
				public void run(){
					Iterator<LatLng> iter = path.iterator();
					while(iter.hasNext()){
						LatLng loc = iter.next();
						pushLocation(loc.latitude, loc.longitude);
						try {
							Thread.sleep(MockLocationProvider.INTERLOCATiON_TIMEINTERVAL*1000);
						} catch (InterruptedException e) {
							if(isLogging)
								Log.e("MockLocationProvider Thread", "Sleep error: "+e.getMessage());
						}
					}
					isThreadRunning = false;
				}
			}).start();
		}else{
			if(isLogging)
				Log.w("MockLocationProvider", "Thread is still running!");
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) @SuppressLint("NewApi") 
	public synchronized void pushLocation(double lat, double lon) {
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		Location mockLocation = new Location(providerName);
		mockLocation.setLatitude(lat);
		mockLocation.setLongitude(lon);
		mockLocation.setAltitude(0);
		mockLocation.setAccuracy(1);
		mockLocation.setTime(System.currentTimeMillis());
		mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
		lm.setTestProviderLocation(providerName, mockLocation);
		if(this.listener != null){
			this.listener.onLocationChanged(mockLocation);
		}
	}

	public void shutdown() {
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		lm.removeTestProvider(providerName);
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		this.listener = listener;
	}

	@Override
	public void deactivate() {
		this.listener = null;
	}
}
