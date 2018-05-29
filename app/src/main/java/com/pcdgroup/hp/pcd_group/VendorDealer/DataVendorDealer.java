package com.pcdgroup.hp.pcd_group.VendorDealer;

public class DataVendorDealer {

    String id;
    String name;
    String type;
    String address;
    String area;
    String mobileno;
    String state;
    String organisation;
    String gst;
    String emailid;
    String designation;

    public DataVendorDealer(String name, String type, Integer address, Integer area, Integer state, Integer gst, Integer emailid, Integer mobileno, String organisation, Integer id, Integer designation){
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

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    public String getArea() {

        return area;
    }

    public void setArea(String area) {

        this.area = area;
    }

    public void setMobileno(String mobileno) {

        this.mobileno = mobileno;
    }

    public String getMobileno() {

        return mobileno;
    }

    public void setState(String state) {

        this.state = state;
    }

    public String getState() {

        return state;
    }

    public void setOrganisation(String organisation) {

        this.organisation = organisation;
    }

    public String getOrganisation() {

        return organisation;
    }

    public void setGst(String gst) {

        this.gst = gst;
    }

    public String getGst() {

        return gst;
    }

    public void setEmailid(String emailid) {

        this.emailid = emailid;
    }

    public String getEmailid() {

        return emailid;
    }

    public void setDesignation(String designation) {

        this.designation = designation;
    }

    public String getDesignation() {

        return designation;
    }
}
