package cat.uvic.fetchtypecachedemo.service;


import cat.uvic.fetchtypecachedemo.entity.Book;
import cat.uvic.fetchtypecachedemo.repository.AuthorRepository;
import cat.uvic.fetchtypecachedemo.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

}
