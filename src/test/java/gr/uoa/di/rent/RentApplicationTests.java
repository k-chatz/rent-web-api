package gr.uoa.di.rent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gr.uoa.di.rent.exceptions.AppException;
import gr.uoa.di.rent.models.*;
import gr.uoa.di.rent.payload.requests.LoginRequest;
import gr.uoa.di.rent.repositories.RoleRepository;
import gr.uoa.di.rent.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = {Application.class})
public class RentApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(RentApplicationTests.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void loginWithCorrectCredentials() throws Exception {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String s = gson.toJson(new LoginRequest("admin@rentcube.com", "asdfk2.daADd"));

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s)
        ).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void insertUser() {

        // Insert the user if not exist.
        if (userRepository.findByEmail("user@gmail.com").isPresent())
            return;

        // Assign an user role
        Role role = roleRepository.findByName(RoleName.ROLE_USER);
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
        userRepository.save(user_temp);
    }

    @Test
    public void insertProvider() {

        // Insert provider (with a business and two hotels, each hotel having 3 rooms)

        // Insert the provider if not exist.
        if (userRepository.findByEmail("provider@gmail.com").isPresent())
            return;

        // Assign an provider role
        Role role = roleRepository.findByName(RoleName.ROLE_PROVIDER);
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

        userRepository.save(user_temp);
    }

    // Not a SpringTest. This is called by the "insertProvider()"-Test.
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

    @Test
    public void createRandomUsers() throws Exception {

        /* Create dummy users */
        int number_of_users = 100;
        Role role = roleRepository.findByName(RoleName.ROLE_USER);

        for (int i = 0; i < number_of_users; i++) {

            if(i==number_of_users/2)
                role = roleRepository.findByName(RoleName.ROLE_PROVIDER);

            String username = "kalampakas" + i;
            String email = "kalamapakas"+ i +"@gmail.com";

            Optional<List<User>> user = userRepository.findByUsernameOrEmail(username, email);
            if (user.isPresent())
                continue;

            User user_temp = new User(username,
                    passwordEncoder.encode("asdfk2.daADd"),
                    email,
                    role,
                    false,
                    false,
                    null
            );

            Profile profile = new Profile(
                    "Rent" + i,
                    "Cube" + i,
                    new Date(),
                    "https://ui-avatars.com/api/?name=Rent+Cube&rounded=true&%20bold=true&" +
                            "background=a8d267&color=00000" + 1
            );

            user_temp.setProfile(profile);
            profile.setOwner(user_temp);
            userRepository.save(user_temp);
        }
    }
}
