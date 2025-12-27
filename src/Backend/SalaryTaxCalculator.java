// SalaryTaxCalculator.java
package Backend;

public class SalaryTaxCalculator extends TaxCalculator {
    @Override
    public double calculateTaxForCategory(String subCategory, double... amounts) {
        double total = 0;
        String cat = "salary_" + subCategory.toLowerCase();
        for (double amount : amounts) {
            total += calculateForAmount(cat, amount);
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


