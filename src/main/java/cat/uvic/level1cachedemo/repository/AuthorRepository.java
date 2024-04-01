package cat.uvic.level1cachedemo.repository;

import cat.uvic.level1cachedemo.entity.Author;
import org.springframework.data.repository.ListCrudRepository;

public interface AuthorRepository extends ListCrudRepository<Author, Long> {
}
