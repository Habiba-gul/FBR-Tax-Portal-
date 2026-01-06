package Backend;

public class User {
    private String cnic;
    private String name;
    private String status;
    private double penalty;
    private double baseTax;

    public User(String cnic, String name, String status, double penalty) {
        this.cnic = cnic;
        this.name = name;
        this.status = status;
        this.penalty = penalty;
    }

    // Getters and Setters
    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    public double getBaseTax() {
        return baseTax;
    }

    public void setBaseTax(double baseTax) {
        this.baseTax = baseTax;
    }
}