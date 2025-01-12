package dojo.supermarket.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Teller { 
    //Code smell: Class name does not describe what the class does
    //Solution: Rename the class to a more descriptive name for example "Checkout" or "Cashier"

    private final SupermarketCatalog catalog;
    private final Map<Product, Offer> offers = new HashMap<>(); //Code smell: Field name does not describe what the field does
    //Solution: Rename the field to a more descriptive name for example "productOffers" or "offersCatalog"
    //Offers should be handled by a separate class

    public Teller(SupermarketCatalog catalog) {
        this.catalog = catalog;
    }

    public void addSpecialOffer(SpecialOfferType offerType, Product product, double argument) { 
        //Code smell: Long Parameter List, "argument" is not descriptive
        //Solution: Replace parameter with a descriptive name or create a new class to hold the parameters
        offers.put(product, new Offer(offerType, product, argument));
        //Offers should be handled by a separate class, for example "OffersCatalog"
    }

    public Receipt checksOutArticlesFrom(ShoppingCart theCart) { 
        //Code smell: Method name does not describe what the method does
        //Solution: Rename the method to a more descriptive name for example "calculateTotal"
        Receipt receipt = new Receipt();
        // offers should be handled before getting items
        List<ProductQuantity> productQuantities = theCart.getItems();
        for (ProductQuantity pq: productQuantities) {
            Product p = pq.getProduct();
            double quantity = pq.getQuantity();
            double unitPrice = catalog.getUnitPrice(p);
            double price = quantity * unitPrice;
            receipt.addProduct(p, quantity, unitPrice, price);
        }
        theCart.handleOffers(receipt, offers, catalog); // unnecessary use, offers should be calculaded earlier in ShoppingCart

        return receipt; // should return cart instead of receipt
    }
}
