package model;

import com.google.gson.annotations.SerializedName;

/**
 * Holds Specific GroceryItem information
 */
public class GroceryItem extends SainsburyItem {

    private String title;
    @SerializedName("kcal_per_100g")
    private Integer calories;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
