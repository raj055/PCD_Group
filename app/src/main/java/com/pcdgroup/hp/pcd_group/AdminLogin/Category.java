package com.pcdgroup.hp.pcd_group.AdminLogin;

import java.util.List;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class Category {

	private String name;

	
	public Category(String name){

		this.name = name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
	}
}
