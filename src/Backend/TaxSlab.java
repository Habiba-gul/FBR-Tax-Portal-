package Backend;

public class TaxSlab {
    private String taxType;
    private String category;
    private double minAmount;
    private double maxAmount;
    private double rate;

    public TaxSlab(String taxType, String category, double minAmount, double maxAmount, double rate) {
        this.taxType = taxType;
        this.category = category;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.rate = rate;
    }

    public String getTaxType() { return taxType; }
    public void setTaxType(String taxType) { this.taxType = taxType; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getMinAmount() { return minAmount; }
    public void setMinAmount(double minAmount) { this.minAmount = minAmount; }
    public double getMaxAmount() { return maxAmount; }
    public void setMaxAmount(double maxAmount) { this.maxAmount = maxAmount; }
    public double getRate() { return rate; }
    public void setRate(double rate) { this.rate = rate; }

    public double calculateTax(double amount) {
        if (amount >= minAmount && amount <= maxAmount) {
            return amount * (rate / 100.0);
        }
        return 0.0;
    }
}