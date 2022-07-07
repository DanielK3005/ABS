package exception;

public class MoreThanYouGotException extends Exception{

    private final String EXCEPTION_MESSAGE = "Your trying to get %,.1f amount of money from your account,\nbut you have only %,.1f";

    private double have;
    private double asking;

    public MoreThanYouGotException(double amount, double withdraw){
        this.have=amount;
        this.asking=withdraw;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,asking,have);
    }
}
