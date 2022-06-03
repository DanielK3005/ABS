package dto;


import javafx.scene.control.Hyperlink;

import java.util.ArrayList;
import java.util.List;

public class LenderDTO {
    private PersonDTO owner;
    private PersonDTO lendTo;
    private double amount;
    private double debt;
    private double payEveryYazPeriod;
    List<PaymentDTO> paymentlog;
    Hyperlink showPayment;
    private double totalPayBack;
    private double leftToPay;
    private double totalInterest;
    private double totalInterestPaidSoFar;
    private double OwnershipPercent;

    public LenderDTO(double amount, double debt, double payEveryYazPeriod,PersonDTO LendTo) {
        this.amount = amount;
        this.debt = debt;
        this.payEveryYazPeriod = payEveryYazPeriod;
        this.paymentlog=new ArrayList<>();
        this.showPayment=new Hyperlink("Show");
        this.lendTo=LendTo;
    }

    public double getOwnershipPercent() {
        return OwnershipPercent;
    }

    public void setOwnershipPercent(double ownershipPercent) {
        OwnershipPercent = ownershipPercent;
    }

    public void setPaymentlog(List<PaymentDTO> paymentlog) {
        this.paymentlog = paymentlog;
    }

    public Hyperlink getShowPayment() {
        return showPayment;
    }

    public void setShowPayment(Hyperlink showPayment) {
        this.showPayment = showPayment;
    }

    public PersonDTO getOwner() {
        return owner;
    }

    public void setOwner(PersonDTO owner) {
        this.owner = owner;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }

    public double getPayEveryYazPeriod() {
        return payEveryYazPeriod;
    }

    public void setPayEveryYazPeriod(double payEveryYazPeriod) {
        this.payEveryYazPeriod = payEveryYazPeriod;
    }


    public List<PaymentDTO> getPaymentlog() {
        return paymentlog;
    }

    public void setPaymentlog(PaymentDTO payment) {
        this.paymentlog.add(payment);
    }

    public double getTotalPayBack() {
        return totalPayBack;
    }

    public void setTotalPayBack(double totalPayBack) {
        this.totalPayBack = totalPayBack;
    }

    public double getLeftToPay() {
        return leftToPay;
    }

    public void setLeftToPay(double leftToPay) {
        this.leftToPay = leftToPay;
    }

    public double getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(double totalInterest) {
        this.totalInterest = totalInterest;
    }

    public double getTotalInterestPaidSoFar() {
        return totalInterestPaidSoFar;
    }

    public void setTotalInterestPaidSoFar(double totalInterestPaidSoFar) {
        this.totalInterestPaidSoFar = totalInterestPaidSoFar;
    }

    public PersonDTO getLendTo() {
        return lendTo;
    }

    public void setLendTo(PersonDTO lendTo) {
        this.lendTo = lendTo;
    }
}
