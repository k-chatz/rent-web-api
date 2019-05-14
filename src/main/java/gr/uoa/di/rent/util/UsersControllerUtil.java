package gr.uoa.di.rent.util;

import gr.uoa.di.rent.models.User;
import org.springframework.util.StringUtils;

public class UsersControllerUtil {

    public static String getRoleNameDirectory(User user) {
        String role = user.getRole().getName().name().toLowerCase();
        role = StringUtils.replace(role, "role_", "");
        return (role + "s");
    }
}
