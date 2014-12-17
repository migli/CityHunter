package lu.uni.cityhunter.challenges;

import lu.uni.cityhunter.datastructure.Challenge;
import lu.uni.cityhunter.datastructure.ChallengeState;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class GuessName extends Challenge implements Parcelable {

	private String name;
	
	public GuessName(String title, String description, int coverPicture, LatLng location, ChallengeState state, int nrOfTries, String name) {
		super(title, description, coverPicture, location, state, nrOfTries);
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static final Parcelable.Creator<Challenge> CREATOR = new Creator<Challenge>() {  
		  public Challenge createFromParcel(Parcel source) {  
			  Challenge challenge = new GuessName(source.readString(), source.readString(), source.readInt(), (LatLng) source.readParcelable(LatLng.class.getClassLoader()), (ChallengeState) source.readParcelable(ChallengeState.class.getClassLoader()), source.readInt(), source.readString());
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
		dest.writeString(this.name);
	}

}
