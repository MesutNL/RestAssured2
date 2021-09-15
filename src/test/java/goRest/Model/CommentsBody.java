package goRest.Model;

import java.util.List;

public class CommentsBody {

    private CommentsMeta meta;
    private List<Comment> data;

    public CommentsMeta getMeta() {
        return meta;
    }

    public void setMeta(CommentsMeta meta) {
        this.meta = meta;
    }

    public List<Comment> getData() {
        return data;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommentsClass{" +
                "meta=" + meta +
                ", comments=" + data +
                '}';
    }
}
