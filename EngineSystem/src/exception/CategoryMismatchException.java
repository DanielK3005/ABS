package exception;

public class CategoryMismatchException extends Exception{

    private final String EXCEPTION_MESSAGE ="After checking your file i noticed that in loan of %s,\nthere is category named %s that is not part of the category list that you supplied";

    private String owner;
    private String category;

    public CategoryMismatchException(String owner, String name){
        this.owner=owner;
        this.category=name;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,owner,category);
    }
}
