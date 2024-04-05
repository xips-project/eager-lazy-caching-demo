package cat.uvic.level1cachedemo.service;

import cat.uvic.level1cachedemo.repository.AuthorRepository;
import cat.uvic.level1cachedemo.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private AuthorRepository authorRepository;
    private BookRepository bookRepository;
}
