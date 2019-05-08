package gr.uoa.di.rent.util;

import gr.uoa.di.rent.models.User;
import gr.uoa.di.rent.payload.responses.UserResponse;

public class ModelMapper {

    public static User mapUserToUserResponse(User user) {
        //return new UserResponse(user);
        return user;
    }

}
