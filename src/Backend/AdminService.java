package Backend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.stream.Collectors;

public class AdminService {
    // Acts as our temporary database
    private ObservableList<User> allUsers = FXCollections.observableArrayList();

    public AdminService() {
        // Initialize with baseTax included (CNIC, Name, Status, BaseTax, Penalty)
        allUsers.add(new User("42201-1111111-1", "Ahmed Ali", "Unpaid", 50000.0, 5000.0));
        allUsers.add(new User("35202-2222222-2", "Zainab Khan", "Paid", 75000.0, 0.0));
        allUsers.add(new User("61101-3333333-3", "Omar Farooq", "Unpaid", 25000.0, 2500.0));
    }

    public ObservableList<User> getAllUsers() {
        return allUsers;
    }

    public ObservableList<User> getDefaulterList() {
        return allUsers.stream()
                .filter(u -> u.getStatus().equalsIgnoreCase("Unpaid"))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    // Logic for the 10% penalty
    public void applyTenPercentPenalty(User user) {
        if (user != null && user.getStatus().equalsIgnoreCase("Unpaid")) {
            double newPenalty = user.getPenalty() + (user.getBaseTax() * 0.10);
            user.setPenalty(newPenalty);
        }
    }
}