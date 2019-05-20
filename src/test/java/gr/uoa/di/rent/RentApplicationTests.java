package gr.uoa.di.rent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gr.uoa.di.rent.exceptions.AppException;
import gr.uoa.di.rent.models.*;
import gr.uoa.di.rent.payload.requests.LoginRequest;
import gr.uoa.di.rent.repositories.BusinessRepository;
import gr.uoa.di.rent.repositories.RoleRepository;
import gr.uoa.di.rent.repositories.RoomRepository;
import gr.uoa.di.rent.repositories.UserRepository;
import gr.uoa.di.rent.services.HotelService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void loginAsAdmin() throws Exception {
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
        if (userRepository.findByEmail("user@testmail.com").isPresent())
            return;

        // Assign an user role
        Role role = roleRepository.findByName(RoleName.ROLE_USER);
        if (role == null) {
            throw new AppException("User Role not set.");
        }

        User user = new User(
                null,
                "user",
                passwordEncoder.encode("asdfk2.daADd"),
                "user@gmail.com",
                role,
                false,
                false,
                null,
                null
        );

        Profile profile = new Profile(
                user,
                "Simple",
                "User",
                new Date(),
                "https://ui-avatars.com/api/?name=Rent+Cube&rounded=true&%20bold=true&" +
                        "background=a8d267&color=000000"
        );
        user.setProfile(profile);

        // Create wallet
        Wallet wallet = new Wallet(user, 1000.00);
        user.setWallet(wallet);

        userRepository.save(user);
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

        User provider = new User(
                null,
                "provider",
                passwordEncoder.encode("asdfk2.daADd"),
                "provider@gmail.com",
                role,
                false,
                false,
                null,
                null
        );

        Profile profile = new Profile(
                provider,
                "Mr",
                "Provider",
                new Date(),
                "https://ui-avatars.com/api/?name=Rent+Cube&rounded=true&%20bold=true&" +
                        "background=a8d267&color=000000"
        );
        provider.setProfile(profile);

        // Create wallet
        Wallet wallet = new Wallet(provider, 0.00);
        provider.setWallet(wallet);

        Business business = createTestBusiness(provider);

        // Assign the business to the provider
        provider.setBusiness(business);

        userRepository.save(provider);
    }

    // Not a SpringTest. This is called by the "insertProvider()"-Test.
    private Business createTestBusiness(User provider) {

        // Create business.
        Business business = new Business(
                "Business_name", "address",
                "tax_number",
                "tax_office",
                "owner_name",
                "owner_surname",
                "owner_patronym",
                "id_card_number",
                new Date(),
                "residence_address",
                provider,
                new Wallet(provider, 0.00)
        );

        // Create 2 hotels each having 3 rooms.
        int numOfHotels = 2;
        int numOfRooms = 3;
        int numOfCalendarsEntriesPerRoom = 2;

        List<Hotel> hotels = new ArrayList<>();
        Hotel hotel;

        List<Room> rooms;
        Room room;

        for (int i = 0; i < numOfHotels; i++)
        {
            hotel = new Hotel(business, String.format("hotel_%d", i + 1), numOfRooms, String.format("10%d", i), String.format("10%d", i), "--Short Description--", "--Long Description--", "4.5");
            rooms = new ArrayList<>();  // (Re)declare the list to add the new rooms (and throw away the previous).

            for (int j = 0; j < numOfRooms; j++)
            {
                room = new Room((j+1), hotel, 2 + (i%4), 50 +  ( i%6 ) * 50);
                List<Calendar> calendars = new ArrayList<>();  // (Re)declare the list to add the new calendars (and throw away the previous).

                // rooms will be booked from ( 2 days from now  to  30 days from now )
                for (int k = 0; k < numOfCalendarsEntriesPerRoom; k++)
                {
                    LocalDate startDate = LocalDate.now().plusDays(2);
                    LocalDate endDate = LocalDate.now().plusMonths(1);

                    calendars.add(new Calendar(startDate, endDate, room));
                }
                room.setCalendars(calendars);
                rooms.add(room);
            }

            // Assign rooms to hotel.
            hotel.setRooms(rooms);
            hotels.add(hotel);
        }

        // Assign the hotels to the business
        business.setHotels(hotels);

        return business;
    }

    @Test
    public void createRandomUsers() {

        /* Create dummy users */
        int number_of_users = 30;
        Role role = roleRepository.findByName(RoleName.ROLE_USER);

        for (int i = 0; i < number_of_users; i++) {

            if (i == number_of_users / 2)
                role = roleRepository.findByName(RoleName.ROLE_PROVIDER);

            String username = "user_" + (i + 1);
            String email = "emailU_" + (i + 1) + "_@mail.com";

            // Continue if already exists
            if (userRepository.findByUsernameOrEmail(username, email).isPresent())
                continue;

            User user = new User(
                    null,
                    username,
                    passwordEncoder.encode("asdfk2.daADd"),
                    email,
                    role,
                    false,
                    false,
                    null,
                    null
            );

            Profile profile = new Profile(
                    user,
                    "Rent_" + i,
                    "Cube_" + i,
                    new Date(),
                    "https://ui-avatars.com/api/?name=Rent+Cube&rounded=true&%20bold=true&" +
                            "background=a8d267&color=00000" + 1
            );
            user.setProfile(profile);

            // Create wallet
            //half of them will have 1000 balance, others 1000
            //Providers also have money and a wallet, but no business       (TODO)
            Wallet wallet = new Wallet(user, (double) (1000 + ((i % 2 == 0 ? 1000 : 0))));
            user.setWallet(wallet);

            userRepository.save(user);
        }
    }

    //@Test
    public void insertHotel(HotelService hotelService, BusinessRepository businessRepository, RoomRepository roomRepository) {
        // Get a business for the hotel:
        Business business = businessRepository.findById((long) 1).orElse(null);
        // Create the hotel:
        Hotel newHotel = new Hotel(business,
                "Blue Dolphin",
                100,
                "0.5",
                "0.6",
                "Nice hotel",
                "Very nice hotel",
                "4");

        hotelService.createHotel(newHotel);

        // Create the rooms:
        for(int i = 1; i <= 30; i++) {
            Room room = new Room(i, newHotel, 2, 100);
            roomRepository.save(room);
        }
        for(int i = 1; i <= 30; i++) {
            Room room = new Room(i, newHotel, 3, 100);
            roomRepository.save(room);
        }
        for(int i = 1; i <= 30; i++) {
            Room room = new Room(i, newHotel, 4, 100);
            roomRepository.save(room);
        }
    }
}
