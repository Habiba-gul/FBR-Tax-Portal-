package Backend;

public class GstTaxCalculator extends TaxCalculator {
    @Override
    public double calculateTaxForCategory(String ignored, double... amounts) {
        double total = 0;
        for (double amount : amounts) {
            total += calculateForAmount("gst", amount);
        }
        return total;
    }

    private double calculateForAmount(String category, double amount) {
        for (TaxRange r : service.getRanges(category)) {
            if (amount >= r.getMinAmount() && amount <= r.getMaxAmount()) {
                return amount * (r.getRate() / 100.0);
            }
        }
        return 0;
    }
}