package com.pcdgroup.hp.pcd_group.PurchaseOrder;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name ProductData
 * @description product data to set and get data
 */

public class ProductData {

    private String id,title,thumbnailUrl,description,price,minimum,hsncode,gst,stock,reorderlevel;

    public ProductData() {
        this.id = id;
        this.title = title;
        this.price = price;
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
    public String getHsncode() {return hsncode;}
    public String getGst() {return gst;}
    public  String getDescription() {return description;}

    public String setId(String id) {
        this.id = id;
        return id;
    }
    public void setTitle(String name) {
        this.title = name;
    }
    public void setPrice(String price) {this.price = price;}
    public void setHsncode(String hsncode) {this.hsncode = hsncode;}
    public void setGst(String gst) {this.gst = gst;}
    public  void setDescription (String description) { this.description= description ;}

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
