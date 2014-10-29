package lu.uni.cityhunter.datastructure;

import com.google.android.gms.maps.model.LatLng;

import android.os.Parcel;
import android.os.Parcelable;

public class Challenge implements Parcelable {

	private LatLng location;
	private int picture;
	
	public final static String CHALLENGE_PAR_KEY = "lu.uni.challenge.par";
	
	public Challenge(int picture) {
		this.picture = picture;
	}
	
	public void setPicture(int picture) {
		this.picture = picture;
	}
	
	public int getPicture() {
		return this.picture;
	}
	
	public static final Parcelable.Creator<Challenge> CREATOR = new Creator<Challenge>() {  
		  public Challenge createFromParcel(Parcel source) {  
			  Challenge challenge = new Challenge(source.readInt());
		      return challenge;  
		  }  
		  public Challenge[] newArray(int size) {  
		      return new Challenge[size];  
		  }  
	};  
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.picture);
	}
	
}
