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

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String role;
    private Boolean pending_provider;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private Principal(Long id, String name, String surname, String email, String password, String role,
                      Boolean pending_provider, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.pending_provider = pending_provider;
        this.authorities = authorities;
    }

    public static Principal getInstance(User user) {
        Set<Role> roles = new HashSet<Role>();
        roles.add(user.getRole());

        List<GrantedAuthority> authorities = roles.stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());

        return new Principal(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPassword(),
                user.getRole().getName().toString(),
                user.getPending_provider(),
                authorities
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Boolean getPending_provider() {
        return pending_provider;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        Principal that = (Principal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Principal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", pending_provider=" + pending_provider +
                ", password='" + password + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
