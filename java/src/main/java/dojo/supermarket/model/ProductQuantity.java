package dojo.supermarket.model;

public class ProductQuantity { // Name is not good, should be CartItem

    private final Product product;
    private final double quantity;
    // Should take a discount as a parameter
    // private final Discount discount;

    public ProductQuantity(Product product, double weight) { // "weight" is not a good name, should be "quantity"
    // should take a discount as a parameter
        this.product = product;
        this.quantity = weight;
        // this.discount = discount;
    }

    public Product getProduct() {
        return product;
    }

    public double getQuantity() {
        return quantity;
    }
    // getter for discount
}
