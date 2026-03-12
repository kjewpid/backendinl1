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
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 25)
    private String genre;

    @Column(nullable = false)
    private int publicationYear;

    @ManyToMany
    private Set<Reader> readers = new HashSet<>();

    public Book() {

    }

    public Book(String title, String genre, int publicationYear) {
        this.title = title;
        this.genre = genre;
        this.publicationYear = publicationYear;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void addReader(Reader reader) {
        if (readers.add(reader)) {
            reader.addReadBook(this);
        }

    }

    @Override
    public String toString() {
        return String.format("""
                %-10s%s
                %-10s%s
                %-10s%d
                """, "Title", title, "Genre", genre, "Published", publicationYear);
    }

}
