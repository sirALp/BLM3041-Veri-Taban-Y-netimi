public class ticketModel {
    private String ticket_id;
    private String equipmentName;
    private int quantity;
    private String status;

    public ticketModel(String ticked_id ,String equipmentName, int quantity, String status) {
        this.ticket_id = ticket_id;
        this.equipmentName = equipmentName;
        this.quantity = quantity;
        this.status = status;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}