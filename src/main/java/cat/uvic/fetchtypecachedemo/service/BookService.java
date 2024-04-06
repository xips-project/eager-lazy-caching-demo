package cat.uvic.fetchtypecachedemo.service;


import cat.uvic.fetchtypecachedemo.entity.Book;
import cat.uvic.fetchtypecachedemo.repository.AuthorRepository;
import cat.uvic.fetchtypecachedemo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {


    private final BookRepository bookRepository;

    @Cacheable("books")
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }





}
