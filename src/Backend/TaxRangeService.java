package Backend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaxRangeService {
    private static TaxRangeService instance;
    private TaxRangeDAO dao = new TaxRangeDAO(); // aggregation

    private TaxRangeService() {}

    public static TaxRangeService getInstance() {
        if (instance == null) instance = new TaxRangeService();
        return instance;
    }

    public ObservableList<TaxRange> getRanges(String category) {
        return FXCollections.observableArrayList(dao.getRangesByCategory(category));
    }

    public boolean addRange(String category, double min, double max, double rate) {
        return dao.addRange(category, min, max, rate);
    }

    public boolean updateRange(TaxRange range) {
        return dao.updateRange(range.getId(), range.getMinAmount(), range.getMaxAmount(), range.getRate());
    }

    public boolean deleteRange(TaxRange range) {
        return dao.deleteRange(range.getId());
    }
}