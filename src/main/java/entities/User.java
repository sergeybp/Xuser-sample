package entities;

/**
 * Created by sergeybp on 20.07.17.
 */
public class User {

    public int id;

    public String firstName;

    public String lastName;

    public String userName;

    public String password;

    public String email;

    public String birthday;

    public int isActive;

    public long createdTimestamp;

    public long lastUpdatedTimestamp;

    public User(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User() {
    }


    public Address address;

    public User(int id, String firstName, String lastName, String userName, String password, String email, String birthday, int isActive, long createdTimestamp, long lastUpdatedTimestamp, Address address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.birthday = birthday;
        this.isActive = isActive;
        this.createdTimestamp = createdTimestamp;
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
        this.address = address;
    }

    public String toString() {
        String res = "";
        res += "ID: " + id + "\n";
        res += "First Name: " + firstName + "\n";
        res += "Last Name: " + lastName + "\n";
        res += "UserName: " + userName + "\n";
        res += "Email: " + email + "\n";
        res += "Birthday: " + birthday + "\n";
        res += "Active: " + (isActive == 1 ? "YES" : "NO") + "\n";
        res += "Created: " + createdTimestamp + ", Updated: " + lastUpdatedTimestamp + "\n";
        res += "Address: " + address.toString();
        return res;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthday() {
        return birthday;
    }

    public int isActive() {
        return isActive;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public long getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public Address getAddress() {
        return address;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public void setLastUpdatedTimestamp(long lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getIsActive() {
        return isActive;
    }
}
