package dto;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import transaction.Loan;

import java.util.ArrayList;
import java.util.List;

public class LoanDTO {
    private String id;
    private PersonDTO owner;
    List<NotificationDTO> Notifications;
    private Loan.Status status;
    private String category;
    private int amount;
    private int totalYaz; //the amount of yaz to close the deal
    private int timeToPay; //number of yaz to pay
    private int activeYaz;//when the loan became active
    private int finishedYaz;
    List<LenderDTO> loanerList;
    Button showLoaner;
    private int interest;
    private int nextPaymentYaz;
    private double totalLoanerMoney;
    private double amountOfMoneyToMakeActive;
    private double totalOnePaymentToAll;
    private double fullDebt;
    private double totalPaidSoFar;
    private double totalAmountToPay;
    CheckBox picked;

    public LoanDTO(String id, Loan.Status status, String category, int amount, int totalYaz, int timeToPay, int activeYaz, int finishedYaz, int interest, List<NotificationDTO> notifications) {
        this.id = id;
        this.status = status;
        this.category = category;
        this.amount = amount;
        this.totalYaz = totalYaz;
        this.timeToPay = timeToPay;
        this.activeYaz = activeYaz;
        this.finishedYaz = finishedYaz;
        this.interest = interest;
        this.showLoaner=new Button("Show");
        this.loanerList=new ArrayList<>();
        this.picked=new CheckBox();
        this.Notifications=new ArrayList<>(notifications);
    }


    public PersonDTO getOwner() {
        return owner;
    }
    public LoanDTO copyLoan(){
      LoanDTO copy=new LoanDTO(this.id,this.status,this.category,this.amount,this.totalYaz,this.timeToPay,this.activeYaz,this.finishedYaz,this.interest,this.Notifications);
      copy.setTotalPaidSoFar(this.totalPaidSoFar);
      copy.setTotalAmountToPay(this.totalAmountToPay);
      copy.setTotalOnePaymentToAll(this.totalOnePaymentToAll);
      copy.setTotalLoanerMoney(this.totalLoanerMoney);
      copy.setFullDebt(this.fullDebt);
      copy.setOwner(this.owner);
      copy.setNextPaymentYaz(this.nextPaymentYaz);
      copy.setLoanerList(this.loanerList);
      return copy;
    }

    public double getTotalAmountToPay() {
        return totalAmountToPay;
    }

    public void setTotalAmountToPay(double totalAmountToPay) {
        this.totalAmountToPay = totalAmountToPay;
    }

    public List<LenderDTO> getLoanerList() {
        return loanerList;
    }

    public void setLoanerList(List<LenderDTO> loanerList) {
        this.loanerList = loanerList;
    }

    public Button getShowLoaner() {
        return showLoaner;
    }

    public void setShowLoaner(Button showLoaner) {
        this.showLoaner = showLoaner;
    }

    public String getId() {
        return id;
    }

    public void setOwner(PersonDTO owner) {
        this.owner = owner;
    }

    public void setLoanerList(LenderDTO loaner) {
        this.loanerList.add(loaner);
    }

    public void setId(String id) {
        this.id = id;
    }

    public Loan.Status getStatus() {
        return status;
    }

    public void setStatus(Loan.Status status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotalYaz() {
        return totalYaz;
    }

    public void setTotalYaz(int totalYaz) {
        this.totalYaz = totalYaz;
    }

    public int getTimeToPay() {
        return timeToPay;
    }

    public void setTimeToPay(int timeToPay) {
        this.timeToPay = timeToPay;
    }

    public int getActiveYaz() {
        return activeYaz;
    }

    public void setActiveYaz(int activeYaz) {
        this.activeYaz = activeYaz;
    }

    public int getFinishedYaz() {
        return finishedYaz;
    }

    public void setFinishedYaz(int finishedYaz) {
        this.finishedYaz = finishedYaz;
    }

    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }

    public int getNextPaymentYaz() {
        return nextPaymentYaz;
    }

    public void setNextPaymentYaz(int nextPaymentYaz) {
        this.nextPaymentYaz = nextPaymentYaz;
    }

    public double getTotalLoanerMoney() {
        return totalLoanerMoney;
    }

    public void setTotalLoanerMoney(double totalLoanerMoney) {
        this.totalLoanerMoney = totalLoanerMoney;
    }

    public double getAmountOfMoneyToMakeActive() {
        return amountOfMoneyToMakeActive;
    }

    public void setAmountOfMoneyToMakeActive(double amountOfMoneyToMakeActive) {
        this.amountOfMoneyToMakeActive = amountOfMoneyToMakeActive;
    }

    public double getTotalOnePaymentToAll() {
        return totalOnePaymentToAll;
    }

    public void setTotalOnePaymentToAll(double totalOnePaymentToAll) {
        this.totalOnePaymentToAll = totalOnePaymentToAll;
    }

    public double getFullDebt() {
        return fullDebt;
    }

    public void setFullDebt(double fullDebt) {
        this.fullDebt = fullDebt;
    }

    public CheckBox getPicked() {
        return picked;
    }

    public void setPicked(CheckBox picked) {
        this.picked = picked;
    }

    public double getTotalPaidSoFar() {
        return totalPaidSoFar;
    }

    public void setTotalPaidSoFar(double totalPaidSoFar) {
        this.totalPaidSoFar = totalPaidSoFar;
    }

    public void setNotification(NotificationDTO notifications) {
        Notifications.add(notifications);
    }

    public List<NotificationDTO> getNotifications() {
        return Notifications;
    }
}
