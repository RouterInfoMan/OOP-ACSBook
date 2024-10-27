package TemaTest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Post implements Likeable, Comparable<Post>{

    private static int id = 1;
    private final int post_id;
    private User creator;
    private String content;
    private final Date date;


    private final ArrayList<User> liked = new ArrayList<>();
    private final ArrayList<Comment> comments = new ArrayList<>();


    public Post() {
        this.date = new Date();
        this.post_id = Post.id;
        Post.id++;
    }


    public Post(User creator, String content) {
        this();
        this.creator = creator;
        this.content = content;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    public int getId() {
        return post_id;
    }
    public User getCreator() {
        return this.creator;
    }

    public ArrayList<Comment> getComments() {
        return new ArrayList<>(comments);
    }
    @Override
    public void like(User liker) {
        liked.add(liker);
    }

    @Override
    public void unlike(User liker) {
         liked.remove(liker);
    }
    public int getLikedCount() {
        return this.liked.size();
    }
    public int getCommentCount() {
        return this.comments.size();
    }
    public JSONObj detailsJSON() {
        JSONObj output = new JSONObj();
        output.addKeyValuePair("'post_text'", "'" + content + "'");
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateAsString = dateFormat.format(date);
        output.addKeyValuePair("'post_date'", "'" + currentDateAsString + "'");
        output.addKeyValuePair("'username'", "'" + creator.getUsername() + "'");
        output.addKeyValuePair("'number_of_likes'", "'" + liked.size() + "'");

        JSONList comment_list = new JSONList();
        Collections.sort(comments);
        for (Comment x : comments) {
            comment_list.append(x.toJSON());
        }
        output.addKeyValuePair("'comments'", comment_list.toStringWithSpaceAfter());
        return output;
    }
    public JSONObj toJSONUsername() {
        JSONObj output = new JSONObj();
        output.addKeyValuePair("'post_id'", "'" + post_id + "'");
        output.addKeyValuePair("'post_text'", "'" + content + "'");
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateAsString = dateFormat.format(date);
        output.addKeyValuePair("'post_date'", "'" + currentDateAsString + "'");
        output.addKeyValuePair("'username'", "'" + creator.getUsername() + "'");
        return output;
    }
    public JSONObj toJSONUsernameComments() {
        JSONObj output = new JSONObj();
        output.addKeyValuePair("'post_id'", "'" + post_id + "'");
        output.addKeyValuePair("'post_text'", "'" + content + "'");
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateAsString = dateFormat.format(date);
        output.addKeyValuePair("'post_date'", "'" + currentDateAsString + "'");
        output.addKeyValuePair("'username'", "'" + creator.getUsername() + "'");
        output.addKeyValuePair("'number_of_comments'", "'" + comments.size() + "'");
        return output;
    }
    public JSONObj toJSON() {
        JSONObj output = new JSONObj();
        output.addKeyValuePair("'post_id'", "'" + post_id + "'");
        output.addKeyValuePair("'post_text'", "'" + content + "'");
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateAsString = dateFormat.format(date);
        output.addKeyValuePair("'post_date'", "'" + currentDateAsString + "'");
        return output;
    }
    public JSONObj toJSONLiked() {
        JSONObj output = new JSONObj();
        output.addKeyValuePair("'post_id'", "'" + post_id + "'");
        output.addKeyValuePair("'post_text'", "'" + content + "'");
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateAsString = dateFormat.format(date);
        output.addKeyValuePair("'post_date'", "'" + currentDateAsString + "'");
        output.addKeyValuePair("'username'", "'" + creator.getUsername() + "'");
        output.addKeyValuePair("'number_of_likes'", "'" + liked.size() + "'");
        return output;
    }
    public int compareTo(Post post) {
        int res = this.date.compareTo(post.date);
        if (res == 0) {
            return -(this.post_id - post.post_id);
        }
        return res;
    }
    public static void resetGlobalId() {
        Post.id = 1;
    }
}
