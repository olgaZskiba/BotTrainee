package by.minilooth.telegrambot.exception;

public class UserNotFoundException extends Throwable {
    
    private static final long serialVersionUID = -6869041367993470199L;

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }

}
