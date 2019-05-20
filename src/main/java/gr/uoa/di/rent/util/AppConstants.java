package gr.uoa.di.rent.util;

import java.time.LocalDate;

public interface AppConstants {

    int DEFAULT_PAGE_NUMBER = 0;
    int DEFAULT_PAGE_SIZE = 10;

    // Used to return both users and provider in paginated responses.
    // Otherwise, if role=2 ,then only the users will be returned. If role=3 then only the providers will be returned. (the role=1 refers to the ADMIN who is never returned)
    int DEFAULT_ROLE = -1;
    String DEFAULT_ORDER = "asc";
    String DEFAULT_FIELD = "id";

    // Default hotel search bar filters
    LocalDate DEFAULT_START_DATE = LocalDate.now();
    LocalDate DEFAULT_END_DATE = LocalDate.now().plusDays(3);
    int DEFAULT_VISITORS_NUMBER = 2;

    int MAX_PAGE_SIZE = 50;
}
