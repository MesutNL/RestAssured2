package goRest.Model;

import java.util.List;

public class PostBody {

   private CommentsMeta meta;
   private List<Posts> data;

    public CommentsMeta getMeta() {
        return meta;
    }

    public void setMeta(CommentsMeta meta) {
        this.meta = meta;
    }

    public List<Posts> getData() {
        return data;
    }

    public void setData(List<Posts> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PostBody{" +
                "commentsMeta=" + meta +
                ", posts=" + data +
                '}';
    }
}
