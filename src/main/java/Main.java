import model.ItemType;
import scraper.ProductWebScraper;

/**
 * Application entry point - future iterations we would most likely want to pass in
 * the PAGE_URL into args and the item type
 */
public class Main {

    private static final String PAGE_URL = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";

    public static void main(String[] args) {
        final ProductWebScraper productWebScraper = new ProductWebScraper();
        productWebScraper.printItemDetailsFromRequestedUrl(PAGE_URL, ItemType.Grocery);
    }

}
