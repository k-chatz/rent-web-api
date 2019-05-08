package gr.uoa.di.rent.payload.requests.filters;

import gr.uoa.di.rent.util.AppConstants;

public class PagedResponseFilter {

    private int page = AppConstants.DEFAULT_PAGE_NUMBER;

    private int size = AppConstants.DEFAULT_PAGE_SIZE;

    private int role = AppConstants.DEFAULT_ROLE;

    private String field = AppConstants.DEFAULT_FIELD;

    private String order = AppConstants.DEFAULT_ORDER;

    public PagedResponseFilter() {
    }

    public PagedResponseFilter(int page, int size, int role, String field, String order) {
        this.page = page;
        this.size = size;
        this.role = role;
        this.field = field;
        this.order = order;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "PagedResponseFilter{" +
                "page='" + page + '\'' +
                ", size='" + size + '\'' +
                ", role='" + role + '\'' +
                ", field='" + field + '\'' +
                ", order='" + order + '\'' +
                '}';
    }
}
