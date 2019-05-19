package extractors.groceryitem;

import model.SainsburyItem;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * Interface for common functionality shared across Extractors
 */
public interface GroceryItemExtractor {

    /**
     * Attempts to create a list of Sainsbury items from a given HTML Document
     *
     * @param requestedDocument the html document to create items from
     * @return a list of Sainsbury items
     */
    List<? extends SainsburyItem> getItemsFromDocument(final Document requestedDocument);

}
