package lu.uni.cityhunter.datastructure;

import com.google.android.gms.maps.model.LatLng;

public abstract class Challenge {

	private String title;
	private String description;
	private int coverPicture;
	private LatLng location;
	private ChallengeState state;
	private int nrOfTries;
	
	public final static String CHALLENGE_PAR_KEY = "lu.uni.challenge.par";
	
	public Challenge(String title, String description, int coverPicture, LatLng location, ChallengeState state, int nrOfTries) {
		this.title = title;
		this.description = description;
		this.coverPicture = coverPicture;
		this.location = location;
		this.state = state;
		this.nrOfTries = nrOfTries;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
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
	
	public void setState(ChallengeState state) {
		this.state = state;
	}
	
	public ChallengeState getState() {
		return this.state;
	}
	
	public void setNrOfTries(int nrOfTries) {
		this.nrOfTries = nrOfTries;
	}
	
	public int getNrOfTries() {
		return this.nrOfTries;
	}
	
}
