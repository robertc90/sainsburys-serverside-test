package model;

import com.google.gson.annotations.SerializedName;

/**
 * Holds common Sainsburys item information
 */
public class SainsburyItem {

    @SerializedName("unit_price")
    private double unitPrice;

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
