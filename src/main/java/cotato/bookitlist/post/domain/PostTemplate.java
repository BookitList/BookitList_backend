package cotato.bookitlist.post.domain;

public enum PostTemplate {
    NON(1, null), TEMPLATE(4, "<============================>");

    public final int num;
    public final String split;

    PostTemplate(int num, String split) {
        this.num = num;
        this.split = split;
    }
}
