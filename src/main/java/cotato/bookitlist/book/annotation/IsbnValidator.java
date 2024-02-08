package cotato.bookitlist.book.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsbnValidator implements ConstraintValidator<IsValidIsbn, String> {

    private String defaultMessage;

    @Override
    public void initialize(IsValidIsbn constraintAnnotation) {
        this.defaultMessage = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() != 13) {
            throw new IllegalArgumentException(defaultMessage);
        }

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(value.charAt(i));
            sum += (i % 2 == 0) ? digit : 3 * digit;
        }

        int lastDigit = Character.getNumericValue(value.charAt(12));
        if ((sum + lastDigit) % 10 != 0) {
            throw new IllegalArgumentException(defaultMessage);
        }

        return true;
    }
}
