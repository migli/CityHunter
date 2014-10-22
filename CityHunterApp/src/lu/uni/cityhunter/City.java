package lu.uni.cityhunter;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class City implements Parcelable {
	
	private String name;
	private String description;
	private ArrayList<Mistery> misteries;
	
	public City(String name, String description) {
		this.name = name;
		this.description = description;
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

	public static final Parcelable.Creator<City> CREATOR = new Creator<City>() {  
		  public City createFromParcel(Parcel source) {  
		      City city = new City(source.readString(), source.readString());
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
	}

}
