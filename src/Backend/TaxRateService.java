package Backend;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TaxRateService {
    private static TaxRateService instance;
    
    // List of observers to notify when rates change
    private List<TaxRateObserver> observers = new ArrayList<>();

    // --- Tax Rate Properties ---
    // These are now the "source of truth" that will be saved/loaded
    private double govtJobRate = 15.0;
    private double privateJobRate = 20.0;
    private double businessRate = 25.0;
    private double vehicleRate = 10.0;
    private double residentialRate = 10.0;
    private double commercialRate = 15.0;
    private double gstRate = 17.0;
    private double penaltyRate = 10.0;

    // --- Persistence ---
    private static final String RATES_FILE = "tax_rates.properties";

    /**
     * Private constructor for the Singleton pattern.
     * It now loads the rates from the file when the first instance is created.
     */
    private TaxRateService() {
        loadRates();
    }

    /**
     * Provides the global point of access to the single instance of TaxRateService.
     * @return The singleton instance of TaxRateService.
     */
    public static TaxRateService getInstance() {
        if (instance == null) {
            instance = new TaxRateService();
        }
        return instance;
    }

    /**
     * Loads tax rates from the properties file.
     * If the file doesn't exist or there's an error, it uses the default values.
     */
    private void loadRates() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(RATES_FILE)) {
            props.load(fis);
            System.out.println("Successfully loaded tax rates from " + RATES_FILE);
            // Read values from the file
            govtJobRate = Double.parseDouble(props.getProperty("govtJobRate", String.valueOf(govtJobRate)));
            privateJobRate = Double.parseDouble(props.getProperty("privateJobRate", String.valueOf(privateJobRate)));
            businessRate = Double.parseDouble(props.getProperty("businessRate", String.valueOf(businessRate)));
            vehicleRate = Double.parseDouble(props.getProperty("vehicleRate", String.valueOf(vehicleRate)));
            residentialRate = Double.parseDouble(props.getProperty("residentialRate", String.valueOf(residentialRate)));
            commercialRate = Double.parseDouble(props.getProperty("commercialRate", String.valueOf(commercialRate)));
            gstRate = Double.parseDouble(props.getProperty("gstRate", String.valueOf(gstRate)));
            penaltyRate = Double.parseDouble(props.getProperty("penaltyRate", String.valueOf(penaltyRate)));
        } catch (IOException e) {
            System.err.println("Could not load tax rates from " + RATES_FILE + ". Using default values.");
            // If the file doesn't exist, we can create it with the default values
            saveRates(); 
        }
    }

    /**
     * Saves the current tax rates to the properties file.
     * This method is called every time a rate is changed.
     */
    private void saveRates() {
        Properties props = new Properties();
        // Set the properties with current values
        props.setProperty("govtJobRate", String.valueOf(govtJobRate));
        props.setProperty("privateJobRate", String.valueOf(privateJobRate));
        props.setProperty("businessRate", String.valueOf(businessRate));
        props.setProperty("vehicleRate", String.valueOf(vehicleRate));
        props.setProperty("residentialRate", String.valueOf(residentialRate));
        props.setProperty("commercialRate", String.valueOf(commercialRate));
        props.setProperty("gstRate", String.valueOf(gstRate));
        props.setProperty("penaltyRate", String.valueOf(penaltyRate));

        try (FileOutputStream fos = new FileOutputStream(RATES_FILE)) {
            props.store(fos, "FBR Tax Portal - Tax Rates");
            System.out.println("Successfully saved tax rates to " + RATES_FILE);
        } catch (IOException e) {
            System.err.println("Error: Could not save tax rates to " + RATES_FILE);
            e.printStackTrace();
        }
    }


    // --- Observer Pattern Methods ---
    public void addObserver(TaxRateObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(TaxRateObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyRateChange() {
        for (TaxRateObserver observer : observers) {
            observer.onTaxRatesChanged();
        }
    }

    // --- Getters ---
    public double getGovtJobRate() { return govtJobRate; }
    public double getPrivateJobRate() { return privateJobRate; }
    public double getBusinessRate() { return businessRate; }
    public double getVehicleRate() { return vehicleRate; }
    public double getResidentialRate() { return residentialRate; }
    public double getCommercialRate() { return commercialRate; }
    public double getGstRate() { return gstRate; }
    public double getPenaltyRate() { return penaltyRate; }

    // --- Setters (Updated to save after each change) ---
    public void setGovtJobRate(double govtJobRate) { 
        this.govtJobRate = govtJobRate; 
        saveRates(); // Save to file immediately
    }
    public void setPrivateJobRate(double privateJobRate) { 
        this.privateJobRate = privateJobRate; 
        saveRates(); // Save to file immediately
    }
    public void setBusinessRate(double businessRate) { 
        this.businessRate = businessRate; 
        saveRates(); // Save to file immediately
    }
    public void setVehicleRate(double vehicleRate) { 
        this.vehicleRate = vehicleRate; 
        saveRates(); // Save to file immediately
    }
    public void setResidentialRate(double residentialRate) { 
        this.residentialRate = residentialRate; 
        saveRates(); // Save to file immediately
    }
    public void setCommercialRate(double commercialRate) { 
        this.commercialRate = commercialRate; 
        saveRates(); // Save to file immediately
    }
    public void setGstRate(double gstRate) { 
        this.gstRate = gstRate; 
        saveRates(); // Save to file immediately
    }
    public void setPenaltyRate(double penaltyRate) { 
        this.penaltyRate = penaltyRate; 
        saveRates(); // Save to file immediately
    }
}