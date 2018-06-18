package com.pcdgroup.hp.pcd_group.DatabaseComponents;

/**
 * @author Grasp
 * @version 1.0 on 18-06-2018.
 */

public enum DataGetUrl {

  USER_LOGIN,
  USER_REGISTER,
  OTP_REGISTER,

  GET_CLIENT_DETAILS,//
  DELETE_MULTIPLE_CLIENT,
  CLIENT_LIST,
  CLIENT_REGISTER,
  EDIT_CLIENT,
  SINGLE_CLIENT,//
  SINGLE_DELETE,

  VIEW_PRODUCT,//
  DELETE_PRODUCTS,
  ADD_PRODUCTS,//
  EDIT_PRODUCTS,//
  SINGLE_PRODUCT,//

  ACCESS_DETAILS,//
  UPDATE_ACCESS_USER_DETAILS,

  ADD_BRAND,
  LIST_BRAND,//

  ACCESS_USER_DETAILS,//
  DISCOUNT_DETAILS,

  ORDERLIST_DETAILS,

  ADD_DEALER,
  ADD_VENDOR,
  DEALER_LISE,
  ASSIGN_DEALER,//
  VENDOR_LIST,//
  VENDOR_PRODUCT_LIST,//

  PURCHASE_ORDER_DETAILS,//
  COMPLETE_ORDER_DETAILS,//
  ADD_VENDOR_PRODUCT_LIST,//

  SELECT_BRAND_LIST,//
  SELECT_BRAND_QUOTATION_LIST,//
  SELECT_PRODUCT_QUOTATION_LIST,
  SELECT_CLIENT_QUOTATION_LIST,
  UPLOAD_TEXT_FILE_QUOTATION_LIST,//
  SHOW_CLIENT_QUOTATION_LIST,
  ALL_QUOTATION_LIST,//
  UPDATE_BILL;

  private String[] urls = {

          "http://dert.co.in/gFiles/UserLogin.php",                 // USER_LOGIN
          "http://dert.co.in/gFiles/UserRegistration.php",          //USER_REGISTER
          "http://dert.co.in/gFiles/otp_registration_user.php",     //OTP_REGISTER

          "http://dert.co.in/gFiles/ClientDetails.php",             //GET_CLIENT_DETAILS    00
          "http://dert.co.in/gFiles/deletemultiple.php",            //DELETE_MULTIPLE_CLIENT
          "http://dert.co.in/gFiles/DataClient.php",                //CLIENT_LIST
          "http://dert.co.in/gFiles/ClientRegister.php",            //CLIENT_REGISTER
          "http://dert.co.in/gFiles/updateclientdetails.php",       //EDIT_CLIENT
          "http://dert.co.in/gFiles/filterclientdata.php",          //SINGLE_CLIENT
          "http://dert.co.in/gFiles/deleteclient.php",              //SINGLE_DELETE

          "http://dert.co.in/gFiles/fimage.php",                    //VIEW_PRODUCT    00
          "http://dert.co.in/gFiles/deleteproduct.php",             //DELETE_PRODUCTS
          "http://dert.co.in/gFiles/upload.php",                    //ADD_PRODUCTS
          "http://dert.co.in/gFiles/updateproductdetails.php",      //EDIT_PRODUCTS
          "http://dert.co.in/gFiles/filterproductdata.php",         //SINGLE_PRODUCT

          "http://dert.co.in/gFiles/accessuserdetails.php",         // ACCESS_DETAILS
          "http://dert.co.in/gFiles/updateuserdetails.php",         //UPDATE_ACCEESS_USER_DETAILS

          "http://dert.co.in/gFiles/brandadd.php",                  //ADD_BRAND
          "http://dert.co.in/gFiles/listbrands.php",                //LIST_BRAND  00

          "http://dert.co.in/gFiles/accessuserdetails.php",         //ACCESS_USER_DETAILS
          "http://dert.co.in/gFiles/DiscountDetails.php",           //DISCOUNT_DETAILS

          "http://dert.co.in/gFiles/orderlist.php",                 //ORDERLIST_DETAILS

          "http://dert.co.in/gFiles/dealer.php",                    //ADD_DEALER
          "http://dert.co.in/gFiles/vendorRegister.php",            //ADD_VENDOR
          "http://dert.co.in/gFiles/dealerlist.php",                //DEALER_LISE
          "http://dert.co.in/gFiles/assigndealor.php",              //ASSIGN_DEALER
          "http://dert.co.in/gFiles/vendorlist.php",                //VENDOR_LIST
          "http://dert.co.in/gFiles/fimage.php",                    //ADD_VENDOR_PRODUCT_LIST   00

          "http://dert.co.in/gFiles/purchaseorder.php",             //PURCHASE_ORDER_DETAILS
          "http://dert.co.in/gFiles/completeorder.php",             //COMPLETE_ORDER_DETAILS
          "http://dert.co.in/gFiles/VendorProductList.php",         //VENDOR_PRODUCT_LIST
          //upload po link pending

          "http://dert.co.in/gFiles/listbrands.php",                //SELECT_BRAND_LIST   00
          "http://dert.co.in/gFiles/listbrands.php",                //SELECT_BRAND_QUOTATION_LIST   00
          "http://dert.co.in/gFiles/fimage.php",                    //SELECT_PRODUCT_QUOTATION_LIST   00
          "http://dert.co.in/gFiles/ClientDetails.php",             //SELECT_CLIENT_QUOTATION_LIST    00
          "http://dert.co.in/gFiles/uploadtxtfile.php",             //UPLOAD_TEXT_FILE_QUOTATION_LIST
          "http://dert.co.in/gFiles/getpdfs.php",                   //SHOW_CLIENT_QUOTATION_LIST
          "http://dert.co.in/gFiles/QuotationList.php",             //ALL_QUOTATION_LIST
          "http://dert.co.in/gFiles/updatebill.php",                //UPDATE_BILL
  };

  public String getUrl(DataGetUrl getUrl){

    //Get the relevant Url for the Data.
    if(getUrl != null) {
      return urls[getUrl.ordinal()];
    }
    else {
      return null;
    }

  }
}
