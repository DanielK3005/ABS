package customer;


import dto.LenderDTO;
import exception.MoreThanYouGotException;
import transaction.Lender;
import transaction.Loan;
import transaction.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Person implements Cloneable{
    private String name;
    private double balance;
    List<Transaction> actions;

    public Person(String name, double balance){
        this.name=name;
        this.balance=balance;
        this.actions=new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Name: "+this.getName()+" , Current balance: "+ String.format("%,.1f",this.balance);
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        Person newPerson=null;
        newPerson=(Person)super.clone();
        newPerson.actions=new ArrayList<>();
        newPerson.actions.addAll(this.actions);
        return newPerson;
    }

    public void setAction(int currentyaz, Transaction.TransactionType type, double amount, double  before, double after) {
        this.actions.add(new Transaction(currentyaz,type,amount,before,after));
    }

    public List<Transaction> getActions() {
        return actions;
    }

    public void Withdraw(int amount,int currentyaz) throws MoreThanYouGotException {
        if(amount<=this.balance) {
            this.setAction(currentyaz, Transaction.TransactionType.WITHDRAW,amount, balance, balance - amount);
            balance = balance - amount;
        }
        else
            throw new MoreThanYouGotException(balance,amount);
    }

    public void Income(int amount,int currentyaz){
        this.setAction(currentyaz, Transaction.TransactionType.INCOME,amount,balance,balance+amount);
        balance=balance+amount;
    }

    //making an array with the number of total loans that belong to the customer in variety of statuses.
    //list array size will be: 5 (5 statuses).
    //index 0: total new loans
    //index 1: total pending loans
    //index 2: total active loans
    //index 3: total risk loans
    //index 4: total finished loans
    public Integer[] getTotalStatusCustomerLoans(List<Loan> data){
        Integer TotalSumsToLoans[];
        TotalSumsToLoans=new Integer[5];
        TotalSumsToLoans[0]=data.stream().filter(x->x.getOwner().equals(this)).filter(x->x.getStatus()== Loan.Status.NEW).collect(Collectors.toList()).size();
        TotalSumsToLoans[1]=data.stream().filter(x->x.getOwner().equals(this)).filter(x->x.getStatus()== Loan.Status.PENDING).collect(Collectors.toList()).size();
        TotalSumsToLoans[2]=data.stream().filter(x->x.getOwner().equals(this)).filter(x->x.getStatus()== Loan.Status.ACTIVE).collect(Collectors.toList()).size();
        TotalSumsToLoans[3]=data.stream().filter(x->x.getOwner().equals(this)).filter(x->x.getStatus()== Loan.Status.RISK).collect(Collectors.toList()).size();
        TotalSumsToLoans[4]=data.stream().filter(x->x.getOwner().equals(this)).filter(x->x.getStatus()== Loan.Status.FINISHED).collect(Collectors.toList()).size();

        return TotalSumsToLoans;
    }
    //making an array with the number of total loans that belong to the customer in variety of statuses.
    //list array size will be: 5 (5 statuses).
    //index 0: total new lenders
    //index 1: total pending lenders
    //index 2: total active lenders
    //index 3: total risk lenders
    //index 4: total finished lenders
    public Integer[] getTotalStatusCustomerLenders(List<Loan> data){
        List<Loan> StatusNew=getStatusFilterLoan(Loan.Status.NEW,data);
        List<Loan> StatusPending=getStatusFilterLoan(Loan.Status.PENDING,data);
        List<Loan> StatusActive=getStatusFilterLoan(Loan.Status.ACTIVE,data);
        List<Loan> StatusRisk=getStatusFilterLoan(Loan.Status.RISK,data);
        List<Loan> StatusFinished=getStatusFilterLoan(Loan.Status.FINISHED,data);
        Integer[] TotalStatusLenders=new Integer[5];
        TotalStatusLenders[0]=getTotalCustomerLenders(StatusNew);
        TotalStatusLenders[1]=getTotalCustomerLenders(StatusPending);
        TotalStatusLenders[2]=getTotalCustomerLenders(StatusActive);
        TotalStatusLenders[3]=getTotalCustomerLenders(StatusRisk);
        TotalStatusLenders[4]=getTotalCustomerLenders(StatusFinished);
        return TotalStatusLenders;
    }
    private Integer getTotalCustomerLenders(List<Loan> data){
        Integer sum=0;
        for(Loan val: data){
            sum=sum+val.getLoanerList().stream().filter(x->x.getOwner().equals(this)).collect(Collectors.toList()).size();
        }
        return sum;

    }

    private List<Loan> getStatusFilterLoan(Loan.Status status,List<Loan> data){
        switch (status){
            case NEW:
                return data.stream().filter(x->x.getStatus()== Loan.Status.NEW).collect(Collectors.toList());
            case PENDING:
               return data.stream().filter(x->x.getStatus()== Loan.Status.PENDING).collect(Collectors.toList());
            case ACTIVE:
                return data.stream().filter(x->x.getStatus()== Loan.Status.ACTIVE).collect(Collectors.toList());
            case RISK:
                return data.stream().filter(x->x.getStatus()== Loan.Status.RISK).collect(Collectors.toList());
            case FINISHED:
                return data.stream().filter(x->x.getStatus()== Loan.Status.FINISHED).collect(Collectors.toList());
        }
        return data;
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
        this.balance = balance;
    }
}
