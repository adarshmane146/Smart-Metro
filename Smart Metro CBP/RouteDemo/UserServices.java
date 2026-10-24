package user;

import exception.InvalidUserException;
import exception.UserNotFoundException;
import enums.UserRole;

public class UserServices implements UserOperations {

    private User[] users = new User[100];
    private int count = 0;

    @Override
    public String registerUser(User user)
            throws InvalidUserException {

        if (user == null) {
            throw new InvalidUserException(
                    "User cannot be null");
        }

        if (count >= users.length) {
            throw new InvalidUserException(
                    "Maximum user limit reached");
        }

        if (user.getUserID() < 100 ||
            user.getUserID() > 999) {

            throw new InvalidUserException(
                    "Invalid User ID");
        }

        String name = user.getName();

        if (name == null || name.trim().isEmpty()) {
            throw new InvalidUserException(
                    "Name cannot be empty");
        }

        name = name.trim();

        if (name.length() < 2) {
            throw new InvalidUserException(
                    "Name must contain at least 2 characters");
        }

        if (name.length() > 50) {
            throw new InvalidUserException(
                    "Name cannot exceed 50 characters");
        }

        if (!name.matches("[a-zA-Z]+([ ][a-zA-Z]+)*")) {
            throw new InvalidUserException(
                    "Name should contain only alphabets and single spaces");
        }

        String email = user.getEmail();

        if (email == null || email.trim().isEmpty()) {
            throw new InvalidUserException(
                    "Email cannot be empty");
        }

        email = email.trim().toLowerCase();

        if (email.length() > 254) {
            throw new InvalidUserException(
                    "Email is too long");
        }

        if (!email.matches(
                "^[A-Za-z0-9]+([._+-][A-Za-z0-9]+)*@gmail\\.com$")) {

            throw new InvalidUserException(
                    "Email must contain @gmail.com");
        }
        
        if (emailExists(email)) {
            throw new InvalidUserException(
                    "Email already exists");
        }

        long mobile = user.getMobile();

        String mobileString = String.valueOf(mobile);

        if (mobile <= 0) {
            throw new InvalidUserException(
                    "Mobile number must be positive");
        }

        if (mobileString.length() != 10) {
            throw new InvalidUserException(
                    "Mobile number must contain exactly 10 digits");
        }

        if (!mobileString.matches("[6-9][0-9]{9}")) {
            throw new InvalidUserException(
                    "Invalid mobile number");
        }

        if (mobileExists(mobile)) {
            throw new InvalidUserException(
                    "Mobile number already exists");
        }

        validatePassword(user.getPassword());

        if (user.getRole() == null) {
            throw new InvalidUserException(
                    "User role cannot be null");
        }

        if (user.getRole() != UserRole.USER) {
            throw new InvalidUserException(
                    "Normal registration is allowed only for USER role");
        }

        if (userExists(user.getUserID())) {
            throw new InvalidUserException(
                    "User ID already exists");
        }

        user.setName(name);
        user.setEmail(email);

        users[count] = user;
        count++;

        return "Registration Successful. User ID: "
                + user.getUserID();
    }

    @Override
    public User loginUser(int userID, String password)
            throws InvalidUserException, UserNotFoundException {

        if (userID < 100 || userID > 999) {
            throw new InvalidUserException(
                    "User ID must contain exactly 3 digits");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new InvalidUserException(
                    "Password cannot be empty");
        }

        if (!userExists(userID)) {
            throw new UserNotFoundException(
                    "User not found with ID: " + userID);
        }

        User user = getUserById(userID);

        if (!user.getPassword().equals(password)) {
            throw new InvalidUserException(
                    "Invalid password");
        }

        return user;
    }

    @Override
    public User loginUser(String email, String password)
            throws InvalidUserException, UserNotFoundException {

        if (email == null || email.trim().isEmpty()) {
            throw new InvalidUserException(
                    "Email cannot be empty");
        }

        email = email.trim().toLowerCase();

        if (!email.matches(
                "^[A-Za-z0-9]+([._+-][A-Za-z0-9]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+$")) {

            throw new InvalidUserException(
                    "Invalid email format");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new InvalidUserException(
                    "Password cannot be empty");
        }

        for (int i = 0; i < count; i++) {

            if (users[i].getEmail().equalsIgnoreCase(email)) {

                if (!users[i].getPassword().equals(password)) {
                    throw new InvalidUserException(
                            "Invalid password");
                }

                return users[i];
            }
        }

        throw new UserNotFoundException(
                "No user found with this email");
    }

    @Override
    public boolean userExists(int userID) {

        if (userID < 100 || userID > 999) {
            return false;
        }

        for (int i = 0; i < count; i++) {

            if (users[i].getUserID() == userID) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean emailExists(String email) {

        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        email = email.trim();

        for (int i = 0; i < count; i++) {

            if (users[i].getEmail()
                    .equalsIgnoreCase(email)) {

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mobileExists(long mobile) {

        if (mobile <= 0) {
            return false;
        }

        for (int i = 0; i < count; i++) {

            if (users[i].getMobile() == mobile) {
                return true;
            }
        }

        return false;
    }

    @Override
    public User getUserById(int userID)
            throws UserNotFoundException {

        if (userID < 100 || userID > 999) {
            throw new UserNotFoundException(
                    "Invalid User ID");
        }

        for (int i = 0; i < count; i++) {

            if (users[i].getUserID() == userID) {
                return users[i];
            }
        }

        throw new UserNotFoundException(
                "User not found with ID: " + userID);
    }

    @Override
    public void displayProfile(int userID)
            throws UserNotFoundException {

        User user = getUserById(userID);

        user.displayProfile();
    }

    @Override
    public void updateName(int userID, String name)
            throws InvalidUserException, UserNotFoundException {

        User user = getUserById(userID);

        if (name == null || name.trim().isEmpty()) {
            throw new InvalidUserException(
                    "Name cannot be empty");
        }

        name = name.trim();

        if (name.length() < 2) {
            throw new InvalidUserException(
                    "Name must contain at least 2 characters");
        }

        if (name.length() > 50) {
            throw new InvalidUserException(
                    "Name cannot exceed 50 characters");
        }

        if (!name.matches("[a-zA-Z]+([ ][a-zA-Z]+)*")) {
            throw new InvalidUserException(
                    "Name should contain only alphabets and single spaces");
        }

        user.setName(name);
    }

    @Override
    public void updateEmail(int userID, String email)
            throws InvalidUserException, UserNotFoundException {

        User user = getUserById(userID);

        if (email == null || email.trim().isEmpty()) {
            throw new InvalidUserException(
                    "Email cannot be empty");
        }

        email = email.trim().toLowerCase();

        if (email.length() > 254) {
            throw new InvalidUserException(
                    "Email is too long");
        }

        if (!email.matches(
                "^[A-Za-z0-9]+([._+-][A-Za-z0-9]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+$")) {

            throw new InvalidUserException(
                    "Invalid email format");
        }

        if (!user.getEmail().equalsIgnoreCase(email)
                && emailExists(email)) {

            throw new InvalidUserException(
                    "Email already exists");
        }

        user.setEmail(email);
    }

    @Override
    public void updateMobile(int userID, long mobile)
            throws InvalidUserException, UserNotFoundException {

        User user = getUserById(userID);

        if (mobile <= 0) {
            throw new InvalidUserException(
                    "Mobile number must be positive");
        }

        String mobileString = String.valueOf(mobile);

        if (mobileString.length() != 10) {
            throw new InvalidUserException(
                    "Mobile number must contain exactly 10 digits");
        }

        if (!mobileString.matches("[6-9][0-9]{9}")) {
            throw new InvalidUserException(
                    "Invalid mobile number");
        }

        if (user.getMobile() != mobile
                && mobileExists(mobile)) {

            throw new InvalidUserException(
                    "Mobile number already exists");
        }

        user.setMobile(mobile);
    }

    @Override
    public void updatePassword(int userID, String password)
            throws InvalidUserException, UserNotFoundException {

        User user = getUserById(userID);

        validatePassword(password);

        if (user.getPassword().equals(password)) {
            throw new InvalidUserException(
                    "New password cannot be same as old password");
        }

        user.setPassword(password);
    }

    @Override
    public boolean verifyPassword(int userID, String password)
            throws UserNotFoundException {

        User user = getUserById(userID);

        if (password == null || password.trim().isEmpty()) {
            return false;
        }

        return user.getPassword().equals(password);
    }

    @Override
    public boolean deleteUser(int userID)
            throws UserNotFoundException {

        User user = getUserById(userID);

        for (int i = 0; i < count; i++) {

            if (users[i] == user) {

                for (int j = i; j < count - 1; j++) {
                    users[j] = users[j + 1];
                }

                users[count - 1] = null;
                count--;

                return true;
            }
        }

        return false;
    }

    @Override
    public void displayAllUsers() {

        if (count == 0) {
            System.out.println("No users registered");
            return;
        }

        for (int i = 0; i < count; i++) {

            System.out.println("--------------------");
            System.out.println("User ID : " + users[i].getUserID());
            System.out.println("Name    : " + users[i].getName());
            System.out.println("Email   : " + users[i].getEmail());
            System.out.println("Mobile  : " + users[i].getMobile());
            System.out.println("Role    : " + users[i].getRole());
        }
    }

    @Override
    public int getUserCount() {
        return count;
    }

    private void validatePassword(String password)
            throws InvalidUserException {

        if (password == null || password.trim().isEmpty()) {
            throw new InvalidUserException(
                    "Password cannot be empty");
        }

        if (!password.equals(password.trim())) {
            throw new InvalidUserException(
                    "Password cannot start or end with spaces");
        }

        if (password.contains(" ")) {
            throw new InvalidUserException(
                    "Password cannot contain spaces");
        }

        if (password.length() < 8) {
            throw new InvalidUserException(
                    "Password must contain at least 8 characters");
        }

        if (password.length() > 64) {
            throw new InvalidUserException(
                    "Password cannot exceed 64 characters");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new InvalidUserException(
                    "Password must contain an uppercase letter");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new InvalidUserException(
                    "Password must contain a lowercase letter");
        }

        if (!password.matches(".*[0-9].*")) {
            throw new InvalidUserException(
                    "Password must contain a number");
        }

        if (!password.matches(".*[@#$%^&+=!].*")) {
            throw new InvalidUserException(
                    "Password must contain a special character");
        }
    }
}