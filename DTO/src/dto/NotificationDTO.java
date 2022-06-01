package dto;

public class NotificationDTO {
    private String name;
    private int paymentYaz;
    private double totalPay;

    public NotificationDTO(String name, int paymentYaz, double totalPay) {
        this.name = name;
        this.paymentYaz = paymentYaz;
        this.totalPay = totalPay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPaymentYaz() {
        return paymentYaz;
    }

    public void setPaymentYaz(int paymentYaz) {
        this.paymentYaz = paymentYaz;
    }

    public double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(double totalPay) {
        this.totalPay = totalPay;
    }
}
