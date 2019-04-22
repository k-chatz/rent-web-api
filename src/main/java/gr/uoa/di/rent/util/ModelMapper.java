package gr.uoa.di.rent.util;

import gr.uoa.di.rent.models.User;
import gr.uoa.di.rent.payload.responses.UserResponse;

public class ModelMapper {

    public static UserResponse mapUserToUserResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(),
                user.getName(), user.getSurname(), user.getBirthday(), user.getRole(), user.getLocked(), user.getPhoto_profile());
    }

}
