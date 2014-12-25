package lu.uni.cityhunter.persistence;

import android.os.Parcel;
import android.os.Parcelable;

public enum MysteryState implements Parcelable {
	
	SUCCESS, PLAYING;
	
	public static final Parcelable.Creator<MysteryState> CREATOR = new Creator<MysteryState>() {  
		  public MysteryState createFromParcel(Parcel source) {  
			  return MysteryState.values()[source.readInt()];
		  }  
		  public MysteryState[] newArray(int size) {  
		      return new MysteryState[size];  
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
