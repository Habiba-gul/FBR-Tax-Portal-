package Backend;

public class TaxSlab {
    private String taxType;
    private String category;
    private double minAmount;
    private double maxAmount;
    private double rate;

    public TaxSlab(String taxType, String category,
                   double minAmount, double maxAmount, double rate) {
        this.taxType = taxType;
        this.category = category;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.rate = rate;
    }

    public String getTaxType() { return taxType; }
    public String getCategory() { return category; }
    public double getMinAmount() { return minAmount; }
    public double getMaxAmount() { return maxAmount; }
    public double getRate() { return rate; }

    public void setRate(double rate) { this.rate = rate; }
}
