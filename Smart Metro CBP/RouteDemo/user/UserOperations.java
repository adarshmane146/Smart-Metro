package user;

import exception.InvalidUserException;
import exception.UserNotFoundException;

public interface UserOperations {

    String registerUser(User user)
            throws InvalidUserException;

    User loginUser(int userID, String password)
            throws InvalidUserException, UserNotFoundException;

    User loginUser(String email, String password)
            throws InvalidUserException, UserNotFoundException;

    boolean userExists(int userID);

    boolean emailExists(String email);

    boolean mobileExists(long mobile);

    User getUserById(int userID)
            throws UserNotFoundException;

    void displayProfile(int userID)
            throws UserNotFoundException;

    void updateName(int userID, String name)
            throws InvalidUserException, UserNotFoundException;

    void updateEmail(int userID, String email)
            throws InvalidUserException, UserNotFoundException;

    void updateMobile(int userID, long mobile)
            throws InvalidUserException, UserNotFoundException;

    void updatePassword(int userID, String password)
            throws InvalidUserException, UserNotFoundException;

    boolean verifyPassword(int userID, String password)
            throws UserNotFoundException;

    boolean deleteUser(int userID)
            throws UserNotFoundException;

    void displayAllUsers();

    int getUserCount();
}
