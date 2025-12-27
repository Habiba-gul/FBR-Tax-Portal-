package Backend;

public abstract class TaxCalculator {
    protected TaxRangeService service = TaxRangeService.getInstance();

    public abstract double calculateTaxForCategory(String subCategory, double... amounts);
}