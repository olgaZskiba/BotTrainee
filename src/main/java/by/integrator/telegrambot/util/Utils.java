package by.integrator.telegrambot.util;

public class Utils {
    
    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty() || input.isBlank()) {
            return null;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

}
