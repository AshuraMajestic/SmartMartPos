package com.example.scannercollege.Domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Product implements Parcelable {
    String name;
    String price;
    String quantity;
    String barcode;

    public Product(){};

    public Product(String name, String price, String quantity, String barcode) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.barcode = barcode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    protected Product(Parcel in) {
        name = in.readString();
        price = in.readString();
        quantity = in.readString();
        barcode = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(quantity);
        parcel.writeString(barcode);
    }
}
