
import java.sql.Date;

public class MarketSaleModel {
    private String saleId;
    private String itemId;
    private String buyerId;
    private int quantitySold;
    private Date transactionDate;
    private String itemName;
    private int pricePerKg;
    private String buyerUsername;

    public MarketSaleModel(String saleId, String itemId, String buyerId, int quantitySold, Date transactionDate, String itemName, int pricePerKg, String buyerUsername) {
        this.saleId = saleId;
        this.itemId = itemId;
        this.buyerId = buyerId;
        this.quantitySold = quantitySold;
        this.transactionDate = transactionDate;
        this.itemName = itemName;
        this.pricePerKg = pricePerKg;
        this.buyerUsername = buyerUsername;
    }

    // Getter ve Setter metodları
    public String getSaleId() { return saleId; }
    public void setSaleId(String saleId) { this.saleId = saleId; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getBuyerId() { return buyerId; }
    public void setBuyerId(String buyerId) { this.buyerId = buyerId; }

    public int getQuantitySold() { return quantitySold; }
    public void setQuantitySold(int quantitySold) { this.quantitySold = quantitySold; }

    public Date getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public int getPricePerKg() { return pricePerKg; }
    public void setPricePerKg(int pricePerKg) { this.pricePerKg = pricePerKg; }

    public String getBuyerUsername() { return buyerUsername; }
    public void setBuyerUsername(String buyerUsername) { this.buyerUsername = buyerUsername; }
}