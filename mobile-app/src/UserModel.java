public class UserModel {
    private String userId;
    private String username;
    private String password;
    private String fname;
    private String lname;
    private String userRole;

    public UserModel(String userId, String username, String password, String fname, String lname, String userRole) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.userRole = userRole;
    }

    // Getter ve Setter metodları

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}