package cotato.bookitlist.mark.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowUser {

    @Id
    @GeneratedValue
    @Column(name = "follow_user_id")
    private Long id;

    private Long fromUserId;

    private Long toUserId;
}
