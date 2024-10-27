package TemaTest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Comment implements Likeable, Comparable<Comment>{
    private static int id = 1;
    private final int comm_id;
    private User creator;
    private Post post;
    private final ArrayList<User> likes = new ArrayList<>();
    private String content;
    private final Date date;


    public Comment(int id) {
        this.date = new Date();
        this.comm_id = id;
    }
    public Comment(User creator, Post post, String continut) {
        this(Comment.id++);
        this.post = post;
        this.creator = creator;
        this.content = continut;
    }

    public int getId() {
        return this.comm_id;
    }
    public static void resetGlobalId() {
        Comment.id = 1;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
    public User getCreator() {
        return this.creator;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Post getPost() {
        return post;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return this.content;
    }

    @Override
    public void like(User liker) {
        likes.add(liker);
    }
    @Override
    public void unlike(User liker) {
        likes.remove(liker);
    }

    @Override
    public int getLikedCount() {
        return this.likes.size();
    }

    public ArrayList<User> getLikes() {
        return new ArrayList<>(likes);
    }
    // TODO
    public JSONObj toJSON() {
        JSONObj output = new JSONObj();
        output.addKeyValuePair("'comment_id'", "'"+ comm_id +"'");
        output.addKeyValuePair("'comment_text'", "'"+content+"'");
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateAsString = dateFormat.format(date);
        output.addKeyValuePair("'comment_date'", "'"+currentDateAsString+"'");
        output.addKeyValuePair("'username'", "'"+creator.getUsername()+"'");
        output.addKeyValuePair("'number_of_likes'", "'"+likes.size()+"'");
        return output;
    }
    public int compareTo(Comment comment) {
        return -this.date.compareTo(comment.date);
    }

}
