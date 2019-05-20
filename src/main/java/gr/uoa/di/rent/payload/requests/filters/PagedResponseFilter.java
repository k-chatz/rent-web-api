package gr.uoa.di.rent.payload.requests.filters;

import gr.uoa.di.rent.util.AppConstants;

public class PagedResponseFilter {

    private int page = AppConstants.DEFAULT_PAGE_NUMBER;

    private int size = AppConstants.DEFAULT_PAGE_SIZE;

    private String sort_field = AppConstants.DEFAULT_FIELD;

    private String order = AppConstants.DEFAULT_ORDER;

    public PagedResponseFilter() {
    }

    public PagedResponseFilter(int page, int size, String sort_field, String order) {
        this.page = page;
        this.size = size;
        this.sort_field = sort_field;
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

    public String getSort_field() {
        return sort_field;
    }

    public void setSort_field(String sort_field) {
        this.sort_field = sort_field;
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
                ", sort_field='" + sort_field + '\'' +
                ", order='" + order + '\'' +
                '}';
    }
}
