package com.pop.model;

public class PopDto {
	private long id;
	private int type;
	private double longitude;
	private double latitude;
	private double altitude;
	private long userId;
	private String userName;
	private int model;
	private int isShowy;
	private String geoHash;



	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getAltitude() {
		return this.altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getModel() {
		return this.model;
	}

	public void setModel(int model) {
		this.model = model;
	}

	public int getIsShowy() {
		return this.isShowy;
	}

	public void setIsShowy(int isShowy) {
		this.isShowy = isShowy;
	}

	public String getGeoHash() {
		return this.geoHash;
	}

	public void setGeoHash(String geoHash) {
		this.geoHash = geoHash;
	}
}
