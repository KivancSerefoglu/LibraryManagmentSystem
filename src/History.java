import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class History {
    private int checkoutID;
    private  int userID;
    private int bookID;

    public History(int userID, int bookID, Statement statement) throws SQLException {
        this.bookID=bookID;
        this.userID=userID;

        String id ="SELECT MAX(checkoutID) FROM History";
        ResultSet rs = statement.executeQuery(id);

        int k=0;
        while (rs.next()) {
            k = rs.getInt(1);
        }

        this.checkoutID= (k+1);
    }

    public int getCheckoutID() {
        return checkoutID;
    }

}
