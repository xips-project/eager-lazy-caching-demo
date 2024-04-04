package cat.uvic.fetchtypecachedemo.service;


import cat.uvic.fetchtypecachedemo.repository.AuthorRepository;
import cat.uvic.fetchtypecachedemo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private BookRepository bookRepository;


}
