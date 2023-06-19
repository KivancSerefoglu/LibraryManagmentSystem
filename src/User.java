import com.sun.jdi.event.StepEvent;

import java.sql.*;

public class User {
    private String name;
    private int userID;
    private String email;
    private String address;
    private int isActive;

    private  int ID;
    public User(String name, String email, String address, Statement statement) throws SQLException {
        this.name=name;
        this.email=email;
        this.address=address;
        this.isActive=1;

        String id ="SELECT MAX(userID) FROM User";
        ResultSet rs = statement.executeQuery(id);

        int k=0;
        while (rs.next()) {
            k = rs.getInt(1);
        }

        this.userID= (k+1);
    }

    public int getUserID() {
        return userID;
    }

}
