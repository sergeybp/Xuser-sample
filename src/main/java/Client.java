import entities.Address;
import entities.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

/**
 * Created by sergeybp on 20.07.17.
 */
public class Client {

    private HttpClient client;
    private String address = "0.0.0.0";
    private int port = 8880;
    URIBuilder BUILDER = new URIBuilder();
    private String currentToken = "-1";

    public static void main(String[] args) throws URISyntaxException {
        if (args.length < 2) {
            System.err.println("Please run with parameters: address port");
            System.exit(0);
        }
        new Client().run(args[0], Integer.parseInt(args[1]));
    }

    void run(String address, int port) throws URISyntaxException {
        this.address = address;
        this.port = port;

        BUILDER.setScheme("http").setHost(address).setPort(port);
        if (!connect()) {
            System.err.println("Server is unreachable.");
            System.exit(0);
        }


        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        new Thread(new Runnable() {
            public void run() {
                System.out.println("Welcome to XUsers client!");
                while (true) {
                    try {
                        String line = reader.readLine();
                        if (line == null || line.equals("exit")) {
                            System.exit(0);
                            return;
                        }
                        String[] slices = line.trim().split(" +");
                        switch (slices[0]) {
                            case "help":
                                System.out.println("Usage:\n" +
                                        "login <userName> <password> -- login for session\n" +
                                        "get -- get list of users in system\n" +
                                        "search -n <namePart> | -e <email> | -b <birthday> -- search for users\n" +
                                        "activate <userId> -- activate user by ID\n" +
                                        "deactivate <userId> -- deactivate user by ID\n" +
                                        "change <newPassword> -- change admin password\n" +
                                        "create <userId> <firstName> <lastName> <userName> <password>\n" +
                                        " <email> <birthday> <zip> <country> <city> <district> <street> -- create new user\n" +
                                        "edit <userId> <firstName> <lastName> <userName> <password>\n" +
                                        " <email> <birthday> <zip> <country> <city> <district> <street> -- edit user (mapped by ID)\n\n");
                                break;
                            case "login":
                                if (slices.length < 3) {
                                    System.err.println("Wrong format");
                                    break;
                                }
                                String userName = slices[1];
                                String userPassword = slices[2];
                                tryLogin(userName, userPassword);

                                break;
                            case "get": {
                                getUsers();
                                break;
                            }
                            case "create": {
                                if (slices.length < 13) {
                                    System.err.println("Wrong format");
                                    break;
                                }
                                Address address = new Address(slices[8], slices[9], slices[10], slices[11], slices[12]);
                                User user = new User();
                                user.id = Integer.parseInt(slices[1]);
                                user.firstName = slices[2];
                                user.lastName = slices[3];
                                user.userName = slices[4];
                                user.password = slices[5];
                                user.email = slices[6];
                                user.birthday = slices[7];
                                user.address = address;
                                createUser(user);
                                break;
                            }
                            case "search": {
                                if (slices.length < 3) {
                                    System.err.println("Wrong format");
                                    break;
                                }
                                searchUsers(slices[1], slices[2]);
                                break;

                            }
                            case "activate": {
                                if (slices.length < 2) {
                                    System.err.println("Wrong format");
                                    break;
                                }
                                activate(Integer.parseInt(slices[1]));
                                break;
                            }
                            case "deactivate": {
                                if (slices.length < 2) {
                                    System.err.println("Wrong format");
                                    break;
                                }
                                deactivate(Integer.parseInt(slices[1]));
                                break;
                            }
                            case "change": {
                                if (slices.length < 2) {
                                    System.err.println("Wrong format");
                                    break;
                                }
                                changeAdminPassword((slices[1]));
                                break;
                            }
                            case "edit": {
                                if (slices.length < 13) {
                                    System.err.println("Wrong format");
                                    break;
                                }
                                Address address = new Address(slices[8], slices[9], slices[10], slices[11], slices[12]);
                                User user = new User();
                                user.id = Integer.parseInt(slices[1]);
                                user.firstName = slices[2];
                                user.lastName = slices[3];
                                user.userName = slices[4];
                                user.password = slices[5];
                                user.email = slices[6];
                                user.birthday = slices[7];
                                user.address = address;
                                editUser(user);
                                break;
                            }
                            default:
                                System.err.println("Unknown command\nUse help command to get some tips.");
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private boolean connect() throws URISyntaxException {
        client = HttpClientBuilder.create().build();
        URIBuilder builder = BUILDER;
        builder.setPath("/is_alive");
        HttpGet request = new HttpGet(builder.build());
        try {
            client.execute(request);
        } catch (Exception r) {
            return false;
        }
        return true;
    }

    private void activate(int id) throws URISyntaxException, IOException {
        client = HttpClientBuilder.create().build();
        URIBuilder builder = BUILDER;
        builder.setPath("/activate")
                .setParameter("token", currentToken)
                .setParameter("userId", "" + id);
        HttpPost post = new HttpPost(builder.build());
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 401) {
            System.err.println("Unauthorized !");
            return;
        }
        if (response.getStatusLine().getStatusCode() == 400) {
            System.err.println("Error!");
        } else if (response.getStatusLine().getStatusCode() == 200) {
            System.out.println("User activated.");
        }

    }

    private void deactivate(int id) throws URISyntaxException, IOException {
        client = HttpClientBuilder.create().build();
        URIBuilder builder = BUILDER;
        builder.setPath("/deactivate")
                .setParameter("token", currentToken)
                .setParameter("userId", "" + id);
        HttpPost post = new HttpPost(builder.build());
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 401) {
            System.err.println("Unauthorized !");
            return;
        }
        if (response.getStatusLine().getStatusCode() == 400) {
            System.err.println("Error!");
        } else if (response.getStatusLine().getStatusCode() == 200) {
            System.out.println("User deactivated.");
        }

    }

    private void changeAdminPassword(String newPassword) throws URISyntaxException, IOException {
        client = HttpClientBuilder.create().build();
        URIBuilder builder = BUILDER;
        builder.setPath("/change_admin_password")
                .setParameter("token", currentToken)
                .setParameter("newPassword", "" + newPassword);
        HttpPost post = new HttpPost(builder.build());
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 401) {
            System.err.println("Unauthorized !");
            return;
        }
        if (response.getStatusLine().getStatusCode() == 400) {
            System.err.println("Error!");
        } else if (response.getStatusLine().getStatusCode() == 200) {
            System.out.println("Password changed!");
        }

    }

    private void tryLogin(String userName, String userPassword) throws IOException, URISyntaxException {
        client = HttpClientBuilder.create().build();
        URIBuilder builder = BUILDER;
        builder.setPath("/login")
                .setParameter("userName", userName)
                .setParameter("userPassword", userPassword);
        HttpPost post = new HttpPost(builder.build());
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 401) {
            System.err.println("Wrong credentials!");
            return;
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        line = rd.readLine();
        if (line == null) {
            System.err.println("Error occurred.");
            return;
        }
        currentToken = line;
        System.out.println("Login successful. Received token: " + currentToken);
    }

    private void getUsers() throws IOException, URISyntaxException {
        client = HttpClientBuilder.create().build();

        URIBuilder builder = BUILDER;
        builder.setPath("/get_users")
                .setParameter("token", currentToken);
        HttpGet request = new HttpGet(builder.build());
        HttpResponse response = client.execute(request);
        if (response.getStatusLine().getStatusCode() == 401) {
            System.err.println("Unauthorized !");
            return;
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
    }

    private void searchUsers(String parameter, String text) throws URISyntaxException, IOException {
        client = HttpClientBuilder.create().build();

        URIBuilder builder = BUILDER;
        builder.setPath("/search_users")
                .setParameter("token", currentToken)
                .setParameter("searchParameter", parameter)
                .setParameter("searchText", text);
        HttpGet request = new HttpGet(builder.build());
        HttpResponse response = client.execute(request);
        if (response.getStatusLine().getStatusCode() == 401) {
            System.err.println("Unauthorized !");
            return;
        }
        if (response.getStatusLine().getStatusCode() == 404) {
            System.err.println("Users not found!");
            return;
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
    }

    private void createUser(User user) throws IOException, URISyntaxException {
        client = HttpClientBuilder.create().build();
        URIBuilder builder = BUILDER;
        builder.setPath("/create_user")
                .setParameter("token", currentToken)
                .setParameter("id", String.valueOf(user.id))
                .setParameter("firstName", user.firstName)
                .setParameter("lastName", user.lastName)
                .setParameter("userName", user.userName)
                .setParameter("password", user.password)
                .setParameter("email", user.email)
                .setParameter("birthday", user.birthday)
                .setParameter("zip", user.address.zip)
                .setParameter("country", user.address.country)
                .setParameter("city", user.address.city)
                .setParameter("district", user.address.district)
                .setParameter("street", user.address.street);
        HttpPost post = new HttpPost(builder.build());
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 401) {
            System.err.println("Unauthorized !");
            return;
        }
        if (response.getStatusLine().getStatusCode() == 409) {
            System.err.println("Error while creating new user.");
            return;
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        line = rd.readLine();
        if (line == null) {
            System.err.println("Error occurred.");
            return;
        }
        System.out.println("User created!");
    }

    private void editUser(User user) throws IOException, URISyntaxException {
        client = HttpClientBuilder.create().build();
        URIBuilder builder = BUILDER;
        builder.setPath("/edit_user")
                .setParameter("token", currentToken)
                .setParameter("id", String.valueOf(user.id))
                .setParameter("firstName", user.firstName)
                .setParameter("lastName", user.lastName)
                .setParameter("userName", user.userName)
                .setParameter("password", user.password)
                .setParameter("email", user.email)
                .setParameter("birthday", user.birthday)
                .setParameter("zip", user.address.zip)
                .setParameter("country", user.address.country)
                .setParameter("city", user.address.city)
                .setParameter("district", user.address.district)
                .setParameter("street", user.address.street);
        HttpPost post = new HttpPost(builder.build());
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 401) {
            System.err.println("Unauthorized !");
            return;
        }
        if (response.getStatusLine().getStatusCode() == 409) {
            System.err.println("Error while updating user.");
            return;
        }
        System.out.println("User updated!");
    }

}
