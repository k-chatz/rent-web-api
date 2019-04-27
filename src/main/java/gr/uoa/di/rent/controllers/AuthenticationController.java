package gr.uoa.di.rent.controllers;

import gr.uoa.di.rent.exceptions.AppException;
import gr.uoa.di.rent.exceptions.NotAuthorizedException;
import gr.uoa.di.rent.exceptions.UserExistsException;
import gr.uoa.di.rent.models.Role;
import gr.uoa.di.rent.models.RoleName;
import gr.uoa.di.rent.models.User;
import gr.uoa.di.rent.payload.requests.LoginRequest;
import gr.uoa.di.rent.payload.requests.ProviderApplicationRequest;
import gr.uoa.di.rent.payload.requests.RegisterRequest;
import gr.uoa.di.rent.payload.responses.ConnectResponse;
import gr.uoa.di.rent.repositories.RoleRepository;
import gr.uoa.di.rent.repositories.UserRepository;
import gr.uoa.di.rent.security.CurrentUser;
import gr.uoa.di.rent.security.JwtTokenProvider;
import gr.uoa.di.rent.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final AtomicInteger counter = new AtomicInteger();

    private String getJwtToken(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication);
    }

    @PostMapping("/register")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        // Check if the user already exists
        userRepository.findByEmail(registerRequest.getEmail())
                .ifPresent((s) -> {
                    logger.warn("A user with the same email '" + registerRequest.getEmail() + "' already exists!");
                    throw new UserExistsException("A user with the same email already exists!");
                });

        userRepository.findByUsername(registerRequest.getUsername())
                .ifPresent((s) -> {
                    logger.warn("A user with the same username '" + registerRequest.getUsername() + "' already exists!");
                    throw new UserExistsException("A user with the same username already exists!");
                });

        User user = new User(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getEmail(),
                registerRequest.getName(),
                registerRequest.getSurname(),
                registerRequest.getBirthday(),
                false,
                null
        );

        // Encrypt the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign a user role
        Role role = roleRepository.findByName(RoleName.USER);
        if (role == null) {
            throw new AppException("User Role not set.");
        }
        user.setRole(role);

        User storedUser = userRepository.save(user);

        logger.debug("User with username '" + storedUser.getUsername() + "', email '" + storedUser.getEmail() + "' and password '" + registerRequest.getPassword() + "' was added!");

        /* Use the non-encrypted password from the registerRequest.*/
        String jwt = getJwtToken(storedUser.getEmail(), registerRequest.getPassword());

        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{id}")
                .buildAndExpand(storedUser.getId()).toUri();
        return ResponseEntity.created(uri).body(new ConnectResponse(jwt, "Bearer", storedUser));
    }

    @PostMapping("/provider_application")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> registerProvider(
            @CurrentUser Principal currentUser,
            @Valid @RequestBody ProviderApplicationRequest providerApplicationRequest) {

        /* 😡 TODO: ▶ Update current user object to determine who is the user that requests to be provider. ⬅ !Important 😡*/

        /* TODO: ▶ Save provider application data in database*/

        /* TODO: ▶ Perform an update at pending_provider field (user object)*/

        /* TODO: ▶ Return success/failure response*/

        return ResponseEntity.ok(currentUser);
    }

    /* Signs a user in to the app.*/
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

        /* Check if the user exists.*/
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new NotAuthorizedException("Invalid email or password.");
        } else if (user.getLocked()) {
            throw new NotAuthorizedException("This user is locked and cannot access the app!");
        }

        String jwt = getJwtToken(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(new ConnectResponse(jwt, "Bearer", user));
    }
}
