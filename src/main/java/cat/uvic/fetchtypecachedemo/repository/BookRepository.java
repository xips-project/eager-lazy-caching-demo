package cat.uvic.fetchtypecachedemo.repository;


import cat.uvic.fetchtypecachedemo.entity.Book;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;


public interface BookRepository extends ListCrudRepository<Book, Long> {

    @Cacheable("books")
    List<Book> findAll();


}
