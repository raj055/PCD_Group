package com.pcdgroup.hp.pcd_group.DatabaseComponents;


/**
 * @author Grasp
 * @version 1.0 on 28-06-2018.
 * @class_name CallBackInterface
 * @description callback interface to execute query result
 */

public interface CallBackInterface {

  void ExecuteQueryResult(String response, DataGetUrl dataGetUrl);

}
