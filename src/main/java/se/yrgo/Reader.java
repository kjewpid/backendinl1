package se.yrgo;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 150)
    private String email;

    @ManyToMany(mappedBy = "readers")
    private Set<Book> readBooks = new HashSet<>();

    public Reader() {

    }

    public Reader(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Set<Book> getReadBooks() {
        return readBooks;
    }

    public void addReadBook(Book book) {
        if (readBooks.add(book)) {
            book.addReader(this);
        }
    }

    public void addMultipleReadBooks(Set<Book> books) {
        for (Book book : books){
            addReadBook(book);
        }
    }

    @Override
    public String toString() {
        return String.format("""
                %-10s%s
                %-10s%s
                %s
                %s
                """, "Name", name, "Email", email, "Read Books", readBooks);
    }

}
