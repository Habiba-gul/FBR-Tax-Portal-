package Backend;

import java.util.ArrayList;
import java.util.List;
import Frontend.ReceiptItem;  // Import from Frontend

public class SystemManager {
    
    private static UserInfo currentUser;
    private static double totalTax = 0.0;
    private static List<ReceiptItem> receiptItems = new ArrayList<>();

    public static UserInfo getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UserInfo user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static double getTotalTax() {
        return totalTax;
    }

    public static void setTotalTax(double tax) {
        totalTax = tax;
    }

    public static List<ReceiptItem> getReceiptItems() {
        return new ArrayList<>(receiptItems);  // Safe copy
    }

    public static void setReceiptItems(List<ReceiptItem> items) {
        receiptItems.clear();
        if (items != null) {
            receiptItems.addAll(items);
        }
    }

    public static void clearReceipt() {
        totalTax = 0.0;
        receiptItems.clear();
    }
}