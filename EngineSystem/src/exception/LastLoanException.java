package exception;

import transaction.Loan;

public class LastLoanException extends Exception{

    private final String EXCEPTION_MESSAGE = "you are trying to loan %,.1f money, but the remaining loan which his name is %s needs"
            + " %,.1f amount of money to be active.\nthe transaction has been made and you got back %,.1f to your account because there is no more loans to loan them the rest of the money.";

    private double amount;
    private double leftToMakeActive;
    private Loan last;

    public LastLoanException(double amount,double leftToMakeActive, Loan last){
        this.amount=amount;
        this.leftToMakeActive=leftToMakeActive;
        this.last=last;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,amount,last.getId(),leftToMakeActive,(amount-leftToMakeActive));
    }

}
