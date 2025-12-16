package Backend;

/**
 * The User class represents a taxpayer in the system.
 * It follows the JavaBeans naming convention so that JavaFX 
 * can automatically link the data to the TableView.
 */
public class User {
    private String cnic;
    private String name;
    private String status;
    private double baseTax;   // The original tax amount 💰
    private double penalty;   // Any additional fines ⚖️

    // This constructor now accepts 5 arguments to match your AdminService logic
    public User(String cnic, String name, String status, double baseTax, double penalty) {
        this.cnic = cnic;
        this.name = name;
        this.status = status;
        this.baseTax = baseTax;
        this.penalty = penalty;
    }

    // --- Getters (Human-named for JavaFX compatibility) ---

    public String getCnic() {
        return cnic;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public double getBaseTax() {
        return baseTax;
    }

    public double getPenalty() {
        return penalty;
    }

    // --- Setters (Used by the Admin for Overrides and Penalties) ---

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }
}