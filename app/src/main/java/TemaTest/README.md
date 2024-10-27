
# ACSBook - Small social platform

A small project homework in Java that applies the concepts taught within the OOP course at the 2023CB series. The theme of the project is small social media platform where you are able to post, comment, like, follow users, and obtain other data based on these features.




## Implementation details

My implementation features the following APIs:

- Platform.java
    - The main platform API, offers the functionality required in the homework assignment
    - Offers multiple methods for interacting with the platform, some being:
        - **create-user**
        - **create-post**
        - **comment-post**
        - **like-post**
        - etc.
    - Holds a list for every user user, post, comment
    - May or may not have a save/load feature for persistance
- User.java
    - Another core component of the implementation
    - Holds data of the user such as username, and password albeit unsafe
    - Holds lists of the created posts, comments, liked posts and comments, following and followers for easier retrieval
    - Contains methods that can be used to determine the user's permissions over comments/post
- Likeable.java
    - A interface that contains some common methods of likeable objects
- Post.java, Comment.java
    - Very simillar objects, the exception being that posts hold comments
    - Have methods to obtain their creator, number of likes, their content, or other details


## Optimizations / Edgecases / Refactoring

Besides using files to have the database persist, there are multiple ways in which the program may be optimized, such as:

- Using a JSON library to form the responses returned by the API
- Using HashMaps/Tables for various tasks, such as finding comments/posts by ID
- If persistance was implemented, it would be beneficial for a temporary HashTable that pairs IDs and the objects to be created at load time
- Using ordered sets or priority queues of the sort for faster requests of top 5 objects that satisfy a condition

Edgecases that I have encountered:

- A user trying to see Top 5 Posts/Comments, should be able to see only the top 5 of the users they **follow**. (Unimplemented due to it not passing tests)
- A user should not be able to comment on a post of a person they do **not** follow. (Unimplemented due to it not passing tests)
- Comments that get deleted should dissapear from the user's liked list.
- If a post gets deleted, its comments as well should be deleted and not be available to the user.

Refactorizations that I can suggest are:

- Changing the list attributes for some API commands that have return only one object (eg. when returning only one post, it shouldn't be encapsulated in a list)
- Adding public and private profiles (public would mean that everything is visible by default)
- Reposting (kind of like retweeting or mentioning a post)
- Changing the tests so that the spaces are more consistent


## Documentation

#### Commands

1. **Create User**
   - Command: ```java tema1 –create-user -u 'my_username' -p 'my_password'```
   - *Description*: Creates a new user with the specified username and password. Returns an error if the user already exists.

2. **Create Post**
   - Command: ```java tema1 –create-post -u 'my_username' -p 'my_password' -text 'Astăzi mă simt bine'```
   - *Description*: Creates a new post with the provided text. Maximum post length is 300 characters.

3. **Delete Post by ID**
   - Command: ```java tema1 –delete-post-by-id -u 'my_username' -p 'my_password' -id 'post_id1'```
   - *Description*: Deletes a post by its unique identifier.

4. **Follow User by Username**
   - Command: ```java tema1 –follow-user-by-username -u 'my_username' -p 'my_password' -username 'username1'```
   - *Description*: Follows a user based on their username.

5. **Unfollow User by Username**
   - Command: ```java tema1 –unfollow-user-by-username -u 'my_username' -p 'my_password' -username 'username1'```
   - *Description*: Unfollows a user based on their username.

6. **Like Post**
   - Command: ```java tema1 –like-post -u 'my_username' -p 'my_password' -post-id 'post_id1'```
   - *Description*: Likes a post identified by its unique identifier.

7. **Unlike Post**
   - Command: ```java tema1 –unlike-post -u 'my_username' -p 'my_password' -post-id 'post_id1'```
   - *Description*: Unlikes a post identified by its unique identifier.

8. **Like Comment**
   - Command: ```java tema1 –like-comment -u 'my_username' -p 'my_password' -comment-id 'comment_id1'```
   - *Description*: Likes a comment identified by its unique identifier.

9. **Unlike Comment**
   - Command: ```java tema1 –unlike-comment -u 'my_username' -p 'my_password' -comment-id 'comment_id1'```
   - *Description*: Unlikes a comment identified by its unique identifier.

10. **Get Following Posts**
    - Command: ```java tema1 –get-followings-posts -u 'my_username' -p 'my_password'```
    - *Description*: Lists posts from users you follow, ordered by date.

11. **Get User Posts**
    - Command: ```java tema1 –get-user-posts -u 'my_username' -p 'my_password' -username 'username1'```
    - *Description*: Lists posts from a specific user, ordered by date.

12. **Get Post Details**
    - Command: ```java tema1 –get-post-details -u 'my_username' -p 'my_password' –post-id 'post_id1'```
    - *Description*: Provides details about a specific post, including text, date, username, number of likes, and comments.

13. **Comment on Post**
    - Command: ```java tema1 –comment-post -u 'my_username' -p 'my_password' –post-id 'post_id1' -text 'text1'```
    - *Description*: Adds a comment to a post with the specified text. Maximum comment length is 300 characters.

14. **Delete Comment by ID**
    - Command: ```java tema1 –delete-comment-by-id -u 'my_username' -p 'my_password' -id 'comment_id1'```
    - *Description*: Deletes a comment by its unique identifier.

15. **Get Following Users**
    - Command: ```java tema1 –get-following -u 'my_username' -p 'my_password'```
    - *Description*: Lists users you are following.

16. **Get Followers**
    - Command: ```java tema1 –get-followers -u 'my_username' -p 'my_password' -username 'username1'```
    - *Description*: Lists followers of a specific user.

17. **Get Top 5 Most Liked Posts**
    - Command: ```java tema1 –get-most-liked-posts -u 'my_username' -p 'my_password'```
    - *Description*: Lists the top 5 most liked posts on the platform.

18. **Get Top 5 Most Commented Posts**
    - Command: ```java tema1 –get-most-commented-posts -u 'my_username' -p 'my_password'```
    - *Description*: Lists the top 5 most commented posts on the platform.

19. **Get Top 5 Most Followed Users**
    - Command: ```java tema1 –get-most-followed-users -u 'my_username' -p 'my_password'```
    - *Description*: Lists the top 5 most followed users on the platform.

20. **Get Top 5 Most Liked Users**
    - Command: ```java tema1 –get-most-liked-users -u 'my_username' -p 'my_password'```
    - *Description*: Lists the top 5 most liked users on the platform.

21. **Cleanup All Data**
    - Command: ```java tema1 –cleanup-all```
    - *Description*: Cleans up all data in the platform. Use with caution; irreversible action!


