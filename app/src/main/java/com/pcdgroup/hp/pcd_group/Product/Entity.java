package com.pcdgroup.hp.pcd_group.Product;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name Entity
 * @description product item name to set and get
 */

public class Entity {
  
  String  id, title,thumbnailUrl,description,brand,price,minimum,hsncode,gst,stock,reorderlevel;

  public Entity(String name, String thumbnailUrl, String price, String gst, String minimum, String hsncode,
                String brand, String description, String stock, String reorderlevel,
                String id) {
    this.id = id;
    this.title = name;
    this.price = price;
    this.minimum = minimum;
    this.hsncode= hsncode;
    this.gst=gst;
    this.brand= brand;
    this.stock=stock;
    this.description=description;
    this.reorderlevel=reorderlevel;
    this.thumbnailUrl = thumbnailUrl;
  }

  public String getId() {

    return id;
  }

  public void setId(String id) {

    this.id = id;
  }

  public String getTitle() {

    return title;
  }

  public void setTitle(String name) {

    this.title = name;
  }

  public String getPrice() {

    return price;
  }

  public void setPrice(String price) {

    this.price = price;
  }

  public String getMinimum() {

    return minimum;
  }

  public void setMinimum(String minimum) {

    this.minimum = minimum;
  }

  public String getHsncode() {

    return hsncode;
  }

  public void setHsncode(String hsncode) {

    this.hsncode = hsncode;
  }

  public  String getGst() {

    return  gst;
  }

  public  void setGst(String gst){

    this.gst=gst;
  }

  public String getBrand() {

    return brand;
  }

  public void setBrand(String brand) {

    this.brand = brand;
  }

  public  String getDescription() {

    return description;
  }

  public  void setDescription (String description) {

    this.description= description ;
  }

  public String getstock() {

    return stock;
  }

  public void setStock(String stock) {

    this.stock = stock;
  }

  public String getReorderlevel() {

    return reorderlevel;
  }

  public void setReorderlevel(String reorderlevel) {

    this.reorderlevel = reorderlevel;
  }

  public String getThumbnailUrl() {

    return thumbnailUrl;
  }

  public void setThumbnailUrl(String thumbnailUrl) {

    this.thumbnailUrl = thumbnailUrl;
  }

}
