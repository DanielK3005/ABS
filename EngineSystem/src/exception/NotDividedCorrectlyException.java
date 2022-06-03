package exception;

public class NotDividedCorrectlyException extends Exception{

    private final String EXCEPTION_MESSAGE ="After checking your file i found in a loan named %s that the total pay time, which is %d"
            + ",\nis not divided correctly by the pay per yaz time, which is %d.\nIt makes the payment time double, which is not possible because number of yaz must be a whole number.";

    private String name;
    private int TotalYaz;
    private int PayEveryYaz;

    public NotDividedCorrectlyException(String id,int totalYaz,int payEveryYaz){
        this.name=id;
        this.TotalYaz=totalYaz;
        this.PayEveryYaz=payEveryYaz;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,name,TotalYaz,PayEveryYaz);
    }
}
