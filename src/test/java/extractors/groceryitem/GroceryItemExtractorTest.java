package extractors.groceryitem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.GroceryItem;
import model.SainsburyItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * Inorder to keep testing simple I have decided to make external calls -
 * Future development we would want to mock so that Unit tests are not
 * dependent on external services
 */
public class GroceryItemExtractorTest {

    private FruitItemExtractor groceryItemExtractor;

    private String testUrl = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
    private String invalidTestUrl = "https://www.google.com/";

    @BeforeEach
    void init() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        groceryItemExtractor = new FruitItemExtractor(gson);
    }

    @Test
    @DisplayName("Asserts that a full document list is returned from the test URL and contains valid elements")
    public void assertPrintItemDetailsFromUrlIsValid() throws IOException {
        Document testDocument = Jsoup.connect(testUrl).get();

        List<? extends SainsburyItem> sainsburyItems = groceryItemExtractor.getItemsFromDocument(testDocument);

        Assertions.assertTrue(this.isItemsValidType(sainsburyItems));
        Assertions.assertEquals(17, sainsburyItems.size());
        this.assertItemsHaveRequiredFields(sainsburyItems);
        this.assertResultsFilterOutCrossSell(sainsburyItems);
    }

    @Test
    @DisplayName("Asserts that when retrieving from a url that does not have the required information, the application does not error")
    public void assertPrintItemDetailsFromUrl() throws IOException {
        Document testDocument = Jsoup.connect(invalidTestUrl).get();

        List<? extends SainsburyItem> sainsburyItems = groceryItemExtractor.getItemsFromDocument(testDocument);
        Assertions.assertEquals(0, sainsburyItems.size());
    }

    /**
     * Utility test method to make sure all returned items are of type Grocery
     *
     * @param sainsburyItems the sainsburyItems returned to check
     * @return TRUE if all items are valid, FALSE otherwise
     */
    private boolean isItemsValidType(final List<? extends SainsburyItem> sainsburyItems) {
        boolean validItem = true;
        if (sainsburyItems != null) {
            for (Object object : sainsburyItems) {
                if (!(object instanceof GroceryItem)) {
                    validItem = false;
                    break;
                }
            }
        }

        return validItem;
    }


    /**
     * Utility test method to make sure all items have the required minimum fields
     *
     * @param sainsburyItems the sainsburyItems returned to check
     */
    private void assertItemsHaveRequiredFields(final List<? extends SainsburyItem> sainsburyItems) {
        @SuppressWarnings("unchecked")
        List<GroceryItem> groceryItems = (List<GroceryItem>) sainsburyItems;

        groceryItems.forEach(groceryItem -> {
            Assertions.assertTrue(!StringUtils.isBlank(groceryItem.getDescription()));
            Assertions.assertTrue(!StringUtils.isBlank(groceryItem.getTitle()));
            Assertions.assertTrue(groceryItem.getUnitPrice() != 0);
        });

    }

    /**
     * Utility test method to make sure all returned items do not contain
     * cross sell items (Using Klip here as the provided example)
     *
     * @param sainsburyItems the sainsburyItems returned to check
     */
    private void assertResultsFilterOutCrossSell(final List<? extends SainsburyItem> sainsburyItems) {
        @SuppressWarnings("unchecked")
        List<GroceryItem> groceryItems = (List<GroceryItem>) sainsburyItems;
        groceryItems.forEach(groceryItem -> Assertions.assertTrue(!groceryItem.getTitle().contains("Klip")));
    }

}
