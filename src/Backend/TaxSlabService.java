package Backend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaxSlabService {

    private static final ObservableList<TaxSlab> slabs =
            FXCollections.observableArrayList();

    static {
        // Income Tax – Private Job
        slabs.add(new TaxSlab("Income Tax", "Private Job", 0, 600000, 0));
        slabs.add(new TaxSlab("Income Tax", "Private Job", 600001, 1200000, 5));

        // Income Tax – Government
        slabs.add(new TaxSlab("Income Tax", "Government Job", 0, 800000, 0));

        // Business
        slabs.add(new TaxSlab("Income Tax", "Business", 0, 1000000, 10));

        // Property Tax
        slabs.add(new TaxSlab("Property Tax", "Residential", 0, 0, 5));
        slabs.add(new TaxSlab("Property Tax", "Commercial", 0, 0, 10));

        // Vehicle Tax
        slabs.add(new TaxSlab("Vehicle Tax", "Below 1000cc", 0, 0, 2));
        slabs.add(new TaxSlab("Vehicle Tax", "Above 1000cc", 0, 0, 5));

        // GST
        slabs.add(new TaxSlab("GST", "General", 0, 0, 18));
    }

    public ObservableList<TaxSlab> getAllSlabs() {
        return slabs;
    }
}
