package Backend;

public class VehicleTaxCalculator extends TaxCalculator {
    @Override
    public double calculateTaxForCategory(String ignored, double... prices) {
        double total = 0;
        for (double price : prices) {
            total += calculateForAmount("vehicle", price);
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