package lu.uni.cityhunter.challenges;

import lu.uni.cityhunter.datastructure.Challenge;
import lu.uni.cityhunter.datastructure.ChallengeState;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class ChoosePicture extends Challenge implements Parcelable {

	private int[] pictures;
	private String[] pictureDescriptions;
	private int answer;
	private String asnwerDescription;
	
	public ChoosePicture(String title, String description, int coverPicture, LatLng location, ChallengeState state, int nrOfTries, int[] pictures, String[] pictureDescriptions, int answer, String answerDescription) {
		super(title, description, coverPicture, location, state, nrOfTries);
		this.pictures = pictures;
		this.pictureDescriptions = pictureDescriptions;
		this.answer = answer;
		this.asnwerDescription = answerDescription;
	}
	
	public void setPictures(int[] pictures) {
		this.pictures = pictures;
	}
	
	public int[] getPictures() {
		return this.pictures;
	}
	
	public void setPictureDescriptions(String[] pictureDescriptions) {
		this.pictureDescriptions = pictureDescriptions;
	}
	
	public String[] getPictureDescriptions() {
		return this.pictureDescriptions;
	}
	
	public void setAsnwer(int answer) {
		this.answer = answer;
	}
	
	public int getAnswer() {
		return this.answer;
	}
	
	public void setAnswerDescription(String answerDescription) {
		this.asnwerDescription = answerDescription;
	}
	
	public String getAnswerDescription() {
		return this.asnwerDescription;
	}

	public static final Parcelable.Creator<Challenge> CREATOR = new Creator<Challenge>() {  
		  public Challenge createFromParcel(Parcel source) {  
			  Challenge challenge = new ChoosePicture(source.readString(), source.readString(), source.readInt(), (LatLng) source.readParcelable(LatLng.class.getClassLoader()), (ChallengeState) source.readParcelable(ChallengeState.class.getClassLoader()), source.readInt(), source.createIntArray(), source.createStringArray(), source.readInt(), source.readString());
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
		dest.writeIntArray(this.pictures);
		dest.writeStringArray(this.pictureDescriptions);
		dest.writeInt(this.answer);
		dest.writeString(this.asnwerDescription);
	}

}
