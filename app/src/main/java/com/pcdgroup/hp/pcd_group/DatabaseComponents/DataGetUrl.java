package com.pcdgroup.hp.pcd_group.DatabaseComponents;

/**
 *
 */

public enum DataGetUrl {

  GET_CLIENT_DETAILS,
  DELETE_MULTIPLE_CLIENT,
  CLIENT_LIST,
  CLIENT_REGISTER,
  DELETE_PRODUCTS,
  ADD_PRODUCTS,
  EDIT_PRODUCTS;

  private String[] urls = {

    "http://dert.co.in/gFiles/ClientDetails.php",
    "http://dert.co.in/gFiles/deletemultiple.php",
    "http://dert.co.in/gFiles/DataClient.php",
    "http://dert.co.in/gFiles/ClientRegister.php",
    "",


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
