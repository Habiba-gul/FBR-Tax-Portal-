package Backend;

public class TaxRateService {
    private static TaxRateService instance;

    private double salaryRate = 15.0;         // Default values
    private double businessRate = 20.0;
    private double rentalRate = 10.0;
    private double capitalGainsRate = 15.0;
    private double penaltyRate = 10.0;         // Used for late penalty

    private TaxRateService() {}

    public static TaxRateService getInstance() {
        if (instance == null) {
            instance = new TaxRateService();
        }
        return instance;
    }

    // Getters
    public double getSalaryRate() { return salaryRate; }
    public double getBusinessRate() { return businessRate; }
    public double getRentalRate() { return rentalRate; }
    public double getCapitalGainsRate() { return capitalGainsRate; }
    public double getPenaltyRate() { return penaltyRate; }

    // Setters
    public void setSalaryRate(double salaryRate) { this.salaryRate = salaryRate; }
    public void setBusinessRate(double businessRate) { this.businessRate = businessRate; }
    public void setRentalRate(double rentalRate) { this.rentalRate = rentalRate; }
    public void setCapitalGainsRate(double capitalGainsRate) { this.capitalGainsRate = capitalGainsRate; }
    public void setPenaltyRate(double penaltyRate) { this.penaltyRate = penaltyRate; }
}