package user;

import java.util.Random;

import enums.UserRole;

public class User {

    private int userID;
    private String name;
    private String email;
    private long mobile;
    private String password;
    private UserRole role;

    
    public User(String name, String email, long mobile, String password, UserRole role) {

        this.userID = generateID();
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.role = role;
	
    }

    
    private int generateID() {

        Random r = new Random();
        return r.nextInt(900) + 100;
    }

    
    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public long getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    // Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    public void displayProfile() {

        System.out.println("===== USER PROFILE =====");
        System.out.println("User ID  : " + userID);
        System.out.println("Name     : " + name);
        System.out.println("Email    : " + email);
        System.out.println("Mobile   : " + mobile);
        System.out.println("Role     : " + role);
    }
}