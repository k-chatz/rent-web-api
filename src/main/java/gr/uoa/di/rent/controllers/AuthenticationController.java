package gr.uoa.di.rent.controllers;

import gr.uoa.di.rent.exceptions.AppException;
import gr.uoa.di.rent.exceptions.NotAuthorizedException;
import gr.uoa.di.rent.exceptions.UserExistsException;
import gr.uoa.di.rent.models.Profile;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@Validated
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

    @PostMapping("/register")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        /* Check if the user already exists.*/
        userRepository.findByUsernameOrEmail(registerRequest.getUsername(), registerRequest.getEmail())
                .ifPresent((s) -> {
                    logger.warn("A user with the same username '" + registerRequest.getUsername() + "' already exists!");
                    throw new UserExistsException("A user with the same username or email already exists!");
                });

        /* Assign a user role.*/
        Role role = roleRepository.findByName(RoleName.ROLE_USER);
        if (role == null) {
            throw new AppException("User Role not set.");
        }

        User user_temp = new User(
                registerRequest.getUsername(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getEmail(),
                role,
                false,
                false,
                null
        );

        Profile profile = new Profile(
                registerRequest.getName(),
                registerRequest.getSurname(),
                registerRequest.getBirthday(),
                "https://ui-avatars.com/api/?name=" + registerRequest.getName() + "+"
                        + registerRequest.getSurname() + "&rounded=true&%20bold=true&background=f0f3f6&color=ffffff"
        );

        user_temp.setProfile(profile);

        profile.setOwner(user_temp);

        User user = userRepository.save(user_temp);

        logger.debug("User with username '" + user.getUsername() + "', email '" + user.getEmail() +
                "' and password '" + registerRequest.getPassword() + "' was added!");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user.getEmail(), registerRequest.getPassword());

        /* Use the non-encrypted password from the registerRequest.*/
        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{id}")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).body(
                new ConnectResponse(jwt, "Bearer", ((Principal) authentication.getPrincipal()).getUser())
        );
    }

    @PostMapping("/provider_application")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> registerProvider(
            @CurrentUser Principal principal,
            @Valid @RequestBody ProviderApplicationRequest providerApplicationRequest) {

        /* TODO: ▶ Save provider application data in database for principal user.*/

        /* TODO: ▶ Perform an update at pending_provider field (user object)*/

        /* TODO: ▶ Return success/failure response*/

        return ResponseEntity.ok(principal);
    }

    /* Signs a user in to the app.*/
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        User user = ((Principal) authentication.getPrincipal()).getUser();

        if (user.getLocked()) {
            throw new NotAuthorizedException("This user is locked and cannot access the app!");
        }

        return ResponseEntity.ok(new ConnectResponse(jwt, "Bearer", user));
    }
}
