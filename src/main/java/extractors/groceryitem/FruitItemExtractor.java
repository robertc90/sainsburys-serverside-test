package extractors.groceryitem;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.GroceryItem;
import model.SainsburyItem;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Extracts/scrapes information on Sainsbury fruit products
 */
public class FruitItemExtractor implements GroceryItemExtractor {

    private Gson gson;

    /**
     * Default Constructor
     *
     * @param gson the gson object to use
     */
    public FruitItemExtractor(final Gson gson) {
        this.gson = gson;
    }

    @Override
    public List<? extends SainsburyItem> getItemsFromDocument(final Document requestedDocument) {
        List<String> itemUrls = this.getItemUrlsFromDocument(requestedDocument);
        return this.getGroceryItems(itemUrls);
    }

    /**
     * Creates a list of strings that contain product urls to scrape
     *
     * @param requestedDocument the document to retrieve urls from
     * @return a list of valid item urls
     */
    private List<String> getItemUrlsFromDocument(final Document requestedDocument) {
        Elements productElements = requestedDocument.select(".productNameAndPromotions");
        return productElements.stream().map(x -> x.getElementsByTag("a").first()).map(link -> link.attr("abs:href")).collect(Collectors.toList());
    }

    /**
     * Creates GroceryItems using Jsoup's document retrieval. Each document
     * retrieved from a url is converted to a valid grocery item containing
     * requested information.
     *
     * @param itemUrls the itemUrls to retrieve documents on
     * @return a list of GroceryItems that have been retrieved
     */
    private List<GroceryItem> getGroceryItems(final List<String> itemUrls) {
        List<GroceryItem> groceryItems = new ArrayList<>();

        itemUrls.forEach(itemUrl -> {

            JsonObject jsonObject = new JsonObject();
            if (!StringUtil.isBlank(itemUrl)) {
                try {
                    final Document document = Jsoup.connect(itemUrl).get();
                    Elements productSummary = document.select(".productSummary");

                    jsonObject.addProperty("title", this.getTitle(productSummary));
                    jsonObject.addProperty("unit_price", this.getUnitPrice(productSummary));

                    int calories = this.getCalories(document);
                    if (calories != -1) {
                        jsonObject.addProperty("kcal_per_100g", calories);
                    }

                    jsonObject.addProperty("description", this.getDescription(document));

                    groceryItems.add(gson.fromJson(jsonObject, GroceryItem.class));

                } catch (Exception ex) {
                    System.out.println("Unable to parse item");
                }
            }
        });

        return groceryItems;
    }

    /**
     * Attempts to get the products title
     *
     * @param productSummary the productSummary element from the main document
     * @return the products title
     */
    private String getTitle(final Elements productSummary) {
        return productSummary.select("h1").first().text();
    }

    /**
     * Attempts to get the products title
     *
     * @param document the main document to retrieve calorie information from
     * @return the products calorie information if available
     */
    private int getCalories(final Document document) {
        Element productTable = document.select("#information").select("table").first();
        if (productTable != null) {
            Optional<Element> kcalElement = productTable.select("tr").stream().filter(tr -> tr.select("td").text().contains("kcal")).findFirst();
            if (kcalElement.isPresent()) {
                return Integer.parseInt(kcalElement.get().children().first().text().replaceAll("[^0-9]", ""));
            }
        }

        return -1;
    }

    /**
     * Uses regex to filter out anything but numbers and decimal points to get a products unit price
     *
     * @param productSummary the productsSummary element
     * @return a double containing a products price
     */
    private double getUnitPrice(final Elements productSummary) {
        return Double.parseDouble(productSummary.select(".pricePerUnit").first().text().replaceAll("[^0-9.]", ""));
    }

    /**
     * Attempts to get the products description
     *
     * @param document the document to search on
     * @return an items product description
     */
    private String getDescription(final Document document) {
        return document.select(".productText").first().text();
    }

}
