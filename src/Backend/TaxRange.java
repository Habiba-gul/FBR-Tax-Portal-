package Backend;

public class TaxRange extends TaxSlab {
    private int id;

    public TaxRange(int id, String taxType, String category, double minAmount, double maxAmount, double rate) {
        super(taxType, category, minAmount, maxAmount, rate);
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}