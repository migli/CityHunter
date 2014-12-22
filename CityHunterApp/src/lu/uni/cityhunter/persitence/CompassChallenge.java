package lu.uni.cityhunter.persitence;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class CompassChallenge extends Challenge {

	private int direction;
	private int time;
	
	public CompassChallenge(String title, String description, int coverPicture, LatLng location, int maxNrOfTries, ChallengeType type, int direction, int time) {
		super(title, description, coverPicture, location, maxNrOfTries, type);
		this.direction = direction;
		this.time = time;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public int getDirection() {
		return this.direction;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	
	public int getTime() {
		return this.time;
	}
	
	public static final Parcelable.Creator<Challenge> CREATOR = new Creator<Challenge>() {  
		  public Challenge createFromParcel(Parcel source) {  
			  Challenge challenge = new CompassChallenge(source.readString(), source.readString(), source.readInt(), (LatLng) source.readParcelable(LatLng.class.getClassLoader()), source.readInt(), (ChallengeType) source.readParcelable(ChallengeType.class.getClassLoader()), source.readInt(), source.readInt());
		      return challenge;  
		  }  
		  public Challenge[] newArray(int size) {  
		      return new Challenge[size];  
		  }  
	}; 

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(this.direction);
		dest.writeInt(this.time);
	}
	
}
