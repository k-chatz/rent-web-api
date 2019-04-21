package gr.uoa.di.rent.controllers;

import gr.uoa.di.rent.models.User;
import gr.uoa.di.rent.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/users")
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private UserRepository userRepository;

    private final AtomicInteger counter = new AtomicInteger();

    @GetMapping("")
    //@PreAuthorize("hasRole('ADMIN')")        //this doesnt work
    @PreAuthorize("hasAuthority('ADMIN')") //this works !!
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
