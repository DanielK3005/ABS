package exception;

public class ExtraPaymentMoneyException extends Exception{
    private final String EXCEPTION_MESSAGE = "In the payment process of Loan named %s, You successfully paid %,.1f to all the Lenders as part of the payment process,\n you got back %,.1f amount of extra money that is yours";

    String id;
    double getBack;
    double pay;

    public ExtraPaymentMoneyException(String ID, double amount, double pay){
        this.id=ID;
        this.getBack =amount;
        this.pay=pay;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,id,pay, getBack);
    }
}
