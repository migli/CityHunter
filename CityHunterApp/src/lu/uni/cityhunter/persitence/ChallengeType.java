package lu.uni.cityhunter.persitence;

import android.os.Parcel;
import android.os.Parcelable;

public enum ChallengeType implements Parcelable {

	CHOOSE_DATE, CHOOSE_PICTURE, GUESS_NAME, FIND_DIRECTION;
	
	public static final Parcelable.Creator<ChallengeType> CREATOR = new Creator<ChallengeType>() {  
		  public ChallengeType createFromParcel(Parcel source) {  
			  return ChallengeType.values()[source.readInt()];
		  }  
		  public ChallengeType[] newArray(int size) {  
		      return new ChallengeType[size];  
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
