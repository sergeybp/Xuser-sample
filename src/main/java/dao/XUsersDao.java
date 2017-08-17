package dao;

import entities.Address;
import entities.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by sergeybp on 20.07.17.
 */
public interface XUsersDao {

    boolean addNewUser(User user);

    ArrayList<User> getUsers();

    boolean activateUser(int userId);

    boolean deactivateUser(int userId);

    boolean editUser(User updatedUser);

    boolean authenticateAdmin(String userName, String userPassword);

    boolean changeAdminPassword(String token, String newPassword);

    boolean setCurrentToken(String userName, String password, String newToken);

    String getCurrentToken();

    ArrayList<User> findUserByName(String name);

    ArrayList<User> findUserByEmail(String email);

    ArrayList<User> findUserByBirthday(String birthday);

}
