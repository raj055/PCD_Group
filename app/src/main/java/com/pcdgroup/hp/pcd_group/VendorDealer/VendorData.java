package com.pcdgroup.hp.pcd_group.VendorDealer;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name VendorData
 * @description vendor data show  to get and set data
 */

public class VendorData {

    private String id;
    private String name;
    private String address;
    private String location;
    private String state;
    private String email;
    private String mobileno;
    private String organisation;
    private String gst;
    private String products;

    public VendorData(String id, String name, String address, String location, String state,
                      String email, String mobileno, String organisation, String gst, String products) {

        this.id = id;
        this.name = name;
        this.address = address;
        this.location = location;
        this.state = state;
        this.email = email;
        this.mobileno = mobileno;
        this.organisation = organisation;
        this.gst = gst;
        this.products = products;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail(){return email;}

    public void setEmail(String email){this.email = email;}

}
