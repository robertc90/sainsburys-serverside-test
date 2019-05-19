package scraper;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import extractors.groceryitem.FruitItemExtractor;
import model.ItemType;
import model.SainsburyItem;
import model.TotalCost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to scrape data from a given url and print out related information
 * from the data.
 */
public class ProductWebScraper {

    private FruitItemExtractor fruitItemExtractor;
    private Gson gson;

    /**
     * Default Constructor
     */
    public ProductWebScraper() {
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        fruitItemExtractor = new FruitItemExtractor(gson);
    }

    /**
     * Scrapes and prints out item details from a given url. The itemType
     * will control what data is attempted to be scraped ontop of the common
     * properties from a SainsburysItem.
     *
     * @param requestedUrl the url to attempt to scrape
     * @param itemType the type of item to process
     */
    public void printItemDetailsFromRequestedUrl(final String requestedUrl, final ItemType itemType) {
        List<? extends SainsburyItem> sainsburyItems = new ArrayList<>();

        try {
            final Document requestedDocument = Jsoup.connect(requestedUrl).get();

            switch (itemType) {
                case Grocery:
                    sainsburyItems = fruitItemExtractor.getItemsFromDocument(requestedDocument);
                    break;

                default:
                    System.out.println("Unsupported Item");
                    break;
            }

            if (sainsburyItems != null && !sainsburyItems.isEmpty()) {
                final TotalCost totalCost = this.getTotalCost(sainsburyItems);
                final JsonObject sainsburysJsonObject = this.getJsonObject(sainsburyItems, totalCost);
                this.printToConsole(sainsburysJsonObject);
            }
        }
        catch (Exception ex) {
            System.out.println("Unable to process request");
        }
    }

    /**
     * Writes a pretty json string from a JsonObject to console
     *
     * @param jsonObject the jsonObject to write out
     */
    private void printToConsole(final JsonObject jsonObject) {
        System.out.println(gson.toJson(jsonObject));
    }

    /**
     * Creates a totalCost item containing the gross of the retrieved products
     * and the vat that has been applied.
     *
     * @param sainsburyItems the items to calculate cost for
     * @return a totalCost (gross + vat)
     */
    private TotalCost getTotalCost(final List<? extends SainsburyItem> sainsburyItems) {
        double gross = 0;

        for (SainsburyItem s : sainsburyItems) {
            gross += s.getUnitPrice();
        }

        return new TotalCost(gross);
    }

    /**
     * Creates a jsonObject containing sainsbury product information
     * that should have been scraped and gathered.
     *
     * @param sainsburyItems the sainsburyItems to write out
     * @param totalCost the object containing the gross and vat of the sainsbury items
     * @return a JsonObject containing the required fields (Item + Cost info)
     */
    private JsonObject getJsonObject(final List<? extends SainsburyItem> sainsburyItems, final TotalCost totalCost) {
        JsonObject SainsburyItemJsonObject = new JsonObject();

        JsonElement sainsburyItemJsonElement = gson.toJsonTree(sainsburyItems, new TypeToken<List<SainsburyItem>>() {}.getType());
        JsonArray sainsburyItemJsonArray = sainsburyItemJsonElement.getAsJsonArray();

        SainsburyItemJsonObject.add("results", sainsburyItemJsonArray);
        SainsburyItemJsonObject.add("total", gson.toJsonTree(totalCost));

        return SainsburyItemJsonObject;
    }

}
