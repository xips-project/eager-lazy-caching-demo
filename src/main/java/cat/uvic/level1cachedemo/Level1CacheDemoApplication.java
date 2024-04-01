package cat.uvic.level1cachedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Level1CacheDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(Level1CacheDemoApplication.class, args);
    }

}
