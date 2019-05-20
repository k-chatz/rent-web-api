package gr.uoa.di.rent.util;


import gr.uoa.di.rent.exceptions.AppException;
import gr.uoa.di.rent.models.*;
import gr.uoa.di.rent.repositories.*;
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

    public void insertAdminWithRentCubeBusiness(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {

        // Insert the admin if not exist.
        if (userRepo.findByEmail("admin@rentcube.com").isPresent())
            return;

        // Assign an admin role
        Role role = roleRepo.findByName(RoleName.ROLE_ADMIN);
        if (role == null) {
            throw new AppException("Admin Role not set.");
        }

        User admin = new User(
                null,
                "admin",
                passwordEncoder.encode("asdfk2.daADd"),
                "admin@rentcube.com",
                role,
                false,
                false,
                null,
                null    // We don't care to create personal wallet for the admin. He will manage the RentCube's wallet.
        );

        Profile profile = new Profile(
                admin,
                "Admin",
                "Administrator",
                new Date(),
                "https://ui-avatars.com/api/?name=Rent+Cube&rounded=true&%20bold=true&" +
                        "background=a8d267&color=000000"
        );

        admin.setProfile(profile);

        /*
            Create the business "RentCube", which will be owned by the Admin.
            In every transaction a user makes to book a room, part of the money (or an extra amount) will be deposited in the RentCube's wallet.
            This way the site will gain money from the transactions.
        */
        Business business = new Business(
                "RentCube",
                "Mesogeiwn 35, Athens",   // Random choice.
                "54390",
                "D.O.Y. Athens",
                "owner_name",
                "owner_surname",
                "owner_patronym",
                "id_card_number",
                new Date(),
                "Aga8okleous 67, Athens",   // Random choice.
                admin,
                null
        );

        Wallet businessWallet = new Wallet(business, 0.0);   // nope
        business.setWallet(businessWallet);

        admin.setBusiness(business);

        userRepo.save(admin);
    }

}

