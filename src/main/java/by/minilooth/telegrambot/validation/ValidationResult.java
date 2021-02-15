package by.minilooth.telegrambot.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class ValidationResult {
    private Boolean isValid;
    private String message;
}
