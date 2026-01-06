package Backend;

import java.time.LocalDateTime;

public class PaymentHistory {
    private int id;
    private LocalDateTime paymentDate;
    private double totalTax;
    private String details;

    public PaymentHistory(int id, LocalDateTime paymentDate, double totalTax, String details) {
        this.id = id;
        this.paymentDate = paymentDate;
        this.totalTax = totalTax;
        this.details = details;
    }

    // Getters
    public int getId() { return id; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public double getTotalTax() { return totalTax; }
    public String getDetails() { return details; }
}