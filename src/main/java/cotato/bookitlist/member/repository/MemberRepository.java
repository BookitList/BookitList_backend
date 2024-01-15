package cotato.bookitlist.member.repository;

import cotato.bookitlist.config.security.oauth.AuthProvider;
import cotato.bookitlist.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByOauth2IdAndAuthProvider(String oauth2Id, AuthProvider authProvider);
}
