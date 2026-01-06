// SalaryTaxCalculator.java
package Backend;

public class SalaryTaxCalculator extends TaxCalculator {
    @Override
    public double calculateTaxForCategory(String subCategory, double... amounts) {
        String cat;
        switch (subCategory.toLowerCase()) {
            case "government":
                cat = "salary_gov";
                break;
            case "private":
                cat = "salary_private_company";
                break;
            case "business":
                cat = "salary_business";
                break;
            default:
                cat = "";
        }
        double total = 0;
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