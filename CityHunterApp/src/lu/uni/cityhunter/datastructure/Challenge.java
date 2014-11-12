package lu.uni.cityhunter.datastructure;

import com.google.android.gms.maps.model.LatLng;

import android.os.Parcel;
import android.os.Parcelable;

public class Challenge implements Parcelable {

	private LatLng location;
	private int coverPicture;
	private String description;
	private String title;
	
	public final static String CHALLENGE_PAR_KEY = "lu.uni.challenge.par";
	
	public Challenge(int coverPicture, LatLng location, String title, String description) {
		this.coverPicture = coverPicture;
		this.location = location;
		this.title = title;
		this.description = description;
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
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public static final Parcelable.Creator<Challenge> CREATOR = new Creator<Challenge>() {  
		  public Challenge createFromParcel(Parcel source) {  
			  Challenge challenge = new Challenge(source.readInt(), (LatLng) source.readParcelable(LatLng.class.getClassLoader()), source.readString(), source.readString());
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
		dest.writeInt(this.coverPicture);
		dest.writeParcelable(location, flags);
		dest.writeString(this.title);
		dest.writeString(this.description);
	}
	
	
}
