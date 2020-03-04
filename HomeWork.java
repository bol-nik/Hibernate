package libraryHome;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import java.math.BigDecimal;
import java.sql.*;

/**
 * @author Bolokan Nikolay
 */

public class HomeWork {
	private static SessionFactory sessionFactory;
	// Имя JDBC драйвера и URL базы данных
	//static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/";

	// Учетные данные базы данных
	static final String USER = "root";
	static final String PASS = "root";

	public static void main(String[] args) {
		// ====================================JDBC=========================================
		Connection conn = null;
		Statement stmt = null;
		String sql = null;
		try {
			// Регистрация JDBC драйвера
			Class.forName("com.mysql.cj.jdbc.Driver");
//			Driver myDriver = new com.mysql.cj.jdbc.Driver();
//			DriverManager.registerDriver(myDriver);

			// Открытие соединение
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// Выполнить запрос

			// Создаем базу данных
			System.out.println("Creating database...");
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet res = meta.getTables("LibraryTwo", null, null, null);
			if (!res.next()) {
				stmt = conn.createStatement();
				String dbName = "LibraryTwo";
				sql = "Create database IF NOT EXISTS " + dbName;
				stmt.executeUpdate(sql);
				System.out.println("Database created successfully...");
				conn.close();
				stmt.close();
			} else {
				System.out.println("Database exists...");
			}

			// Создаем таблицу
			conn = DriverManager.getConnection("jdbc:mysql://localhost/LibraryTwo", USER, PASS);
			stmt = conn.createStatement();
			meta = conn.getMetaData();
			res = meta.getTables("LibraryTwo", null, "book", null);
			if (!res.next()) {
				System.out.println("Creating table...");
				sql = "Create table book" + "(BookId int not null primary key auto_increment,"
						+ "BookAuthor varchar(100) not null," + "BookName varchar(100) not null,"
						+ "BookPublisher varchar(100) not null," + "BookPrice decimal(7,2) not null,"
						+ "BookQuantity int not null," + "BookVolume int not null" + ");";

				stmt.execute(sql);
				System.out.println("Table created successfully...");
			} else
				System.out.println("Table exists");

			// Заполняем таблицу
			ResultSet rs = stmt.executeQuery("select count(*) as count from book");
			int count = -1;
			if (rs.next()) {
				count = rs.getInt("count");
			}
			if (count == 0) {
				stmt.executeUpdate("Alter table book auto_increment = 1");
				sql = "insert book(BookAuthor,BookName,BookPublisher,BookPrice,BookQuantity,BookVolume) "
						+ "values('Pushkin Alexandr','Evghenii Onegin','Zarea',100,200,120),"
						+ "('Pushkin Alexandr','Ruslan i Ludmila','Zarea',120,56,48),"
						+ "('Tolstoi Lev','Voina i Mir','Aurora',500,423,1589),"
						+ "('Dostoevski','Idiot','Petersburg',430,158,536),"
						+ "('Eminescu','Poeizii','Bucuresti',147,45,77),"
						+ "('Sadoveanu','Fratii Jder','Sibiu',230,122,456),"
						+ "('Shakespeare William','Romeo and Juliet','New York',300,10,123)";
				stmt.executeUpdate(sql);
				System.out.println("The table is full...");

			} else
				System.out.println("The table has data...");

			// Извлечение данных из набора результатов
			sql = "SELECT * FROM book";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {

				int bookId = rs.getInt("BookId");
				String bookAuthor = rs.getString("BookAuthor");
				String bookName = rs.getString("BookName");
				String bookPublisher = rs.getString("bookPublisher");
				String bookPrice = rs.getString("bookPrice");
				String bookQuantity = rs.getString("bookPrice");
				String bookVolume = rs.getString("bookVolume");

				System.out.print(bookId + " " + bookAuthor + " \"" + bookName + "\" " + bookPublisher + " " + bookPrice
						+ " " + bookQuantity + " " + bookVolume + " " + "\n");

			}
			System.out.println();

			stmt.close();
			conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (ClassNotFoundException ex) {
			System.out.println("Error: unable to load driver class!");
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		// ====================================Hibernate=========================================
		sessionFactory = new Configuration().configure().buildSessionFactory();
		HomeWork one = new HomeWork();

		// Добавление
		// one.addBook("Nikolay Bolokan", "JDBC Hibernate", "kisinau", new
		// BigDecimal(25.5), 500_000, 3);

		// Обновление по Id и по имени автора
		// one.updateBookId(8, "Update JDBC Hibernate");
		// one.updateBookAuthor("Nikolay Bolokan", "Update2 JDBC Hibernate");

		// Удаление по Id и имени автора
		// one.deleteBookId(8);
		// one.deleteBookAuthor("Nikolay Bolokan");

		// Вывод всех книг
		one.listBook();

		// ===========================Менеджер сущностей===========================
		Connection conn2 = null;
		Statement stmt2 = null;

		// Получает EntityManager и транзакцию
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("chapter04PU");
		EntityManager em = emf.createEntityManager();
		
		//Проверка на ввод новой книги
		try {
			conn2 = DriverManager.getConnection("jdbc:mysql://localhost/LibraryTwo", USER, PASS);
			stmt2 = conn2.createStatement();
			ResultSet rs2 = stmt2.executeQuery("select count(*) from book b where b.BookAuthor = 'Ivanov'");
			int count = -1;
			if (rs2.next()) {
				count = rs2.getInt("count(*)");
			}
			if (count == 0) {
				// Создает экземпляр Book
				Book book = new Book("Ivanov", "Lesson One", "Berlin", new BigDecimal(156.558), 500_000, 3);

				// Обеспечивает постоянство Book в базе данных
				EntityTransaction tx = em.getTransaction();
				tx.begin();
				em.persist(book);
				tx.commit();
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Выполняет именованный запрос
		System.out.println("Именованный запрос на вывод всех книг: findAllBook");
		TypedQuery<Book> query = em.createNamedQuery("findAllBook", Book.class);
		List<Book> results = query.getResultList();
		for (Object b : results) {

			System.out.println(b);
			System.out.println();
		}
		
		// Выполняет именованный запрос c параметром
		System.out.println("\nИменованный запрос с параметром: findByName");
		@SuppressWarnings("unchecked")
		List<Book> results2= em.createNamedQuery("findByName").setParameter("name", "Ivanov").getResultList();
	
		for (Object b : results2) {

			System.out.println(b);
			System.out.println();
		}
	

		// Закрывает EntityManager и EntityManagerFactory
		em.close();
		emf.close();

		System.out.println("Goodbye!");

	}

	public void addBook(String BookAuthor, String BookName, String BookPublisher, BigDecimal BookPrice,
			int BookQuantity, int BookVolume) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		transaction = session.beginTransaction();

		Book tmp = new Book(BookAuthor, BookName, BookPublisher, BookPrice, BookQuantity, BookVolume);
		session.save(tmp);
		transaction.commit();
		session.close();
	}

	public void updateBookId(int Id, String BookName) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		transaction = session.beginTransaction();
		Book tmp = session.get(Book.class, Id);
		tmp.setBookName(BookName);
		session.update(tmp);
		transaction.commit();
		session.close();
	}

	public void deleteBookId(int Id) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		transaction = session.beginTransaction();
		Book tmp = session.get(Book.class, Id);
		session.delete(tmp);
		transaction.commit();
		session.close();
	}

	public void updateBookAuthor(String Author, String BookName) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		org.hibernate.query.Query<Book> query = session
				.createQuery("update Book set BookName = :newBookName where BookAuthor = '" + Author + "'");
		((org.hibernate.query.Query<Book>) query).setParameter("newBookName", BookName);
		((javax.persistence.Query) query).executeUpdate();
		transaction.commit();
		session.close();
	}

	public void deleteBookAuthor(String Author) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		org.hibernate.query.Query<Book> query = session.createQuery("delete from Book where BookAuthor = '" + Author + "'");
		((javax.persistence.Query) query).executeUpdate();
		transaction.commit();
		session.close();
	}

	public void listBook() {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		transaction = session.beginTransaction();
		List<Book> all = session.createQuery("FROM Book").list();

		for (Book book : all) {
			System.out.println(book);
			System.out.println("\n================\n");
		}
		session.close();
	}

}
