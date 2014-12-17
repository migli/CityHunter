package lu.uni.cityhunter.datastructure;

import lu.uni.cityhunter.R;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class ChallengeInfoWindow implements InfoWindowAdapter {
	
	private LayoutInflater layoutInflater;
	public ChallengeInfoWindow(LayoutInflater layoutInflater){
		this.layoutInflater = layoutInflater;
	}
	
	@Override
	public View getInfoContents(Marker marker) {
		View v = layoutInflater.inflate(R.layout.info_window_layout, null);
		TextView title = (TextView) v.findViewById(R.id.infoWindow_title);
		title.setText(marker.getTitle());
		TextView description = (TextView) v.findViewById(R.id.infoWindow_text);
		description.setText(marker.getSnippet());
		return v;
	}
	
	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}
}