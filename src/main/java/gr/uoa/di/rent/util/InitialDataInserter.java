package gr.uoa.di.rent.util;


import gr.uoa.di.rent.exceptions.AppException;
import gr.uoa.di.rent.models.*;
import gr.uoa.di.rent.repositories.RoleRepository;
import gr.uoa.di.rent.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        if (userRepo.findByEmail("admin@rentcube.com").isPresent())
            return;

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

    public void insertUser(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {

        // Insert the user if not exist.
        if (userRepo.findByEmail("user@gmail.com").isPresent())
            return;

        // Assign an user role
        Role role = roleRepo.findByName(RoleName.ROLE_USER);
        if (role == null) {
            throw new AppException("User Role not set.");
        }

        User user_temp = new User("user",
                passwordEncoder.encode("asdfk2.daADd"),
                "user@gmail.com",
                role,
                false,
                false,
                null
        );

        Profile profile = new Profile(
                "Simple",
                "User",
                new Date(),
                "https://ui-avatars.com/api/?name=Rent+Cube&rounded=true&%20bold=true&" +
                        "background=a8d267&color=000000"
        );

        user_temp.setProfile(profile);
        profile.setOwner(user_temp);
        userRepo.save(user_temp);
    }

    public void insertProvider(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {

        // Insert the provider if not exist.
        if (userRepo.findByEmail("provider@gmail.com").isPresent())
            return;

        // Assign an provider role
        Role role = roleRepo.findByName(RoleName.ROLE_PROVIDER);
        if (role == null) {
            throw new AppException("Provider Role not set.");
        }

        User user_temp = new User("provider",
                passwordEncoder.encode("asdfk2.daADd"),
                "provider@gmail.com",
                role,
                false,
                false,
                null
        );

        Profile profile = new Profile(
                "Mr",
                "Provider",
                new Date(),
                "https://ui-avatars.com/api/?name=Rent+Cube&rounded=true&%20bold=true&" +
                        "background=a8d267&color=000000"
        );

        user_temp.setProfile(profile);
        profile.setOwner(user_temp);

        Business business = createTestBusiness(user_temp);

        // Assign the business to the provider
        user_temp.setBusiness(business);

        userRepo.save(user_temp);
    }


    private Business createTestBusiness(User provider) {

        // Create business.
        Business business = new Business("Business_name", "address", "tax_number", "tax_office", "owner_name",
                "owner_surname", "owner_patronym", "id_card_number", new Date(), "residence_address", provider);


        // Create 2 hotels each having 3 rooms.

        List<Hotel> hotels = new ArrayList<>();

        Hotel hotel;
        Room room;

        // First hotel
        hotel = new Hotel(provider, business, 10, "10", "10", "Short Description", "Long Description", "4.5");

        List<Room> rooms = new ArrayList<>();
        room = new Room(hotel, 80, 2);
        rooms.add(room);

        room = new Room(hotel, 90, 3);
        rooms.add(room);

        room = new Room(hotel, 100, 4);
        rooms.add(room);

        hotel.setRooms(rooms);
        hotels.add(hotel);


        // Second hotel
        hotel = new Hotel(provider, business, 100, "101", "100", "Short Description", "Long Description", "3.5");

        rooms = new ArrayList<>();
        room = new Room(hotel, 40, 2);
        rooms.add(room);

        room = new Room(hotel, 50, 3);
        rooms.add(room);

        room = new Room(hotel, 60, 4);
        rooms.add(room);

        hotel.setRooms(rooms);

        hotels.add(hotel);

        // Assign the hotels to the business
        business.setHotels(hotels);

        return business;
    }
}
