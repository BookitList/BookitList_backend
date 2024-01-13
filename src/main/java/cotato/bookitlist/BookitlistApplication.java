package cotato.bookitlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class BookitlistApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookitlistApplication.class, args);
    }

}
