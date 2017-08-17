package model;

import config.JdbcDaoContextConfiguration;
import dao.XUsersDao;
import entities.Address;
import entities.User;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by sergeybp on 20.07.17.
 */

@Path("")
public class Logic {

    private XUsersDao xUsersDao = new JdbcDaoContextConfiguration().xusersJdbcDao();


    private Utils utils = new Utils();

    public Logic() throws SQLException {
    }

    @GET
    @Path("/get_users")
    public Response getAllUsers(@QueryParam("token") String token) {
        if(!checkToken(token)){
            return Response.status(401).build();
        }
        StringBuilder result = new StringBuilder();
        result.append("----------\n");
        ArrayList<User> users = xUsersDao.getUsers();
        if (users.size() == 0) {
            return Response.status(404).build();
        }
        for (User user : users) {
            result.append(user.toString());
            result.append("----------\n");
        }
        String res = result.toString();

        return Response.status(200).entity(res).build();
    }

    @GET
    @Path("/search_users")
    public Response get(@QueryParam("token") String token,
                        @QueryParam("searchParameter") String parameter,
                        @QueryParam("searchText") String text) throws SQLException {
        if(!checkToken(token)){
            return Response.status(401).build();
        }
        StringBuilder result = new StringBuilder();
        result.append("----------\n");
        ArrayList<User> users;
        switch (parameter) {
            case "-n":
                users = xUsersDao.findUserByName(text);
                break;

            case "-e":
                users = xUsersDao.findUserByEmail(text);
                break;

            case "-b":
                users = xUsersDao.findUserByBirthday(text);
                break;

            default:
                users = new ArrayList<>();
        }

        if (users.size() == 0) {
            return Response.status(404).build();
        }
        for (User user : users) {
            result.append(user.toString());
            result.append("----------\n");
        }
        String res = result.toString();

        return Response.status(200).entity(res).build();
    }

    @GET
    @Path("/is_alive")
    public Response isAlive() {
        return Response.status(200).build();
    }

    @POST
    @Path("/activate")
    public Response activateUser(@QueryParam("token") String token, @QueryParam("userId") int userId){
        if(!checkToken(token)){
            return Response.status(401).build();
        }
        if(xUsersDao.activateUser(userId)){
            return Response.status(200).build();
        } else {
            return Response.status(400).build();
        }
    }

    @POST
    @Path("/deactivate")
    public Response deactivateUser(@QueryParam("token") String token, @QueryParam("userId") int userId){
        if(!checkToken(token)){
            return Response.status(401).build();
        }
        if(xUsersDao.deactivateUser(userId)){
            return Response.status(200).build();
        } else {
            return Response.status(400).build();
        }
    }

    @POST
    @Path("/change_admin_password")
    public Response changeAdminPassword(@QueryParam("token") String token, @QueryParam("newPassword") String newPassword){
        if(!checkToken(token)){
            return Response.status(401).build();
        }
        if(xUsersDao.changeAdminPassword(token, calcPassword(newPassword))){
            return Response.status(200).build();
        } else {
            return Response.status(400).build();
        }
    }

    @POST
    @Path("/login")
    public Response login(@QueryParam("userName") String userName, @QueryParam("userPassword") String userPassword) {
        if (xUsersDao.authenticateAdmin(userName, calcPassword(userPassword))) {
            String token = utils.generateNewToken();
            if(xUsersDao.setCurrentToken(userName, calcPassword(userPassword), token)){
                return Response.status(200).type(MediaType.TEXT_PLAIN_TYPE).entity(token).build();
            } else {
                return Response.status(500).build();
            }
        } else {
            return Response.status(401).build();
        }

    }

    @POST
    @Path("/edit_user")
    public Response editUser(@QueryParam("token") String token,
                             @QueryParam("id") int id,
                             @QueryParam("firstName") String firstName,
                             @QueryParam("lastName") String lastName,
                             @QueryParam("userName") String userName,
                             @QueryParam("password") String password,
                             @QueryParam("email") String email,
                             @QueryParam("birthday") String birthday,
                             @QueryParam("zip") String zip,
                             @QueryParam("country") String country,
                             @QueryParam("city") String city,
                             @QueryParam("district") String district,
                             @QueryParam("street") String street) throws SQLException {
        if(!checkToken(token)){
            return Response.status(401).build();
        }
        Address addr = new Address(zip, country, city, district, street);
        User user = new User(id, firstName, lastName, userName, calcPassword(password), email, birthday, 1, 0, System.currentTimeMillis(), addr);
        if (xUsersDao.editUser(user)) {
            return Response.status(200).entity("userUpdated").build();
        } else {
            return Response.status(409).build();
        }

    }

    @POST
    @Path("/create_user")
    public Response createUser(@QueryParam("token") String token,
                               @QueryParam("id") int id,
                               @QueryParam("firstName") String firstName,
                               @QueryParam("lastName") String lastName,
                               @QueryParam("userName") String userName,
                               @QueryParam("password") String password,
                               @QueryParam("email") String email,
                               @QueryParam("birthday") String birthday,
                               @QueryParam("zip") String zip,
                               @QueryParam("country") String country,
                               @QueryParam("city") String city,
                               @QueryParam("district") String district,
                               @QueryParam("street") String street) throws SQLException {
        if(!checkToken(token)){
            return Response.status(401).build();
        }
        Address addr = new Address(zip, country, city, district, street);
        User user = new User(id, firstName, lastName, userName, calcPassword(password), email, birthday, 0, System.currentTimeMillis(), System.currentTimeMillis(), addr);
        if (xUsersDao.addNewUser(user)) {
            return Response.status(200).entity("userCreated").build();
        } else {
            return Response.status(409).build();
        }

    }

    private String calcPassword(String pass) {
        int x = pass.hashCode();
        return "" + x;
    }

    private boolean checkToken(String token){
        if(token == null){
            return false;
        }
        return xUsersDao.getCurrentToken().equals(token);
    }

}
