package Backend;

public class PropertyTaxCalculator extends TaxCalculator {
    @Override
    public double calculateTaxForCategory(String subCategory, double... prices) {
        double total = 0;
        String cat = "property_" + subCategory.toLowerCase();
        System.out.println("Calculating property tax for subCategory: " + subCategory + ", cat: " + cat); // Debug
        for (double price : prices) {
            double tax = calculateForAmount(cat, price);
            System.out.println("Price: " + price + ", Calculated tax: " + tax); // Debug
            total += tax;
        }
        return total;
    }

    private double calculateForAmount(String category, double amount) {
        System.out.println("Checking amount " + amount + " for category " + category); // Debug
        for (TaxRange r : service.getRanges(category)) {
            System.out.println("Range: " + r.getMinAmount() + " - " + r.getMaxAmount() + ", Rate: " + r.getRate()); // Debug
            if (amount >= r.getMinAmount() && amount <= r.getMaxAmount()) {
                return amount * (r.getRate() / 100.0);
            }
        }
        System.out.println("No matching range for amount " + amount + " in " + category + " - tax 0"); // Debug
        return 0;
    }
}