package Backend;

public class TaxRange extends TaxSlab {
    private int id;

    // Constructor matching what DAO uses
    public TaxRange(int id, String category, double minAmount, double maxAmount, double rate) {
        super("Tax", category, minAmount, maxAmount, rate);  // taxType can be anything, not used
        this.id = id;
    }

    // Optional: Keep a no-arg or other constructors if needed
    public TaxRange() {
        super("", "", 0, 0, 0);
    }

    public int getId() { 
        return id; 
    }

    public void setId(int id) { 
        this.id = id; 
    }
}