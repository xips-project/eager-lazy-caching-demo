package cat.uvic.fetchtypecachedemo.repository;


import cat.uvic.fetchtypecachedemo.entity.Author;
import org.springframework.data.repository.ListCrudRepository;

public interface AuthorRepository extends ListCrudRepository<Author, Long> {
}
