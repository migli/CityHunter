package lu.uni.cityhunter.persitence;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public abstract class Challenge implements Parcelable {

	private String title;
	private String description;
	private int coverPicture;
	private LatLng location;
	private int maxNrOfTries;
	private ChallengeType type;
	
	public final static String CHALLENGE_PAR_KEY = "lu.uni.challenge.par";
	
	public Challenge(String title, String description, int coverPicture, LatLng location, int maxNrOfTries, ChallengeType type) {
		this.title = title;
		this.description = description;
		this.coverPicture = coverPicture;
		this.location = location;
		this.maxNrOfTries = maxNrOfTries;
		this.type = type;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setCoverPicture(int coverPicture) {
		this.coverPicture = coverPicture;
	}
	
	public int getCoverPicture() {
		return this.coverPicture;
	}
	
	public void setLocation(LatLng location){
		this.location = location;
	}
	
	public LatLng getLocation(){
		return this.location;
	}
	
	public void setMaxNrOfTries(int maxNrOfTries) {
		this.maxNrOfTries = maxNrOfTries;
	}
	
	public int getMaxNrOfTries() {
		return this.maxNrOfTries;
	}

	public void setType(ChallengeType type) {
		this.type = type;
	}
	
	public ChallengeType getType() {
		return this.type;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.title);
		dest.writeString(this.description);
		dest.writeInt(this.coverPicture);
		dest.writeParcelable(this.location, flags);
		dest.writeInt(this.maxNrOfTries);
		dest.writeParcelable(this.type, flags);
	}
	
}
