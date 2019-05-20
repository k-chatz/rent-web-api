package gr.uoa.di.rent.util;

import gr.uoa.di.rent.models.User;

public class ModelMapper {

    public static User mapUserToUserResponse(User user) {
        return user;
       /* return new UserResponse(new User(user.getId(), user.getUsername(), user.getPassword(),
                user.getEmail(), user.getRole(), user.getLocked(), user.getPending_provider(),
                user.getProfile(), null));*/
    }

}
