package gr.uoa.di.rent.controllers;

import gr.uoa.di.rent.models.User;
import gr.uoa.di.rent.repositories.AuthenticationRepository;
import gr.uoa.di.rent.payload.requests.SignUpRequest;
import gr.uoa.di.rent.payload.responses.SignUpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {


    @Autowired
    AuthenticationRepository authenticationRepository;

    private final AtomicInteger counter = new AtomicInteger();

    @PostMapping("/")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {

        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getPassword(),
                signUpRequest.getEmail(),
                signUpRequest.getName(),
                signUpRequest.getSurname(),
                0,
                signUpRequest.getBirthday(),
                false,
                null);


        User result = authenticationRepository.save(user);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(uri).body(new SignUpResponse(result.getUsername(), result.getEmail()));
    }
}
