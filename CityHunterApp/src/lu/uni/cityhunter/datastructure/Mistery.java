package lu.uni.cityhunter.datastructure;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Mistery implements Parcelable {
	
	private String title;
	private String question;
	private String answer;
	private ArrayList<Challenge> challenges;

	public final static String MISTERY_PAR_KEY = "lu.uni.mistery.par";
	
	public Mistery(String title, String question, String answer, ArrayList<Challenge> challenges) {
		this.title = title;
		this.question = question;
		this.answer = answer;
		this.challenges = challenges;
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
	public static final Parcelable.Creator<Mistery> CREATOR = new Creator<Mistery>() {  
		  public Mistery createFromParcel(Parcel source) {  
		      Mistery mistery = new Mistery(source.readString(), source.readString(), source.readString(), source.readArrayList(Challenge.class.getClassLoader()));
		      return mistery;  
		  }  
		  public Mistery[] newArray(int size) {  
		      return new Mistery[size];  
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
	}
	
}
