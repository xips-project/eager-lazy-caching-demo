package cat.uvic.fetchtypecachedemo;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableCaching
public class FetchTypeCacheDemoApplication {


    public static void main(String[] args) {
        SpringApplication.run(FetchTypeCacheDemoApplication.class, args);
    }

}
