import java.time.LocalDate;

public class salesModel {
    private String saleId;       // Satış ID'si
    private String itemId;       // Satılan ürünün ID'si
    private String buyerId;      // Alıcının kullanıcı ID'si
    private int quantitySold;    // Satılan miktar
    private LocalDate transactionDate; // İşlem tarihi

    public salesModel(String saleId, String itemId, String buyerId, int quantitySold, LocalDate transactionDate) {
        this.saleId = saleId;
        this.itemId = itemId;
        this.buyerId = buyerId;
        this.quantitySold = quantitySold;
        this.transactionDate = transactionDate;
    }

    // Getter ve Setter'lar
    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

}