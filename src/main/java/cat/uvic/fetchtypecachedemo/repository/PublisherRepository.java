package cat.uvic.fetchtypecachedemo.repository;

import cat.uvic.fetchtypecachedemo.entity.Publisher;
import org.springframework.data.repository.ListCrudRepository;

public interface PublisherRepository extends ListCrudRepository<Publisher, Long> {
}
