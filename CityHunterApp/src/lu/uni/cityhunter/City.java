package lu.uni.cityhunter;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class City implements Parcelable {
	
	private String name;
	private String description;
	private int previewPicture;
	private int coverPicture;
	private ArrayList<Mistery> misteries;
	
	protected final static String CITY_PAR_KEY = "lu.uni.city.par";
    protected final static String CITY_ARRAY_PAR_KEY = "lu.uni.cityarray.par"; 
	
	public City(String name, String description, int previewPicture, int coverPicture) {
		this.name = name;
		this.description = description;
		this.previewPicture = previewPicture;
		this.coverPicture = coverPicture;
		this.misteries = new ArrayList<Mistery>();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setPreviewPicture(int previewPicture) {
		this.previewPicture = previewPicture;
	}
	
	public int getPreviewPicture() {
		return this.previewPicture;
	}
	
	public void setCoverPicture(int coverPicture) {
		this.coverPicture = coverPicture;
	}

	public int getCoverPicture() {
		return this.coverPicture;
	}
	
	public void setMistery(Mistery mistery) {
		this.misteries.add(mistery);
	}
	
	public Mistery getMistery(int index) {
		return this.misteries.get(index);
	}
	
	public void removeMistery(Mistery mistery) {
		this.misteries.remove(mistery);
	}
	
	public ArrayList<Mistery> getMisteries() {
		return this.misteries;
	}
	
	public static final Parcelable.Creator<City> CREATOR = new Creator<City>() {  
		  public City createFromParcel(Parcel source) {  
		      City city = new City(source.readString(), source.readString(), source.readInt(), source.readInt());
		      return city;  
		  }  
		  public City[] newArray(int size) {  
		      return new City[size];  
		  }  
	};  
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeString(this.description); 
		dest.writeInt(this.previewPicture);
		dest.writeInt(this.coverPicture);
	}

}
