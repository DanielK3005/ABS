package transaction;

import customer.Person;

import java.util.ArrayList;
import java.util.List;

public class Lender implements Cloneable{
    private Person owner;
    private double amount;
    private double totalPayback;
    private double debt;
    private double payEveryYazPeriod;
    private int intrest;
    List<Payment> paymentlog;
    private double OwnershipPercent;

    public Lender(Person owner, double money, int intrest){
        this.owner=owner;
        this.amount=money;
        this.paymentlog=new ArrayList<>();
        this.intrest=intrest;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Lender newLender =null;
        newLender =(Lender)super.clone();
        newLender.setOwner(new Person(this.getOwner().getName(),this.getOwner().getBalance()));
        newLender.paymentlog=new ArrayList<>();
        newLender.paymentlog.addAll(this.paymentlog);
        return newLender;
    }

    public double getAmount() {
        return amount;
    }
    public double leftToPay() {
        if(this.getTotalAmountToPay() -totalPayback<0)
            return 0;
        else
            return this.getTotalAmountToPay() -totalPayback;
    }

    public double getTotalAmountToPay(){
        double add=amount*((double) intrest/100);
        return amount + add;
    }
    public double TotalInterest(){
        double result=amount*((double) intrest/100);
        return result;
    }

    public List<Payment> getPaymentlog() {
        return paymentlog;
    }

    public double TotalInterestPaidSoFar(){
        return this.paymentlog.stream().filter(x-> x.isPaidSuccesfully()==true).mapToDouble(x-> x.getInterestOnePay()).sum();
    }

    public void setPaymentlog(Payment payment) {
        this.paymentlog.add(payment);
    }

    public void MakeAllPaymentsPaid(){
        for(Payment val: this.paymentlog){
            val.setPaidSuccesfully(true);
        }
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotalPayback() {
        return totalPayback;
    }

    public void setTotalPayback(double totalPayback) {
        this.totalPayback = totalPayback;
    }
    @Override
    public String toString() {
        return "Lender Details: "+this.owner+", invest: "+String.format("%,.1f",this.amount);
    }

    public double getPayEveryYazPeriod() {
        return payEveryYazPeriod;
    }

    public void setPayEveryYazPeriod(int totalyaz, int payEveryyaz) {
        double payEveryOneYaz=this.amount/totalyaz;
        this.payEveryYazPeriod=payEveryOneYaz*payEveryyaz;
    }
    public double onePayment(){
        double oneintrest = payEveryYazPeriod * ((double) intrest / 100);
        return payEveryYazPeriod + oneintrest;
    }

    public double getOwnershipPercent() {
        return OwnershipPercent;
    }

    public void setOwnershipPercent(int capital) {
        OwnershipPercent = ((this.amount/capital)*100);
    }
}
