package gr.uoa.di.rent.payload.requests.filters;
import gr.uoa.di.rent.util.AppConstants;

public class PagedUsersFilter extends PagedResponseFilter {

    private int role = AppConstants.DEFAULT_ROLE;

    public PagedUsersFilter() {
        super();
    }

    public PagedUsersFilter(int page, int size, String field, String order, int role) {
        super(page, size, field, order);
        this.role = role;
    }


    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "PagedUsersFilter{" +
                "role=" + role +
                '}';
    }
}
