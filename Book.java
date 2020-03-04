package libraryHome;
import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@NamedQueries({
@NamedQuery(name = "findAllBook",
query = "SELECT b FROM Book b"),
@NamedQuery(name = "findByName",
query = "SELECT b FROM Book b where b.BookAuthor = :name")
})

@Table(name="book", schema = "librarytwo")
public class Book {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name="BookId")
private int id;

@Basic
@Column(name="BookName")
private String BookName;

@Basic
@Column(name="BookAuthor")
private String BookAuthor;

@Basic
@Column(name="BookPublisher")
private String  BookPublisher;

@Basic
@Column(name ="BookPrice")
private BigDecimal BookPrice;

@Basic
@Column(name ="BookQuantity")
private int BookQuantity;

@Basic
@Column(name ="BookVolume")
private int BookVolume;


public String getBookName() {
	return BookName;
}
public void setBookName(String BookName) {
	this.BookName = BookName;
}

public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getBookAuthor() {
	return BookAuthor;
}
public void setBookAuthor(String bookAuthor) {
	BookAuthor = bookAuthor;
}
public String getBookPublisher() {
	return BookPublisher;
}
public void setBookPublisher(String bookPublisher) {
	BookPublisher = bookPublisher;
}
public BigDecimal getBookPrice() {
	return BookPrice;
}
public void setBookPrice(BigDecimal bookPrice) {
	BookPrice = bookPrice;
}
public int getBookQuantity() {
	return BookQuantity;
}
public void setBookQuantity(int bookQuantity) {
	BookQuantity = bookQuantity;
}
public int getBookVolume() {
	return BookVolume;
}
public void setBookVolume(int bookVolume) {
	BookVolume = bookVolume;
}


public Book() {
	
}
public Book(String BookAuthor,String BookName, String BookPublisher, BigDecimal BookPrice, int BookQuantity, int BookVolume) {
	this.BookAuthor=BookAuthor;
	this.BookName=BookName;
	this.BookPublisher=BookPublisher;
	this.BookPrice=BookPrice;
	this.BookQuantity=BookQuantity;
	this.BookVolume=BookVolume;
}

@Override
public String toString() {
	return "BookAuthor: "+BookAuthor+
	"\nBookName: "+BookName+
    "\nBookPublisher: "+BookPublisher+
   "\nBookPrice: "+BookPrice+
    "\nBookQuantity: "+BookQuantity+
    "\nBookVolume: "+BookVolume;
}

}
