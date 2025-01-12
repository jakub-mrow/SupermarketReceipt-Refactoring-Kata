package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private final List<ProductQuantity> items = new ArrayList<>(); 
    // Code smell: Duplicated code, may not sync with productQuantities
    // Solution: Remove the field and use the items field instead
    private final Map<Product, Double> productQuantities = new HashMap<>();

    List<ProductQuantity> getItems() {
        return Collections.unmodifiableList(items);
    }

    void addItem(Product product) { 
        // Method not used
        // Solution: Remove the method
        addItemQuantity(product, 1.0);
    }

    Map<Product, Double> productQuantities() {
        return Collections.unmodifiableMap(productQuantities);
        // Code smell: Duplicated code, may not sync with items
        // Solution: Remove the field and use the items field instead
    }

    public void addItemQuantity(Product product, double quantity) {
        // Code smell: Name does not describe what the method does
        // Solution: Rename the method to a more descriptive name for example "addCartItem"


        items.add(new ProductQuantity(product, quantity)); // have to detect if product is already in the list
        if (productQuantities.containsKey(product)) { // Code smell: Duplicated code, may not sync with items
            // Solution: Remove the field and use the items field instead
            productQuantities.put(product, productQuantities.get(product) + quantity);
        } else {
            productQuantities.put(product, quantity);
        }
    }

    //should implement total price calculation method

    void handleOffers(Receipt receipt, Map<Product, Offer> offers, SupermarketCatalog catalog) { 
        // Strategy pattern should be used here depending on the offer type
        // Method is too long
        // Discount should be applied on an item from a cart

        for (Product p: productQuantities().keySet()) {
            double quantity = productQuantities.get(p);
            if (offers.containsKey(p)) {
                Offer offer = offers.get(p);
                double unitPrice = catalog.getUnitPrice(p);
                int quantityAsInt = (int) quantity;
                Discount discount = null;
                int x = 1;
                if (offer.offerType == SpecialOfferType.THREE_FOR_TWO) {
                    x = 3;

                } else if (offer.offerType == SpecialOfferType.TWO_FOR_AMOUNT) {
                    x = 2;
                    if (quantityAsInt >= 2) {
                        double total = offer.argument * (quantityAsInt / x) + quantityAsInt % 2 * unitPrice;
                        double discountN = unitPrice * quantity - total;
                        discount = new Discount(p, "2 for " + offer.argument, -discountN);
                    }

                } if (offer.offerType == SpecialOfferType.FIVE_FOR_AMOUNT) {
                    x = 5;
                }
                int numberOfXs = quantityAsInt / x;
                if (offer.offerType == SpecialOfferType.THREE_FOR_TWO && quantityAsInt > 2) {
                    double discountAmount = quantity * unitPrice - ((numberOfXs * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
                    discount = new Discount(p, "3 for 2", -discountAmount);
                }
                if (offer.offerType == SpecialOfferType.TEN_PERCENT_DISCOUNT) {
                    discount = new Discount(p, offer.argument + "% off", -quantity * unitPrice * offer.argument / 100.0);
                }
                if (offer.offerType == SpecialOfferType.FIVE_FOR_AMOUNT && quantityAsInt >= 5) {
                    double discountTotal = unitPrice * quantity - (offer.argument * numberOfXs + quantityAsInt % 5 * unitPrice);
                    discount = new Discount(p, x + " for " + offer.argument, -discountTotal);
                }
                if (discount != null)
                    receipt.addDiscount(discount);
            }
        }
    }
}
