package lu.uni.cityhunter.persistence;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class QuestionChallenge extends Challenge {

	private String[] possibleAnswers;
	private String[] possibleAnswerDescriptions;
	private String answer;
	private String answerDescription;
	
	public QuestionChallenge(String title, String description, int coverPicture, LatLng location, int maxNrOfTries, ChallengeType type, String[] possibleAnswers, String[] possibleAnswerDescriptions, String answer, String answerDescription) {
		super(title, description, coverPicture, location, maxNrOfTries, type);
		this.possibleAnswers = possibleAnswers;
		this.possibleAnswerDescriptions = possibleAnswerDescriptions;
		this.answer = answer;
		this.answerDescription = answerDescription;
	}
	
	public void setPossibleAnswers(String[] possibleAnswers) {
		this.possibleAnswers = possibleAnswers;
	}
	
	public String[] getPossibleAnswers() {
		return this.possibleAnswers;
	}
	
	public void setPossibleAnswerDescriptions(String[] possibleAnswerDescriptions) {
		this.possibleAnswerDescriptions = possibleAnswerDescriptions;
	}
	
	public String[] getPossibleAnswerDescriptions() {
		return this.possibleAnswerDescriptions;
	}
	
	public void setAsnwer(String answer) {
		this.answer = answer;
	}
	
	public String getAnswer() {
		return this.answer;
	}
	
	public void setAnswerDescription(String answerDescription) {
		this.answerDescription = answerDescription;
	}
	
	public String getAnswerDescription() {
		return this.answerDescription;
	}

	public static final Parcelable.Creator<Challenge> CREATOR = new Creator<Challenge>() {  
		  public Challenge createFromParcel(Parcel source) {  
			  Challenge challenge = new QuestionChallenge(source.readString(), source.readString(), source.readInt(), (LatLng) source.readParcelable(LatLng.class.getClassLoader()), source.readInt(), (ChallengeType) source.readParcelable(ChallengeType.class.getClassLoader()), source.createStringArray(), source.createStringArray(), source.readString(), source.readString());
		      return challenge;  
		  }  
		  public Challenge[] newArray(int size) {  
		      return new Challenge[size];  
		  }  
	}; 
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeStringArray(this.possibleAnswers);
		dest.writeStringArray(this.possibleAnswerDescriptions);
		dest.writeString(this.answer);
		dest.writeString(this.answerDescription);
	}

}
