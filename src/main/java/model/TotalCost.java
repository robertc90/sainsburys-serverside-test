package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Holds data on total gross and VAT
 */
public class TotalCost {

    private static final double VAT_PERCENTAGE = 1.20;

    private double gross;
    private double vat;

    public TotalCost(double gross) {
        this.gross = gross;
        this.vat = this.calculateVat();
    }

    /**
     * Calculates vat at a preset percent
     *
     * @return the calculated vat
     */
    private double calculateVat() {
        vat = gross - this.gross / VAT_PERCENTAGE;
        return this.getRoundedVatValue();
    }

    /**
     * Rounds vat to two decimal places
     *
     * @return vat to two decimal places
     */
    private double getRoundedVatValue() {
        BigDecimal bd = new BigDecimal(this.vat);
        bd = bd.setScale(2, RoundingMode.HALF_DOWN);
        return bd.doubleValue();
    }

    public double getGross() {
        return gross;
    }

    public void setGross(double gross) {
        this.gross = gross;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

}
