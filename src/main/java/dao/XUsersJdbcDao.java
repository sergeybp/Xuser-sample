package dao;

import entities.Address;
import entities.Admin;
import entities.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergeybp on 20.07.17.
 */
public class XUsersJdbcDao extends JdbcDaoSupport implements XUsersDao {

    public XUsersJdbcDao(DataSource dataSource) {
        super();
        setDataSource(dataSource);
        String sql = "CREATE TABLE IF NOT EXISTS `users` (" +
                " `id` INTEGER ," +
                " `firstName` VARCHAR ," +
                " `lastName` VARCHAR ," +
                " `userName` VARCHAR ," +
                " `password` VARCHAR ," +
                " `email` VARCHAR ," +
                " `birthday` VARCHAR ," +
                " `isActive` INTEGER ," +
                " `createdTimestamp` VARCHAR ," +
                " `lastUpdatedTimestamp` VARCHAR ," +
                " PRIMARY KEY (`id`) " +
                " )";
        getJdbcTemplate().update(sql);
        sql = "CREATE TABLE IF NOT EXISTS `addresses` (" +
                " `id` INTEGER ," +
                " `zip` VARCHAR ," +
                " `country` VARCHAR ," +
                " `city` VARCHAR ," +
                " `district` VARCHAR ," +
                " `street` VARCHAR ," +
                " PRIMARY KEY (`id`) " +
                " )";
        getJdbcTemplate().update(sql);
        sql = "CREATE TABLE IF NOT EXISTS `admin` (" +
                " `userName` VARCHAR ," +
                " `password` VARCHAR ," +
                " `token` VARCHAR ," +
                " PRIMARY KEY (`userName`) " +
                " )";
        getJdbcTemplate().update(sql);
        sql = "SELECT * FROM admin";
        List<Admin> admins = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(Admin.class));
        if(admins.size() == 0) {
            sql = "INSERT INTO admin (userName, password, token) VALUES (?, ?, ?)";
            getJdbcTemplate().update(sql, "admin", calcPassword("admin"), "0000000");
        }
    }

    public boolean addNewUser(User user) {
        String sql = "INSERT INTO users (id, firstName, lastName, userName, password, email, birthday, isActive, createdTimestamp, lastUpdatedTimestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        String sql1 = "INSERT INTO addresses (id, zip, country, city, district, street) VALUES (?, ?, ?, ?, ?, ?) ";
        try {
            getJdbcTemplate().update(sql, user.id, user.firstName, user.lastName, user.userName, user.password, user.email, user.birthday, user.isActive, "" + user.createdTimestamp, "" + user.lastUpdatedTimestamp);
            getJdbcTemplate().update(sql1, user.id, user.address.zip, user.address.country, user.address.city, user.address.district, user.address.street);
        } catch (Exception ignored) {
            return false;
        }
        return true;
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> res = new ArrayList<>();
        String sql = "SELECT * FROM users";
        List<User> users = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(User.class));
        sql = "SELECT * FROM addresses";
        List<Address> addresses = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(Address.class));
        for (User user : users) {
            for (Address a : addresses) {
                if (a.id == user.id) {
                    user.address = a;
                    break;
                }
            }
            res.add(user);
        }
        return res;
    }


    public ArrayList<User> findUserByName(String name) {
        ArrayList<User> res = new ArrayList<>();
        String sql = "SELECT * FROM users";
        List<User> users = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(User.class));
        sql = "SELECT * FROM addresses";
        List<Address> addresses = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(Address.class));
        for (User user : users) {
            if (user.firstName.equals(name) || user.lastName.equals(name)) {
                for (Address a : addresses) {
                    if (a.id == user.id) {
                        user.address = a;
                        break;
                    }
                }
                res.add(user);
            }
        }
        return res;
    }

    public ArrayList<User> findUserByEmail(String email) {
        ArrayList<User> res = new ArrayList<>();
        String sql = "SELECT * FROM users";
        List<User> users = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(User.class));
        sql = "SELECT * FROM addresses";
        List<Address> addresses = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(Address.class));
        for (User user : users) {
            if (user.email.equals(email)) {
                for (Address a : addresses) {
                    if (a.id == user.id) {
                        user.address = a;
                        break;
                    }
                }
                res.add(user);
            }
        }
        return res;
    }

    public ArrayList<User> findUserByBirthday(String birthday) {
        ArrayList<User> res = new ArrayList<>();
        String sql = "SELECT * FROM users";
        List<User> users = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(User.class));
        sql = "SELECT * FROM addresses";
        List<Address> addresses = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(Address.class));
        for (User user : users) {
            if (user.birthday.equals(birthday)) {
                for (Address a : addresses) {
                    if (a.id == user.id) {
                        user.address = a;
                        break;
                    }
                }
                res.add(user);
            }
        }
        return res;
    }

    public boolean activateUser(int userId) {
        String sql = "UPDATE users SET isActive = 1 WHERE users.id = " + userId;
        try {
            getJdbcTemplate().update(sql);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean deactivateUser(int userId) {
        String sql = "UPDATE users SET isActive = 0 WHERE id = " + userId;
        try {
            getJdbcTemplate().update(sql);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean editUser(User updatedUser) {
        String sql = "SELECT * FROM users";
        List<User> users = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(User.class));
        boolean flag = false;
        for (User u : users) {
            if (u.id == updatedUser.id && u.isActive == 1) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            return false;
        }
        sql = "UPDATE users SET firstName = ?, lastName = ?, userName = ?, password = ?, email = ?, birthday = ?, isActive = ?, lastUpdatedTimestamp = ? WHERE id = " + updatedUser.id;
        String sql1 = "UPDATE addresses SET zip = ?, country = ?, city = ?, district = ?, street = ? WHERE id = " + updatedUser.id;
        try {
            getJdbcTemplate().update(sql, updatedUser.firstName, updatedUser.lastName, updatedUser.userName, updatedUser.password, updatedUser.email, updatedUser.birthday, updatedUser.isActive, "" + updatedUser.lastUpdatedTimestamp);
            getJdbcTemplate().update(sql1, updatedUser.address.zip, updatedUser.address.country, updatedUser.address.city, updatedUser.address.district, updatedUser.address.street);
        } catch (Exception ignored) {
            return false;
        }
        return true;

    }

    public boolean authenticateAdmin(String userName, String userPassword) {
        String sql = "SELECT * FROM admin";
        List<Admin> admins = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(Admin.class));
        for(Admin a: admins){
            if(a.userName.equals(userName) && a.password.equals(userPassword)){
                return true;
            }
        }
        return false;
    }

    public boolean changeAdminPassword(String token, String newPassword) {

        String sql = "SELECT * FROM admin";
        List<Admin> admins = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(Admin.class));
        String username = "";
        for(Admin a: admins){
            if(a.token.equals(token)){
                username = a.userName;
            }
        }
        sql = "UPDATE admin SET password = ? WHERE userName = ?";
        try {
            getJdbcTemplate().update(sql, newPassword, username);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean setCurrentToken(String userName, String userPassword, String token) {
        String sql = "UPDATE admin SET token = ? WHERE userName = ?";
        try {
            getJdbcTemplate().update(sql, token, userName);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String getCurrentToken() {
        String sql = "SELECT * FROM admin";
        List<Admin> admins = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(Admin.class));
        return admins.get(0).token;
    }

    private String calcPassword(String pass) {
        int x = pass.hashCode();
        return "" + x;
    }

}
