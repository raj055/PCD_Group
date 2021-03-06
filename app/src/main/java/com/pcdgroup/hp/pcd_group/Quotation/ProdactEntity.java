package com.pcdgroup.hp.pcd_group.Quotation;

import java.io.Serializable;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name ProdactEntity
 * @description product list to get and set data
 */

public class ProdactEntity implements Serializable{

    private String title,thumbnailUrl,description;
    private int availableStock;
    private Integer price,minimum,hsncode,gst,stock,reorderlevel;
    private String AddedQuontity;

    public ProdactEntity(String name, String thumbnailUrl, Integer price, Integer quantity,Integer hsncode,Integer gst,String description,Integer stock,Integer reorderlevel, int availableStock) {
        this.title = name;
        this.price = price;
        this.minimum = quantity;
        this.hsncode= hsncode;
        this.gst= gst;
        this.stock=stock;
        this.description=description;
        this.reorderlevel=reorderlevel;
        this.thumbnailUrl = thumbnailUrl;
        this.availableStock = availableStock;
        this.AddedQuontity = "0";
    }

    public String getTitle() {
        return title;
    }
    public Integer getPrice() {return price;}
    public Integer getQuantity() {return minimum;}
    public Integer getHsncode() {return hsncode;}
    public Integer getGst() {return gst;}
    public  String getDescription() {return description;}
    public Integer getstock() {return stock;}
    public Integer getReorderlevel() {return reorderlevel;}
    public String getAddedQuontity() {return AddedQuontity;}

    public void setTitle(String name) {
        this.title = name;
    }
    public void setPrice(Integer price) {this.price = price;}
    public void setQuantity(Integer minimum) {this.minimum = minimum;}
    public void setHsncode(Integer hsncode) {this.hsncode = hsncode;}
    public void setGst(Integer gst) {this.gst = gst;}
    public  void setDescription (String description) { this.description= description ;}
    public void setStock(Integer stock) {this.stock = stock;}
    public void setReorderlevel(Integer reorderlevel) {this.reorderlevel = reorderlevel;}
    public String setAddedQuontity(String s) {return AddedQuontity;}

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
    public int getStock() {
        return availableStock;
    }
    public void setStock(int availableStock) {
        this.availableStock = availableStock;
    }

}
