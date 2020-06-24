package j;
public class IncorrectConstructorParameters extends Exception {
    private String message;
    public IncorrectConstructorParameters (String massage) {
        super();
        this.message = massage;
    }

    @Override
    public String getMessage() {
        return message;
    }
}