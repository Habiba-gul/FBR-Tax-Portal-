package Backend;

public class User {
    private int id;           // NEW: user ID
    private String cnic;
    private String name;
    private String status;
    private double penalty;

    // Constructor with ID
    public User(int id, String name, String cnic, String status, double penalty) {
        this.id = id;
        this.name = name;
        this.cnic = cnic;
        this.status = status;
        this.penalty = penalty;
    }

    // Constructor without ID (optional)
    public User(String name, String cnic, String status, double penalty) {
        this.name = name;
        this.cnic = cnic;
        this.status = status;
        this.penalty = penalty;
    }

    // GETTERS AND SETTERS
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCnic() { return cnic; }
    public void setCnic(String cnic) { this.cnic = cnic; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getPenalty() { return penalty; }
    public void setPenalty(double penalty) { this.penalty = penalty; }
}
