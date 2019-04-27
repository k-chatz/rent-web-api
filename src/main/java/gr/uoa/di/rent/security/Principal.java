package gr.uoa.di.rent.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.uoa.di.rent.models.Role;
import gr.uoa.di.rent.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

/*
 * This is the class whose instances will be returned from our custom UserDetailsServiceImpl.
 * Spring Security will use the information stored in the Principal
 * object to perform authentication and authorization.
 */
public class Principal implements UserDetails {
    private static final Logger logger = LoggerFactory.getLogger(Principal.class);
    private User user;
    private Collection<? extends GrantedAuthority> authorities;

    private Principal(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    public static Principal getInstance(User user) {
        Set<Role> roles = new HashSet<Role>();
        roles.add(user.getRole());
        List<GrantedAuthority> authorities = roles.stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());

        return new Principal(user, authorities);
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Principal principal = (Principal) o;
        return Objects.equals(user, principal.user) &&
                Objects.equals(authorities, principal.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, authorities);
    }

    @Override
    public String toString() {
        return "Principal{" +
                "user=" + user +
                ", authorities=" + authorities +
                '}';
    }
}
