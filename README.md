I created 4 different table which are User, Book, History and Genre on SQL WORKBENCH. 


•	USER TABLE

o	Attributes: userID, name, email, address and isActive.

o	 I use isActive because I want to hold old user in the History table, so I do not delete the user on table. I just turned İS Active to 0 ( 1 means he/she is active user in library, 0 means he/she is not active in library) 

o	userID is a primary key

•	BOOK TABLE

o	Attributes: bookID, name, amount, genreName and isActive

o	I use isActive again for the same reason I use it in the User table.

o	bookID is a primary key

o	genreName is a foreign key from Genre table

•	History

o	I created this table to see their past book purchases to lend

o	Attributes: checkoutID, userID, bookID and returned

o	I use returned attributes to see if the book has been returned

o	checkoutID is a primary key

o	userID is taken from User table as foreign key and bookID is taken from Book table as foreign key.

•	Genre

o	I created this table to hold the genre information

o	Attributes: genreID and genreName

o	genreID is a primary key and genreName is an unique because there shouldn’t be two same name in genre.

I created 4 class which are LibraryManagementSystem, History, User and Book on JAVA.


•	History, User and Book

o	I created these classes because of the holding unique id.

o	 I did ID like this I take the highest number in the column from SQLWorkbench and increment it by 1 so it became unique.


•	LibraryManagementSystem


o	It is a class which includes methods. 

o	addUser and createBook methods take the parameters and then create a constructor in JAVA. After that insert into SQLWrokbench by taking the ID from created constructor.  

o	deleteUser and deleteBook methods did not delete the books or users on SQL. they just update the isActive data to 0 on SQL and it means the users or books were deleted. I did like this because I want to hold history of them on SQL. Also I check the user if he still has the book that he borrowed previously because the system does not delete the user if he has not returned the book back yet. also the book is controlled and deleted by this way.  

o	listAllBooks and listBooksGenres methods show the books. I use System.out.format() because make it more readable  

o	checkoutBook method provides to user borrow a book. Firstly it take active user ID (“isActive” =1) then take active bookID and check the book library has the book to lend to the user.  

o	returnBook method take the book from user and increment the amount of the book in library by 1.  

o	getCurrentlyBorrowedBooks method show the books borrowed and not returned. I use two extra statement because access the user name and book name.  
 
o	getCurrentlyHistoryUser method show the books which are borrowed. Also whow the books which are returned or not returned.  

o	writeMainPage method display the main page. I created this method to show it again and again. This method include all method. For (num == 4) if condition, I us resCount because if user entry the higher the number of the books it want user to entry new number and it is same for (num==6) genres. I also designed the menu in a way that is convenient for the user.
 
	 
