package goRest.Model;

import java.util.List;

public class CommentsMeta {

    private CommentsPagination pagination;

    public CommentsPagination getPagination() {
        return pagination;
    }

    public void setPagination(CommentsPagination pagination) {
        this.pagination = pagination;
    }

    @Override
    public String toString() {
        return "CommentsMeta{" +
                "pagination=" + pagination +
                '}';
    }
}
