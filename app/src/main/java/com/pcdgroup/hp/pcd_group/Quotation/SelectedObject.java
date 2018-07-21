package com.pcdgroup.hp.pcd_group.Quotation;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class SelectedObject implements Parcelable,Serializable {

    public  ArrayList<String> address;
    public ArrayList<String> brandAddress;
    public ArrayList<ProdactEntity> productSelected;
    public ArrayList<String> quantityList;
    public int Transportation;
    public int DiscountValue;

    public SelectedObject() {

       address = new ArrayList<String>();
       brandAddress = new ArrayList<String>();
       productSelected = new ArrayList<ProdactEntity>();
       quantityList = new ArrayList<String>();

    }

    protected SelectedObject(Parcel in) {
        address = in.createStringArrayList();
        brandAddress = in.createStringArrayList();
        quantityList = in.createStringArrayList();
        Transportation = in.readInt();
        DiscountValue = in.readInt();
    }

    public static final Creator<SelectedObject> CREATOR = new Creator<SelectedObject>() {
        @Override
        public SelectedObject createFromParcel(Parcel in) {
            return new SelectedObject(in);
        }

        @Override
        public SelectedObject[] newArray(int size) {
            return new SelectedObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(address);
        dest.writeStringList(brandAddress);
        dest.writeStringList(quantityList);
        dest.writeInt(Transportation);
        dest.writeInt(DiscountValue);
    }
}
