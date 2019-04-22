package gr.uoa.di.rent.controllers;

import gr.uoa.di.rent.exceptions.AppException;
import gr.uoa.di.rent.exceptions.BadRequestException;
import gr.uoa.di.rent.exceptions.NotAuthorizedException;
import gr.uoa.di.rent.exceptions.UserExistsException;
import gr.uoa.di.rent.models.RoleName;
import gr.uoa.di.rent.models.User;
import gr.uoa.di.rent.payload.requests.LockRequest;
import gr.uoa.di.rent.payload.requests.UnlockRequest;
import gr.uoa.di.rent.payload.requests.UserUpdateRequest;
import gr.uoa.di.rent.payload.responses.PagedResponse;
import gr.uoa.di.rent.payload.responses.UserResponse;
import gr.uoa.di.rent.repositories.UserRepository;
import gr.uoa.di.rent.security.CurrentUser;
import gr.uoa.di.rent.security.UserDetailsImpl;
import gr.uoa.di.rent.services.UserService;
import gr.uoa.di.rent.util.AppConstants;
import gr.uoa.di.rent.util.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/users")
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final AtomicInteger counter = new AtomicInteger();

    @GetMapping("")
    //@PreAuthorize("hasRole('ADMIN')")        //this doesnt work
    @PreAuthorize("hasAuthority('ADMIN')") //this works !!
    public PagedResponse<UserResponse> getUsers(@CurrentUser UserDetailsImpl currentUser,
                                                @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                                @RequestParam(value = "role", defaultValue = AppConstants.DEFAULT_ROLE) int role) {

        validatePageNumberAndSize(page, size);

        List<RoleName> rolenames = new ArrayList<>();

        switch (role){
            // case 1: ADMIN, poy den 8eloyme na ton fernei
            case 2:
                rolenames.add(RoleName.USER);
                break;
            case 3:
                rolenames.add(RoleName.PROVIDER);
                break;
            default:
                rolenames.add(RoleName.USER);
                rolenames.add(RoleName.PROVIDER);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
        Page<User> users = userRepository.findAllByRoleNameIn(rolenames, pageable);

        if (users.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), users.getNumber(),
                    users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
        }

        List<UserResponse> userResponses = users.map(ModelMapper::mapUserToUserResponse).getContent();

        return new PagedResponse<>(userResponses, users.getNumber(),
                users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    @PutMapping("/lock")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Resource> lockUsers(@RequestBody LockRequest lockRequest, @Valid @CurrentUser UserDetailsImpl currentUser) {

        List<Long> userIDs = lockRequest.getUserIDs();

        //logger.debug("UserIDs: " + userIDs.toString());

        // Make sure the admin will NOT get locked by mistake!
        userIDs.remove(currentUser.getId());

        int changed = userRepository.lockUsers(userIDs);

        return handleUsersUpdateResponse(changed, userIDs, "locked");
    }


    @PutMapping("/unlock")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Resource> unlockUsers(@RequestBody UnlockRequest unlockRequest) {

        List<Long> userIDs = unlockRequest.getUserIDs();

        //logger.debug("UserIDs: " + userIDs.toString());

        int changed = userRepository.unlockUsers(userIDs);

        return handleUsersUpdateResponse(changed, userIDs, "unlocked");
    }


    @PutMapping("/updateUser")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<?> updateUserInfo(@Valid @RequestBody UserUpdateRequest userUpdateRequest, @Valid @CurrentUser UserDetailsImpl currentUser) {

        User user = userUpdateRequest.asUser();
        Long userId = user.getId();

        // If current user is not Admin and the given "userId" is not the same as the current user requesting, then return error.
        if (!currentUser.getId().equals(userId) && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            throw new NotAuthorizedException("You are not authorized to update the data of another user!");
        }

        // Check if the new email or username is already reserved by another user..
        userRepository.findByEmail(user.getEmail())
                .ifPresent((storedUser) -> {
                    // If the user to be updated wants to have the email which belongs to another user throw an exception.
                    if (!storedUser.getId().equals(userId)) {
                        logger.warn("OTHER USER! Email: " + user.getEmail() + ", storedUserID = " + storedUser.getId() + ", id = " + userId + ", currentUserId = " + currentUser.getId());
                        throw new UserExistsException("A user with the same email \"" + storedUser.getEmail() + "\" already exists!");    // It gets logged inside
                    } else {
                        logger.debug("Email: " + user.getEmail() + ", storedUserID = " + storedUser.getId() + ", id = " + userId + ", currentUserId = " + currentUser.getId());
                    }
                });

        userRepository.findByUsername(user.getUsername())
                .ifPresent((storedUser) -> {
                    // If the user to be updated wants to have the username which belongs to another user throw an exception.
                    if (!storedUser.getId().equals(userId)) {
                        logger.warn("OTHER USER! Username: " + user.getUsername() + ", storedUserID = " + storedUser.getId() + " id = " + userId + ", currentUserId = " + currentUser.getId());
                        throw new UserExistsException("A user with the same username \"" + storedUser.getUsername() + "\" already exists!");
                    } else {
                        logger.debug("Username: " + user.getUsername() + ", storedUserID = " + storedUser.getId() + ", id = " + userId + ", currentUserId = " + currentUser.getId());
                    }
                });

        //logger.debug(user.toString());

        user.setPassword(passwordEncoder.encode(user.getPassword()));   // Encrypt the password.

        int affectedRows = this.userService.updateUserData(user);
        if (affectedRows == 1) {
            logger.debug("User info was updated for user with id: " + userId);
            return ResponseEntity.ok().build();
        } else {
            logger.warn("No user was found in DataBase having userId: " + userId);
            return ResponseEntity.badRequest().build();
        }
    }


    private ResponseEntity<Resource> handleUsersUpdateResponse(int changed, List<Long> userIDs, String operation) {
        String errorMsg;
        if (changed == 0) {
            errorMsg = "No users were " + operation + "!";
            logger.error(errorMsg);
            throw new AppException(errorMsg);
        } else if (changed != userIDs.size()) {
            errorMsg = "Operation unsuccessful: " + (userIDs.size() - changed) + " users were not" + operation + "!";
            logger.error(errorMsg);
            throw new AppException(errorMsg);
        } else
            return ResponseEntity.ok().build();
    }

}