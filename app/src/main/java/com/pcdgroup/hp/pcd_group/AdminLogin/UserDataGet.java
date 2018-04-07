package com.pcdgroup.hp.pcd_group.AdminLogin;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
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
