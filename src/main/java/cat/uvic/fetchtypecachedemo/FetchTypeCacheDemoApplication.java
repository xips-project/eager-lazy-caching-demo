package cat.uvic.fetchtypecachedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FetchTypeCacheDemoApplication {


    public static void main(String[] args) {
        SpringApplication.run(FetchTypeCacheDemoApplication.class, args);
    }

}
