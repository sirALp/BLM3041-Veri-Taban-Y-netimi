public class GardenModel {
    private String gardenId;
    private String location;
    private String status;
    private int acres;
    private int monthly_price;

    public GardenModel(String gardenId, String location, String status, int acres, int monthly_price) {
        this.gardenId = gardenId;
        this.location = location;
        this.acres = acres;
        this.monthly_price = monthly_price;
        this.status = status;
    }

    // Getter ve Setter metodları

    public String getGardenId() {
        return gardenId;
    }

    public void setGardenId(String gardenId) {
        this.gardenId = gardenId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getAcres() {
        return acres;
    }

    public void setAcres(int acres) {
        this.acres = acres;
    }

    public int getPrice() {
        return monthly_price;
    }

    public void setPrice(int price) {
        this.monthly_price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}