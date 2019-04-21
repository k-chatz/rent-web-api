package gr.uoa.di.rent;

import gr.uoa.di.rent.exceptions.AppException;
import gr.uoa.di.rent.models.Role;
import gr.uoa.di.rent.models.RoleName;
import gr.uoa.di.rent.models.User;
import gr.uoa.di.rent.repositories.RoleRepository;
import gr.uoa.di.rent.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // Insert some initial-data into the repository.
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initialData(RoleRepository roleRepo, UserRepository userRepo) {
        return args -> {
            // Insert the RoleNames if they don't exist.
            if ( roleRepository.findByName(RoleName.ADMIN) == null ) {
                roleRepository.save(new Role(0, RoleName.ADMIN));
            }
            if ( roleRepository.findByName(RoleName.USER) == null ) {
                roleRepository.save(new Role(0, RoleName.USER));
            }

            // Insert the admin if not exist.
            if ( !userRepository.findByEmail("admin@mail.com").isPresent() ) {
                User user = new User("admin", passwordEncoder.encode("123456"), "admin@mail.com", "admin", "admin", new Date(), false, null);
                // Assign an admin role
                Role role = roleRepository.findByName(RoleName.ADMIN);
                if (role == null) {
                    throw new AppException("Admin Role not set.");
                }
                user.setRole(role);
                userRepository.save(user);
            }
        };
    }
}
