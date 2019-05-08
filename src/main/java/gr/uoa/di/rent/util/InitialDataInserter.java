package gr.uoa.di.rent.util;


import gr.uoa.di.rent.exceptions.AppException;
import gr.uoa.di.rent.models.*;
import gr.uoa.di.rent.repositories.HotelRepository;
import gr.uoa.di.rent.repositories.RoleRepository;
import gr.uoa.di.rent.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

/**
 * This class contains methods to fill the database with some initial data.
 */
public class InitialDataInserter {

    public void insertRoles(RoleRepository roleRepo) {

        // Insert the RoleNames if they don't exist.
        if (roleRepo.findByName(RoleName.ROLE_ADMIN) == null) {
            roleRepo.save(new Role(RoleName.ROLE_ADMIN));
        }
        if (roleRepo.findByName(RoleName.ROLE_USER) == null) {
            roleRepo.save(new Role(RoleName.ROLE_USER));
        }
        if (roleRepo.findByName(RoleName.ROLE_PROVIDER) == null) {
            roleRepo.save(new Role(RoleName.ROLE_PROVIDER));
        }
    }

    public void insertAdmin(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {

        // Insert the admin if not exist.
        if (!userRepo.findByEmail("admin@rentcube.com").isPresent()) {

            // Assign an admin role
            Role role = roleRepo.findByName(RoleName.ROLE_ADMIN);
            if (role == null) {
                throw new AppException("Admin Role not set.");
            }

            User user_temp = new User("admin",
                    passwordEncoder.encode("asdfk2.daADd"),
                    "admin@rentcube.com",
                    role,
                    false,
                    false,
                    null
            );

            Profile profile = new Profile(
                    "Rent",
                    "Cube",
                    new Date(),
                    "https://ui-avatars.com/api/?name=Rent+Cube&rounded=true&%20bold=true&" +
                            "background=a8d267&color=000000"
            );

            user_temp.setProfile(profile);

            profile.setOwner(user_temp);

            userRepo.save(user_temp);
        }
    }

    public void insertHotel(HotelRepository hotelRepo, UserRepository userRepo) {

        // Create hotel example:
        String shortD = "Short Description";
        String longD = "Long Description";
        hotelRepo.save(new Hotel(userRepo.findByEmail("admin@rentcube.com").orElse(null), "10",
                "10", "10", shortD, longD, "4.5"));
    }
}
