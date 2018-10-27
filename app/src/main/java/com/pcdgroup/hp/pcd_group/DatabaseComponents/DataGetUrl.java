package com.pcdgroup.hp.pcd_group.DatabaseComponents;

/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name DataGetUrl
 * @description set name to link php file from database
 */

public enum DataGetUrl {

  USER_LOGIN,
  USER_REGISTER,
  OTP_REGISTER,

  GET_CLIENT_DETAILS,
  DELETE_MULTIPLE_CLIENT,
  CLIENT_LIST,
  CLIENT_REGISTER,
  EDIT_CLIENT,
  SINGLE_DELETE,

  VIEW_PRODUCT,
  DELETE_PRODUCTS,

  ACCESS_DETAILS,
  UPDATE_ACCESS_USER_DETAILS,

  ADD_BRAND,
  LIST_BRAND,

  DISCOUNT_DETAILS,

  ORDERLIST_DETAILS,

  ADD_DEALER,
  ADD_VENDOR,
  DEALER_LIST,
  ASSIGN_DEALER,
  VENDOR_LIST,

  PURCHASE_ORDER_DETAILS,
  COMPLETE_ORDER_DETAILS,
  VENDOR_PRODUCT_LIST,
  COMPLETE_ORDER,

  SHOW_CLIENT_QUOTATION_LIST,
  ALL_QUOTATION_LIST,
  UPDATE_BILL;

  private String[] urls = {

          // Godaddy Server File's

          "http://dert.co.in/gFiles/UserLogin.php",                 // USER_LOGIN
          "http://dert.co.in/gFiles/UserRegistration.php",          //USER_REGISTER
          "http://dert.co.in/gFiles/otp_registration_user.php",     //OTP_REGISTER

          "http://dert.co.in/gFiles/ClientDetails.php",             //GET_CLIENT_DETAILS    00
          "http://dert.co.in/gFiles/deletemultiple.php",            //DELETE_MULTIPLE_CLIENT
          "http://dert.co.in/gFiles/DataClient.php",                //CLIENT_LIST
          "http://dert.co.in/gFiles/ClientRegister.php",            //CLIENT_REGISTER
          "http://dert.co.in/gFiles/updateclientdetails.php",       //EDIT_CLIENT
          "http://dert.co.in/gFiles/deleteclient.php",              //SINGLE_DELETE

          "http://dert.co.in/gFiles/fimage.php",                    //VIEW_PRODUCT    00
          "http://dert.co.in/gFiles/deleteproduct.php",             //DELETE_PRODUCTS

          "http://dert.co.in/gFiles/accessuserdetails.php",         // ACCESS_DETAILS 00
          "http://dert.co.in/gFiles/updateuserdetails.php",         //UPDATE_ACCEESS_USER_DETAILS

          "http://dert.co.in/gFiles/brandadd.php",                  //ADD_BRAND
          "http://dert.co.in/gFiles/listbrands.php",                //LIST_BRAND  00

          "http://dert.co.in/gFiles/DiscountDetails.php",           //DISCOUNT_DETAILS

          "http://dert.co.in/gFiles/orderlist.php",                 //ORDERLIST_DETAILS

          "http://dert.co.in/gFiles/dealer.php",                    //ADD_DEALER
          "http://dert.co.in/gFiles/vendorRegister.php",            //ADD_VENDOR
          "http://dert.co.in/gFiles/dealerlist.php",                //DEALER_LIST
          "http://dert.co.in/gFiles/assigndealor.php",              //ASSIGN_DEALER
          "http://dert.co.in/gFiles/vendorlist.php",                //VENDOR_LIST

          "http://dert.co.in/gFiles/purchaseorder.php",             //PURCHASE_ORDER_DETAILS
          "http://dert.co.in/gFiles/completeorder.php",             //COMPLETE_ORDER_DETAILS
          "http://dert.co.in/gFiles/VendorProductList.php",         //VENDOR_PRODUCT_LIST
          "http://dert.co.in/gFiles/completeorder.php",             //COMPLETE_ORDER

          "http://dert.co.in/gFiles/getpdfs.php",                   //SHOW_CLIENT_QUOTATION_LIST
          "http://dert.co.in/gFiles/QuotationList.php",             //ALL_QUOTATION_LIST
          "http://dert.co.in/gFiles/updatebill.php"                //UPDATE_BILL


          // Local Host File's

         /* "http://10.0.2.2/gFiles/UserLogin.php",                 // USER_LOGIN
          "http://10.0.2.2/gFiles/UserRegistration.php",          //USER_REGISTER
          "http://10.0.2.2/gFiles/otp_registration_user.php",     //OTP_REGISTER

          "http://10.0.2.2/gFiles/ClientDetails.php",             //GET_CLIENT_DETAILS    00
          "http://10.0.2.2/gFiles/deletemultiple.php",            //DELETE_MULTIPLE_CLIENT
          "http://10.0.2.2/gFiles/DataClient.php",                //CLIENT_LIST
          "http://10.0.2.2/gFiles/ClientRegister.php",            //CLIENT_REGISTER
          "http://10.0.2.2/gFiles/updateclientdetails.php",       //EDIT_CLIENT
          "http://10.0.2.2/gFiles/deleteclient.php",              //SINGLE_DELETE

          "http://10.0.2.2/gFiles/fimage.php",                    //VIEW_PRODUCT    00
          "http://10.0.2.2/gFiles/deleteproduct.php",             //DELETE_PRODUCTS

          "http://10.0.2.2/gFiles/accessuserdetails.php",         // ACCESS_DETAILS 00
          "http://10.0.2.2/gFiles/updateuserdetails.php",         //UPDATE_ACCEESS_USER_DETAILS

          "http://10.0.2.2/gFiles/brandadd.php",                  //ADD_BRAND
          "http://10.0.2.2/gFiles/listbrands.php",                //LIST_BRAND  00

          "http://10.0.2.2/gFiles/DiscountDetails.php",           //DISCOUNT_DETAILS

          "http://10.0.2.2/gFiles/orderlist.php",                 //ORDERLIST_DETAILS

          "http://10.0.2.2/gFiles/dealer.php",                    //ADD_DEALER
          "http://10.0.2.2/gFiles/vendorRegister.php",            //ADD_VENDOR
          "http://10.0.2.2/gFiles/dealerlist.php",                //DEALER_LIST
          "http://10.0.2.2/gFiles/assigndealor.php",              //ASSIGN_DEALER
          "http://10.0.2.2/gFiles/vendorlist.php",                //VENDOR_LIST

          "http://10.0.2.2/gFiles/purchaseorder.php",             //PURCHASE_ORDER_DETAILS
          "http://10.0.2.2/gFiles/completeorder.php",             //COMPLETE_ORDER_DETAILS
          "http://10.0.2.2/gFiles/VendorProductList.php",         //VENDOR_PRODUCT_LIST
          "http://10.0.2.2/gFiles/completeorder.php",             //COMPLETE_ORDER

          "http://10.0.2.2/gFiles/getpdfs.php",                   //SHOW_CLIENT_QUOTATION_LIST
          "http://10.0.2.2/gFiles/QuotationList.php",             //ALL_QUOTATION_LIST
          "http://10.0.2.2/gFiles/updatebill.php"                //UPDATE_BILL*/
  };

  public String getUrl(DataGetUrl getUrl){

    //Get the relevant Url for the Data.
    if(getUrl != null){
      return urls[getUrl.ordinal()];
    }
    else {
      return null;
    }
  }
}
