package se.yrgo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 30)
    private String nationality;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "author_fk")
    private List<Book> writtenBooks = new ArrayList<>();

    public Author() {

    }

    public Author(String name, String nationality) {
        this.name = name;
        this.nationality = nationality;
    }

    public String getName() {
        return name;
    }

    public String getNationality() {
        return nationality;
    }

    public List<Book> getWrittenBooks() {
        return writtenBooks;
    }

    public void addBook(Book book) {
        writtenBooks.add(book);
    }

    public void addMultipleBooks(Book[] books) {
        for (Book book : books) {
            if (!writtenBooks.contains(book)) {
                writtenBooks.add(book);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("""
                %-10s%s
                %-10s%s
                %-10s
                %s
                """, "Name", name, "Nationality", nationality, "Written books");
    }
}
