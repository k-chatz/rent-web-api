package gr.uoa.di.rent.util;

public interface AppConstants {
    int DEFAULT_PAGE_NUMBER = 0;
    int DEFAULT_PAGE_SIZE = 10;

    // Used to return both users and provider in paginated responses.
    // Otherwise, if role=2 ,then only the users will be returned. If role=3 then only the providers will be returned. (the role=1 refers to the ADMIN who is never returned)
    int DEFAULT_ROLE = -1;
    String DEFAULT_ORDER = "asc";
    String DEFAULT_FIELD = "id";

    int MAX_PAGE_SIZE = 50;
}
