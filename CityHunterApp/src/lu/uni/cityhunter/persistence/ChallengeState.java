package lu.uni.cityhunter.persistence;

import android.os.Parcel;
import android.os.Parcelable;

public enum ChallengeState implements Parcelable {

	SUCCESS, PLAYING, LOST;
	
	public static final Parcelable.Creator<ChallengeState> CREATOR = new Creator<ChallengeState>() {  
		  public ChallengeState createFromParcel(Parcel source) {  
			  return ChallengeState.values()[source.readInt()];
		  }  
		  public ChallengeState[] newArray(int size) {  
		      return new ChallengeState[size];  
		  }  
	};  
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ordinal()); 
	}
	
}
