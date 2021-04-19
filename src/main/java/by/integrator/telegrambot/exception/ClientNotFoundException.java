package by.integrator.telegrambot.exception;

public class ClientNotFoundException extends Throwable {
    
    private static final long serialVersionUID = 7566804820447654352L;

    public ClientNotFoundException() {
        super();
    }

    public ClientNotFoundException(String message) {
        super(message);
    }

}
