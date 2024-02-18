package cotato.bookitlist.book.dto.response;

public record BookLikeResponse(
        boolean liked
) {

    public static BookLikeResponse of(boolean liked) {
        return new BookLikeResponse(liked);
    }
}
