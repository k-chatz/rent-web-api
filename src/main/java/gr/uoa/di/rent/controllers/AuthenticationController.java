package gr.uoa.di.rent.controllers;

import gr.uoa.di.rent.exceptions.AppException;
import gr.uoa.di.rent.exceptions.BadRequestException;
import gr.uoa.di.rent.exceptions.UserExistsException;
import gr.uoa.di.rent.models.Role;
import gr.uoa.di.rent.models.RoleName;
import gr.uoa.di.rent.models.User;
import gr.uoa.di.rent.payload.requests.SignInRequest;
import gr.uoa.di.rent.payload.responses.SignInResponse;
import gr.uoa.di.rent.repositories.RoleRepository;
import gr.uoa.di.rent.repositories.UserRepository;
import gr.uoa.di.rent.payload.requests.SignUpRequest;
import gr.uoa.di.rent.payload.responses.SignUpResponse;
import gr.uoa.di.rent.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final AtomicInteger counter = new AtomicInteger();


    @PostMapping("/signup")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {

        // Check if the user already exists
        userRepository.findByEmail(signUpRequest.getEmail())
                .ifPresent((s) -> {
                    logger.warn("A user with the same email \"" + signUpRequest.getEmail() + "\" already exists!");
                    throw new UserExistsException("A user with the same email already exists!");
                });

        userRepository.findByUsername(signUpRequest.getUsername())
                .ifPresent((s) -> {
                    logger.warn("A user with the same username \"" + signUpRequest.getUsername() + "\" already exists!");
                    throw new UserExistsException("A user with the same username already exists!");
                });

        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getPassword(),
                signUpRequest.getEmail(),
                signUpRequest.getName(),
                signUpRequest.getSurname(),
                signUpRequest.getBirthday(),
                false,
                null);

        // Encrypt the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign a user role
        Role role = roleRepository.findByName(RoleName.ROLE_USER);
        if (role == null) {
            throw new AppException("User Role not set.");
        }
        user.setRole(role);

        User result = userRepository.save(user);

        logger.debug("User with username \"" + result.getUsername() + "\" was added!");

        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(uri).body(new SignUpResponse(result.getUsername(), result.getEmail()));
    }


    // Signs a user in to the app
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        // Check if the user exists
        User user = userRepository.findByEmail(signInRequest.getEmail()).orElse(null);
        if (user == null) {
            throw new BadRequestException("Invalid email or password.");
        }

        String jwt = getJwtToken(signInRequest.getEmail(), signInRequest.getPassword(), user.getRole().getName().name());

        return ResponseEntity.ok(
                new SignInResponse(
                        jwt,
                        user.getId(),
                        user.getEmail(),
                        user.getName(),
                        user.getSurname(),
                        user.getRole().getName().name()
                )
        );
    }


    private String getJwtToken(String email, String password, String roleName)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication, roleName);
    }

}
