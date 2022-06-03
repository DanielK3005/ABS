package dto;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import transaction.Loan;
import transaction.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PersonDTO {

    private String name;
    private double balance;
    List<Transaction> actions;
    List<LenderDTO> OwnLenders;
    List<LoanDTO> OwnLoans;
    private Integer TotalNewLoans;
    private Integer TotalPendingLoans;
    private Integer TotalActiveLoans;
    private Integer TotalRiskLoans;
    private Integer TotalFinishedLoans;
    private Integer TotalSumLoans;
    private Integer TotalNewLenders;
    private Integer TotalPendingLenders;
    private Integer TotalActiveLenders;
    private Integer TotalRiskLenders;
    private Integer TotalFinishedLenders;


    public PersonDTO(String name, double balance,List<Transaction> actions) {
        this.name = name;
        this.balance =balance;
        this.actions = new ArrayList<>(actions);
        this.OwnLenders=new ArrayList<>();
        this.OwnLoans=new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance=balance;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public List<Transaction> getActions() {
        return actions;
    }

    public void setActions(List<Transaction> actions) {
        this.actions = actions;
    }

    public Integer getTotalNewLoans() {
        return TotalNewLoans;
    }

    public void setTotalNewLoans(Integer totalNewLoans) {
        TotalNewLoans = totalNewLoans;
    }

    public Integer getTotalPendingLoans() {
        return TotalPendingLoans;
    }

    public void setTotalPendingLoans(Integer totalPendingLoans) {
        TotalPendingLoans = totalPendingLoans;
    }

    public Integer getTotalActiveLoans() {
        return TotalActiveLoans;
    }

    public void setTotalActiveLoans(Integer totalActiveLoans) {
        TotalActiveLoans = totalActiveLoans;
    }

    public Integer getTotalRiskLoans() {
        return TotalRiskLoans;
    }

    public void setTotalRiskLoans(Integer totalRiskLoans) {
        TotalRiskLoans = totalRiskLoans;
    }

    public Integer getTotalFinishedLoans() {
        return TotalFinishedLoans;
    }

    public void setTotalFinishedLoans(Integer totalFinishedLoans) {
        TotalFinishedLoans = totalFinishedLoans;
    }

    public Integer getTotalSumLoans() {
        return TotalSumLoans;
    }

    public void setTotalSumLoans(Integer totalSumLoans) {
        TotalSumLoans = totalSumLoans;
    }

    public Integer getTotalNewLenders() {
        return TotalNewLenders;
    }

    public void setTotalNewLenders(Integer totalNewLenders) {
        TotalNewLenders = totalNewLenders;
    }

    public Integer getTotalPendingLenders() {
        return TotalPendingLenders;
    }

    public void setTotalPendingLenders(Integer totalPendingLenders) {
        TotalPendingLenders = totalPendingLenders;
    }

    public Integer getTotalActiveLenders() {
        return TotalActiveLenders;
    }

    public void setTotalActiveLenders(Integer totalActiveLenders) {
        TotalActiveLenders = totalActiveLenders;
    }

    public Integer getTotalRiskLenders() {
        return TotalRiskLenders;
    }

    public void setTotalRiskLenders(Integer totalRiskLenders) {
        TotalRiskLenders = totalRiskLenders;
    }

    public Integer getTotalFinishedLenders() {
        return TotalFinishedLenders;
    }

    public void setTotalFinishedLenders(Integer totalFinishedLenders) {
        TotalFinishedLenders = totalFinishedLenders;
    }

    public List<LenderDTO> getOwnLenders() {
        return OwnLenders;
    }
    public void setLoansAndLendersList(List<LoanDTO> loans){
        List<LoanDTO> myLoans=loans.stream().filter(x->x.getOwner().getName().equalsIgnoreCase(this.getName())).collect(Collectors.toList());
        this.OwnLoans=myLoans;
        for (LoanDTO val:loans){
            this.OwnLenders.addAll(val.getLoanerList().stream().filter(x->x.getOwner().getName().equalsIgnoreCase(this.getName())).collect(Collectors.toList()));
        }
    }

    public List<LoanDTO> getOwnLoans() {
        return OwnLoans;
    }
}
