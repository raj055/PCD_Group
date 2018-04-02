package com.pcdgroup.hp.pcd_group.Product;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class Entity {

  private String title,thumbnailUrl,description,brand;
  private int availableStock;
  private Integer price,minimum,hsncode,gst,stock,reorderlevel;

  public Entity(String name, String thumbnailUrl, Integer price, Integer gst, Integer minimum, Integer hsncode,
                String brand, String description, Integer stock, Integer reorderlevel,
                int availableStock) {
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
    this.availableStock = availableStock;
  }

  public String getTitle() {
    return title;
  }
  public Integer getPrice() {return price;}
  public Integer getMinimum() {return minimum;}
  public Integer getHsncode() {return hsncode;}
  public  Integer getGst() {return  gst;}
  public String getBrand() {return brand;}
  public  String getDescription() {return description;}
  public Integer getstock() {return stock;}
  public Integer getReorderlevel() {return reorderlevel;}

  public void setTitle(String name) {
    this.title = name;
  }
  public void setPrice(Integer price) {this.price = price;}
  public void setMinimum(Integer minimum) {this.minimum = minimum;}
  public void setHsncode(Integer hsncode) {this.hsncode = hsncode;}
  public  void setGst(Integer gst){this.gst=gst;}
  public void setBrand(String brand) {this.brand = brand;}
  public  void setDescription (String description) { this.description= description ;}
  public void setStock(Integer stock) {this.stock = stock;}
  public void setReorderlevel(Integer reorderlevel) {this.reorderlevel = reorderlevel;}

  public String getThumbnailUrl() {
    return thumbnailUrl;
  }
  public String gettitle() {return title;}

  public void setThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }
  public void settitle(String title) {
    this.title = title;
  }
  public void setprice(Integer price) {
    this.price = price;
  }

  public int getStock() {
    return availableStock;
  }
  public void setStock(int availableStock) {
    this.availableStock = availableStock;
  }
}
