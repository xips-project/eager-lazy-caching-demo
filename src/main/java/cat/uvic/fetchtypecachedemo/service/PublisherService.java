package cat.uvic.fetchtypecachedemo.service;

import cat.uvic.fetchtypecachedemo.entity.Publisher;
import cat.uvic.fetchtypecachedemo.repository.PublisherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public void save(Publisher publisher){
        publisherRepository.save(publisher);
    }

    public Long count(){
        return publisherRepository.count();
    }

    public List<Publisher> findAll(){
        return publisherRepository.findAll();
    }

    @Transactional
    public List<Publisher> findAllTransactional(){
        List<Publisher> publishers = publisherRepository.findAll();
        for(var publisher : publishers){
            Hibernate.initialize(publisher.getBooks().size());
        }
        return publishers;
    }
}
