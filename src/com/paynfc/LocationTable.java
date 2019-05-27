package com.paynfc;

public class LocationTable {
	@com.google.gson.annotations.SerializedName("id")
	private String mId;
	
	@com.google.gson.annotations.SerializedName("longitude")
	private double mLongitude;
	
	@com.google.gson.annotations.SerializedName("latitude")
	private double mLatitude;

	@com.google.gson.annotations.SerializedName("nameboat")
	private String mNameboat;
	
	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public double getmLongitude() {
		return mLongitude;
	}

	public void setmLongitude(double mLongitude) {
		this.mLongitude = mLongitude;
	}

	public double getmLatitude() {
		return mLatitude;
	}

	public void setmLatitude(double mLatitude) {
		this.mLatitude = mLatitude;
	}

	public String getmNameboat() {
		return mNameboat;
	}

	public void setmNameboat(String mNameboat) {
		this.mNameboat = mNameboat;
	}	
}
