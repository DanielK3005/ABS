package transaction;

public class Payment {
    private int currentyaz;
    private double interestOnePay;
    private double onePayment;
    private boolean paidSuccesfully;
    private double paymentTotal;

    public Payment(double onePayment,double interest, int currentyaz,boolean paidSuccesfully){
        this.onePayment=onePayment;
        this.interestOnePay=interest;
        this.currentyaz=currentyaz;
        this.paidSuccesfully=paidSuccesfully;
    }

    public int getCurrentyaz() {
        return currentyaz;
    }

    public double getOnePayment() {
        return onePayment;
    }

    public double getInterestOnePay() {
        return interestOnePay;
    }

    public boolean isPaidSuccesfully() {
        return paidSuccesfully;
    }

    public void setOnePayment(double onePayment) {
        this.onePayment = onePayment;
    }

    public void setCurrentyaz(int currentyaz) {
        this.currentyaz = currentyaz;
    }

    public void setInterestOnePay(double interestOnePay) {
        this.interestOnePay = interestOnePay;
    }

    public double getPaymentTotal() {
        return paymentTotal;
    }

    public void setPaymentTotal(double paymentTotal) {
        this.paymentTotal = paymentTotal;
    }

    public void setPaidSuccesfully(boolean paidSuccesfully) {
        this.paidSuccesfully = paidSuccesfully;
    }
    public double getTotalOnePayment(){
        paymentTotal=onePayment+interestOnePay;
        return paymentTotal;
    }

    @Override
    public String toString() {
        return "Payment yaz: "+ this.currentyaz +", Payment amount: " + String.format("%,.1f",this.onePayment) + ", interest: "+ String.format("%,.1f",this.interestOnePay) +
                ", Total of this payment: "+ String.format("%,.1f",this.getTotalOnePayment()) + ", payment got paid: " +(this.paidSuccesfully ? "yes": "no");
    }
}
