package cat.uvic.fetchtypecachedemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LevelTwoCacheTest {

    @Autowired
    CacheManager cacheManager;

    @Test
    void cacheManagerClassShouldMatchCaffeineCacheManager(){
        Class<? extends CacheManager> clazz = cacheManager.getClass();
        assertEquals(clazz, CaffeineCacheManager.class);
    }
}
