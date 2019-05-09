package gr.uoa.di.rent.util;

import gr.uoa.di.rent.models.User;
import gr.uoa.di.rent.payload.responses.UserResponse;

public class ModelMapper {

    public static User mapUserToUserResponse(User user) {
        return user;
       /* return new UserResponse(new User(user.getId(), user.getUsername(), user.getPassword(),
                user.getEmail(), user.getRole(), user.getLocked(), user.getPending_provider(),
                user.getProfile(), null));*/
    }

}
