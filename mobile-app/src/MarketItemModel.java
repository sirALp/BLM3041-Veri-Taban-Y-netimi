public class MarketItemModel {
    private String itemId;
    private String itemName;
    private int price;
    private int quantity;
    private String sellerId;
    private boolean is_active;

    public MarketItemModel(String itemId ,String itemName, int price, int quantity, String sellerId, boolean is_active) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.sellerId = sellerId;
        this.is_active = is_active;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSellerId() {
        return sellerId;
    }

    public boolean isActive() {
        return is_active;
    }
}

