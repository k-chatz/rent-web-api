package gr.uoa.di.rent.util;

import gr.uoa.di.rent.exceptions.BadRequestException;


public class PaginatedResponseUtil {

    public static <T> void validateParameters(int page, int size, String field, Class<T> tClass)
            throws InstantiationException, IllegalAccessException, BadRequestException {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }

        T t_class = tClass.newInstance();

        try {
            t_class.getClass().getDeclaredField(field);
        } catch (Exception e) {
            throw new BadRequestException("Invalid field. The field must belong to the '" + t_class.getClass().getSimpleName() + "' class!");
        }
    }
}
