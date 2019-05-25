package gr.uoa.di.rent.util;

import gr.uoa.di.rent.models.Hotel;
import gr.uoa.di.rent.models.User;

public class ModelMapper {

    public static User mapUserToUserResponse(User user) {
        return user;
       /* return new UserResponse(new User(user.getId(), user.getUsername(), user.getPassword(),
                user.getEmail(), user.getRole(), user.getLocked(), user.getPendingProvider(),
                user.getProfile(), null));*/
    }

    public static Hotel mapHoteltoHotelResponse(Hotel hotel) {
        return hotel;
    }
}
