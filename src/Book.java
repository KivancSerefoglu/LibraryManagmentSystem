import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Book {
    private String name;
    private int amount;
    private String genre;
    private  int bookID;
    private int isActive;


    public Book(String name, int amount, String genre, Statement statement) throws SQLException {
        this.name= name;
        this.amount=amount;
        this.genre= genre;
        this.isActive=1;

        String id ="SELECT MAX(bookID) FROM Book";
        ResultSet rs = statement.executeQuery(id);

        int k=0;
        while (rs.next()) {
            k = rs.getInt(1);
        }

        this.bookID= (k+1);



    }

    public int getBookID() {
        return bookID;
    }
}
