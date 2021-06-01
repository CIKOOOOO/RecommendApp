package com.yosua.recommendapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {

    // Nama produk
    private String name;

    private String vendorName;

    private double price;

    private float rate;

    public Data() {
    }

    public Data(String name, String vendorName, double price, float rate) {
        this.name = name;
        this.vendorName = vendorName;
        this.price = price;
        this.rate = rate;
    }

    protected Data(Parcel in) {
        name = in.readString();
        vendorName = in.readString();
        price = in.readDouble();
        rate = in.readFloat();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public void setData(Data data){
        this.name = data.getName();
        this.vendorName = data.getVendorName();
        this.price = data.getPrice();
        this.rate = data.getRate();
    }

    public String getName() {
        return name;
    }

    public String getVendorName() {
        return vendorName;
    }

    public double getPrice() {
        return price;
    }

    public float getRate() {
        return rate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(vendorName);
        parcel.writeDouble(price);
        parcel.writeFloat(rate);
    }
}
