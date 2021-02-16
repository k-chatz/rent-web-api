package gr.uoa.di.rent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gr.uoa.di.rent.exceptions.AppException;
import gr.uoa.di.rent.models.*;
import gr.uoa.di.rent.payload.requests.LoginRequest;
import gr.uoa.di.rent.repositories.*;
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
import java.util.*;
import java.util.stream.Collectors;

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
    AmenitiesRepository amenitiesRepository;

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
        if (userRepository.findByEmail("user@mail.com").isPresent())
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
                "user@mail.com",
                role,
                false,
                false,
                null,
                null
        );

        String name = "Simple";
        String surname = "User";
        Profile profile = new Profile(
                user,
                name,
                surname,
                new Date(),
                "https://ui-avatars.com/api/?name=" + name + "+" + surname + "&rounded=true&%20bold=true&" +
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
        if (userRepository.findByEmail("provider@mail.com").isPresent())
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
                "provider@mail.com",
                role,
                false,
                false,
                null,
                null
        );

        String name = "Mr";
        String surname = "Provider";
        Profile profile = new Profile(
                provider,
                name,
                surname,
                new Date(),
                "https://ui-avatars.com/api/?name=" + name + "+" + surname + "&rounded=true&%20bold=true&" +
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
                "Business_name",
                "info@business_name.com",
                "address",
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
        int numOfHotels = 5;
        int numOfRooms = 3;
        int numOfCalendarsEntriesPerRoom = 2;

        List<Hotel> hotels = new ArrayList<>();
        Hotel hotel;

        List<Room> rooms;
        Room room;

        List<String> names = Arrays.asList(
                "Parrotel Beach Resort Ex. Radisson Blu",
                "Baron Resort Sharm El Sheikh",
                "Xperience St. George Sharm El Sheikh",
                "Solymar Naama Bay",
                "Marina Sharm Hotel (Ex. Helnan Marina)"
        );

        List<String> descriptionLongs = Arrays.asList(
                "Overlooking a private beach and the turquoise waters of Naama Bay, Marina Sharm Hotel offers spacious rooms, most of them with private balconies. There are 2 outdoor pools and a wellness centre. WiFi access to be available at the lobby area and it’s free of charge\n" +
                        "\n" +
                        "Rooms at this 4-star hotel all have floor-to-ceiling windows and modern furnishings. They come equipped with air conditioning, minibar and satellite flat-screen TV. All rooms offer Red Sea views.\n" +
                        "\n" +
                        "Dining options at the Marina Sharm include the Mermaid seafood restaurant, also serving Italian cuisine. The beach bar offers cocktails, refreshing beverages and live music entertainment.\n" +
                        "\n" +
                        "Guests of the Marina Sharm can treat themselves to a rejuvenating massage, or relax in the spa pool, hammam or sauna. The hotel also has a children’s playground, and a games room with billiards.\n" +
                        "\n" +
                        "The Marina Sharm is 11 km from Sharm El Sheikh International Airport, 7 km from the Old Market and a 15-minute drive from the town’s centre. The hotel provides 24-hour front desk service.\n" +
                        "\n" +
                        "Αυτό είναι το αγαπημένο μέρος των επισκεπτών μας στον προορισμό Σαρμ Ελ Σέιχ σύμφωνα με ανεξάρτητα σχόλια.\n" +
                        "\n" +
                        "Το κατάλυμα αυτό βρίσκεται επίσης σε μία από τις τοποθεσίες με την καλύτερη βαθμολογία στο Σαρμ Ελ Σέιχ! Αρέσει περισσότερο στους επισκέπτες σε σχέση με άλλα καταλύματα στην περιοχή.\n" +
                        "\n" +
                        "Η τοποθεσία αρέσει ιδιαίτερα σε ζευγάρια – τη βαθμολόγησαν με 8,8 για ταξίδι δύο ατόμων.",
                "Αυτό το πολυτελές θέρετρο διαθέτει ιδιωτική παραλία, 3 πισίνες, 3 γήπεδα τένις και γήπεδο βόλεϊ. Προσφέρει επίσης 5 εστιατόρια, καθημερινή ψυχαγωγία και θαλάσσια αθλήματα.\n" +
                        "\n" +
                        "Το 5 αστέρων Parrotel Beach Resort παρέχει δωμάτια διακοσμημένα με πλούσια υφάσματα. Όλα έχουν ιδιωτικό μπαλκόνι και δορυφορική τηλεόραση. Στο λόμπι και στα Marine Club δωμάτια παρέχεται δωρεάν WiFi.\n" +
                        "\n" +
                        "Το πλήρως εξοπλισμένο σπα του θερέτρου προσφέρει σάουνες, υδρομασάζ και περιποιήσεις μασάζ από έμπειρο προσωπικό. Οι επισκέπτες μπορούν επίσης να κάνουν μια βουτιά στη μεγάλη εξωτερική πισίνα του θερέτρου, η οποία περιβάλλεται από φοίνικες. Για τους μικρούς επισκέπτες, το θέρετρο διαθέτει το Abracadabra Club που προσφέρει ψυχαγωγία υπό την επίβλεψη προσωπικού.\n" +
                        "\n" +
                        "Τα πολλά εστιατόρια του θερέτρου σερβίρουν ποικιλία εκλεκτών εδεσμάτων, όπως παραδοσιακά αιγυπτιακά πιάτα, υψηλής ποιότητας ιταλικές σπεσιαλιτέ, εξαιρετικά θαλασσινά, καθώς και πίτσα ψημένη σε ξυλόφουρνο. Στα πλήρους εξυπηρέτησης μπαρ και καφέ του θερέτρου μπορείτε να απολαύσετε κοκτέιλ και καφέ.\n" +
                        "\n" +
                        "Το κατάλυμα απέχει 8χλμ. από το Διεθνές Αεροδρόμιο του Σαρμ Ελ Σέιχ και την Πλατεία Soho και 17χλμ. από τον Κόλπο Naama. Καθημερινά οργανώνεται υπηρεσία μεταφοράς κατόπιν αιτήματος.\n" +
                        "\n" +
                        "Το κατάλυμα αυτό βρίσκεται επίσης σε μία από τις τοποθεσίες με την καλύτερη βαθμολογία στο Σαρμ Ελ Σέιχ! Αρέσει περισσότερο στους επισκέπτες σε σχέση με άλλα καταλύματα στην περιοχή.\n" +
                        "\n" +
                        "Η τοποθεσία αρέσει ιδιαίτερα σε ζευγάρια – τη βαθμολόγησαν με 8,7 για ταξίδι δύο ατόμων.\n" +
                        "\n" +
                        "Αυτό το κατάλυμα έχει αξιολογηθεί ως αυτό με την καλύτερη σχέση ποιότητας τιμής στο Σαρμ Ελ Σέιχ! Εδώ τα χρήματα των επισκεπτών έχουν μεγαλύτερη αξία σε σχέση με άλλα καταλύματα στην πόλη.",
                "Αυτό το κομψό, στιλάτο, αριστοκρατικό θέρετρο 5 αστέρων βρίσκεται στο Sharm El Sheikh και απέχει 600μ. από την πανέμορφη, ιδιωτική, μεταξένια, λευκή, αμμώδη παραλία. Διαθέτει πισίνα σε στιλ λιμνοθάλασσας και μια πλατφόρμα μήκους 145μ. για κολύμβηση με αναπνευστήρα και καταδύσεις. Έχει 9 εστιατόρια και μπαρ. Περιλαμβάνει επίσης μεγάλη αμμώδη παραλία, ολυμπιακών διαστάσεων πισίνα με γλυκό νερό (θερμαινόμενη το χειμώνα) και μόνο για ενήλικες, υδρομασάζ με πίδακες με εν μέρει θαλασσινό νερό, καθώς και παιδική πισίνα. Οι επισκέπτες μπορούν να επωφεληθούν από τη δωρεάν υπηρεσία μεταφοράς με λεωφορείο προς το συγκρότημα Soho Square μία φορά την ημέρα, το βράδυ.\n" +
                        "\n" +
                        "Τα δωμάτια του Baron Resort παρέχουν επιλογή μαξιλαριού, δορυφορική τηλεόραση και κλινοσκεπάσματα από 100% αιγυπτιακό βαμβάκι. Ακόμη, διαθέτουν παράθυρα από το δάπεδο μέχρι την οροφή και επιπλωμένο μπαλκόνι. Ορισμένα έχουν θέα στην Ερυθρά Θάλασσα, το νησί Tiran ή τους κήπους του θερέτρου.\n" +
                        "\n" +
                        "Το Baron Resort προσφέρει 9 επιλογές για φαγητό και ποτό, μεταξύ αυτών το παραλιακό Taj Mahal που σερβίρει ινδικές σπεσιαλιτέ. Στο μπαρ Now & Zen Dance θα απολαύσετε εξωτικά κοκτέιλ και μουσική κατά τις βραδινές ώρες. Επίσης, διοργανώνονται διάφορες θεματικές βραδιές στα εστιατόρια, όπως παραδοσιακά δείπνα Βεδουίνων. Στο Al Sakia Seafood Restaurant και το Oasis Pool Restaurant & Bar μπορείτε να απολαύσετε ζωντανό ψυχαγωγικό πρόγραμμα.\n" +
                        "\n" +
                        "Στο κέντρο ευεξίας και χαλάρωσης του Baron Resort διατίθενται περιτυλίξεις σώματος με φύκια και αιγυπτιακά μασάζ. Επίσης, υπάρχουν γυμναστήριο και ατμόλουτρα. Στο θέρετρο έχετε τη δυνατότητα να επιδοθείτε και σε άλλες δραστηριότητες, όπως beach volley, τρέξιμο και ποδηλασία σε πίστα. Για τους μικρούς επισκέπτες, υπάρχει παιδική λέσχη με χώρο ύπνου και προσωπικό επιτήρησης.\n" +
                        "\n" +
                        "Το 24ωρο προσωπικό μπορεί να οργανώσει εκδρομές στο εθνικό πάρκο Ras Mohammed, το οποίο απέχει 1 ώρα και 30 λεπτά με το σκάφος. Το Baron Resort Sharm El Sheikh βρίσκεται σε απόσταση 3χλμ. από τις επιλογές διασκέδασης του Soho Square και 15χλμ. από την περιοχή Naama Bay. Το Διεθνές Αεροδρόμιο Sharm El Sheikh είναι προσβάσιμο σε 8χλμ. από το κατάλυμα.\n" +
                        "\n" +
                        "Η τοποθεσία αρέσει ιδιαίτερα σε ζευγάρια – τη βαθμολόγησαν με 8,1 για ταξίδι δύο ατόμων.\n" +
                        "\n" +
                        "Αυτό το κατάλυμα έχει αξιολογηθεί ως αυτό με την καλύτερη σχέση ποιότητας τιμής στο Σαρμ Ελ Σέιχ! Εδώ τα χρήματα των επισκεπτών έχουν μεγαλύτερη αξία σε σχέση με άλλα καταλύματα στην πόλη.",
                "Αυτό το θέρετρο 4 αστέρων βρίσκεται σε απόσταση 2χλμ. από την Ερυθρά Θάλασσα και διαθέτει ιδιωτική αμμώδη παραλία. Το κατάλυμα έχει πρόσβαση σε 2 παραλίες, η μία με αμμουδιά και η άλλη με κοράλλια στο βυθό για κολύμβηση με αναπνευστήρα και καταδύσεις. Θα βρείτε ακόμη 4 εξωτερικές πισίνες, ηλιόλουστη βεράντα και μια ιδιωτική παραλία με χρυσή αμμουδιά, όπου λειτουργεί παραθαλάσσιο εστιατόριο-beach bar. Το Εθνικό Πάρκο Ras Mohammad είναι 30χλμ. μακριά.\n" +
                        "\n" +
                        "Το Xperience St. George προσφέρει κλιματιζόμενα δωμάτια με ιδιωτικό μπάνιο. Όλα τα δωμάτια περιλαμβάνουν δορυφορική τηλεόραση και επιφάνεια εργασίας.\n" +
                        "\n" +
                        "Οι επισκέπτες μπορούν να απολαύσουν το πρωινό τους στο εστιατόριο του ξενοδοχείου ή στο δωμάτιό τους. Το Xperience φιλοξενεί ένα εστιατόριο à la carte και ένα εστιατόριο με μπουφέ, ενώ ετοιμάζει και γεύματα σε πακέτο. Προσφέρονται επίσης μενού ειδικής διατροφής, κατόπιν αιτήματος.\n" +
                        "\n" +
                        "Το γυμναστήριο και το σπα του ξενοδοχείου, το οποίο περιλαμβάνει σάουνα και υδρομασάζ, είναι ιδανικά για χαλάρωση. Το St. George Homestay διαθέτει επίσης εγκαταστάσεις θαλάσσιων σπορ, πινγκ πονγκ, μπιλιάρδου και βόλεϊ.\n" +
                        "\n" +
                        "Το Διεθνές Αεροδρόμιο του Sharm el-Sheikh απέχει μόλις 18 χιλιόμετρα. Το ξενοδοχείο μπορεί να οργανώσει υπηρεσία μεταφοράς και παρέχει δωρεάν ιδιωτικό χώρο στάθμευσης στις εγκαταστάσεις του. Ο Κόλπος Naama, προς τον οποίο προσφέρεται δωρεάν υπηρεσία μεταφοράς, είναι 5χλμ. μακριά.\n" +
                        "\n" +
                        "Αυτό το κατάλυμα έχει αξιολογηθεί ως αυτό με την καλύτερη σχέση ποιότητας τιμής στο Σαρμ Ελ Σέιχ! Εδώ τα χρήματα των επισκεπτών έχουν μεγαλύτερη αξία σε σχέση με άλλα καταλύματα στην πόλη.",
                "Το Solymar Naama Bay βρίσκεται σε ιδανική τοποθεσία, στο κέντρο του δημοφιλούς Κόλπου Naama, σε απόσταση μόλις 10 λεπτών από το Σαρμ. Το κατάλυμα προσφέρει WiFi κατόπιν αιτήματος, εξωτερική πισίνα και πινγκ-πονγκ. Το Διεθνές Αεροδρόμιο Sharm El Sheikh είναι προσβάσιμο σε 10 λεπτά οδικώς.\n" +
                        "\n" +
                        "Όλα τα δωμάτια του Solymar Naama Bay διαθέτουν μπαλκόνι, τηλεόραση επίπεδης οθόνης και μίνι μπαρ. Το μπάνιο περιλαμβάνει ντους και μπανιέρα.\n" +
                        "\n" +
                        "Μπορείτε να απολαύσετε διάφορα πιάτα στο κεντρικό εστιατόριο, που έχει μεγάλη βεράντα με θέα στον Κόλπο Naama και την Ερυθρά Θάλασσα. Τα 2 μπαρ του θέρετρου σερβίρουν διάφορα ποτά. Στις εγκαταστάσεις του καταλύματος προσφέρονται διάφορες δραστηριότητες, όπως κολύμβηση με αναπνευστήρα, καταδύσεις και ιστιοσανίδα.\n" +
                        "\n" +
                        "Το Εθνικό Πάρκο Ras Mohammed απέχει 16χλμ. από το κατάλυμα. Παρέχεται δωρεάν υπηρεσία μεταφοράς με λεωφορείο προς την παραλία. Μπορείτε να κάνετε καταδύσεις, κανό και ιππασία κοντά στο ξενοδοχείο, με πρόσθετη χρέωση.\n" +
                        "\n" +
                        "Αυτό είναι το αγαπημένο μέρος των επισκεπτών μας στον προορισμό Σαρμ Ελ Σέιχ σύμφωνα με ανεξάρτητα σχόλια."
        );

        for (int i = 0; i < numOfHotels; i++)
        {
            hotel = new Hotel(business, names.get(i), "info@" + String.format("hotel_%d", i + 1) + ".com", numOfRooms, 100 * i, 100 * i, "Short Description " + i, descriptionLongs.get(i), i%3+2.5);
            rooms = new ArrayList<>();  // (Re)declare the list to add the new rooms (and throw away the previous).

            for (int j = 0; j < numOfRooms; j++) {

                int interval = 0;

                room = new Room((j + 1), hotel, 2 + (i % 4), 50 + (i % 6) * 50);
                /*List<Calendar> calendars = new ArrayList<>();  // (Re)declare the list to add the new calendars (and throw away the previous).

                // rooms will be booked from ( 2 days from now  to  30 days from now )
                for (int k = 0; k < numOfCalendarsEntriesPerRoom; k++) {
                    LocalDate startDate = LocalDate.now().plusMonths(interval);
                    LocalDate endDate = LocalDate.now().plusMonths(interval+1);

                    calendars.add(new Calendar(startDate, endDate, room));
                    interval = interval + 2;
                }
                room.setCalendars(calendars);*/
                rooms.add(room);
            }


            Collection<Amenity> amenities = new ArrayList<>(amenitiesRepository.findAll());

            // Assign rooms to hotel.
            hotel.setRooms(rooms);
            hotel.setAmenities(amenities);
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
            String email = "emailU" + (i + 1) + "@mail.com";

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

            String name = "Rent_" + i;
            String surname = "Cube_" + i;
            Profile profile = new Profile(
                    user,
                    name,
                    surname,
                    new Date(),
                    "https://ui-avatars.com/api/?name=" + name + "+" + surname + "&rounded=true&%20bold=true&" +
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
    public void insertHotel(HotelRepository hotelRepository, BusinessRepository businessRepository, RoomRepository roomRepository) {
        // Get a business for the hotel:
        Business business = businessRepository.findById((long) 1).orElse(null);
        // Create the hotel:
        Hotel newHotel = new Hotel(business,
                "Blue Dolphin",
                "info@blue_dolphin.com",
                100,
                30.8,
                25.7,
                "Nice hotel",
                "Very nice hotel",
                4.3);

        hotelRepository.save(newHotel);

        // Create the rooms:
        for (int i = 1; i <= 30; i++) {
            Room room = new Room(i, newHotel, 2, 100);
            roomRepository.save(room);
        }
        for (int i = 1; i <= 30; i++) {
            Room room = new Room(i, newHotel, 3, 100);
            roomRepository.save(room);
        }
        for (int i = 1; i <= 30; i++) {
            Room room = new Room(i, newHotel, 4, 100);
            roomRepository.save(room);
        }
    }

}
