package cotato.bookitlist.annotation;

import cotato.bookitlist.factory.WithCustomMockUserFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMockUserFactory.class)
public @interface WithCustomMockUser {
    Long userId = 1L;
    String role = "USER";
}
