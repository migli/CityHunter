package lu.uni.cityhunter.persistence;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Mystery implements Parcelable {
	
	private String title;
	private String question;
	private String answer;
	private ArrayList<Challenge> challenges;
	private String hint;

	public final static String MYSTERY_PAR_KEY = "lu.uni.mystery.par";
	
	public Mystery(String title, String question, String answer, ArrayList<Challenge> challenges, String hint) {
		this.title = title;
		this.question = question;
		this.answer = answer;
		this.challenges = challenges;
		this.hint = hint;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public String getQuestion() {
		return this.question;
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public String getAnswer() {
		return this.answer;
	}
	
	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}
	
	public void setChallenge(Challenge challenge) {
		this.challenges.add(challenge);
	}
	
	public Challenge getChallenge(int index) {
		return this.challenges.get(index);
	}
	
	public void removeChallenge(Challenge challenge) {
		this.challenges.remove(challenge);
	}
	
	public ArrayList<Challenge> getChallenges() {
		return this.challenges;
	}

	@SuppressWarnings("unchecked")
	public static final Parcelable.Creator<Mystery> CREATOR = new Creator<Mystery>() {  
		  public Mystery createFromParcel(Parcel source) {  
		      Mystery mistery = new Mystery(source.readString(), source.readString(), source.readString(), source.readArrayList(Challenge.class.getClassLoader()),  source.readString());
		      return mistery;  
		  }  
		  public Mystery[] newArray(int size) {  
		      return new Mystery[size];  
		  }  
	};  
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.title);
		dest.writeString(this.question); 
		dest.writeString(this.answer); 
		dest.writeList(this.challenges);
		dest.writeString(this.hint);
	}
	
}
