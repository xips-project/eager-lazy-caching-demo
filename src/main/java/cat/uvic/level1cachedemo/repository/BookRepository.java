package cat.uvic.level1cachedemo.repository;

import cat.uvic.level1cachedemo.entity.Book;
import org.springframework.data.repository.ListCrudRepository;

public interface BookRepository extends ListCrudRepository<Book, Long> {
}
