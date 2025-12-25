package Backend;

/**
 * The Observer interface for the TaxRateService.
 * Any class that wants to be notified of tax rate changes must implement this interface.
 */
public interface TaxRateObserver {
    /**
     * This method is called by the TaxRateService whenever a tax rate is updated.
     * Implementing classes should define their logic here for reacting to the change.
     */
    void onTaxRatesChanged();
}