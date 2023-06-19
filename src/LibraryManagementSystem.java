import java.sql.*;
import java.util.Scanner;

public class LibraryManagementSystem {



    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/bookreservatşon";
    static final String USER = "root";
    static final String PASS = "";
    /* It is highly recommended to put the following code inside a try-catch structure. */
    public static Connection connection = null;



    public LibraryManagementSystem() throws SQLException {
    }

    public static void establishConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void lastPart(Statement statement) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Type 1 to go back to the main page \nType 2 to Exit ");
        int type = sc.nextInt();
        if (type== 1)
            writeMainPage(statement);
        else if (type==2)
            System.out.println("Have a nice day!");
    }


    public static void addUser(String name, String email, String address, Statement statement) throws SQLException {
        User user = new User(name,email,address, statement);
        String insertQuery = "INSERT INTO User VALUES('"+user.getUserID()+"','"+name+"','"+email+"','"+address+"','1')";
        statement.executeUpdate(insertQuery);
        System.out.println("Your ID is "+user.getUserID());
        System.out.println("Successfully added user");
    }

    public static void deleteUser(int id, Statement statement) throws SQLException {
        ResultSet res = statement.executeQuery("SELECT * FROM  history WHERE returned = '0' AND userID='"+id+"'");
        int checkTheUser = 0;
        if (res.next())
            checkTheUser = res.getInt("checkoutID");
        if (checkTheUser==0){
        statement.executeUpdate("UPDATE user SET isActive = '0'  WHERE userID = '" + id + "' ");
        System.out.println("Successfully removed user");
        }else
            System.out.println("Since the user is borrowing a book, it cannot be deleted.");
    }

    public static void createBook(String name, int amount, String genre, Statement statement) throws SQLException {
        Book book= new Book(name, amount, genre, statement);
        String insertQuery = "INSERT INTO Book VALUES('"+book.getBookID()+"','"+name+"','"+amount+"','"+genre+"','1')";
        statement.executeUpdate(insertQuery);
        System.out.println("Successfully added book");
    }

    public static void deleteBook(int id, Statement statement) throws SQLException {
        ResultSet res = statement.executeQuery("SELECT * FROM  history WHERE returned = '0' AND bookID='"+id+"'");
        int checkTheBook = 0;
        if (res.next())
            checkTheBook = res.getInt("checkoutID");
        if (checkTheBook==0){
        statement.executeUpdate("UPDATE book SET isActive = '0'  WHERE bookID = '" + id + "' ");
        System.out.println("Successfully removed book");
        }else
            System.out.println("Since the book is on loan, it cannot be deleted from the library.");

    }

    public static void listAllBooks(Statement statement) throws SQLException {
        ResultSet res = statement.executeQuery("SELECT * FROM  book WHERE isActive = '1'");

        if (!res.first()){
            System.out.println("There are no books in the library.");
        }else{
            int amount = res.getInt("amount");
            String name = res.getString("name");
            int bookID = res.getInt("bookID");
            System.out.format("%10s%25s%30s\n","\033[1mID\033[0m", "\033[1mBook Name\033[0m", "\033[1mQuantity\033[0m");
            System.out.println("----------------------------------------------------");
            System.out.format("%-10d%-25s%-20d\n",bookID, name, amount);
            System.out.println("----------------------------------------------------");
            while (res.next()){
                int bookID2 = res.getInt("bookID");
                int amount2 = res.getInt("amount");
                String name2 = res.getString("name");
                System.out.format("%-10d%-25s%-20d\n", bookID2, name2, amount2);
                System.out.println("----------------------------------------------------");
            }


    }}

    public  static void listBooksGenres(int genre_id, Statement statement) throws SQLException {
        ResultSet resG = statement.executeQuery("SELECT genreName FROM  genre WHERE genreID='"+genre_id+"'" );
        String genre_name=null;
        if (resG.next())
            genre_name= resG.getString("genreName");

        ResultSet res = statement.executeQuery("SELECT * FROM  book WHERE isActive='1' AND genreName='"+genre_name+"'" );

        if (!res.first()){
            System.out.println("There are no "+genre_name+" in the library.");
        }else{
            int amount = res.getInt("amount");
            String name = res.getString("name");
            String genre= res.getString("genreName");
            System.out.format("%-32s%10s%32s\n", "\033[1mBook Name\033[0m", "\033[1mQuantity\033[0m","\033[1mGenre\033[0m");
            System.out.println("-------------------------------------------------------------");
            System.out.format("%-32s%-10d%13s\n", name, amount, genre);
            System.out.println("-------------------------------------------------------------");

            while (res.next()){
                int amount2 = res.getInt("amount");
                String name2 = res.getString("name");
                String genre2= res.getString("genreName");
                System.out.format("%-32s%-10d%13s\n", name2, amount2, genre2);
                System.out.println("-------------------------------------------------------------");
            }
            }
    }


    public static void checkoutBook(int bookID, int userID, Statement statement) throws SQLException {
        ResultSet resU = statement.executeQuery("SELECT * FROM  user WHERE userID = '"+userID+"' AND isActive='1'" );
        int activeUserID=0;
        if (resU.next())
            activeUserID=resU.getInt("userID");
        ResultSet res = statement.executeQuery("SELECT amount FROM  book WHERE bookID = '"+bookID+"' AND isActive='1'" );
        int amount_num=0;
        if (res.next())
             amount_num = res.getInt("amount");

        History history=new History(userID,bookID, statement);

        if (amount_num > 0) {
            String insertQuery = "INSERT INTO History VALUES('" + history.getCheckoutID() + "','" + activeUserID + "','" + bookID + "','" + 0 + "')";
            statement.executeUpdate(insertQuery);
            statement.executeUpdate("UPDATE book SET amount = '" + (amount_num - 1) + "'  WHERE bookID = '" + bookID + "' ");
            System.out.println("The book was successfully borrowed.");
        } else
            System.out.println("This book is not available in the library.");
    }

    public static void returnBook(int checkoutID, Statement statement) throws SQLException {
        ResultSet res = statement.executeQuery("SELECT * FROM  history WHERE checkoutID = '"+checkoutID+"'" );
        int return_value = 0;
        if (res.next()) {
            return_value = res.getInt("returned");
            if (return_value == 1)
                System.out.println("The book has already been returned");
            else if (return_value == 0) {
                int bookID= res.getInt("bookID");
                ResultSet rs = statement.executeQuery("SELECT amount FROM  book WHERE bookID = '"+bookID+"' AND isActive='1'" );
                int amount_num =0;
                if(rs.next())
                    amount_num=rs.getInt("amount");
                statement.executeUpdate("UPDATE book SET amount = '" + (amount_num + 1) + "'  WHERE bookID = '" + bookID + "' ");
                statement.executeUpdate("UPDATE history SET returned = '1'  WHERE checkoutID= '" + checkoutID + "' ");
                System.out.println("The book has been successfully returned.");
            }
        }
    }

    public static void getCurrentlyBorrowedBooks (Statement statement) throws SQLException {
        Statement statement2 = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        Statement statement3 = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet res = statement.executeQuery("SELECT * FROM  history WHERE returned = '0'" );
        if (!res.first()){
            System.out.println("There are no borrowed books.");
        }else{
            System.out.format("%10s%25s%30s\n","\033[1mID\033[0m", "\033[1mBook Name\033[0m", "\033[1mBorrower\033[0m");
            System.out.println("----------------------------------------------------");
            int bookID= res.getInt("bookID");
            int userID = res.getInt("userID");
            ResultSet bRs = statement2.executeQuery("SELECT name FROM  book WHERE bookID = '"+bookID+"'" );
            String bookName = null;
            if (bRs.next()) {
                bookName = bRs.getString("name");
            }
            ResultSet uRs = statement3.executeQuery("SELECT name FROM  user WHERE userID = '"+userID+"'" );
            String userName= null;
            if (uRs.next()) {
                userName = uRs.getString("name");
            }
            int checkoutID = res.getInt("checkoutID");

            System.out.format("%-10d%-25s%-20s\n",checkoutID, bookName, userName);
            System.out.println("----------------------------------------------------");

            while (res.next()){
                int bookID2= res.getInt("bookID");
                int userID2 = res.getInt("userID");
                ResultSet bRs2 = statement2.executeQuery("SELECT name FROM  book WHERE bookID = '"+bookID2+"'" );
                String bookName2 = null;
                if (bRs2.next()) {
                    bookName2 = bRs2.getString("name");
                }
                ResultSet uRs2 = statement3.executeQuery("SELECT name FROM  user WHERE userID = '"+userID2+"'" );
                String userName2= null;
                if (uRs2.next()) {
                    userName2 = uRs2.getString("name");
                }

                int checkoutID2 = res.getInt("checkoutID");

                System.out.format("%-10d%-25s%-20s\n",checkoutID2, bookName2, userName2);
                System.out.println("----------------------------------------------------");
            }
        }
    }

    public static void getCurrentlyHistoryOfUser(int user_id, Statement statement) throws SQLException {
        Statement statement2 = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet res = statement.executeQuery("SELECT * FROM  history WHERE userID = '"+user_id+"'");
        if (!res.first()){
            System.out.println("He/She hasn't borrowed any books.");
        }else{
            System.out.format("%-32s%10s\n", "\033[1mBook Name\033[0m", "\033[1mStatus\033[0m");
            System.out.println("-------------------------------------------------------------");
            int bookID= res.getInt("bookID");
            ResultSet bRs = statement2.executeQuery("SELECT name FROM  book WHERE bookID = '"+bookID+"'" );
            String bookName = null;
            if (bRs.next()) {
                bookName = bRs.getString("name");
            }
            String status=null;
            int status_num = res.getInt("returned");
            if (status_num==1)
                status = "returned";
            else if (status_num==0)
                status = "not returned";
            System.out.format("%-32s%-10s\n", bookName, status);
            System.out.println("-------------------------------------------------------------");

            while (res.next()){
                int bookID2= res.getInt("bookID");
                ResultSet bRs2 = statement2.executeQuery("SELECT name FROM  book WHERE bookID = '"+bookID2+"'" );
                String bookName2 = null;
                if (bRs2.next()) {
                    bookName2 = bRs2.getString("name");
                }
                String status2=null;
                int status_num2 = res.getInt("returned");
                if (status_num2==1)
                    status2 = "returned";
                else if (status_num2==0)
                    status2 = "not returned";
                System.out.format("%-32s%-10s\n", bookName2, status2);
                System.out.println("-------------------------------------------------------------");

            }




        }
    }


    public static void writeMainPage(Statement statement) throws SQLException {

        System.out.println("-------------------------------------------------------------\n" +
                "| Please write the number of the transaction you want to do |\n" +
                "-------------------------------------------------------------\n" +
                "0. Exit\n" +
                "1. Add User(name, email, address)\n" +
                "2. Remove User(user id)\n" +
                "3. Create Book(name, amount, genre(must be selected from a list))\n" +
                "4. Remove Book(book id)\n" +
                "5. List all books with stock info\n" +
                "6. List all books from a Specific Genre, with stock info(genre_id)\n" +
                "7. Get Currently Borrowed Books\n" +
                "8. Get Currently Borrowed and Borrow History of a User(user_id)\n" +
                "9. Checkout book(book_id, user_id)\n" +
                "10.Return Book(checkout_id)\n" +
                "-------------------------------------------------------------\n" );

        Scanner sc = new Scanner(System.in);

        int num = sc.nextInt();
        if (num== 0){
            System.out.println("Have a nice day!");
        }else if( num == 1){
            System.out.println("Please type the user's name:");
            String name = sc.next();
            System.out.println("Please type the user's email:");
            String email = sc.next();
            System.out.println("Please type the user's address:");
            String address = sc.next();
            addUser(name, email, address, statement);
            lastPart(statement);
        }else if (num == 2){
            System.out.println("Please enter the ID of the user you want to remove:");
            int deleteID = sc.nextInt();
            deleteUser(deleteID, statement);
            lastPart(statement);
        }else if (num==3){
            System.out.println("Please type the book's name:");
            sc.nextLine();
            String name = sc.nextLine();
            System.out.println("Please type amount of the book:");
            int amount = sc.nextInt();
            ResultSet res = statement.executeQuery("SELECT * FROM  genre" );
            while(res.next()){
                System.out.println(res.getInt("genreID")+". "+ res.getString("genreName"));
            }
            System.out.println("Please type the number of the book's genre:");

            int genreID = sc.nextInt();
            ResultSet ps = statement.executeQuery ("SELECT genreName FROM  Genre WHERE genreID='" + genreID + "'");
            String genre_Name=null;
            if(ps.next())
                genre_Name = ps.getString("genreName");
            createBook(name, amount, genre_Name, statement);
            lastPart(statement);

        }else if (num ==4){
            listAllBooks(statement);
            System.out.println("Please enter the ID of the book you want to remove:");
            int deleteID = sc.nextInt();
            ResultSet resCount =statement.executeQuery("SELECT MAX(bookID) FROM Book");
            int count=0;
            if (resCount.next())
                count = resCount.getInt(1);

            while (deleteID>count){
                System.out.println("You entered an incorrect number, please enter one of the above types:");
                deleteID = sc.nextInt();
            }
            deleteBook(deleteID, statement);
            lastPart(statement);
        }else if (num == 5){
            listAllBooks(statement);
            lastPart(statement);
        }else if (num == 6){
            ResultSet resGW = statement.executeQuery("SELECT * FROM  genre" );
            System.out.format("%10s%25s\n","\033[1mgenreID\033[0m", "\033[1mGenre Name\033[0m");
            System.out.println("----------------------------------------------------");


            while (resGW.next()){
                String genreName = resGW.getString("genreName");
                int genreID = resGW.getInt("genreID");
                System.out.format("%-15d%-25s\n", genreID, genreName);
                System.out.println("----------------------------------------------------");


            }
            System.out.println("Please enter the ID of the genre you want to see:");
            int genreID = sc.nextInt();

            ResultSet resCount =statement.executeQuery("SELECT MAX(genreID) FROM Genre");
            int count=0;
            if (resCount.next())
                count = resCount.getInt(1);

            while (genreID>count){
                System.out.println("You entered an incorrect number, please enter one of the above types:");
                genreID = sc.nextInt();
            }
            listBooksGenres(genreID, statement);
            lastPart(statement);
        }else if (num==7){
            getCurrentlyBorrowedBooks(statement);
            lastPart(statement);
        }else if (num==8){
            System.out.println("Enter the ID of the user you want to see the borrowed books:");
            int borrowUserID = sc.nextInt();
            getCurrentlyHistoryOfUser(borrowUserID, statement);
            lastPart(statement);
        }else if (num==9){
            listAllBooks(statement);
            System.out.println("\nPlease enter the ID of the book you want to borrow:");
            int borrowBookID = sc.nextInt();
            System.out.println("Please enter the ID of the user who wants to borrow:");
            int borrowUserID = sc.nextInt();
            checkoutBook(borrowBookID,borrowUserID, statement);
            lastPart(statement);
        }else if (num==10){
            getCurrentlyBorrowedBooks(statement);
            System.out.println("Please enter the ID of the return transaction:");
            int returnBookID = sc.nextInt();
            returnBook(returnBookID,statement);
            lastPart(statement);

        }
    }




    public static void main(String[] args) throws SQLException {


        try {
            establishConnection();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            writeMainPage(statement);

            closeConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}

