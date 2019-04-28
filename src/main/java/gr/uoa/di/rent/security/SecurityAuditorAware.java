package gr.uoa.di.rent.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityAuditorAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        if (principal instanceof String && principal.equals("anonymousUser")) {
            return Optional.empty();
        } else if (principal instanceof Principal) {
            return Optional.of(((Principal) principal).getUser().getId());
        } else {
            return Optional.empty();
        }
    }
}
