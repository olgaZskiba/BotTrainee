package by.integrator.telegrambot.validation;

public class Validator {

    private final static String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private final static String FIRSTNAME_REGEX = "^[A-Za-zА-Яа-я]+$"; 
    private final static String SURNAME_REGEX = "^[A-Za-zА-Яа-я]+$";
    private final static String PHONE_NUMBER_REGEX = "^[+]{1}[3]{1}[7]{1}[5]{1}[(]{1}[0-9]{2}[)]{1}[-\\s/0-9]{9}$";
    
    private final static Integer EMAIL_MIN_LENGTH = 10;
    private final static Integer EMAIL_MAX_LENGTH = 40; 
    private final static Integer FIRSTNAME_MIN_LENGTH = 2;
    private final static Integer FIRSTNAME_MAX_LENGTH = 40;
    private final static Integer SURNAME_MIN_LENGTH = 3;
    private final static Integer SURNAME_MAX_LENGTH = 40;

    private static Validator instance;

    private Validator() {}

    public static Validator getInstance() {
        if (instance == null) {
            instance = new Validator();
        }
        return instance;
    }

    public static ValidationResult isEmailValid(String email) {
        if (!email.matches(EMAIL_REGEX)) {
            return new ValidationResult(false, "message.email.incorrectFormat");
        }
        if (email.length() < EMAIL_MIN_LENGTH || email.length() > EMAIL_MAX_LENGTH) {
            return new ValidationResult(false, "message.email.incorrectLength");
        }
        return new ValidationResult(true, null);
    }

    public static ValidationResult isFirstNameValid(String firstname) {
        if (!firstname.matches(FIRSTNAME_REGEX)) {
            return new ValidationResult(false, "message.firstname.incorrectFormat");
        }
        if (firstname.length() < FIRSTNAME_MIN_LENGTH || firstname.length() > FIRSTNAME_MAX_LENGTH) {
            return new ValidationResult(false, "message.firstname.incorrectLength");
        }
        return new ValidationResult(true, null);
    }

    public static ValidationResult isSurnameValid(String surname) {
        if (!surname.matches(SURNAME_REGEX)) {
            return new ValidationResult(false, "message.surname.incorrectFormat");
        }
        if (surname.length() < SURNAME_MIN_LENGTH || surname.length() > SURNAME_MAX_LENGTH) {
            return new ValidationResult(false, "message.surname.incorrectLength"); 
        }
        return new ValidationResult(true, null);
    }

    public ValidationResult isPhoneNumberValid(String phoneNumber) {
        if (!phoneNumber.matches(PHONE_NUMBER_REGEX)) {
            return new ValidationResult(false, "message.mobilePhone.incorrectFormat");
        }
        return new ValidationResult(true, null);
    }

}
