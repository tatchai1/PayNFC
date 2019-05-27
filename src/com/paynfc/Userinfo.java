package com.paynfc;


public class Userinfo {
	
	@com.google.gson.annotations.SerializedName("id")
	private String mId;
	
	@com.google.gson.annotations.SerializedName("username")
	private String mUsername;
	
	@com.google.gson.annotations.SerializedName("password")
	private String mPassword;	
	
	@com.google.gson.annotations.SerializedName("phone")
	private String mTel;		
	
	@com.google.gson.annotations.SerializedName("email")
	private String mEmail;	
	
	@com.google.gson.annotations.SerializedName("balance")
	private int mBalance;	
	
	@com.google.gson.annotations.SerializedName("status")
	private String mStatus;	
	
	public Userinfo() {
		// empty
	}
	
	public String getId() {
		return mId;
	}
	

	public String getUsername() {
		return mUsername;
	}
	
	public String getPassword() {
		return mPassword;
	}
	
	public String getTel() {
		return mTel;
	}

	
	public String getEmail() {
		return mEmail;
	}
	public int getBalance() {
		return mBalance;
	}

	public String getStatus() {
		return mStatus;
	}

	public Userinfo(String username, String password, 
			String tel,String email,int balance,String status) {
		this.setUsername(username);
		this.setPassword(password);
		this.setTel(tel);
		this.setEmail(email);
		this.setBalance(balance);
		this.setStatus(status);
	}
	
	public final void setUsername(String username) {
		mUsername = username;
	}
	
	public final void setPassword(String password) {
		mPassword = password;
	}
	

	public final void setTel(String tel) {
		mTel = tel;
	}
	
	public final void setEmail(String email) {
		mEmail = email;
	}
	
	public final void setBalance(int balance) {
		mBalance = balance;
	}
	public final void setStatus(String status) {
		mStatus = status;
	}
}
