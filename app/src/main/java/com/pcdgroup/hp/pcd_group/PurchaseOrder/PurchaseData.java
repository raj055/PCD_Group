package com.pcdgroup.hp.pcd_group.PurchaseOrder;

public class PurchaseData {

    private String id;
    private String url;
    private String email;
    private String purchaseorder;
    private String completeorder;

    public PurchaseData(String id, String urlname, String email, String purchaseorder, String completeorder) {
        this.id = id;
        this.url = urlname;
        this.email = email;
        this.purchaseorder = purchaseorder;
        this.completeorder = completeorder;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail(){return email;}

    public void setEmail(String email){this.email = email;}


    public String getPurchaseorder(){return purchaseorder;}

    public void setPurchaseorder(String purchaseorder){this.purchaseorder = purchaseorder;}

    public String getCompleteorder(){return completeorder;}

    public void setCompleteorder(String completeorder){this.completeorder = completeorder;}
}
