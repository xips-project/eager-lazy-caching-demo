package cat.uvic.fetchtypecachedemo;

import cat.uvic.fetchtypecachedemo.service.BookService;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LevelTwoCacheTest {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    BookService bookService;

    // Clear books cache before every test
    @BeforeEach
    void setUp() {
        Objects.requireNonNull(cacheManager.getCache("books")).clear();
    }

    // Checks Caffeine is correctly setup as CacheManager
    @Test
    void cacheManagerClassShouldMatchCaffeineCacheManager() {
        Class<? extends CacheManager> clazz = cacheManager.getClass();
        assertEquals(clazz, CaffeineCacheManager.class);
    }

    @Test
    void checkIfBooksCacheIsPresent() {
        assertThat(cacheManager.getCacheNames().contains("books"));
    }

    // Result of findAllBooks is cached, so only first run goes to the db, resulting in same select counts before and
    // after both runs
    @Test
    void fetchingASecondTimeIsFasterUsingLevel2Cache() {

        SQLStatementCountValidator.reset();
        bookService.findAllBooks();
        SQLStatementCountValidator.assertSelectCount(9);

        bookService.findAllBooks();
        SQLStatementCountValidator.assertSelectCount(9);
    }


    // Clearing the cache before the second call to findAllBooks should result in double the select count statements
    @Test
    void fetchingASecondTimeShouldTimeoutIfCacheIsCleared() {


        SQLStatementCountValidator.reset();
        bookService.findAllBooks();
        SQLStatementCountValidator.assertSelectCount(9);

        Objects.requireNonNull(cacheManager.getCache("books")).clear();

        System.out.println(cacheManager.getCacheNames());

        bookService.findAllBooks();
        SQLStatementCountValidator.assertSelectCount(18);


    }

}
