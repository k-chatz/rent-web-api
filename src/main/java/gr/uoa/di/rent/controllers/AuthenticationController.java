package gr.uoa.di.rent.controllers;

import gr.uoa.di.rent.exceptions.AppException;
import gr.uoa.di.rent.exceptions.NotAuthorizedException;
import gr.uoa.di.rent.exceptions.UserExistsException;
import gr.uoa.di.rent.models.*;
import gr.uoa.di.rent.payload.requests.LoginRequest;
import gr.uoa.di.rent.payload.requests.ProviderApplicationRequest;
import gr.uoa.di.rent.payload.requests.RegisterRequest;
import gr.uoa.di.rent.payload.responses.ConnectResponse;
import gr.uoa.di.rent.repositories.BusinessRepository;
import gr.uoa.di.rent.repositories.RoleRepository;
import gr.uoa.di.rent.repositories.UserRepository;
import gr.uoa.di.rent.security.CurrentUser;
import gr.uoa.di.rent.security.JwtTokenProvider;
import gr.uoa.di.rent.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

@RestController
@Validated
@RequestMapping("/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BusinessRepository businessRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(UserRepository userRepository, RoleRepository roleRepository, BusinessRepository businessRepository, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.businessRepository = businessRepository;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        /* Check if the user already exists.*/
        userRepository.findByUsernameOrEmail(registerRequest.getUsername(), registerRequest.getEmail())
                .ifPresent((s) -> {
                    logger.error("A user with the same username \"" + registerRequest.getUsername() + "\" or email \"" + registerRequest.getEmail() + "\" already exists!");
                    throw new UserExistsException("A user with the same username or email already exists!");
                });

        /* Assign a user role.*/
        Role role = roleRepository.findByName(RoleName.ROLE_USER);
        if (role == null) {
            throw new AppException("User Role not set.");
        }

        User user_temp = new User(
                null,
                registerRequest.getUsername(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getEmail(),
                role,
                false,
                false,
                null,
                null
        );

        Profile profile = new Profile(
                user_temp,
                registerRequest.getName(),
                registerRequest.getSurname(),
                registerRequest.getBirthday(),
                "https://ui-avatars.com/api/?name=" + registerRequest.getName() + "+"
                        + registerRequest.getSurname() + "&rounded=true&%20bold=true&background=a8d267&color=000000"
        );
        user_temp.setProfile(profile);

        Wallet wallet = new Wallet(
                user_temp, 99999.0
        );
        user_temp.setWallet(wallet);

        User user = userRepository.save(user_temp);

        logger.debug("User with username '" + user.getUsername() + "', email '" + user.getEmail() + "' and password '" + registerRequest.getPassword() + "' was added!");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user.getEmail(), registerRequest.getPassword());    // Use the non-encrypted password from the registerRequest.

        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{id}")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).body(new ConnectResponse(jwt, "Bearer", user));
    }

    @PostMapping("/provider-application")
    @ResponseBody
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> registerProvider(
            @CurrentUser Principal principal,
            @Valid @RequestBody ProviderApplicationRequest providerApplicationRequest) {

        if (principal.getUser().getPending_provider()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        Business b = new Business(
                providerApplicationRequest.getCompany_name(),
                providerApplicationRequest.getCompany_address(),
                providerApplicationRequest.getTax_number(),
                providerApplicationRequest.getTax_office(),
                providerApplicationRequest.getOwner_name(),
                providerApplicationRequest.getOwner_surname(),
                providerApplicationRequest.getOwner_patronym(),
                providerApplicationRequest.getId_card_number(),
                providerApplicationRequest.getId_card_date_of_issue(),
                providerApplicationRequest.getResidence_address(),
                principal.getUser(), null);

        Wallet wallet = new Wallet(b, 0.0);
        b.setWallet(wallet);
        Business business = businessRepository.save(b);
        if (business != null) {
            userRepository.updatePendingProvider(principal.getUser().getId());
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fail to create business!");
        }
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
