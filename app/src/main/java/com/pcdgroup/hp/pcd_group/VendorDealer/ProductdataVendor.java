package com.pcdgroup.hp.pcd_group.VendorDealer;

public class ProductdataVendor {

    private String id,title,thumbnailUrl,description,price,minimum,hsncode,gst,stock,reorderlevel;

    public ProductdataVendor(String id, String name, String thumbnailUrl, String price, String quantity,String hsncode,String gst,String description,String stock,String reorderlevel) {
        this.id = id;
        this.title = name;
        this.price = price;
        this.minimum = quantity;
        this.hsncode= hsncode;
        this.gst= gst;
        this.stock=stock;
        this.description=description;
        this.reorderlevel=reorderlevel;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getId() {
        return id;
    }
    public String getTitle() { return title; }
    public String getPrice() {return price;}
    public String getQuantity() {return minimum;}
    public String getHsncode() {return hsncode;}
    public String getGst() {return gst;}
    public  String getDescription() {return description;}
    public String getstock() {return stock;}
    public String getReorderlevel() {return reorderlevel;}

    public void setId(String id) {
        this.id = id;
    }
    public void setTitle(String name) {
        this.title = name;
    }
    public void setPrice(String price) {this.price = price;}
    public void setQuantity(String minimum) {this.minimum = minimum;}
    public void setHsncode(String hsncode) {this.hsncode = hsncode;}
    public void setGst(String gst) {this.gst = gst;}
    public  void setDescription (String description) { this.description= description ;}


    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    public String gettitle() {
        return title;
    }
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
    public void settitle(String title) {
        this.title = title;
    }
    public void setprice(String price) {
        this.price = price;
    }

}
