package gr.uoa.di.rent.services;

import gr.uoa.di.rent.models.User;
import gr.uoa.di.rent.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public int updateUserData(User user) {
        return userRepository.updateUserData(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getName(), user.getSurname(), user.getBirthday(), user.getPhoto_profile());
    }
}
