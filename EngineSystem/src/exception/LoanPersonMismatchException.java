package exception;

public class LoanPersonMismatchException extends Exception{

    private final String EXCEPTION_MESSAGE ="After checking your file,\nI found that there is a person named %s from a loan named %s,\n that isnt exist in the customers list.";

    private String PersonName;
    private String LoanName;

    public LoanPersonMismatchException(String name,String id){
        this.PersonName=name;
        this.LoanName=id;
    }


    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,PersonName,LoanName);
    }
}
