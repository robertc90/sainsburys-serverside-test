package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TotalCostTest {

    @Test
    @DisplayName("Asserts that when creating a total cost item the correct calculation is made for gross and vat")
    public void assertTotalCostCalculationIsCorrect() {
        TotalCost totalCost = new TotalCost(5.0);
        Assertions.assertEquals(5.0, totalCost.getGross());
        Assertions.assertEquals(0.83, totalCost.getVat());
    }

}
