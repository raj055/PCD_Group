package com.pcdgroup.hp.pcd_group.Quotation;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name ProductInfoAdapter
 * @description product information get and set data
 */

public class ProductInfoAdapter {

  String name;
  String amount = "0";

  public String getName() {

    return name;
  }

  public void setName(String name) {

    this.name = name;
  }


  public String getAmount() {

    return amount;
  }

  public void setAmount(String amount) {

    this.amount = amount;
  }

}
