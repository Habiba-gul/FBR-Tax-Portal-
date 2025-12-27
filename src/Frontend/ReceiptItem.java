package Frontend;

public class ReceiptItem {
    private String description;
    private double price;
    private int quantity;
    private double tax;

    public ReceiptItem(String description, double price, int quantity, double tax) {
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.tax = tax;
    }

    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public double getTax() { return tax; }
}