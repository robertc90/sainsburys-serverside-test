package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GroceryItemTest {

    private Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    private static final String EXPECTED_JSON = "{\n" +
            "  \"title\": \"Test Item\",\n" +
            "  \"kcal_per_100g\": 50,\n" +
            "  \"description\": \"Test Description\",\n" +
            "  \"unit_price\": 5.0\n" +
            "}";

    private static final String EXPECTED_JSON_NO_CALORIES = "{\n" +
            "  \"title\": \"Test Item\",\n" +
            "  \"description\": \"Test Description\",\n" +
            "  \"unit_price\": 5.0\n" +
            "}";

    @Test
    @DisplayName("Asserts that a Grocery Item uses the correct SerializedName's when converted to JSON")
    public void assertGroceryItemMapsToJson() {
        GroceryItem groceryItem = new GroceryItem();
        groceryItem.setTitle("Test Item");
        groceryItem.setDescription("Test Description");
        groceryItem.setCalories(50);
        groceryItem.setUnitPrice(5.0);

        String json = gson.toJson(groceryItem);
        Assertions.assertEquals(EXPECTED_JSON, json);
    }

    @Test
    @DisplayName("Asserts that a Grocery Item that doesn't have calorie information excludes that field from JSON")
    public void assertGroceryItemMapsToJsonWithoutCalories() {
        GroceryItem groceryItem = new GroceryItem();
        groceryItem.setTitle("Test Item");
        groceryItem.setDescription("Test Description");
        groceryItem.setUnitPrice(5.0);

        String json = gson.toJson(groceryItem);
        Assertions.assertEquals(EXPECTED_JSON_NO_CALORIES, json);
    }

}
