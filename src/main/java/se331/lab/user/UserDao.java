package se331.lab.user;

public interface UserDao {
    User findByUsername(String username);

    User save(User user);
}