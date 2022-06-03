package dto;

public class PaymentDTO {
    private int currentyaz;
    private double interestOnePay;
    private double onePayment;
    private boolean paidSuccesfully;
    private double totalOnePayment;

    public PaymentDTO(int currentyaz, double interestOnePay, double onePayment, boolean paidSuccesfully,double totalOnePayment) {
        this.currentyaz = currentyaz;
        this.interestOnePay = interestOnePay;
        this.onePayment = onePayment;
        this.paidSuccesfully = paidSuccesfully;
        this.totalOnePayment=totalOnePayment;
    }

    public double getTotalOnePayment() {
        return totalOnePayment;
    }

    public void setTotalOnePayment(double totalOnePayment) {
        this.totalOnePayment = totalOnePayment;
    }

    public int getCurrentyaz() {
        return currentyaz;
    }

    public void setCurrentyaz(int currentyaz) {
        this.currentyaz = currentyaz;
    }

    public double getInterestOnePay() {
        return interestOnePay;
    }

    public void setInterestOnePay(double interestOnePay) {
        this.interestOnePay = interestOnePay;
    }

    public double getOnePayment() {
        return onePayment;
    }

    public void setOnePayment(double onePayment) {
        this.onePayment = onePayment;
    }

    public boolean isPaidSuccesfully() {
        return paidSuccesfully;
    }

    public void setPaidSuccesfully(boolean paidSuccesfully) {
        this.paidSuccesfully = paidSuccesfully;
    }

    @Override
    public String toString() {
        return "Payment yaz: "+ this.currentyaz +", Payment amount: " + String.format("%,.1f",this.onePayment) + ", interest: "+ String.format("%,.1f",this.interestOnePay) +
                ", Total of this payment: "+ String.format("%,.1f",this.getTotalOnePayment()) + ", payment got paid: " +(this.paidSuccesfully ? "yes": "no");
    }
}
