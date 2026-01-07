package Frontend;

public class ReceiptRow {

    private String taxType;
    private String category;
    private double taxPercent;
    private double originalPrice;
    private double amount;  // Tax amount deducted
    private String date;
    private double penalty;

    public ReceiptRow(String taxType, String category, double taxPercent, double originalPrice,
                      double amount, String date, double penalty) {
        this.taxType = taxType;
        this.category = category;
        this.taxPercent = taxPercent;
        this.originalPrice = originalPrice;
        this.amount = amount;
        this.date = date;
        this.penalty = penalty;
    }

    public String getTaxType() { return taxType; }
    public String getCategory() { return category; }
    public double getTaxPercent() { return taxPercent; }
    public double getOriginalPrice() { return originalPrice; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public double getPenalty() { return penalty; }
}