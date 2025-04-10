package id.ac.ui.cs.advprog.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class DotenvConfig {

    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure()
                .ignoreIfMissing()
                .load();
    }
}
