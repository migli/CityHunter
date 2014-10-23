package lu.uni.cityhunter;

import android.os.Parcel;
import android.os.Parcelable;

public class Mistery implements Parcelable {
	
	private String title;
	private String question;
	private String answer;

	protected final static String MISTERY_PAR_KEY = "lu.uni.mistery.par";
	
	public Mistery(String title, String question, String answer) {
		this.title = title;
		this.question = question;
		this.answer = answer;
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

	public static final Parcelable.Creator<Mistery> CREATOR = new Creator<Mistery>() {  
		  public Mistery createFromParcel(Parcel source) {  
		      Mistery mistery = new Mistery(source.readString(), source.readString(), source.readString());
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
	}
	
}
