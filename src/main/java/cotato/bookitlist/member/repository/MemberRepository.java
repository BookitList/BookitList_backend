package cotato.bookitlist.member.repository;

import cotato.bookitlist.config.security.oauth.AuthProvider;
import cotato.bookitlist.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByOauth2IdAndAuthProvider(String oauth2Id, AuthProvider authProvider);

    @Query("select m from Member m where m.status = 'PUBLIC'")
    Page<Member> findPublicMember(Pageable pageable);

}
