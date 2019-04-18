package gr.uoa.di.rent.repositories;

import gr.uoa.di.rent.models.Role;
import gr.uoa.di.rent.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(RoleName roleName);
    
}
