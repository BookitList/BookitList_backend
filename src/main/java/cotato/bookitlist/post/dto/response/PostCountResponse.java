package cotato.bookitlist.post.dto.response;

public record PostCountResponse(
        int count
) {

    public static PostCountResponse of(int count) {
        return new PostCountResponse(count);
    }
}
