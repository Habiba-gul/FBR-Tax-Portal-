package Backend;

import java.time.LocalDate;

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

    public String getGender() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGender'");
    }

    public LocalDate getDob() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDob'");
    }

    public String getEmail() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEmail'");
    }

    public String getAddress() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAddress'");
    }

    public String getPhone() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPhone'");
    }

    public void setName(String text) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setName'");
    }

    public void setDob(LocalDate value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDob'");
    }

    public void setGender(String value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setGender'");
    }

    public void setAddress(String text) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setAddress'");
    }

    public void setEmail(String text) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEmail'");
    }

    public void setPhone(String text) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPhone'");
    }
}