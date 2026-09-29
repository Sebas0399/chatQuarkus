package Application.Entities;

import lombok.Data;

@Data
public class PagedRequest<T> {
    private int page = 0;
    private int size = 10;
    private T filter;
}
