package exception;

import transaction.Loan;

public class NotEnoughMoneyException extends Exception{

    private final String EXCEPTION_MESSAGE = "In Loan named %s, which currently in Status %s,\nYou are trying to make a payment and you dont have enough money to pay to all the Lenders,\nYou need to pay them %,.1f amount of money in this payment, and you got %,.1f";

    private String name;
    private String status;
    private double pay;
    private double have;

    public NotEnoughMoneyException(String id,String status,double pay, double amount){
        this.name=id;
        this.pay=pay;
        this.have=amount;
        this.status=status;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,name,status,pay,have);
    }

}
