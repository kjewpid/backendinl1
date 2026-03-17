package se.yrgo;

import java.util.List;
import java.util.Set;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("databaseConfig");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();

        // Lägger till 3 authors
        Author danishAuthor = new Author("Lukas", "Denmark");
        Author frenchAuthor = new Author("Josephine", "France");
        Author polishAuthor = new Author("Murdock", "Poland");

        // Skapar 7 böcker och kopplar till författare
        Book gardeningBook = new Book("Gardening for non-green fingers", "Hobby", 2002);
        Book cookBook = new Book("Cook-along with Le Chef", "Culinary", 2015);
        Book scaryBook = new Book("Spooky stuff goin on in here", "Horror", 2000);
        Book coloringBook = new Book("The most calming coloring book", "Hobby", 2024);
        Book biologyBook = new Book("Coolest sharks ever that are also cute", "Biology", 2018);
        Book scienceBook = new Book("Quick Science Cool Facts", "Science", 2019);
        Book kidsBook = new Book("Mamma mu doing her thang", "Picture Book", 2005);

        // Lägger till böcker i arrayer för att göra det lättare att lägga till
        Book[] danishBooks = { scaryBook, coloringBook, kidsBook, scienceBook };
        Book[] frenchBooks = { cookBook, gardeningBook };

        // Lägger till böckerna till authors
        danishAuthor.addMultipleBooks(danishBooks);
        frenchAuthor.addMultipleBooks(frenchBooks);
        polishAuthor.addBook(biologyBook);

        // Sparar bara authors i databasen, behöver inte göra det med böckerna med tack
        // vare cascade & CascadeType.PERSIST
        em.persist(danishAuthor);
        em.persist(frenchAuthor);
        em.persist(polishAuthor);

        // Skapar 3 läsare
        Reader happyReader = new Reader("Tulipe", "tulipe@mail.com");
        Reader unmotivatedReader = new Reader("Papillon", "papillon16@mail.com");
        Reader intenseReader = new Reader("Milou", "tintinsnumber1@mail.com");

        // Sparar till databas
        em.persist(happyReader);
        em.persist(unmotivatedReader);
        em.persist(intenseReader);

        // Lägger till lästa böcker
        happyReader.addMultipleReadBooks(Set.of(gardeningBook, coloringBook, cookBook));
        unmotivatedReader.addMultipleReadBooks(Set.of(biologyBook, scienceBook));
        intenseReader.addMultipleReadBooks(Set.of(scaryBook, kidsBook, scienceBook));

        et.commit();

        // Hämta alla böcker av en specifik författare (JPQL)
        Author author = em.createQuery("select a from Author a where a.name = :name", Author.class)
                .setParameter("name", "Lukas")
                .getSingleResult();

        author.getWrittenBooks().forEach(System.out::println);

        List<Book> booksByAuthor = em
                .createQuery("select b from Author a join a.writtenBooks b where a.name = :name", Book.class)
                .setParameter("name", "Lukas")
                .getResultList();
        booksByAuthor.forEach(System.out::println);

        // Hämta alla läsare( readers) som har läst en viss bok (member of)
        Book science = em.createQuery("select b from Book b where b.title = :title", Book.class)
                .setParameter("title", "Quick Science Cool Facts")
                .getSingleResult();

        List<Reader> readers = em.createQuery("select r from Reader r where :book member of r.readBooks", Reader.class)
                .setParameter("book", science)
                .getResultList();
        readers.forEach(x -> System.out.println(x.getName()));

        // Hämta författare vars böcker har lästs av minst en läsare (join)
        List<Author> authors = em
                .createQuery("select distinct a from Author a join a.writtenBooks b join b.readers r", Author.class)
                .getResultList();
        authors.forEach(x -> System.out.println(x.getName()));

        // Räkna antalet böcker per författare (Aggregation Query)
        List<Object[]> numOfBooks = em
                .createQuery("select a.name, count(b) from Author a join a.writtenBooks b group by a.name")
                .getResultList();
        for (Object[] obj : numOfBooks) {
            String name = (String) obj[0];
            long numBooks = (Long) obj[1];
            if (numBooks > 1) {
                System.out.println(name + " has written " + numBooks + " books.");
            } else {
                System.out.println(name + " has written " + numBooks + " book.");
            }

        }
        // Named Query - Hämta böcker efter genre
        List<Book> booksInGenre = em.createNamedQuery("Book.searchByGenre", Book.class)
                .setParameter("genre", "Hobby")
                .getResultList();
        booksInGenre.forEach(System.out::println);
    }

}
