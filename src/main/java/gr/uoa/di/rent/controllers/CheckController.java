package gr.uoa.di.rent.controllers;
import gr.uoa.di.rent.models.User;
import gr.uoa.di.rent.payload.responses.CheckResponse;
import gr.uoa.di.rent.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/check")
public class CheckController {

    private static final Logger logger = LoggerFactory.getLogger(CheckController.class);

    @Autowired
    private UserRepository userRepository;

    // Check if the given email exists.
    @GetMapping("/email/{email}")
    public ResponseEntity<?> checkEmailExists(@PathVariable("email") String email) {

        // Check if the given email exists.
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) { // available
            logger.debug("email {} is available", email);
            return ResponseEntity.ok(
                new CheckResponse(true)
            );
        }
        else {  // not available
            logger.debug("email {} is NOT available", email);
            return ResponseEntity.ok(
                    new CheckResponse(false)
            );
        }
    }

    // Check if the username exists.
    @GetMapping("/username/{username}")
    public ResponseEntity<?> checkUsernameExists(@PathVariable("username") String username) {

        // Check if the user exists
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) { // available
            logger.debug("username {} is available", username);
            return ResponseEntity.ok(
                    new CheckResponse(true)
            );
        }
        else {  // not available
            logger.debug("username {} is NOT available", username);
            return ResponseEntity.ok(
                    new CheckResponse(false)
            );
        }
    }

}
