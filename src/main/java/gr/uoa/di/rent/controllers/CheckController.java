package gr.uoa.di.rent.controllers;

import gr.uoa.di.rent.models.User;
import gr.uoa.di.rent.payload.responses.CheckProviderApplicationStatusResponse;
import gr.uoa.di.rent.payload.responses.CheckResponse;
import gr.uoa.di.rent.repositories.UserRepository;
import gr.uoa.di.rent.security.CurrentUser;
import gr.uoa.di.rent.security.Principal;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static gr.uoa.di.rent.config.Constraint.*;

@RestController
@Validated
@RequestMapping("/check")
public class CheckController {

    private static final Logger logger = LoggerFactory.getLogger(CheckController.class);

    @Autowired
    private UserRepository userRepository;

    /* Check if the given email exists.*/
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<?> checkEmailExists(@PathVariable("email") @NotNull @Email String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            logger.debug("email {} is available", email);
            return ResponseEntity.ok(new CheckResponse(true));
        } else {
            logger.debug("email {} is NOT available", email);
            return ResponseEntity.ok(new CheckResponse(false));
        }
    }

    /* Check if the username exists.*/
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<?> checkUsernameExists(
            @PathVariable("username")
            @NotNull
            @Length(min = USERNAME_MIN, max = USERNAME_MAX)
            @Pattern(regexp = USERNAME_PATTERN, message = USERNAME_PATTERN_MESSAGE) String username) {

        /* Check if the user exists*/
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            logger.debug("username {} is available", username);
            return ResponseEntity.ok(new CheckResponse(true));
        } else {
            logger.debug("username {} is NOT available", username);
            return ResponseEntity.ok(new CheckResponse(false));
        }
    }

    /* Check if the user has already request to be provider exists.*/
    @GetMapping("/provider-application-status")
    @PreAuthorize("hasRole('USER') or hasRole('PROVIDER')")
    public ResponseEntity<?> checkProviderApplicationStatus(@CurrentUser Principal principal) {
        return ResponseEntity.ok(new CheckProviderApplicationStatusResponse(principal.getUser().getPending_provider()));
    }
}
