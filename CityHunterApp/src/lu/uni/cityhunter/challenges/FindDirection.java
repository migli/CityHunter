package lu.uni.cityhunter.challenges;

import lu.uni.cityhunter.datastructure.Challenge;
import lu.uni.cityhunter.datastructure.ChallengeState;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class FindDirection extends Challenge implements Parcelable {

	private int direction;
	private int time;
	
	public FindDirection(String title, String description, int coverPicture, LatLng location, ChallengeState state, int nrOfTries, int direction, int time) {
		super(title, description, coverPicture, location, state, nrOfTries);
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
			  Challenge challenge = new FindDirection(source.readString(), source.readString(), source.readInt(), (LatLng) source.readParcelable(LatLng.class.getClassLoader()), (ChallengeState) source.readParcelable(ChallengeState.class.getClassLoader()), source.readInt(), source.readInt(), source.readInt());
		      return challenge;  
		  }  
		  public Challenge[] newArray(int size) {  
		      return new Challenge[size];  
		  }  
	}; 
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.getTitle());
		dest.writeString(this.getDescription());
		dest.writeInt(this.getCoverPicture());
		dest.writeParcelable(this.getLocation(), flags);
		dest.writeParcelable(getState(), flags);
		dest.writeInt(this.getNrOfTries());
		dest.writeInt(this.direction);
		dest.writeInt(this.time);
	}
	
}
