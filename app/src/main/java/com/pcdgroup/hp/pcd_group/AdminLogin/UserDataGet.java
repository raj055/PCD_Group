package com.pcdgroup.hp.pcd_group.AdminLogin;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name UserDataGet
 * @description email and access type get and set
 */

public class UserDataGet {

    private String Email;
    private String AccessType = "";

    public UserDataGet(String emial){

        this.Email = emial;
    }

    public String getEmail(){return Email;}

    public void setEmail(String email){this.Email = email;}

    public void setAccessType(String access){this.AccessType = access;}

    public String getAccessType(){return AccessType;}
}
