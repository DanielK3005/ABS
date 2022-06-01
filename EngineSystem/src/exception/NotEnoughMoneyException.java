package exception;

import transaction.Loan;

public class NotEnoughMoneyException extends Exception{
    private final String EXCEPTION_MESSAGE = "In Loan named %s, which currently in Status %s,\nYou are trying to make a payment and you dont have enough money to pay to all the Lenders,\nYou need to pay them %d amount of money in this payment, and you got %d";

    String id;
    Loan.Status status;
    double have;
    double pay;

    public NotEnoughMoneyException(String ID, Loan.Status Status, double amount, double pay){
        this.id=ID;
        this.status=Status;
        this.have=amount;
        this.pay=pay;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,id,status,pay,have);
    }
}
