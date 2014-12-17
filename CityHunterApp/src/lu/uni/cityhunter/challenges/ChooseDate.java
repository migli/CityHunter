package lu.uni.cityhunter.challenges;

import lu.uni.cityhunter.datastructure.Challenge;
import lu.uni.cityhunter.datastructure.ChallengeState;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class ChooseDate extends Challenge implements Parcelable {

	private int[] dates;
	private int answer;
	
	public ChooseDate(String title, String description, int coverPicture, LatLng location, ChallengeState state, int nrOfTries, int[] dates, int answer) {
		super(title, description, coverPicture, location, state, nrOfTries);
		this.dates = dates;
		this.answer = answer;
	}
	
	public void setDates(int[] dates) {
		this.dates = dates;
	}
	
	public int[] getDates() {
		return this.dates;
	}
	
	public void setAsnwer(int answer) {
		this.answer = answer;
	}
	
	public int getAnswer() {
		return this.answer;
	}
	
	public static final Parcelable.Creator<Challenge> CREATOR = new Creator<Challenge>() {  
		  public Challenge createFromParcel(Parcel source) {  
			  Challenge challenge = new ChooseDate(source.readString(), source.readString(), source.readInt(), (LatLng) source.readParcelable(LatLng.class.getClassLoader()), (ChallengeState) source.readParcelable(ChallengeState.class.getClassLoader()), source.readInt(), source.createIntArray(), source.readInt());
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
		dest.writeString(this.getTitle());
		dest.writeString(this.getDescription());
		dest.writeInt(this.getCoverPicture());
		dest.writeParcelable(this.getLocation(), flags);
		dest.writeParcelable(getState(), flags);
		dest.writeInt(this.getNrOfTries());
		dest.writeIntArray(this.dates);
		dest.writeInt(this.answer);
	}

}
