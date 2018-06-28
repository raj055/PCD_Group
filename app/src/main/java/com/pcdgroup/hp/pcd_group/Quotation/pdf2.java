package com.pcdgroup.hp.pcd_group.Quotation;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name pdf2
 * @description pdf to set and get name
 */

public class pdf2 {

    private String id;
    private String url;
    private String name;
    private String billed;
    private String email;

    private String purchaseorder;
    private String completeorder;

    public pdf2(String id, String name, String urlname, String email, String bill, String purchaseorder, String completeorder) {
        this.id = id;
        this.name = name;
        this.url = urlname;
        this.email = email;
        this.billed = bill;

        this.purchaseorder = purchaseorder;
        this.completeorder = completeorder;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBilled() {
        return billed;
    }

    public void setBilled(String billed) {
        this.billed = billed;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail(){return email;}

    public void setEmail(String email){this.email = email;}


    public String getPurchaseorder(){return purchaseorder;}

    public void setPurchaseorder(String purchaseorder){this.purchaseorder = purchaseorder;}

    public String getCompleteorder(){return completeorder;}

    public void setCompleteorder(String completeorder){this.completeorder = completeorder;}

}
