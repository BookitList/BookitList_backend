package cotato.bookitlist.factory;

import cotato.bookitlist.annotation.WithCustomMockUser;
import cotato.bookitlist.config.security.jwt.AuthDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithCustomMockUserFactory implements WithSecurityContextFactory<WithCustomMockUser> {
    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser mockUser) {
        UserDetails userDetails = new AuthDetails(mockUser.userId.toString(), mockUser.role);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, "user", userDetails.getAuthorities()));
        return context;
    }
}
