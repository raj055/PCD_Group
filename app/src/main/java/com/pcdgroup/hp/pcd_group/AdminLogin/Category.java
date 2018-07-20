package com.pcdgroup.hp.pcd_group.AdminLogin;


/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name Category
 * @description Holds all the entities (related data) of client / users
 */

public class Category {

	String name;
	String address;
	String address1;
	String address2;
	String pincode;
	String state;
	String mobileno;
	String email;
	String website;
	String pan;
	String gst;

	public Category(String name , String address, String address1, String address2, String pincode,
					String state, String mobileno, String email, String website, String pan, String gst){

		this.name = name;
		this.address = address;
		this.address1 = address1;
		this.address2 = address2;
		this.pincode = pincode;
		this.state = state;
		this.mobileno = mobileno;
		this.email = email;
		this.website = website;
		this.pan = pan;
		this.gst = gst;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return this.address;
	}

	public void setAddress1(String address1){
		this.address1 = address1;
	}

	public String getAddress1(){
		return this.address1;
	}

	public void setAddress2(String address2){
		this.address2 = address2;
	}

	public String getAddress2(){
		return this.address2;
	}

	public void setPincode(String pincode){
		this.pincode = pincode;
	}

	public String getPincode(){
		return this.pincode;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return this.state;
	}

	public void setMobileno(String mobileno){
		this.mobileno = mobileno;
	}

	public String getMobileno(){
		return this.mobileno;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return this.email;
	}

	public void setWebsite(String website){
		this.website = website;
	}

	public String getWebsite(){
		return this.website;
	}

	public void setPan(String pan){
		this.pan = pan;
	}

	public String getPan(){
		return this.pan;
	}

	public void setGst(String gst){
		this.gst = gst;
	}

	public String getGst(){
		return this.gst;
	}
}
