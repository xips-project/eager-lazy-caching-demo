package cat.uvic.fetchtypecachedemo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@RequiredArgsConstructor
@Data
@EqualsAndHashCode
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Integer publicationYear;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;


    @ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<Author> authors;


}
