package cat.uvic.fetchtypecachedemo.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@NamedEntityGraph(
        name = "publish-entity-graph-with-books-and-authors",
        attributeNodes = {
                @NamedAttributeNode("id"),
                @NamedAttributeNode("name"),
                @NamedAttributeNode(value = "books", subgraph = "books-subgraph"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "books-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("id"),
                                @NamedAttributeNode("title"),
                                @NamedAttributeNode("publicationYear"),
                                @NamedAttributeNode("publisher"),
                        }
                ),

        }
)

public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "publisher", fetch = FetchType.LAZY)
    List<Book> books;

    @Override
    public String toString() {
        return "Publisher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
