package cotato.bookitlist.book.dto.request;


import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;

public record BookRegisterRequest(
        @Size(min = 13, max = 13)
        String isbn13
) {

    @AssertTrue(message = "잘못된 형식의 isbn13입니다.")
    public boolean isValidIsbn() {
        if (isbn13 == null || isbn13.length() != 13) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn13.charAt(i));
            sum += (i % 2 == 0) ? digit : 3 * digit;
        }

        int lastDigit = Character.getNumericValue(isbn13.charAt(12));
        return (sum + lastDigit) % 10 == 0;
    }
}
