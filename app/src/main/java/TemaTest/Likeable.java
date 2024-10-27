package TemaTest;

public interface Likeable  {
    void like(User liker);
    void unlike(User liker);
    int getLikedCount();
}
