package com.pcdgroup.hp.pcd_group.Global;

import com.pcdgroup.hp.pcd_group.Quotation.ProductInfoAdapter;

import java.util.ArrayList;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class GlobalVariable {

    //Declare the class singleton class
    private static GlobalVariable myObj;
    public static GlobalVariable getInstance(){
        if(myObj == null){
            myObj = new GlobalVariable();
        }
        return myObj;
    }

    public String currentUserEmail;

    public  String[] globalClient = new String[8];

    public  String[] globalProduct = new String[5];

    public  String[] globalBarnd = new String[11];

    public String AccessType;

    public String DiscountType;

    public String[] GlobalImageUpload = new String[10];

    public String[] GlobalImageSingleRecord = new String[10];
}
