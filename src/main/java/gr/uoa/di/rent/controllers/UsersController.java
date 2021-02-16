package gr.uoa.di.rent.controllers;

import gr.uoa.di.rent.exceptions.*;
import gr.uoa.di.rent.models.Role;
import gr.uoa.di.rent.models.RoleName;
import gr.uoa.di.rent.models.User;
import gr.uoa.di.rent.payload.requests.ApproveApplicationRequest;
import gr.uoa.di.rent.payload.requests.LockUnlockRequest;
import gr.uoa.di.rent.payload.requests.UserUpdateRequest;
import gr.uoa.di.rent.payload.requests.filters.PagedUsersFilter;
import gr.uoa.di.rent.payload.responses.LockUnlockResponse;
import gr.uoa.di.rent.payload.responses.PagedResponse;
import gr.uoa.di.rent.payload.responses.UploadFileResponse;
import gr.uoa.di.rent.repositories.ProfileRepository;
import gr.uoa.di.rent.repositories.RoleRepository;
import gr.uoa.di.rent.repositories.UserRepository;
import gr.uoa.di.rent.security.CurrentUser;
import gr.uoa.di.rent.security.Principal;
import gr.uoa.di.rent.services.FileStorageService;
import gr.uoa.di.rent.services.ProfileService;
import gr.uoa.di.rent.services.UserService;
import gr.uoa.di.rent.util.AppConstants;
import gr.uoa.di.rent.util.ModelMapper;
import gr.uoa.di.rent.util.PaginatedResponseUtil;
import gr.uoa.di.rent.util.UsersControllerUtil;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RestController
@Validated
@RequestMapping("/users")
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    private final UserService userService;

    private final UserRepository userRepository;

    private final ProfileService profileService;

    private final ProfileRepository profileRepository;

    private final RoleRepository roleRepository;

    private final FileStorageService fileStorageService;

    private final PasswordEncoder passwordEncoder;

    private final FileController fileController;

    private static final String profileBaseURI = "https://localhost:8443/api/users/";
    private static final String profilePhotoBaseName = "profile_photo";
    private static String fileStoragePath;  // Set during run-time.
    public static String currentDirectory = System.getProperty("user.dir");
    public static String localResourcesDirectory = currentDirectory + File.separator + "src" + File.separator + "main" + File.separator + "resources";
    private static final String localImageDirectory = localResourcesDirectory + File.separator + "img";
    private static final String genericPhotoName = "generic_profile_photo.png";
    private static final String imageNotFoundName = "image_not_found.png";

    public UsersController(UserService userService, UserRepository userRepository, ProfileService profileService, ProfileRepository profileRepository, RoleRepository roleRepository, FileStorageService fileStorageService, PasswordEncoder passwordEncoder, FileController fileController) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.profileService = profileService;
        this.profileRepository = profileRepository;
        this.roleRepository = roleRepository;
        this.fileStorageService = fileStorageService;
        this.passwordEncoder = passwordEncoder;
        this.fileController = fileController;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public PagedResponse<User> getUsers(PagedUsersFilter pagedResponseFilter) {

        try {
            PaginatedResponseUtil.validateParameters(pagedResponseFilter.getPage(), pagedResponseFilter.getSize(), pagedResponseFilter.getSort_field(), User.class);
        } catch (BadRequestException bre) {
            throw bre;
        } catch (Exception e) {
            throw new BadRequestException("Instantiation problem!");
        }

        Sort.Direction sort_order;

        // Default order is ASC, otherwise DESC
        if (AppConstants.DEFAULT_ORDER.equals(pagedResponseFilter.getOrder()))
            sort_order = Sort.Direction.ASC;
        else
            sort_order = Sort.Direction.DESC;

        List<RoleName> rolenames = new ArrayList<>();

        switch (pagedResponseFilter.getRole()) {
            // case 1: ADMIN, which we don't want to be returned.
            case 2:
                rolenames.add(RoleName.ROLE_USER);
                break;
            case 3:
                rolenames.add(RoleName.ROLE_PROVIDER);
                break;
            default:
                rolenames.add(RoleName.ROLE_USER);
                rolenames.add(RoleName.ROLE_PROVIDER);
        }

        Pageable pageable = PageRequest.of(pagedResponseFilter.getPage(), pagedResponseFilter.getSize(),
                sort_order, pagedResponseFilter.getSort_field());

        Page<User> users = userRepository.findAllByRoleNameIn(rolenames, pageable);

        if (users.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), users.getNumber(),
                    users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
        }

        List<User> userResponses = users.map(ModelMapper::mapUserToUserResponse).getContent();

        return new PagedResponse<>(userResponses, users.getNumber(),
                users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getUserByUsername(@PathVariable(value = "username") String username) {

        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("/{username}/profile")
    @PreAuthorize("hasRole('USER') or hasRole('PROVIDER') or hasRole('ADMIN')")
    // A user should be able to get its own profile. If we don't want to take the profile of another user, then we can just check if the requested username belongs to the principal or not.
    public ResponseEntity getProfileByUsername(@PathVariable(value = "username") String username) {

        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return ResponseEntity.ok(user.getProfile());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PutMapping("/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LockUnlockResponse> lockUsers(@Valid @RequestBody LockUnlockRequest lockUnlockRequest, @Valid @CurrentUser Principal principal) {

        List<Long> userIDs = lockUnlockRequest.getUserIDs();

        //logger.debug("UserIDs: " + userIDs.toString());

        // Make sure the admin will NOT get locked by mistake!
        userIDs.remove(principal.getUser().getId());

        int updateCount = userRepository.lockUsers(userIDs);

        return handleUsersLockUnlockResponse(updateCount, userIDs, "locked");
    }

    @PutMapping("/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LockUnlockResponse> unlockUsers(@Valid @RequestBody LockUnlockRequest lockUnlockRequest) {

        List<Long> userIDs = lockUnlockRequest.getUserIDs();

        //logger.debug("UserIDs: " + userIDs.toString());

        int updateCount = userRepository.unlockUsers(userIDs);

        return handleUsersLockUnlockResponse(updateCount, userIDs, "unlocked");
    }

    private ResponseEntity<LockUnlockResponse> handleUsersLockUnlockResponse(int updateCount, List<Long> userIDs, String operation) throws AppException {

        int requestedUsers = userIDs.size();

        if (updateCount == requestedUsers)
            return ResponseEntity.ok(new LockUnlockResponse(updateCount, updateCount + " users were " + operation));

        String errorMsg;

        // Get the count of the users exist in the database.
        List<User> users = userRepository.findAllById(userIDs);
        int foundUsers = users.size();
        if (foundUsers != requestedUsers) {
            // Then there were less users in the database than the requested ones.
            errorMsg = "Operation unsuccessful: " + (requestedUsers - foundUsers) + " users were not found in the database! The found ones were " + operation + ".";
            logger.error(errorMsg);
            throw new BadRequestException(errorMsg);
        } else {
            errorMsg = "Operation unsuccessful: " + (requestedUsers - updateCount) + " users could not get " + operation + "!";
            logger.error(errorMsg);
            throw new AppException(errorMsg);
        }
    }

    @PutMapping("/approve-application")
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LockUnlockResponse> approveApplications(@Valid @RequestBody ApproveApplicationRequest approveApplicationRequest, @Valid @CurrentUser Principal principal) {

        List<Long> userIDs = approveApplicationRequest.getUserIDs();

        // Make sure the admin will NOT get locked by mistake!
        userIDs.remove(principal.getUser().getId());

        Role role = roleRepository.findByName(RoleName.ROLE_PROVIDER);

        // Approve application and change role to provider and update pending_provider column back to false
        for (Long id : approveApplicationRequest.getUserIDs()) {
            User user = userRepository.getOne(id);
            if (user != null) {
                user.setPendingProvider(false);
                user.setRole(role);
                userRepository.save(user);
            }
        }

        return ResponseEntity.ok(null);
    }

    @PutMapping("/{userId:[\\d]+}/update")
    @PreAuthorize("hasRole('USER') or hasRole('PROVIDER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateUserInfo(@Valid @PathVariable(value = "userId") Long userId,
                                            @Valid @RequestBody UserUpdateRequest userUpdateRequest, @Valid @CurrentUser Principal principal) {
        User user = userUpdateRequest.asUser(userId, principal.getUser().getRole());

        // If current user is not Admin and the given "userId" is not the same as the current user requesting, then return error.
        if (!principal.getUser().getId().equals(userId) && !principal.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new NotAuthorizedException("You are not authorized to update the data of another user!");
        }

        // Check if the new email or username is already reserved by another user..
        checkReservedData(userId, principal, user);

        //logger.debug(user.toString());

        user.setPassword(passwordEncoder.encode(user.getPassword()));   // Encrypt the password.

        user.setId(userId); // Make sure the database request contains the "id" field, otherwise a "ERROR: operator does not exist: bigint = bytea" will be thrown!
        int affectedRowsForUser = this.userService.updateUserCredentials(user);
        int affectedRowsForProfile = this.profileService.updateUserProfile(user.getProfile());
        if ((affectedRowsForUser == 0) || (affectedRowsForProfile == 0)) {
            logger.warn("No user was found in DataBase having userId: " + userId);
            return ResponseEntity.badRequest().build();
        } else {
            logger.debug("User info was updated for user with id: " + userId);
            return ResponseEntity.ok().build();
        }
    }


    private void checkReservedData(Long userId, Principal principal, User user) throws UserExistsException {

        userRepository.findByEmail(user.getEmail())
                .ifPresent((storedUser) -> {
                    // If the user to be updated wants to have the email which belongs to another user throw an exception.
                    if (!storedUser.getId().equals(userId)) {
                        logger.warn("OTHER USER! Email: " + user.getEmail() + ", storedUserID = " + storedUser.getId() + ", id = " + userId + ", currentUserId = " + principal.getUser().getId());
                        throw new UserExistsException("A user with the same email \"" + storedUser.getEmail() + "\" already exists!");    // It gets logged inside
                    } else {
                        logger.debug("Email: " + user.getEmail() + ", storedUserID = " + storedUser.getId() + ", id = " + userId + ", currentUserId = " + principal.getUser().getId());
                    }
                });

        userRepository.findByUsername(user.getUsername())
                .ifPresent((storedUser) -> {
                    // If the user to be updated wants to have the username which belongs to another user throw an exception.
                    if (!storedUser.getId().equals(userId)) {
                        logger.warn("OTHER USER! Username: " + user.getUsername() + ", storedUserID = " + storedUser.getId() + " id = " + userId + ", currentUserId = " + principal.getUser().getId());
                        throw new UserExistsException("A user with the same username \"" + storedUser.getUsername() + "\" already exists!");
                    } else {
                        logger.debug("Username: " + user.getUsername() + ", storedUserID = " + storedUser.getId() + ", id = " + userId + ", currentUserId = " + principal.getUser().getId());
                    }
                });
    }


    @PostMapping("/{userId:[\\d]+}/profile_photo")
    @PreAuthorize("hasRole('USER')or hasRole('PROVIDER') or hasRole('ADMIN')")
    public UploadFileResponse uploadProfilePhoto(@RequestParam("file") MultipartFile file, @PathVariable(value = "userId") Long userId, @Valid @CurrentUser Principal principal) {

        // If current user is not Admin and the given "userId" is not the same as the current user requesting, then return error.
        if (!principal.getUser().getId().equals(userId) && !principal.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new NotAuthorizedException("You are not authorized to update the data of another user!");
        }

        // Check if the user which will have its profile_photo changed, exists or not.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistException("User with id <" + userId + "> does not exist!"));

        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            logger.error("Failure when retrieving the filename of the incoming file!");
            return new UploadFileResponse(null, null, null, file.getSize());
        }

        String fileExtension = FilenameUtils.getExtension(fileName)
                                            .toLowerCase(); // Linux are case insensitive, so make all file-extensions to lowerCase.

        // Replace with standard profile_photo name.
        fileName = StringUtils.replace(fileName, fileName, profilePhotoBaseName + "." + fileExtension);

        String fileDownloadURI = profileBaseURI + userId + "/" + fileName;

        // Update database with the new "profile_photo"-name..
        if (profileRepository.updatePictureById(userId, fileDownloadURI) == 0) {
            logger.error("Could not update the picture for user with id: " + userId);
            return new UploadFileResponse(fileName, null, null, file.getSize()); // Don't want to store a file having n relation with the database.. so return..
        } else
            profileRepository.flush(); // We want the DB to be updated immediately.

        // Send file to be stored.
        return fileController.uploadFile(Principal.getInstance(user), file, fileName, "photos", fileDownloadURI);
    }


    @GetMapping("/{userId:[\\d]+}/{file_name:profile_photo(?:.[\\w]{2,4})?}")
    // Maybe no authorization should exist here as the profile photo should be public.
    public ResponseEntity<Resource> getProfilePhoto(@PathVariable(value = "userId") Long userId, @PathVariable(value = "file_name") String file_name, HttpServletRequest request) {
        // We don't use the "file_name" of the path-variable since we set it to be optional (in order to map the following strings: "profile_photo", "profile_photo.png", "profile_photo.jpeg").
        // We want to match the simple "profile_photo"-string in case we don't know the actual filename when requesting (with the file-extension).

        // Check if the user we want to get its profile_photo exists or not.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistException("User with id <" + userId + "> does not exist!"));

        if (fileStoragePath == null) {
            String roleNameDirectory = UsersControllerUtil.getRoleNameDirectory(user);
            fileStoragePath = Paths.get(fileStorageService.getFileStorageLocation() + File.separator + roleNameDirectory).toString();
        }

        String fileFullPath;
        String user_picture = user.getProfile().getPhoto_url();

        if (user_picture == null) {
            logger.debug("No picture was found for user with \"user_id\": {}. Loading the generic one.", userId);
            fileFullPath = localImageDirectory + File.separator + genericPhotoName;
        } else {
            // Parse the URI and get the filename.
            URI uri;
            try {
                uri = new URI(user_picture);
            } catch (Exception e) {
                String errorMsg = "Failed to extract the fileName from the profile_url!";
                logger.error(errorMsg, e);
                fileStoragePath = null;
                throw new ProfilePhotoException(errorMsg);
            }

            String uriStr = uri.toString();
            if ( !uriStr.contains(profileBaseURI) ) {
                String errorMsg = "This uri does not refer to a file existing in this server! - " + uriStr + "";
                logger.error(errorMsg);
                fileStoragePath = null;
                throw new ProfilePhotoException(errorMsg);
            }

            String fileName = uriStr.substring(uriStr.lastIndexOf('/') + 1);
            fileFullPath = fileStoragePath + File.separator + userId + File.separator + "photos" + File.separator + fileName;
        }

        Resource resource;
        try {
            resource = fileStorageService.loadFileAsResource(fileFullPath);
        } catch (FileNotFoundException fnfe) {
            if (user_picture != null) {   // If the dataBase says that this user has its own profilePhoto, but it was not found in storage..
                // Loading the "image_not_found", so that the user will be notified that sth's wrong with the storage of its picture, even though one was given.
                fileFullPath = localImageDirectory + File.separator + imageNotFoundName;
                try {
                    resource = fileStorageService.loadFileAsResource(fileFullPath);
                } catch (FileNotFoundException fnfe2) {
                    String errorMsg = "The \"" + imageNotFoundName + "\" was not found in storage!";
                    logger.error(errorMsg);
                    throw new ProfilePhotoException(errorMsg);
                }
            } else {
                String errorMsg = "The \"" + genericPhotoName + "\" was not found in storage!";
                logger.error(errorMsg);
                throw new ProfilePhotoException(errorMsg);
            }
        }
        finally { // Reset variable on exception.
            fileStoragePath = null;
        }

        return fileController.GetFileResponse(request, resource);
    }
}
