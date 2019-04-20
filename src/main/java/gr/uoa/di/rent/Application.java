package gr.uoa.di.rent;

import gr.uoa.di.rent.models.Role;
import gr.uoa.di.rent.models.RoleName;
import gr.uoa.di.rent.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // Insert some initial-data into the repository.
    @Autowired
    private RoleRepository roleRepository;

    @Bean
    public CommandLineRunner demoData(RoleRepository repo) {
        return args -> {
            // Insert the RoleNames if they don't exist.
            if ( roleRepository.findByName(RoleName.ADMIN) == null ) {
                repo.save(new Role(0, RoleName.ADMIN));
            }
            if ( roleRepository.findByName(RoleName.USER) == null ) {
                repo.save(new Role(0, RoleName.USER));
            }
        };
    }
}
