package cat.uvic.level1cachedemo.service;

import cat.uvic.level1cachedemo.entity.Book;
import cat.uvic.level1cachedemo.repository.AuthorRepository;
import cat.uvic.level1cachedemo.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Transactional
    public Long countAllBookTitlesTransactional() {
        return countAllBookTitles();
    }


    public Long countAllBookTitlesNonTransactional() {
        return countAllBookTitles();
    }

    public Long countAllBookTitles() {
        return authorRepository.findAll().stream()
                .flatMap(author->author.getBooks().stream())
                .map(Book::getTitle).count();
    }
}
