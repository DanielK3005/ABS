package exception;

public class DuplicatePersonException extends Exception{

    private final String EXCEPTION_MESSAGE ="I found in your file that the customer %s appear more than once in your customer list. \nplease upload a valid file!";

    private String name;

    public DuplicatePersonException(String name){this.name=name;}

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,name);
    }
}
