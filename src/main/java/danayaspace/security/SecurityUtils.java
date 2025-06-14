package danayaspace.security;

import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SecurityUtils {

    public static boolean isAuthenticated() {
        return getAuthentication() != null;
    }

    public static DSUser getCurrentUser() {
        return findCurrentUser().orElse(null);
    }

    public static Long getUserId() {
        return getCurrentUser().getId();
    }

    public static Optional<DSUser> findCurrentUser() {
        Authentication auth = getAuthentication();
        if (auth != null) {
            Object principal = auth.getPrincipal();
            return Optional.of((DSUser) principal);
        }
        return Optional.empty();
    }


    public static Authentication getAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }

        return auth;
    }
}
