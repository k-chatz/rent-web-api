package gr.uoa.di.rent;


import gr.uoa.di.rent.properties.FileStorageProperties;
import gr.uoa.di.rent.repositories.*;
import gr.uoa.di.rent.services.HotelService;
import gr.uoa.di.rent.util.InitialDataInserter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Insert the required initial-data into the repository.
    @Bean
    public CommandLineRunner insertInitialData(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder passwordEncoder,
                                               HotelService hotelService, BusinessRepository businessRepository, RoomRepository roomRepository) {
        return args -> {
            InitialDataInserter initDataInserter = new InitialDataInserter();

            // Insert the RoleNames if they don't exist.
            initDataInserter.insertRoles(roleRepo);

            // Insert the admin if not exist.
            initDataInserter.insertAdminWithRentCubeBusiness(userRepo, roleRepo, passwordEncoder);
            initDataInserter.insertHotel(hotelService, businessRepository, roomRepository);
        };
    }
}
