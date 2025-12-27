package Backend;

public class PropertyTaxCalculator extends TaxCalculator {
    @Override
    public double calculateTaxForCategory(String subCategory, double... prices) {
        double total = 0;
        String cat = "property_" + subCategory.toLowerCase();
        for (double price : prices) {
            total += calculateForAmount(cat, price);
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