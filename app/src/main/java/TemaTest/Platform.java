package TemaTest;

import java.util.ArrayList;
import java.util.Comparator;




public class Platform {
    private final int max_content_length = 300;
    private final ArrayList<User> users = new ArrayList<>();
    private final ArrayList<Post> posts = new ArrayList<>();
    private final ArrayList<Comment> comments = new ArrayList<>();

    // Helper class
    private static class PostLikesComparator implements Comparator<Post> {
        public int compare(Post like1, Post like2) {
            return -like1.getLikedCount() + like2.getLikedCount();
        }
    }
    private static class PostCommentsComparator implements Comparator<Post> {
        public int compare(Post commented1, Post commented2) {
            return -commented1.getCommentCount() + commented2.getCommentCount();
        }
    }
    private static class UsersFollowedComparator implements Comparator<User> {
        public int compare(User followed1, User followed2) {
            return -followed1.getFollowerCount() + followed2.getFollowerCount();
        }
    }
    private static class UsersLikedComparator implements Comparator<User> {
        public int compare(User liked1, User liked2) {
            return -liked1.getTotalLikeCount() + liked2.getTotalLikeCount();
        }
    }

    private User fetchUserByUsername(String username) {
        for(User x : users) {
            if (x.getUsername().equals(username))
                return x;
        }
        return null;
    }
    private User authUser(String username, String passwd) {
        for(User x : users) {
            if (x.auth(username, passwd))
                return x;
        }
        return null;
    }

    private Post fetchPostById(int id) {
        for(Post x : posts) {
            if (x.getId() == id)
                return x;
        }
        return null;
    }
    private Comment fetchCommentById(int id) {
        for (Comment x : comments) {
            if(x.getId() == id)
                return x;
        }
        return null;
    }

    public User handleLogin(JSONObj response, String username, String passwd) {
        response.addKeyValuePair("'status'", "'error'");

        // Check if both arguments are missing
        if (username == null || passwd == null) {
            response.addKeyValuePair("'message'", "'You need to be authenticated'");
            return null;
        }
        User user = authUser(username, passwd);

        // Could not find user
        if (user == null) {
            response.addKeyValuePair("'message'", "'Login failed'");
            return null;
        }
        return user;
    }
    public String createUser(String username, String passwd) {
        JSONObj response = new JSONObj();
        response.addKeyValuePair("'status'", "'error'");

        // No username arg provided
        if (username == null) {
            response.addKeyValuePair("'message'", "'Please provide username'");
            return response.toString();
        }
        // no passwd arg provided
        if (passwd == null) {
            response.addKeyValuePair("'message'", "'Please provide password'");
            return response.toString();
        }
        // User doesn't exist in database
        if (fetchUserByUsername(username) != null) {
            response.addKeyValuePair("'message'", "'User already exists'");
            return response.toString();
        }
        // no errors i guess, add the user
        this.users.add(new User(username, passwd));
        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", "'User created successfully'");
        return response.toString();
    }

    public String createPost(String username, String passwd, String text) {
        JSONObj response = new JSONObj();
        User user = handleLogin(response, username, passwd);

        // No user -> can't log in
        if (user == null) {
            return response.toString();
        }
        // No text arg provided
        if (text == null) {
            response.addKeyValuePair("'message'", "'No text provided'");
            return response.toString();
        }
        // Text exceeds limit
        if (text.length() > max_content_length) {
            response.addKeyValuePair("'message'", "'Post text length exceeded'");
            return response.toString();
        }
        // all went well
        Post post = user.createPost(text);
        this.posts.add(post);
        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", "'Post added successfully'");
        return response.toString();
    }

    public String deletePostById(String username, String passwd, int id) {
        JSONObj response = new JSONObj();
        User user = handleLogin(response, username, passwd);

        if (user == null) {
            return response.toString();
        }
        // No id arg provided
        if (id == -1) {
            response.addKeyValuePair("'message'", "'No identifier was provided'");
            return response.toString();
        }
        Post post = user.fetchCreatedPostById(id);
        // No post found in database
        if (post == null) {
            response.addKeyValuePair("'message'", "'The identifier was not valid'");
            return response.toString();
        }
        // Remove post
        user.removePost(post);
        this.posts.remove(post);
        // Remove post's comments
        for (Comment com : post.getComments()) {
            com.getCreator().removeCreatedComment(com);
            for (User y : com.getLikes()) {
                y.removeLikedComment(com);
            }
            comments.remove(com);
        }
        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", "'Post deleted successfully'");
        return response.toString();
    }

    public String followUser(String username, String passwd, String follow) {
        JSONObj response = new JSONObj();
        User user = handleLogin(response, username, passwd);

        if (user == null) {
            return response.toString();
        }
        // No following arg provided
        if (follow == null) {
            response.addKeyValuePair("'message'", "'No username to follow was provided'");
            return response.toString();
        }
        User userToFollow = fetchUserByUsername(follow);
        // Is already following the user or user doesn't exist
        if (userToFollow == null || user.isFollowing(userToFollow)) {
            response.addKeyValuePair("'message'", "'The username to follow was not valid'");
            return response.toString();
        }

        user.follow(userToFollow);
        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", "'Operation executed successfully'");
        return response.toString();
    }
    public String unfollowUser(String username, String passwd, String unfollow) {
        JSONObj response = new JSONObj();
        User user = handleLogin(response, username, passwd);

        if (user == null) {
            return response.toString();
        }
        // No unfollow arg provided
        if (unfollow == null) {
            response.addKeyValuePair("'message'", "'No username to unfollow was provided'");
            return response.toString();
        }
        User userToUnfollow = fetchUserByUsername(unfollow);
        // Is already not following the user or user doesn't exist
        if (userToUnfollow == null || !user.isFollowing(userToUnfollow)) {
            response.addKeyValuePair("'message'", "'The username to unfollow was not valid'");
            return response.toString();
        }

        user.unfollow(userToUnfollow);
        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", "'Operation executed successfully'");
        return response.toString();
    }
    public String likePostById(String username, String passwd, int id) {
        JSONObj response = new JSONObj();
        User user = handleLogin(response, username, passwd);

        if (user == null) {
            return response.toString();
        }
        // No id arg provided
        if (id == -1) {
            response.addKeyValuePair("'message'", "'No post identifier to like was provided'");
            return response.toString();
        }
        Post post = fetchPostById(id);
        // No post found or post is not likeable
        if (post == null || !user.isPostLikeable(post)) {
            response.addKeyValuePair("'message'", "'The post identifier to like was not valid'");
            return response.toString();
        }
        user.likePost(post);
        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", "'Operation executed successfully'");
        return response.toString();
    }
    public String unlikePostById(String username, String passwd, int id) {
        JSONObj response = new JSONObj();
        User user = handleLogin(response, username, passwd);

        if (user == null) {
            return response.toString();
        }
        // No id arg provided
        if (id == -1) {
            response.addKeyValuePair("'message'", "'No post identifier to unlike was provided'");
            return response.toString();
        }
        Post post = fetchPostById(id);
        // No post found or post is not unlikeable
        if (post == null || !user.isPostAlreadyLiked(post)) {
            response.addKeyValuePair("'message'", "'The post identifier to unlike was not valid'");
            return response.toString();
        }
        user.unlikePost(post);
        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", "'Operation executed successfully'");
        return response.toString();
    }

    public String likeCommentById(String username, String passwd, int id) {
        JSONObj response = new JSONObj();
        User user = handleLogin(response, username, passwd);

        if (user == null) {
            return response.toString();
        }
        // No id arg found
        if (id == -1) {
            response.addKeyValuePair("'message'", "'No comment identifier to like was provided'");
            return response.toString();
        }
        Comment comment = fetchCommentById(id);
        // No comment found or comment is not likeable
        if (comment == null || !user.isCommentLikeable(comment)) {
            response.addKeyValuePair("'message'", "'The comment identifier to like was not valid'");
            return response.toString();
        }
        user.likeComment(comment);
        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", "'Operation executed successfully'");
        return response.toString();
    }
    public String unlikeCommentById(String username, String passwd, int id) {
        JSONObj response = new JSONObj();
        User user = handleLogin(response, username, passwd);

        if (user == null) {
            return response.toString();
        }
        // No id arg found
        if (id == -1) {
            response.addKeyValuePair("'message'", "'No comment identifier to unlike was provided'");
            return response.toString();
        }
        Comment comment = fetchCommentById(id);
        // No comment found or comment is not likeable
        if (comment == null || !user.isCommentAlreadyLiked(comment)) {
            response.addKeyValuePair("'message'", "'The comment identifier to unlike was not valid'");
            return response.toString();
        }
        user.unlikeComment(comment);
        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", "'Operation executed successfully'");
        return response.toString();
    }
    public String getFollowingsPosts(String username, String passwd) {
        JSONObj response = new JSONObj();
        User user = handleLogin(response, username, passwd);

        if (user == null) {
            return response.toString();
        }
        // Compile list of posts
        JSONList post_list = new JSONList();
        ArrayList<Post> posts = user.getFollowingsPosts();
        for (Post x : posts) {
            post_list.append(x.toJSONUsername());
        }
        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", post_list.toString());
        return response.toString();
    }
    public String getUserPosts(String username, String passwd, String username2) {
        JSONObj response = new JSONObj();
        User user = handleLogin(response, username, passwd);

        if (user == null) {
            return response.toString();
        }
        // user not found
        if (username2 == null) {
            response.addKeyValuePair("'message'", "'No username to list posts was provided'");
            return response.toString();
        }

        User user2 = fetchUserByUsername(username2);
        // User is not in db or isn't followed
        if (user2 == null || !user.isFollowing(user2)) {
            response.addKeyValuePair("'message'", "'The username to list posts was not valid'");
            return response.toString();
        }
        // Compile list
        JSONList post_list = new JSONList();
        ArrayList<Post> posts = user2.getPosts();
        for (Post x : posts) {
            post_list.append(x.toJSON());
        }
        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", post_list.toString());
        return response.toString();
    }
    public String getPostDetailsById(String username, String passwd, int id) {
        JSONObj response = new JSONObj();
        User user = handleLogin(response, username, passwd);

        if (user == null) {
            return response.toString();
        }
        // No id arg provided
        if (id == -1) {
            response.addKeyValuePair("'message'", "'No post identifier was provided'");
            return response.toString();
        }
        Post post = fetchPostById(id);
        // No post found or post is not visible
        if (post == null || !user.isPostVisible(post)) {
            response.addKeyValuePair("'message'", "'The post identifier was not valid'");
            return response.toString();
        }
        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", "[" + post.detailsJSON() + "] ");
        return response.toString();
    }
    public String commentPostById(String username, String passwd, int id, String content) {
        JSONObj response = new JSONObj();

        User user = handleLogin(response, username, passwd);
        if (user == null) {
            return response.toString();
        }
        // No content arg provided
        if (content == null) {
            response.addKeyValuePair("'message'", "'No text provided'");
            return response.toString();
        }
        // Content length exceeded
        if (content.length() > max_content_length)  {
            response.addKeyValuePair("'message'", "'Comment text length exceeded'");
            return response.toString();
        }
        // No post id arg provided
        if (id == -1) {
            response.addKeyValuePair("'message'", "'No post identifier was provided'");
            return response.toString();
        }
        Post post = fetchPostById(id);
        // No post found or post is not visible
        if (post == null || !user.isPostVisible(post)) {
            response.addKeyValuePair("'message'", "'The post identifier was not valid'");
            return response.toString();
        }

        Comment comment = user.createComment(post, content);
        this.comments.add(comment);
        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", "'Comment added successfully'");
        return response.toString();
    }
    public String deleteCommentById(String username, String passwd, int id) {
        JSONObj response = new JSONObj();
        User user = handleLogin(response, username, passwd);

        if (user == null) {
            return response.toString();
        }
        // No id arg provided
        if (id == -1) {
            response.addKeyValuePair("'message'", "'No identifier was provided'");
            return response.toString();
        }
        Comment comment = fetchCommentById(id);
        // No comment found or user doesn't have permissions over comment
        if (comment == null || !user.hasPerms(comment)) {
            response.addKeyValuePair("'message'", "'The identifier was not valid'");
            return response.toString();
        }
        user.removeCreatedComment(comment);
        for (User x : comment.getLikes()) {
            x.unlikeComment(comment);
        }
        comment.getPost().removeComment(comment);
        comments.remove(comment);

        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", "'Operation executed successfully'");
        return response.toString();
    }
    public String getFollowing(String username, String passwd) {
        JSONObj response = new JSONObj();
        User user = handleLogin(response, username, passwd);

        if (user == null) {
            return response.toString();
        }

        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", user.getFollowingAsString());
        return response.toString();
    }
    public String getFollowers(String username, String passwd, String username2) {
        JSONObj response = new JSONObj();
        User user = handleLogin(response, username, passwd);

        if (user == null) {
            return response.toString();
        }
        if (username2 == null) {
            response.addKeyValuePair("'message'", "'No username to list followers was provided'");
            return response.toString();
        }
        User user2 = fetchUserByUsername(username2);
        if (user2 == null) {
            response.addKeyValuePair("'message'", "'The username to list followers was not valid'");
            return response.toString();
        }
        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", user.getFollowersAsString());
        return response.toString();
    }
    public String getTop5MostLikedPosts(String username, String passwd) {
        JSONObj response = new JSONObj();
        User user = handleLogin(response, username, passwd);

        if (user == null) {
            return response.toString();
        }
        JSONList list = new JSONList();
        posts.sort(new PostLikesComparator());

        for (int k = 0; k < 5 && k < posts.size(); k++) {
            list.append(posts.get(k).toJSONLiked());
        }

        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", list.toString());
        return response.toString();
    }
    public String getTop5MostCommentedPosts(String username, String passwd) {
        JSONObj response = new JSONObj();
        User user = handleLogin(response, username, passwd);

        if (user == null) {
            return response.toString();
        }
        JSONList list = new JSONList();
        posts.sort(new PostCommentsComparator());
        for (int k = 0; k < 5 && k < posts.size(); k++) {
            list.append(posts.get(k).toJSONUsernameComments());
        }

        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", list.toString());
        return response.toString();
    }
    public String getTop5MostFollowedUsers(String username, String passwd) {
        JSONObj response = new JSONObj();
        User user = handleLogin(response, username, passwd);

        if (user == null) {
            return response.toString();
        }
        JSONList list = new JSONList();
        users.sort(new UsersFollowedComparator());

        for (int k = 0; k < 5 && k < users.size(); k++) {
            JSONObj obj = new JSONObj("'username'", "'"+users.get(k).getUsername()+"'");
            obj.addKeyValuePair("'number_of_followers'", "'"+users.get(k).getFollowerCount()+"'");
            list.append(obj);
        }

        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", list.toString());
        return response.toString();
    }
    public String getTop5MostLikedUsers(String username, String passwd) {
        JSONObj response = new JSONObj();
        User user = handleLogin(response, username, passwd);

        if (user == null) {
            return response.toString();
        }
        JSONList list = new JSONList();
        users.sort(new UsersLikedComparator());

        for (int k = 0; k < 5 && k < users.size(); k++) {
            list.append(users.get(k).toJSONLikedCount());
        }

        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", list.toString());
        return response.toString();
    }

    public String cleanupAll() {
        users.clear();
        comments.clear();
        posts.clear();
        User.resetGlobalId();
        Comment.resetGlobalId();
        Post.resetGlobalId();
        // Should I force gc now that all references are lost?
        // System.gc();
        JSONObj response = new JSONObj();
        response.setKeyValue("'status'", "'ok'");
        response.addKeyValuePair("'message'", "'Cleanup finished successfully'");
        return response.toString();
    }

}
