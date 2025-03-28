import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RentalModel {
    private final StringProperty garden;
    private final StringProperty startDate;
    private final StringProperty endDate;

    public RentalModel(String garden, String startDate, String endDate) {
        this.garden = new SimpleStringProperty(garden);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
    }

    // Getter ve Setter metodları

    public String getGarden() {
        return garden.get();
    }

    public void setGarden(String garden) {
        this.garden.set(garden);
    }

    public StringProperty gardenProperty() {
        return garden;
    }

    public String getStartDate() {
        return startDate.get();
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public StringProperty startDateProperty() {
        return startDate;
    }

    public String getEndDate() {
        return endDate.get();
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
    }

    public StringProperty endDateProperty() {
        return endDate;
    }
}